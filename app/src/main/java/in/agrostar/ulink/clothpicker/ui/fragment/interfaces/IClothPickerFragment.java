package in.agrostar.ulink.clothpicker.ui.fragment.interfaces;

import java.util.HashMap;

import in.agrostar.ulink.clothpicker.domain.Suggestion;
import in.agrostar.ulink.clothpicker.ui.activity.interfaces.BaseView;

/**
 * Created by ayush on 23/4/17.
 */

public interface IClothPickerFragment extends BaseView {
    void pushData( HashMap<Integer,Suggestion> suggestions);
}
