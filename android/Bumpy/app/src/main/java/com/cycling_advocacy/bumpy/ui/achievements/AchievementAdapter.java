package com.cycling_advocacy.bumpy.ui.achievements;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.cycling_advocacy.bumpy.R;
import com.cycling_advocacy.bumpy.entities.Achievement;

import java.util.List;

public class AchievementAdapter extends RecyclerView.Adapter<AchievementAdapter.ViewHolder> {

    private List<Achievement> achievementList;

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView title, detail;
        public ImageView imageTrophy;

        public ViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.title);
            detail = view.findViewById(R.id.details_achievement);
            imageTrophy = view.findViewById(R.id.image_trophy);
        }
    }

    public AchievementAdapter(List<Achievement> achievementList) {
        this.achievementList = achievementList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_achievement, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Achievement ptrip = achievementList.get(position);
        holder.title.setText(ptrip.getTitle());
        holder.detail.setText(ptrip.getDetail());
        //holder.image_upload.set(ptrip.getIsUploaded());
    }

    @Override
    public int getItemCount() {
        return achievementList.size();
    }

}

