package com.example.myapplication;

       import android.annotation.SuppressLint;
        import android.app.Activity;
        import android.content.Context;
        import android.content.Intent;
        import android.location.Criteria;
        import android.location.Location;
        import android.location.LocationListener;
        import android.location.LocationManager;
        import android.location.LocationProvider;
        import android.os.Build;
        import android.provider.Settings;
        import android.os.Bundle;
        import android.text.TextUtils;
        import android.util.Log;
        import android.view.View;
        import android.widget.Button;
        import android.widget.CheckBox;
        import android.widget.TextView;
        import android.widget.Toast;



        public class MainActivity extends Activity {
        private TextView latitudeField;
        private TextView longitudeField;
        private TextView choose;
        private CheckBox choice;
        private Button button;
        private LocationManager locationManager;
        private String provider;
        Criteria criteria;
        private MyLocationListener mylistener;


       @SuppressLint("InlinedApi") @SuppressWarnings("deprecation")
        public static int getLocationMode(Context context)
       {
            int locationMode = Settings.Secure.LOCATION_MODE_OFF;
            String locationProviders;

               if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
                       try {
                           locationMode = Settings.Secure.getInt(context.getContentResolver(),
                                               Settings.Secure.LOCATION_MODE);

                           } catch (Settings.SettingNotFoundException e) {
                               e.printStackTrace();
                            }

                    }else{
                       locationProviders = Settings.Secure.getString(context.getContentResolver(),
                                       Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
                        if (TextUtils.isEmpty(locationProviders)){
                              locationMode = Settings.Secure.LOCATION_MODE_OFF;
                            }
                       else if (locationProviders.contains(LocationManager.GPS_PROVIDER)
                                && locationProviders.contains(LocationManager.NETWORK_PROVIDER)){
                                locationMode = Settings.Secure.LOCATION_MODE_HIGH_ACCURACY;
                           }
                        else if (locationProviders.contains(LocationManager.GPS_PROVIDER)){
                               locationMode = Settings.Secure.LOCATION_MODE_SENSORS_ONLY;
                            }
                        else if (locationProviders.contains(LocationManager.NETWORK_PROVIDER)){
                                locationMode = Settings.Secure.LOCATION_MODE_BATTERY_SAVING;
                            }

                    }
                return locationMode;    }


        public void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
              setContentView(R.layout.activity_maps);
              latituteField= (TextView) findViewById(R.id.Latitude);
                longitudeField = (TextView) findViewById(R.id.Longitute);
                choose = (TextView) findViewById(R.id.choice);
              choice = (CheckBox) findViewById(R.id.checkBox);
               button = (Button) findViewById(R.id.button);

              button.setOnClickListener(new View.OnClickListener()
              {

                       public void onClick(View v) {
                                               // TODO Auto-generated method stub
                                               if (choice.isChecked()) {

                                       criteria.setAccuracy(Criteria.ACCURACY_FINE);

                                                   choose.setText("fine accuracy selected. " +
                                                                                      "WIFIì •ë³´ í™œìš©í•˜ì—¬ ìœ„ì¹˜ ì°¾ê¸°.ë” ì •í™•í•¨");



                                                   criteria.setAccuracy(Criteria.ACCURACY_COARSE);

                                        choose.setText("coarse accuracy selected.ê¸°ì§€êµ­ì •ë³´ í™œìš©í•˜ì—¬ ìœ„ì¹˜ ì°¾ê¸°");

                                   }
                                           }

                            });

                locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);


              criteria = new Criteria();

               criteria.setAccuracy(Criteria.ACCURACY_COARSE);
               criteria.setBearingRequired(true);

               criteria.setPowerRequirement(Criteria.POWER_LOW);

                criteria.setCostAllowed(true);

                provider = locationManager.getBestProvider(criteria, true);
              int mode = getLocationMode(this);

                switch (mode) {
                        case android.provider.Settings.Secure.LOCATION_MODE_OFF:
                                Toast.makeText(MainActivity.this, "LOCATION_MODE_OFF",
                                               Toast.LENGTH_SHORT).show();
                               break;
                        case android.provider.Settings.Secure.LOCATION_MODE_SENSORS_ONLY:
                            Toast.makeText(MainActivity.this,
                                              "LOCATION_MODE_SENSORS_ONLY = GPS_PROVIDER",
                                           Toast.LENGTH_SHORT).show();
                               break;
                        case android.provider.Settings.Secure.LOCATION_MODE_BATTERY_SAVING:
                               Toast.makeText(MainActivity.this,
                               "LOCATION_MODE_BATTERY_SAVING = NETWORK_PROVIDER",
                                               Toast.LENGTH_SHORT).show();
                               break;
                        case android.provider.Settings.Secure.LOCATION_MODE_HIGH_ACCURACY:
                             Toast.makeText(MainActivity.this,
                                                "LOCATION_MODE_HIGH_ACCURACY = GPS_PROVIDER+NETWORK_PROVIDER",
                                               Toast.LENGTH_SHORT).show();
                            break;
                       default:
                           break;
                   }





                Toast.makeText(MainActivity.this, provider + "providerê°€ ì„ íƒë˜ì—ˆìŒ.",
                               Toast.LENGTH_SHORT).show();


               Location location=null;
               try {
                       location = locationManager.getLastKnownLocation(provider);
                    } catch (SecurityException e) {
                        Log.e("PERMISSION_EXCEPTION", "PERMISSION_NOT_GRANTED");
                    }

                mylistener = new MyLocationListener();

                if (location != null) {
                   mylistener.onLocationChanged(location);
                   } else {

                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(intent);
                    }
           }

       protected void onResume() {
               super.onResume();
               Toast.makeText(MainActivity.this, "onResume",
                             Toast.LENGTH_SHORT).show();

                provider = locationManager.getBestProvider(criteria, true);
               Toast.makeText(MainActivity.this, provider + "providerê°€ ì„ íƒë˜ì—ˆìŒ.",
                         Toast.LENGTH_SHORT).show();



                try {
                        locationManager.requestLocationUpdates(provider, 200, 1, mylistener);
                    } catch (SecurityException e) {
                        Log.e("PERMISSION_EXCEPTION", "PERMISSION_NOT_GRANTED");
                  }

            }

                @Override
        protected void onPause() {
                    super.onPause();
               Toast.makeText(MainActivity.this, "onPause",
                             Toast.LENGTH_SHORT).show();

               try {
                       locationManager.removeUpdates(mylistener);
                   } catch (SecurityException e) {
                       Log.e("PERMISSION_EXCEPTION", "PERMISSION_NOT_GRANTED");
                   }

            }


            private class MyLocationListener implements LocationListener
            {


                public void onLocationChanged(Location location) {



                        latituteField.setText("Latitude: "
                                +String.valueOf(location.getLatitude()));
                        longitudeField.setText("Longitude: "
                                + String.valueOf(location.getLongitude()));

                        Toast.makeText(MainActivity.this, provider
                                        + " providerê°€ ì„ íƒë˜ì—ˆìŒ. ìœ„ì¹˜ê°€ ë°”ë€Œì—ˆìŒ ",
                            Toast.LENGTH_SHORT).show();
                  }
                public void onStatusChanged(String provider, int status, Bundle extras) {
                    switch (status) {
                               case LocationProvider.AVAILABLE:
                                       Toast.makeText(MainActivity.this, provider +" state visible",
                                                     Toast.LENGTH_SHORT).show();
                                      break;
                               case LocationProvider.OUT_OF_SERVICE:
                                       Toast.makeText(MainActivity.this, provider +" out of service",
                                                     Toast.LENGTH_SHORT).show();
                                      break;
                               case LocationProvider.TEMPORARILY_UNAVAILABLE:
                                     Toast.makeText(MainActivity.this, provider +" service stop",
                                                      Toast.LENGTH_SHORT).show();
                                       break;
                            }

                   }

              public void onProviderEnabled(String provider)
              {
                      Toast.makeText(MainActivity.this, "Provider " + provider + " enabled!",
                                      Toast.LENGTH_SHORT).show();

                  }

           public void onProviderDisabled(String provider)
           {
                      Toast.makeText(MainActivity.this, "Provider " + provider + " disabled!",
                                      Toast.LENGTH_SHORT).show();
                   Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                      startActivity(intent);
           }
         }
    }
