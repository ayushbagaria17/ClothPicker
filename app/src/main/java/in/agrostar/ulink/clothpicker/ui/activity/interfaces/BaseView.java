package in.agrostar.ulink.clothpicker.ui.activity.interfaces;

import android.content.Context;

/**
 * Created by ayush on 20/4/17.
 */


public interface BaseView {
    void showProgressBar();

    void hideProgressBar();

    Context getContext();

    void showErrorDialog(String string);

}