package in.agrostar.ulink.clothpicker.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import in.agrostar.ulink.clothpicker.R;
import in.agrostar.ulink.clothpicker.ui.activity.interfaces.IBaseUI;
import in.agrostar.ulink.clothpicker.ui.activity.interfaces.IHomeView;
import in.agrostar.ulink.clothpicker.ui.fragment.BookMarkFragment;
import in.agrostar.ulink.clothpicker.ui.fragment.ClothPickerFragment;
import in.agrostar.ulink.clothpicker.ui.fragment.HomeFragment;

public class HomeActivity extends AppCompatActivity implements IHomeView, IBaseUI {

    private TextView mTextMessage;

    @InjectView(R.id.navigation)
    BottomNavigationView navigation;

    @InjectView(R.id.content)
    FrameLayout content;
    private Fragment selectedFragment;

    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        return intent;
    }


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    selectedFragment = HomeFragment.newInstance();
                    break;
                case R.id.navigation_dashboard:
                    selectedFragment = ClothPickerFragment.newInstance();
                   break;
                case R.id.navigation_notifications:
                    selectedFragment = BookMarkFragment.newInstance();
                   break;
            }

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.content, selectedFragment);
            transaction.commit();
            return true;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.inject(this);
        init();

    }

    @Override
    public void showProgressBar() {

    }

    @Override
    public void hideProgressBar() {

    }

    @Override
    public Context getContext() {
        return null;
    }

    @Override
    public void showErrorDialog(String string) {

    }

    @Override
    public void init() {
        initView();
        initPresenter();

    }

    @Override
    public void initView() {
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        selectedFragment = HomeFragment.newInstance();
        transaction.replace(R.id.content, selectedFragment);
        transaction.commit();
    }

    @Override
    public void initPresenter() {

    }


}
