package in.agrostar.ulink.clothpicker.ui.ViewHolder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.bumptech.glide.Glide;

import java.io.File;

import butterknife.ButterKnife;
import butterknife.InjectView;
import in.agrostar.ulink.clothpicker.R;

/**
 * Created by ayush on 22/4/17.
 */

public class UploadPicViewHolder extends RecyclerView.ViewHolder{
    @InjectView(R.id.iv_upload_pic)
    ImageView ivCropImage;

    @InjectView(R.id.tv_upload_status)
    TextView tvUploadStatus;


    @InjectView(R.id.iv_delete_pic)
    ImageView retryOrDeletePic;


    public UploadPicViewHolder(View itemView) {
        super(itemView);
        ButterKnife.inject(this,itemView);
    }

    public void pushData(Context context, String imagePath, TransferState uploadStatus) {
        if (uploadStatus.equals(TransferState.getState("PAUSED")) || uploadStatus.equals(TransferState.getState("WAITING_FOR_NETWORK")) ||  uploadStatus.equals(TransferState.getState("WAITING"))) {
            retryOrDeletePic.setImageDrawable(context.getDrawable(R.drawable.ic_retry));
            tvUploadStatus.setText(R.string.label_paused);
        } else if (uploadStatus.equals(TransferState.IN_PROGRESS)) {
            tvUploadStatus.setText(R.string.label_in_progress);
            retryOrDeletePic.setVisibility(View.GONE);
        } else if (uploadStatus.equals(TransferState.COMPLETED)) {
            tvUploadStatus.setText(R.string.label_complete);
            retryOrDeletePic.setVisibility(View.VISIBLE);
            retryOrDeletePic.setImageDrawable(context.getDrawable(R.mipmap.ic_delete));
        } else if (uploadStatus.equals(TransferState.FAILED)) {
            tvUploadStatus.setText(R.string.label_failed);
        }

        Glide.with(context)
                .load(new File(imagePath))
                .centerCrop()
                .placeholder(R.color.place_holder_color)
                .crossFade()
                .into(ivCropImage);
    }



}
