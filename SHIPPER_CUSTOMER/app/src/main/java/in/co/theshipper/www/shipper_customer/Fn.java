package in.co.theshipper.www.shipper_customer;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Application;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.Provider;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TimeZone;

/**
 * Created by GB on 3/4/2016.
 */
public class Fn {
    protected static void logD(String key, String value) {
        Log.d(key, value);
    }

    protected static void logE(String key, String value) {
        Log.e(key, value);
    }

    protected static void logV(String key, String value) {
        Log.v(key, value);
    }

    protected static void logW(String key, String value) {
        Log.w(key, value);
    }

    protected static void putPreference(Context context, String key, String value) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putString(key, String.valueOf(value)).commit();
    }

    protected static String getPreference(Context context, String key) {
        String value = PreferenceManager.getDefaultSharedPreferences(context).getString(key, "defaultStringIfNothingFound");
        return value;
    }

    protected static void Toast(Context context, String string) {
        Toast.makeText(context, string, Toast.LENGTH_SHORT).show();
    }
    protected static void ToastShort(Context context, String string) {Toast.makeText(context, string, Toast.LENGTH_SHORT).show();}

    protected static void showGpsSettingDialog(final Context context) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        //Setting Dialog Title
        alertDialog.setTitle(R.string.GPSAlertDialogTitle);
        //Setting Dialog Message
        alertDialog.setMessage(R.string.GPSAlertDialogMessage);
        //On Pressing Setting button
        alertDialog.setPositiveButton(R.string.action_settings, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                context.startActivity(intent);
                dialog.dismiss();
            }
        });
        //On pressing cancel button
        alertDialog.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
//                    ((MainActivity)context).moveTaskToBack(true);
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(1);
                dialog.dismiss();
            }
        });
        alertDialog.setOnKeyListener(new Dialog.OnKeyListener() {

            @Override
            public boolean onKey(DialogInterface arg0, int keyCode,
                                 KeyEvent event) {
                // TODO Auto-generated method stub
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    //alertDialog.dismiss();
//                    ((MainActivity)context).moveTaskToBack(true);
                    android.os.Process.killProcess(android.os.Process.myPid());
                    System.exit(1);
                }
                return true;
            }
        });
        alertDialog.show();
    }

    protected static void showNetworkSettingDialog(final Context context) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        //Setting Dialog Title
        alertDialog.setTitle(R.string.NetworkAlertDialogTitle);
        //Setting Dialog Message
        alertDialog.setMessage(R.string.NetworkAlertDialogMessage);
        //On Pressing Setting button
        alertDialog.setPositiveButton(R.string.action_settings, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_SETTINGS);
                context.startActivity(intent);
                dialog.dismiss();
            }
        });
        //On pressing cancel button
        alertDialog.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
