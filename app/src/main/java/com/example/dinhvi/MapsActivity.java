package com.example.dinhvi;

import androidx.fragment.app.FragmentActivity;

import android.net.Network;
import android.os.Bundle;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    public double latitude;
    public double longitude;
    public String targetName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        targetName = getIntent().getStringExtra("CHILD_NAME");
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */

    public void getRequestQueueForMap() {
        RequestQueue requestQueue;
        String urlRequest = "https://dacnpm-backend.herokuapp.com/users/5e92c0641c9d44000027dae1/getchildrenping";
        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.GET, urlRequest, (String) null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            latitude = Double.parseDouble(response.getString("latitude"));;
                            longitude = Double.parseDouble(response.getString("longitude"));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MapsActivity.this, "Can not get coordinates !", Toast.LENGTH_LONG).show();
                    }
                }
        );

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //GET the location from server
        getRequestQueueForMap();

        // Add a marker in Sydney and move the camera
        LatLng trackingTarget = new LatLng(latitude, longitude);
        mMap.addMarker(new MarkerOptions().position(trackingTarget).title(targetName));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(trackingTarget));
        //mMap.animateCamera(CameraUpdateFactory.zoomTo(30));
    }
}
