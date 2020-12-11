package com.jarvins.controller;

import com.jarvins.entity.response.ErrorResponse;
import com.jarvins.entity.Response;
import com.jarvins.entity.response.SuccessResponse;
import com.jarvins.service.FileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

import static com.jarvins.entity.response.ErrorEnum.*;

@Slf4j
@RestController
@RequestMapping("/file")
public class FileController {

    FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @GetMapping("/add")
    public Response addFolder(@RequestParam String prefix, @RequestParam String name) {
        if (fileService.addFolder(prefix, name)) {
            return SuccessResponse.success(true);
        }
        return ErrorResponse.error(ADD_ERROR);
    }

    @GetMapping("/delete")
    public Response deleteFile(@RequestParam String prefix, @RequestParam String name, @RequestParam boolean folder) {
        if (fileService.deleteFile(prefix, name, folder)) {
            return SuccessResponse.success(true);
        }
        return ErrorResponse.error(DElETE_ERROR);
    }

    @GetMapping("/open")
    public Response open(@RequestParam String prefix, @RequestParam String name) {
        return SuccessResponse.success(fileService.open(prefix, name));
    }

    @PostMapping("/upload")
    public Response upload(HttpServletRequest request) {
        MultiValueMap<String, MultipartFile> multiFileMap = ((MultipartRequest) request).getMultiFileMap();
        try {
            fileService.upload(multiFileMap);
            return SuccessResponse.success(true);
        } catch (IOException e) {
            return ErrorResponse.error(NET_ERROR);
        }
    }

    @GetMapping("/load")
    public Response loadFile(@RequestParam String prefix, @RequestParam String name) {
        return SuccessResponse.success(fileService.loadFile(prefix, name));
    }

    @GetMapping("/loadPic")
    public Response loadPic(@RequestParam String prefix, @RequestParam String name) {
        return SuccessResponse.success(fileService.loadAllPic(prefix, name));
    }
}

