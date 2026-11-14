package ru.itmo.wp.model.domain;

import java.util.ArrayList;
import java.util.List;

public class Role {
    private long id;
    private String name;
    private final List<Privilege> privileges = new ArrayList<>();

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Privilege> getPrivileges() {
        return privileges;
    }
}
