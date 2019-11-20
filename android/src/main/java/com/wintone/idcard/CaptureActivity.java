package com.wintone.idcard;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Point;
import android.hardware.Camera;
import android.hardware.Camera.ShutterCallback;
import android.media.AudioManager;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.wintone.R;
import com.wintone.view.ViewfinderView;

import exocr.exocrengine.EXOCRModel;
import exocr.idcard.CameraManager;
import exocr.idcard.CaptureHandler;
import exocr.idcard.PreviewCallback;
import exocr.idcard.ViewUtil;
import exocr.view.CaptureView;
//import exocr.view.CaptureView;

/**
 * description: 证件扫描
 * create by kalu on 2018/11/19 13:16
 */
public final class CaptureActivity extends Activity implements SurfaceHolder.Callback {

    public static final int RESULT_CODE = 2001;
    public static final String INTNET_FRONT = "ShouldFront";
    private ViewfinderView myView;
    public static final String EXTRA_SCAN_RESULT = "exocr.idcard.scanResult";
    private static final String TAG = CaptureActivity.class.getSimpleName();
    private CaptureHandler handler;
    private boolean hasSurface;

    public static final int PHOTO_ID = 0x1025;
    private boolean bPhotoReco;
    private RelativeLayout re_c;
    private boolean bshouleFront;
    private boolean bCamera;
    private ImageButton flash;
    private ImageButton back;
    private final ShutterCallback shutterCallback = new ShutterCallback() {
        public void onShutter() {
            AudioManager mgr = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
            mgr.playSoundEffect(AudioManager.FLAG_PLAY_SOUND);
        }
    };
    private int width;
    private int height;
    private boolean isFatty = false;
    private Camera camera;

    @Override
    public void finish() {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.finish();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final View root = LayoutInflater.from(getApplicationContext()).inflate(R.layout.activity_capture, null);
        final boolean isFront = getIntent().getBooleanExtra(INTNET_FRONT, true);
        final CaptureView capture = root.findViewById(R.id.captuer_scan);
        capture.setFront(isFront);
        setContentView(root);


        // 检测摄像头权限
        bCamera = hardwareSupportCheck();
        // CameraManager
        CameraManager.init(getApplication());
        // 横屏
        // setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        // FLAG_TRANSLUCENT_NAVIGATION
        if (VERSION.SDK_INT >= 19) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }

        if (bCamera) {
            hasSurface = false;
            bPhotoReco = false;
            bshouleFront = getIntent().getBooleanExtra(INTNET_FRONT, true);
            Log.d(TAG, "bshouleFront:" + bshouleFront);
            if (bshouleFront) {
                Log.d(TAG, "正面");
            } else {
                Log.d(TAG, "反面");
            }
        }
        findView();
    }
    private void findView(){
        this.back = (ImageButton) findViewById(R.id.back_camera);
        this.flash = (ImageButton) findViewById(R.id.flash_camera);
        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        this.width = metric.widthPixels;
        this.height = metric.heightPixels;
        if (this.width * 3 == this.height * 4) {
            this.isFatty = true;
        }
        int back_w = (int) (((double) this.width) * 0.066796875d);
        int back_h = back_w;
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(back_w, back_h);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        int Fheight = this.height;
        if (this.isFatty) {
            Fheight = (int) (((double) this.height) * 0.75d);
        }
        layoutParams.leftMargin = (int) ((((((double) this.width) - ((((double) Fheight) * 0.8d) * 1.585d)) / 2.0d) - ((double) back_h)) / 2.0d);
        layoutParams.bottomMargin = (int) (((double) this.height) * 0.10486111111111111d);
        this.back.setLayoutParams(layoutParams);
        int flash_w = (int) (((double) this.width) * 0.066796875d);
        layoutParams = new RelativeLayout.LayoutParams(flash_w, flash_w);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
        if (this.isFatty) {
            Fheight = (int) (((double) this.height) * 0.75d);
        }
        layoutParams.leftMargin = (int) ((((((double) this.width) - ((((double) Fheight) * 0.8d) * 1.585d)) / 2.0d) - ((double) back_h)) / 2.0d);
        layoutParams.topMargin = (int) (((double) this.height) * 0.10486111111111111d);
        this.flash.setLayoutParams(layoutParams);
        this.back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                CaptureActivity.this.finish();
            }
        });
        this.flash.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(CameraManager.get()!=null){
                    CameraManager.get().switchFlashlight();
                }
            }
        });
        this.re_c = (RelativeLayout) findViewById(R.id.re_c);
        this.myView = new ViewfinderView(this, this.width, this.height,this.isFatty);
        this.re_c.addView(this.myView);
    }

    public  boolean hardwareSupportCheck() {
        // Camera needs to open
        boolean ret = true;
        try {
            this.camera = Camera.open();
        } catch (RuntimeException e) {
            // throw new RuntimeException();
            ret = false;
        }
        if (this.camera == null) { // 没有背摄像头
            return false;
        }
        if (ret) {
            this.camera.release();
            this.camera=null;
        }
        return ret;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (bCamera && bPhotoReco == false) {
            int IDpreview_viewId = ViewUtil.getResourseIdByName(getApplicationContext().getPackageName(), "id", "IDpreview_view");
            SurfaceView surfaceView = findViewById(IDpreview_viewId);
            SurfaceHolder surfaceHolder = surfaceView.getHolder();
            if (hasSurface) {
                initCamera(surfaceHolder);
            } else {
                surfaceHolder.addCallback(this);
                surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (handler != null) {
            handler.quitSynchronously();
            handler = null;
        }
        CameraManager.get().closeDriver();
    }

    private void initCamera(SurfaceHolder surfaceHolder) {
        try {
            CameraManager.get().openCamera(surfaceHolder);
        } catch (Exception ioe) {
            return;
        }
        if (handler == null) {
            handler = new CaptureHandler(this);
        }
    }

    public Handler getHandler() {
        return handler;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        try {
            if (VERSION.SDK_INT < 14) {        //lower than android 4.0
                return false;
            }
            if (bCamera) {
                float x = event.getX();
                float y = event.getY();
                Point res = CameraManager.get().getResolution();

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (x > res.x * 8 / 10 && y < res.y / 4) {
                        return false;
                    }

                    handleDecode(null);

                    // 点击重新聚焦
                    if (handler != null) {
                        handler.restartAutoFocus();
                    }
                    return true;
                }
            }
        } catch (NullPointerException e) {

        }
        return false;
    }

    public ShutterCallback getShutterCallback() {
        return shutterCallback;
    }

    /**********************************************************************************************/

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (!hasSurface && bPhotoReco == false) {
            hasSurface = true;
            initCamera(holder);
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        hasSurface = false;
    }

    /**********************************************************************************************/

    public void handleDecode(EXOCRModel result) {

        if (null == result)
            return;

        if (result.isDecodeSucc()) {
            final boolean isFront = getIntent().getBooleanExtra(INTNET_FRONT, true);
            if(!isFront&result.cardnum!=null){
                return;
            }else if(isFront&result.cardnum==null){
                return;
            }else {
                Intent intent = new Intent();
                intent.putExtra(EXTRA_SCAN_RESULT, result);
                setResult(RESULT_CODE, intent);
                finish();
            }
        } else {
            Message message = Message.obtain(this.getHandler(), PreviewCallback.PARSE_FAIL);
            message.sendToTarget();
        }
    }
}