package ru.netology.page;

import com.codeborne.selenide.Condition;
import ru.netology.data.DataHelper;

import static com.codeborne.selenide.Selenide.$;

public class LoginPage {

    public LoginPage() {
        $("[data-test-id=login] input").shouldBe(Condition.visible);
    }

    public VerificationPage login(DataHelper.AuthInfo user) {
        $("[data-test-id=login] input").setValue(user.getLogin());
        $("[data-test-id=password] input").setValue(user.getPassword());
        $("[data-test-id=action-login]").click();
        return new VerificationPage();
    }

    public void shouldSeeError() {
        $("[data-test-id=error-notification]").shouldBe(Condition.visible);
    }
}