package com.wintone;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.facebook.react.bridge.ActivityEventListener;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.BaseActivityEventListener;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Promise;
import com.wintone.smartvision_bankCard.ScanCamera;
import com.facebook.react.bridge.WritableMap;


public class CardOcrModule extends ReactContextBaseJavaModule {
    public static final int REQUEST_CODE_FRONT = 1000;
    public static final int REQUEST_CODE_BACK = 1001;
    private final ReactApplicationContext reactContext;

    private Promise _promise;

    public CardOcrModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;
        reactContext.addActivityEventListener(mActivityEventListener);
    }

    private final ActivityEventListener mActivityEventListener = new BaseActivityEventListener() {

        @Override
        public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {
            if(requestCode==277){
            switch (resultCode) {
                case 250:
                    String bankNumber = data.getExtras().getString("StringR");
                    WritableMap _map = Arguments.createMap();
                    _map.putString("bankNumber", bankNumber);
                    _promise.resolve(_map);
                default:
//                    _promise.reject("400", "cancel");
            }

        }}
    };

    @Override
    public String getName() {
        return "CardOcr";
    }


    @ReactMethod
    public void bankCardOcr(Promise promise) {
        Intent intent = new Intent(reactContext, ScanCamera.class);
        Activity currentActivity = getCurrentActivity();
        currentActivity.startActivityForResult(intent, 277, null);
        _promise = promise;
    }

    @ReactMethod
    public void idCardOcr(String type, Promise promise) {
        _promise=promise;
//        _promise.reject("401","未开发");


    }

}
