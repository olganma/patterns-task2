package ru.netology.testmode.test;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;
import static ru.netology.testmode.data.DataGenerator.Registration.getRegisteredUser;
import static ru.netology.testmode.data.DataGenerator.Registration.getUser;
import static ru.netology.testmode.data.DataGenerator.getRandomLogin;
import static ru.netology.testmode.data.DataGenerator.getRandomPassword;

class AuthTest {

    @BeforeEach
    void setup() {
        open("http://localhost:9999");
    }

    @Test
    @DisplayName("Should successfully login with active registered user")
    void shouldSuccessfulLoginIfRegisteredActiveUser() {
        var registeredUser = getRegisteredUser("active");
        Configuration.holdBrowserOpen = true;
        $("[data-test-id=\"login\"] .input__control").setValue(registeredUser.getLogin());
        $("[data-test-id=\"password\"] .input__control").setValue(registeredUser.getPassword());
        $("[data-test-id=\"action-login\"]").click();
        $x("//h2").shouldHave(Condition.text(("Личный кабинет").trim()))
         .shouldBe(visible);
    }

    @Test
    @DisplayName("Should get error message if login with not registered user")
    void shouldGetErrorIfNotRegisteredUser() {
        var notRegisteredUser = getUser("active");
        Configuration.holdBrowserOpen = true;
        $("[data-test-id=\"login\"] .input__control").setValue(notRegisteredUser.getLogin());
        $("[data-test-id=\"password\"] .input__control").setValue(notRegisteredUser.getPassword());
        $("[data-test-id=\"action-login\"]").click();
        $(".notification__content").shouldHave(Condition.text(("Ошибка! Неверно указан логин или пароль").trim()))
                .shouldBe(visible);
    }

    @Test
    @DisplayName("Should get error message if login with blocked registered user")
    void shouldGetErrorIfBlockedUser() {
        var blockedUser = getRegisteredUser("blocked");
        Configuration.holdBrowserOpen = true;
        $("[data-test-id=\"login\"] .input__control").setValue(blockedUser.getLogin());
        $("[data-test-id=\"password\"] .input__control").setValue(blockedUser.getPassword());
        $("[data-test-id=\"action-login\"]").click();
        $(".notification__content").shouldHave(Condition.text(("Ошибка! Пользователь заблокирован").trim()))
                .shouldBe(visible);
    }

    @Test
    @DisplayName("Should get error message if login with wrong login")
    void shouldGetErrorIfWrongLogin() {
        var registeredUser = getRegisteredUser("active");
        var wrongLogin = getRandomLogin();
        Configuration.holdBrowserOpen = true;
        $("[data-test-id=\"login\"] .input__control").setValue(wrongLogin);
        $("[data-test-id=\"password\"] .input__control").setValue(registeredUser.getPassword());
        $("[data-test-id=\"action-login\"]").click();
        $(".notification__content").shouldHave(Condition.text(("Ошибка! Неверно указан логин или пароль").trim()))
                .shouldBe(visible);
    }

    @Test
    @DisplayName("Should get error message if login with wrong password")
    void shouldGetErrorIfWrongPassword() {
        var registeredUser = getRegisteredUser("active");
        var wrongPassword = getRandomPassword();
        Configuration.holdBrowserOpen = true;
        $("[data-test-id=\"login\"] .input__control").setValue(registeredUser.getLogin());
        $("[data-test-id=\"password\"] .input__control").setValue(wrongPassword);
        $("[data-test-id=\"action-login\"]").click();
        $(".notification__content").shouldHave(Condition.text(("Ошибка! Неверно указан логин или пароль").trim()))
                .shouldBe(visible);
    }
}
