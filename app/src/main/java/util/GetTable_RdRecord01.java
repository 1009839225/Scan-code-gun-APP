package util;

import android.util.Log;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

public class GetTable_RdRecord01 {
    private static final String AddressnameSpace = "http://tongchang.org/";
    // private static final String Addressurl =
    // "http://192.168.1.164:8585/WebService1.asmx";
    private static final String Addressmethod = "GetTable_RdRecord01";
    private static final String AddresssoapAction = "http://tongchang.org/GetTable_RdRecord01";

    // 连接服务器
    public static String getland(String edit, String Addressurl)
            throws Exception {
        Log.e("222", "222");
        Log.e("edit", edit);
        SoapObject soapObject = new SoapObject(AddressnameSpace, Addressmethod);
        soapObject.addProperty("code", edit);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.bodyOut = soapObject;
        envelope.dotNet = true;
        envelope.setOutputSoapObject(soapObject);
        HttpTransportSE httpTransportSE = new HttpTransportSE(Addressurl);
        Log.e("8", "7");
        httpTransportSE.call(AddresssoapAction, envelope);
        Log.e("444", "444");
        SoapObject object = (SoapObject) envelope.bodyIn;
        String beforeresult = object.getProperty(0).toString();
        Log.e("Logresult", beforeresult);
        // handler.sendEmptyMessage(0x001);
        // 初始化内容
        return beforeresult;

    }
}
