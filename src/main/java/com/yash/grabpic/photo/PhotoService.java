package com.yash.grabpic.photo;

import com.yash.grabpic.common.BadRequestException;
import com.yash.grabpic.event.Event;
import com.yash.grabpic.event.EventService;
import com.yash.grabpic.photo.dto.PhotoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PhotoService {

    private final PhotoRepository photoRepository;
    private final EventService eventService;

    @Value("${app.upload.dir:uploads}")
    private String baseUploadDir;

    @Transactional
    public List<PhotoResponse> uploadPhotos(Long eventId, List<MultipartFile> files) {
        if (files == null || files.isEmpty()) {
            throw new BadRequestException("No files provided for upload.");
        }

        // Verify event exists (throws 404 ResourceNotFoundException if missing)
        Event event = eventService.getEventEntity(eventId);

        // Path: uploads/{eventId}
        Path eventUploadPath = Paths.get(baseUploadDir, String.valueOf(eventId));
        try {
            Files.createDirectories(eventUploadPath);
        } catch (IOException e) {
            throw new RuntimeException("Could not create directory for event uploads: " + eventUploadPath, e);
        }

        List<Photo> photosToSave = new ArrayList<>();

        for (MultipartFile file : files) {
            if (file.isEmpty()) {
                continue;
            }

            // Extract file extension cleanly
            String originalFilename = StringUtils.cleanPath(file.getOriginalFilename() != null ? file.getOriginalFilename() : "");
            String extension = extractExtension(originalFilename);

            // Generate unique UUID filename to prevent collision and preserve raw photo
            String uniqueFilename = UUID.randomUUID() + extension;
            Path destinationFile = eventUploadPath.resolve(uniqueFilename).normalize().toAbsolutePath();

            // Save original file AS-IS to disk without compression or re-encoding
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                throw new RuntimeException("Failed to store file: " + originalFilename, e);
            }

            // Prepare Photo entity for DB
            Photo photo = Photo.builder()
                    .event(event)
                    .originalPath(destinationFile.toString())
                    .status(PhotoStatus.UPLOADED)
                    .build();

            photosToSave.add(photo);
        }

        if (photosToSave.isEmpty()) {
            throw new BadRequestException("All uploaded files were empty.");
        }

        List<Photo> savedPhotos = photoRepository.saveAll(photosToSave);

        return savedPhotos.stream()
                .map(this::mapToResponse)
                .toList();
    }

    private String extractExtension(String filename) {
        int dotIndex = filename.lastIndexOf('.');
        if (dotIndex >= 0) {
            return filename.substring(dotIndex).toLowerCase();
        }
        return ".jpg"; // default fallback extension
    }

    private PhotoResponse mapToResponse(Photo photo) {
        return PhotoResponse.builder()
                .id(photo.getId())
                .eventId(photo.getEvent().getId())
                .originalPath(photo.getOriginalPath())
                .status(photo.getStatus())
                .uploadedAt(photo.getUploadedAt())
                .build();
    }
}
