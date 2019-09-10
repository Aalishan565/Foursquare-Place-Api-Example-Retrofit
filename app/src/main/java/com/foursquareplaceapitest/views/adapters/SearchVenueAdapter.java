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
import com.foursquareplaceapitest.dtos.searchresponse.Venue;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

public class SearchVenueAdapter extends RecyclerView.Adapter<SearchVenueAdapter.VenueViewHolder> {
    private Context context;
    private List<Venue> searchVenueList;

    public SearchVenueAdapter(Context context, List<Venue> searchVenueList) {
        this.context = context;
        this.searchVenueList = searchVenueList;
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
        holder.tvVenueName.setText(searchVenueList.get(position).getName());
        holder.tvVenueCat.setText(searchVenueList.get(position).getCategories().get(0).getName());
        holder.tvVenueAddress.setText(searchVenueList.get(position).getLocation().getAddress());
        if (null != searchVenueList.get(position).getCategories().get(0).getIcon().toString()) {
            ImageLoader.getInstance().displayImage(searchVenueList.get(position).getCategories().get(0).getIcon().toString(), holder.ivIcon);
        }
    }

    @Override
    public int getItemCount() {
        return searchVenueList.size();
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
