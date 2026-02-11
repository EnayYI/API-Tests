package API_Tests;

import io.qameta.allure.Allure;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static io.restassured.RestAssured.given;
import static io.restassured.config.EncoderConfig.encoderConfig;
import static io.restassured.config.RestAssuredConfig.config;
import static org.hamcrest.Matchers.*;

@DisplayName("API тесты")
class NotesApiHealthTest {

    @BeforeAll
    static void setUp() {
        RestAssured.filters(new AllureRestAssured());
        RestAssured.baseURI = "https://practice.expandtesting.com";
        RestAssured.basePath = "/notes/api";
        RestAssured.config = config().encoderConfig(
                encoderConfig().defaultContentCharset("UTF-8"));
    }

    //@Test
    void healthCheck() {
        given()
                .when()
                .get("/health-check")
                .then()
                .log().body()
                .statusCode(200)
                .body("message", is("Notes API is Running"))
                // мягкий порог по времени ответа (например, < 1500 мс)
                .time(lessThan(1500L), TimeUnit.MILLISECONDS);
    }

    @Test
    @DisplayName("Регистрация")
    void registration_of_new_account(){
        String name = "User " + UUID.randomUUID().toString().substring(0, 1);
        String email = "user_" + UUID.randomUUID() + "@mail.com";
        String password = "123456";

        Allure.parameter("name", name);
        Allure.parameter("email", email);

        Allure.step("Регистрация: отправить запрос и проверить ответ", () -> {
            given()
                    .contentType("application/x-www-form-urlencoded; charset=UTF-8")
                    .accept(ContentType.JSON)
                    .formParam("name", name)
                    .formParam("email", email)
                    .formParam("password", password)
                    .when()
                    .post("/users/register")
                    .then()
                    .statusCode(anyOf(is(200), is(201)))
                    .body("message", equalTo("User account created successfully"));
        });
    }

    @Test
    @DisplayName("Регистрация уже существующего пользователя")
    void registration_of_existing_account(){
        String name = "User " + UUID.randomUUID().toString().substring(0, 1);
        String email = "user_" + UUID.randomUUID() + "@mail.com";
        String password = "123456";

        Allure.parameter("name", name);
        Allure.parameter("email", email);

        Allure.step("Регистрация: отправить запрос и проверить ответ 200", () -> {
            given()
                    .contentType("application/x-www-form-urlencoded; charset=UTF-8")
                    .accept(ContentType.JSON)
                    .formParam("name", name)
                    .formParam("email", email)
                    .formParam("password", password)
            .when()
                    .post("/users/register")
            .then()
                    .statusCode(anyOf(is(200), is(201)))
                    .body("message", equalTo("User account created successfully"));
        });

        Allure.step("Регистрация: Существующий пользователь, проверить ответ 400", () -> {
            given()
                    .contentType("application/x-www-form-urlencoded; charset=UTF-8")
                    .accept(ContentType.JSON)
                    .formParam("name", name)
                    .formParam("email", email)
                    .formParam("password", password)
            .when()
                    .post("/users/register")
            .then()
                    .statusCode(anyOf(is(400), is(409)))
                    .body("message", equalTo("An account already exists with the same email address"));

        });
    }

    @Test
    @DisplayName("Логин и получение токена")
    void login_and_receiving_token(){
        String name = "User " + UUID.randomUUID().toString().substring(0, 1);
        String email = "user_" + UUID.randomUUID() + "@mail.com";
        String password = "123456";

        Allure.parameter("name", name);
        Allure.parameter("email", email);

        Allure.step("Регистрация: отправить запрос и проверить ответ 200", () -> {
            given()
                    .contentType("application/x-www-form-urlencoded; charset=UTF-8")
                    .accept(ContentType.JSON)
                    .formParam("name", name)
                    .formParam("email", email)
                    .formParam("password", password)
                    .when()
                    .post("/users/register")
                    .then()
                    .statusCode(anyOf(is(200), is(201)))
                    .body("message", equalTo("User account created successfully"));
        });

        Allure.step("Логин и получение токена", () -> {
            given()
                    .contentType("application/x-www-form-urlencoded; charset=UTF-8")
                    .accept(ContentType.JSON)
                    .formParam("email", email)
                    .formParam("password", password)
                    .when()
                    .post("/users/login")
                    .then()
                    .statusCode(200)
                    .body("data.token", notNullValue());
        });
    }

}