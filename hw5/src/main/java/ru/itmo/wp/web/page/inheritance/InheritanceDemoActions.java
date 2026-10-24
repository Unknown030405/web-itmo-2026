package ru.itmo.wp.web.page.inheritance;

import java.util.Map;

public interface InheritanceDemoActions {
    default void fromInterface(Map<String, Object> view) {
        view.put("source", "default method of the interface");
    }
}
