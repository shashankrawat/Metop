package com.elintminds.mac.metatopos.activities;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.elintminds.mac.metatopos.R;
import com.elintminds.mac.metatopos.beans.mappath.TaskLoadedCallback;
import com.elintminds.mac.metatopos.common.GetLocation;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ShareLocationActivity extends AppCompatActivity implements OnMapReadyCallback, TaskLoadedCallback, GoogleMap.OnInfoWindowClickListener {
    private GoogleMap mMap;
    private MarkerOptions place1, place2;
    Button getDirection;
    private Polyline currentPolyline;
    public static final int requestCode_Location = 80;

    private GetLocation getLocation;
    double currentLat, curentLng;
    double postLng, postLat;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_activity_demo);
        getLocation = new GetLocation(getApplicationContext());
        checkForPermissionLocation(this);
//        getDirection = findViewById(R.id.btnGetDirection);
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.mapNearBy);
        mapFragment.getMapAsync(this);
        Intent intent = getIntent();
        String postLatStr = intent.getStringExtra("POST_LATITUDE");
        String postLngStr = intent.getStringExtra("POST_LONGITUDE");
        if (postLatStr != null && postLngStr != null) {
            postLat = Double.parseDouble(postLatStr);
            postLng = Double.parseDouble(postLngStr);
        }
        Log.e("Post_Latitude", "" + postLat + "Post_Longitude" + postLng);
        place1 = new MarkerOptions().position(new LatLng(currentLat, curentLng)).title("").icon(BitmapDescriptorFactory.fromResource(R.drawable.draw_track_emoji_icon));
        place2 = new MarkerOptions().position(new LatLng(postLat, postLng)).title("Get Direction").icon(BitmapDescriptorFactory.fromResource(R.drawable.draw_path_post_icon));
//        String url = getUrl(place1.getPosition(), place2.getPosition(), "driving");
//        Log.e("DIRECTION_URL",""+url);
//        new FetchURL(this).execute(url, "driving");
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng destination = new LatLng(postLat, postLng);
        LatLng origin = new LatLng(currentLat, curentLng);
        mMap = googleMap;
        try {
            boolean success = mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style));

            googleMap.addMarker(place1);
            googleMap.addMarker(place2);
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(origin));
            CameraUpdate zoom = CameraUpdateFactory.newLatLngZoom(origin, 15);
            mMap.animateCamera(zoom);


            mMap.setOnInfoWindowClickListener(this);

            Log.d("mylog", "Added Markers");


            if (!success) {
                Log.e("tag", "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e("tag", "Can't find style. Error: ", e);
        }


//        String url = getDirectionsUrl(origin, destination);
        String url = getUrl(place1.getPosition(), place2.getPosition(), "driving");
        Log.e("DIRECTION_URL", "" + url);

        DownloadTask downloadTask = new DownloadTask();

        // Start downloading json data from Google Directions API
        downloadTask.execute(url);
    }

    private String getUrl(LatLng position, LatLng dest, String directionMode) {
        String str_origin = "origin=" + position.latitude + "," + position.longitude;
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        String mode = "mode=" + directionMode;
        String parameters = str_origin + "&" + str_dest + "&" + mode;
        String output = "json";
        return "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=" + getString(R.string.google_maps_key);
    }

    @Override
    public void onTaskDone(Object... values) {
        if (currentPolyline != null)
            currentPolyline.remove();
        currentPolyline = mMap.addPolyline((PolylineOptions) values[0]);
    }


    private void checkForPermissionLocation(Context context) {
        int permissionCheck = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.ACCESS_FINE_LOCATION)) {
                alertdialogMesg();
            } else {
                ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, requestCode_Location);
            }
        } else {
            if (getLocation.enabledLocation()) {
                getLocation.getUserLocation();
                currentLat = getLocation.getUserLatitude();
                curentLng = getLocation.getUserLongitude();
            } else {
                Toast.makeText(getApplicationContext(), "You have denied access location request", Toast.LENGTH_SHORT).show();

            }

        }
    }

    private void alertdialogMesg() {
        AlertDialog.Builder mybulider = new AlertDialog.Builder(getApplicationContext());
        mybulider.setTitle("Alert Message");
        mybulider.setMessage(R.string.Alert_Explanation_Location);
        mybulider.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, requestCode_Location);
            }
        });
        AlertDialog ad = mybulider.create();
        ad.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case requestCode_Location: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (getLocation.enabledLocation()) {
                        getLocation.getUserLocation();
                        currentLat = getLocation.getUserLatitude();
                        curentLng = getLocation.getUserLongitude();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "You have denied access location request", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }


    @Override
    public void onInfoWindowClick(Marker marker) {
        Log.e("MARKER_INFO", "" + marker.getTitle());
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                String uri = "http://maps.google.com/maps?saddr=" + place1.getPosition().latitude + "," + place1.getPosition().longitude + "&daddr=" + place2.getPosition().latitude + "," + place2.getPosition().longitude;
//                Uri gmmIntentUri = Uri.parse("geo:0,0?q=");
                Uri gmmIntentUri = Uri.parse(uri);
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
            }
        }, 500);
    }


    private class DownloadTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... url) {

            String data = "";

            try {
                data = downloadUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            ParserTask parserTask = new ParserTask();
            parserTask.execute(result);

        }
    }

    /**
     * A class to parse the Google Places in JSON format
     */
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();

                routes = parser.parse(jObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList points = null;
            PolylineOptions lineOptions = null;

            for (int i = 0; i < result.size(); i++) {
                points = new ArrayList();
                lineOptions = new PolylineOptions();

                List<HashMap<String, String>> path = result.get(i);

                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                lineOptions.addAll(points);
                lineOptions.width(12);
                lineOptions.color(Color.RED);
                lineOptions.geodesic(true);

            }

            // Drawing polyline in the Google Map for the i-th route
            Log.e("LINE_OPTION", "" + lineOptions);
            if (lineOptions != null) {
                mMap.addPolyline(lineOptions);
            }
        }
    }

    /**
     * A method to download json data from url
     */
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.connect();

            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        } catch (Exception e) {
            Log.d("Exception", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

}
