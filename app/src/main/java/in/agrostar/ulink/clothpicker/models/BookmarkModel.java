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

public class BookmarkModel {
    WeakReference<Context> contextWeakReference;

    public BookmarkModel(Context context) {
        contextWeakReference = new WeakReference<Context>(context);
    }

    public ArrayList<Suggestion> getSuggestions() {
        if (contextWeakReference.get()!= null) {
            GetSuggestions getSuggestions = GsonUtils.getObjectFromJson(SharedPreferencesUtil.getInstance(contextWeakReference.get()).getData(Constants.SUGGESTIONS, null), GetSuggestions.class);
            return getSuggestions.getSuggestions();
        }
        return null;
    }
}
