package ru.itmo.wp.model.service;

import ru.itmo.wp.model.domain.Event;
import ru.itmo.wp.model.repository.EventRepository;
import ru.itmo.wp.model.repository.impl.EventRepositoryImpl;

import java.sql.Timestamp;
import java.time.Instant;

public class EventService {
    private final EventRepository eventRepository = new EventRepositoryImpl();

    public void recordEnter(long userId) {
        record(userId, Event.Type.ENTER);
    }

    public void recordLogout(long userId) {
        record(userId, Event.Type.LOGOUT);
    }

    private void record(long userId, Event.Type type) {
        Event event = new Event();
        event.setUserId(userId);
        event.setType(type);
        event.setCreationTime(Timestamp.from(Instant.now()));
        eventRepository.save(event);
    }
}
