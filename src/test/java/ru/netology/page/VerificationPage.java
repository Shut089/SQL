package ru.netology.page;

import com.codeborne.selenide.Condition;

import static com.codeborne.selenide.Selenide.$;

public class VerificationPage {

    public VerificationPage() {
        $("[data-test-id=code] input").shouldBe(Condition.visible);
    }

    public DashboardPage verify(String code) {
        $("[data-test-id=code] input").setValue(code);
        $("[data-test-id=action-verify]").click();
        return new DashboardPage();
    }

    public void shouldShowErrorMessage(String expectedText) {
        $("[data-test-id=error-notification]")
                .shouldBe(Condition.visible)
                .shouldHave(Condition.text(expectedText));
    }
}