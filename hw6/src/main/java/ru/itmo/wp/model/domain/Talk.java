package ru.itmo.wp.model.domain;

import java.sql.Timestamp;

public class Talk {
    private long id;
    private long sourceUserId;
    private long targetUserId;
    private String sourceLogin;
    private String targetLogin;
    private String text;
    private Timestamp creationTime;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getSourceUserId() {
        return sourceUserId;
    }

    public void setSourceUserId(long sourceUserId) {
        this.sourceUserId = sourceUserId;
    }

    public long getTargetUserId() {
        return targetUserId;
    }

    public void setTargetUserId(long targetUserId) {
        this.targetUserId = targetUserId;
    }

    public String getSourceLogin() {
        return sourceLogin;
    }

    public void setSourceLogin(String sourceLogin) {
        this.sourceLogin = sourceLogin;
    }

    public String getTargetLogin() {
        return targetLogin;
    }

    public void setTargetLogin(String targetLogin) {
        this.targetLogin = targetLogin;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Timestamp getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Timestamp creationTime) {
        this.creationTime = creationTime;
    }
}
