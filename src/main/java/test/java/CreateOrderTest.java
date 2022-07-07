package test.java;

import io.restassured.response.Response;
import models.AuthResponseModel;
import models.UserRequestModel;
import org.junit.Test;
import util.BaseTest;
import util.OrderDataProducer;

import static org.junit.Assert.assertEquals;

public class CreateOrderTest extends BaseTest {

    @Test
    public void createOrderWithAuthorizedUser() {
        UserRequestModel saida = new UserRequestModel("karakhanova" + getUniqueId() + "@mail.ru", "qwerty", "Saida");
        postNewUser(saida);
        AuthResponseModel authResponseModel = authorizeUser(saida).as(AuthResponseModel.class);

        Response response = createOrder(OrderDataProducer.getCorrectOrder(), authResponseModel.getAccessToken());

        assertEquals("Order should have been created!", 200, response.getStatusCode());
    }

    @Test
    public void createOrderWithUnauthorizedUser() {
        UserRequestModel saida = new UserRequestModel("karakhanova" + getUniqueId() + "@mail.ru", "qwerty", "Saida");
        postNewUser(saida);

        Response response = createOrder(OrderDataProducer.getCorrectOrder(), "");

        assertEquals("Order should have been created!", 200, response.getStatusCode());
    }

    @Test
    public void createOrderWithoutIngredients() {
        UserRequestModel saida = new UserRequestModel("karakhanova" + getUniqueId() + "@mail.ru", "qwerty", "Saida");
        postNewUser(saida);
        AuthResponseModel authResponseModel = authorizeUser(saida).as(AuthResponseModel.class);

        Response response = createOrder(OrderDataProducer.getEmptyOrder(), authResponseModel.getAccessToken());

        assertEquals("Order without ingredients cannot be created!", 400, response.getStatusCode());
    }

    @Test
    public void createOrderWithWrongIngredients() {
        UserRequestModel saida = new UserRequestModel("karakhanova" + getUniqueId() + "@mail.ru", "qwerty", "Saida");
        postNewUser(saida);
        AuthResponseModel authResponseModel = authorizeUser(saida).as(AuthResponseModel.class);

        Response response = createOrder(OrderDataProducer.getIncorrectOrder(), authResponseModel.getAccessToken());

        assertEquals("Order with wrong ingredients cannot be created!", 500, response.getStatusCode());
    }
}