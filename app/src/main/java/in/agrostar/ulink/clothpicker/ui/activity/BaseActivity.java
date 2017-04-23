package in.agrostar.ulink.clothpicker.ui.activity;

import android.app.ProgressDialog;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

/**
 * Created by ayush on 20/4/17.
 */

public class BaseActivity extends AppCompatActivity {
    public ProgressDialog progress;

    public synchronized void showProgress() {
       /* if (!progress.isAdded()) {
            progress.show(getFragmentManager(), null);
        }*/
        progress =  new ProgressDialog(this);
        progress.setMessage("Loading...");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setCancelable(false);
        progress.show();

    }

    public synchronized void hideProgress() {
       /* if (progress != null && progress.getActivity() != null) {
            progress.dismissAllowingStateLoss();
        }*/
        if (progress != null && progress.isShowing()) {
            progress.dismiss();
            progress = null;
        }


    }

    protected void toast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public void setupToolBar(Toolbar toolbar, String title, Drawable drawable) {
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
        getSupportActionBar().setIcon(null);
        if (title != null)
            getSupportActionBar().setTitle(title);
        getSupportActionBar().invalidateOptionsMenu();

    }




}