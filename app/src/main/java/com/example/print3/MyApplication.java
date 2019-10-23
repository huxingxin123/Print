package com.example.print3;

import android.app.Application;
import android.posapi.PosApi;

public class MyApplication extends Application {
    private String mCurDev = "";
    static MyApplication instance = null;
    //PosSDK mSDK = null;
    PosApi mPosApi = null;
    public MyApplication(){
        super.onCreate();
        instance = this;
    }

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        //mDb = Database.getInstance(this);
        mPosApi = PosApi.getInstance(this);

    }

    public static MyApplication getInstance(){
        if(instance==null){
            instance =new MyApplication();
        }
        return instance;
    }

    public String getCurDevice() {
        return mCurDev;
    }

    public void setCurDevice(String mCurDev) {
        this.mCurDev = mCurDev;
    }

    public PosApi getPosApi(){
        return mPosApi;
    }
}
