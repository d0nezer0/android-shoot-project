package org.ggxz.shoot.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.common_module.db.mode.SingleShootDataModel;

import org.ggxz.shoot.R;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {
    private Context context;
    private OnItemClickListener listener;
    private List<SingleShootDataModel> data;
    private DecimalFormat df = new DecimalFormat("#.#");

    public MainAdapter(Context context) {
        this.context = context;
        data = new ArrayList<>();
    }

    @NonNull
    @Override
    public MainAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_shoot_data, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        if (listener != null) {
            viewHolder.check.setOnClickListener(v -> listener.onItemClick(viewHolder, viewHolder.getAdapterPosition()));
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MainAdapter.ViewHolder holder, int position) {
        SingleShootDataModel model = data.get(position);
        holder.faxu.setText(String.valueOf(position + 1));
        holder.ring.setText(model.getRing() == 11F ? "10.10" : df.format(model.getRing()));
        holder.time.setText(df.format(model.getTimeSecond()));
        holder.way.setText(model.getDirection());
    }

    public void setData(List<SingleShootDataModel> data) {

            this.data = data;
            notifyDataSetChanged();


    }

    public List<SingleShootDataModel> getData() {
        return this.data;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView check;
        TextView faxu;
        TextView ring;
        TextView time;
        TextView way;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            check = itemView.findViewById(R.id.check);
            faxu = itemView.findViewById(R.id.faxu);
            ring = itemView.findViewById(R.id.ring);
            time = itemView.findViewById(R.id.time);
            way = itemView.findViewById(R.id.way);
        }
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
