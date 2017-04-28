package in.agrostar.ulink.clothpicker.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.ButterKnife;
import butterknife.InjectView;
import in.agrostar.ulink.clothpicker.R;
import in.agrostar.ulink.clothpicker.domain.Suggestion;
import in.agrostar.ulink.clothpicker.domain.Suggestion1;
import in.agrostar.ulink.clothpicker.presenters.ClothPickerFragmentPresenter;
import in.agrostar.ulink.clothpicker.ui.activity.interfaces.IBaseUI;
import in.agrostar.ulink.clothpicker.ui.custom.CustomViewPager;
import in.agrostar.ulink.clothpicker.ui.fragment.interfaces.IClothPickerFragment;

/**
 * Created by ayush on 23/4/17.
 */

public class ClothPickerFragment extends  BaseFragment implements IClothPickerFragment, IBaseUI, SuggestionFragment.SuggestionFragmentListener {
    Context context;
    @InjectView(R.id.vp_picker)
    CustomViewPager viewPagerPicker;
    private ClothPickerFragmentPresenter presenter;
    private ViewPagerAdapter viewPagerAdapter;
    private HashMap<Integer, Suggestion1> suggestions;
    private HashMap<Integer, Suggestion1> tempSuggestions;


    public static ClothPickerFragment newInstance() {

        Bundle args = new Bundle();

        ClothPickerFragment fragment = new ClothPickerFragment();
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
        View view = inflater.inflate(R.layout.fragment_picker,container, false);
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
        viewPagerPicker.setPagingEnabled(false);
        viewPagerAdapter = new ViewPagerAdapter(getChildFragmentManager());
        viewPagerPicker.setAdapter(viewPagerAdapter);
    }

    @Override
    public void initPresenter() {
        presenter = new ClothPickerFragmentPresenter();
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
    public void pushData( HashMap<Integer,Suggestion1> suggestions) {
        this.suggestions = suggestions;
        if (suggestions.size() != 0 )
            viewPagerAdapter.update(suggestions);

    }

    @Override
    public void liked(int position) {
        updateSuggestion(true, position);
        if (suggestions.size() == position + 1) {
            viewPagerPicker.setVisibility(View.GONE);
        }
        viewPagerPicker.setCurrentItem(position+1);
    }

    private void updateSuggestion(boolean likeFlag, int position) {
        if (tempSuggestions == null){
            tempSuggestions = new HashMap<>();
        }
        Suggestion1 tempSuggestion = suggestions.get(position);
        tempSuggestion.setFlag(likeFlag);
        tempSuggestion.setAlreadySeen(true);
        tempSuggestions.put(tempSuggestion.getId(),tempSuggestion);
    }

    @Override
    public void unLiked(int position) {
        updateSuggestion(false, position);
        if (suggestions.size() == position + 1) {
            viewPagerPicker.setVisibility(View.GONE);
        }
        viewPagerPicker.setCurrentItem(position+1);
    }

    @Override
    public void onPause() {
        super.onPause();
        presenter.saveSuggestions(tempSuggestions);
    }

    public class ViewPagerAdapter extends FragmentStatePagerAdapter {

        private  HashMap<Integer,Suggestion1> suggestions;

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public void update( HashMap<Integer,Suggestion1> suggestions) {
            this.suggestions = suggestions;
            notifyDataSetChanged();
        }

        @Override
        public Fragment getItem(int position) {
            return SuggestionFragment.newInstance(position,suggestions.get(position).getUploadObjects().get(0), suggestions.get(position).getUploadObjects().get(1));
        }

        @Override
        public int getCount() {
            if (suggestions != null) {
                return suggestions.size();
            }
            return 0;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.rxUnsubscribe();
        presenter.detachView();
    }
}
