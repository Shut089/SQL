package ru.netology.test;

import com.codeborne.selenide.Condition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import ru.netology.db.DbUtils;

import static com.codeborne.selenide.Selenide.*;

public class LoginTest {

    @BeforeEach
    void setup() {
        DbUtils.cleanDatabase();
        open("http://localhost:9999");
    }

    @Test
    void shouldLoginWithCodeFromDatabase() {
        String login = "vasya";
        String password = "qwerty123";

        $("[data-test-id=login] input").setValue(login);
        $("[data-test-id=password] input").setValue(password);
        $("[data-test-id=action-login]").click();

        // если логин не прошёл — дальше идти бессмысленно
        if ($("[data-test-id=error-notification]").exists()
                && $("[data-test-id=error-notification]").isDisplayed()) {
            throw new AssertionError("Логин/пароль не приняты: " +
                    $("[data-test-id=error-notification]").getText());
        }

        // дождаться формы ввода кода
        $("[data-test-id=code] input").shouldBe(Condition.visible);

        // ждём, пока код появится в БД
        String code = null;
        for (int i = 0; i < 10; i++) { // до ~5 секунд
            code = DbUtils.getAuthCode(login);
            if (code != null) break;
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException(e);
            }
        }
        Assertions.assertNotNull(code, "Код не появился в БД");

        $("[data-test-id=code] input").setValue(code);
        $("[data-test-id=action-verify]").click();

        // проверяем, что код принят: ошибки нет и поле кода исчезло
        $("[data-test-id=error-notification]").shouldNotBe(Condition.visible);
        $("[data-test-id=code] input").shouldNotBe(Condition.visible);
    }

    @Test
    void shouldBlockAfterThreeWrongPasswords() {
        String login = "vasya";

        for (int i = 0; i < 3; i++) {
            open("http://localhost:9999");

            // ждём, что форма точно появилась
            $("[data-test-id=login] input").shouldBe(Condition.visible);

            // очищаем поля (важно!)
            $("[data-test-id=login] input").setValue("").setValue(login);
            $("[data-test-id=password] input").setValue("").setValue("wrong");
            $("[data-test-id=action-login]").click();

            $("[data-test-id=error-notification]").shouldBe(Condition.visible);
        }

        open("http://localhost:9999");
        $("[data-test-id=login] input").shouldBe(Condition.visible);

        $("[data-test-id=login] input").setValue("").setValue(login);
        $("[data-test-id=password] input").setValue("").setValue("qwerty123");
        $("[data-test-id=action-login]").click();

        // после блокировки мы НЕ должны попасть на страницу кода
        $("[data-test-id=code] input").shouldNotBe(Condition.visible);

        // и должна быть ошибка
        $("[data-test-id=error-notification]").shouldBe(Condition.visible);
    }
}
