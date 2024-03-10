package org.ggxz.shoot.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.utils.TimeUtils;
import com.example.common_module.db.mode.ShootDataModel;

import org.ggxz.shoot.R;

import java.util.List;
import java.util.Random;


public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {
    private Context context;

    private ItemClickListener<HistoryAdapter.ViewHolder> listener;
    private List<ShootDataModel> data;

    public HistoryAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_history_layout, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ShootDataModel model = data.get(position);
        holder.tvNum.setText(position + 1 + "");
        holder.tvTime.setText(TimeUtils.millis2String(model.getCreateTime()));
        holder.userName.setText(model.getUserName());
        holder.grade.setText(String.valueOf(model.getTotalGrade()));
        holder.gun.setText(String.valueOf(model.getTotalShoot()));
        holder.collimation.setText(String.valueOf(model.getTotalCollimation()));
        holder.send.setText(String.valueOf(model.getTotalSend()));
        holder.total.setText(String.valueOf(model.getTotalAll()));
        if (position % 2 == 0) {
            holder.itemView.setBackground(context.getDrawable(R.drawable.bg_item_history_rv));
        } else {
            holder.itemView.setBackgroundColor(Color.TRANSPARENT);
        }
        holder.check.setOnClickListener(v -> {
            if (listener != null)
                listener.onItemClickListener(holder, position);
        });
    }

    public void setData(List<ShootDataModel> list) {
        data = list;
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(ItemClickListener<HistoryAdapter.ViewHolder> listener) {
        this.listener = listener;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvNum;
        TextView tvTime;
        TextView userName;
        TextView grade;
        TextView gun;
        TextView collimation;
        TextView send;
        TextView total;
        TextView check;
        View itemView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView = itemView;
            tvNum = itemView.findViewById(R.id.tvNum);
            tvTime = itemView.findViewById(R.id.tvTime);
            userName = itemView.findViewById(R.id.userName);
            grade = itemView.findViewById(R.id.grade);
            gun = itemView.findViewById(R.id.gun);
            collimation = itemView.findViewById(R.id.collimation);
            send = itemView.findViewById(R.id.send);
            gun = itemView.findViewById(R.id.gun);
            total = itemView.findViewById(R.id.total);
            check = itemView.findViewById(R.id.check);

        }
    }
}