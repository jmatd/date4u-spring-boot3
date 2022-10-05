package com.tutego.date4u.core.photo;

import com.tutego.date4u.core.profile.Profile;
import com.tutego.date4u.core.profile.ProfileRepository;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.Optional;


@RestController
public class PhotoRestController {

    private final PhotoService photoService;

    private final ProfileRepository profileRepository;



    public PhotoRestController(PhotoService photoService, ProfileRepository profileRepository) {
        this.photoService = photoService;
        this.profileRepository = profileRepository;

    }

    @GetMapping(path = "/api/photos/{imageName}",
            produces = MediaType.IMAGE_JPEG_VALUE)
    public byte[] photo(@PathVariable("imageName") String imageName) {

        return photoService.download(imageName).orElseThrow(PhotoNotFoundException::new);
    }





}