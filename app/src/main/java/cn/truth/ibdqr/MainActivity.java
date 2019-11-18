package cn.truth.ibdqr;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Result;
import com.google.zxing.common.GlobalHistogramBinarizer;
import com.google.zxing.common.HybridBinarizer;

import java.io.File;
import java.util.Hashtable;
import java.util.Map;

import cn.truth.ibdrqlib.helpful.Utils;
import cn.truth.utils.BaseUtils;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BaseUtils.init(getApplication());

        System.out.println("model->" + Build.MODEL);

        initView();

//        testBarcode();
    }

    private Button btnScan;

    private void initView() {
        btnScan = findViewById(R.id.btn_scan);

        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ScanActivity.class);
                startActivity(intent);
//                IntentIntegrator integrator = new IntentIntegrator(MainActivity.this);
//                // 设置要扫描的条码类型，ONE_D_CODE_TYPES：一维码，QR_CODE_TYPES-二维码
//                integrator.setDesiredBarcodeFormats(IntentIntegrator.ONE_D_CODE_TYPES);
//                integrator.setCaptureActivity(ScanActivity.class); //设置打开摄像头的Activity
//                integrator.setPrompt("请扫描ISBN"); //底部的提示文字，设为""可以置空
//                integrator.setCameraId(0); //前置或者后置摄像头
//                integrator.setBeepEnabled(true); //扫描成功的「哔哔」声，默认开启
//                integrator.setBarcodeImageEnabled(true);
//                integrator.initiateScan();

            }
        });
    }

    private void testBarcode() {
        Result result = null;
        try {
            Bitmap bitmap = BitmapFactory.decodeFile("/sdcard/barcode2.png");

            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            int[] pixels = new int[width * height];
            bitmap.getPixels(pixels, 0, width, 0, 0, width, height);
            // 新建一个RGBLuminanceSource对象
            RGBLuminanceSource source = new RGBLuminanceSource(width, height, pixels);
            // 将图片转换成二进制图片
            BinaryBitmap binaryBitmap = new BinaryBitmap(new GlobalHistogramBinarizer(source));

            result = new MultiFormatReader().decode(binaryBitmap, null);
            System.out.println("Truth->" + result.getText());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
