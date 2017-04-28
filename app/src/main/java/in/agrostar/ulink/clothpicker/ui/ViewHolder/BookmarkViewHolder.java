package in.agrostar.ulink.clothpicker.ui.ViewHolder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import in.agrostar.ulink.clothpicker.R;
import in.agrostar.ulink.clothpicker.domain.UploadObject;
import in.agrostar.ulink.clothpicker.domain.UploadType;

/**
 * Created by ayush on 23/4/17.
 */

public class BookmarkViewHolder extends RecyclerView.ViewHolder {
    @InjectView(R.id.iv_shirt_image1)
    ImageView ivShirt;

    @InjectView(R.id.iv_trouser_image1)
    ImageView ivTrouser;

    @InjectView(R.id.tv_image_description)
    TextView tvDescription;

    public BookmarkViewHolder(View itemView) {
        super(itemView);
        ButterKnife.inject(this,itemView);
    }


    public void pushData(Context context, List<UploadObject> uploadObjects) {
        for(UploadObject uploadObject: uploadObjects) {
            if (uploadObject.getType().toString().equalsIgnoreCase(UploadType.SHIRT.toString())) {
                File file = new File(uploadObject.getFilePath());
                Glide.with(context)
                        .load(file)
                        .into(ivShirt);
            } else {
                File file = new File(uploadObject.getFilePath());
                Glide.with(context)
                        .load(file)
                        .into(ivTrouser);
            }
        }
    }
}
