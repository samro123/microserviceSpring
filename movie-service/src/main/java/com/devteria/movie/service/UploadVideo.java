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
public class UploadVideo {
    Cloudinary cloudinary;
    public String uploadVideo(MultipartFile file) {
        String publicValue = null;
        String extension = null;
        File fileUpload = null;
        try {
            assert file.getOriginalFilename() != null;
            publicValue = generatePublicValue(file.getOriginalFilename());
            log.info("publicValue is: {}", publicValue);

            extension = getFileName(file.getOriginalFilename())[1];
            log.info("extension is: {}", extension);

            fileUpload = convert(file);
            log.info("fileUpload is: {}", fileUpload);

            cloudinary.uploader().upload(fileUpload, ObjectUtils.asMap(
                    "resource_type", "video",
                    "public_id", publicValue,
                    "eager_async", true
            ));

            log.info("url is: {}", cloudinary.url().generate());
            return cloudinary.url().resourceType("video").generate(StringUtils.join(publicValue, ".", extension));
        } catch (IOException e) {
            log.error("Error uploading video: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to upload video", e); // Or handle as needed
        } finally {
            if (fileUpload != null && fileUpload.exists()) {
                cleanDisk(fileUpload);
            }
        }
    }

    public String uploadImage(MultipartFile file) {
        try {
            assert file.getOriginalFilename() != null;
            String publicValue = generatePublicValue(file.getOriginalFilename());
            log.info("publicValue is: {}", publicValue);

            String extension = getFileName(file.getOriginalFilename())[1];
            log.info("extension is: {}", extension);

            File fileUpload = convert(file);
            log.info("fileUpload is: {}", fileUpload);

            cloudinary.uploader().upload(fileUpload, ObjectUtils.asMap("public_id", publicValue));
            cleanDisk(fileUpload);

            return cloudinary.url().resourceType("image").publicId(publicValue).format(extension).generate();

        } catch (IOException e) {
            log.error("IOException occurred while uploading image: {}", e.getMessage());
            e.printStackTrace(); // Or handle it according to your logging and error strategy
            return "Error: Unable to upload the file due to an IO exception";
        } catch (Exception e) {
            log.error("An error occurred while uploading image: {}", e.getMessage());
            e.printStackTrace(); // Or handle it according to your logging and error strategy
            return "Error: Unable to upload the image due to an unexpected error";
        }
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
