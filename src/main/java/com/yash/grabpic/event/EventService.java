package com.yash.grabpic.event;

import com.yash.grabpic.common.BadRequestException;
import com.yash.grabpic.common.ResourceNotFoundException;
import com.yash.grabpic.event.dto.CreateEventRequest;
import com.yash.grabpic.event.dto.EventResponse;
import com.yash.grabpic.photo.PhotoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;
    private final PhotoRepository photoRepository;

    private static final String CODE_CHARS = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789";
    private static final SecureRandom RANDOM = new SecureRandom();

    @Transactional
    public EventResponse createEvent(CreateEventRequest request) {
        String accessCode;

        if (request.getAccessCode() != null && !request.getAccessCode().trim().isEmpty()) {
            accessCode = request.getAccessCode().trim().toUpperCase(Locale.ROOT);
            if (eventRepository.existsByAccessCode(accessCode)) {
                throw new BadRequestException("Access code '" + accessCode + "' is already taken. Please choose another.");
            }
        } else {
            accessCode = generateUniqueAccessCode();
        }

        Event event = Event.builder()
                .name(request.getName().trim())
                .accessCode(accessCode)
                .build();

        Event saved = eventRepository.save(event);
        return mapToResponse(saved, 0L);
    }

    @Transactional(readOnly = true)
    public EventResponse getEventById(Long id) {
        Event event = getEventEntity(id);
        long photoCount = photoRepository.countByEventId(id);
        return mapToResponse(event, photoCount);
    }

    // Helper method used across services to fetch entity or throw 404
    @Transactional(readOnly = true)
    public Event getEventEntity(Long id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found with id: " + id));
    }

    private String generateUniqueAccessCode() {
        String code;
        int attempts = 0;
        do {
            StringBuilder sb = new StringBuilder(6);
            for (int i = 0; i < 6; i++) {
                sb.append(CODE_CHARS.charAt(RANDOM.nextInt(CODE_CHARS.length())));
            }
            code = sb.toString();
            attempts++;
            if (attempts > 10) {
                // Failsafe in case of extremely high code density
                code = code + (System.currentTimeMillis() % 1000);
            }
        } while (eventRepository.existsByAccessCode(code));
        return code;
    }

    private EventResponse mapToResponse(Event event, long totalPhotos) {
        return EventResponse.builder()
                .id(event.getId())
                .name(event.getName())
                .accessCode(event.getAccessCode())
                .createdAt(event.getCreatedAt())
                .totalPhotos(totalPhotos)
                .build();
    }
}
