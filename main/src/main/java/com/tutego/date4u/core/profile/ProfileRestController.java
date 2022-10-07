package com.tutego.date4u.core.profile;


import com.tutego.date4u.core.photo.Photo;

import com.tutego.date4u.core.photo.PhotoService;
import com.tutego.date4u.core.photo.PhotoUploadException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.Optional;

@RestController
public class ProfileRestController {
    private final PhotoService photoService;

    private final ProfileRepository profileRepository;



    public ProfileRestController(PhotoService photoService, ProfileRepository profileRepository) {
        this.photoService = photoService;
        this.profileRepository = profileRepository;

    }

    @PostMapping("/api/profiles/{id}/photos/")
    public ResponseEntity<Photo> uploadImage(@PathVariable("id") long profileId, @RequestParam("file") MultipartFile file) {

        Optional<Profile> optionalProfile = profileRepository.findById(profileId);
        Profile profile = optionalProfile.orElseThrow(ProfileNotFoundException::new);

        byte[] fileBytes;
        try {
            fileBytes = file.getBytes();
        } catch (IOException e) {
            throw new PhotoUploadException(); //BAD Request
        }

        String uploadName = photoService.upload(fileBytes);

        Photo photo = new Photo(null, profile, uploadName, false, LocalDateTime.now());
       // photoRepository.save(photo); // could be replaced with cascade = CascadeType.ALL in Profile > photo
        profile.add(photo);
        profileRepository.save(profile);

        return ResponseEntity
                .created(URI.create("api/photos/" + uploadName)).build();
    }



}
