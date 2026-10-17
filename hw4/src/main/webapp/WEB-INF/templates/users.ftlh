<#import "./common.ftlh" as c>
<@c.page>
    <div class="datatable">
        <div class="caption">Users</div>
        <table>
            <thead><tr><th>Id</th><th>User</th><th>Handle</th></tr></thead>
            <tbody>
                <#list users as u>
                    <tr><td>${u.id?c}</td><td><@c.userlink u/></td><td>${u.handle}</td></tr>
                </#list>
            </tbody>
        </table>
    </div>
</@c.page>
