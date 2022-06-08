package tests.demoWeb;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.WebDriverRunner;
import com.codeborne.selenide.logevents.SelenideLogger;
import helpers.Attach;
import io.qameta.allure.selenide.AllureSelenide;
import io.restassured.RestAssured;
import org.aeonbits.owner.ConfigFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import owner.LinksOwnerBug;
import owner.LinksOwnerDemoWeb;
import owner.RemoteOwner;
import owner.UserOwner;
import org.openqa.selenium.Cookie;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.*;
import static helpers.CustomApiListener.withCustomTemplates;
import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;

public class DemowebshopTests {
    static LinksOwnerDemoWeb linkConfig = ConfigFactory.create(LinksOwnerDemoWeb.class);
    static UserOwner userConfig = ConfigFactory.create(UserOwner.class);
    static String userLogin = userConfig.userLogin(),
            userPassword = userConfig.userPassword(),
            authCookieName = "NOPCOMMERCE.AUTH";

    @BeforeAll
    static void setUp() {
        SelenideLogger.addListener("AllureSelenide", new AllureSelenide());
        Configuration.baseUrl = linkConfig.uiURL();
        RestAssured.baseURI = linkConfig.apiURI();
        RemoteOwner confRemote = ConfigFactory.create(RemoteOwner.class);
        SelenideLogger.addListener("AllureSelenide", new AllureSelenide()); //Алюр лисенер

        String propertyBrowserSize = System.getProperty("browserSize", "1980x1024"),
                propertyRemoteUrl = System.getProperty("remoteUrl", confRemote.url());


        Configuration.browserSize = propertyBrowserSize;
        Configuration.remote = propertyRemoteUrl;

    }

    @AfterEach
    void afterEach() {
        Attach.screenshotAs("Итоговый скрин");
        closeWebDriver();
    }

    @Test
    void test01() {

        step("Получить куки авторизации из API метода и добавить их в браузер", () -> {
            String authCookieValue =
                    given()
                            .filter(withCustomTemplates())
                            .contentType("application/x-www-form-urlencoded")
                            .formParam("Email", userLogin)
                            .formParam("Password", userPassword)
                            .log().all()
                            .when()
                            .post("/login")
                            .then().log().all()
                            .statusCode(302)
                            .extract().cookie(authCookieName);
            step("Открыть легковесную страницу сайта", () ->
                    open("/Themes/DefaultClean/Content/images/logo.png"));

            step("Добавить куки в открытый браузер(с легковесной страницей)", () -> {
                //задаю название кук и значение
                Cookie authCookie = new Cookie(authCookieName, authCookieValue);
                // через импорт import org.openqa.selenium.Cookie;
                WebDriverRunner.getWebDriver().manage().addCookie(authCookie);
            });
        });
        step("Открыть главную страницу сайта", () ->
                open(""));
        step("Проверка успешной авторизации по логину в личном кабинете", () -> {
            $(".account").shouldHave(text(userLogin));
        });
    }


}
