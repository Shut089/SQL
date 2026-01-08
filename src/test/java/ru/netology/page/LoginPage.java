package ru.netology.page;

import com.codeborne.selenide.Condition;
import ru.netology.data.DataHelper;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.$;

public class LoginPage {

    public LoginPage() {
        $("[data-test-id=login] input").shouldBe(Condition.visible);
    }

    public VerificationPage login(DataHelper.AuthInfo user) {
        submitCredentials(user);
        return new VerificationPage();
    }

    public LoginPage loginWithInvalidPassword(DataHelper.AuthInfo user) {
        submitCredentials(user);
        return this;
    }

    private void submitCredentials(DataHelper.AuthInfo user) {
        $("[data-test-id=login] input").clear();
        $("[data-test-id=password] input").clear();
        $("[data-test-id=login] input").setValue(user.getLogin());
        $("[data-test-id=password] input").setValue(user.getPassword());
        $("[data-test-id=action-login]").click();
    }


    // Делаем проверку по заголовку и по содержимому.
    public void shouldShowErrorMessage() {
        $("[data-test-id=error-notification]")
                .shouldBe(Condition.visible, Duration.ofSeconds(10))
                .shouldHave(Condition.text("Ошибка"))
                .shouldHave(Condition.text("Пользователь заблокирован"));
    }
    public void shouldShowBlockedMessage() {
        $("[data-test-id=error-notification]")
                .shouldBe(Condition.visible, Duration.ofSeconds(10))
                .shouldHave(Condition.text("Пользователь заблокирован"));
    }

}