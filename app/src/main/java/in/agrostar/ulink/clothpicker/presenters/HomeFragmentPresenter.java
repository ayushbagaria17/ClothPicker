package in.agrostar.ulink.clothpicker.presenters;

import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferType;
import com.google.android.gms.tasks.Task;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import in.agrostar.ulink.clothpicker.R;
import in.agrostar.ulink.clothpicker.domain.UploadType;
import in.agrostar.ulink.clothpicker.models.HomeFragmentModel;
import in.agrostar.ulink.clothpicker.presenters.interfaces.IHomeFragmentPresenter;
import in.agrostar.ulink.clothpicker.ui.fragment.interfaces.IHomeFragment;
import in.agrostar.ulink.clothpicker.utils.TransferUtil;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by ayush on 20/4/17.
 */

public class HomeFragmentPresenter implements IHomeFragmentPresenter, TransferListener {

    HomeFragmentModel model;
    private IHomeFragment view;
    CompositeSubscription subscriptions;
    private ArrayList<HashMap<String, Object>> transferRecordMaps;
    private List<TransferObserver> observers;

    @Override
    public void attachView(IHomeFragment view) {
        this.view = view;
        subscriptions = new CompositeSubscription();
        model  = new HomeFragmentModel(view.getContext());
        transferRecordMaps = new ArrayList<>();


    }

    @Override
    public void detachView() {
        this.view = null;
    }

    @Override
    public void rxUnsubscribe() {
        if (subscriptions != null) {
            if (!subscriptions.isUnsubscribed()) {
                subscriptions.unsubscribe();
            }
        }
    }

    @Override
    public void onStateChanged(int id, TransferState state) {
        if (state.equals(TransferState.COMPLETED)) {
            //push url to backend
            model.updateUpload(id);
           model.pushFileToBackend();
        }
       updateList(id);
    }

    @Override
    public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
        updateList(id);
    }

    @Override
    public void onError(int id, Exception ex) {
        updateList(id);
    }

    @Override
    public void uploadImage(String filePath, UploadType type) {
        if (filePath == null) {
            //TODO handle this properly
            view.showErrorDialog(view.getContext().getString(R.string.error_try_again));
            return;
        }
        /*
         * Upload file using amazon sdk
         */
        File file = new File(filePath);
        TransferObserver observer = model.uploadFile(file);
        observer.setTransferListener(this);
        model.addUpload(observer, type);

    }

    @Override
    public void deleteUpload(int id, int position) {

        model.delete(id);
        observers.remove(position);
        transferRecordMaps.remove(position);
        updateList(id);
    }

    @Override
    public void resumeUpload(int id) {
        model.resume(id).setTransferListener(this);
    }

    @Override
    public void onResume() {
        model.getUploadMap();
//        Subscription subscription = Observable.just(model.getUploadMap())
//                .subscribeOn(Schedulers.computation())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe();
//        subscriptions.add(subscription);
    }

    private void updateList(int id) {
        TransferObserver observer = null;
        HashMap<String, Object> map = null;
        for (int i = 0; i < observers.size(); i++) {
            observer = observers.get(i);
            map = transferRecordMaps.get(i);
            TransferUtil.fillMap(map, observer, false);
        }
        if (view != null)
        view.notifyAdapter();
    }



    public void getData() {
        transferRecordMaps.clear();
        // Use TransferUtility to get all upload transfers.
        observers = model.getUploads();
        for (TransferObserver observer : observers) {

            // For each transfer we will will create an entry in
            // transferRecordMaps which will display
            // as a single row in the UI
            HashMap<String, Object> map = new HashMap<String, Object>();
            TransferUtil.fillMap(map, observer, false);
            transferRecordMaps.add(map);

            // Sets listeners to in progress transfers
            if (TransferState.WAITING.equals(observer.getState())
                    || TransferState.WAITING_FOR_NETWORK.equals(observer.getState())
                    || TransferState.IN_PROGRESS.equals(observer.getState())) {
                observer.setTransferListener(this);
            }

            view.setData(transferRecordMaps);
        }
    }
}
