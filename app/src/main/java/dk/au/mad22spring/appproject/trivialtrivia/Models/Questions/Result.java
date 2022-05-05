
package dk.au.mad22spring.appproject.trivialtrivia.Models.Questions;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Result implements Serializable
{

    @SerializedName("category")
    @Expose
    private String category;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("difficulty")
    @Expose
    private String difficulty;
    @SerializedName("question")
    @Expose
    private String question;
    @SerializedName("correct_answer")
    @Expose
    private String correctAnswer;
    @SerializedName("incorrect_answers")
    @Expose
    private List<String> incorrectAnswers = null;
    private final static long serialVersionUID = -1564949533197455527L;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Result withCategory(String category) {
        this.category = category;
        return this;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Result withType(String type) {
        this.type = type;
        return this;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public Result withDifficulty(String difficulty) {
        this.difficulty = difficulty;
        return this;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public Result withQuestion(String question) {
        this.question = question;
        return this;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public Result withCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
        return this;
    }

    public List<String> getIncorrectAnswers() {
        return incorrectAnswers;
    }

    public void setIncorrectAnswers(List<String> incorrectAnswers) {
        this.incorrectAnswers = incorrectAnswers;
    }

    public Result withIncorrectAnswers(List<String> incorrectAnswers) {
        this.incorrectAnswers = incorrectAnswers;
        return this;
    }

}
