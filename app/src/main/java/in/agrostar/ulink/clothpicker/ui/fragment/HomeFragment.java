package in.agrostar.ulink.clothpicker.ui.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.ComponentName;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import in.agrostar.ulink.clothpicker.BuildConfig;
import in.agrostar.ulink.clothpicker.R;
import in.agrostar.ulink.clothpicker.presenters.HomeFragmentPresenter;
import in.agrostar.ulink.clothpicker.ui.activity.HomeActivity;
import in.agrostar.ulink.clothpicker.ui.activity.interfaces.IBaseUI;
import in.agrostar.ulink.clothpicker.ui.adapter.UploadPicAdapter;
import in.agrostar.ulink.clothpicker.ui.fragment.interfaces.IHomeFragment;
import in.agrostar.ulink.clothpicker.utils.Logger;
import in.agrostar.ulink.clothpicker.utils.PermissionUtils;

/**
 * Created by ayush on 20/4/17.
 */

public class HomeFragment extends BaseFragment implements IBaseUI, IHomeFragment, UploadPicAdapter.UploadPicAdapterListener
{
    private static final int REQUEST_CODE_ASK_PERMISSIONS = 1 ;
    private static final int REQUEST_CODE_GET_IMAGE =2;
    HomeFragmentPresenter presenter;
    private ArrayList<HashMap<String, Object>> transferRecordMaps;
    private List<TransferObserver> observers;
    private LinearLayoutManager layoutManager;
    private UploadPicAdapter adapter;
    private String tempFilePath;

