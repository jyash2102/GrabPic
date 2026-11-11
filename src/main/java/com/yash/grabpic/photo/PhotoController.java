package com.yash.grabpic.photo;

import com.yash.grabpic.photo.dto.PhotoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/events/{id}/photos")
@RequiredArgsConstructor
public class PhotoController {

    private final PhotoService photoService;

    // POST /events/{id}/photos - Upload multiple original photos for an event
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<List<PhotoResponse>> uploadPhotos(
            @PathVariable Long id,
            @RequestParam("files") List<MultipartFile> files) {
        List<PhotoResponse> responses = photoService.uploadPhotos(id, files);
        return ResponseEntity.status(HttpStatus.CREATED).body(responses);
    }
}
