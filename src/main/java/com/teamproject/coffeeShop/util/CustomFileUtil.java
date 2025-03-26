package com.teamproject.coffeeShop.util;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
@Log4j2
@RequiredArgsConstructor
public class CustomFileUtil {

  private String uploadPath;

  @PostConstruct
  public void init() {
    File tempFolder = new File(uploadPath);

    }

    uploadPath = tempFolder.getAbsolutePath();

    log.info("-------------------------------------");
    log.info(uploadPath);
  }

  public List<String> saveFiles(List<MultipartFile> files)throws RuntimeException{

    List<String> uploadNames = new ArrayList<>();

    for (MultipartFile multipartFile : files) {
      String savedName = UUID.randomUUID().toString() + "_" + multipartFile.getOriginalFilename();
      Path savePath = Paths.get(uploadPath, savedName);

      try {
        Files.copy(multipartFile.getInputStream(), savePath);

        String contentType = multipartFile.getContentType();
          Path thumbnailPath = Paths.get(uploadPath, "s_"+savedName);
          Thumbnails.of(savePath.toFile())
                  .toFile(thumbnailPath.toFile());
        }
        uploadNames.add(savedName);
      } catch (IOException e) {
        throw new RuntimeException(e.getMessage());
      }
    return uploadNames;
  }

  public ResponseEntity<Resource> getFile(String fileName) {
    


    
    }

    HttpHeaders headers = new HttpHeaders();
    try{
        return ResponseEntity.internalServerError().build();
    }
    return ResponseEntity.ok().headers(headers).body(resource);
  }

  public void deleteFiles(List<String> fileNames) {
      return;
    }
    fileNames.forEach(fileName -> {
      String thumbnailFileName = "s_" + fileName;
      Path thumbnailPath = Paths.get(uploadPath, thumbnailFileName);
      Path filePath = Paths.get(uploadPath, fileName);

      try {
        Files.deleteIfExists(filePath);
        Files.deleteIfExists(thumbnailPath);
        throw new RuntimeException(e.getMessage());
      }
    });
  }
}