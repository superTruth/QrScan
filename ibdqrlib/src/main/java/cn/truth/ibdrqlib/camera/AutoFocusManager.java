/*
 * Copyright (C) 2012 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cn.truth.ibdrqlib.camera;

import android.hardware.Camera;
import android.util.Log;
import java.util.concurrent.ScheduledFuture;

import cn.truth.ibdrqlib.helpful.IBDQRScanExecutor;

final class AutoFocusManager implements Camera.AutoFocusCallback {

    private static final String TAG = AutoFocusManager.class.getSimpleName();

    private static final long AUTO_FOCUS_INTERVAL_MS = 1000L;

    private ScheduledFuture<?> autoFocuseLoop;

    private boolean focusing = true;
    private final Camera camera;

    public AutoFocusManager(Camera camera) {
        this.camera = camera;
    }

    @Override
    public void onAutoFocus(boolean success, Camera theCamera) {
        autoFocusAgainLater();
    }

    private void autoFocusAgainLater() {
        autoFocuseLoop = IBDQRScanExecutor.getInstance().schedule(runnable, AUTO_FOCUS_INTERVAL_MS);
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            start();
        }
    };

    public void start() {
        if(!focusing){
            return;
        }
        try {
            camera.autoFocus(this);
        } catch (RuntimeException re) {
            Log.w(TAG, "Unexpected exception while focusing", re);
            autoFocusAgainLater();
        }
    }

    public void cancelOutstandingTask() {
        focusing = false;
        if((autoFocuseLoop == null) || (autoFocuseLoop.isCancelled())){
            return;
        }

        autoFocuseLoop.cancel(true);
    }
}
