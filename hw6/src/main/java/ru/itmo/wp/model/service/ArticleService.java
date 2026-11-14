package ru.itmo.wp.model.service;

import ru.itmo.wp.model.domain.Article;
import ru.itmo.wp.model.exception.ValidationException;
import ru.itmo.wp.model.repository.ArticleRepository;
import ru.itmo.wp.model.repository.impl.ArticleRepositoryImpl;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

public class ArticleService {
    private final ArticleRepository articleRepository = new ArticleRepositoryImpl();

    public void create(long userId, String title, String text) throws ValidationException {
        if (title == null || title.trim().isEmpty()) {
            throw new ValidationException("Title is required");
        }
        title = title.trim();
        if (title.length() > 255) {
            throw new ValidationException("Title can't be longer than 255 characters");
        }
        if (text == null || text.trim().isEmpty()) {
            throw new ValidationException("Text is required");
        }
        if (text.length() > 10000) {
            throw new ValidationException("Text can't be longer than 10000 characters");
        }

        Article article = new Article();
        article.setUserId(userId);
        article.setTitle(title);
        article.setText(text);
        article.setCreationTime(Timestamp.from(Instant.now()));
        articleRepository.save(article);
    }

    public List<Article> findAllVisible() {
        return articleRepository.findAllVisible();
    }

    public List<Article> findByUserId(long userId) {
        return articleRepository.findByUserId(userId);
    }

    public boolean setHidden(long userId, String articleIdText, String hiddenText) throws ValidationException {
        long articleId;
        try {
            articleId = Long.parseLong(articleIdText);
        } catch (NumberFormatException e) {
            throw new ValidationException("Invalid article id");
        }
        if (!"true".equals(hiddenText) && !"false".equals(hiddenText)) {
            throw new ValidationException("Invalid hidden value");
        }
        Article article = articleRepository.find(articleId);
        if (article == null || article.getUserId() != userId) {
            throw new ValidationException("Article not found or access denied");
        }
        boolean hidden = Boolean.parseBoolean(hiddenText);
        articleRepository.setHidden(articleId, userId, hidden);
        return hidden;
    }
}
