package cn.truth.ibdqr;

import android.content.Context;
import android.graphics.Rect;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.PlanarYUVLuminanceSource;
import com.google.zxing.ReaderException;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Hashtable;
import java.util.Map;
import java.util.Vector;

import cn.truth.ibdrqlib.decode.IDecode;
import cn.truth.utils.ConvertUtils;

public class ZxingDecode implements IDecode {

    //    private final MultiFormatReader multiFormatReader;
    private MultiFormatReader multiFormatReader;
    private Hashtable<DecodeHintType, Object> hints;

    public ZxingDecode() {

//        Map<DecodeHintType, Object> mHints = new Hashtable<>();
//        mHints.put(DecodeHintType.CHARACTER_SET, "utf-8");
//        mHints.put(DecodeHintType.TRY_HARDER, Boolean.TRUE);
//        mHints.put(DecodeHintType.POSSIBLE_FORMATS, BarcodeFormat.QR_CODE);

        multiFormatReader = new MultiFormatReader();



        hints = new Hashtable<DecodeHintType, Object>(2);
        hints.put(DecodeHintType.CHARACTER_SET, "utf-8");

        Collection<BarcodeFormat> decodeFormats = new ArrayList<>();
        decodeFormats.add(BarcodeFormat.CODABAR);  // Bar code
        decodeFormats.add(BarcodeFormat.UPC_A);
        decodeFormats.add(BarcodeFormat.UPC_E);
        decodeFormats.add(BarcodeFormat.EAN_8);
        decodeFormats.add(BarcodeFormat.EAN_13);
        decodeFormats.add(BarcodeFormat.RSS_14);
        decodeFormats.add(BarcodeFormat.CODE_39);
        decodeFormats.add(BarcodeFormat.CODE_93);
        decodeFormats.add(BarcodeFormat.CODE_128);
        decodeFormats.add(BarcodeFormat.ITF);
        decodeFormats.add(BarcodeFormat.RSS_EXPANDED);

        decodeFormats.add(BarcodeFormat.AZTEC); // QRCode
        decodeFormats.add(BarcodeFormat.DATA_MATRIX);
        decodeFormats.add(BarcodeFormat.MAXICODE);
        decodeFormats.add(BarcodeFormat.PDF_417);
        decodeFormats.add(BarcodeFormat.QR_CODE);
        decodeFormats.add(BarcodeFormat.UPC_EAN_EXTENSION);

        hints.put(DecodeHintType.POSSIBLE_FORMATS, decodeFormats);

        multiFormatReader.setHints(hints);
    }

    @Override
    public String decode(byte[] data, int width, int height) {
        Result rawResult = null;

        System.out.println("Truth->"+width+","+height+","+ConvertUtils.bytes2HexString(data));

        SourceData sourceData = new SourceData(data, width, height, 0, 90);
        sourceData.setCropRect(new Rect(0, 0, height, width));
        PlanarYUVLuminanceSource source = sourceData.createSource();

        if (source != null) {
            BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
            try {
                rawResult = multiFormatReader.decode(bitmap, hints);
//                rawResult = multiFormatReader.decodeWithState(bitmap);
            } catch (ReaderException re) {
                // continue
            } finally {
                multiFormatReader.reset();
//                multiFormatReader.reset();
            }
        }
        if (rawResult == null) {
            return null;
        }

        System.out.println("rawResult->" + rawResult.toString());
        System.out.println("rawResult 2->" + rawResult.getText());

        return rawResult.getText();
    }
}
