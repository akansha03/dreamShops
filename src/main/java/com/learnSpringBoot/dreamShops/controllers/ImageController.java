package com.learnSpringBoot.dreamShops.controllers;

import com.learnSpringBoot.dreamShops.dto.ImageDto;
import com.learnSpringBoot.dreamShops.exceptions.ResourceNotFoundException;
import com.learnSpringBoot.dreamShops.model.Image;
import com.learnSpringBoot.dreamShops.response.APIResponse;
import com.learnSpringBoot.dreamShops.service.image.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.sql.SQLException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/images")
public class ImageController {
    @Autowired
    private ImageService imageService;

    @PostMapping("/upload")
    public ResponseEntity<APIResponse> saveImages(@RequestParam List<MultipartFile> files,
                                                  @RequestParam Long productID)
    {
        try {
            List<ImageDto> imageDtos = imageService.saveImage(files, productID);
            return ResponseEntity.ok(new APIResponse("Upload is successful", imageDtos));
        }
        catch(Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new APIResponse("Upload Failed!", e.getMessage()));
        }
    }

    @GetMapping("/image/download/{imageId}")
    public ResponseEntity<Resource> downloadImage(@PathVariable Long imageId) throws SQLException {
            Image image = imageService.getImageByID(imageId);
            ByteArrayResource resource = new ByteArrayResource(image.getImage().getBytes(1,
                    (int) image.getImage().length()));

            return ResponseEntity.ok().contentType(MediaType.parseMediaType(image.getFileType()))
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; fileName=\"" + image.getFileName() + "\"").body(resource);
    }

    @PutMapping ("/image/{imageID}/update")
    public ResponseEntity<APIResponse> updateImage(@PathVariable Long imageID, @RequestBody MultipartFile file) {
        try {
            Image image = imageService.getImageByID(imageID);
            if (image != null) {
                imageService.updateImage(file, imageID);
                return ResponseEntity.ok(new APIResponse("Image Update is successful!", null));
            }
        }
        catch(ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new APIResponse(e.getMessage(), null));
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new APIResponse("Update Failed", HttpStatus.INTERNAL_SERVER_ERROR));
    }

    @DeleteMapping ("/image/{imageID}/delete")
    public ResponseEntity<APIResponse> deleteImage(@PathVariable Long imageID) {
        try {
            Image image = imageService.getImageByID(imageID);
            if(image != null) {
            imageService.deleteImageById(imageID);
                return ResponseEntity.ok(new APIResponse("Image Delete is successful!", null));
            }
        }
        catch(ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new APIResponse(e.getMessage(), null));
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new APIResponse("Update Failed", HttpStatus.INTERNAL_SERVER_ERROR));
    }

}
