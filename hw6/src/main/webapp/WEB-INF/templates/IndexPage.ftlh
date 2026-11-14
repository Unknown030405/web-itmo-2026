<#import "commons.ftlh" as c>

<@c.page>
    <div id="articles">Loading articles...</div>
    <template id="article-template">
        <article class="article">
            <h2 class="article-title"></h2>
            <div class="article-meta">By <span class="article-author"></span> · <time class="article-time"></time></div>
            <p class="article-text"></p>
        </article>
    </template>
</@c.page>
