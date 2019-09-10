package com.foursquareplaceapitest.views.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.foursquareplaceapitest.R;
import com.foursquareplaceapitest.dtos.vanues.Item;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

public class VenueAdapter extends RecyclerView.Adapter<VenueAdapter.VenueViewHolder> {
    private Context context;
    private List<Item> venueList;

    public VenueAdapter(Context context, List<Item> venueList) {
        this.context = context;
        this.venueList = venueList;
    }

    @NonNull
    @Override
    public VenueViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.row_item_venues, parent, false);
        return new VenueViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VenueViewHolder holder, int position) {
        holder.tvVenueName.setText(venueList.get(position).getVenue().getName());
        holder.tvVenueCat.setText(venueList.get(position).getVenue().getCategories().get(0).getName());
        holder.tvVenueAddress.setText(venueList.get(position).getVenue().getLocation().getAddress());
        if (null != venueList.get(position).getVenue().getCategories().get(0).getIcon().toString()) {
            ImageLoader.getInstance().displayImage(venueList.get(position).getVenue().getCategories().get(0).getIcon().toString(), holder.ivIcon);
        }
    }

    @Override
    public int getItemCount() {
        return venueList.size();
    }

    static class VenueViewHolder extends RecyclerView.ViewHolder {
        ImageView ivIcon;
        TextView tvVenueName;
        TextView tvVenueCat;
        TextView tvVenueAddress;

        VenueViewHolder(@NonNull View itemView) {
            super(itemView);
            ivIcon = itemView.findViewById(R.id.ivIcon);
            tvVenueName = itemView.findViewById(R.id.tvVenueName);
            tvVenueCat = itemView.findViewById(R.id.tvVenueCategory);
            tvVenueAddress = itemView.findViewById(R.id.tvVenueAdd);
        }
    }

}
