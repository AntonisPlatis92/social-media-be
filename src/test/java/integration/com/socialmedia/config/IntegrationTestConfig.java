package integration.com.socialmedia.config;

import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import static integration.com.socialmedia.config.TestContainerSetup.postgresqlContainer;

public class IntegrationTestConfig implements BeforeAllCallback, AfterAllCallback {

    @Override
    public void beforeAll(ExtensionContext context) {
        System.setProperty("app.environment", "integration-test");
        postgresqlContainer.start();
        TestContainerSetup.setUp();
    }

    @Override
    public void afterAll(ExtensionContext extensionContext) {
        postgresqlContainer.close();
        postgresqlContainer.stop();
    }
}
