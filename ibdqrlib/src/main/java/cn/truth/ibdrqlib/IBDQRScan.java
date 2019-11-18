package cn.truth.ibdrqlib;

import android.arch.lifecycle.Lifecycle;
import android.content.Context;
import android.hardware.Camera;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;

import cn.truth.ibdrqlib.camera.CameraManager;
import cn.truth.ibdrqlib.decode.IDecode;
import cn.truth.ibdrqlib.helpful.IBDQRScanExecutor;
import cn.truth.ibdrqlib.view.IFinder;
import cn.truth.ibdrqlib.view.TSurfaceView;

/***************************************************************************************************
 *                                  Copyright (C), Truth.                                      *
 *                                                                              *
 ***************************************************************************************************
 * usage           : 
 * Version         : 1
 * Author          : Truth
 * Date            : 2018/8/3
 * Modify          : create file
 **************************************************************************************************/
public class IBDQRScan {
    private Lifecycle lifecycle;
    private TSurfaceView surfaceView;

    private CameraManager cameraManager;
    private IDecode decoder;
    private IFinder finder;
    private int cameraID;

    public IBDQRScan(AppCompatActivity activity, TSurfaceView surfaceView, IFinder finder, IDecode decoder, int cameraID){
        lifecycle = activity.getLifecycle();
        this.surfaceView = surfaceView;
        this.finder = finder;
        this.decoder = decoder;
        this.cameraID = cameraID;

        init(activity.getApplicationContext());
    }

    public IBDQRScan(Fragment fragment, TSurfaceView surfaceView, IFinder finder, IDecode decoder, int cameraID){
        lifecycle = fragment.getLifecycle();
        this.surfaceView = surfaceView;
        this.finder = finder;
        this.decoder = decoder;
        this.cameraID = cameraID;

        init(fragment.getActivity().getApplicationContext());
    }

    private void init(Context context){
//        decoder = DecodeFactory.getDecodeModule(context);

        cameraManager = new CameraManager(context, surfaceView, cameraID);

        cameraManager.setStatuesListener(statuesListener);
    }

    private CameraManager.StatuesListener statuesListener = new CameraManager.StatuesListener() {
        @Override
        public void onSurfaceChanged() {
            isScaning = true;
            IBDQRScanExecutor.getInstance().schedule(startShotRunnable, 1000);
        }

        @Override
        public void onSurfaceDestroyed() {
            isScaning = false;
        }
    };

    private Runnable startShotRunnable = new Runnable() {
        @Override
        public void run() {
            if(!isScaning){
                return;
            }
            cameraManager.shotPreView(previewCallback);
        }
    };

    private Camera.PreviewCallback previewCallback = new Camera.PreviewCallback() {
        @Override
        public void onPreviewFrame(byte[] data, Camera camera) {
            if(!isScaning){
                return;
            }

            needDecodeDatas = data;
            Camera.Size size = camera.getParameters().getPreviewSize();

            datasWith = size.width;
            datasHeight = size.height;
            IBDQRScanExecutor.getInstance().schedule(decodeRunnable);
        }


    };

    private byte[] needDecodeDatas;
    private int datasWith;
    private int datasHeight;
    private Runnable decodeRunnable = new Runnable() {
        @Override
        public void run() {
            String decodeRet = decoder.decode(needDecodeDatas, datasWith, datasHeight);
            if(TextUtils.isEmpty(decodeRet)){
                startShotRunnable.run();
                return;
            }
            listener.onScan(decodeRet);

            cameraManager.stopPreView();
        }
    };

    private ScanListener listener;
    private boolean isScaning = false;
    public void startScan(ScanListener listener){
        isScaning = true;
        this.listener = listener;
    }

    public void stopScan(){
        isScaning = false;
        cameraManager.release();
    }


    public interface ScanListener{
        void onScan(String content);
    }
}
