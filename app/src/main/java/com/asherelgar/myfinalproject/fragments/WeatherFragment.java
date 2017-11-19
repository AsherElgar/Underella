package com.asherelgar.myfinalproject.fragments;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.asherelgar.myfinalproject.R;
import com.asherelgar.myfinalproject.models.UserLocation;
import com.asherelgar.myfinalproject.models.WeatherDataSource;
import com.bumptech.glide.Glide;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


/**
 * A simple {@link Fragment} subclass.
 */
public class WeatherFragment extends Fragment implements AdapterView.OnItemClickListener {

    private static final int RC_LOCATION = 1;
    private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
    private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
    private static final String OUT_JSON = "/json";
    private static final String API_KEY = "AIzaSyCz2MYTemt43V757yGWiEUS8sR61TqpZbE";
    protected Context context;
    ProgressBar progressBar;
    TextView tvTemp;
    @BindView(R.id.tvSunset)
    TextView tvSunset;
    Unbinder unbinder;
    @BindView(R.id.ivWeather)
    ImageView ivWeather;
    @BindView(R.id.textView)
    TextView tvPlace;
    @BindView(R.id.imageViewLocation)
    ImageView ivLocation;
    //    @BindView(R.id.etSearchWeather)
//    EditText etSearchWeather;
    @BindView(R.id.ivSearchWeather)
    ImageView ivSearchWeather;
    ImageView ivBackground;
    String locationName;
    Double lat;
    Double lon;
    LocationRequest mRequest;
    LocationListener locationListener;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    LocationManager locationManager;
    FusedLocationProviderClient mFusedLocationProviderClient;
    AutoCompleteTextView autoCompView;
    Location k = null;

