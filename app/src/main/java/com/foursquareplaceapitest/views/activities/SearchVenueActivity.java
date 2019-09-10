package com.foursquareplaceapitest.views.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.foursquareplaceapitest.R;
import com.foursquareplaceapitest.dtos.searchresponse.SearchVenueResponse;
import com.foursquareplaceapitest.dtos.searchresponse.Venue;
import com.foursquareplaceapitest.presenters.SearchVenueReqImpl;
import com.foursquareplaceapitest.presenters.SearchVenueReqPresenter;
import com.foursquareplaceapitest.presenters.SearchVenueResponseListener;
import com.foursquareplaceapitest.utils.AppUtils;
import com.foursquareplaceapitest.utils.Constants;
import com.foursquareplaceapitest.views.adapters.SearchVenueAdapter;

import java.util.List;

public class SearchVenueActivity extends AppCompatActivity implements SearchVenueResponseListener {
    private RecyclerView mRvMapView;
    private String latLang = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_venue);
        SearchView searchView = findViewById(R.id.searchView);
        mRvMapView = findViewById(R.id.rv_map_vew);
        Intent intent = getIntent();
        latLang = intent.getStringExtra(Constants.LAT_LANG);
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRvMapView.setLayoutManager(mLinearLayoutManager);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                callSearchVenueApi(s);
                AppUtils.hideKeyboard(SearchVenueActivity.this, getCurrentFocus().getRootView());
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

    }

    private void callSearchVenueApi(String query) {
        if (AppUtils.getNetworkConnectivityState(this)) {
            AppUtils.showProgressDialog(this);
            SearchVenueReqPresenter presenter = new SearchVenueReqImpl(this);
            presenter.getSearchVenuesReq(latLang, query);
        } else {
            AppUtils.showToastWithMsg(this, getString(R.string.no_internet));
        }
    }

    @Override
    public void onSuccessSearchVenueResponse(SearchVenueResponse searchVenueResponse) {
        AppUtils.dismissProgressDialog();
        List<Venue> venueList = searchVenueResponse.getResponse().getVenues();
        SearchVenueAdapter searchVenueAdapter = new SearchVenueAdapter(this, venueList);
        mRvMapView.setAdapter(searchVenueAdapter);

    }

    @Override
    public void onFailureSearchVenueResponse(String message) {
        AppUtils.dismissProgressDialog();
        AppUtils.showToastWithMsg(this, message);
    }
}
