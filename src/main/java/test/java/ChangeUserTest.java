package test.java;

import io.restassured.response.Response;
import models.AuthResponseModel;
import models.PatchResponseModel;
import models.UserRequestModel;
import org.junit.Test;
import util.BaseTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ChangeUserTest extends BaseTest {

    @Test
    public void changeUserWithAuth() {
        UserRequestModel saida = new UserRequestModel("karakhanova" + getUniqueId() + "@mail.ru", "qwerty", "Saida");
        postNewUser(saida);
        AuthResponseModel authResponseModel = authorizeUser(saida).as(AuthResponseModel.class);

        String newName = "Saida Karakhanova";
        saida.setName(newName);

        Response response1 = updateUser(saida, authResponseModel.getAccessToken());

        assertEquals("Code should be 200!", 200, response1.getStatusCode());
        assertTrue("User update should have succeeded!", response1.as(PatchResponseModel.class).isSuccess());
        assertEquals("This field should have been updated!", newName, response1.as(PatchResponseModel.class).getUser().getName());


        String newEmail = "newemail" + getUniqueId() + "@gmail.com";
        saida.setEmail(newEmail);

        Response response2 = updateUser(saida, authResponseModel.getAccessToken());

        assertEquals("Code should be 200!", 200, response2.getStatusCode());
        assertTrue("User update should have succeeded!", response2.as(PatchResponseModel.class).isSuccess());
        assertEquals("This field should have been updated!", newEmail, response2.as(PatchResponseModel.class).getUser().getEmail());


        String newPassword = "123456";
        saida.setPassword(newPassword);

        Response response3 = updateUser(saida, authResponseModel.getAccessToken());

        assertEquals("Code should be 200!", 200, response3.getStatusCode());
        assertTrue("User update should have succeeded!", response3.as(PatchResponseModel.class).isSuccess());

        Response authWithNewPassword = authorizeUser(saida);

        assertEquals("Should have authorized with new password!",
                200,
                authWithNewPassword.getStatusCode());
    }

    @Test
    public void changeUserWithoutAuth() {
        UserRequestModel saida = new UserRequestModel("karakhanova" + getUniqueId() + "@mail.ru", "qwerty", "Saida");
        UserRequestModel updatedSaida = new UserRequestModel("karakhanova" + getUniqueId() + "@mail.ru", "qwerty", "Saida");
        postNewUser(saida);

        String newName = "Saida Karakhanova";
        updatedSaida.setName(newName);

        Response response1 = updateUser(updatedSaida, "");

        assertEquals("Code should be 401!", 401, response1.getStatusCode());


        String newEmail = "newemail" + getUniqueId() + "@gmail.com";
        updatedSaida.setEmail(newEmail);

        Response response2 = updateUser(updatedSaida, "");

        assertEquals("Code should be 401!", 401, response2.getStatusCode());


        String newPassword = "123456";
        updatedSaida.setPassword(newPassword);

        Response response3 = updateUser(updatedSaida, "");

        assertEquals("Code should be 401!", 401, response3.getStatusCode());
    }
}
