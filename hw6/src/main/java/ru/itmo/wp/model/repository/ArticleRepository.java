package ru.itmo.wp.model.repository;

import ru.itmo.wp.model.domain.Article;

import java.util.List;

public interface ArticleRepository {
    void save(Article article);

    Article find(long id);

    List<Article> findAllVisible();

    List<Article> findByUserId(long userId);

    void setHidden(long articleId, long userId, boolean hidden);
}
