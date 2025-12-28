package ru.netology.test;

import org.junit.jupiter.api.*;
import ru.netology.data.DataHelper;
import ru.netology.db.DbUtils;
import ru.netology.page.LoginPage;
import ru.netology.page.VerificationPage;

import static com.codeborne.selenide.Selenide.open;

public class LoginTest {

    @BeforeEach
    void setUp() {
        DbUtils.cleanAuthCodes();
        open(DataHelper.BASE_URL);
    }

    @Test
    void shouldLoginWithCodeFromDatabase() {
        var user = DataHelper.validUser();

        LoginPage loginPage = new LoginPage();
        VerificationPage verificationPage = loginPage.login(user);

        String code = DbUtils.waitLatestAuthCodeByLogin(user.getLogin(), 10, 500);
        Assertions.assertNotNull(code, "Код не появился в БД");

        verificationPage.verifyWithCode(code);
        verificationPage.shouldNotSeeError();
        verificationPage.shouldLeaveVerificationPage();
    }

    @Test
    void shouldBlockAfterThreeWrongPasswords() {
        // 3 неверных попытки → затем правильный пароль всё равно не должен пускать.
        var valid = DataHelper.validUser();
        var wrong = DataHelper.userWithWrongPassword();

        LoginPage loginPage = new LoginPage();

        for (int i = 0; i < 3; i++) {
            loginPage.login(wrong);          // попытка логина с неверным паролем
            loginPage.shouldSeeError();
            open(DataHelper.BASE_URL);
            loginPage = new LoginPage();
        }

        // Попытка с верным паролем после 3 ошибок — ожидаем блокировку (ошибка)
        loginPage.login(valid);
        loginPage.shouldSeeError();
    }
}