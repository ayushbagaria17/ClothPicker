package in.agrostar.ulink.clothpicker.ui.ViewHolder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import butterknife.ButterKnife;
import butterknife.InjectView;
import in.agrostar.ulink.clothpicker.R;

/**
 * Created by ayush on 23/4/17.
 */

public class BookmarkViewHolder extends RecyclerView.ViewHolder {
    @InjectView(R.id.iv_bookmark_image)
    ImageView ivBookmark;

    @InjectView(R.id.tv_image_description)
    TextView tvDescription;

    public BookmarkViewHolder(View itemView) {
        super(itemView);
        ButterKnife.inject(this,itemView);
    }

    public void pushData(Context context, String description, String image) {
        tvDescription.setText(description);
        Glide.with(context)
                .load(image)
                .into(ivBookmark);
    }
}
