package ru.itmo.wp.model.service;

import ru.itmo.wp.model.domain.Talk;
import ru.itmo.wp.model.exception.ValidationException;
import ru.itmo.wp.model.repository.TalkRepository;
import ru.itmo.wp.model.repository.UserRepository;
import ru.itmo.wp.model.repository.impl.TalkRepositoryImpl;
import ru.itmo.wp.model.repository.impl.UserRepositoryImpl;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

public class TalkService {
    private final TalkRepository talkRepository = new TalkRepositoryImpl();
    private final UserRepository userRepository = new UserRepositoryImpl();

    public void send(long sourceUserId, String targetUserIdText, String text) throws ValidationException {
        long targetUserId;
        try {
            targetUserId = Long.parseLong(targetUserIdText);
        } catch (NumberFormatException e) {
            throw new ValidationException("Choose a recipient");
        }
        if (targetUserId <= 0 || userRepository.find(targetUserId) == null) {
            throw new ValidationException("Recipient not found");
        }
        if (text == null || text.trim().isEmpty()) {
            throw new ValidationException("Message text is required");
        }
        if (text.length() > 10000) {
            throw new ValidationException("Message can't be longer than 10000 characters");
        }

        Talk talk = new Talk();
        talk.setSourceUserId(sourceUserId);
        talk.setTargetUserId(targetUserId);
        talk.setText(text);
        talk.setCreationTime(Timestamp.from(Instant.now()));
        talkRepository.save(talk);
    }

    public List<Talk> findByUserId(long userId) {
        return talkRepository.findByUserId(userId);
    }
}
