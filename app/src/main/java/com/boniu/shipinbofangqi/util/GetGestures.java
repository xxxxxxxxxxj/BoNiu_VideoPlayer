package com.boniu.shipinbofangqi.util;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date zhoujunxia on 2020-03-18 23:25
 */
public class GetGestures {
    /**
     * 读取固定的文件中的内容,这里就是读取sd卡中保存的设备唯一标识符
     *
     * @param context
     * @return
     */
    public static String readGestures(Context context) {
        try {
            File file = FileUtil.createFile(context, 8, "", null);
            StringBuffer buffer = new StringBuffer();
            FileInputStream fis = new FileInputStream(file);
            InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
            Reader in = new BufferedReader(isr);
            int i;
            while ((i = in.read()) > -1) {
                buffer.append((char) i);
            }
            in.close();
            return buffer.toString();
        } catch (IOException e) {
            Log.e("TAG", "readGestures失败 e = " + e.toString());
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 保存 内容到 SD卡中,  这里保存的就是 设备唯一标识符
     * * @param context
     */
    public static void saveGestures(Context context,String pwd) {
        try {
            File file = FileUtil.createFile(context, 8, "", null);
            FileOutputStream fos = new FileOutputStream(file);
            Writer out = new OutputStreamWriter(fos, "UTF-8");
            out.write(pwd);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("TAG", "saveGestures失败 e = " + e.toString());
        }
    }
}
