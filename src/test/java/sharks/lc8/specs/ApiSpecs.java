package sharks.lc8.specs;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import sharks.lc8.config.ConfigManager;

/**
 * Fluent for the API: the spec is assembled by a chain of RequestSpecBuilder calls.
 * Without it the same four lines would be copied into every request in SlotCityApiClient.
 * final + private constructor + static methods = a tools-only class, it holds no data,
 * so there is nothing to create.
 */
public final class ApiSpecs {

    // Base url comes from the Singleton, never hardcoded in the client
    private static final ConfigManager CONFIG = ConfigManager.getInstance();

    private ApiSpecs() {
    }

    public static RequestSpecification base() {
        return new RequestSpecBuilder()
                .setBaseUri(CONFIG.getApiBaseUrl())
                .setContentType(ContentType.JSON)
                .addHeader("localization", CONFIG.getLocalization())
                // Filters, not .log() - a request spec cannot log the response,
                // and without the response in the console nothing is debuggable
                .addFilter(new RequestLoggingFilter())
                .addFilter(new ResponseLoggingFilter())
                .build();
    }

    // Reuses base() instead of repeating it, so both kinds of request go to the same host
    // with the same headers
    public static RequestSpecification authorized(String token) {
        return new RequestSpecBuilder()
                .addRequestSpecification(base())
                .addHeader("authorization", "Bearer " + token)
                .build();
    }
}