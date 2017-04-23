package in.agrostar.ulink.clothpicker.models;

import android.content.Context;

import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferType;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.List;

import in.agrostar.ulink.clothpicker.Constants;
import in.agrostar.ulink.clothpicker.utils.TransferUtil;
import rx.Observable;

/**
 * Created by ayush on 21/4/17.
 */

public class HomeFragmentModel extends BaseModel {

    private final TransferUtility transferUtility;
    WeakReference<Context> contextWeakReference;

    public HomeFragmentModel(Context context) {
        this.contextWeakReference = new WeakReference<Context>(context);
        transferUtility = TransferUtil.getTransferUtility(context);
    }

    public TransferObserver uploadFile(File file) {

        //can use observer but right now here now doing anything with it.
        TransferObserver observer = transferUtility.upload(Constants.VALUES.BUCKET_NAME, file.getName(),
                file);
        return observer;
    }

    public void pushFileToBackend(){
        //save file at the backend
    }

    public List<TransferObserver> getUploads() {
        return transferUtility.getTransfersWithType(TransferType.UPLOAD);
    }

    public void delete(int id) {
        transferUtility.deleteTransferRecord(id);
    }

    public TransferObserver resume(int id) {
        return transferUtility.resume(id);
    }
}
