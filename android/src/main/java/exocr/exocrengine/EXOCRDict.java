package exocr.exocrengine;

import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.wintone.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import exocr.activity.OcrActivity;

/**
 * description: 初始化字典
 * create by kalu on 2018/11/16 9:34
 */
public final class EXOCRDict {
    Context context;

    private static final void clearDict() {

        int code = EXOCREngine.nativeDone();
        Log.e("kalu", "clearDict ==> code = "+code);
    }

    private static final boolean checkSign(final Context context) {
        int code = EXOCREngine.nativeCheckSignature(context);
        Log.e("kalu", "checkSign ==> code = " + code);
        return code == 1;
    }

    private static final boolean checkDict(final String path) {

        final byte[] bytes = path.getBytes();
        Log.e("bytes",String.valueOf(bytes.length));
        Log.e("path",path);
         int code = EXOCREngine.nativeInit(bytes);
        Log.e("kalu", "checkDict ==> code = " + code);
        return true;
    }

    private static final boolean checkFile(final Context context, final String pathname) {

        final File file = new File(pathname);
        if (!file.exists() || file.isDirectory()) {

            file.delete();

            try {
                file.createNewFile();

                int byteread;
                final byte[] buffer = new byte[1024];
//                Log.e("resources",context.getResources());
                final InputStream is = context.getResources().openRawResource(R.raw.zocr0);
                final OutputStream fs = new FileOutputStream(file);// to为要写入sdcard中的文件名称

                while ((byteread = is.read(buffer)) != -1) {
                    fs.write(buffer, 0, byteread);
                }
                is.close();
                fs.close();
                Log.e("greated",String.valueOf(file.length()) );
                return true;
            } catch (Exception e) {
                Log.e("kalu", "checkFile ==> message = " + e.getMessage(), e);
                return false;
            }
        } else {
            Log.e("exists",String.valueOf(file.length()) );
            return true;
        }
    }

    public static final boolean InitDict(final Context activity) {

        final String name = "/zocr0.lib";
        final String path = activity.getExternalCacheDir().getPath();
        final String pathname = path + name;
        clearDict();
        // step1: 检测字典是否存在
        boolean okFile = checkFile(activity, pathname);
        if (!okFile) {
            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            builder.setTitle("checkFile失败\n");
            builder.setMessage("请检查识字典文件是否存在");
            builder.setCancelable(true);
            builder.create().show();

            clearDict();
            return false;
        }
        final File file = new File(pathname);
        Log.e("file",String.valueOf(file.length()));
        // step2: 检测字典是否正确
        boolean okDict = checkDict(path);
        if (!okDict) {
            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            builder.setTitle("checkDict失败\n");
            builder.setMessage("请检查识字典文件是否正确");
            builder.setCancelable(true);
            builder.create().show();

            clearDict();
            return false;
        }

        // step3: 检测字典签名
        boolean okSign = checkSign(activity);
//        if (okSign) {
//            activity.setOnActivityLifecycleListener(new OcrActivity.OnActivityLifecycleListener() {
//                @Override
//                public void onFinish() {
//                    clearDict();
//                }
//            });
//        } else {
//            clearDict();
//        }
//        clearDict();
        return okSign;
    }
}
