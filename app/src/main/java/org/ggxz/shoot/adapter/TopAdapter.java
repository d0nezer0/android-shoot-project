package org.ggxz.shoot.adapter;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import org.ggxz.shoot.R;

import java.util.Random;

public class TopAdapter extends RecyclerView.Adapter<TopAdapter.ViewHolder> {
    private Context context;
    @DrawableRes
    private int[] imgIds = {
            R.drawable.top1,
            R.drawable.top2,
            R.drawable.top3,
            R.drawable.top4,
            R.drawable.top5,
            R.drawable.top6,
            R.drawable.top7,
            R.drawable.top8,
            R.drawable.top9,
            R.drawable.top10
    };
    private String[] names = {
            "张三",
            "李强",
            "王阳",
            "李小龙",
            "张艺源",
            "洪金龙",
            "王博",
            "李飞飞",
            "秦襄阳",
            "郭艾东",
    };

    public TopAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public TopAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_top_layout, parent, false);
        return new ViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(@NonNull TopAdapter.ViewHolder holder, int position) {
        holder.topImg.setImageDrawable(context.getDrawable(imgIds[position]));
        holder.dayTv.setText(names[new Random().nextInt(10)]);
        holder.weekTv.setText(names[new Random().nextInt(10)]);
        holder.monthTv.setText(names[new Random().nextInt(10)]);
        holder.yearTv.setText(names[new Random().nextInt(10)]);
    }

    @Override
    public int getItemCount() {
        return 10;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView topImg;
        private TextView dayTv;
        private TextView weekTv;
        private TextView monthTv;
        private TextView yearTv;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            topImg = itemView.findViewById(R.id.topImg);
            dayTv = itemView.findViewById(R.id.dayTv);
            weekTv = itemView.findViewById(R.id.weekTv);
            monthTv = itemView.findViewById(R.id.monthTv);
            yearTv = itemView.findViewById(R.id.yearTv);
        }
    }
}