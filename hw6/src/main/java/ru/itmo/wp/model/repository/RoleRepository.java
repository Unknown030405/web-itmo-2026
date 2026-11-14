package ru.itmo.wp.model.repository;

import ru.itmo.wp.model.domain.Role;

import java.util.List;

public interface RoleRepository {
    List<Role> findByUserId(long userId);

    List<Long> findUserIdsByRole(String roleName);

    void assignRole(long userId, String roleName);

    void removeRole(long userId, String roleName);
}
