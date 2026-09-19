package sharks.lc8.specs;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import sharks.lc8.config.ConfigManager;

public final class ApiSpecs {

    private static final ConfigManager CONFIG = ConfigManager.getInstance();

    private ApiSpecs() {
    }

    public static RequestSpecification base() {
        return new RequestSpecBuilder()
                .setBaseUri(CONFIG.getApiBaseUrl())
                .setContentType(ContentType.JSON)
                .addHeader("localization", CONFIG.getLocalization())
                .addFilter(new RequestLoggingFilter())
                .addFilter(new ResponseLoggingFilter())
                .build();
    }

    public static RequestSpecification authorized(String token) {
        return new RequestSpecBuilder()
                .addRequestSpecification(base())
                .addHeader("authorization", "Bearer " + token)
                .build();
    }

    public static ResponseSpecification successResponse() {
        return new ResponseSpecBuilder()
                .expectStatusCode(200)
                .expectContentType(ContentType.JSON)
                .log(LogDetail.ALL)
                .build();
    }

    public static ResponseSpecification unauthorizedResponse() {
        return new ResponseSpecBuilder()
                .expectStatusCode(401)
                .build();
    }
}