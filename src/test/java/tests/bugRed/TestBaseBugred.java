package tests.bugRed;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.logevents.SelenideLogger;
import helpers.Attach;
import io.qameta.allure.selenide.AllureSelenide;
import io.restassured.RestAssured;
import org.aeonbits.owner.ConfigFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.openqa.selenium.remote.DesiredCapabilities;
import owner.LinksOwnerBug;
import owner.RemoteOwner;

import static com.codeborne.selenide.Selenide.closeWebDriver;

@Tag("bugred")
public class TestBaseBugred {
    @BeforeAll
    static void SetUp() {
        LinksOwnerBug confBugLink = ConfigFactory.create(LinksOwnerBug.class);
        RemoteOwner confRemote = ConfigFactory.create(RemoteOwner.class);
        SelenideLogger.addListener("AllureSelenide", new AllureSelenide()); //Алюр лисенер

        String propertyBrowserSize = System.getProperty("browserSize", "1980x1024"),
                propertyBaseUrl = System.getProperty("baseUrl", confBugLink.uiURL()),
                propertyRemoteUrl = System.getProperty("remoteUrl", confRemote.url()),
                propertyBaseUri = System.getProperty("baseUri", confBugLink.apiURI());

        Configuration.browserSize = propertyBrowserSize;
        Configuration.baseUrl = propertyBaseUrl;
        Configuration.remote = propertyRemoteUrl;
        RestAssured.baseURI = propertyBaseUri;

        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("enableVNC", true);
        capabilities.setCapability("enableVideo", true);
        Configuration.browserCapabilities = capabilities;
    }

    @AfterEach
    @DisplayName("Добавить пруфы")
    void addAttachments() {
        Attach.screenshotAs("Итоговый скрин");
        Attach.pageSource();
        Attach.browserConsoleLogs();
        Attach.addVideo();
        closeWebDriver();
    }
}
