package ru.itmo.wp.web.page;

import com.google.common.base.Strings;
import ru.itmo.wp.model.domain.User;
import ru.itmo.wp.model.service.UserService;
import ru.itmo.wp.model.service.RoleService;
import ru.itmo.wp.web.exception.RedirectException;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public class Page {
    private final UserService userService = new UserService();
    private final RoleService roleService = new RoleService();
    private HttpServletRequest request;

    public void before(HttpServletRequest request, Map<String, Object> view) {
        this.request = request;

        User user = getUser();
        if (user != null) {
            view.put("user", user);
        }
        view.put("userCount", userService.findCount());

        String message = (String) request.getSession().getAttribute("message");
        if (!Strings.isNullOrEmpty(message)) {
            view.put("message", message);
            request.getSession().removeAttribute("message");
        }
    }

    public void after(HttpServletRequest request, Map<String, Object> view) {
    }

    protected void action(HttpServletRequest request, Map<String, Object> view) {
    }

    protected User getUser() {
        return (User) request.getSession().getAttribute("user");
    }

    protected void requireUser() {
        User user = getUser();
        if (user == null) {
            throw new RedirectException("/index");
        }
    }

    protected void requirePrivilege(String code) {
        requireUser();
        if (!roleService.hasPrivilege(getUser().getId(), code)) {
            throw new RedirectException("/index");
        }
    }

    protected void setUser(User user) {
        if (user == null) {
            request.getSession().removeAttribute("user");
        } else {
            request.getSession().setAttribute("user", user);
        }
    }

    protected void setMessage(String message) {
        request.getSession().setAttribute("message", message);
    }
}
