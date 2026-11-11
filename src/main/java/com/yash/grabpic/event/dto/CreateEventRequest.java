package com.yash.grabpic.event.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateEventRequest {

    @NotBlank(message = "Event name is required")
    @Size(min = 3, max = 100, message = "Event name must be between 3 and 100 characters")
    private String name;

    // Optional: Organizer can supply custom code or leave empty for auto-generated code
    @Size(min = 4, max = 20, message = "Access code must be between 4 and 20 characters")
    private String accessCode;
}
