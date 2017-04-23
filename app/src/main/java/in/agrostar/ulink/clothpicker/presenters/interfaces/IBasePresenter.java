package in.agrostar.ulink.clothpicker.presenters.interfaces;

/**
 * Created by ayush on 20/4/17.
 */
public interface IBasePresenter<V> {

    void attachView(V view);
    void detachView();
    void rxUnsubscribe();
}
