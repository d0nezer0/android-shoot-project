package org.ggxz.shoot.adapter;

import androidx.recyclerview.widget.RecyclerView;

public interface ItemClickListener<VH extends RecyclerView.ViewHolder> {
    void onItemClickListener(VH holder, int position);
}
