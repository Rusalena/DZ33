package ru.Rusalena.tests;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static java.util.function.Predicate.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static sun.nio.cs.Surrogate.is;

public class GreateTokenTests {

    @BeforeAll
    static void beforeAll() {

        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    @Test
    void createTokenPositiveTest() {

        given()
                .log()
                .method()
                .log()
                .uri()
                .log()
                .body()
                .header("Content-Type", "application/json")
                .body("{\n" +
                        "    \"username\" : \"admin\",\n" +
                        "    \"password\" : \"password123\"\n" +
                        "}")
                .expect()
                .statusCode(200)
                .body("token", is(not(nullValue())))
                .when()
                .post("https://restful-booker.herokuapp.com/auth")
                .prettyPeek();
    }

    @Test
    void createTokenWithAWrongPasswordNegativeTest() {

        given()
                .log()
                .method()
                .log()
                .uri()
                .log()
                .body()
                .header("Content-Type", "application/json")
                .body("{\n" +
                        "    \"username\" : \"admin\",\n" +
                        "    \"password\" : \"password111\"\n" +
                        "}")
                .when()
                .post("https://restful-booker.herokuapp.com/auth")
                .prettyPeek()
                .then()
                .statusCode(200)
                .body("reason", equalTo("Bad credentials"));
    }

    @Test
    void createTokenWithAWrongUsernameAndPasswordNegativeTest() {

        Response response = given()
                .log()
                .method()
                .log()
                .uri()
                .log()
                .body()
                .header("Content-Type", "application/json")
                .body("{\n" +
                        "    \"username\" : \"admin444\",\n" +
                        "    \"password\" : \"password111\"\n" +
                        "}")
                .when()
                .post("https://restful-booker.herokuapp.com/auth")
                .prettyPeek();
        assertThat(response.statusCode(), equalTo(200));
        assertThat(response.body().jsonPath().get("reason"), containsStringIgnoringCase("Bad credentials"));

    }
}
