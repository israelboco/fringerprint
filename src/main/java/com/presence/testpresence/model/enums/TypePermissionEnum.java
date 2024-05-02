package com.presence.testpresence.model.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum TypePermissionEnum {

    PERMISSION(0, "en permission"),
    MISSION(1, "en mission");

    private int key;
    private String value;

    public String getValue() {
        return value;
    }

    public int getKey() {
        return key;
    }

}
