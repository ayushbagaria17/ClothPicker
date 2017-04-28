package in.agrostar.ulink.clothpicker.domain;

import com.google.gson.annotations.SerializedName;

import java.util.HashSet;
import java.util.List;


/**
 * Created by ayush on 28/4/17.
 */

public class SuggestionPool {

    @SerializedName("uploadObjectSet")
    HashSet<Integer> uploadObjectSet;

    @SerializedName("suggestions")
    List<Suggestion1> suggestionList;

    public SuggestionPool() {

    }

    public HashSet<Integer> getUploadObjectSet() {
        return uploadObjectSet;
    }

    public void setUploadObjectSet(HashSet<Integer> uploadObjectSet) {
        this.uploadObjectSet = uploadObjectSet;
    }

    public List<Suggestion1> getSuggestionList() {
        return suggestionList;
    }

    public void setSuggestionList(List<Suggestion1> suggestionList) {
        this.suggestionList = suggestionList;
    }
}
