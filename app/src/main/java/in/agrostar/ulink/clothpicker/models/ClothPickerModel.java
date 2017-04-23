package in.agrostar.ulink.clothpicker.models;

import android.content.Context;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import in.agrostar.ulink.clothpicker.Constants;
import in.agrostar.ulink.clothpicker.domain.GetSuggestions;
import in.agrostar.ulink.clothpicker.domain.Suggestion;
import in.agrostar.ulink.clothpicker.utils.GsonUtils;
import in.agrostar.ulink.clothpicker.utils.SharedPreferencesUtil;

/**
 * Created by ayush on 23/4/17.
 */

public class ClothPickerModel extends BaseModel {

    WeakReference<Context> contextWeakReference;

    public ClothPickerModel(Context context) {
        this.contextWeakReference = new WeakReference<Context>(context);
    }

    public ArrayList<Suggestion> getSuggestions() {
        if (contextWeakReference.get()!= null) {
            GetSuggestions getSuggestions = GsonUtils.getObjectFromJson(SharedPreferencesUtil.getInstance(contextWeakReference.get()).getData(Constants.SUGGESTIONS, null), GetSuggestions.class);
            return getSuggestions.getSuggestions();
            }
        return null;
    }

    public Void saveSuggestions(HashMap<Integer, Suggestion> suggestions) {
        if (contextWeakReference.get()!= null) {
            GetSuggestions getSuggestions = GsonUtils.getObjectFromJson(SharedPreferencesUtil.getInstance(contextWeakReference.get()).getData(Constants.SUGGESTIONS, null), GetSuggestions.class);
            ArrayList<Suggestion> allSuggestions = getSuggestions.getSuggestions();
            ArrayList<Suggestion> newSuggestionList = new ArrayList<>();
            for (Suggestion suggestion : allSuggestions) {

                if (suggestions.containsKey(suggestion.getId())) {
                    Suggestion tempSuggestion = suggestions.get(suggestion.getId());
                    suggestion.setFlag(tempSuggestion.isFlag());
                    suggestion.setHasAlreadySeen(tempSuggestion.isHasAlreadySeen());
                }
                newSuggestionList.add(suggestion);
            }
            getSuggestions.setSuggestions(newSuggestionList);
            String suggestions1  = GsonUtils.getJsonString(getSuggestions);
            SharedPreferencesUtil.getInstance(contextWeakReference.get()).saveData(Constants.SUGGESTIONS,suggestions1);
        }
        return null;
    }
}
