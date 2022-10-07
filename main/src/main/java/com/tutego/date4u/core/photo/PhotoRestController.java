package com.tutego.date4u.core.photo;

import com.tutego.date4u.core.profile.ProfileRepository;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;


@RestController
public class PhotoRestController {

    private final PhotoService photoService;


    public PhotoRestController(PhotoService photoService) {
        this.photoService = photoService;
    }

    @GetMapping(path = "/api/photos/{imageName}",
            produces = MediaType.IMAGE_JPEG_VALUE)
    public byte[] photo(@PathVariable("imageName") String imageName) {

        return photoService.download(imageName).orElseThrow(PhotoNotFoundException::new);
    }





}