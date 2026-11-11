package com.yash.grabpic.photo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PhotoRepository extends JpaRepository<Photo, Long> {

    // Retrieve all photos belonging to a specific event
    List<Photo> findByEventId(Long eventId);

    // Retrieve photos filtered by processing status (useful for background processing pipeline)
    List<Photo> findByEventIdAndStatus(Long eventId, PhotoStatus status);

    // Count how many photos have been uploaded for an event
    long countByEventId(Long eventId);
}
