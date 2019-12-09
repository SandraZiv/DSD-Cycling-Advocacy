package com.cycling_advocacy.bumpy.ui.pastTrips;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.cycling_advocacy.bumpy.R;
import com.cycling_advocacy.bumpy.entities.PastTrip;

import java.util.List;

public class PastTripAdapter extends RecyclerView.Adapter<PastTripAdapter.ViewHolder>  {

        private List<PastTrip> pasTripList;

        public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView date, detail;
            public ImageButton imageUpload;

            public ViewHolder(View view) {
                super(view);
//                date = view.findViewById(R.id.date);
//                detail = view.findViewById(R.id.details);
//                imageUpload = view.findViewById(R.id.upload_image);
            }
        }
        public PastTripAdapter(List<PastTrip> pasTripList) {
            this.pasTripList = pasTripList;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_past_trips, parent, false);

            return new ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            PastTrip ptrip = pasTripList.get(position);
            //holder.detail.setText(ptrip.getGenre());
            //holder.imageUpload.set(ptrip.getIsUploaded());
        }

        @Override
        public int getItemCount() {
            return pasTripList.size();
        }

}
