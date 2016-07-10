package in.co.theshipper.www.shipper_customer;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.location.places.ui.SupportPlaceAutocompleteFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * A simple {@link Fragment} subclass.
 */
public class BookNow extends Fragment implements View.OnClickListener {
    View view;
    PlaceAutocompleteFragment pickup_point, dropoff_point;
    Button get_quote;
    String pickup_address, dropoff_address, pickuppoint_name, dropoffpoint_name, booking_datetime;
    LatLng pickup_latlng, dropoff_latlng;
    Double pickup_lat, pickup_lng, dropoff_lat, dropoff_lng;
    DatePicker datePicker;
    TimePicker timePicker;
    Location location;
    String provider_info;
    LatLng southwest,northeast;
    boolean isGPSEnabled = false;
    // flag for network status
    boolean isNetworkEnabled = false;
    // flag for GPS Tracking is enabled
    boolean isGPSTrackingEnabled = false;


    public BookNow() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (container == null) {
            return null;
        } else {

            view = inflater.inflate(R.layout.fragment_book_now, container, false);
            datePicker = (DatePicker) view.findViewById(R.id.date_picker);
            timePicker = (TimePicker) view.findViewById(R.id.time_picker);
            get_quote = (Button) view.findViewById(R.id.get_quote);
            return view;
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        datePicker.setEnabled(false);
        timePicker.setEnabled(false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            datePicker.setMinDate(System.currentTimeMillis() - Constants.Config.MIN_DATE_DURATION);
            datePicker.setMaxDate((System.currentTimeMillis() + Constants.Config.MIN_DATE_DURATION));
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        Fn.logD("BOOKNOW_FRAGMENT", "onActivityCreated");
        get_quote.setOnClickListener(this);
        if(FullActivity.mGoogleApiClient.isConnected()) {
            location = Fn.getAccurateCurrentlocation(FullActivity.mGoogleApiClient,getActivity());
            if (location != null) {
                southwest = new LatLng(location.getLatitude() - 2, location.getLongitude() - 2);
                northeast = new LatLng(location.getLatitude() + 2, location.getLongitude() + 2);
            }
        }
        /*
        * The following code example shows setting an AutocompleteFilter on a PlaceAutocompleteFragment to
        * set a filter returning only results with a precise address.
        */
        AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                .setTypeFilter(AutocompleteFilter.TYPE_FILTER_NONE)
                .build();
        if (pickup_point == null) {
            Fn.logD("BOOKNOW_FRAGMENT", "autocompleteFragment_null");
            pickup_point = (PlaceAutocompleteFragment) PlaceAutocompleteFragment.instantiate(getActivity(), "com.google.android.gms.location.places.ui.PlaceAutocompleteFragment");
            getActivity().getFragmentManager().beginTransaction().replace(R.id.pickup_container, pickup_point).commit();
            Fn.logD("pickup_point_fragment", String.valueOf(pickup_point));
            pickup_point.setFilter(typeFilter);
            if((southwest!=null)&&(northeast!=null))
            {
                System.out.println("**haha**my curn loc is : "+southwest.longitude+" "+southwest.latitude);
                pickup_point.setBoundsBias(new LatLngBounds(southwest,northeast));
            }
            pickup_point.setOnPlaceSelectedListener(new PlaceSelectionListener() {
                @Override
                public void onPlaceSelected(Place place) {
                    Fn.logD("BOOKNOW_FRAGMENT", "onPlaceSelected");
                    pickuppoint_name = (String) place.getName();
                    pickup_address = (String) place.getAddress();
                }

                @Override
                public void onError(Status status) {
                    Fn.logD("BOOKNOW_FRAGMENT", "onError");
                    // TODO: Handle the error.
                    Fn.logD("BOOKNOW_FRAGMENT", "An error occurred: " + status);
                }
            });
        }
        if (dropoff_point == null) {
            Fn.logD("BOOKNOW_FRAGMENT", "autocompleteFragment_null");
            dropoff_point = (PlaceAutocompleteFragment) PlaceAutocompleteFragment.instantiate(getActivity(), "com.google.android.gms.location.places.ui.PlaceAutocompleteFragment");
            getActivity().getFragmentManager().beginTransaction().replace(R.id.dropoff_container, dropoff_point).commit();
            //pickup_point.setHint("Pickup Point");
            dropoff_point.setFilter(typeFilter);
            if((southwest!=null)&&(northeast!=null)) {
                dropoff_point.setBoundsBias(new LatLngBounds(southwest, northeast));
            }
            dropoff_point.setOnPlaceSelectedListener(new PlaceSelectionListener() {
                @Override
                public void onPlaceSelected(Place place) {
                    Fn.logD("BOOKNOW_FRAGMENT", "onPlaceSelected");
                    dropoffpoint_name = (String) place.getName();
                    dropoff_address = (String) place.getAddress();
                }

                @Override
                public void onError(Status status) {
                    Fn.logD("BOOKNOW_FRAGMENT", "onError");
                    // TODO: Handle the error.
                    Fn.logD("BOOKNOW_FRAGMENT", "An error occurred: " + status);
                }
            });
        }
    }
    @Override
    public void onClick (View v){
        Calendar calendar = new GregorianCalendar(datePicker.getYear(),
                datePicker.getMonth(),
                datePicker.getDayOfMonth(),
                timePicker.getCurrentHour(),
                timePicker.getCurrentMinute());
        long datetime = calendar.getTimeInMillis();
        long currentTime = Fn.getDateTimeNowMillis();
        currentTime=currentTime-300000;
        boolean dtval=false;
        if(datetime>=currentTime)
            dtval=true;
        booking_datetime = Fn.getDate(datetime);
        //Fn.putPreference(getActivity(),"selected_booking_datetime",booking_datetime);
        Bundle bundle = new Bundle();
        if (isValid(pickup_address, dropoff_address) && dtval) {
            bundle.putString("selected_pickup_address", pickup_address);
            bundle.putString("selected_dropoff_address", dropoff_address);
            bundle.putString("selected_booking_datetime", booking_datetime);
            FragmentManager fragmentManager = FullActivity.fragmentManager;
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            Fragment fragment = new ConfirmBooking();
            fragment.setArguments(Fn.CheckBundle(bundle));
            transaction.replace(R.id.main_content, fragment, Constants.Config.CURRENT_FRAG_TAG);
            if ((FullActivity.homeFragmentIndentifier == -5)) {
                FullActivity.homeFragmentIndentifier = transaction.commit();
            } else {
                transaction.commit();
                Fn.logD("fragment instanceof Book", "homeidentifier != -1");
            }
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.title_confirm_booking_fragment);
        }
        else{
            if(!isValid(pickup_address, dropoff_address))
            {
                if(!dtval)
                    Toast.makeText(getContext(), "Form Contains Invalid Address and Date Fields", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(getContext(), "Form Contains Invalid Address Field(s)", Toast.LENGTH_SHORT).show();
            }
            else
                Toast.makeText(getContext(), "Form Contains Invalid Date Field", Toast.LENGTH_SHORT).show();
        }
    }
    private boolean isValid(String pickup,String dropoff){
        if(pickup==null)
            return false;
        if(dropoff==null)
            return false;
        if(pickup.length()==0)
            return false;
        if(dropoff.length()==0)
            return false;
        return true;
    }
}