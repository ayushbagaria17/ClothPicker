package in.agrostar.ulink.clothpicker.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import butterknife.ButterKnife;
import butterknife.InjectView;
import in.agrostar.ulink.clothpicker.R;
import in.agrostar.ulink.clothpicker.domain.Suggestion;
import in.agrostar.ulink.clothpicker.domain.Suggestion1;
import in.agrostar.ulink.clothpicker.domain.UploadObject;
import in.agrostar.ulink.clothpicker.presenters.BookmarkFragmentPresenter;
import in.agrostar.ulink.clothpicker.ui.activity.interfaces.IBaseUI;
import in.agrostar.ulink.clothpicker.ui.adapter.BookmarkAdapter;
import in.agrostar.ulink.clothpicker.ui.fragment.interfaces.IBookmarkFragment;
import in.agrostar.ulink.clothpicker.utils.Logger;

import static android.R.attr.bitmap;
import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by ayush on 23/4/17.
 */

public class BookMarkFragment extends BaseFragment implements IBookmarkFragment, IBaseUI, BookmarkAdapter.BookMarkAdapterClickListener {
    @InjectView(R.id.rv_bookmark)
    RecyclerView recylerView;
    private Context context;
    private LinearLayoutManager layoutManger;
    private BookmarkAdapter adapter;
    private BookmarkFragmentPresenter presenter;

    public static BookMarkFragment newInstance() {
        
        Bundle args = new Bundle();
        
        BookMarkFragment fragment = new BookMarkFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bookmark,container,false);
        ButterKnife.inject(this,view);
        init();
        return view;
    }

    @Override
    public void init() {
        initView();
        initPresenter();
    }

    @Override
    public void initView() {
        layoutManger = new LinearLayoutManager(context);
        adapter = new BookmarkAdapter(context,this);
        recylerView.setAdapter(adapter);
        recylerView.setLayoutManager(layoutManger);
    }

    @Override
    public void initPresenter() {
        presenter = new BookmarkFragmentPresenter();
        presenter.attachView(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.getData();
    }

    @Override
    public void showProgressBar() {

    }

    @Override
    public void hideProgressBar() {

    }

    @Override
    public void showErrorDialog(String string) {

    }

    @Override
    public void shareClick(Suggestion1 suggestion) {
        Logger.logError("Share Click");
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("image/*");
        ArrayList<Uri> files = new ArrayList<Uri>();
        for(UploadObject uploadObject : suggestion.getUploadObjects()) {
            File file = new File(uploadObject.getFilePath());
            Uri uri = Uri.fromFile(file);
            files.add(uri);
        }
        i.putParcelableArrayListExtra(Intent.EXTRA_STREAM, files);
        startActivity(Intent.createChooser(i, "Share Image"));

    }

    public void shareItem(String url) {

    }

    public Uri getLocalBitmapUri(Bitmap bmp) {
        Uri bmpUri = null;
        try {
            File file =  new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "share_image_" + System.currentTimeMillis() + ".png");
            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.close();
            bmpUri = Uri.fromFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bmpUri;
    }

    @Override
    public void showData(ArrayList<Suggestion1> suggestions) {
        adapter.setData(suggestions);
    }

    @Override
    public void shareBitmap(Bitmap bitmap) {
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("image/*");
        i.putExtra(Intent.EXTRA_STREAM, getLocalBitmapUri(bitmap));
        startActivity(Intent.createChooser(i, "Share Image"));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.detachView();
        presenter.rxUnsubscribe();
    }
}
