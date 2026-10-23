<#import "./common.ftlh" as c>
<@c.page>
<#if selecedUser ??>
    <#assign userPosts = c.postsByUser(posts, selectedUser.id)?sort_by("id")?reverse>
    <h1>Posts by <@c.userlink selectedUser/></h1>
    <#if userPosts?has_content>
        <#list userPosts as post><@c.post post/></#list>
    <#else>
        <p>This user has no posts yet.</p>
    </#if>
        <#else>
        <p>Unknown user</p>
        </#if>
</@c.page>