    public static Fragment newInstance() {
        Bundle args = new Bundle();
        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @InjectView(R.id.fab)
    FloatingActionButton fab;



    @InjectView(R.id.rv_upload_pic)
    RecyclerView recylerView;

    Context context;
    String captureImagePath;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home,container, false);
        ButterKnife.inject(this,view);
        init();
        return view;
    }

    @Override
    public void init() {
        initPresenter();
        initView();
        initPermissions();
    }

    private void initPermissions() {
        if (!haveReadAndWriteExteranlStoragePermissions()) {
            List<String> permissionList = new ArrayList<String>();
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            permissionList.add(Manifest.permission.READ_EXTERNAL_STORAGE);
            permissionList.add(Manifest.permission.CAMERA);
            PermissionUtils.askForPermissions(REQUEST_CODE_ASK_PERMISSIONS, context, permissionList);
        }
    }

    private boolean haveReadAndWriteExteranlStoragePermissions() {
        return (ActivityCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) && (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
    }

    @OnClick(R.id.fab)
    void addPicture() {

    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS: {
                Map<String, Integer> perms = new HashMap<String, Integer>();
                perms.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.READ_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.CAMERA, PackageManager.PERMISSION_GRANTED);
                for (int i = 0; i < permissions.length; i++) {
                    perms.put(permissions[i], grantResults[i]);
                }
                if (perms.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                        perms.get(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    // All Permissions Granted
                } else {
                    // Permission Denied
                    getActivity().finish();
                }
                break;
            }
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public void initView() {
        layoutManager = new LinearLayoutManager(context);
        recylerView.setLayoutManager(layoutManager);
        adapter = new UploadPicAdapter(context, this);
        recylerView.setAdapter(adapter);
    }


    @Override
    public void initPresenter() {
        presenter = new HomeFragmentPresenter();
        presenter.attachView(this);
    }

    @Override
    public void showProgressBar() {

    }

    @Override
    public void hideProgressBar() {

    }

    @Override
    public Context getContext() {
        return context;
    }

    @Override
    public void showErrorDialog(String string) {

    }


    @OnClick(R.id.fab)
    void getImageFromCameraOrGallery() {
        startActivityForResult(getPickImageChooserIntent(), REQUEST_CODE_GET_IMAGE);
    }

    public Intent getPickImageChooserIntent() {

        // Determine Uri of camera image to save.
        Uri outputFileUri = null;
        try {
            outputFileUri = getCaptureImageOutputUri();
        } catch (IOException e) {
            e.printStackTrace();
        }

        ArrayList<Intent> allIntents = new ArrayList();
        PackageManager packageManager = context.getPackageManager();

        // collect all camera intents
        Intent captureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        ArrayList<ResolveInfo> listCam = (ArrayList<ResolveInfo>) packageManager.queryIntentActivities(captureIntent, 0);
        for (ResolveInfo res : listCam) {
            Intent intent = new Intent(captureIntent);
            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            intent.setPackage(res.activityInfo.packageName);
            if (outputFileUri != null) {
                intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
            }
            allIntents.add(intent);
        }

        // collect all gallery intents
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        ArrayList<ResolveInfo> listGallery = (ArrayList<ResolveInfo>) packageManager.queryIntentActivities(galleryIntent, 0);
        for (ResolveInfo res : listGallery) {
            Intent intent = new Intent(galleryIntent);
            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            intent.setPackage(res.activityInfo.packageName);
            allIntents.add(intent);
        }

        // the main intent is the last in the list (fucking android) so pickup the useless one
        Intent mainIntent = allIntents.get(allIntents.size() - 1);
        for (Intent intent : allIntents) {
            if (intent.getComponent().getClassName().equals("com.android.documentsui.DocumentsActivity")) {
                mainIntent = intent;
                break;
            }
        }
        allIntents.remove(mainIntent);

        // Create a chooser from the main intent
        Intent chooserIntent = Intent.createChooser(mainIntent, "Select source");

        // Add all other intents
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, allIntents.toArray(new Parcelable[allIntents.size()]));

        return chooserIntent;
    }

    public Uri getPickImageResultUri(Intent data) throws IOException {
        boolean isCamera = true;
        if (data != null) {
            String action = data.getAction();
            isCamera = action != null && action.equals(MediaStore.ACTION_IMAGE_CAPTURE);
        }


        return isCamera ? getCaptureImageOutputUri() : data.getData();
    }


    private File createImageFile() throws IOException {
        // Create an image file name
        String imageFileName = "image" + new Date().getTime();
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DCIM),  Environment.DIRECTORY_PICTURES);
        if (!storageDir.exists())
            storageDir.mkdirs();
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        captureImagePath = "file:" + image.getAbsolutePath();
        return image;
    }


    private Uri getCaptureImageOutputUri() throws IOException {
        Uri outputFileUri = null;
        File getImage = context.getExternalCacheDir();
        File file;
        if (getImage != null) {
            file = createImageFile();
            outputFileUri = Uri.fromFile(file);
            captureImagePath = "file:" + file.getAbsolutePath();
        }
        try {
            captureImagePath = getPath(outputFileUri);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return outputFileUri;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            try {
                if (getPickImageResultUri(data) != null) {
                    Uri picUri = getPickImageResultUri(data);
                    try {
                        captureImagePath = getPath(picUri);
                        Logger.logError(captureImagePath);
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }
                } else {
                    Logger.logError(captureImagePath);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            Uri imageUri = Uri.parse(captureImagePath);
            FileOutputStream out = null;
            try {
                Bitmap bmp = BitmapFactory.decodeFile(imageUri.getPath());
                out = new FileOutputStream(imageUri.getPath());
                //write the compressed bitmap at the destination specified by filename.
                bmp.compress(Bitmap.CompressFormat.JPEG, 40, out);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            tempFilePath = captureImagePath;
        }

    }

    private String getPath(Uri uri) throws URISyntaxException {
        final boolean needToCheckUri = Build.VERSION.SDK_INT >= 19;
        String selection = null;
        String[] selectionArgs = null;
        // Uri is different in versions after KITKAT (Android 4.4), we need to
        // deal with different Uris.
        if (needToCheckUri && DocumentsContract.isDocumentUri(context, uri)) {
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                return Environment.getExternalStorageDirectory() + "/" + split[1];
            } else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                uri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
            } else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                if ("image".equals(type)) {
                    uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                selection = "_id=?";
                selectionArgs = new String[] {
                        split[1]
                };
            }
        }
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = {
                    MediaStore.Images.Media.DATA
            };
            Cursor cursor = null;
            try {
                cursor = context.getContentResolver()
                        .query(uri, projection, selection, selectionArgs, null);
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index);
                }
            } catch (Exception e) {
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }


    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }


    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }


    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    @Override
    public void onResume() {
        super.onResume();
        if (tempFilePath != null) {
            presenter.uploadImage(tempFilePath);
            tempFilePath = null;
        }
        initData();
    }

    private void initData() {
        presenter.getData();
    }

    @Override
    public void setData(ArrayList<HashMap<String, Object>> transferRecordMaps) {
        this.transferRecordMaps = transferRecordMaps;
        adapter.setUploadList(transferRecordMaps);
    }
    @Override
    public void notifyAdapter() {
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(HashMap<String, Object> item, int position) {
        if (item.get("state").toString().equalsIgnoreCase(TransferState.COMPLETED.toString())|| item.get("state").toString().equalsIgnoreCase(TransferState.FAILED.toString())) {

            presenter.deleteUpload((Integer) item.get("id"),position);
            adapter.notifyItemRemoved(position);
        } else {
            presenter.resumeUpload((Integer)item.get("id"));
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.detachView();
        presenter.rxUnsubscribe();
    }
}
