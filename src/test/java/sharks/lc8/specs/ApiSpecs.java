package sharks.lc8.specs;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import sharks.lc8.config.ConfigManager;

/**
 * Shared settings for all API requests (address, JSON, language, logging), so they are not repeated in every request.
 * Fluent: the settings are built with a chain of RequestSpecBuilder calls, dot after dot.
 * Utility class: final + private constructor + only static methods. No data, no objects needed (like Math).
 */
public final class ApiSpecs {

    // Settings come from the Singleton: the address is not hardcoded anywhere, it lives in config.properties.
    // static: static methods can use only static fields.
    private static final ConfigManager CONFIG = ConfigManager.getInstance();

    // private: objects of this class are not needed
    private ApiSpecs() {
    }

    // "Plain form": for requests without a token (register, login, guestSession)
    public static RequestSpecification base() {
        return new RequestSpecBuilder()
                .setBaseUri(CONFIG.getApiBaseUrl())
                .setContentType(ContentType.JSON)
                .addHeader("localization", CONFIG.getLocalization())
                // Filters, not .log(): they print both the request and the response. Without the response a failed test is hard to debug.
                .addFilter(new RequestLoggingFilter())
                .addFilter(new ResponseLoggingFilter())
                .build();
    }

    // "Signed form": everything from base() + the token. For requests of a logged-in user (activatePromocode).
    // The token comes after register or login, AuthApiFacade keeps it and passes it here through SlotCityApiClient.
    public static RequestSpecification authorized(String token) {
        return new RequestSpecBuilder()
                .addRequestSpecification(base())
                .addHeader("authorization", "Bearer " + token)
                .build();
    }
}