package in.agrostar.ulink.clothpicker.models;

import android.content.Context;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import in.agrostar.ulink.clothpicker.Constants;
import in.agrostar.ulink.clothpicker.domain.GetSuggestions;
import in.agrostar.ulink.clothpicker.domain.Suggestion;
import in.agrostar.ulink.clothpicker.utils.GsonUtils;
import in.agrostar.ulink.clothpicker.utils.SharedPreferencesUtil;

/**
 * Created by ayush on 23/4/17.
 */

public class LoginModel extends BaseModel {

    WeakReference<Context> contextWeakReference;

    public LoginModel(Context context) {
        this.contextWeakReference = new WeakReference<Context>(context);
    }

    public Void createSuggestions() {
        if (contextWeakReference.get() != null) {
            ArrayList<Suggestion> suggestions = new ArrayList<>();
            for (int i =0; i<99; i++) {
                Suggestion tempSuggestion  = new Suggestion();
                tempSuggestion.setId(i);
                tempSuggestion.setFlag(false);
                tempSuggestion.setDescription("Image No." + i);
                tempSuggestion.setHasAlreadySeen(false);
                tempSuggestion.setImageUrl("https://s3.amazonaws.com/sampleproject-userfiles-mobilehub-636799597/Screenshot_20170423-080536.png");
                suggestions.add(tempSuggestion);
            }
            GetSuggestions getSuggestions = new GetSuggestions();
            getSuggestions.setSuggestions(suggestions);
            String suggestions1  = GsonUtils.getJsonString(getSuggestions);
            SharedPreferencesUtil.getInstance(contextWeakReference.get()).saveData(Constants.SUGGESTIONS,suggestions1);

        }

        return null;
    }
}
