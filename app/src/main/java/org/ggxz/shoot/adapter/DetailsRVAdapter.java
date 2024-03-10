package org.ggxz.shoot.adapter;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.common_module.db.mode.SingleShootDataModel;

import org.ggxz.shoot.R;

import java.util.ArrayList;
import java.util.List;

public class DetailsRVAdapter extends RecyclerView.Adapter<DetailsRVAdapter.ViewHolder> {
    private Context context;
    private ListView.OnItemClickListener listener;
    private int curPos = 0;
    private List<SingleShootDataModel> data = new ArrayList<>();

    public DetailsRVAdapter(Context context, List<SingleShootDataModel> data) {
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_listview_tv, parent, false);

        return new ViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvNum.setText(String.valueOf(position + 1));
        if (curPos == position)
            holder.itemView.setBackground(context.getDrawable(R.drawable.button_round_trans_8));
        else
            holder.itemView.setBackgroundColor(Color.TRANSPARENT);
        holder.itemView.setOnClickListener(v -> {
            if (listener != null)
                listener.onItemClick(null, holder.itemView, position, position);
        });
    }

    public void setData(List<SingleShootDataModel> data) {
        this.data = data;
        notifyDataSetChanged();

    }

    public void setData(List<SingleShootDataModel> data, int curPos) {
        this.data = data;
        this.curPos = curPos;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNum;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNum = itemView.findViewById(R.id.tvNum);
        }
    }

    public void setOnItemClickListener(ListView.OnItemClickListener listener) {
        this.listener = listener;
    }

    public void setPos(int pos) {
        curPos = pos;
        notifyDataSetChanged();
    }
}
