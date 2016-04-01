package com.example.liyang.baimap;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiAddrInfo;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.route.BikingRouteResult;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteLine;
import com.baidu.mapapi.search.route.TransitRoutePlanOption;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteResult;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private MapView map;
    private LocationClient locationClient;


    private BDLocationListener bdLocationListener = new BDLocationListener() {
        @Override
        public void onReceiveLocation(BDLocation location) {
            Toast.makeText(MainActivity.this, "当前城市是-------------->" + location.getCity(), Toast.LENGTH_SHORT).show();
            Log.d("ooo", "onReceiveLocation: " + location.getCity() + location.getAddrStr());


//            BaiduMap baiduMap = map.getMap();
//            baiduMap.setTrafficEnabled(true);
//            LatLng lng = new LatLng(location.getLatitude(), location.getLongitude());
//            BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher);
//            MarkerOptions options = new MarkerOptions().draggable(true).position(lng).icon(bitmapDescriptor);
//            baiduMap.addOverlay(options);

        //① search Prepare
        PoiSearch poiSearch = PoiSearch.newInstance();
        //② setListener
        poiSearch.setOnGetPoiSearchResultListener(new OnGetPoiSearchResultListener() {
            @Override
            public void onGetPoiResult(PoiResult poiResult) {
                //a)get Result for search
                if(poiResult.error == SearchResult.ERRORNO.NO_ERROR) {
                    //b）search success
                    List<PoiInfo> allPoi = poiResult.getAllPoi();
                    for(PoiInfo p :allPoi){
                        Log.d(TAG, "onGetPoiResult:search Result name ------------->"+p.name);
                        Log.d(TAG, "onGetPoiResult:search Result Address-----------> "+p.location);
                        Log.d(TAG, "onGetPoiResult:search Result Type-------------->"+p.type);
                        Log.d(TAG, "onGetPoiResult:search Result Phone------------->"+p.phoneNum);
                    }

                }else{
                    Toast.makeText(MainActivity.this, "search Error", Toast.LENGTH_SHORT).show();
                }
            }
            public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {

            }
        });
        //③ start Search
        poiSearch.searchInCity(new PoiCitySearchOption().city("北京").keyword("自助餐").pageNum(10));



        //--------------------Search Traffic-------------------------------
            RoutePlanSearch routePlanSearch = RoutePlanSearch.newInstance();
            routePlanSearch.setOnGetRoutePlanResultListener(new OnGetRoutePlanResultListener() {
                @Override
                public void onGetWalkingRouteResult(WalkingRouteResult walkingRouteResult) {

                }

                @Override
                public void onGetTransitRouteResult(TransitRouteResult transitRouteResult) {
                    if (transitRouteResult.error == SearchResult.ERRORNO.NO_ERROR) {
                        //success
                        List<TransitRouteLine> routeLines = transitRouteResult.getRouteLines();
                        for(TransitRouteLine t : routeLines){

                            Log.d(TAG, "onGetPoiResult:search Result Distance-------------->"+t.getDistance());
                            Log.d(TAG, "onGetPoiResult:search Result Duration------------->"+t.getDuration());
                            for(TransitRouteLine.TransitStep step :t.getAllStep())
                            Log.d(TAG, "onGetPoiResult:search Result Step ------------->"+step.getInstructions());
                        }

                    }else{
                        Toast.makeText(MainActivity.this, "Search Null", Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onGetDrivingRouteResult(DrivingRouteResult drivingRouteResult) {

                }

                @Override
                public void onGetBikingRouteResult(BikingRouteResult bikingRouteResult) {

                }
            });

            //start Search Traffic
            PlanNode start = PlanNode.withCityNameAndPlaceName("北京","龙泽");
            PlanNode end = PlanNode.withCityNameAndPlaceName("北京","西单");
            routePlanSearch.transitSearch(new TransitRoutePlanOption().city("北京").from(start).to(end));


        }};



    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        map = (MapView) findViewById(R.id.map);



        // ① 给特定的及位置 设置标记
       // BaiduMap baiduMap = this.map.getMap();

   //     baiduMap.setTrafficEnabled(true);

//        LatLng lng = new LatLng(39.963175, 116.400244);
//        BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher);
//        MarkerOptions options = new MarkerOptions().draggable(true).position(lng).icon(bitmapDescriptor);
//        baiduMap.addOverlay(options);
//
//        baiduMap.setOnMarkerDragListener(new BaiduMap.OnMarkerDragListener() {
//            public void onMarkerDrag(Marker marker) {
//                //拖拽中
//            }
//
//            public void onMarkerDragEnd(Marker marker) {
//                //拖拽结束
//                Toast.makeText(MainActivity.this, "拖拽到的位置是" + marker.getPosition().toString(), Toast.LENGTH_SHORT).show();
//            }
//
//            public void onMarkerDragStart(Marker marker) {
//                //开始拖拽
//            }
//        });

        //  call back Interface when the parameter is interface may be callBack Interface
            locationClient = new LocationClient(getApplicationContext());


                // 开启定位图层
              //  setCenter(location);


//            private void setCenter(BDLocation location) {
//                baiduMap.setMyLocationEnabled(true);
//                // 构造定位数据
//                MyLocationData locData = new MyLocationData.Builder()
//                        .accuracy(location.getRadius())
//                        // 此处设置开发者获取到的方向信息，顺时针0-360
//                        .direction(100).latitude(location.getLatitude())
//                        .longitude(location.getLongitude()).build();
//                baiduMap.setMyLocationData(locData);
//                baiduMap.setMyLocationData(locData);
//
//                // set center point  show  the map  embrace two point
//                // this way is wrong can't be scale
////                baiduMap.setMapStatusLimits(new LatLngBounds.Builder().
////                        include(new LatLng(location.getLatitude() + 0.01, location.getLongitude() + 0.01))
////                        .include(new LatLng(location.getLatitude() + 0.01, location.getLongitude() + 0.01)
////                        ).build());
//
//            // show  comparing size
//             baiduMap.setMapStatus(MapStatusUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(),location.getLongitude()),15));
//
//            }

        initLocation();
        locationClient.registerLocationListener(bdLocationListener);
        locationClient.start();



        // start search eat and play Thing
//        PoiSearch poiSearch = PoiSearch.newInstance();
//        poiSearch.setOnGetPoiSearchResultListener(new OnGetPoiSearchResultListener() {
//            @Override
//            public void onGetPoiResult(PoiResult poiResult) {
//
//                if (poiResult.error == PoiResult.ERRORNO.NO_ERROR) {
//
//                    List<PoiAddrInfo> allPois = poiResult.getAllAddr();
//                    for (PoiAddrInfo info :allPois){
//
//                        //print the thing that you search
//                        Log.d(TAG, "onGetPoiResult: "+info.name);
//                    }
//                }
//
//            }
//
//            @Override
//            public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {
//
//            }
//        });

        // this is in create Method call by sys  and set PageNum
        // search form EditText
     //   poiSearch.searchInCity(new PoiCitySearchOption().city("北京 ").keyword("KFC").pageNum(10));

    }


    @Override
    protected void onResume() {
        super.onResume();
        map.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        map.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        map.onDestroy();
    }

    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy
        );//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
        int span = 1000;
        option.setScanSpan(0);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.setLocationNotify(true);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤gps仿真结果，默认需要
        locationClient.setLocOption(option);
    }


}
