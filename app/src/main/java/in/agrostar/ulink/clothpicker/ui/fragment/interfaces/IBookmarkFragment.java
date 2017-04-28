package in.agrostar.ulink.clothpicker.ui.fragment.interfaces;

import android.graphics.Bitmap;

import java.util.ArrayList;

import in.agrostar.ulink.clothpicker.domain.Suggestion;
import in.agrostar.ulink.clothpicker.domain.Suggestion1;
import in.agrostar.ulink.clothpicker.ui.activity.interfaces.BaseView;

/**
 * Created by ayush on 23/4/17.
 */

public interface IBookmarkFragment extends BaseView {
    void showData(ArrayList<Suggestion1> suggestions);

    void shareBitmap(Bitmap bitmap);
}
