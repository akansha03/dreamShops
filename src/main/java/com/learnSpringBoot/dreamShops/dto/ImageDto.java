package com.learnSpringBoot.dreamShops.dto;

import lombok.Data;

@Data
public class ImageDto {
    private Long imageID;
    private String imageName;
    private String downloadURL;

    public Long getImageID() {
        return imageID;
    }

    public void setImageID(Long imageID) {
        this.imageID = imageID;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getDownloadURL() {
        return downloadURL;
    }

    public void setDownloadURL(String downloadURL) {
        this.downloadURL = downloadURL;
    }
}
