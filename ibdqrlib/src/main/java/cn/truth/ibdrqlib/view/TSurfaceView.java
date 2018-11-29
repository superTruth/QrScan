package cn.truth.ibdrqlib.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class TSurfaceView extends SurfaceView implements SurfaceHolder.Callback{
    public TSurfaceView(Context context) {
        super(context);
        init();
    }

    public TSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TSurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public TSurfaceView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init(){
        getHolder().addCallback(this);
    }

    private boolean created = false;
    private boolean changed = false;
    private boolean destroyed = false;
    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        created = true;
        destroyed = false;
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        changed = true;
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        destroyed = true;
    }

    public boolean isCreated() {
        return created;
    }

    public boolean isChanged() {
        return changed;
    }

    public boolean isDestroyed() {
        return destroyed;
    }
}
