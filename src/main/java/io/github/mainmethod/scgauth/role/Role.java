package io.github.mainmethod.scgauth.role;

public enum Role {
    USER("USER"),
    ADMIN("ADMIN");

    Role(String value) {
        this.value = value;
    }

    private String value;

    public String getValue() {
        return value;
    }

}
