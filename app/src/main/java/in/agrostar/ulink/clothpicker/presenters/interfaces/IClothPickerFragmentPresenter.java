package in.agrostar.ulink.clothpicker.presenters.interfaces;

import java.util.HashMap;

import in.agrostar.ulink.clothpicker.ui.fragment.interfaces.IClothPickerFragment;
import in.agrostar.ulink.clothpicker.domain.Suggestion;

/**
 * Created by ayush on 23/4/17.
 */

public interface IClothPickerFragmentPresenter extends IBasePresenter<IClothPickerFragment>{
    void getData();

    void saveSuggestions(HashMap<Integer, Suggestion> suggestions);
}
