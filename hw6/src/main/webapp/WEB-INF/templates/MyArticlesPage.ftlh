<#-- @ftlvariable name="articles" type="ru.itmo.wp.model.domain.Article[]" -->
<#import "commons.ftlh" as c>

<@c.page>
    <div class="datatable">
        <div class="caption">My articles</div>
        <table id="my-articles">
            <thead>
            <tr><th>Id</th><th>Title</th><th>Visibility</th></tr>
            </thead>
            <tbody>
            <#list articles as article>
                <tr>
                    <td>${article.id}</td>
                    <td>${article.title}</td>
                    <td>
                        <button type="button" data-article-id="${article.id}" data-hidden="${article.hidden?c}">
                            <#if article.hidden>Show<#else>Hide</#if>
                        </button>
                    </td>
                </tr>
            <#else>
                <tr class="noData"><td colspan="3">No articles</td></tr>
            </#list>
            </tbody>
        </table>
    </div>
</@c.page>
