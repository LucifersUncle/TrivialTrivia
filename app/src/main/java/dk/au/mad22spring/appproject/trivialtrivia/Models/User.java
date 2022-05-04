package dk.au.mad22spring.appproject.trivialtrivia.Models;

public class User {

    public String username, email, userReference;

    public User() {
        //empty constructor
    }

    public User(String username, String email) {
        this.username = username;
        this.email = email;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserReference() {
        return userReference;
    }

    public void setUserReference(String userReference) {
        this.userReference = userReference;
    }

}
