<#-- @ftlvariable name="users" type="ru.itmo.wp.model.domain.User[]" -->
<#import "commons.ftlh" as c>

<@c.page>
    <div id="users" class="users datatable">
        <div class="caption">User</div>
        <table>
            <thead>
            <tr>
                <th>Id</th>
                <th>Login</th>
                <th>Created</th>
                <#if canManageUsers><th>Admin</th></#if>
            </tr>
            </thead>
            <tbody>
            <#list users as u>
                <tr>
                    <td>${u.id}</td>
                    <td>${u.login}</td>
                    <td>${u.creationTime?datetime}</td>
                    <#if canManageUsers>
                        <#assign isAdmin = adminUserIds?seq_contains(u.id)>
                        <td>
                            <span class="admin-value">${isAdmin?c}</span>
                            <button type="button" data-user-id="${u.id}" data-admin="${isAdmin?c}">
                                <#if isAdmin>Disable<#else>Enable</#if>
                            </button>
                        </td>
                    </#if>
                </tr>
            <#else>
                <tr class="noData">
                    <td colspan="${canManageUsers?then(4, 3)}">No data</td>
                </tr>
            </#list>
            </tbody>
        </table>
    </div>
</@c.page>
