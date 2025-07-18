package com.tugra.model;

import lombok.Getter;

@Getter
public enum Role {

    KULLANICI("KULLANICI");

    private String value;

    Role(String value) {
        this.value = value;
    }


}
