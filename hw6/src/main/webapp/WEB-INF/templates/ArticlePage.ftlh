<#import "commons.ftlh" as c>

<@c.page>
    <div class="article-form form-box">
        <div class="header">Create article</div>
        <div class="body">
            <form method="post" action="/article" data-ajax>
                <input type="hidden" name="action" value="create"/>
                <div class="field">
                    <div class="name"><label for="title">Title</label></div>
                    <div class="value"><input id="title" name="title" type="text"/></div>
                </div>
                <div class="field">
                    <div class="name"><label for="text">Text</label></div>
                    <div class="value"><textarea id="text" name="text" rows="8"></textarea></div>
                </div>
                <div class="error" role="alert" style="display: none"></div>
                <div class="button-field"><input type="submit" value="Create"></div>
            </form>
        </div>
    </div>
</@c.page>
