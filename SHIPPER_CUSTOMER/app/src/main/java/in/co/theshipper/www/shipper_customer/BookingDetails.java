package in.co.theshipper.www.shipper_customer;


import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static com.google.android.gms.internal.zzir.runOnUiThread;


/**
 * A simple {@link Fragment} subclass.
 */
public class BookingDetails extends Fragment implements View.OnClickListener{

    protected RequestQueue requestQueue;
    private String TAG = BookingDetails.class.getName();
    protected View view;
    private LinearLayout map_view,map;
    private Button callButton;
    private SupportMapFragment mMapFragment;
    private GoogleMap mMap=null;
    private boolean stopTimer = false;
    private Timer timer;
    private Location location;
    private String received_driver_current_lat,received_driver_current_lng, received_driver_token;
    private String crn_no="";
    private TextView location_datetime;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Fn.logD("BOOKING_DETAILS_FRAGMENT_LIFECYCLE", "onAttach Called");
    }

    public BookingDetails() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Fn.logD("BOOKING_DETAILS_FRAGMENT_LIFECYCLE", "onCreateView Called");
//        Fn.logD("booking_status_url",booking_status_url);
        Bundle bundle = this.getArguments();
        crn_no = bundle.getString("crn_no");
        Fn.logD("bundle_crn_no", crn_no);
        System.out.println(crn_no);
        view = inflater.inflate(R.layout.fragment_booking_details, container, false);
        map_view = (LinearLayout) view.findViewById(R.id.map_view);
        map = (LinearLayout) view.findViewById(R.id.map);
        location_datetime = (TextView) view.findViewById(R.id.location_datetime);
        callButton = (Button) view.findViewById(R.id.driver_mobile_no);
        callButton.setOnClickListener(this);
        Fn.logD("Map Added", "Map Added");
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        Fn.logD("BOOKING_DETAILS_FRAGMENT_LIFECYCLE", "onViewCreated Called");
        super.onViewCreated(view, savedInstanceState);
        mMapFragment = SupportMapFragment.newInstance();
        getChildFragmentManager().beginTransaction().replace(R.id.map, mMapFragment, "MAP_FRAGMENT").commit();
        String booking_status_url = Constants.Config.ROOT_PATH+"get_booking_status";
        HashMap<String,String> hashMap= new HashMap<String,String>();
        String user_token = Fn.getPreference(getActivity(),"user_token");
        hashMap.put("crn_no", crn_no);
        hashMap.put("user_token", user_token);
        sendVolleyRequest(booking_status_url, Fn.checkParams(hashMap), "booking_status");
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
    public void sendVolleyRequest(String URL, final HashMap<String,String> hMap,final String method){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Fn.logD("onResponse_booking_status", String.valueOf(response));
                if(method.equals("booking_status")) {
                    Fn.logD("booking_status","booking_status");
                    bookingStatusSuccess(response);
                }else if (method.equals("vehicle_location")) {
                    Fn.logD("vehicle_location","vehicle_location");
                    vehicleLocationSuccess(response);
                }else if(method.equals("draw_path")){
                    Fn.logD("method","method");
                    drawPath(response);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Fn.logD("onErrorResponse", String.valueOf(error));
                Fn.ToastShort(getActivity(), Constants.Message.NETWORK_ERROR);
            }
        }){
            @Override
            protected HashMap<String,String> getParams(){
                return hMap;
            }
        };
        stringRequest.setTag(TAG);
        Fn.addToRequestQue(requestQueue, stringRequest, getActivity());
    }
    protected void bookingStatusSuccess(String response){
        Fn.logD("BOOKING_DETAILS_FRAGMENT_LIFECYCLE", "bookingStatusSuccess Called");
        if(!Fn.CheckJsonError(response)){
//            Fn.logD("bookingStatusSuccess", "bookingStatusSuccess Called");
            Fn.logD("received_json", response);
            JSONObject jsonObject;
            JSONArray jsonArray;
            try {
                jsonObject = new JSONObject(response);
                String errFlag = jsonObject.getString("errFlag");
                if(errFlag.equals("1")){
                    Fn.logD("toastNotdone","toastNotdone");
                }
                else if(errFlag.equals("0"))
                {
                    TextView textView=new TextView(getActivity());
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT);
                    textView.setLayoutParams(layoutParams);
                    textView.setId(R.id.large_text);
                    textView.setTextSize(TypedValue.COMPLEX_UNIT_SP,getResources().getDimensionPixelSize(R.dimen.large_text_size));
                    textView.setGravity(Gravity.CENTER);
                    if(jsonObject.has("likes"))
                    {
                        jsonArray = jsonObject.getJSONArray("likes");
                        int count = 0;
                        while (count < jsonArray.length())
                        {
                            Fn.logD("likes_entered", "likes_entered");
                            JSONObject JO = jsonArray.getJSONObject(count);
                            String received_is_booked = JO.getString("is_booked");
                            String received_is_active= JO.getString("is_active");
                            Fn.logD("received_is_booked",received_is_booked);
                            Fn.logD("received_is_active",received_is_active);
                            if(received_is_booked.equals("1")&&received_is_active.equals("1"))
                            {
                                LinearLayout location_found_view = (LinearLayout) view.findViewById(R.id.location_found);
                                Fn.logD("FUTURE_BOOKING_FRAGMENT_LIFECYCLE","driver_found");
                                LinearLayout driver_found_view = (LinearLayout) view.findViewById(R.id.driver_found);
//                                Button driver_mobile_no_view = (Button) view.findViewById(R.id.driver_mobile_no);
                                TextView driver_name_view = (TextView) view.findViewById(R.id.driver_name);
                                received_driver_token = JO.getString("driver_token");
                                Fn.putPreference(getActivity(),"driver_token",received_driver_token);
//                                received_driver_vehicle_no = JO.getString("driver_vehicle_no");
                                String received_driver_name = JO.getString("driver_name");
                                String received_driver_mobile_no = JO.getString("driver_mobile_no");
                                String received_driver_location_datetime = JO.getString("driver_location_datetime");
                                received_driver_current_lat = JO.getString("driver_location_lat");
                                received_driver_current_lng = JO.getString("driver_location_lng");
//
//                                Fn.logD("received_driver_current_lat", received_driver_current_lat);
//                                Fn.logD("received_driver_current_lng", received_driver_current_lng);
                                location_datetime.setText("Last Seen: "+Fn.getDateName(received_driver_location_datetime));
                                location_found_view.setVisibility(View.VISIBLE);
                                driver_name_view.setText(received_driver_name);
                                callButton.setText(received_driver_mobile_no);
                                driver_found_view.setVisibility(View.VISIBLE);
                                location_found_view.setVisibility(View.VISIBLE);
                                map.setVisibility(View.VISIBLE);
                                map_view.setVisibility(View.GONE);
                                setUpMapIfNeeded();
                                TimerProgramm();
                            }
                            else
                            {
                                Fn.logD("vehicle_match",Constants.Message.VEHICLE_ALLOCATION_PENDING);
                                textView.setText(Constants.Message.VEHICLE_ALLOCATION_PENDING);
                                map_view.addView(textView);
                            }
                            count++;
                        }
                    }
                    else
                    {
                        Fn.logD("vehicle_match",Constants.Message.NO_CURRENT_BOOKING);
                        textView.setText(Constants.Message.NO_CURRENT_BOOKING);
                        map_view.addView(textView);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else{
            ErrorDialog(Constants.Title.SERVER_ERROR,Constants.Message.SERVER_ERROR);
//            Fn.ToastShort(getActivity(), Constants.Message.NETWORK_ERROR);
        }
    }
    protected void vehicleLocationSuccess(String response){
        Fn.logD("BOOKING_DETAILS_FRAGMENT_LIFECYCLE", "vehicleLocationSuccess Called");
        if(!Fn.CheckJsonError(response)){
//            Fn.logD("vehicleLocationSuccess", "vehicleLocationSuccess Called");
//            super.onPostExecute(JsonString);
//            String received_crn_no = "DefaultIsNothing";
            JSONObject jsonObject;
            JSONArray jsonArray;
            try {
                jsonObject = new JSONObject(response);
                //jsonArray = jsonObject.getJSONArray("likes");
                String errFlag = jsonObject.getString("errFlag");
//                errMsg = jsonObject.getString("errMsg");
                if(errFlag.equals("1")){
                    Fn.logD("toastNotdone","toastNotdone");
                }
                else if(errFlag.equals("0"))
                {
                    if(jsonObject.has("likes"))
                    {
                        jsonArray = jsonObject.getJSONArray("likes");
                        int count = 0;
                        while (count < jsonArray.length())
                        {
                            JSONObject JO = jsonArray.getJSONObject(count);
                            received_driver_current_lat = JO.getString("driver_location_lat");
                            received_driver_current_lng = JO.getString("driver_location_lng");
                            String received_driver_location_datetime = JO.getString("driver_location_datetime");
                            location_datetime.setText("Last Seen: "+Fn.getDateName(received_driver_location_datetime));
//                        crn_found_view = (LinearLayout) view.findViewById(R.id.crn_found);
//                        crn_no_view = (TextView) view.findViewById(R.id.crn_no);
//                        vehicle_type_view = (TextView) view.findViewById(R.id.vehicle_type);
                            map.setVisibility(View.VISIBLE);
                            map_view.setVisibility(View.GONE);
                            if(mMap != null)
                            {
                                mMap.clear();
                                mMap = null;
                            }
                            Fn.logD("LocationSuccessCallingMap","LocationSuccessCallingMap");
                            setUpMapIfNeeded();
                            count++;
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else{
            Fn.ToastShort(getActivity(), Constants.Message.SERVER_ERROR);
        }
    }
    @Override
    public void onClick(View v) {
        Fn.logD("BOOKING_DETAILS_FRAGMENT_LIFECYCLE", "onClick Called");
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + callButton.getText().toString()));
        callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(callIntent);
    }
    public void TimerProgramm() {
        Fn.logD("BOOKING_DETAILS_FRAGMENT_LIFECYCLE", "TimerProgramm Called");
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                runOnUiThread(new Runnable() {
                    public void run() {
                        Fn.logD("TimerProgram_running", "TimerProgram_running");
                        if (!stopTimer) {
                            if (mMap != null) {
                                mMap.clear();
                            }
//                            hashMap.clear();
                            String driver_location_url = Constants.Config.ROOT_PATH + "get_driver_location";
                            HashMap<String,String> hashMap = new HashMap<String, String>();
                            hashMap.put("driver_token", received_driver_token);
                            sendVolleyRequest(driver_location_url, hashMap, "vehicle_location");
                        }
                    }
                });
            }
        }, Constants.Config.GET_DRIVER_LOCATION_DELAY, Constants.Config.GET_DRIVER_LOCATION_PERIOD);
    }
    private void setUpMapIfNeeded() {
        Fn.logD("setUpMapIfNeeded", "map_setup" + String.valueOf(mMap));
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
//            Try to obtain the map from the SupportMapFragment.
//            mMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentByTag("MAP_FRAGMENT");
            Fn.logD("mMapFragment", String.valueOf(mMapFragment));
            mMap = mMapFragment.getMap();
            Fn.logD("map_not_null", String.valueOf(mMap));
            // Check if we were successful in obtaining the map.
            if (mMap != null) {

                if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                mMap.setMyLocationEnabled(true);
                if(FullActivity.mGoogleApiClient.isConnected()) {
                    location = Fn.getAccurateCurrentlocation(FullActivity.mGoogleApiClient, getActivity());
                    if (location != null) {
                        double current_lat = location.getLatitude();
                        double current_lng = location.getLongitude();
                        LatLng latlng = new LatLng(current_lat, current_lng);// This methods gets the users current longitude and latitude.
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(latlng));//Moves the camera to users current longitude and latitude
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlng, Constants.Config.MAP_HIGH_ZOOM_LEVEL));//Animates camera and zooms to preferred state on the user's current location.
                        Fn.logD("received_driver_current_lat", received_driver_current_lat);
                        Fn.logD("received_driver_current_lng", received_driver_current_lng);
                        mMap.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(received_driver_current_lat), Double.parseDouble(received_driver_current_lng))).title("Driver"));
//                                mMap.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble("22.6256"), Double.parseDouble("88.3576"))).title("Driver"));
                        String url = makeURL(received_driver_current_lat, received_driver_current_lng, String.valueOf(current_lat), String.valueOf(current_lng));
                        Log.d("made_url", url);
                        HashMap<String,String> hashMap = new HashMap<String,String>();
                        sendVolleyRequest(url, Fn.checkParams(hashMap), "draw_path");
                    }
                }
            }
        }
    }
    public String makeURL(String sourceLat, String sourceLng, String destLat,String destLng){
        StringBuilder urlString = new StringBuilder();
        try {
            urlString.append("https://maps.googleapis.com/maps/api/directions/json");
            urlString.append("?origin=");// from
            urlString.append(URLEncoder.encode(sourceLat,"UTF-8"));
            urlString.append(",");
            urlString.append(URLEncoder.encode(sourceLng,"UTF-8"));
            urlString.append("&destination=");// to
            urlString.append(URLEncoder.encode(destLat,"UTF-8"));
            urlString.append(",");
            urlString.append(URLEncoder.encode(destLng,"UTF-8"));
            urlString.append("&sensor=false&mode=driving&alternatives=true");
            urlString.append("&key="+URLEncoder.encode(getResources().getString(R.string.server_APIkey1), "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return urlString.toString();
    }
    public void drawPath(String  result) {
        Fn.logD("DrawPathRunning", "DrawPathRunning");
        Fn.logD("JsonString", result);
        try {
            //Tranform the string into a json object
            final JSONObject json = new JSONObject(result);
            JSONArray routeArray = json.getJSONArray("routes");
            JSONObject routes = routeArray.getJSONObject(0);
            JSONObject overviewPolylines = routes.getJSONObject("overview_polyline");
            String encodedString = overviewPolylines.getString("points");
            List<LatLng> list = decodePoly(encodedString);
            Polyline line = mMap.addPolyline(new PolylineOptions()
                    .addAll(list)
                    .width(12)
                    .color(Color.parseColor("#05b1fb"))//Google maps blue color
                    .geodesic(true)
            );
            JSONArray legsArray = routes.getJSONArray("legs");
            JSONObject legs = legsArray.getJSONObject(0);
            JSONObject distance = legs.getJSONObject("distance");
            String distance_km  = distance.getString("text");
            JSONObject duration = legs.getJSONObject("duration");
            String duration_min  = duration.getString("text");
//                      Fn.logD("distance_km",distance_km);
            ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("");
            ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(duration_min + " ( " + distance_km + " ) ");
            Fn.logD("PolyLine Added", "PolyLineAdded");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private List<LatLng> decodePoly(String encoded) {
        Fn.logD("DecodePoly Running", "DecodePoly Running");
        List<LatLng> poly = new ArrayList<LatLng>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;
            LatLng p = new LatLng( (((double) lat / 1E5)),
                    (((double) lng / 1E5) ));
            poly.add(p);
        }
        return poly;
    }
    @Override
    public void onStart() {
        super.onStart();
//        setUpMapIfNeeded();
        Fn.logD("BOOKING_DETAILS_FRAGMENT_LIFECYCLE", "onStart Called");
    }
    //start of extra method
    @Override
    public void onResume() {
        super.onResume();
        Fn.startAllVolley(requestQueue);
        stopTimer = false;
        Fn.logD("BOOKING_DETAILS_FRAGMENT_LIFECYCLE", "onResume Called");
    }
    @Override
    public void onPause() {
        super.onPause();
        Fn.stopAllVolley(requestQueue);
        Fn.logD("BOOKING_DETAILS_FRAGMENT_LIFECYCLE", "onPause Called");
        stopTimer = true;
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(timer != null){
            timer.cancel();
            timer = null;
        }
        if(mMap != null){
            mMap = null;
        }
        FullActivity.fragmentManager.beginTransaction().remove(getChildFragmentManager().findFragmentByTag("MAP_FRAGMENT")).commitAllowingStateLoss();
        Fn.cancelAllRequest(requestQueue, TAG);
        Fn.logD("BOOKING_DETAILS_FRAGMENT_LIFECYCLE", "onDestroyView Called");
    }
    //end of extra method
    @Override
    public void onStop() {
        super.onStop();
        Fn.logD("BOOKING_DETAILS_FRAGMENT_LIFECYCLE", "onStop Called");
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        Fn.logD("BOOKING_DETAILS_FRAGMENT_LIFECYCLE", "onDestroy Called");
    }
    @Override
    public void onDetach() {
        super.onDetach();
        Fn.logD("BOOKING_DETAILS_FRAGMENT_LIFECYCLE", "onDetach Called");

    }
    private void ErrorDialog(String Title,String Message){
        Fn.showDialog(getActivity(), Title, Message);
    }
}