    public static String getAddressFromLatLon(Context context, double LATITUDE, double LONGITUDE) {

        String city = "";
        String country = "";
        //Set Address
        try {
            Geocoder geocoder = new Geocoder(context, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null && addresses.size() > 0) {


                String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                city = addresses.get(0).getLocality();
                String state = addresses.get(0).getAdminArea();
                country = addresses.get(0).getCountryName();
                String postalCode = addresses.get(0).getPostalCode();
                String knownName = addresses.get(0).getFeatureName(); // Only if available else return NULL

                Log.d("EEE", "getAddress:  address " + address);
                Log.d("EEE", "getAddress:  city " + city);
                Log.d("EEE", "getAddress:  state " + country);
                Log.d("EEE", "getAddress:  postalCode " + postalCode);
                Log.d("EEE", "getAddress:  knownName " + knownName);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return (city + ", " + country);
    }

    public static ArrayList<String> autocomplete(String input) {
        ArrayList<String> resultList = null;

        HttpURLConnection conn = null;
        StringBuilder jsonResults = new StringBuilder();
        try {
            StringBuilder sb = new StringBuilder(PLACES_API_BASE + TYPE_AUTOCOMPLETE + OUT_JSON);
            sb.append("?key=" + API_KEY);
            sb.append("&components=");
            sb.append("&input=" + URLEncoder.encode(input, "utf8"));

            URL url = new URL(sb.toString());

            System.out.println("URL: " + url);
            conn = (HttpURLConnection) url.openConnection();
            InputStreamReader in = new InputStreamReader(conn.getInputStream());

            // Load the results into a StringBuilder
            int read;
            char[] buff = new char[1024];
            while ((read = in.read(buff)) != -1) {
                jsonResults.append(buff, 0, read);
            }
        } catch (MalformedURLException e) {
            Log.e("JSONE", "Error processing Places API URL", e);
            return resultList;
        } catch (IOException e) {
            Log.e("JSONE", "Error connecting to Places API", e);
            return resultList;
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        try {

            // Create a JSON object hierarchy from the results
            JSONObject jsonObj = new JSONObject(jsonResults.toString());
            JSONArray predsJsonArray = jsonObj.getJSONArray("predictions");

            // Extract the Place descriptions from the results
            resultList = new ArrayList<String>(predsJsonArray.length());
            for (int i = 0; i < predsJsonArray.length(); i++) {
                System.out.println(predsJsonArray.getJSONObject(i).getString("description"));
                System.out.println("============================================================");
                resultList.add(predsJsonArray.getJSONObject(i).getString("description"));
            }
        } catch (JSONException e) {
            Log.e("JSONE", "Cannot process JSON results", e);
        }

        return resultList;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_weather, container, false);

        autoCompView = v.findViewById(R.id.autoCompleteTextView);

        autoCompView.setAdapter(new GooglePlacesAutocompleteAdapter(getContext(), R.layout.list_item));
        autoCompView.setOnItemClickListener(this);

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());

        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);


//        createLocationRequest();
//        userLocation();
        //find_Location(getContext());


//        mRequest = requestLocationUpdates();
//        locationListener = new LocationCallback() {
//            @Override
//            public void onLocationResult(LocationResult locationResult) {
//                List<Location> locationList = locationResult.getLocations();
//                for (Location location : locationList) {
//                    updateUI(location);
//                     getAddress(location);
//
//                    MapsActivity a = (MapsActivity) getActivity();
//                    LatLng position = new LatLng(location.getLatitude(), location.getLongitude());
//
//                    Toast.makeText(getContext(), position.toString() + "Pos", Toast.LENGTH_SHORT).show();
//                    saveLocationToDB(position);
//                }
//            }
//        };


        progressBar = v.findViewById(R.id.progressBar);
        tvTemp = v.findViewById(R.id.tvTemp);
        ivBackground = v.findViewById(R.id.backgroundImage);

        if (lat == null || lon == null) {
            lat = 32.0612759;
            lon = 34.8841603;
        }

        WeatherDataSource.getWeather(new WeatherDataSource.OnWeatherListener() {
            @Override
            public void onWeather(final WeatherDataSource.Weather data) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (lat == null || lon == null) {
                            lat = 32.0612759;
                            lon = 34.8841603;
                        }
                        updateUIWeather(data);
                    }
                });
                double temp = data.getTemp();
                String description = data.getDescription();
                String t = String.valueOf(temp);
                tvTemp.setText(t);
                tvSunset.setText(description);
                Glide.with(getContext()).load("http://openweathermap.org/img/w/" + data.getIcon() + ".png").into(ivWeather);
            }
        }, lat, lon);


