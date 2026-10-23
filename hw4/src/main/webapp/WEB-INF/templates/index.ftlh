<#import "./common.ftlh" as c>
<@c.page>
    <h1>Recent posts</h1>
    <#list posts?reverse as post><@c.post post/></#list>
</@c.page>
