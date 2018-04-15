package com.example.authorization;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

public class UserFile {
    @Getter @Setter private MultipartFile file;
    @Getter @Setter private String token;
}
