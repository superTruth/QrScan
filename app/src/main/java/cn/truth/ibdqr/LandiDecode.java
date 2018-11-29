package cn.truth.ibdqr;

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

        return resultTextString;
    }
}
