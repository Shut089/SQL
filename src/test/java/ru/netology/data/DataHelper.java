package ru.netology.data;

import lombok.Value;

public class DataHelper {
    private DataHelper() {}

    public static final String BASE_URL = "http://localhost:9999";

    public static AuthInfo validUser() {
        return new AuthInfo("vasya", "qwerty123");
    }

    public static AuthInfo userWithWrongPassword() {
        return new AuthInfo("vasya", "wrong");
    }

    @Value
    public static class AuthInfo {
        String login;
        String password;
    }
}