//                ((MainActivity)context).moveTaskToBack(true);
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(1);
                dialog.dismiss();
            }
        });
        alertDialog.setOnKeyListener(new Dialog.OnKeyListener() {

            @Override
            public boolean onKey(DialogInterface arg0, int keyCode,
                                 KeyEvent event) {
                // TODO Auto-generated method stub
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    //alertDialog.dismiss();
//                    ((MainActivity)context).moveTaskToBack(true);
                    android.os.Process.killProcess(android.os.Process.myPid());
                    System.exit(1);
                }
                return true;
            }
        });
        alertDialog.show();
    }

    protected static void showDialog(Context context, String title, String message) {

        AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(context);
        alertDialog2.setTitle(title);
        alertDialog2.setMessage(message);
        alertDialog2.setCancelable(false);
        alertDialog2.setNegativeButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        alertDialog2.show();
    }
    protected static void showProgressDialog(String message,Context ctx){
        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(ctx);
        progressDialog.setMessage(message);
        progressDialog.setIndeterminate(true);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                progressDialog.dismiss();
            }
        }, Constants.Config.PROGRESSBAR_DELAY);  // 3000 milliseconds
    }
    protected static void showProgressDialogLong(String message,Context ctx){
        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(ctx);
        progressDialog.setMessage(message);
        progressDialog.setIndeterminate(true);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
    }
    protected static String getDateTimeNow() {
        TimeZone tz = TimeZone.getTimeZone("GMT+05:30");
        Calendar c = Calendar.getInstance(tz);
        Date date = c.getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String strDate = df.format(date);
        return strDate;
    }

    protected static long getDateTimeNowMillis() {
        TimeZone tz = TimeZone.getTimeZone("GMT+05:30");
        Calendar c = Calendar.getInstance(tz);
        Date date = c.getTime();
        long time = date.getTime();
        return time;
    }

    protected static String getDate(long dateInMillis) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = formatter.format(new Date(dateInMillis));
        return dateString;
    }

    protected static boolean CheckJsonError(String json_string) {
        String errFlag, errMsg;
        Boolean flag = false;
        JSONObject jsonObject;
        JSONArray jsonArray;
        Fn.logD("json_string", json_string);
        try {
            jsonObject = new JSONObject(json_string);
            errFlag = jsonObject.getString("errFlag");
            if (errFlag.equals("1")) {
                flag = true;
            } else if (errFlag.equals("0")) {
                flag = false;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return flag;
    }

    protected static class DownloadImage extends AsyncTask<String, Void, Bitmap> {
        ImageView imageView;
        Context ctx;

        public DownloadImage(ImageView imageView, Context ctx) {
            this.imageView = imageView;
            this.ctx = ctx;
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            String image_path = params[0];
            Bitmap return_param = BitmapFactory.decodeResource(ctx.getResources(), R.mipmap.ic_launcher);

            try {
                URL image_url = new URL(image_path);
                HttpURLConnection httpURLConnection = (HttpURLConnection) image_url.openConnection();
                httpURLConnection.setConnectTimeout(1000 * 30);
                httpURLConnection.setReadTimeout(1000 * 30);
                return_param = BitmapFactory.decodeStream((InputStream) httpURLConnection.getContent(), null, null);
                //Fn.logd("background_done", "background_done");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return return_param;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (bitmap != null) {
                Fn.logD("background_received", "received");
                imageView.setImageBitmap(bitmap);
            }
        }
    }

    protected static String AndroidToServer(Context ctx, String... params) {
        Fn.logD("AndroidToServer", "AndroidToServer");
        String JSON_STRING;
        String return_param = "";
        String server_url = params[0];
        String data = params[1];
        try {
            URL url = new URL(server_url);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            OutputStream OS = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(OS, "UTF-8"));
            bufferedWriter.write(data);
            bufferedWriter.flush();
            bufferedWriter.close();
            OS.close();
            InputStream IS = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(IS));
            StringBuilder stringBuilder = new StringBuilder();
            //Log.d("JSON_STRING_PREV",JSON_STRING+"JSON_STRING_PREV");
            while ((JSON_STRING = bufferedReader.readLine()) != null) {
                stringBuilder.append(JSON_STRING + "\n");
            }
            bufferedReader.close();
            IS.close();
            Fn.logE("JSON_STRING", JSON_STRING + "JSON_STRING");
            return_param = stringBuilder.toString().trim();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            Fn.logE("error1", e.toString());
            return_param = "{errFlag='1',errMsg='nothing'}";
//            Fn.Toast(ctx,"Error while connectng to server");
        } catch (IOException e) {
            e.printStackTrace();
            Fn.logE("error2", e.toString());
            return_param = "{errFlag='1',errMsg='nothing'}";
        }
        return return_param;
    }

    protected static <T> void addToRequestQue(RequestQueue requestQueue, Request<T> request, Context ctx) {
        if (requestQueue != null) {
            requestQueue.add(request);
        } else {
            requestQueue = Volley.newRequestQueue(ctx);
            requestQueue.add(request);
        }
    }

    protected static void cancelAllRequest(RequestQueue mRequestQueue, String tag) {
//        mRequestQueue.cancelAll(TAG);
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

    protected static void stopAllVolley(RequestQueue mRequestQueue) {
        if (mRequestQueue != null) {
            mRequestQueue.stop();
        }
    }

    protected static void startAllVolley(RequestQueue mRequestQueue) {
        if (mRequestQueue != null) {
            mRequestQueue.start();
        }
    }

    protected static String VehicleName(String vehicle_id, Context context) {
        String vehicle_name = "";
        DBController controller = new DBController(context);
        String query1 = "SELECT " + controller.VEHICLE_NAME + " FROM " + controller.TABLE_VIEW_VEHICLE_TYPE + " WHERE " + controller.VEHICLETYPE_ID + " = " + vehicle_id;
        Fn.logD("Query:", query1);
        SQLiteDatabase database = controller.getWritableDatabase();
        Cursor vehic = database.rawQuery(query1, null);
        Fn.logD("vehicle_cusrsor:", String.valueOf(vehic));

        try {
            if (vehic.moveToFirst()) {
                do {
                    vehicle_name = vehic.getString(0);
                } while (vehic.moveToNext());
            }
        } catch (Exception e) {
            System.out.println("no rows");
        }
        return vehicle_name;
    }

    protected static String VehicleID(String vehicle_name, Context context) {
        String vehicle_id = "";
        DBController controller = new DBController(context);
        String query1 = "SELECT " + controller.VEHICLETYPE_ID + " FROM " + controller.TABLE_VIEW_VEHICLE_TYPE + " WHERE " + controller.VEHICLE_NAME + " = '" + vehicle_name + "' ";
        Fn.logD("Query:", query1);
        SQLiteDatabase database = controller.getWritableDatabase();
        Cursor vehic = database.rawQuery(query1, null);
        Fn.logD("vehicle_cusrsor:", String.valueOf(vehic));

        try {
            if (vehic.moveToFirst()) {
                do {
                    vehicle_id = vehic.getString(0);
                } while (vehic.moveToNext());
            }
        } catch (Exception e) {
            System.out.println("no rows");
        }
        return vehicle_id;
    }

    protected static String getDateName(String datetime) {
        String return_date = "";
        try {
            DateFormat f = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            DateFormat return_format = new SimpleDateFormat("EEE, MMM dd yyyy, hh:mm a");
            Date d = f.parse(datetime);
            String date_return = return_format.format(d);
            DateFormat date = new SimpleDateFormat("dd/MM/yyyy");
            DateFormat time = new SimpleDateFormat("hh:mm a");
            String time_passed = time.format(d);
            String date_passed = date.format(d);
            TimeZone tz = TimeZone.getTimeZone("GMT+05:30");
            Calendar c = Calendar.getInstance(tz);
            Calendar c1 = Calendar.getInstance(tz);
            Calendar c2 = Calendar.getInstance(tz);
            Calendar c3 = Calendar.getInstance(tz);
            c.setTime(d);
            c2.add(Calendar.DATE, -1);
            c3.add(Calendar.DATE, 1);
            if (c.get(Calendar.YEAR) == c1.get(Calendar.YEAR) && c.get(Calendar.DAY_OF_YEAR) == c1.get(Calendar.DAY_OF_YEAR)) {
                return_date = "Today " + time_passed;
            } else if (c.get(Calendar.YEAR) == c2.get(Calendar.YEAR) && c.get(Calendar.DAY_OF_YEAR) == c2.get(Calendar.DAY_OF_YEAR)) {
                return_date = "Yesterday " + time_passed;
            } else if (c.get(Calendar.YEAR) == c3.get(Calendar.YEAR) && c.get(Calendar.DAY_OF_YEAR) == c3.get(Calendar.DAY_OF_YEAR)) {
                return_date = "Tomorrow " + time_passed;
            } else {
                return_date = date_return;
            }

//            all_date = "yesterday_date"+yesterday_date+"today_date"+today_date+"tomorrow_date"+tomorrow_date;
//            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//            String strDate = df.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return return_date;
    }

    protected static int getVehicleImage(int vehicletype_id) {
        int flag = 0;
        switch (vehicletype_id) {
            case 1:
                flag = R.drawable.abc_checkbox_vehicle_1;
                break;
            case 2:
                flag = R.drawable.abc_checkbox_vehicle_2;
                break;
            case 3:
                flag = R.drawable.abc_checkbox_vehicle_3;
                break;
            case 4:
                flag = R.drawable.abc_checkbox_vehicle_4;
                break;
            case 5:
                flag = R.drawable.abc_checkbox_vehicle_5;
                break;
            case 6:
                flag = R.drawable.abc_checkbox_vehicle_6;
                break;
            case 7:
                flag = R.drawable.abc_checkbox_vehicle_7;
                break;
            case 9:
                flag = R.drawable.abc_checkbox_vehicle_9;
                break;
        }
        return flag;
    }

    protected static boolean isMyServiceRunning(Class<?> serviceClass, Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    protected static HashMap<String, String> checkParams(HashMap<String, String> map) {
        Iterator<HashMap.Entry<String, String>> it = map.entrySet().iterator();
        while (it.hasNext()) {
            HashMap.Entry<String, String> pairs = (HashMap.Entry<String, String>) it.next();
            if (pairs.getValue() == null) {
                map.put(pairs.getKey(), "");
            }
        }
        return map;
    }
    protected static Bundle CheckBundle(Bundle bundle) {
        if (bundle != null) {
            Set<String> keys = bundle.keySet();
            Iterator<String> it = keys.iterator();
            while (it.hasNext()) {
                String key = it.next();
                if(bundle.get(key) == null){
                    bundle.putString(key,"");
                }
            }
        }
        return bundle;
    }
    protected static Intent CheckIntent(Intent intent) {
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            Set<String> keys = bundle.keySet();
            Iterator<String> it = keys.iterator();
            while (it.hasNext()) {
                String key = it.next();
                if(bundle.get(key) == null){
                    intent.putExtra(key, "");
                }
            }
        }
        return intent;
    }
    protected static String getValueFromBundle(Bundle b, String key) {
        String value = String.valueOf(b.get(key));
        if (value == null) {
            value = "";
        }
        return value;
    }
    protected static boolean isGpsEnabled(Context ctx) {
        LocationManager locationManager = (LocationManager) ctx.getSystemService(Context.LOCATION_SERVICE);
        //getting GPS status
        Boolean isGPSenabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (isGPSenabled == true) {
            return true;
        } else {
            return false;
        }
    }

    protected static boolean isInternetEnabled(Context ctx) {
        LocationManager locationManager = (LocationManager) ctx.getSystemService(Context.LOCATION_SERVICE);
        //getting GPS status
        Boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (isNetworkEnabled == true) {
            return true;
        }
        return false;
    }

    protected static boolean isNetworkEnabled(Context ctx) {
        ConnectivityManager conMgr = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();
        if (netInfo == null || !netInfo.isConnected() || !netInfo.isAvailable()) {
            return false;
        } else {
            return true;
        }
    }

    protected static void delay(int time) {
        for (int i = 0; i < 1000 * time; i++) ;
    }


    public static void showGpsAlertDialg(GoogleApiClient mGoogleApiClient, final Context ctx){
         final int REQUEST_CHECK_SETTINGS = 0x1;
        if(mGoogleApiClient != null) {
            LocationRequest locationRequest = LocationRequest.create();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationRequest.setInterval(30 * 1000);
            locationRequest.setFastestInterval(5 * 1000);
            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                    .addLocationRequest(locationRequest);
            builder.setAlwaysShow(true); //this is the key ingredient

            PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());
            result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
                @Override
                public void onResult(LocationSettingsResult result) {
                    final Status status = result.getStatus();
                    final LocationSettingsStates state = result.getLocationSettingsStates();
                    switch (status.getStatusCode()) {
                        case LocationSettingsStatusCodes.SUCCESS:
                            // All location settings are satisfied. The client can initialize location
                            // requests here.
                            break;
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            // Location settings are not satisfied. But could be fixed by showing the user
                            // a dialog.
                            try {
                                // Show the dialog by calling startResolutionForResult(),
                                // and check the result in onActivityResult().
                                status.startResolutionForResult((Activity) ctx, REQUEST_CHECK_SETTINGS);
                            } catch (IntentSender.SendIntentException e) {
                                // Ignore the error.
                            }
                            break;
                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            // Location settings are not satisfied. However, we have no way to fix the
                            // settings so we won't show the dialog.
                            break;
                    }
                }
            });
        }
    }
    protected static Location getCurrentlocation(Context ctx) {
        boolean isGPSEnabled = false;
        boolean isNetworkEnabled = false;
        boolean isGPSTrackingEnabled = false;
        String provider_info = "";
        Location location = null;
        LocationManager locationManager = (LocationManager) ctx.getSystemService(Context.LOCATION_SERVICE);
        //getting GPS status
        isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        //getting network status
        isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        // Try to get location if you GPS Service is enabled
        if (isGPSEnabled) {
            isGPSTrackingEnabled = true;
//            Fn.logD(TAG, "Application use GPS Service");
                        /*
                         * This provider determines location using
                         * satellites. Depending on conditions, this provider may take a while to return
                         * a location fix.
                         */
            provider_info = LocationManager.GPS_PROVIDER;
        } else if (isNetworkEnabled) { // Try to get location if you Network Service is enabled
            isGPSTrackingEnabled = true;

//            Fn.logD(TAG, "Application use Network State to get GPS coordinates");
                    /*
                     * This provider determines location based on
                     * availability of cell tower and WiFi access points. Results are retrieved
                     * by means of a network lookup.
                     */
            provider_info = LocationManager.NETWORK_PROVIDER;
        } else {
            Fn.showGpsSettingDialog(ctx);
        }
        // Application can use GPS or Network Provider
        if (!provider_info.isEmpty()) {
            Fn.logD("location_manager", String.valueOf(locationManager));
            if (locationManager != null) {
                Fn.logD("location_manager_new", String.valueOf(locationManager));
                if (ActivityCompat.checkSelfPermission(ctx, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(ctx, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    location = locationManager.getLastKnownLocation(provider_info);
                    Fn.logD("permission_check_done1", "permission_check_done1");
                }
                if (location != null) {
                    return location;
                } else {
                    locationManager.requestLocationUpdates(
                            provider_info,
                            Constants.Config.MIN_TIME_BW_UPDATES,
                            Constants.Config.MIN_DISTANCE_CHANGE_FOR_UPDATES, (android.location.LocationListener) ctx);
                    return location;
                }
            }

        }
        return location;
    }
    protected static Location getAccurateCurrentlocation(GoogleApiClient mGoogleApiClient,Context ctx){
        if(Fn.isGpsEnabled(ctx)) {
            LocationRequest mLocationRequest = LocationRequest.create()
                    .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                    .setInterval(2 * 1000)        // 10 seconds, in milliseconds
                    .setFastestInterval(1 * 1000); // 1 second, in milliseconds
            Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            if (location == null) {
                LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, (LocationListener) ctx);
                return location;
            } else {
                return location;
            }
        }else{
            showGpsAutoEnableRequest(mGoogleApiClient,ctx);
            return null;
        }
    }
    public static void  showGpsAutoEnableRequest(GoogleApiClient mGoogleApiClient, final Context ctx) {
         final int REQUEST_CHECK_SETTINGS = 0x1;
        if(mGoogleApiClient != null) {
            LocationRequest locationRequest = LocationRequest.create();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationRequest.setInterval(Constants.Config.GPS_INTERVAL);
            locationRequest.setFastestInterval(Constants.Config.GPS_FASTEST_INTERVAL);
            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                    .addLocationRequest(locationRequest);
            builder.setAlwaysShow(true); //this is the key ingredient

            PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());
            result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
                @Override
                public void onResult(LocationSettingsResult result) {
                    final Status status = result.getStatus();
                    final LocationSettingsStates state = result.getLocationSettingsStates();
                    switch (status.getStatusCode()) {
                        case LocationSettingsStatusCodes.SUCCESS:
                            // All location settings are satisfied. The client can initialize location
                            // requests here.
                            break;
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            // Location settings are not satisfied. But could be fixed by showing the user
                            // a dialog.
                            try {
                                // Show the dialog by calling startResolutionForResult(),
                                // and check the result in onActivityResult().
                                status.startResolutionForResult((Activity) ctx, REQUEST_CHECK_SETTINGS);
                            } catch (IntentSender.SendIntentException e) {
                                // Ignore the error.
                            }
                            break;
                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            // Location settings are not satisfied. However, we have no way to fix the
                            // settings so we won't show the dialog.
                            break;
                    }
                }
            });
        }
    }
    protected static Location getAccurateCurrentlocationService(GoogleApiClient mGoogleApiClient,Context ctx){
        if(Fn.isGpsEnabled(ctx)) {
            LocationRequest mLocationRequest = LocationRequest.create()
                    .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                    .setInterval(2 * 1000)        // 10 seconds, in milliseconds
                    .setFastestInterval(1 * 1000); // 1 second, in milliseconds
            Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            if (location == null) {
                LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, (LocationListener) ctx);
                return location;
            } else {
                return location;
            }
        }else{
            return null;
        }
    }
//    public static void  showGpsAutoEnableRequestService(GoogleApiClient mGoogleApiClient) {
//        if(mGoogleApiClient != null) {
//            LocationRequest locationRequest = LocationRequest.create();
//            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
//            locationRequest.setInterval(Constants.Config.GPS_INTERVAL);
//            locationRequest.setFastestInterval(Constants.Config.GPS_FASTEST_INTERVAL);
//            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
//                    .addLocationRequest(locationRequest);
//            builder.setAlwaysShow(true); //this is the key ingredient
//            PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());
//
//            result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
//                @Override
//                public void onResult(LocationSettingsResult result) {
//
//                }
//            });
//        }
//    }
}
