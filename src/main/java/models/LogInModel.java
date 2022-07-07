package models;

public class LogInModel {
    private final String email;
    private final String password;

    public LogInModel(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}