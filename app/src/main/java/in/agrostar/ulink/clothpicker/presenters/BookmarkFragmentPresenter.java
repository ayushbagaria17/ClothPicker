package in.agrostar.ulink.clothpicker.presenters;

import android.graphics.Bitmap;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import in.agrostar.ulink.clothpicker.domain.Suggestion;
import in.agrostar.ulink.clothpicker.models.BookmarkModel;
import in.agrostar.ulink.clothpicker.presenters.interfaces.IBookmarkPresenter;
import in.agrostar.ulink.clothpicker.ui.fragment.interfaces.IBookmarkFragment;
import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by ayush on 23/4/17.
 */

public class BookmarkFragmentPresenter implements IBookmarkPresenter {


    private IBookmarkFragment view;
    private BookmarkModel model;
    private CompositeSubscription subscriptions;

    @Override
    public void attachView(IBookmarkFragment view) {
        this.view = view;
        this.model = new BookmarkModel(view.getContext());
        this.subscriptions = new CompositeSubscription();
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
    public void getData() {
        Subscription subscription = Observable.from(model.getSuggestions())
                .filter(new Func1<Suggestion, Boolean>() {
                    @Override
                    public Boolean call(Suggestion suggestion) {
                        return suggestion.isHasAlreadySeen() && suggestion.isFlag();
                    }
                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Suggestion>() {
                    ArrayList<Suggestion> suggestions = new ArrayList<Suggestion>();
                    @Override
                    public void onCompleted() {
                        view.showData(suggestions);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Suggestion suggestion) {
                        suggestions.add(suggestion);
                    }
                });
        subscriptions.add(subscription);
    }

    @Override
    public void shareImage(String imageUrl) {
        Observable.just(imageUrl)
                .subscribeOn(Schedulers.computation())
                .flatMap(new Func1<String, Observable<?>>() {
                    @Override
                    public Observable<Bitmap> call(String s) {
                        return Observable.just(getBitmap(s));
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Object>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Object o) {
                        if (o != null && o instanceof Bitmap)
                        view.shareBitmap((Bitmap)o);
                    }
                });
    }

    private Bitmap getBitmap(String imageUrl) {
        try {
            return Glide.
                    with(getApplicationContext()).
                    load(imageUrl).
                    asBitmap().
                    into(100, 100).
                    get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return  null;
    }
}
