package ru.itmo.wp.model.repository.impl;

import ru.itmo.wp.model.database.DatabaseUtils;
import ru.itmo.wp.model.domain.Privilege;
import ru.itmo.wp.model.domain.Role;
import ru.itmo.wp.model.exception.RepositoryException;
import ru.itmo.wp.model.repository.RoleRepository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class RoleRepositoryImpl implements RoleRepository {
    private final DataSource dataSource = DatabaseUtils.getDataSource();

    @Override
    public List<Role> findByUserId(long userId) {
        String sql = "SELECT r.`id` AS `roleId`, r.`name` AS `roleName`, "
                + "p.`id` AS `privilegeId`, p.`code` AS `privilegeCode` "
                + "FROM `UserRole` ur JOIN `Role` r ON r.`id` = ur.`roleId` "
                + "LEFT JOIN `RolePrivilege` rp ON rp.`roleId` = r.`id` "
                + "LEFT JOIN `Privilege` p ON p.`id` = rp.`privilegeId` "
                + "WHERE ur.`userId` = ? ORDER BY r.`id`, p.`id`";
        Map<Long, Role> roles = new LinkedHashMap<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, userId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    long roleId = resultSet.getLong("roleId");
                    Role role = roles.get(roleId);
                    if (role == null) {
                        role = new Role();
                        role.setId(roleId);
                        role.setName(resultSet.getString("roleName"));
                        roles.put(roleId, role);
                    }
                    long privilegeId = resultSet.getLong("privilegeId");
                    if (!resultSet.wasNull()) {
                        Privilege privilege = new Privilege();
                        privilege.setId(privilegeId);
                        privilege.setCode(resultSet.getString("privilegeCode"));
                        role.getPrivileges().add(privilege);
                    }
                }
            }
        } catch (SQLException e) {
            throw new RepositoryException("Can't find user roles.", e);
        }
        return new ArrayList<>(roles.values());
    }

    @Override
    public List<Long> findUserIdsByRole(String roleName) {
        List<Long> userIds = new ArrayList<>();
        String sql = "SELECT ur.`userId` FROM `UserRole` ur "
                + "JOIN `Role` r ON r.`id` = ur.`roleId` WHERE r.`name` = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, roleName);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    userIds.add(resultSet.getLong(1));
                }
            }
        } catch (SQLException e) {
            throw new RepositoryException("Can't find users by role.", e);
        }
        return userIds;
    }

    @Override
    public void assignRole(long userId, String roleName) {
        try (Connection connection = dataSource.getConnection()) {
            long roleId;
            try (PreparedStatement statement = connection.prepareStatement(
                    "SELECT `id` FROM `Role` WHERE `name` = ?")) {
                statement.setString(1, roleName);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (!resultSet.next()) {
                        throw new RepositoryException("Role not found: " + roleName);
                    }
                    roleId = resultSet.getLong(1);
                }
            }
            try (PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO `UserRole` (`userId`, `roleId`) VALUES (?, ?) "
                            + "ON DUPLICATE KEY UPDATE `roleId` = VALUES(`roleId`)")) {
                statement.setLong(1, userId);
                statement.setLong(2, roleId);
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RepositoryException("Can't assign role.", e);
        }
    }

    @Override
    public void removeRole(long userId, String roleName) {
        String sql = "DELETE FROM `UserRole` WHERE `userId` = ? "
                + "AND `roleId` = (SELECT `id` FROM `Role` WHERE `name` = ?)";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, userId);
            statement.setString(2, roleName);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RepositoryException("Can't remove role.", e);
        }
    }
}
