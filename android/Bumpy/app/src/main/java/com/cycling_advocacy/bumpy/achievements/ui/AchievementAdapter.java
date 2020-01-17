package com.cycling_advocacy.bumpy.achievements.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.cycling_advocacy.bumpy.R;
import com.cycling_advocacy.bumpy.achievements.Achievement;

import java.util.ArrayList;
import java.util.List;

public class AchievementAdapter extends RecyclerView.Adapter<AchievementAdapter.ViewHolder> {

    private Context context;
    private List<Achievement> achievementList = new ArrayList<>();

    public AchievementAdapter(Context context) {
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_achievement, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Achievement achievement = achievementList.get(position);
        holder.title.setText(context.getResources().getString(achievement.getTitleId()));
        holder.detail.setText(context.getResources().getString(achievement.getDetailId()));

        if (achievement.isCompleted()) {
            holder.imageTrophy.setVisibility(View.VISIBLE);
            holder.title.setTextColor(ContextCompat.getColor(context, R.color.cyclingAdvocacyYellow));
            holder.detail.setTextColor(ContextCompat.getColor(context, R.color.cyclingAdvocacyYellow));
        } else {
            holder.imageTrophy.setVisibility(View.INVISIBLE);
            holder.title.setTextColor(ContextCompat.getColor(context, R.color.cyclingAdvocacyYellowHint));
            holder.detail.setTextColor(ContextCompat.getColor(context, R.color.cyclingAdvocacyYellowHint));
        }
    }

    @Override
    public int getItemCount() {
        return achievementList.size();
    }

    public void setAchievementList(List<Achievement> achievementList) {
        this.achievementList = achievementList;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView title, detail;
        public ImageView imageTrophy;

        public ViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.tv_achievement_title);
            detail = view.findViewById(R.id.tv_achievement_detail);
            imageTrophy = view.findViewById(R.id.image_trophy);
        }
    }
}

