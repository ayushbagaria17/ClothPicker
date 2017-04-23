package in.agrostar.ulink.clothpicker.presenters;

import in.agrostar.ulink.clothpicker.Constants;
import in.agrostar.ulink.clothpicker.models.LoginModel;
import in.agrostar.ulink.clothpicker.presenters.interfaces.ILoginPresenter;
import in.agrostar.ulink.clothpicker.ui.activity.interfaces.ILoginView;
import in.agrostar.ulink.clothpicker.utils.SharedPreferencesUtil;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by ayush on 20/4/17.
 */

public class LoginPresenter implements ILoginPresenter{
    ILoginView view;
    CompositeSubscription subscriptions;
    private SharedPreferencesUtil preferences;
    LoginModel model;

    @Override
    public void attachView(ILoginView view) {
        this.view = view;
        model = new LoginModel(view.getContext());
        subscriptions = new CompositeSubscription();
        preferences = SharedPreferencesUtil.getInstance(view.getContext());
    }

    @Override
    public void detachView() {
        this.view = null;
    }

    @Override
    public void rxUnsubscribe() {
        if (subscriptions != null && !subscriptions.isUnsubscribed()) {
            subscriptions.unsubscribe();
        }
    }


    @Override
    public void getUserBasedOnLogin(String accessToken, String loginSource) {
        //Send token to backend
        preferences.saveData(Constants.IS_REGISTRATION_DONE,true);
        preferences.saveData(Constants.KEY.USER_TOKEN, accessToken);
        preferences.saveData(Constants.KEY.LOGIN_SOURCE, Constants.KEY.FACEBOOK);
        Subscription subscription = Observable.just(model.createSuggestions())
                .subscribe();
        subscriptions.add(subscription);
        registrationDone();
    }

    @Override
    public void upadeUserFbToken(String newAccessTokenKey, String oldAccessTokenKey, String facebook) {
        //update token at the backend
        preferences.saveData(Constants.KEY.userName, newAccessTokenKey);
        preferences.saveData(Constants.KEY.LOGIN_SOURCE, Constants.KEY.FACEBOOK);
        registrationDone();
    }

    @Override
    public void registrationDone() {


        view.goToHome();
    }
}
