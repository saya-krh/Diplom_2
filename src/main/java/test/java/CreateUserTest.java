package test.java;

import io.restassured.response.Response;
import models.UserRequestModel;
import org.junit.Test;
import util.BaseTest;

import static org.junit.Assert.assertEquals;

public class CreateUserTest extends BaseTest {

    @Test
    public void createUniqueUserTest() {
        UserRequestModel saida = new UserRequestModel("karakhanova" + getUniqueId() + "@mail.ru", "qwerty", "Saida");

        postNewUser(saida);
    }

    @Test
    public void createAlreadyExistingUserTest() {
        String uniqueId = getUniqueId();
        UserRequestModel saida = new UserRequestModel("karakhanova" + uniqueId + "@mail.ru", "qwerty", "Saida");
        usersToDelete.add(saida);

        Response response = createUser(saida);

        assertEquals("User should have been created!",
                200,
                response.getStatusCode());

        Response failedResponse = createUser(saida);

        assertEquals("Should have received status code 403 because user already exists!",
                403,
                failedResponse.getStatusCode());
    }

    @Test
    public void createUniqueUserWithoutRequiredFieldTest() {
        UserRequestModel user1 = new UserRequestModel("karakhanova" + getUniqueId() + "@mail.ru", "qwerty", null);
        UserRequestModel user2 = new UserRequestModel("karakhanova" + getUniqueId() + "@mail.ru", null, "Saida");
        UserRequestModel user3 = new UserRequestModel(null, "qwerty", "Saida");

        Response failedResponse2 = createUser(user1);
        assertEquals("Should have received status code 403 because there are missing required fields!",
                403,
                failedResponse2.getStatusCode());

        Response failedResponse1 = createUser(user2);
        assertEquals("Should have received status code 403 because there are missing required fields!",
                403,
                failedResponse1.getStatusCode());

        Response failedResponse = createUser(user3);
        assertEquals("Should have received status code 403 because there are missing required fields!",
                403,
                failedResponse.getStatusCode());
    }
}

