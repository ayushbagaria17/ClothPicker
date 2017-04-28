package in.agrostar.ulink.clothpicker.models;

import android.content.Context;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import in.agrostar.ulink.clothpicker.Constants;
import in.agrostar.ulink.clothpicker.domain.Suggestion;
import in.agrostar.ulink.clothpicker.domain.Suggestion1;
import in.agrostar.ulink.clothpicker.domain.SuggestionPool;
import in.agrostar.ulink.clothpicker.utils.GsonUtils;
import in.agrostar.ulink.clothpicker.utils.SharedPreferencesUtil;
import rx.Observable;

/**
 * Created by ayush on 23/4/17.
 */

public class ClothPickerModel extends BaseModel {

    WeakReference<Context> contextWeakReference;

    public ClothPickerModel(Context context) {
        this.contextWeakReference = new WeakReference<Context>(context);
    }

    public List<Suggestion1> getSuggestions() {
        if (contextWeakReference.get()!= null) {
            SuggestionPool suggestionPool = GsonUtils.getObjectFromJson(SharedPreferencesUtil.getInstance(contextWeakReference.get()).getData(Constants.KEY.SUGGESTION_POOL, null), SuggestionPool.class);
            return suggestionPool != null ? suggestionPool.getSuggestionList() : null;
            }
        return null;
    }

    public Void saveSuggestions(HashMap<Integer, Suggestion1> suggestions) {
        if (contextWeakReference.get()!= null) {
            SuggestionPool suggestionPool = GsonUtils.getObjectFromJson(SharedPreferencesUtil.getInstance(contextWeakReference.get()).getData(Constants.KEY.SUGGESTION_POOL, null), SuggestionPool.class);
            List<Suggestion1> allSuggestions = suggestionPool.getSuggestionList();
            ArrayList<Suggestion1> newSuggestionList = new ArrayList<>();
            for (Suggestion1 suggestion : allSuggestions) {

                if (suggestions.containsKey(suggestion.getId())) {
                    Suggestion1 tempSuggestion = suggestions.get(suggestion.getId());
                    suggestion.setFlag(tempSuggestion.isFlag());
                    suggestion.setAlreadySeen(tempSuggestion.isAlreadySeen());
                }
                newSuggestionList.add(suggestion);
            }
            suggestionPool.setSuggestionList(newSuggestionList);
            String suggestions1  = GsonUtils.getJsonString(suggestionPool);
            SharedPreferencesUtil.getInstance(contextWeakReference.get()).saveData(Constants.KEY.SUGGESTION_POOL,suggestions1);
        }
        return null;
    }
}
