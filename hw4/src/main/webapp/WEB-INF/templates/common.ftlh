<#macro page>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Codeforces</title>
    <link rel="stylesheet" href="${basePath}/css/normalize.css">
    <link rel="stylesheet" href="${basePath}/css/style.css">
    <link rel="icon" type="image/png" href="${basePath}/favicon.ico?v=2">
</head>
<body>
<@header/>
<div class="middle">
    <@sidebar/>
    <main><#nested></main>
</div>
<@footer/>
</body>
</html>
</#macro>

<#macro header>
<header>
    <a href="${basePath}/index" class="logo"><img alt="Codeforces" title="Codeforces" src="${basePath}/img/codeforces-logo-with-telegram.png"></a>
    <div class="languages">
        <a href="#"><img alt="In English" title="In English" src="${basePath}/img/gb.png"></a>
        <a href="#"><img alt="In Russian" title="In Russian" src="${basePath}/img/ru.png"></a>
    </div>
    <div class="enter-or-register-box">
        <#if user??>
            <@userlink user=user nameOnly=true/> |
            <a href="${basePath}/logout">Logout</a>
        <#else>
            <a href="${basePath}/enter">Enter</a> |
            <a href="${basePath}/register">Register</a>
        </#if>
    </div>
    <nav>
        <#assign items=[
            {"href":"/index", "name":"Home", "paths":["/index", "/post", "/posts"]},
            {"href":"/users", "name":"Users", "paths":["/users", "/user"]},
            {"href":"/misc/help", "name":"Help", "paths":["/misc/help"]},
            {"href":"/contests", "name":"Contests", "paths":["/contests"]}
        ]>
        <ul>
            <#list items as item>
                <li><a href="${basePath}${item.href}"<#if item.paths?seq_contains(currentPath)> class="active" aria-current="page"</#if>>${item.name}</a></li>
            </#list>
        </ul>
    </nav>
</header>
</#macro>

<#function preview value>
    <#if value?length gt 350>
        <#return value?substring(0, 350) + "...">
    </#if>
    <#return value>
</#function>

<#function postsByUser allPosts userId>
    <#return allPosts?filter(p -> p.userId == userId)>
</#function>

<#macro post post full=false>
<article class="post">
    <h2 class="title"><#if full>${post.title}<#else><a href="${basePath}/post?post_id=${post.id?c}">${post.title}</a></#if></h2>
    <div class="information">By <@userlink post.author/></div>
    <div class="body"><p><#if full>${post.text}<#else>${preview(post.text)}</#if></p></div>
    <#if !full><div class="post-footer"><a href="${basePath}/post?post_id=${post.id?c}">Read more →</a></div></#if>
</article>
</#macro>

<#macro sidebarPost post>
<section>
    <div class="header">Post #${post.id?c}</div>
    <div class="body">${preview(post.text)}</div>
    <div class="footer"><a href="${basePath}/post?post_id=${post.id?c}">View all</a></div>
</section>
</#macro>

<#macro sidebar>
<aside>
    <#list posts?reverse as sidebarPostItem>
        <@sidebarPost sidebarPostItem/>
    </#list>
</aside>
</#macro>

<#macro footer>
<footer>2010-2021 &copy; by Mike Mirzayanov</footer>
</#macro>

<#macro userlink user nameOnly=false>
<a class="user-link<#if nameOnly> name-only</#if>" href="${basePath}/user?handle=${user.handle?url}">
    <span class="user-name">${user.name}</span><#if !nameOnly> <span class="user-handle color-${user.color?string?lower_case}">${user.handle}</span></#if>
</a>
</#macro>
