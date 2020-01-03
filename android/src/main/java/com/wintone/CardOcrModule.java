package com.wintone;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.facebook.react.bridge.ActivityEventListener;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.BaseActivityEventListener;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Promise;
import com.facebook.react.modules.core.PermissionAwareActivity;
import com.facebook.react.modules.core.PermissionListener;
import com.wintone.idcard.CaptureActivity;
import com.wintone.smartvision_bankCard.ScanCamera;
import com.facebook.react.bridge.WritableMap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;

import exocr.exocrengine.EXOCRDict;
import exocr.exocrengine.EXOCRModel;


public class CardOcrModule extends ReactContextBaseJavaModule {
    private static final String E_PERMISSIONS_MISSING = "E_PERMISSION_MISSING";
    private static final String E_CALLBACK_ERROR = "E_CALLBACK_ERROR";
    private final ReactApplicationContext reactContext;
    String type="FRONT";

    private Promise _promise;

    public CardOcrModule(final ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;
        EXOCRDict.InitDict(reactContext.getApplicationContext());
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


    private void permissionsCheck(final Activity activity, final Promise promise, final List<String> requiredPermissions, final Callable<Void> callback) {

        List<String> missingPermissions = new ArrayList<>();

        for (String permission : requiredPermissions) {
            int status = ActivityCompat.checkSelfPermission(activity, permission);
            if (status != PackageManager.PERMISSION_GRANTED) {
                missingPermissions.add(permission);
            }
        }

        if (!missingPermissions.isEmpty()) {

            ((PermissionAwareActivity) activity).requestPermissions(missingPermissions.toArray(new String[missingPermissions.size()]), 1, new PermissionListener() {

                @Override
                public boolean onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
                    if (requestCode == 1) {

                        for (int grantResult : grantResults) {
                            if (grantResult == PackageManager.PERMISSION_DENIED) {
                                promise.reject(E_PERMISSIONS_MISSING, "Required permission missing");
                                return true;
                            }
                        }

                        try {
                            callback.call();
                        } catch (Exception e) {
                            promise.reject(E_CALLBACK_ERROR, "Unknown error", e);
                        }
                    }

                    return true;
                }
            });

            return;
        }

        // all permissions granted
        try {
            callback.call();
        } catch (Exception e) {
            promise.reject(E_CALLBACK_ERROR, "Unknown error", e);
        }
    }

    @ReactMethod
    public void bankCardOcr(final Promise promise) {
        final Activity activity = getCurrentActivity();
        permissionsCheck(activity, promise, Arrays.asList(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE), new Callable<Void>() {
            @Override
            public Void call() {
                Intent intent = new Intent(reactContext, ScanCamera.class);
                Activity currentActivity = getCurrentActivity();
                currentActivity.startActivityForResult(intent, 277, null);
                _promise = promise;
                return null;
            }
        });

    }

    @ReactMethod
    public void idCardOcr(final String _type,final Promise promise) {

        final Activity activity = getCurrentActivity();
        permissionsCheck(activity, promise, Arrays.asList(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE), new Callable<Void>() {
            @Override
            public Void call() {
                _promise = promise;
                Intent intent = new Intent(reactContext, CaptureActivity.class);
                type=_type;
                Activity currentActivity = getCurrentActivity();
                if(type.equals("BACK") ){
                    intent.putExtra(CaptureActivity.INTNET_FRONT, false);
                }else{
                    intent.putExtra(CaptureActivity.INTNET_FRONT, true);
                }
                currentActivity.startActivityForResult(intent,277,null);
                _promise=promise;
                return null;
            }
        });


    }

}
