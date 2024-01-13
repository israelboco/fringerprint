package com.presence.testpresence.model.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum UserTypeEnum {
    BO("Backoffice");

    private String type;

    public String getType() {
        return type;
    }
}
