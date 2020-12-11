package com.jarvins.controller;

import com.jarvins.entity.Response;
import com.jarvins.entity.dto.FileSys;
import com.jarvins.entity.param.NoteParam;
import com.jarvins.entity.response.ErrorResponse;
import com.jarvins.entity.response.SuccessResponse;
import com.jarvins.service.NoteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

import static com.jarvins.entity.response.ErrorEnum.*;

@Slf4j
@RestController
@RequestMapping("/note")
public class NoteController {

    NoteService noteService;

    public NoteController(NoteService noteService) {
        this.noteService = noteService;
    }

    @GetMapping("/add")
    public Response addNote(@RequestParam String name, @RequestParam String prefix, @RequestParam boolean folder) {
        if (noteService.add(name,prefix,folder)) {
            return SuccessResponse.success(true);
        }
        return ErrorResponse.error(ADD_ERROR);
    }

    @GetMapping("/open")
    public Response open(@RequestParam String prefix, @RequestParam String name){
        List<FileSys> open = noteService.open(prefix, name);
        return SuccessResponse.success(open);
    }

    @GetMapping("/load")
    public Response loadNote(@RequestParam String prefix, @RequestParam String name){
        String content = noteService.load(prefix, name);
        return SuccessResponse.success(content);
    }

    @GetMapping("/delete")
    public Response deleteNote(@RequestParam String prefix, @RequestParam String name, @RequestParam boolean folder) {
        if (noteService.delete(prefix,name,folder)) {
            return SuccessResponse.success(true);
        }
        return ErrorResponse.error(DElETE_ERROR);
    }

    @PostMapping("/update")
    public Response updateNote(@RequestBody NoteParam param) {
        if (noteService.update(param)) {
            return SuccessResponse.success(true);
        }
        return ErrorResponse.error(UPDATE_ERROR);
    }

    @PostMapping("/upload")
    public Response upload(HttpServletRequest request){
        MultipartRequest multipartRequest = (MultipartRequest) request;
        try {
            String url = noteService.upload(multipartRequest);
            return SuccessResponse.success(url);
        }catch (IOException e){
            return ErrorResponse.error(NET_ERROR);
        }
    }
}
