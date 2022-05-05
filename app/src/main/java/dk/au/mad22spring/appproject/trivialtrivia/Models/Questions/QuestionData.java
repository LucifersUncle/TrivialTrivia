
package dk.au.mad22spring.appproject.trivialtrivia.Models.Questions;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class QuestionData implements Serializable
{

    @SerializedName("response_code")
    @Expose
    private Integer responseCode;
    @SerializedName("results")
    @Expose
    private List<Result> results = null;
    private final static long serialVersionUID = -2349693789975301931L;

    public Integer getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(Integer responseCode) {
        this.responseCode = responseCode;
    }

    public QuestionData withResponseCode(Integer responseCode) {
        this.responseCode = responseCode;
        return this;
    }

    public List<Result> getResults() {
        return results;
    }

    public void setResults(List<Result> results) {
        this.results = results;
    }

    public QuestionData withResults(List<Result> results) {
        this.results = results;
        return this;
    }

}
