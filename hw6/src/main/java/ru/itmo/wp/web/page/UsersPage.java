package ru.itmo.wp.web.page;

import ru.itmo.wp.model.domain.User;
import ru.itmo.wp.model.exception.ValidationException;
import ru.itmo.wp.model.service.RoleService;
import ru.itmo.wp.model.service.UserService;
import ru.itmo.wp.web.annotation.Json;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@SuppressWarnings({"unused"})
public class UsersPage extends Page {
    private final UserService userService = new UserService();
    private final RoleService roleService = new RoleService();

    @Override
    protected void action(HttpServletRequest request, Map<String, Object> view) {
        view.put("users", userService.findAll());
        User currentUser = getUser();
        boolean canManageUsers = currentUser != null
                && roleService.hasPrivilege(currentUser.getId(), RoleService.MANAGE_USERS);
        view.put("canManageUsers", canManageUsers);
        if (canManageUsers) {
            view.put("adminUserIds", roleService.findAdminUserIds());
        }
    }

    @Json
    private void setAdmin(HttpServletRequest request, Map<String, Object> view) throws ValidationException {
        User currentUser = getUser();
        long actorUserId = currentUser == null ? 0 : currentUser.getId();
        boolean admin = roleService.setAdmin(
                actorUserId, request.getParameter("id"), request.getParameter("admin"));
        view.put("admin", admin);
    }
}
