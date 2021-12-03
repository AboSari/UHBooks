package com.uhb.uhbooks.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.uhb.uhbooks.R;
import com.uhb.uhbooks.api.ApiInterface;
import com.uhb.uhbooks.models.Item;

import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = "ItemAdapter";

    private List<Item> mItems;
    private Context mContext;
    private OnItemListener mOnItemListener;

    public interface OnItemListener {
        void onItemClick(int i);
    }

    public ItemAdapter(Context context, List<Item> items, OnItemListener onItemListener) {
        this.mContext = context;
        this.mItems = items;
        this.mOnItemListener = onItemListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item, viewGroup, false);
        return new ViewHolder(view, mOnItemListener);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((ViewHolder) holder).setItemDetails(mItems.get(position));
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    @Override
    public int getItemViewType(int position) {
        return mItems.get(position).getType().ordinal();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView ivItem, ivItemThumb;
        TextView tvTitle, tvSubtitle;
        OnItemListener onItemListener;

        private ViewHolder(@NonNull View itemView, OnItemListener onItemListener) {
            super(itemView);

            ivItem = itemView.findViewById(R.id.ivItem);
            ivItemThumb = itemView.findViewById(R.id.ivItemThumb);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvSubtitle = itemView.findViewById(R.id.tvSubtitle);

            this.onItemListener = onItemListener;

            itemView.setOnClickListener(this);
        }

        private void setItemDetails(Item item) {
            if (item.getType() == Item.Type.FILE) {
                ivItem.setVisibility(View.GONE);
                Glide.with(mContext)
                        .load(ApiInterface.BASE_URL + item.getThumb())
                        .placeholder(R.drawable.ic_baseline_picture_as_pdf_24)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .centerCrop()
                        .into(ivItemThumb);
            }
            tvTitle.setText(item.getName());
            tvSubtitle.setText("");
        }

        @Override
        public void onClick(View v) {
            Log.d(TAG, "onClick::" + mItems.get(getBindingAdapterPosition()).getName() + "clicked");
            if (onItemListener != null) onItemListener.onItemClick(getBindingAdapterPosition());
        }
    }

    public void notifyChanged(List<Item> items) {
        mItems = items;
        notifyDataSetChanged();
    }
}
