package com.foursquareplaceapitest.presenters;


import com.foursquareplaceapitest.dtos.searchresponse.SearchVenueResponse;

public interface SearchVenueResponseListener {

    void onSuccessSearchVenueResponse(SearchVenueResponse venuesResponse);

    void onFailureSearchVenueResponse(String message);
}
