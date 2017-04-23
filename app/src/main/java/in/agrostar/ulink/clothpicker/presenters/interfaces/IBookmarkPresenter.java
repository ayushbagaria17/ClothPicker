package in.agrostar.ulink.clothpicker.presenters.interfaces;

import in.agrostar.ulink.clothpicker.ui.fragment.interfaces.IBookmarkFragment;

/**
 * Created by ayush on 23/4/17.
 */

public interface IBookmarkPresenter extends IBasePresenter<IBookmarkFragment> {
    void getData();

    void shareImage(String imageUrl);
}
