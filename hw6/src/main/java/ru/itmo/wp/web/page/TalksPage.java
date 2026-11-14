package ru.itmo.wp.web.page;

import ru.itmo.wp.model.exception.ValidationException;
import ru.itmo.wp.model.service.TalkService;
import ru.itmo.wp.model.service.RoleService;
import ru.itmo.wp.model.service.UserService;
import ru.itmo.wp.web.exception.RedirectException;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@SuppressWarnings("unused")
public class TalksPage extends Page {
    private final TalkService talkService = new TalkService();
    private final UserService userService = new UserService();

    @Override
    public void before(HttpServletRequest request, Map<String, Object> view) {
        super.before(request, view);
        requirePrivilege(RoleService.SEND_TALK);
    }

    @Override
    protected void action(HttpServletRequest request, Map<String, Object> view) {
        populate(view);
    }

    private void send(HttpServletRequest request, Map<String, Object> view) throws ValidationException {
        try {
            talkService.send(getUser().getId(), request.getParameter("targetUserId"), request.getParameter("text"));
        } catch (ValidationException e) {
            populate(view);
            throw e;
        }
        setMessage("Message sent");
        throw new RedirectException("/talks");
    }

    private void populate(Map<String, Object> view) {
        view.put("users", userService.findAll());
        view.put("talks", talkService.findByUserId(getUser().getId()));
    }
}
