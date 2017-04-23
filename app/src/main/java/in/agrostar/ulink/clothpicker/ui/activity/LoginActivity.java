package in.agrostar.ulink.clothpicker.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import in.agrostar.ulink.clothpicker.Constants;
import in.agrostar.ulink.clothpicker.R;
import in.agrostar.ulink.clothpicker.presenters.LoginPresenter;
import in.agrostar.ulink.clothpicker.ui.activity.interfaces.IBaseUI;
import in.agrostar.ulink.clothpicker.ui.activity.interfaces.ILoginView;
import in.agrostar.ulink.clothpicker.utils.Logger;
import in.agrostar.ulink.clothpicker.utils.SharedPreferencesUtil;


/**
 * Created by ayush on 20/4/17.
 */

public class LoginActivity extends BaseActivity implements IBaseUI, GoogleApiClient.OnConnectionFailedListener, ILoginView {

    @InjectView(R.id.fb_login_button)
    LoginButton fbLoginBtn;

    @InjectView(R.id.google_sign_in_button)
    SignInButton googleLoginBtn;


    CallbackManager mFacebookCallbackManager;
    private GoogleApiClient mGoogleApiClient;
    private static final int RC_SIGN_IN = 9001;
    SharedPreferencesUtil preferences;
    private AccessTokenTracker mFacebookAccessTokenTracker;
    private LoginPresenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        makeFullScreen();
        setContentView(R.layout.activity_login);
        ButterKnife.inject(this);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        preferences = SharedPreferencesUtil.getInstance(this);
        init();

    }

    @Override
    public void init() {
        initPresenter();
        initView();

    }

    private void makeFullScreen() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }


    @Override
    public void initView() {
        if (preferences.getData(Constants.IS_REGISTRATION_DONE,false)) {
            fbLoginBtn.setVisibility(View.GONE);
            googleLoginBtn.setVisibility(View.GONE);
        }

        FacebookSdk.sdkInitialize(getApplicationContext());
        mFacebookCallbackManager = CallbackManager.Factory.create();
        mFacebookAccessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
                presenter.upadeUserFbToken(currentAccessToken.ACCESS_TOKEN_KEY, oldAccessToken.ACCESS_TOKEN_KEY, Constants.KEY.FACEBOOK);
            }
        };
        fbLoginBtn.setReadPermissions("email");
        fbLoginBtn.registerCallback(mFacebookCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Logger.logError(loginResult.toString());
                presenter.getUserBasedOnLogin(loginResult.getAccessToken().ACCESS_TOKEN_KEY, Constants.KEY.FACEBOOK);
            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
            }
        });
    }

    @Override
    public void initPresenter() {
        presenter = new LoginPresenter();
        presenter.attachView(this);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        } else {
            mFacebookCallbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            GoogleSignInAccount acct = result.getSignInAccount();
            presenter.getUserBasedOnLogin(acct.getIdToken(), Constants.KEY.GOOGLE);
        } else {

        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @OnClick(R.id.google_sign_in_button)
    public void googleSignInClick() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void showProgressBar() {

    }

    @Override
    public void hideProgressBar() {

    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void showErrorDialog(String string) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (preferences.getData(Constants.IS_REGISTRATION_DONE,false)) {
           presenter.registrationDone();
        }

    }

    @Override
    public void goToHome() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                goToActivty(HomeActivity.newIntent(LoginActivity.this));
            }
        }, 2000);
    }

    public void goToActivty(Intent intent) {
        startActivity(intent);
        finish();
    }
}
