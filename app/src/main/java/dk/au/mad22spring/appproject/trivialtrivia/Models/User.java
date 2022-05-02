package dk.au.mad22spring.appproject.trivialtrivia.Models;

public class User {
    public String username, email;

    public User() {
        //empty constructor
    }

    public User(String username, String email) {
        this.username = username;
        this.email = email;
    }
}
