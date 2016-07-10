package in.co.theshipper.www.shipper_customer;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity
{
    protected  String TAG = MainActivity.class.getName();
    protected  EditText MOBILE_NO;
    protected  String mobile_no;
    protected  TextView textView;
    protected  Boolean isNetworkEnabled = false;
    protected  LocationManager locationManager;
    protected  RequestQueue requestQueue;
    protected  HashMap<String,String> hashMap;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Fn.logE("MAIN_ACTIVITY_LIFECYCLE", "onCreate called");
        MOBILE_NO = (EditText) findViewById(R.id.editText);

    }
    @Override
    protected void onRestart() {
        super.onRestart();
        Fn.logE("MAIN_ACTIVITY_LIFECYCLE", "onRestart called");
    }
    @Override
    protected void onStart() {
        super.onStart();
        Fn.logE("MAIN_ACTIVITY_LIFECYCLE", "onStart called");
    }
    public boolean isOnline() {
        ConnectivityManager conMgr = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();

        if(netInfo == null || !netInfo.isConnected() || !netInfo.isAvailable()){
//            Toast.makeText(context, "No Internet connection!", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }
    @Override
    protected void onResume()
    {
        super.onResume();
        Fn.logE("MAIN_ACTIVITY_LIFECYCLE","onResume called");
    }
    @Override
    protected void onPause() {
        super.onPause();
        Fn.logE("MAIN_ACTIVITY_LIFECYCLE","onPause called");
    }
    @Override
    protected void onStop() {
        super.onStop();
        Fn.logE("MAIN_ACTIVITY_LIFECYCLE","onStop called");
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Fn.logE("MAIN_ACTIVITY_LIFECYCLE", "onDestroy called");
    }
    public void userReg(View view){
        Fn.logD("MAIN_ACTIVITY_LIFECYCLE", "userReg called");
        if(checkValidation()) {
//            Fn.showProgressDialog(Constants.Message.LOADING,this);
            String reg_url = Constants.Config.ROOT_PATH + "customer_registration";
            Random ran = new Random();
            int otp = (100000 + ran.nextInt(900000));
            mobile_no = MOBILE_NO.getText().toString();
            hashMap = new HashMap<String, String>();
            hashMap.put("mobile_no", mobile_no);
            hashMap.put("OTP", String.valueOf(otp));
            Fn.putPreference(this, "OTP", String.valueOf(otp));
            Fn.putPreference(this, "mobile_no", mobile_no);
            Fn.logD("OTP", Fn.getPreference(this, "OTP"));
            sendVolleyRequest(reg_url,Fn.checkParams(hashMap));
        }
        else
            Toast.makeText(MainActivity.this, "Form contains error", Toast.LENGTH_LONG).show();
    }
    private boolean checkValidation() {
        boolean ret = true;

        //if (!Validation.hasText(etNormalText)) ret = false;
        //if (!Validation.isEmailAddress(etEmailAddrss, true)) ret = false;
        if (!FormValidation.isPhoneNumber(MOBILE_NO, true)) ret = false;

        return ret;
    }
    public void sendVolleyRequest(String URL, final HashMap<String,String> hMap){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Fn.logD("onResponse", String.valueOf(response));
                registerSuccess();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Fn.logD("onErrorResponse", String.valueOf(error));
                ErrorDialog(Constants.Title.NETWORK_ERROR, Constants.Message.NETWORK_ERROR);
            }
        }){
            @Override
            protected HashMap<String,String> getParams(){
                return hMap;
            }
        };
        stringRequest.setTag(TAG);
        Fn.addToRequestQue(requestQueue, stringRequest, this);
    }
    private void ErrorDialog(String Title,String Message){
        Fn.showDialog(this, Title, Message);
    }
    public void registerSuccess(){
        Intent intent = new Intent(this, OtpVerification.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        System.out.println(" ### Back pressed!!!");
    }
}
