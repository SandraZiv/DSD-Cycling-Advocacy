package com.cycling_advocacy.bumpy.ui.pastTrips;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.cycling_advocacy.bumpy.R;
import com.cycling_advocacy.bumpy.entities.PastTrip;

import java.util.ArrayList;
import java.util.List;

public class PastTripAdapter extends RecyclerView.Adapter<PastTripAdapter.ViewHolder>  {

        private List<PastTrip> pastTripList = new ArrayList<>();
        private Context context;

        public PastTripAdapter(Context context) {
            this.context = context;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView title, detail;
            public ImageButton imageUpload;

            public ViewHolder(View view) {
                super(view);
                title = view.findViewById(R.id.tv_past_trips_title);
                detail = view.findViewById(R.id.tv_past_trips_details);
                imageUpload = view.findViewById(R.id.image_uploaded);
            }
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_past_trips, parent, false);

            return new ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            PastTrip pastTrip = pastTripList.get(position);

            String startTime = pastTrip.getStartTime().toString();
            holder.title.setText(startTime);

            long duration = pastTrip.getDuration();
            // TODO: This should either be handled by an util or extracted to some class
            String durationString = String.format("%d:%02d:%02d", duration / 3600, (duration % 3600) / 60, (duration % 60));

            holder.detail.setText(context.getString(R.string.trip_description_display, durationString, pastTrip.getDistance()));

            if (!pastTrip.isUploaded()) {
                holder.imageUpload.setVisibility(View.VISIBLE);
            } else {
                holder.imageUpload.setVisibility(View.INVISIBLE);
            }
        }

        @Override
        public int getItemCount() {
            return pastTripList.size();
        }

        public void setPastTripList(List<PastTrip> pastTripList) {
            this.pastTripList = pastTripList;
            notifyDataSetChanged();
        }
}
