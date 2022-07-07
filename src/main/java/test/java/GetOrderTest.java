package test.java;

import io.restassured.response.Response;
import models.AuthResponseModel;
import models.UserRequestModel;
import org.junit.Test;
import util.BaseTest;

import static org.junit.Assert.assertEquals;

public class GetOrderTest extends BaseTest {

    @Test
    public void getOrdersForAuthorizedUserTest() {
        UserRequestModel saida = new UserRequestModel("karakhanova" + getUniqueId() + "@mail.ru", "qwerty", "Saida");
        postNewUser(saida);
        AuthResponseModel authResponseModel = authorizeUser(saida).as(AuthResponseModel.class);

        Response orders = getOrders(authResponseModel.getAccessToken());

        assertEquals("Request should be successful!", 200, orders.statusCode());
    }

    @Test
    public void getOrdersForUnauthorizedUserTest() {
        UserRequestModel saida = new UserRequestModel("karakhanova" + getUniqueId() + "@mail.ru", "qwerty", "Saida");
        postNewUser(saida);

        Response orders = getOrders("");

        assertEquals("Request should have failed!", 401, orders.statusCode());
    }
}

