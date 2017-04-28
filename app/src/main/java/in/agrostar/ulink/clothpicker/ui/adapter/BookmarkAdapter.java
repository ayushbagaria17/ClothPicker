package in.agrostar.ulink.clothpicker.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import in.agrostar.ulink.clothpicker.R;
import in.agrostar.ulink.clothpicker.domain.Suggestion;
import in.agrostar.ulink.clothpicker.domain.Suggestion1;
import in.agrostar.ulink.clothpicker.ui.ViewHolder.BookmarkViewHolder;

/**
 * Created by ayush on 23/4/17.
 */

public class BookmarkAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private final BookMarkAdapterClickListener clickListener;
    private ArrayList<Suggestion1> suggestions;
    Context context;
    LayoutInflater inflater;

    public BookmarkAdapter(Context context, BookMarkAdapterClickListener listener) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.clickListener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_bookmark, parent, false);
        final BookmarkViewHolder viewHolder = new BookmarkViewHolder(view);
        View shareBtn = viewHolder.itemView.findViewById(R.id.iv_share);
        shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = viewHolder.getAdapterPosition();
                if (pos != -1) {
                    clickListener.shareClick(suggestions.get(pos));
                }
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((BookmarkViewHolder)holder).pushData(context,suggestions.get(position).getUploadObjects());
    }

    void setBookMarkList(ArrayList<Suggestion1> suggestions) {
        this.suggestions = suggestions;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (suggestions != null) {
            return suggestions.size();
        }
        return 0;
    }

    public void setData(ArrayList<Suggestion1> suggestions) {
        this.suggestions = suggestions;
        notifyDataSetChanged();
    }

    public interface BookMarkAdapterClickListener {
        void shareClick(Suggestion1 suggestion);
    }
}
