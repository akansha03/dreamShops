package com.learnSpringBoot.dreamShops.service.image;

import com.learnSpringBoot.dreamShops.dto.ImageDto;
import com.learnSpringBoot.dreamShops.exceptions.ResourceNotFoundException;
import com.learnSpringBoot.dreamShops.model.Image;
import com.learnSpringBoot.dreamShops.model.Product;
import com.learnSpringBoot.dreamShops.repository.ImageRepository;
import com.learnSpringBoot.dreamShops.repository.ProductRepository;
import com.learnSpringBoot.dreamShops.service.product.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ImageService implements IImageService{
    @Autowired
    private ImageRepository imageRepository;
    @Autowired
    private ProductService productService;
    @Override
    public Image getImageByID(Long Id) {
        return imageRepository.findById(Id)
                .orElseThrow(() -> new ResourceNotFoundException("Image Not Found"));
    }

    @Override
    public void deleteImageById(Long Id) {
        imageRepository.findById(Id)
                .ifPresentOrElse(imageRepository :: delete, () -> {
                    throw new ResourceNotFoundException("No Image was found with Id " + Id);
                });
    }

    @Override
    public List<ImageDto> saveImage(List<MultipartFile> files, Long productID) {
        Product product = productService.getProductById(productID);
        List<ImageDto> savedImageDto = new ArrayList<>();

        for(MultipartFile file : files) {
            try {
                Image image = new Image();
                image.setFileName(file.getOriginalFilename());
                image.setFileType(file.getContentType());

                image.setImage(new SerialBlob(file.getBytes()));
                image.setProduct(product);
                String buildDownloadURL = "api/v1/image/download/";
                String downloadURL =  buildDownloadURL+ image.getId();

                image.setDownloadURL(downloadURL);
                Image savedImage = imageRepository.save(image);

                savedImage.setDownloadURL( buildDownloadURL + savedImage.getId());
                imageRepository.save(savedImage);

                ImageDto imageDto = new ImageDto();
                imageDto.setImageID(savedImage.getId());
                imageDto.setImageName(savedImage.getFileName());
                imageDto.setDownloadURL(savedImage.getDownloadURL());
                savedImageDto.add(imageDto);
            }
            catch (IOException | SQLException e) {
                throw new RuntimeException(e.getMessage());
            }
        }
        return savedImageDto;
    }

    @Override
    public void updateImage(MultipartFile file, Long imageID) {
        Image image = getImageByID(imageID);
        try
        {
            image.setFileName(file.getOriginalFilename());
            image.setImage(new SerialBlob(file.getBytes()));
            imageRepository.save(image);
        }
        catch (IOException | SQLException e){
            throw new RuntimeException(e.getMessage());
        }
    }
}
