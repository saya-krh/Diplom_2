package util;

import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import models.*;
import org.junit.After;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;

public class BaseTest {
    static {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site/api";
    }

    static final String USER_CREATION_ENDPOINT = "/auth/register";
    static final String USER_AUTH_ENDPOINT = "/auth/login";
    static final String USER_LOGOUT_ENDPOINT = "/auth/logout";
    static final String USER_ENDPOINT = "/auth/user";
    static final String ORDER_ENDPOINT = "/orders";

    protected List<UserRequestModel> usersToDelete = new ArrayList<>();

    @After
    public void tearDown() {
        if (!usersToDelete.isEmpty()) {
            for (UserRequestModel user : usersToDelete) {
                Response response = authorizeUser(user);

                deleteUser(response.as(AuthResponseModel.class).getAccessToken());
            }
            usersToDelete = new ArrayList<>();
        }
    }

    @Step("Create a user")
    protected static Response createUser(UserRequestModel user) {
        return given()
                .header("Content-type", "application/json")
                .body(user)
                .post(USER_CREATION_ENDPOINT);
    }

    @Step("Authorize a user")
    protected static Response authorizeUser(UserRequestModel user) {
        return given()
                .header("Content-type", "application/json")
                .body(new LogInModel(user.getEmail(), user.getPassword()))
                .post(USER_AUTH_ENDPOINT);
    }

    @Step("Delete a user")
    protected static void deleteUser(String token) {
        given()
                .header("Content-type", "application/json")
                .header("Authorization", token)
                .delete(USER_ENDPOINT)
                .then()
                .statusCode(202);
    }

    @Step("Successfully create a user")
    protected void postNewUser(UserRequestModel saida) {
        Response response = createUser(saida);

        assertEquals("User should have been created!",
                200,
                response.getStatusCode());
        usersToDelete.add(saida);
    }

    @Step("Successfully update a user")
    protected Response updateUser(UserRequestModel updatedUser, String token) {
        return given()
                .header("Content-type", "application/json")
                .header("Authorization", token)
                .body(updatedUser)
                .patch(USER_ENDPOINT);
    }

    @Step("Get authorized users orders")
    protected Response getOrders(String token) {
        return given()
                .header("Authorization", token)
                .get(ORDER_ENDPOINT);
    }

    @Step("Create an order")
    protected Response createOrder(OrderRequestModel order, String token) {
        return given()
                .header("Content-type", "application/json")
                .header("Authorization", token)
                .body(order)
                .post(ORDER_ENDPOINT);
    }

    protected String getUniqueId() {
        return UUID.randomUUID().toString();
    }
}