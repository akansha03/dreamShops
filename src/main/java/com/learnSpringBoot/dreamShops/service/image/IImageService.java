package com.learnSpringBoot.dreamShops.service.image;

import com.learnSpringBoot.dreamShops.dto.ImageDto;
import com.learnSpringBoot.dreamShops.model.Image;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IImageService {
    Image getImageByID(Long Id);
    void deleteImageById(Long Id);
    List<ImageDto> saveImage(List<MultipartFile> file, Long productID);
    void updateImage(MultipartFile file, Long imageID);
}
