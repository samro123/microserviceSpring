package com.devteria.movie.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UploadImage {
    Cloudinary cloudinary;
    public String uploadImage(MultipartFile file) throws IOException{
            assert file.getOriginalFilename() != null;
            String publicValue = generatePublicValue(file.getOriginalFilename());
            log.info("publicValue is: {}", publicValue);
            String extension = getFileName(file.getOriginalFilename())[1];
            log.info("extension is: {}", extension);
            File fileUpload = convert(file);
            log.info("fileUpload is: {}", fileUpload);
            cloudinary.uploader().upload(fileUpload, ObjectUtils.asMap(
                    "resource_type", "video", "public_id",
                    publicValue));
            cleanDisk(fileUpload);
        log.info("url is: {}",  cloudinary.url().generate());
            return  cloudinary.url().resourceType("video").generate(StringUtils.join(publicValue, ".", extension));

    }

    private File convert(MultipartFile file){
        assert file.getOriginalFilename() != null;
        File convertFile = new File(StringUtils.join(generatePublicValue(
                file.getOriginalFilename()),
                getFileName(file.getOriginalFilename())[1]));
        try(InputStream is = file.getInputStream()){
            Files.copy(is, convertFile.toPath());
        }catch (Exception e){
            log.error(e.getMessage());
        }
        return convertFile;
    }

    private void cleanDisk(File file){
        try {
            log.info("file.toPath(): {}", file.toPath());
            Path filePath = file.toPath();
            Files.delete(filePath);
        }catch (Exception e){
            log.error("Error");
        }
    }
    public  String generatePublicValue(String originalName){
        String fileName = getFileName(originalName)[0];
        return StringUtils.join(UUID.randomUUID().toString(),"_", fileName);
    }

    public String[] getFileName(String originalName){
        return  originalName.split("\\.");
    }

}
