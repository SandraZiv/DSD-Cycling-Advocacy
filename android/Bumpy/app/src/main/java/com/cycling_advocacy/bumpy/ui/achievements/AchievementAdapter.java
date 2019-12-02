package com.cycling_advocacy.bumpy.ui.achievements;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.cycling_advocacy.bumpy.R;


import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class AchievementAdapter extends RecyclerView.Adapter<AchievementAdapter.MyViewHolder> {

    private List<Achievement> achievementList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, detail;
        public ImageView image_trophy;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            detail = (TextView) view.findViewById(R.id.details_achievement);
            image_trophy = (ImageView) view.findViewById(R.id.image_trophy);
        }
    }


    public AchievementAdapter(List<Achievement> achievementList) {
        this.achievementList = achievementList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_achievements, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Achievement ptrip = achievementList.get(position);
        holder.title.setText(ptrip.getTitle());
        holder.detail.setText(ptrip.getDetail());
        //holder.image_upload.set(ptrip.getUpload_image());
    }

    @Override
    public int getItemCount() {
        return achievementList.size();
    }

}

