package ru.netology.test;

import org.junit.jupiter.api.*;
import ru.netology.data.DataHelper;
import ru.netology.db.DbUtils;
import ru.netology.page.DashboardPage;
import ru.netology.page.LoginPage;
import ru.netology.page.VerificationPage;

import static com.codeborne.selenide.Selenide.open;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class LoginTest {

    @BeforeEach
    void setUp() {
        open(DataHelper.BASE_URL);
    }

    @AfterAll
    void tearDown() {
        DbUtils.cleanAllTestData();
    }

    @Test
    void shouldLoginWithCodeFromDatabase() {
        var user = DataHelper.validUser();

        LoginPage loginPage = new LoginPage();
        VerificationPage verificationPage = loginPage.login(user);

        String code = DbUtils.waitAuthCode(user.getLogin(), 10, 500);
        Assertions.assertNotNull(code, "Код не появился в БД");

        DashboardPage dashboardPage = verificationPage.verify(code);
        dashboardPage.shouldBeVisible();
    }

    @Test
    void shouldBlockAfterThreeWrongPasswords() {
        var valid = DataHelper.validUser();
        var wrong = DataHelper.userWithWrongPassword();

        for (int i = 0; i < 3; i++) {
            open(DataHelper.BASE_URL);
            LoginPage loginPage = new LoginPage();
            loginPage.loginWithInvalidPassword(wrong);
            loginPage.shouldShowErrorMessage("Ошибка!");
        }

        open(DataHelper.BASE_URL);
        LoginPage loginPage = new LoginPage();
        loginPage.login(valid);
        loginPage.shouldShowErrorMessage("Ошибка!");
    }
}