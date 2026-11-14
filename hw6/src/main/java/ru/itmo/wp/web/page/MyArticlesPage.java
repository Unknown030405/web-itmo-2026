package ru.itmo.wp.web.page;

import ru.itmo.wp.model.exception.ValidationException;
import ru.itmo.wp.model.service.ArticleService;
import ru.itmo.wp.web.annotation.Json;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@SuppressWarnings("unused")
public class MyArticlesPage extends Page {
    private final ArticleService articleService = new ArticleService();

    @Override
    public void before(HttpServletRequest request, Map<String, Object> view) {
        super.before(request, view);
        requireUser();
    }

    @Override
    protected void action(HttpServletRequest request, Map<String, Object> view) {
        view.put("articles", articleService.findByUserId(getUser().getId()));
    }

    @Json
    private void setHidden(HttpServletRequest request, Map<String, Object> view) throws ValidationException {
        boolean hidden = articleService.setHidden(
                getUser().getId(), request.getParameter("id"), request.getParameter("hidden"));
        view.put("hidden", hidden);
    }
}
