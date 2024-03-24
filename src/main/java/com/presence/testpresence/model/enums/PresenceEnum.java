package com.presence.testpresence.model.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum PresenceEnum {

    EN_RETARD(0, "en retard"),
    A_HEURE(1, "à l'heure"),
    ABSENT(2, "absent"),
    NON_DEFINIE(3, "non definie");

    private int key;
    private String value;

    public String getValue() {
        return value;
    }

    public int getKey() {
        return key;
    }

}
