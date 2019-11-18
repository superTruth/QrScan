package cn.truth.ibdqr;

import com.google.zxing.PlanarYUVLuminanceSource;
import com.landicorp.android.scan.decode.DecodeEngine;

import cn.truth.ibdrqlib.decode.IDecode;

public class LandiDecode implements IDecode {

    private DecodeEngine decodeEngine;
    public LandiDecode(){
        decodeEngine = DecodeEngine.getInstance();
        decodeEngine.init(false, true);
    }

    @Override
    public String decode(byte[] data, int width, int height) {
        String resultTextString = decodeEngine.decode(data, width, height);

        if (resultTextString == null) {
            PlanarYUVLuminanceSource source = new PlanarYUVLuminanceSource(data, width, height, 0, 0,
                    width, height, false);
            data = source.invert().getMatrix();

            resultTextString = decodeEngine.decode(data, width, height);
        }

        return resultTextString;
    }
}
