package in.agrostar.ulink.clothpicker.ui.fragment.interfaces;

import java.util.ArrayList;
import java.util.HashMap;

import in.agrostar.ulink.clothpicker.ui.activity.interfaces.BaseView;

/**
 * Created by ayush on 20/4/17.
 */

public interface IHomeFragment extends BaseView{
    void setData(ArrayList<HashMap<String, Object>> transferRecordMaps);
    void notifyAdapter();
}
