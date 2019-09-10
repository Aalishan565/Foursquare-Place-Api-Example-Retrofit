package com.foursquareplaceapitest.views.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.foursquareplaceapitest.R;
import com.foursquareplaceapitest.dtos.vanues.Item;
import com.foursquareplaceapitest.dtos.vanues.VenuesResponse;
import com.foursquareplaceapitest.gatewaymodel.GpsTracker;
import com.foursquareplaceapitest.presenters.VenueReqImpl;
import com.foursquareplaceapitest.presenters.VenueReqPresenter;
import com.foursquareplaceapitest.presenters.VenueResponseListener;
import com.foursquareplaceapitest.utils.AppUtils;
import com.foursquareplaceapitest.utils.Constants;
import com.foursquareplaceapitest.utils.LocationUtils;
import com.foursquareplaceapitest.views.adapters.VenueAdapter;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

public class MainActivity extends AppCompatActivity implements VenueResponseListener, OnMapReadyCallback {
    private RecyclerView mRvMapView;
    private double deviceCurrentLat = 0.0;
    private double deviceCurrentLong = 0.0;
    private LocationUtils locationUtils;
    private GoogleMap mMap;
    private SupportMapFragment mapFragment;
    private GoogleApiClient googleApiClient;
    private final static int REQUEST_LOCATION = 199;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRvMapView = findViewById(R.id.rv_map_vew);
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRvMapView.setLayoutManager(mLinearLayoutManager);

        if (!hasGPSDevice(this)) {
            Toast.makeText(this, R.string.gps_not_supported, Toast.LENGTH_SHORT).show();
            finish();
        }

        final LocationManager manager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER) && hasGPSDevice(this)) {
        }
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER) && hasGPSDevice(this)) {
            enableLoc();
        }


        locationUtils = new LocationUtils(this);
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        if (getLatLang()) {
            callVenueApi();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.navigation_search:
                Intent intent = new Intent(this, SearchVenueActivity.class);
                intent.putExtra(Constants.LAT_LANG, deviceCurrentLat + "," + deviceCurrentLong);
                startActivity(intent);
                return true;
            case R.id.navigation_list:
                mapFragment.getView().setVisibility(View.GONE);
                mRvMapView.setVisibility(View.VISIBLE);
                return true;
            case R.id.navigation_map:
                mapFragment.getView().setVisibility(View.VISIBLE);
                mRvMapView.setVisibility(View.GONE);

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void callVenueApi() {
        if (AppUtils.getNetworkConnectivityState(this)) {
            AppUtils.showProgressDialog(this);
            VenueReqPresenter presenter = new VenueReqImpl(this);
            presenter.getVenuesReq(deviceCurrentLat + "," + deviceCurrentLong);
        } else {
            AppUtils.showToastWithMsg(this, getString(R.string.no_internet));
        }
    }

    @Override
    public void onSuccessVenueResponse(VenuesResponse venuesResponse) {
        AppUtils.dismissProgressDialog();
        List<Item> venueList = venuesResponse.getResponse().getGroups().get(0).getItems();
        for (Item item : venueList) {
            drawMarker(item.getVenue().getLocation().getLat(), item.getVenue().getLocation().getLng());
        }
        VenueAdapter adapter = new VenueAdapter(this, venueList);
        mRvMapView.setAdapter(adapter);

    }

    @Override
    public void onFailureVenueResponse(String message) {
        AppUtils.dismissProgressDialog();
        AppUtils.showToastWithMsg(this, message);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        } else {
            mMap.setMyLocationEnabled(true);
        }
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        //  mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setZoomGesturesEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        LatLng currentLoc = new LatLng(deviceCurrentLat, deviceCurrentLong);
        //drawMarker(deviceCurrentLat, deviceCurrentLong);
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(currentLoc, 15);
        mMap.animateCamera(cameraUpdate);
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                // showBottomSheetDialog(marker);
                return true;
            }
        });
    }

    private boolean hasGPSDevice(Context context) {
        final LocationManager mgr = (LocationManager) context
                .getSystemService(Context.LOCATION_SERVICE);
        if (mgr == null)
            return false;
        final List<String> providers = mgr.getAllProviders();
        if (providers == null)
            return false;
        return providers.contains(LocationManager.GPS_PROVIDER);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == -1) {
            //  moveNextScreen();
            return;
        }

    }

    private void enableLoc() {
        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                        @Override
                        public void onConnected(Bundle bundle) {
                        }

                        @Override
                        public void onConnectionSuspended(int i) {
                            googleApiClient.connect();
                        }
                    })
                    .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                        @Override
                        public void onConnectionFailed(ConnectionResult connectionResult) {
                        }
                    }).build();
            googleApiClient.connect();
            LocationRequest locationRequest = LocationRequest.create();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationRequest.setInterval(30 * 1000);
            locationRequest.setFastestInterval(5 * 1000);
            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                    .addLocationRequest(locationRequest);

            builder.setAlwaysShow(true);
            PendingResult<LocationSettingsResult> result =
                    LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
            result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
                @Override
                public void onResult(LocationSettingsResult result) {
                    final Status status = result.getStatus();
                    switch (status.getStatusCode()) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            try {
                                // Show the dialog by calling startResolutionForResult(),
                                // and check the result in onActivityResult().
                                status.startResolutionForResult(MainActivity.this, REQUEST_LOCATION);
                            } catch (IntentSender.SendIntentException e) {
                                // Ignore the error.
                            }
                            break;
                        case LocationSettingsStatusCodes.SUCCESS:
                            try {
                                status.startResolutionForResult(MainActivity.this, REQUEST_LOCATION);
                            } catch (IntentSender.SendIntentException e) {
                                // Ignore the error.
                            }
                            break;
                        case LocationSettingsStatusCodes.CANCELED:
                            try {
                                status.startResolutionForResult(MainActivity.this, REQUEST_LOCATION);
                                finish();
                            } catch (IntentSender.SendIntentException e) {
                                // Ignore the error.
                            }
                            break;
                    }
                }
            });
        }

    }

    private boolean getLatLang() {
        try {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 101);
            } else {
                boolean canGetLoc = locationUtils.canGetGpsLocation();
                if (canGetLoc) {
                    GpsTracker gpsTracker = new GpsTracker(this);
                    deviceCurrentLat = gpsTracker.getLatitude();
                    deviceCurrentLong = gpsTracker.getLongitude();
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private void drawMarker(double Lat, double lang) {
        LatLng loc = new LatLng(Lat, lang);
        mMap.addMarker(new MarkerOptions().position(loc)
                .icon(LocationUtils.bitmapDescriptorFromVector(this,
                        R.drawable.ic_place)));
    }
}
