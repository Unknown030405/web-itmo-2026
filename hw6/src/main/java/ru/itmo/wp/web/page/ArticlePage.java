package ru.itmo.wp.web.page;

import ru.itmo.wp.model.exception.ValidationException;
import ru.itmo.wp.model.service.ArticleService;
import ru.itmo.wp.model.service.RoleService;
import ru.itmo.wp.web.annotation.Json;
import ru.itmo.wp.web.exception.RedirectException;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@SuppressWarnings("unused")
public class ArticlePage extends Page {
    private final ArticleService articleService = new ArticleService();

    @Override
    public void before(HttpServletRequest request, Map<String, Object> view) {
        super.before(request, view);
        requirePrivilege(RoleService.CREATE_ARTICLE);
    }

    @Json
    private void create(HttpServletRequest request, Map<String, Object> view) throws ValidationException {
        articleService.create(getUser().getId(), request.getParameter("title"), request.getParameter("text"));
        setMessage("Article created");
        throw new RedirectException("/index");
    }
}
