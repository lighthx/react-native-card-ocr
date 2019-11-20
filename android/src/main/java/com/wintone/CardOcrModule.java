package com.wintone;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.facebook.react.bridge.ActivityEventListener;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.BaseActivityEventListener;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Promise;
import com.wintone.idcard.CaptureActivity;
import com.wintone.smartvision_bankCard.ScanCamera;
import com.facebook.react.bridge.WritableMap;

import exocr.exocrengine.EXOCRDict;
import exocr.exocrengine.EXOCRModel;


public class CardOcrModule extends ReactContextBaseJavaModule {

    private final ReactApplicationContext reactContext;
    String type="FRONT";

    private Promise _promise;

    public CardOcrModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;
        boolean succ = EXOCRDict.InitDict(reactContext.getApplicationContext());
        if (!succ) return;
        reactContext.addActivityEventListener(mActivityEventListener);
    }

    private final ActivityEventListener mActivityEventListener = new BaseActivityEventListener() {

        @Override
        public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {
            if (requestCode == 277) {
                switch (resultCode) {
                    case 250:
                        String bankNumber = data.getExtras().getString("StringR");
                        bankNumber=bankNumber.replaceAll("\u0000","");
                        WritableMap _map = Arguments.createMap();
                        Log.e("bankNumber",bankNumber);
                        _map.putString("bankNumber", bankNumber);
                        _promise.resolve(_map);
                        break;
                    case 2001:
                        final Bundle extras = data.getExtras();
                        final EXOCRModel result = extras.getParcelable(CaptureActivity.EXTRA_SCAN_RESULT);
                        WritableMap map=Arguments.createMap();
                        Log.e("kalu", "result = " + result);
                        map.putString("idCard",result.cardnum);
                        map.putString("image",result.bitmapPath);
                        map.putString("name",result.name);
                        map.putString("gender",result.sex);
                        map.putString("nation",result.nation);
                        map.putString("address",result.address);
                        map.putString("issue",result.office);
                        map.putString("valid",result.validdate);
                        map.putString("type",type.equals("FRONT")?"正面":"反面");
                        _promise.resolve(map);
                        break;
                    default:
                        if(_promise!=null){
                            _promise.reject("400", "cancel");
                        }
                }

            }
        }
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
        _promise = promise;
        Intent intent = new Intent(reactContext, CaptureActivity.class);
        this.type=type;
        Activity currentActivity = getCurrentActivity();
        if(type.equals("BACK") ){
            intent.putExtra(CaptureActivity.INTNET_FRONT, false);
        }else{
            intent.putExtra(CaptureActivity.INTNET_FRONT, true);
        }
        currentActivity.startActivityForResult(intent,277,null);
        _promise=promise;
    }

}
