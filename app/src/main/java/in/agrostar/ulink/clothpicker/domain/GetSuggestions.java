package in.agrostar.ulink.clothpicker.domain;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by ayush on 23/4/17.
 */

public class GetSuggestions {

    @SerializedName("suggestion")
    ArrayList<Suggestion> suggestions;

    public ArrayList<Suggestion> getSuggestions() {
        return suggestions;
    }

    public void setSuggestions(ArrayList<Suggestion> suggestions) {
        this.suggestions = suggestions;
    }
}
