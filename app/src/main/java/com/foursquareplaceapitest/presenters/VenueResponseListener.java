package com.foursquareplaceapitest.presenters;

import com.foursquareplaceapitest.dtos.vanues.VenuesResponse;

public interface VenueResponseListener {

    void onSuccessVenueResponse(VenuesResponse venuesResponse);

    void onFailureVenueResponse(String message);
}
