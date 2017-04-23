package in.agrostar.ulink.clothpicker.presenters.interfaces;

import in.agrostar.ulink.clothpicker.ui.fragment.interfaces.IHomeFragment;

/**
 * Created by ayush on 20/4/17.
 */

public interface IHomeFragmentPresenter extends IBasePresenter<IHomeFragment> {
    void uploadImage(String captureImagePath);

    void deleteUpload(int id,int position);

    void resumeUpload(int id);
}
