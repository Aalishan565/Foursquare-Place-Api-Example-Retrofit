package com.foursquareplaceapitest.presenters;

import com.foursquareplaceapitest.dtos.vanues.VenuesResponse;
import com.foursquareplaceapitest.gatewaymodel.CommunicationManager;
import com.foursquareplaceapitest.utils.WebServiceConstants;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VenueReqImpl implements VenueReqPresenter {

    private CommunicationManager communicationManager;
    private VenueResponseListener venueResponseListener;

    public VenueReqImpl(VenueResponseListener venueResponseListener) {
        this.communicationManager = CommunicationManager.getInstance();
        this.venueResponseListener = venueResponseListener;
    }

    @Override
    public void getVenuesReq(String latLang) {
        Call<VenuesResponse> call = communicationManager.getVenuesListReq(latLang);
        if (call != null) {
            call.enqueue(new Callback<VenuesResponse>() {
                @Override
                public void onResponse(Call<VenuesResponse> call, Response<VenuesResponse> response) {
                    if (null != response && WebServiceConstants.STATUS_SUCCESS == response.body().getMeta().getCode()) {
                        VenuesResponse venuesResponse = response.body();
                        venueResponseListener.onSuccessVenueResponse(venuesResponse);
                    } else {
                        venueResponseListener.onFailureVenueResponse(response.errorBody().toString());
                    }
                }

                @Override
                public void onFailure(Call<VenuesResponse> call, Throwable t) {
                    venueResponseListener.onFailureVenueResponse(t.getLocalizedMessage());
                }
            });
        }


    }
}
