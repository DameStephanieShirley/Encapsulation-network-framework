package com.example.liyang.baimap;

import android.app.Application;

import com.baidu.mapapi.SDKInitializer;

/**
 * Created by liyang on 2016/3/26.
 */
public class BaseAc extends Application{

    @Override
    public void onCreate() {
        super.onCreate();
        SDKInitializer.initialize(this);
    }
}
