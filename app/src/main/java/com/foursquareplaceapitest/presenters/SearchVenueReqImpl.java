package com.foursquareplaceapitest.presenters;

import com.foursquareplaceapitest.dtos.searchresponse.SearchVenueResponse;
import com.foursquareplaceapitest.dtos.vanues.VenuesResponse;
import com.foursquareplaceapitest.gatewaymodel.CommunicationManager;
import com.foursquareplaceapitest.utils.WebServiceConstants;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchVenueReqImpl implements SearchVenueReqPresenter {

    private CommunicationManager communicationManager;
    private SearchVenueResponseListener searchVenueResponseListener;

    public SearchVenueReqImpl(SearchVenueResponseListener searchVenueResponseListener) {
        this.communicationManager = CommunicationManager.getInstance();
        this.searchVenueResponseListener = searchVenueResponseListener;
    }

    @Override
    public void getSearchVenuesReq(String latLang,String query) {
        Call<SearchVenueResponse> call = communicationManager.getSearchVenuesListReq(latLang,query);
        if (call != null) {
            call.enqueue(new Callback<SearchVenueResponse>() {
                @Override
                public void onResponse(Call<SearchVenueResponse> call, Response<SearchVenueResponse> response) {
                    if (null != response && WebServiceConstants.STATUS_SUCCESS == response.body().getMeta().getCode()) {
                        SearchVenueResponse venuesResponse = response.body();
                        searchVenueResponseListener.onSuccessSearchVenueResponse(venuesResponse);
                    } else {
                        searchVenueResponseListener.onFailureSearchVenueResponse(response.errorBody().toString());
                    }
                }

                @Override
                public void onFailure(Call<SearchVenueResponse> call, Throwable t) {
                    searchVenueResponseListener.onFailureSearchVenueResponse(t.getLocalizedMessage());
                }
            });
        }


    }
}
