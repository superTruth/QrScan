package cn.truth.ibdrqlib.camera;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.hardware.Camera;
import android.view.SurfaceHolder;

import java.io.IOException;
import java.util.List;

import cn.truth.ibdrqlib.helpful.Utils;
import cn.truth.ibdrqlib.view.TSurfaceView;

public final class CameraManager implements SurfaceHolder.Callback {
    private Camera camera;
    private TSurfaceView surfaceView;
    private AutoFocusManager autoFocusManager;
    private int cameraID;
    private Context context;

    public CameraManager(Context context, TSurfaceView surfaceView, int cameraID) {
        this.context = context;
        this.surfaceView = surfaceView;
        this.cameraID = cameraID;

        surfaceView.getHolder().addCallback(this);
    }

    private void initParams() {
        camera = Camera.open(cameraID);
        if (camera == null) {
            camera = Camera.open(0);
        }

        if(Utils.isPortrait(context)){
            camera.setDisplayOrientation(90);
        }

        hasRelease = false;

        try {
            camera.setPreviewDisplay(surfaceView.getHolder());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void startPreView() {
        if (camera == null) {
            return;
        }
        camera.startPreview();

        autoFocusManager = new AutoFocusManager(camera);
        autoFocusManager.start();
    }

    public void stopPreView() {
        camera.stopPreview();

        if (autoFocusManager != null) {
            autoFocusManager.cancelOutstandingTask();
        }
    }

    private boolean hasRelease = false;

    public void release() {
        if (hasRelease) {
            return;
        }
        hasRelease = true;
        stopPreView();

        System.out.println("Truth release");
        camera.release();
    }

    public void shotPreView(Camera.PreviewCallback callback) {
        camera.setOneShotPreviewCallback(callback);
    }

    //**********************************************************************
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        System.out.println("Truth->surfaceCreated");
        initParams();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        System.out.println("Truth->surfaceChanged");
        // reset preview size
        Camera.Size size = matchPreViewSize(camera.getParameters().getSupportedPreviewSizes(), width, height);
        Camera.Parameters parameters = camera.getParameters();
        parameters.setPreviewSize(size.width, size.height);
        camera.setParameters(parameters);

        startPreView();

        if (listener != null) {
            listener.onSurfaceChanged();
        }
    }

    private Camera.Size matchPreViewSize(List<Camera.Size> sizes, int width, int height) {
        int minIndex = 0;
        int minValue = Integer.MAX_VALUE;
        for (int i = 0; i < sizes.size(); i++) {
            Camera.Size size = sizes.get(i);

            int tmpValue = Math.abs(size.height - width) + Math.abs(size.width - height);

            if (tmpValue < minValue) {
                minValue = tmpValue;
                minIndex = i;
            }
        }

        return sizes.get(minIndex);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        System.out.println("Truth->surfaceDestroyed");
        if (listener != null) {
            listener.onSurfaceDestroyed();
        }

        release();
    }
    //**********************************************************************

    public Camera getCamera() {
        return camera;
    }

    private StatuesListener listener;

    public void setStatuesListener(StatuesListener listener) {
        this.listener = listener;

        if (surfaceView.isCreated()) {  // 应对未拿到回调的情况
            surfaceCreated(surfaceView.getHolder());

            if (surfaceView.isChanged()) {
                surfaceChanged(surfaceView.getHolder(), 0, surfaceView.getWidth(), surfaceView.getHeight());
            }
        }
    }

    public interface StatuesListener {
        void onSurfaceChanged();

        void onSurfaceDestroyed();
    }
}
