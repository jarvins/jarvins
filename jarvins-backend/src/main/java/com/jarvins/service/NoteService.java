package com.jarvins.service;

import com.jarvins.entity.dto.FileSys;
import com.jarvins.entity.file.NoteInfo;
import com.jarvins.entity.param.NoteParam;
import com.jarvins.mapper.NoteMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class NoteService {

    @Resource
    NoteMapper noteMapper;

    private static final String NOTE_SUFFIX = ".md";
    private static final String NOTE_PICTURE_ROOT = "/jarvins/markdown/";
    private static final String NOTE_PIC_URL_PREFIX = "https://jarvins.com/mdCloud/";
    private static final String IMAGE = "image";

    public List<FileSys> open(String prefix, String name) {
        String path = prefix.equals("/") ? prefix + name : prefix + (name.equals("") ? "" : '/' + name);
        List<NoteInfo> childNote = noteMapper.selectChild(path);
        List<FileSys> child = childNote.stream()
                .map(e -> FileSys.builder().name(e.getName()).folder(e.isFolder()).build())
                .sorted((f1, f2) -> {
                    if (f1.isFolder() ^ f2.isFolder()) {
                        return f1.isFolder() ? -1 : 1;
                    } else {
                        return f1.getName().compareTo(f2.getName());
                    }
                }).collect(Collectors.toList());

        log.info("NoteService.open: open folder success,path:[{}]", path);
        return child;
    }

    public boolean add(String name, String prefix, boolean folder) {
        String pre = prefix.equals("/") ? prefix + name : prefix + '/' + name;
        String path = folder ? pre : pre + NOTE_SUFFIX;
        NoteInfo noteFileSys = NoteInfo.builder().name(name).path(path).parentPath(prefix).content("").folder(folder).build();
        if (noteMapper.insertFolder(noteFileSys) == 1) {
            log.info("NoteService.add: add success, path:[{}]", path);
            return true;
        } else {
            log.warn("NoteService.add: add failed, path:[{}]", path);
            return false;
        }
    }

    public boolean delete(String prefix, String name, boolean folder) {
        String _name = folder ? name : name + NOTE_SUFFIX;
        String path = prefix.equals("/") ? prefix + _name : prefix + '/' + _name;
        List<String> deletePathList = new ArrayList<>();
        deletePathList.add(path);
        //循环递归删除
        if (folder) {
            List<NoteInfo> child = noteMapper.selectChild(path);
            Deque<NoteInfo> deque = new ArrayDeque<>(child);
            while (!deque.isEmpty()) {
                NoteInfo pop = deque.pop();
                if (pop.isFolder()) {
                    deque.addAll(noteMapper.selectChild(pop.getPath()));
                }
                deletePathList.add(pop.getPath());
            }
        }

        int deletedSize = noteMapper.batchDeleteNote(deletePathList);
        if (deletedSize != deletePathList.size()) {
            log.warn("NoteService.deleteFile: delete db path:[{}],delete [{}] record, actual delete [{}].", path, deletePathList.size(), deletedSize);
            return false;
        } else {
            log.info("NoteService.deleteFile: delete db path:[{}],delete [{}] record.", path, deletePathList.size());
            return true;
        }
    }

    public boolean update(NoteParam param) {
        String prefix = param.getPrefix();
        String name = param.getName();
        String path = (prefix.equals("/") ? prefix + name : prefix + '/' + name) + NOTE_SUFFIX;
        if (noteMapper.updateNote(param.getContent(), path) == 1) {
            log.info("NoteService.update: update note sccess,path:[{}]",path);
            return true;
        }
        else{
            log.warn("NoteService.update: update note failed,path:[{}]",path);
            return false;
        }
    }

    public String load(String prefix, String name) {
        String path = (prefix.equals("/") ? prefix + name : prefix + '/' + name) + NOTE_SUFFIX;
        log.info("NoteService.load: load note success,path:[{}]",path);
        return noteMapper.selectNote(path).getContent();
    }

    public String upload(MultipartRequest request) throws IOException {
        MultipartFile file = request.getFile(IMAGE);
        String filename = file.getOriginalFilename();
        String path = NOTE_PICTURE_ROOT + filename;
        File pic = new File(path);
        if(!pic.exists()) {
            pic.createNewFile();
        }
        Path _path = Paths.get(path);
        Files.write(_path, file.getBytes());
        log.info("NoteService.upload: upload picture succcess, path:[{}]",path);
        return NOTE_PIC_URL_PREFIX + filename;
    }
}
