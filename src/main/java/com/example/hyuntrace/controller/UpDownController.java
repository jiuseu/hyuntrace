package com.example.hyuntrace.controller;

import com.example.hyuntrace.dto.UploadFileDTO;
import com.example.hyuntrace.dto.UploadResultDTO;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.extern.log4j.Log4j2;
import net.coobird.thumbnailator.Thumbnailator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@RestController
@RequestMapping("/api/upload")
@Log4j2
public class UpDownController {

    @Value("${com.example.upload.path}")
    private String uploadPath;

    @PostMapping(value = "/post", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public List<UploadResultDTO> uploadPost(@RequestBody UploadFileDTO uploadFileDTO){

        log.info("============================ Rest Api Upload Post ============================");
        log.info(uploadFileDTO);

        List<UploadResultDTO> list = new ArrayList<>();

        if(uploadFileDTO.getFiles() != null){
            uploadFileDTO.getFiles().forEach(multipartFile -> {

                String originalFileName = multipartFile.getOriginalFilename();
                String uuid = UUID.randomUUID().toString();
                Path savePath = Paths.get(uploadPath,uuid+"_"+originalFileName);
                boolean img = false;
                try{
                    multipartFile.transferTo(savePath);

                    if(Files.probeContentType(savePath).startsWith("image")){
                        img = true;
                        File thumbnailFile = new File(uploadPath,"s_"+uuid+"_"+originalFileName);
                        Thumbnailator.createThumbnail(savePath.toFile(),thumbnailFile,200,200);
                    }
                }catch (Exception e){
                    log.info("============================ Rest Upload File Error ============================");
                    e.getMessage();
                }
                list.add(UploadResultDTO.builder()
                        .uuid(uuid)
                        .fileName(originalFileName)
                        .img(img)
                        .build());
            });
            return list;
        }
        return null;
    }

    @GetMapping("/view/{fileName}")
    public ResponseEntity<Resource> viewFile(@PathVariable String fileName){

        log.info("============================ Rest Upload File View ============================");

        Resource resource = new FileSystemResource(uploadPath+File.separator+fileName);
        String resourcePath = resource.getFilename();
        HttpHeaders headers = new HttpHeaders();

        try{
           headers.add("Content-Type", Files.probeContentType(resource.getFile().toPath()));
        }catch (IOException e){
            ResponseEntity.internalServerError().build();
        }
        return ResponseEntity.ok().headers(headers).body(resource);
    }

    @DeleteMapping("/remove/{fileName}")
    public Map<String,Boolean> removeFile(@PathVariable String fileName){

        log.info("============================ Rest Upload File Remove ============================");

        Resource resource = new FileSystemResource(uploadPath+File.separator+fileName);
        String resourcePah = resource.getFilename();
        Map<String,Boolean> resultMap = new HashMap<>();
        boolean removed = false;

        try{
            String contentType = Files.probeContentType(resource.getFile().toPath());
            removed = resource.getFile().delete();

            if(contentType.startsWith("image")){
                File thumbnailFile = new File(uploadPath+File.separator+"s_"+fileName);
                thumbnailFile.delete();
            }
        }catch (IOException e){
            log.info("============================ Rest Remove IOException ============================");
            e.printStackTrace();;
        }
        resultMap.put("result",removed);

        return resultMap;
    }
}