//        WeatherDataSource.getWeather(new WeatherDataSource.OnWeatherListener() {
//
//            @Override
//            public void onWeather(final WeatherDataSource.Weather data) {
//                getActivity().runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        if (locationName == null) {
//                            locationName = "Tel-Aviv";
//                        }
//                        updateUIWeather(data);
//                    }
//
//                });
//
//                double temp = data.getTemp();
//                String description = data.getDescription();
//                String t = String.valueOf(temp);
//                tvTemp.setText(t);
//                tvSunset.setText(description);
//                Glide.with(getContext()).load("http://openweathermap.org/img/w/" + data.getIcon() + ".png").into(ivWeather);
//            }
//        }, locationName);


        unbinder = ButterKnife.bind(this, v);
        return v;

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case 10:
                onViewClicked();
                break;
            default:
                break;
        }
    }

    protected void createLocationRequest() {
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    public void updateUIWeather(WeatherDataSource.Weather data) {
        //hide the progressBar!!!


        progressBar.animate().alpha(0).setDuration(1000);
        tvPlace.setText(data.getCityName().toUpperCase());
        //double temp = data.getTemp();
//        if (temp > 23) {
//            ivWeather.setImageResource(R.drawable.weather_icon);
//        } else ivWeather.setImageResource(R.drawable.night_cloud);
        //String s = temp + "";
        //s.substring(1, 3);
        //tvTemp.setText(s);

        double temp = data.getTemp();
        String description = data.getDescription();
        String t = String.valueOf(temp);
        tvTemp.setText(t);
        tvSunset.setText(description);

        Glide.with(getContext()).load("http://openweathermap.org/img/w/" + data.getIcon() + ".png").into(ivWeather);

        setWeatherIcon(800, data.getSunrise(), data.getSunset());

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    ///--->> Locations...

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onPause() {
        super.onPause();

    }

    private boolean checkLocationPermission() {
        boolean granted = ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
        if (!granted) {
            Toast.makeText(getContext(), "No Location Permission", Toast.LENGTH_SHORT).show();
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, RC_LOCATION);
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return granted;
            }
        }
        return granted;
    }

    private void setWeatherIcon(int actualId, long sunrise, long sunset) {
        int id = actualId / 100;
        String icon = "";
        if (actualId == 800) {
            long currentTime = (new Date().getTime()) / 1000;

            if (currentTime >= sunrise && currentTime < sunset) {
                ivBackground.setImageResource(R.drawable.blue_sky);
                tvPlace.setTextColor(Color.BLACK);
                tvSunset.setTextColor(Color.BLACK);
                tvTemp.setTextColor(Color.BLACK);
                ivLocation.setColorFilter(Color.BLACK);

            } else {
                ivBackground.setImageResource(R.drawable.night_sky);
                tvPlace.setTextColor(Color.WHITE);
                tvSunset.setTextColor(Color.WHITE);
                tvTemp.setTextColor(Color.WHITE);
                ivLocation.setColorFilter(Color.WHITE);
            }
        }
    }

    @OnClick(R.id.imageViewLocation)
    public void onViewClicked() {

        find_Location(getActivity());

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                locationName = getAddress(location);
                Log.d("WWWQ", "onLocationChanged: " + locationName);
                WeatherDataSource.getWeather(new WeatherDataSource.OnWeatherListener() {
                    @Override
                    public void onWeather(final WeatherDataSource.Weather data) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                updateUIWeather(data);
                            }
                        });
                        double temp = data.getTemp();
                        String description = data.getDescription();
                        String t = String.valueOf(temp);
                        tvTemp.setText(t);
                        tvSunset.setText(description);
                        Glide.with(getContext()).load("http://openweathermap.org/img/w/" + data.getIcon() + ".png").into(ivWeather);
                    }
                }, location.getLatitude(), location.getLongitude());
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {
                Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(i);
            }
        };

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                                Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.INTERNET}
                        , 10);
            }
            return;
        }


        mLastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        Log.d("JJJ", "onViewClicked: " + mLastLocation);

        locationName = getAddress(mLastLocation);
        //Toast.makeText(getContext(), "" + locationName, Toast.LENGTH_SHORT).show();

        WeatherDataSource.getWeather(new WeatherDataSource.OnWeatherListener() {
            @Override
            public void onWeather(final WeatherDataSource.Weather data) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        updateUIWeather(data);
                    }
                });
                double temp = data.getTemp();
                String description = data.getDescription();
                String t = String.valueOf(temp);
                tvTemp.setText(t);
                tvSunset.setText(description);
                Glide.with(getContext()).load("http://openweathermap.org/img/w/" + data.getIcon() + ".png").into(ivWeather);
            }
        }, lat, lon);

        locationManager.requestLocationUpdates("gps", 5000, 0, locationListener);

    }

    public LatLng getLocationFromAddress(Context context, String strAddress) {

        Geocoder coder = new Geocoder(context);
        List<Address> address;
        LatLng p1 = null;

        try {
            // May throw an IOException
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }
            Address location = address.get(0);
            location.getLatitude();
            location.getLongitude();

            p1 = new LatLng(location.getLatitude(), location.getLongitude());

        } catch (IOException ex) {

            ex.printStackTrace();
        }

        return p1;
    }

    @OnClick(R.id.ivSearchWeather)
    public void onSearchClicked() {
        Editable text = autoCompView.getText(); //etSearchWeather.getText();

        if (TextUtils.isEmpty(locationName) || text == null) {
            locationName = "Tel-Aviv";
        } else {
            locationName = text.toString();
        }
        LatLng latLng = getLocationFromAddress(getContext(), locationName);


        Log.d("PPP", "onSearchClicked: " + locationName);
        WeatherDataSource.getWeather(new WeatherDataSource.OnWeatherListener() {
            @Override
            public void onWeather(final WeatherDataSource.Weather data) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        updateUIWeather(data);
                    }

                });

                double temp = data.getTemp();
                String description = data.getDescription();
                String t = String.valueOf(temp);
                tvTemp.setText(t);
                tvSunset.setText(description);

                Glide.with(getContext()).load("http://openweathermap.org/img/w/" + data.getIcon()).into(ivWeather);

                // Toast.makeText(getContext(), "onWeather: " + "http://openweathermap.org/img/w/" + data.getIcon(), Toast.LENGTH_SHORT).show();
                // Log.d("###999", "onWeather: " + "http://openweathermap.org/img/w/" + data.getIcon());
