package in.agrostar.ulink.clothpicker.presenters.interfaces;

import in.agrostar.ulink.clothpicker.ui.activity.interfaces.ILoginView;

/**
 * Created by ayush on 20/4/17.
 */

public interface ILoginPresenter extends IBasePresenter<ILoginView> {
    void getUserBasedOnLogin(String accessToken, String loginSource);

    void upadeUserFbToken(String newAccessTokenKey, String oldAccessTokenKey, String facebook);

    void registrationDone();
}
