<#import "./common.ftlh" as c>
<@c.page>
    <#assign userPosts = c.postsByUser(posts, selectedUser.id)?reverse>
    <h1>Posts by <@c.userlink selectedUser/></h1>
    <#if userPosts?has_content>
        <#list userPosts as post><@c.post post/></#list>
    <#else>
        <p>This user has no posts yet.</p>
    </#if>
</@c.page>
