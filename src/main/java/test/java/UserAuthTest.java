package test.java;

import io.restassured.response.Response;
import models.AuthResponseModel;
import models.UserRequestModel;
import org.junit.Test;
import util.BaseTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class UserAuthTest extends BaseTest {

    @Test
    public void authorizeExistingUserTest() {
        UserRequestModel saida = new UserRequestModel("karakhanova" + getUniqueId() + "@mail.ru", "qwerty", "Saida");
        postNewUser(saida);

        Response response = authorizeUser(saida);

        assertEquals("Code should be 200!", 200, response.getStatusCode());
        assertTrue("User auth should have succeeded!", response.as(AuthResponseModel.class).isSuccess());
    }

    @Test
    public void authorizeExistingUserWithWrongCredentialsTest() {
        UserRequestModel saida = new UserRequestModel("karakhanova" + getUniqueId() + "@mail.ru", "qwerty", "Saida");
        postNewUser(saida);

        Response response = authorizeUser(new UserRequestModel("wrongEmail@mail.ru", "wrongPassword", "Saida"));

        assertEquals("User auth should have failed because of wrong credentials!",
                401,
                response.getStatusCode());
    }
}