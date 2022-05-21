package ru.Rusalena.tests;

import io.restassured.RestAssured;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import static io.restassured.RestAssured.given;
import static java.util.function.Predicate.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.nullValue;
import static sun.nio.cs.Surrogate.is;

public class PatchTests {
    static String token;
    static String id;

    @BeforeAll
    static void beforeAll() {
        token = given()
                .log()
                .all()
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
                .prettyPeek()
                .body()
                .jsonPath()
                .get("token")
                .toString();

    }

    @BeforeEach
    void setUp() {
        id = given()
                .log()
                .all()
                .header("Content-Type", "application/json")
                .body("{\n"
                        + "    \"firstname\" : \"Jim\",\n"
                        + "    \"lastname\" : \"Brown\",\n"
                        + "    \"totalprice\" : 111,\n"
                        + "    \"depositpaid\" : true,\n"
                        + "    \"bookingdates\" : {\n"
                        + "        \"checkin\" : \"2018-01-01\",\n"
                        + "        \"checkout\" : \"2019-01-01\"\n"
                        + "    },\n"
                        + "    \"additionalneeds\" : \"Breakfast\"\n"
                        + "}")
                .expect()
                .statusCode(200)
                .when()
                .post("https://restful-booker.herokuapp.com/booking")
                .prettyPeek()
                .body()
                .jsonPath()
                .get("bookingid")
                .toString();
    }

    @AfterAll
    static void afterAll() {

        given()
                .log()
                .method()
                .log()
                .uri()
                .log()
                .body()
                .header("Cookie", "token=" + token)
                .when()
                .delete("https://restful-booker.herokuapp.com/booking/" + id)
                .prettyPeek()
                .then()
                .statusCode(201);
    }

    @Test
    void updateBookingCookiePositiveTest() {

        given()
                .log()
                .method()
                .log()
                .uri()
                .log()
                .body()
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .header("Cookie", "token=" + token)
                .body("{\n" +
                        "    \"firstname\" : \"Petr\",\n" +
                        "    \"lastname\" : \"Brown\",\n" +
                        "    \"totalprice\" : 111,\n" +
                        "    \"depositpaid\" : true,\n" +
                        "    \"bookingdates\" : {\n" +
                        "        \"checkin\" : \"2022-01-01\",\n" +
                        "        \"checkout\" : \"2022-02-01\"\n" +
                        "    },\n" +
                        "    \"additionalneeds\" : \"Breakfast\"\n" +
                        "}")
                .when()
                .patch("https://restful-booker.herokuapp.com/booking/" + id)
                .prettyPeek()
                .then()
                .statusCode(200)
                .body("firstname", equalTo("Petr"))
                .body("lastname", equalTo("Brown"))
                .body("totalprice", equalTo(Integer.valueOf("111")))
                .body("depositpaid", equalTo(Boolean.valueOf("true")))
                .body("bookingdates.checkin", is(equalTo("2022-01-01")))
                .body("bookingdates.checkout", is(equalTo("2022-02-01")))
                .body("additionalneeds", equalTo("Breakfast"));
    }

    @Test
    void updateBookingCookiePositiveTest() {

        given()
                .log()
                .method()
                .log()
                .uri()
                .log()
                .body()
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .header("Cookie", "token=" + token)
                .body("{\n" +
                        "    \"firstname\" : \"Petr\",\n" +
                        "    \"lastname\" : \"Brown\",\n" +
                        "    \"totalprice\" : 111,\n" +
                        "    \"depositpaid\" : true,\n" +
                        "    \"bookingdates\" : {\n" +
                        "        \"checkin\" : \"2022-01-01\",\n" +
                        "        \"checkout\" : \"2022-02-01\"\n" +
                        "    },\n" +
                        "    \"additionalneeds\" : \"Breakfast\"\n" +
                        "}")
                .when()
                .patch("https://restful-booker.herokuapp.com/booking/" + id)
                .prettyPeek()
                .then()
                .statusCode(200)
                .body("firstname", equalTo("Petr"))
                .body("lastname", equalTo("Brown"))
                .body("totalprice", equalTo(Integer.valueOf("111")))
                .body("depositpaid", equalTo(Boolean.valueOf("true")))
                .body("bookingdates.checkin", is(equalTo("2022-01-01")))
                .body("bookingdates.checkout", is(equalTo("2022-02-01")))
                .body("additionalneeds", equalTo("Breakfast"));
    }

}
