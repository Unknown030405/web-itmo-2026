<#import "./common.ftlh" as c>
<@c.page>
    <#if selectedUser??>
        <#list users as u>
            <#if u.id == selectedUser.id>
                <#assign userPosts = c.postsByUser(posts, u.id)>
                <section class="profile">
                    <div class="profile-details">
                        <div class="profile-name"><@c.userlink u/></div>
                        <p>User ID: ${u.id?c}</p>
                        <p>Posts: ${userPosts?size?c}</p>
                        <p><a href="${basePath}/posts?user_id=${u.id?c}">View all posts</a></p>
                    </div>
                    <img class="avatar" src="${basePath}/img/avatar.png" alt="User avatar">
                </section>
                <div class="profile-arrows">
                    <#if u?index gt 0>
                        <a aria-label="Previous user" href="${basePath}/user?handle=${users[u?index - 1].handle?url}">←</a>
                    <#else>
                        <span class="disabled" aria-label="No previous user">←</span>
                    </#if>
                    <#if u?index + 1 lt users?size>
                        <a aria-label="Next user" href="${basePath}/user?handle=${users[u?index + 1].handle?url}">→</a>
                    <#else>
                        <span class="disabled" aria-label="No next user">→</span>
                    </#if>
                </div>
                <#break>
            </#if>
        </#list>
    <#else>
        <div class="not-found">
            <h1>User not found</h1>
            <p>There is no user with this handle or ID</p>
            <p><a href="${basePath}/users">Back to users</a></p>
        </div>
    </#if>
</@c.page>
