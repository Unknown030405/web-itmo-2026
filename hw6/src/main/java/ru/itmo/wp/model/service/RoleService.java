package ru.itmo.wp.model.service;

import ru.itmo.wp.model.domain.Privilege;
import ru.itmo.wp.model.domain.Role;
import ru.itmo.wp.model.exception.ValidationException;
import ru.itmo.wp.model.repository.RoleRepository;
import ru.itmo.wp.model.repository.UserRepository;
import ru.itmo.wp.model.repository.impl.RoleRepositoryImpl;
import ru.itmo.wp.model.repository.impl.UserRepositoryImpl;

import java.util.List;

public class RoleService {
    private static final String USER_ROLE = "USER";
    private static final String ADMIN_ROLE = "ADMIN";
    public static final String SEND_TALK = "SEND_TALK";
    public static final String CREATE_ARTICLE = "CREATE_ARTICLE";
    public static final String MANAGE_USERS = "MANAGE_USERS";

    private final RoleRepository roleRepository = new RoleRepositoryImpl();
    private final UserRepository userRepository = new UserRepositoryImpl();

    public boolean hasPrivilege(long userId, String code) {
        for (Role role : roleRepository.findByUserId(userId)) {
            for (Privilege privilege : role.getPrivileges()) {
                if (code.equals(privilege.getCode())) {
                    return true;
                }
            }
        }
        return false;
    }

    public List<Long> findAdminUserIds() {
        return roleRepository.findUserIdsByRole(ADMIN_ROLE);
    }

    public void assignDefaultRole(long userId) {
        roleRepository.assignRole(userId, USER_ROLE);
    }

    public boolean setAdmin(long actorUserId, String targetUserIdText, String enabledText)
            throws ValidationException {
        if (!hasPrivilege(actorUserId, MANAGE_USERS)) {
            throw new ValidationException("Administrator access required");
        }
        long targetUserId;
        try {
            targetUserId = Long.parseLong(targetUserIdText);
        } catch (NumberFormatException e) {
            throw new ValidationException("Invalid user id");
        }
        if (targetUserId <= 0 || userRepository.find(targetUserId) == null) {
            throw new ValidationException("User not found");
        }
        if (!"true".equals(enabledText) && !"false".equals(enabledText)) {
            throw new ValidationException("Invalid admin value");
        }

        boolean enabled = Boolean.parseBoolean(enabledText);
        if (enabled) {
            roleRepository.assignRole(targetUserId, ADMIN_ROLE);
        } else {
            roleRepository.removeRole(targetUserId, ADMIN_ROLE);
        }
        return enabled;
    }
}
