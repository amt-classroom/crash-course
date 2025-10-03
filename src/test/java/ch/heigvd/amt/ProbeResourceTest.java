package ch.heigvd.amt;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

@QuarkusTest
public class ProbeResourceTest {

    @Test
    public void testIndexPage() {
        given()
          .when()
                .get("/")
          .then()
             .statusCode(200)
                .body(org.hamcrest.Matchers.containsString("Welcome to Uptime"));
    }

    @Test
    public void testCreatePage() {
        given()
          .when()
                .formParam("url", "http://example.com")
                .post("/probes")
          .then()
             .statusCode(200)
                .body(org.hamcrest.Matchers.containsString("http://example.com"));
    }


}
