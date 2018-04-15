package com.example.authorization.controllers;

import com.example.authorization.Services.StorageService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class UploadController {
    @Autowired
    StorageService storageService;

    List<String> files = new ArrayList<String>();
    class PathObject{
        @Getter @Setter String path;
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/post")
    public ResponseEntity<String> handleFileUpload(@RequestPart("token") @Valid String token, @RequestParam("file") MultipartFile multipartFile) {
        String message = "";
        try {
            storageService.store(multipartFile, token);
            files.add(multipartFile.getOriginalFilename());

            message = "You successfully uploaded " + multipartFile.getOriginalFilename() + "!";
            return ResponseEntity.status(HttpStatus.OK).body(message);
        } catch (Exception e) {
            message = "FAIL to upload " + multipartFile.getOriginalFilename() + "!";
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(message);
        }
    }

    @GetMapping("/getallfiles")
    public @ResponseBody  PathObject getFile(@RequestHeader("token") String token) {
        PathObject path = new PathObject();
        path.setPath(storageService.givePathToAvatar(token));
        return path;
    }

    @GetMapping("/static/images/{filename:.+}")
    public ResponseEntity<InputStreamResource> getImage(@PathVariable  String filename) throws IOException{
        ClassPathResource imgFile = new ClassPathResource("/static/images/"+filename);
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(new InputStreamResource(imgFile.getInputStream()));
    }
}
