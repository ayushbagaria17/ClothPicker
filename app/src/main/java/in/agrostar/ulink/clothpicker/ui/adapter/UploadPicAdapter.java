package in.agrostar.ulink.clothpicker.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;

import java.util.ArrayList;
import java.util.HashMap;

import in.agrostar.ulink.clothpicker.R;
import in.agrostar.ulink.clothpicker.ui.ViewHolder.UploadPicViewHolder;

/**
 * Created by ayush on 21/4/17.
 */

public class UploadPicAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;
    LayoutInflater inflater;
    UploadPicAdapterListener clickListener;
    ArrayList<HashMap<String,Object>> uploadList;

    public UploadPicAdapter(Context context, UploadPicAdapterListener listener) {
        this.context = context;
        this.inflater =LayoutInflater.from(context);
        this.clickListener = listener;

    }

    @Override

    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_uploaded_pictures, parent, false);
        final UploadPicViewHolder viewHolder = new UploadPicViewHolder(view);
        View imgView = viewHolder.itemView.findViewById(R.id.iv_delete_pic);
        imgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = viewHolder.getAdapterPosition();
                if (pos != -1) {
                    clickListener.onClick(uploadList.get(pos), pos);
                }
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((UploadPicViewHolder)holder).pushData(context, (String) uploadList.get(position).get("fileName"),(TransferState) uploadList.get(position).get("state"));
    }

    @Override
    public int getItemCount() {
        if (uploadList != null)
        return uploadList.size();
        else return 0;
    }

    public void setUploadList(ArrayList<HashMap<String,Object>> uploadList) {
        this.uploadList = uploadList;
        notifyDataSetChanged();
    }

    public interface UploadPicAdapterListener {
        void onClick(HashMap<String,Object> item, int position);
    }
}
