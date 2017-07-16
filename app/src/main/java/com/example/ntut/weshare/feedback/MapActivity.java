package com.example.ntut.weshare.feedback;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ntut.weshare.R;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap map;
    private Marker marker_taroko;
    private Marker marker_yushan;
    private Marker marker_kenting;
    private Marker marker_yangmingshan;
    private TextView tvMarkerDrag;
    private LatLng taroko;
    private LatLng yushan;
    private LatLng kenting;
    private LatLng yangmingshan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_fragment);
        initPoints();//初始化地圖上的點，經緯度
        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.fmMap);
        mapFragment.getMapAsync(this);
//        tvMarkerDrag = (TextView) findViewById(R.id.tvMarkerDrag);
    }

    private void initPoints() {
        taroko = new LatLng(24.151287, 121.625537);//(緯度,經度)
        yushan = new LatLng(23.791952, 120.861379);
        kenting = new LatLng(21.985712, 120.813217);
        yangmingshan = new LatLng(25.091075, 121.559834);
    }

    @Override
    public void onMapReady(GoogleMap map) {
        this.map = map;
        setUpMap();
    }

    private void setUpMap() {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED) {
            map.setMyLocationEnabled(true);
        }
        map.getUiSettings().setZoomControlsEnabled(true);
        //---------------拉畫面，將標記可以顯示在地圖上--------------------
        CameraPosition cameraPosition = new CameraPosition.Builder()//並不是拍照，意旨畫面的移動
                .target(taroko)//畫面正中央，焦點
                .zoom(7)//大小，縮放程度，數字越大，看得越清楚
                .build();
        CameraUpdate cameraUpdate = CameraUpdateFactory
                .newCameraPosition(cameraPosition);
        map.animateCamera(cameraUpdate);//拉動鏡頭
        //--------------------------------------------------------------
        addMarkersToMap();//打標記，可以設定內容

        map.setInfoWindowAdapter(new MyInfoWindowAdapter());//點擊才會顯示出資料(客製化)

        MyMarkerListener myMarkerListener = new MyMarkerListener();//監聽器
        map.setOnMarkerClickListener(myMarkerListener);//點擊Marker
        map.setOnInfoWindowClickListener(myMarkerListener);//再次點擊
        map.setOnMarkerDragListener(myMarkerListener);//長按(移動)
    }

    private void addMarkersToMap() {//裡面有個List存裡面的標記
//        marker_taroko = map.addMarker(new MarkerOptions()
//                .position(taroko)//標記位置
//                .title(getString(R.string.marker_title_taroko))
//                .snippet(getString(R.string.marker_snippet_taroko))
//                .icon(BitmapDescriptorFactory.fromResource(R.drawable.pin)));//標記的圖示，若沒有是預設

        marker_yushan = map.addMarker(new MarkerOptions()
                .position(yushan)
                .title("玉山")
                .snippet("第一高峰")
                .draggable(true));//長按標記可以拖曳

//        marker_kenting = map.addMarker(new MarkerOptions().position(kenting)
//                .title(getString(R.string.marker_title_kenting))
//                .snippet(getString(R.string.marker_snippet_kenting))
//                .icon(BitmapDescriptorFactory
//                        .defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));

//        marker_yangmingshan = map.addMarker(new MarkerOptions()
//                .position(yangmingshan)
//                .title(getString(R.string.marker_title_yangmingshan))
//                .snippet(getString(R.string.marker_snippet_yangmingshan))
//                .icon(BitmapDescriptorFactory
//                        .defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
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
            String text = "onMarkerDragStart";
            tvMarkerDrag.setText(text);
        }

        @Override
        public void onMarkerDragEnd(Marker marker) {//放開
            String text = "onMarkerDragEnd";
            tvMarkerDrag.setText(text);
        }

        @Override
        public void onMarkerDrag(Marker marker) {
            String text = "onMarkerDrag.  Current Position: "
                    + marker.getPosition();//取的位置
            tvMarkerDrag.setText(text);
        }
        //----------------------------------------------------------
    }


    private class MyInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {
        private final View infoWindow;

        MyInfoWindowAdapter() {
            infoWindow = View.inflate(MapActivity.this, R.layout.custom_info_window, null);//取的layout檔，並載入
        }

        @Override
        public View getInfoWindow(Marker marker) {//marker順序是按照addMarkersToMap()的順序
            int logoId;
            if (marker.equals(marker_yangmingshan)) {
                logoId = R.drawable.hand;//圖示
            }
//            else if (marker.equals(marker_taroko)) {
//                logoId = R.drawable.logo_taroko;
//            } else if (marker.equals(marker_yushan)) {
//                logoId = R.drawable.logo_yushan;
//            } else if (marker.equals(marker_kenting)) {
//                logoId = R.drawable.logo_kenting;
//            }

            else {
                logoId = 0;
            }

            ImageView ivLogo = ((ImageView) infoWindow
                    .findViewById(R.id.ivLogo));
            ivLogo.setImageResource(logoId);

            String title = marker.getTitle();
            TextView tvTitle = ((TextView) infoWindow
                    .findViewById(R.id.tvTitle));
            tvTitle.setText(title);

            String snippet = marker.getSnippet();
            TextView tvSnippet = ((TextView) infoWindow
                    .findViewById(R.id.tvSnippet));
            tvSnippet.setText(snippet);
            return infoWindow;
        }

        @Override
        public View getInfoContents(Marker marker) {
            return null;
        }
    }


    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }


}