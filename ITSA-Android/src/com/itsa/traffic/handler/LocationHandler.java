package com.itsa.traffic.handler;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

public class LocationHandler implements LocationListener, ConnectionCallbacks,
		OnConnectionFailedListener {

	private GoogleApiClient mGoogleApiClient;
	private Context context;
	private LocationRequest mLocationRequest;
	private TrafficManager manager;

	public LocationHandler(Context context, TrafficManager trafficManager) {
		this.context = context;
		this.manager = trafficManager;
		buildGoogleApiClient();
		createLocationRequest();
	}

	protected void buildGoogleApiClient() {
		mGoogleApiClient = new GoogleApiClient.Builder(context)
				.addConnectionCallbacks(this)
				.addOnConnectionFailedListener(this)
				.addApi(LocationServices.API).build();
	}

	protected void createLocationRequest() {
		mLocationRequest = new LocationRequest();
		mLocationRequest.setInterval(25000);
		mLocationRequest.setFastestInterval(10000);
		mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
	}

	public void start() {
		if (GooglePlayServicesUtil.isGooglePlayServicesAvailable(context) != 0) {
			Log.e("LocationHandler", "GooglePlayServices isn't available");
		}
		if (!(mGoogleApiClient.isConnected() || mGoogleApiClient.isConnecting()))
			mGoogleApiClient.connect();
	}

	public void stop() {
		LocationServices.FusedLocationApi.removeLocationUpdates(
				mGoogleApiClient, this);
	}

	protected void changePosition(Location location) {

	}

	@Override
	public void onLocationChanged(Location location) {
		manager.handleChangePosition(location);

	}

	@Override
	public void onConnected(Bundle bundle) {
		Log.i("PLAY SERVICE", "Connected");
		changePosition(LocationServices.FusedLocationApi
				.getLastLocation(mGoogleApiClient));
		startLocationUpdates();

	}

	protected void startLocationUpdates() {
		LocationServices.FusedLocationApi.requestLocationUpdates(
				mGoogleApiClient, mLocationRequest, this);
	}

	@Override
	public void onConnectionSuspended(int arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onConnectionFailed(ConnectionResult arg0) {
		// TODO Auto-generated method stub

	}

}
