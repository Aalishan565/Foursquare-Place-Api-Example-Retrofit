package com.foursquareplaceapitest.gatewaymodel;

import com.foursquareplaceapitest.dtos.searchresponse.SearchVenueResponse;
import com.foursquareplaceapitest.dtos.vanues.VenuesResponse;
import com.foursquareplaceapitest.utils.Constants;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RetrofitAPI {
    @GET("venues/explore?")
    Call<VenuesResponse> venuesResponse(@Query(Constants.CLIENT_ID) String clientId,
                                        @Query(Constants.CLIENT_SECRET_KEY) String clientSecretKey,
                                        @Query(Constants.VERSION) String version,
                                        @Query(Constants.LAT_LANG) String latLang,
                                        @Query(Constants.LIMIT) String limit);

    @GET("venues/search?")
    Call<SearchVenueResponse> searchVenuesResponse(@Query(Constants.CLIENT_ID) String clientId,
                                                   @Query(Constants.CLIENT_SECRET_KEY) String clientSecretKey,
                                                   @Query(Constants.VERSION) String version,
                                                   @Query(Constants.LAT_LANG) String latLang,
                                                   @Query("query") String query,
                                                   @Query(Constants.LIMIT) String limit);

}
