package dk.au.mad22spring.appproject.trivialtrivia.Models;


//Ved ikke om vi får brug for den eftersom vi bruger firestore.
//Jeg har lavet den til at teste mine RCVs
public class UserModel {

    public String userName;
    public int score;

    public UserModel(String u, int s){
        this.userName = u;
        this.score = s;
    }

}
