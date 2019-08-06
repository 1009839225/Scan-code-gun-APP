package util;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.util.Log;

public class Loginconnect {
    //连接webservice的Ip以及参数
    private static final String AddressnameSpace = "http://tongchang.org/";
    //private static final String Addressurl = "http://192.168.1.112:8686/WebService_T_YM.asmx";
    private static final String Addressmethod = "AA_Login";
    private static final String AddresssoapAction = "http://tongchang.org/AA_Login";

    //连接服务器
    public static String getland(String edit1, String edit2, String edit, String Addressurl) throws Exception {
        Log.e("Log", "222");
        Log.e("edit1", edit1);
        Log.e("edit2", edit2);
        Log.e("edit", edit);
        SoapObject soapObject = new SoapObject(AddressnameSpace, Addressmethod);
        soapObject.addProperty("UserName", edit1);
        soapObject.addProperty("Password", edit2);
        soapObject.addProperty("AccNum", edit);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.bodyOut = soapObject;
        envelope.dotNet = true;
        envelope.setOutputSoapObject(soapObject);
        HttpTransportSE httpTransportSE = new HttpTransportSE(Addressurl);
        httpTransportSE.call(AddresssoapAction, envelope);
        Log.e("444", "444");
        SoapObject object = (SoapObject) envelope.bodyIn;
        String beforeresult = object.getProperty(0).toString();

        // handler.sendEmptyMessage(0x001);
        //初始化内容
        return beforeresult;

    }
}
