package ru.netology.data;

public class DataHelper {
    private DataHelper() {
    }

    public static final String BASE_URL = "http://localhost:9999";

    public static AuthInfo validUser() {
        return new AuthInfo("vasya", "qwerty123"); // если пароль другой — замени здесь
    }

    public static AuthInfo userWithWrongPassword() {
        return new AuthInfo("vasya", "wrong");
    }


    public static class AuthInfo {
        private final String login;
        private final String password;

        public AuthInfo(String login, String password) {
            this.login = login;
            this.password = password;
        }

        public String getLogin() {
            return login;
        }

        public String getPassword() {
            return password;
        }
    }
}