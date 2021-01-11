package com.jarvins.service;

import com.jarvins.entity.dto.FileSys;
import com.jarvins.entity.file.FileInfo;
import com.jarvins.entity.file.FileType;
import com.jarvins.mapper.FileMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FileService {

    private static final String FILE_ROOT_PATH = "/jarvins/static/";
    private static final String STATIC_RESOURCE_PREFIX = "https://jarvins.com/icloud/";
    private static final BigDecimal _M_ = BigDecimal.valueOf(1024 * 1024);

    FileMapper fileMapper;

    public FileService(FileMapper fileMapper) {
        this.fileMapper = fileMapper;
    }

    /**
     * 实际上文件夹均为虚拟的,所有文件均存储在{@link FileService#FILE_ROOT_PATH}中
     * 这里只是虚拟的实现了目录结构
     *
     * @param prefix
     * @param name
     * @return
     */
    public boolean addFolder(String prefix, String name) {
        try {
            name = URLDecoder.decode(name, StandardCharsets.UTF_8.toString());
        }catch (UnsupportedEncodingException e){
            //ignore
        }
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        boolean rootFolder = prefix.equals("/");
        String path = rootFolder ? '/' + name : prefix + '/' + name;
        String parentPath = rootFolder ? "/" : prefix;
        FileInfo fileInfo = new FileInfo(name, uuid, "folder", true, BigDecimal.ZERO, path, path, parentPath);
        try {
            return fileMapper.insertFileInfo(fileInfo) == 1;
        } catch (DataIntegrityViolationException e) {
            return false;
        }
    }

    public boolean deleteFile(String prefix, String name, boolean folder) {
        String path = prefix.equals("/") ? '/' + name : prefix + '/' + name;
        List<String> deletePathList = new ArrayList<>();
        if (folder) {
            //删除当前文件
            deletePathList.add(path);
            List<FileInfo> childFile = fileMapper.selectChildFile(path);
            Deque<FileInfo> deque = new ArrayDeque<>(childFile);
            //循环删除文件下下的子文件
            while (!deque.isEmpty()) {
                FileInfo file = deque.pop();
                if (file.isFolder()) {
                    deque.addAll(fileMapper.selectChildFile(file.getPath()));
                }
                deletePathList.add(file.getPath());
            }
        } else {
            FileInfo file = fileMapper.selectFile(path);
            deletePathList.add(file.getPath());
        }

        //真实删除
        delete(fileMapper.selectFile(path).getFilePath());

        //数据库删除
        int deletedSize = fileMapper.batchDeleteFile(deletePathList);
        if (deletedSize != deletePathList.size()) {
            log.warn("FileService.deleteFile: delete db path:[{}],delete [{}] record, actual delete [{}].", path, deletePathList.size(), deletedSize);
            return false;
        } else {
            log.info("FileService.deleteFile: delete db path:[{}],delete [{}] record.", path, deletePathList.size());
            return true;
        }
    }

    public List<FileSys> open(String prefix, String name) {
        String path = prefix.equals("/") ? prefix + name : prefix + (name.equals("") ? "" : '/' + name);
        List<FileInfo> childFile = fileMapper.selectChildFile(path);
        List<FileSys> child = childFile.stream()
                .map(e -> FileSys.builder()
                        .name(e.getName())
                        .type(e.getType())
                        .folder(e.isFolder())
                        .size(e.getSize())
                        .create(e.getCreateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                        .build())
                .sorted((f1, f2) -> {
                    if (f1.isFolder() ^ f2.isFolder()) {
                        return f1.isFolder() ? -1 : 1;
                    } else {
                        return f1.getName().compareTo(f2.getName());
                    }
                }).collect(Collectors.toList());

        log.info("FileService.open: open folder success,path:[{}]", path);
        return child;
    }

    public void upload(MultiValueMap<String, MultipartFile> multiValueMap) throws IOException {
        for (Map.Entry<String, List<MultipartFile>> entry : multiValueMap.entrySet()) {
            long start = System.currentTimeMillis();
            MultipartFile multipartFile = entry.getValue().get(0);
            try {
                //维护虚拟文件结构
                String type = FileType.getType(Objects.requireNonNull(multipartFile.getOriginalFilename()));
                String name = multipartFile.getOriginalFilename();
                String realName = UUID.randomUUID().toString().replaceAll("-", "");
                String fileName = realName.concat(".").concat(type);
                String parentPath = entry.getKey();
                String path = parentPath.equals("/") ? parentPath + name : parentPath + '/' + name;
                String filePath = FILE_ROOT_PATH + fileName;
                BigDecimal size = new BigDecimal(multipartFile.getSize()).divide(_M_, 2, RoundingMode.HALF_UP);
                FileInfo file = new FileInfo(name, fileName, type, false, size, path, filePath, parentPath);
                //上传文件
                File _file = new File(filePath);
                if (!_file.exists()) {
                    _file.createNewFile();
                }
                multipartFile.transferTo(_file);
                fileMapper.insertFileInfo(file);
                long end = System.currentTimeMillis();
                log.info("FileService.uploadNote: upload file success, file:[{}], consume [{}] milliseconds.", name, end - start);
            } catch (IllegalArgumentException e) {
                //ignore
                log.warn("FileService.uploadNote: unsupported type,file:[{}]", multipartFile.getOriginalFilename());
            } catch (DataIntegrityViolationException e) {
                //ignore
                log.warn("FileService.uploadNote: [{}]", e.getMessage());
            }
        }
    }

    public String loadFile(String prefix, String name) {
        String path = prefix.equals("/") ? '/' + name : prefix + '/' + name;
        FileInfo fileInfo = fileMapper.selectFile(path);
        return STATIC_RESOURCE_PREFIX + fileInfo.getFileName();
    }

    public List<String> loadAllPic(String prefix, String name) {
        String path = prefix.equals("/") ? '/' + name : prefix + '/' + name;
        FileInfo fileInfo = fileMapper.selectFile(path);
        String parentPath = fileInfo.getParentPath();
        List<FileInfo> childFile = fileMapper.selectChildFile(parentPath);
        List<String> pic = Arrays.asList("png", "jpg", "jpeg", "gif");
        return childFile.stream().filter(e -> pic.contains(e.getType())).map(e -> STATIC_RESOURCE_PREFIX + e.getFileName()).collect(Collectors.toList());
    }

    private void delete(String path) {
        File file = new File(path);
        if (file.exists()) {
            if (file.isDirectory()) {
                File[] files = file.listFiles();
                for (File child : files) {
                    if (child.isFile()) {
                        child.delete();
                    } else {
                        delete(child.getAbsolutePath());
                    }
                }
            } else {
                file.delete();
            }
        }
    }
}
