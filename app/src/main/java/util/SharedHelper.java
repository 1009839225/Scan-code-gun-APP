package util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import java.util.HashMap;
import java.util.Map;

public class SharedHelper {
    private Context mContext;

    public SharedHelper() {
    }

    public SharedHelper(Context mContext) {
        this.mContext = mContext;
    }

    // 保存
    public void save(String strname, String strpasswd, String pname) {
        SharedPreferences sp = mContext.getSharedPreferences("mysp1",
                Context.MODE_PRIVATE);
        Editor editor = sp.edit();

        editor.putString("strname", strname);//用户名
        editor.putString("strpasswd", strpasswd);//密码
        editor.putString("pname", pname);//账套

        editor.apply();
    }

    // 读取
    public Map<String, String> read() {
        Map<String, String> data = new HashMap<>();
        SharedPreferences sp = mContext.getSharedPreferences("mysp1",
                Context.MODE_PRIVATE);
        data.put("strname", sp.getString("strname", ""));//读取用户名

        return data;
    }

    // 保存2
    public void save(String userwb) {
        SharedPreferences sp = mContext.getSharedPreferences("mysp2",
                Context.MODE_PRIVATE);
        Editor editor = sp.edit();

        editor.putString("userwb", userwb);//IP地址

        editor.commit();
    }

    // 读取2
    public Map<String, String> read2() {
        Map<String, String> data = new HashMap<>();
        SharedPreferences sp = mContext.getSharedPreferences("mysp2",
                Context.MODE_PRIVATE);

        data.put("userwb", sp.getString("userwb", ""));//读取IP地址
        return data;
    }
}
