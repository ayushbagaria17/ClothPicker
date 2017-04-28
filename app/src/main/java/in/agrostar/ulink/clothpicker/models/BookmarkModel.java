package in.agrostar.ulink.clothpicker.models;

import android.content.Context;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import in.agrostar.ulink.clothpicker.Constants;
import in.agrostar.ulink.clothpicker.domain.GetSuggestions;
import in.agrostar.ulink.clothpicker.domain.Suggestion;
import in.agrostar.ulink.clothpicker.domain.Suggestion1;
import in.agrostar.ulink.clothpicker.domain.SuggestionPool;
import in.agrostar.ulink.clothpicker.utils.GsonUtils;
import in.agrostar.ulink.clothpicker.utils.SharedPreferencesUtil;

/**
 * Created by ayush on 23/4/17.
 */

public class BookmarkModel {
    WeakReference<Context> contextWeakReference;

    public BookmarkModel(Context context) {
        contextWeakReference = new WeakReference<Context>(context);
    }

    public List<Suggestion1> getSuggestions() {
        if (contextWeakReference.get()!= null) {
            SuggestionPool suggestionPool = GsonUtils.getObjectFromJson(SharedPreferencesUtil.getInstance(contextWeakReference.get()).getData(Constants.KEY.SUGGESTION_POOL, null), SuggestionPool.class);
            return suggestionPool != null ? suggestionPool.getSuggestionList() : null;
        }
        return null;
    }
}
