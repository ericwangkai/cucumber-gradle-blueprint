package jenkins_gradle_bootstrap;

import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.fluentlenium.core.FluentAdapter;
import org.fluentlenium.core.annotation.Page;
import org.fluentlenium.core.domain.FluentWebElement;
import org.openqa.selenium.*;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.remote.UnreachableBrowserException;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.fluentlenium.assertj.FluentLeniumAssertions.assertThat;

public class TestSteps extends FluentAdapter {
    private WebDriver webDriver;

    public TestSteps() {
        webDriver = getDefaultDriver();
        this.initFluent(webDriver);
        this.initTest();
    }

    @After
    public void close(Scenario scenario) {
        closeCurrentDriver();
    }

    @Page
    private TestPage testPage;

    @Given("User views page (.*)$")
    public void goToPage(final String identifier) {
        testPage.setUrl(identifier);
        try {
            goTo(testPage);
            waitUntilDocumentIsReady();
        } catch (TimeoutException tex) {
            System.out.println("Timeout while waiting for documentReady : " + tex);
            await().atMost(10000).untilPage(testPage).isAt();
        }
    }

    @Then("(\\d+) search results are displayed")
    public void assertSearchResultsDisplayed(int expectedNumberOfResults) {
        waitUntilDocumentIsReady();
        int numberOfResults = testPage.getSearchResults().size();
        final String errorMessage = expectedNumberOfResults + " search results should be displayed, but is " + numberOfResults;
        assertThat(numberOfResults).overridingErrorMessage(errorMessage).isEqualTo(expectedNumberOfResults);
    }

    @And("page contains a search input field")
    public void assertInputFieldIsDisplayed() {
        await().atMost(30, TimeUnit.SECONDS).until(testPage.getSearchInputBoxSelector()).areDisplayed();
        assertThat(testPage.getSearchInputBox()).overridingErrorMessage("error").isDisplayed();
    }

    @Then("^page (.+) is displayed$")
    public void pageIsDisplayed(String expectedUrl) {
        waitUntilDocumentIsReady();
        String currentUrl = getDriver().getCurrentUrl();
        final String errorMessage = "Expected page to be '" + expectedUrl + "', found " + currentUrl;
        assertThat(currentUrl).overridingErrorMessage(errorMessage).isEqualTo(expectedUrl);
    }

    @When("^user searches for (.+)$")
    public void fillOutSearchForm(final String searchTerm) {
        FluentWebElement inputBox = testPage.getSearchInputBox();
        inputBox.text(searchTerm);
        inputBox.submit();
    }

    @Override
    public WebDriver getDriver() {
        return getDefaultDriver();
    }

    @Override
    public WebDriver getDefaultDriver() {
        if (null == webDriver) {
            try {
                // suppress htmlunit warnings
                java.util.logging.Logger.getLogger("com.gargoylesoftware").setLevel(java.util.logging.Level.OFF);
                webDriver = new HtmlUnitDriver();
                ((HtmlUnitDriver) webDriver).setJavascriptEnabled(true);
            } catch (Exception ex) {
                System.err.println("Could not instantiate webdriver!");
            }
        }
        return webDriver;
    }

    private void closeCurrentDriver() {
        try {
            if (!(null == webDriver)) {
                webDriver.quit();
                webDriver = null;
            }
        } catch (UnreachableBrowserException ex) {
            System.err.print("Cannot close current webdriver: unreachable browser" + ex);
        }
    }

    public void waitUntilDocumentIsReady() {
        ExpectedCondition<Boolean> pageLoadCondition = driver -> {
            String readyState;
            try {
                readyState = ((JavascriptExecutor) driver).executeScript("return document.readyState").toString();
            } catch (Exception e) {
                System.err.println("Unable to get Document state. Document state will be set to 'unknown' " + e);
                readyState = "unknown";
            }
            System.out.println("document-State: " + readyState);
            return "interactive".equals(readyState) || "complete".equals(readyState);
        };
        WebDriverWait wait = new WebDriverWait(getDriver(), 30 / 1000, 1000);
        wait.until(pageLoadCondition);
    }
}
