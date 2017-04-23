package in.agrostar.ulink.clothpicker.presenters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import in.agrostar.ulink.clothpicker.domain.Suggestion;
import in.agrostar.ulink.clothpicker.models.ClothPickerModel;
import in.agrostar.ulink.clothpicker.presenters.interfaces.IClothPickerFragmentPresenter;
import in.agrostar.ulink.clothpicker.ui.fragment.interfaces.IClothPickerFragment;
import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by ayush on 23/4/17.
 */

public class ClothPickerFragmentPresenter implements IClothPickerFragmentPresenter {
    
   IClothPickerFragment view;
    private CompositeSubscription subscriptions;
    ClothPickerModel model;

    @Override
    public void attachView(IClothPickerFragment view) {
        this.view = view;
        subscriptions = new CompositeSubscription();
        model = new ClothPickerModel(view.getContext());
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
                        return !suggestion.isHasAlreadySeen();
                    }
                }).subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Suggestion>() {
                    HashMap<Integer,Suggestion> suggestions = new  HashMap<Integer,Suggestion>();
                    int position = 0;
                    @Override
                    public void onCompleted() {
                        view.pushData(suggestions);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Suggestion suggestion) {

                        suggestions.put(position,suggestion);
                        position++;
                    }
                });
        subscriptions.add(subscription);
    }

    @Override
    public void saveSuggestions(HashMap<Integer, Suggestion> suggestions) {
        if (suggestions != null) {
            Subscription subscription = Observable.just(model.saveSuggestions(suggestions))
                    .observeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe();
        }
    }
}
