package com.yash.grabpic.event.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventResponse {

    private Long id;
    private String name;
    private String accessCode;
    private LocalDateTime createdAt;
    private long totalPhotos;
}
