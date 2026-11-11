package com.yash.grabpic.event;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    // Find an event by its unique access code (used during attendee login/lookup)
    Optional<Event> findByAccessCode(String accessCode);

    // Check if an access code already exists to avoid collisions
    boolean existsByAccessCode(String accessCode);
}
