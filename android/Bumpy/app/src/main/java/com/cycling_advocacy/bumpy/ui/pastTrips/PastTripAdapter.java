package com.cycling_advocacy.bumpy.ui.pastTrips;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.cycling_advocacy.bumpy.R;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class PastTripAdapter extends RecyclerView.Adapter<PastTripAdapter.MyViewHolder>  {

        private List<PastTrip> pasTripList;

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView date, detail;
            public ImageButton image_upload;

            public MyViewHolder(View view) {
                super(view);
                date = (TextView) view.findViewById(R.id.date);
                detail = (TextView) view.findViewById(R.id.details);
                image_upload = (ImageButton) view.findViewById(R.id.upload_image);
            }
        }


        public PastTripAdapter(List<PastTrip> pasTripList) {
            this.pasTripList = pasTripList;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_past_trips, parent, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            PastTrip ptrip = pasTripList.get(position);
            holder.date.setText(ptrip.getDate());
            //holder.detail.setText(ptrip.getGenre());
            //holder.image_upload.set(ptrip.getUpload_image());
        }

        @Override
        public int getItemCount() {
            return pasTripList.size();
        }

}