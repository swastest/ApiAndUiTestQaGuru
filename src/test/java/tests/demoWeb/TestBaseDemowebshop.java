package tests.demoWeb;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.logevents.SelenideLogger;
import helpers.Attach;
import io.qameta.allure.selenide.AllureSelenide;
import io.restassured.RestAssured;
import org.aeonbits.owner.ConfigFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import owner.LinksOwnerDemoWeb;
import owner.RemoteOwner;

import static com.codeborne.selenide.Selenide.closeWebDriver;

public class TestBaseDemowebshop {

    @BeforeAll
    static void setUp() {
        LinksOwnerDemoWeb linkConfig = ConfigFactory.create(LinksOwnerDemoWeb.class);
        SelenideLogger.addListener("AllureSelenide", new AllureSelenide());
        Configuration.baseUrl = linkConfig.uiURL();
        RestAssured.baseURI = linkConfig.apiURI();
        RemoteOwner confRemote = ConfigFactory.create(RemoteOwner.class);

        String propertyBrowserSize = System.getProperty("browserSize", "1980x1024"),
                propertyRemoteUrl = System.getProperty("remoteUrl", confRemote.url());


        Configuration.browserSize = propertyBrowserSize;
        Configuration.remote = propertyRemoteUrl;

    }

    @AfterEach
    void afterEach() {
        Attach.screenshotAs("Итоговый скрин");
        Attach.pageSource();
        Attach.browserConsoleLogs();
        Attach.addVideo();
        closeWebDriver();
    }

}
