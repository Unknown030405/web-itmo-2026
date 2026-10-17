package ru.itmo.web.hw4.model;

public class Post {
    private final long id;
    private final String title;
    private final String text;
    private final long user_id;
    private final User author;

    public Post(long id, String title, String text, User author) {
        this.id = id;
        this.title = title;
        this.text = text;
        this.user_id = author.getId();
        this.author = author;
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }

    public long getUserId() {
        return user_id;
    }

    public User getAuthor() {
        return author;
    }
}
