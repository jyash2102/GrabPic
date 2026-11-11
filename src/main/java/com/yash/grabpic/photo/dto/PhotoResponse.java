package com.yash.grabpic.photo.dto;

import com.yash.grabpic.photo.PhotoStatus;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PhotoResponse {

    private Long id;
    private Long eventId;
    private String originalPath;
    private PhotoStatus status;
    private LocalDateTime uploadedAt;
}