//                if (temp > 23) {
//                    ivWeather.setImageResource(R.drawable.weather_icon);
//                } else ivWeather.setImageResource(R.drawable.night_cloud);

            }
        }, latLng.latitude, latLng.longitude);
    }

    private void saveLocationToDB(LatLng position) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            DatabaseReference rowRef = FirebaseDatabase.getInstance().getReference("Location").child(user.getUid()).push();

            UserLocation userLocation = new UserLocation(position.latitude, position.longitude, rowRef.getKey());
        }
    }

    private LocationRequest requestLocationUpdates() {
        //1) LocationRequest..
        LocationRequest request = new LocationRequest();
        //FusedLocationApi(GPS + Cellular)
        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY); //Use GPS or location is already known
//        request.setPriority(LocationRequest.PRIORITY_LOW_POWER); //Use Cellular and improve with GPS
//        request.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY); //Use GPS or location is already known

        //Use GPS each 2 seconds for my app
        request.setInterval(2 * 1000);

        //if some other app is requesting the location each 0.5Sec -> we want in!
        request.setFastestInterval(500);

        //give me only 5 update click Gps.
        //request.setNumUpdates(5);

        //don't update me if not displaced 100 meters at least.
        //request.setSmallestDisplacement(100);

        return request;
    }

    private Location getLastKnownLocation() {
        if (checkLocationPermission())
            mFusedLocationProviderClient.getLastLocation().addOnCompleteListener(getActivity(), new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    if (task.isSuccessful()) {
                        Location location = task.getResult();

                        getAddress(location);
                        // Toast.makeText(getContext(), "LOC: " + location.toString(), Toast.LENGTH_SHORT).show();
                        k = location;
                    }
                }
            });
        return k;
    }

    private void updateUI(Location location) {
        if (location != null) {
            String update = location.getLatitude() + ", " + location.getLongitude();

            tvPlace.setText(update);
        }
    }

    private String getAddress(Location l) {
        Geocoder coder = new Geocoder(getContext(), Locale.getDefault());
        List<String> data = new ArrayList<>();

        if (l == null) {
            Log.d("TTT", "getAddress: " + l);
        } else {
            try {

                Address address = coder.getFromLocation(l.getLatitude(), l.getLongitude(), 1).get(0);
                for (int i = 0; i <= address.getMaxAddressLineIndex(); i++) {
                    String addressLine = address.getAddressLine(i);
                    //tvPlace.setText(addressLine);
                    Log.d("TOTO", "getAddress: " + addressLine);
                    // Toast.makeText(getContext(), "Loc:--> "+addressLine, Toast.LENGTH_SHORT).show();
                    data.add(i, addressLine);
                }

            } catch (IOException e) {
                e.printStackTrace();
                //Toast.makeText(getContext(), e.getClass().getName() + ":", Toast.LENGTH_SHORT).show();
            }
            //Log.d("TOTO2", "getAddress: " + data.get(0).toString()); /*", " + data.get(1).toString());*/

            locationName = data.get(0).toString();
            char[] charArray = locationName.toCharArray();
            for (int i = 0; i < charArray.length - 1; i++) {
                Character g = charArray[i];
                Log.d("TOTO23", "get: " + i + " " + g);
                String v = g.toString();
                if (v.equals(",")) {
                    // Toast.makeText(getContext(), "--> "+ v, Toast.LENGTH_SHORT).show();
                    locationName = locationName.substring(i, locationName.length());
                    Log.d("TOTO2", "get: " + i + " " + locationName);
                    break;
                }


            }

            Log.d("TOTO2", "getAddress: " + locationName);
        }
        Log.d(">>>", "getAddress: " + locationName);
        return locationName;

    }

    public void find_Location(Context con) {
        Log.d("Find Location", "in find_location");

        String location_context = Context.LOCATION_SERVICE;
        locationManager = (LocationManager) con.getSystemService(location_context);
        List<String> providers = locationManager.getProviders(true);
        for (String provider : providers) {
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(con, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                //    ActivityCompat#requestPermissions

                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, RC_LOCATION);
                return;
            }
            locationManager.requestLocationUpdates(provider, 1000, 0, new android.location.LocationListener() {
                @Override
                public void onLocationChanged(Location location) {

                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {

                }

                @Override
                public void onProviderEnabled(String provider) {

                }

                @Override
                public void onProviderDisabled(String provider) {

                }
            });
            Location location = locationManager.getLastKnownLocation(provider);
            if (location != null) {
                //Toast.makeText(getContext(), "Yehh: -------------> new12122  " + location.toString(), Toast.LENGTH_LONG).show();

                lat = location.getLatitude();
                lon = location.getLongitude();


                Log.d("ZAZA", "find_Location: " + lat + " lon: " + lon);
                getAddressFromLatLon(getContext(), lat, lon);
                //String temp_c = SendToUrl(addr);
            }

        }
    }

    //////////////////////////////
    public void userLocation() {


        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());
        Task r = mFusedLocationProviderClient.getLastLocation();

        r.addOnSuccessListener(getActivity(), new OnSuccessListener() {
            @Override
            public void onSuccess(Object o) {
                //   Toast.makeText(getContext(),"Task: ", Toast.LENGTH_SHORT).show();
            }
        });
        if (mLastLocation != null) {
            //  Toast.makeText(getContext(), "HHHHH" + mLastLocation.getLongitude(), Toast.LENGTH_SHORT).show();
        } else {
            //  Toast.makeText(getContext(), "Location null" , Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String str = (String) parent.getItemAtPosition(position);


        // Toast.makeText(getContext(), str, Toast.LENGTH_SHORT).show();
        locationName = str;
    }

    class GooglePlacesAutocompleteAdapter extends ArrayAdapter implements Filterable {

        private ArrayList resultList;


        public GooglePlacesAutocompleteAdapter(Context context, int textViewResourceId) {

            super(context, textViewResourceId);

        }


        @Override

        public int getCount() {

            return resultList.size();
        }


        @Override

        public String getItem(int index) {

            return (String) resultList.get(index);

        }


        @Override

        public Filter getFilter() {

            Filter filter = new Filter() {

                @Override

                protected FilterResults performFiltering(CharSequence constraint) {

                    FilterResults filterResults = new FilterResults();

                    if (constraint != null) {

                        // Retrieve the autocomplete results.

                        resultList = autocomplete(constraint.toString());


                        // Assign the data to the FilterResults

                        filterResults.values = resultList;

                        filterResults.count = resultList.size();

                    }

                    return filterResults;

                }


                @Override

                protected void publishResults(CharSequence constraint, FilterResults results) {

                    if (results != null && results.count > 0) {
                        notifyDataSetChanged();

                    } else {

                        notifyDataSetInvalidated();

                    }

                }

            };

            return filter;

        }

    }

}
