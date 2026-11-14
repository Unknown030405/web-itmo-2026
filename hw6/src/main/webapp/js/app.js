// Всплывающее уведомление в правом нижнем углу.
window.notify = function (message, isError) {
    $.notify(message, {
        position: "right bottom",
        className: isError ? "error" : "success"
    });
};

window.ajax = function (options) {
    return $.ajax($.extend({
        type: "POST",
        dataType: "json"
    }, options)).done(function (response) {
        if (response.redirect) {
            location.href = response.redirect;
        }
    });
};

$(function () {
    const message = $("body").attr("data-message");
    if (message) {
        notify(message);
    }

    $("form[data-ajax]").on("submit", function (event) {
        event.preventDefault();

        const form = $(this);
        const error = form.find(".error");

        ajax({url: form.attr("action"), data: form.serialize()})
            .done(function (response) {
                if (response.error) {
                    error.text(response.error).show();
                }
            })
            .fail(function () {
                error.text("Request failed. Please try again.").show();
            });
    });

    const articles = $("#articles");
    const articleTemplate = document.getElementById("article-template");
    if (articles.length && articleTemplate) {
        ajax({url: "/index", type: "GET", data: {action: "articles"}})
            .done(function (response) {
                articles.empty();
                if (!response.articles.length) {
                    articles.text("No articles yet");
                    return;
                }

                response.articles.forEach(function (article) {
                    const item = articleTemplate.content.cloneNode(true);
                    $(item.querySelector(".article-title")).text(article.title);
                    $(item.querySelector(".article-author")).text(article.authorLogin);
                    $(item.querySelector(".article-time")).text(article.creationTime);
                    $(item.querySelector(".article-text")).text(article.text);
                    articles.append(item);
                });
            })
            .fail(function () {
                articles.text("Could not load articles.");
            });
    }

    $("#my-articles").on("click", "button[data-article-id]", function () {
        const button = $(this);
        const nextHidden = button.attr("data-hidden") === "true" ? "false" : "true";
        button.prop("disabled", true);

        ajax({
            url: "/myArticles",
            data: {
                action: "setHidden",
                id: button.attr("data-article-id"),
                hidden: nextHidden
            }
        }).done(function (response) {
            if (response.error) {
                notify(response.error, true);
                return;
            }
            button.attr("data-hidden", String(response.hidden));
            button.text(response.hidden ? "Show" : "Hide");
        }).fail(function () {
            notify("Could not update the article.", true);
        }).always(function () {
            button.prop("disabled", false);
        });
    });

    $("#users").on("click", "button[data-user-id]", function () {
        const button = $(this);
        const nextAdmin = button.attr("data-admin") === "true" ? "false" : "true";
        button.prop("disabled", true);

        ajax({
            url: "/users",
            data: {
                action: "setAdmin",
                id: button.attr("data-user-id"),
                admin: nextAdmin
            }
        }).done(function (response) {
            if (response.error) {
                notify(response.error, true);
                return;
            }
            button.attr("data-admin", String(response.admin));
            button.text(response.admin ? "Disable" : "Enable");
            button.closest("td").find(".admin-value").text(String(response.admin));
        }).fail(function () {
            notify("Could not update administrator role.", true);
        }).always(function () {
            button.prop("disabled", false);
        });
    });
});
