package ru.itmo.wp.web.page.inheritance;

import java.util.Map;

public abstract class InheritanceDemoParent implements InheritanceDemoActions {
    protected void action(Map<String, Object> view) {
        view.put("source", "method of the parent class");
    }
}
