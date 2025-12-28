package ru.netology.page;

import com.codeborne.selenide.Condition;

import static com.codeborne.selenide.Selenide.$;

public class VerificationPage {

    public VerificationPage() {
        // если логин/пароль неверные — сюда не попадём (упадёт по visible)
        $("[data-test-id=code] input").shouldBe(Condition.visible);
    }

    public void verifyWithCode(String code) {
        $("[data-test-id=code] input").setValue(code);
        $("[data-test-id=action-verify]").click();
    }

    public void shouldNotSeeError() {
        $("[data-test-id=error-notification]").shouldNotBe(Condition.visible);
    }

    /** Универсальная проверка успеха: со страницы кода ушли */
    public void shouldLeaveVerificationPage() {
        $("[data-test-id=code] input").shouldNotBe(Condition.visible);
    }

    public void shouldSeeError() {
        $("[data-test-id=error-notification]").shouldBe(Condition.visible);
    }
}