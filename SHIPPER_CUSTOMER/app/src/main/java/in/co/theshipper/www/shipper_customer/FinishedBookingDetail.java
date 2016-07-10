package in.co.theshipper.www.shipper_customer;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.maps.SupportMapFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 */
public class FinishedBookingDetail extends Fragment{

//    protected final String booking_status_url = Constants.Config.ROOT_PATH+"get_booking_status";
    protected RequestQueue requestQueue;
    protected HashMap<String,String> hashMap;
    private String TAG = FinishedBookingDetail.class.getName();
    protected View view;
    protected Context context;
    private LinearLayout map_view,crn_found_view, driver_found_view,map;
    private TextView driver_name_view, crn_no_view, vehicle_type_view,booking_detail;
    private Button callButton, driver_mobile_no_view;
    private SupportMapFragment mMapFragment;
    private String errFlag, errMsg,received_vehicletype_id,received_pickup_point,received_dropoff_point, received_booking_datetime, received_driver_token, received_is_booked,received_is_active,received_driver_current_lat,received_driver_current_lng,
            received_driver_vehicle_no,  received_driver_name, received_driver_mobile_no;
    private TextView errFlagt, errMsgt,received_vehicletype_idt,received_pickup_pointt,received_dropoff_pointt, received_booking_datetimet, received_driver_tokent, received_is_bookedt,received_is_activet,received_driver_current_latt,received_driver_current_lngt,
            received_driver_vehicle_nott,  received_driver_namet, received_driver_mobile_not;
    ImageView i;
    myDialog d;
    TextView received_crn_not;
    private String crn_no="";
    public FinishedBookingDetail() {
        // Required empty public constructor
        super.onAttach(context);
        this.context = context;
        Fn.logD("COMPLETED_BOOKING_DETAILS_FRAGMENT_LIFECYCLE", "onAttach Called");
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (container == null) {
            return null;
        } else {
            view = inflater.inflate(R.layout.fragment_finished_booking_detail, container, false);
            // Inflate the layout for this fragment
            Fn.logD("COMPLETED_BOOKING_DETAILS_FRAGMENT_LIFECYCLE", "onCreateView Called");
//        Fn.logD("booking_status_url",booking_status_url);
            Bundle bundle = this.getArguments();
            crn_no = bundle.getString("crn_no");
            Fn.logD("bundle_crn_no", crn_no);
            System.out.println(crn_no);
            //booking_detail = (TextView) view.findViewById(R.id.booking_detail);
            Fn.logD("booking_detail_textview", String.valueOf(booking_detail));
            i=(ImageView)view.findViewById(R.id.imageView);
            i.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    d = new myDialog();
                    d.show(getActivity().getFragmentManager(), "ABC");
                }
            });

                    return view;
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Fn.logD("COMPLETED_BOOKING_DETAILS_FRAGMENT_LIFECYCLE", "onViewCreated Called");
        super.onViewCreated(view, savedInstanceState);
        HashMap<String,String>  hashMap= new HashMap<String,String>();
        String booking_status_url = Constants.Config.ROOT_PATH+"get_completed_booking_status";
        // String CrnNo = Fn.getPreference(getActivity(),"current_crn_no");
        String user_token = Fn.getPreference(getActivity(),"user_token");
        hashMap.put("crn_no", crn_no);
        hashMap.put("user_token", user_token);
        sendVolleyRequest(booking_status_url, Fn.checkParams(hashMap));
    }

    public void sendVolleyRequest(String URL, final HashMap<String,String> hMap){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Fn.logD("onResponse_booking_status", String.valueOf(response));
                bookingStatusSuccess(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Fn.logD("onErrorResponse", String.valueOf(error));
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
        Fn.logD("COMPLETED_BOOKING_DETAILS_FRAGMENT_LIFECYCLE", "bookingStatusSuccess Called");
        if(!Fn.CheckJsonError(response)){
//            Fn.logD("bookingStatusSuccess", "bookingStatusSuccess Called");
            Fn.logD("received_json", response);
            JSONObject jsonObject;
            JSONArray jsonArray;

            try {
                jsonObject = new JSONObject(response);
                //jsonArray = jsonObject.getJSONArray("likes");
                errFlag = jsonObject.getString("errFlag");
                errMsg = jsonObject.getString("errMsg");
                if(errFlag.equals("1")){
                    Fn.logD("toastNotdone","toastNotdone");
                }
                else if(errFlag.equals("0"))
                {
                    if(jsonObject.has("likes"))
                    {
                        jsonArray = jsonObject.getJSONArray("likes");
                        int count = 0;
//                        crn_found_view = (LinearLayout) view.findViewById(R.id.crn_found);
//                        crn_no_view = (TextView) view.findViewById(R.id.crn_no);
//                        vehicle_type_view = (TextView) view.findViewById(R.id.vehicle_type);
                        while (count < jsonArray.length())
                        {
                            Fn.logD("likes_entered", "likes_entered");
                            JSONObject JO = jsonArray.getJSONObject(count);
                            String received_pickup_point = JO.getString("pickup_point");
                            String received_dropoff_point = JO.getString("dropoff_point");
                            String received_vehicletype_id = JO.getString("vehicletype_id");
                            String received_booking_datetime = JO.getString("booking_datetime");
                            String total_fare = JO.getString("total_fare");
                            String received_crn_no = JO.getString("crn_no");
//                            crn_no_view.setText(received_crn_no);
                            String received_truck = Fn.VehicleName(received_vehicletype_id, getActivity());
                            TextView received_truckt;
                            received_driver_name = JO.getString("driver_name");
                            received_driver_mobile_no = JO.getString("driver_mobile_no");

                            String detail = "Pickup Point: "+received_pickup_point+System.getProperty("line.separator")+System.getProperty("line.separator")+
                                    "Dropoff Point: "+received_dropoff_point+System.getProperty("line.separator")+System.getProperty("line.separator")+
                                    "Booking Date & Time: "+Fn.getDateName(received_booking_datetime)+System.getProperty("line.separator")+System.getProperty("line.separator")+
                                    "Truck: "+received_truck+System.getProperty("line.separator")+System.getProperty("line.separator")+
                                    "Total Fare: "+total_fare+System.getProperty("line.separator")+System.getProperty("line.separator")+
                                    "Driver: "+received_driver_name+System.getProperty("line.separator")+System.getProperty("line.separator")+
                                    "Driver Mobile No: "+received_driver_mobile_no+System.getProperty("line.separator")+System.getProperty("line.separator")+
                                    "CRN No: "+received_crn_no+System.getProperty("line.separator");
                        //    booking_detail.setText(detail);
                            received_pickup_pointt=(TextView)view.findViewById(R.id.pickup);
                            received_pickup_pointt.setText(received_pickup_point);
                            received_dropoff_pointt=(TextView)view.findViewById(R.id.drop);
                            received_dropoff_pointt.setText(received_dropoff_point);
                            received_booking_datetimet=(TextView)view.findViewById(R.id.Datetime);
                            received_booking_datetimet.setText(received_booking_datetime);
                            received_driver_namet=(TextView)view.findViewById(R.id.drivername);
                            received_driver_namet.setText(received_driver_name);
                            received_driver_mobile_not=(TextView)view.findViewById(R.id.mobno);
                            received_driver_mobile_not.setText(received_driver_mobile_no);
                            received_crn_not=(TextView)view.findViewById(R.id.crn);
                            received_crn_not.setText(received_crn_no);
                            received_truckt=(TextView)view.findViewById(R.id.truck);
                            received_truckt.setText(received_truck);

                            count++;
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}
