package com.yash.grabpic.event;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "events")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    // Unique 6-8 character code used by attendees to access their photos
    @Column(nullable = false, unique = true, length = 20)
    private String accessCode;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // Automatically set timestamp before inserting new record
    @PrePersist
    protected void onCreate() {
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
    }
}
