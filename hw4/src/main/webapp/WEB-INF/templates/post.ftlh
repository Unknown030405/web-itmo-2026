<#import "./common.ftlh" as c>
<@c.page>
    <#if selecedPost ??>
        <@c.post post=selectedPost full=true/>
    <#else>
            <div class="not-found">
                <h1>Post not found</h1>
                <p><a href="${basePath}/posts">Back to posts</a></p>
            </div>
    </#if>
</@c.page>
