package jenkins_gradle_bootstrap;

import org.fluentlenium.core.FluentPage;
import org.fluentlenium.core.domain.FluentList;
import org.fluentlenium.core.domain.FluentWebElement;

import java.util.concurrent.TimeUnit;

public class TestPage extends FluentPage {

    private String url;
    private final String SEARCH_INPUT_FIELD = "#search > input[type=text]";
    private final String SEARCH_RESULTS = "#mainbar > div.search-results.js-search-results > .question-summary.search-result";

    @Override
    public String getUrl() {
        return url;
    }

    public void setUrl(final String url) {
        this.url = url;
    }

    public FluentWebElement getSearchInputBox() {
        return findFirst(SEARCH_INPUT_FIELD);
    }

    public FluentList<FluentWebElement> getSearchResults() {
        getDriver().manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        return find(SEARCH_RESULTS);
    }

    public final String getSearchInputBoxSelector() {
        return SEARCH_INPUT_FIELD;
    }
}
