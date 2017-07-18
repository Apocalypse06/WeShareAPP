package com.example.ntut.weshare.dealDetail;

import android.Manifest;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ntut.weshare.Common;
import com.example.ntut.weshare.R;
import com.example.ntut.weshare.homeGoodsDetail.DealBean;
import com.example.ntut.weshare.message.MessageBean;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;


public class ChooseMapActivity extends AppCompatActivity implements OnMapReadyCallback {
    private final static String TAG = "ChooseMapActivity";

    private GoogleMap map;
    private Location lastLocation;
    private GoogleApiClient googleApiClient;
    private Marker marker_my;
    private Marker marker_final;
    private TextView tvMarkerDrag;
    private EditText edAddress;
    private LatLng firstMarkPoint;
    private LatLng myMark;
    private boolean firstMark = true;
    private final static int REQUEST_CODE_RESOLUTION = 1;
    private String finalMarkName = null;
    private String finalMarkSreach = null;

    private DealBean deal;

    @Override
    protected void onResume() {
        super.onResume();
        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(connectionCallbacks)
                    .addOnConnectionFailedListener(onConnectionFailedListener)
                    .build();
        }
        googleApiClient.connect();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_fragment);
        Bundle bundleFromList = this.getIntent().getBundleExtra("intentdDeal");
        deal = (DealBean) bundleFromList.getSerializable("deal");

        initPoints();//初始化地圖上的點，經緯度
        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.fmMap);
        mapFragment.getMapAsync(this);
        tvMarkerDrag = (TextView) findViewById(R.id.tvMarkerDrag);
        edAddress = (EditText) findViewById(R.id.edAddress);
    }

    @Override
    public void onMapReady(GoogleMap map) {
        this.map = map;
        setUpMap();
    }

    private LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            lastLocation = location;
        }
    };

    private GoogleApiClient.ConnectionCallbacks connectionCallbacks =
            new GoogleApiClient.ConnectionCallbacks() {
                @Override
                public void onConnected(Bundle bundle) {
                    Log.i(TAG, "GoogleApiClient connected");
                    if (ActivityCompat.checkSelfPermission(ChooseMapActivity.this,
                            Manifest.permission.ACCESS_COARSE_LOCATION) ==
                            PackageManager.PERMISSION_GRANTED) {
                        lastLocation = LocationServices.FusedLocationApi
                                .getLastLocation(googleApiClient);
                        //檢視app是否有得到使用者的權限
                        //預設為發出請求最後的位置(曾經定位過的位置歷史資料)
                        // 優：不耗電
                        // 缺：和實際上位置可能會有差異

                        LocationRequest locationRequest = LocationRequest.create()
                                //預設先抓取使用者手機內部上一次的的位置資料
                                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                                //選擇最高精準度的定位為優先
                                .setInterval(10000)
                                //設定多久重新定位刷新一次/毫秒
                                .setSmallestDisplacement(1000);
                        //設定和上一次定位位置相差多遠才會被認為有移動重新定位刷新
                        LocationServices.FusedLocationApi.requestLocationUpdates(
                                googleApiClient, locationRequest, locationListener);
                        //用locationListener檢測新的定位是否和上一次相同，如果相同則不刷新頁面

                    }
                }

                @Override
                public void onConnectionSuspended(int i) {
                    showToast("msg_GoogleApiClientConnectionSuspended");
                }
            };

    private GoogleApiClient.OnConnectionFailedListener onConnectionFailedListener =
            new GoogleApiClient.OnConnectionFailedListener() {
                @Override
                public void onConnectionFailed(@NonNull ConnectionResult result) {
                    showToast("msg_GoogleApiClientConnectionFailed");
                    if (!result.hasResolution()) {
                        GoogleApiAvailability.getInstance().getErrorDialog(
                                ChooseMapActivity.this,
                                result.getErrorCode(),
                                0
                        ).show();
                        return;
                    }
                    try {
                        result.startResolutionForResult(
                                ChooseMapActivity.this,
                                REQUEST_CODE_RESOLUTION);
                    } catch (IntentSender.SendIntentException e) {
                        Log.e(TAG, "Exception while starting resolution activity");
                    }
                }
            };

    private void initPoints() {
        firstMarkPoint = new LatLng(23.791952, 120.861379);
    }

    private void initPointss() {
        myMark = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
        finalMarkName = "" + lastLocation.getLatitude() + "," + lastLocation.getLongitude();
    }

    private void setUpMap() {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED) {
            map.setMyLocationEnabled(true);
        }
        map.getUiSettings().setZoomControlsEnabled(true);
        map.getUiSettings().setMyLocationButtonEnabled(true);
        map.getUiSettings().setZoomControlsEnabled(true);


        //---------------拉畫面，將標記可以顯示在地圖上--------------------
        CameraPosition cameraPosition = new CameraPosition.Builder()//並不是拍照，意旨畫面的移動
                .target(firstMarkPoint)//畫面正中央，焦點
                .zoom(7)//大小，縮放程度，數字越大，看得越清楚
                .build();
        CameraUpdate cameraUpdate = CameraUpdateFactory
                .newCameraPosition(cameraPosition);
        map.animateCamera(cameraUpdate);//拉動鏡頭
        //--------------------------------------------------------------
        //addMarkersToMap();//打標記，可以設定內容

        //map.setInfoWindowAdapter(new MyInfoWindowAdapter());//點擊才會顯示出資料(客製化)

        MyMarkerListener myMarkerListener = new MyMarkerListener();//監聽器
        map.setOnMarkerClickListener(myMarkerListener);//點擊Marker
        map.setOnInfoWindowClickListener(myMarkerListener);//再次點擊
        map.setOnMarkerDragListener(myMarkerListener);//長按(移動)
    }

    private void addMarkersToMap() {//裡面有個List存裡面的標記
        marker_my = map.addMarker(new MarkerOptions()
                .position(myMark)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.pin))//標記的圖示，若沒有是預設
                .draggable(true));//長按標記可以拖曳
    }

    private void moveMarkersToMap() {//裡面有個List存裡面的標記
        map.clear();
        initPointss();
        marker_my = map.addMarker(new MarkerOptions()
                .position(myMark)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.pin))//標記的圖示，若沒有是預設
                .draggable(true));//長按標記可以拖曳
    }

    private class MyMarkerListener implements GoogleMap.OnMarkerClickListener,
            GoogleMap.OnInfoWindowClickListener, GoogleMap.OnMarkerDragListener {
        @Override
        public boolean onMarkerClick(Marker marker) {//本來就會，不用特別實作
//            showToast(marker.getTitle());
            return false;
        }

        @Override
        public void onInfoWindowClick(Marker marker) {
//            showToast(marker.getTitle());
        }

        //------------OnMarkerDragListener---------------------------
        @Override
        public void onMarkerDragStart(Marker marker) {//點擊
            finalMarkSreach = marker.getPosition().latitude + "," + marker.getPosition().longitude;
            locationGatAddress(finalMarkSreach);
        }

        @Override
        public void onMarkerDragEnd(Marker marker) {//放開
            String text = "onMarkerDragEnd";
            finalMarkName = marker.getPosition().latitude + "," + marker.getPosition().longitude;
            finalMarkSreach = marker.getPosition().latitude + "," + marker.getPosition().longitude;
//            tvMarkerDrag.setText("latitude=" + marker.getPosition().latitude + "\nlongitude=" + marker.getPosition().longitude);
            locationGatAddress(finalMarkSreach);
        }

        @Override
        public void onMarkerDrag(Marker marker) {
            String text = "地標移動中...";
            tvMarkerDrag.setText(text);
        }
        //----------------------------------------------------------
    }


    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public void onListenterSearch(View view) {
        String locationName = edAddress.getText().toString().trim();
        if (locationName.length() > 0) {
            locationNameToMarker(locationName);
        } else {
            //showToast(R.string.msg_LocationNameNotFound);
        }
    }

    public void onListenterMark(View view) {
        String addressText = edAddress.getText().toString().trim();
        if (addressText.length() <= 0) {
            moveMarkersToMap();
        } else {
            locationNameToMarkerPoint(addressText);
        }
        locationGatAddress(finalMarkName);

    }

    private void locationGatAddress(String locationName) {
        Geocoder geocoder = new Geocoder(this);
        List<Address> addressList = null;
        int maxResults = 1;
        try {
            addressList = geocoder
                    .getFromLocationName(locationName, maxResults);//.getFromLocationName(地名或地址,希望回傳的筆數)，回傳List物件
        } catch (IOException e) {
            Log.e(TAG, e.toString());
        }

        if (addressList == null || addressList.isEmpty()) {
            showToast("沒有地址");
        } else {
            Address address = addressList.get(0);//取得物件，第1筆，index(0)
            String snippet = address.getAddressLine(0);//取得地址index(0)
            tvMarkerDrag.setText("面交地址=" + snippet);
        }
    }

    private void locationNameToMarker(String locationName) {
        map.clear();
        Geocoder geocoder = new Geocoder(this);
        List<Address> addressList = null;
        int maxResults = 1;
        try {
            addressList = geocoder
                    .getFromLocationName(locationName, maxResults);//.getFromLocationName(地名或地址,希望回傳的筆數)，回傳List物件
        } catch (IOException e) {
            Log.e(TAG, e.toString());
        }

        if (addressList == null || addressList.isEmpty()) {
            //showToast(R.string.msg_LocationNameNotFound);
        } else {
            Address address = addressList.get(0);//取得物件，第1筆，index(0)

            LatLng position = new LatLng(address.getLatitude(),//取的經緯度
                    address.getLongitude());
            finalMarkName = address.getLatitude() + "," + address.getLongitude();
            String snippet = address.getAddressLine(0);//取得地址index(0)
            // tvMarkerDrag.setText("面交地址=" + snippet);
            map.addMarker(new MarkerOptions().position(position)
                    .title(locationName).snippet(snippet));

            //------------拉畫面，將查詢內容傳到畫面正中央--------------------
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(position).zoom(15).build();
            map.animateCamera(CameraUpdateFactory
                    .newCameraPosition(cameraPosition));
            //-------------------------------------------------------------
        }
    }

    private void locationNameToMarkerPoint(String locationName) {
        map.clear();
        Geocoder geocoder = new Geocoder(this);
        List<Address> addressList = null;
        int maxResults = 1;
        try {
            addressList = geocoder
                    .getFromLocationName(locationName, maxResults);//.getFromLocationName(地名或地址,希望回傳的筆數)，回傳List物件
        } catch (IOException e) {
            Log.e(TAG, e.toString());
        }

        if (addressList == null || addressList.isEmpty()) {
            //showToast(R.string.msg_LocationNameNotFound);
        } else {
            Address address = addressList.get(0);//取得物件，第1筆，index(0)

            LatLng position = new LatLng(address.getLatitude(),//取的經緯度
                    address.getLongitude());
            finalMarkName = address.getLatitude() + "," + address.getLongitude();
            String snippet = address.getAddressLine(0);//取得地址index(0)
            // tvMarkerDrag.setText("面交地址=" + snippet);
            map.addMarker(new MarkerOptions().position(position)
                    .title(locationName).snippet(snippet));
            map.addMarker(new MarkerOptions().position(position)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.pin))//標記的圖示，若沒有是預設
                    .draggable(true));//長按標記可以拖曳);

            //------------拉畫面，將查詢內容傳到畫面正中央--------------------
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(position).zoom(15).build();
            map.animateCamera(CameraUpdateFactory
                    .newCameraPosition(cameraPosition));
            //-------------------------------------------------------------

        }
    }


    public void onListenterNavigate(View view) {
        int count = 0;
        String url = Common.URL + "DealServlet";
        String action = "sendDealContext";
        if (Common.networkConnected(this)) {//傳送到server端
            try {
                count = new SendDealingContextTask().execute(url, action, deal.getDealNo(), finalMarkName).get();
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }

            if (count == 0) {
                Common.showToast(this, R.string.msg_SendFail);
            } else {
                Common.showToast(this, "地點決定成功");
                finish();
            }
        } else {
            Common.showToast(this, R.string.msg_NoNetwork);
        }
    }
}