package com.learnSpringBoot.dreamShops.repository;

import com.learnSpringBoot.dreamShops.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {
}
