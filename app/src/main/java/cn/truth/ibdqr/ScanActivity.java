package cn.truth.ibdqr;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import cn.truth.ibdrqlib.IBDQRScan;
import cn.truth.ibdrqlib.view.FinderView;
import cn.truth.ibdrqlib.view.TSurfaceView;

public class ScanActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);

        initView();

        initScanner();
    }

    private TSurfaceView sv;
    private FinderView fv;
    private void initView(){
        sv = findViewById(R.id.sv);
        fv = findViewById(R.id.fv);
    }

    private IBDQRScan ibdqrScan;
    private void initScanner(){
        ibdqrScan = new IBDQRScan(this, sv, fv, new LandiDecode(), 0);

        ibdqrScan.startScan(new IBDQRScan.ScanListener() {
            @Override
            public void onScan(String content) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                });
                System.out.println("onScan->"+content);
            }
        });
    }
}
