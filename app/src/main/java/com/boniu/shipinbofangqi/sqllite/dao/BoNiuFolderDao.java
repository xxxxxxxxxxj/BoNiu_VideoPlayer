package com.boniu.shipinbofangqi.sqllite.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.boniu.shipinbofangqi.log.RingLog;
import com.boniu.shipinbofangqi.mvp.model.entity.BoNiuFolderInfo;
import com.boniu.shipinbofangqi.sqllite.helper.DBHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date zhoujunxia on 2020-02-29 12:30
 */
public class BoNiuFolderDao {

    private DBHelper dbHelper;

    public BoNiuFolderDao(Context context) {
        dbHelper = new DBHelper(context);
    }

    /**
     * 添加一个文件夹
     */
    public void add(BoNiuFolderInfo boNiuFolderInfo) {
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        // insert
        ContentValues values = new ContentValues();
        values.put("boniu_folder_name", boNiuFolderInfo.getBoniu_folder_name());
        values.put("boniu_folder_formatmemory", boNiuFolderInfo.getBoniu_folder_formatmemory());
        values.put("boniu_folder_memory", boNiuFolderInfo.getBoniu_folder_memory());
        values.put("boniu_folder_createtime", boNiuFolderInfo.getBoniu_folder_createtime());
        long id = database.insert("boniu_folder", null, values);
        RingLog.e("id = " + id);
        // 保存产生的id
        boNiuFolderInfo.setBoniu_folder_id((int) id);
        database.close();
    }

    /**
     * 查询所有文件夹
     *
     * @return
     */
    public List<BoNiuFolderInfo> getAll() {
        List<BoNiuFolderInfo> list = new ArrayList<BoNiuFolderInfo>();
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        // query
        String sql = "select * from boniu_folder order by boniu_folder_id desc";
        Cursor cursor = database.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            int boniu_folder_id = cursor.getInt(0);
            String boniu_folder_name = cursor.getString(1);
            String boniu_folder_formatmemory = cursor.getString(2);
            double boniu_folder_memory = cursor.getDouble(3);
            String boniu_folder_createtime = cursor.getString(4);
            BoNiuFolderInfo boNiuFolderInfo = new BoNiuFolderInfo(boniu_folder_id,
                    boniu_folder_name, boniu_folder_formatmemory, boniu_folder_memory,
                    boniu_folder_createtime);
            list.add(boNiuFolderInfo);
        }
        cursor.close();
        database.close();
        RingLog.e("list = " + list.toString());
        return list;
    }

    /**
     * 查询除了指定文件夹之外的所有文件夹
     *
     * @return
     */
    public List<BoNiuFolderInfo> getAll(int local_boniu_folder_id) {
        List<BoNiuFolderInfo> list = new ArrayList<BoNiuFolderInfo>();
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        // query
        String sql = "select * from boniu_folder where boniu_folder_id!=? order by boniu_folder_id desc";
        Cursor cursor = database.rawQuery(sql, new String[]{String.valueOf(local_boniu_folder_id)});
        while (cursor.moveToNext()) {
            int boniu_folder_id = cursor.getInt(0);
            String boniu_folder_name = cursor.getString(1);
            String boniu_folder_formatmemory = cursor.getString(2);
            double boniu_folder_memory = cursor.getDouble(3);
            String boniu_folder_createtime = cursor.getString(4);
            BoNiuFolderInfo boNiuFolderInfo = new BoNiuFolderInfo(boniu_folder_id,
                    boniu_folder_name, boniu_folder_formatmemory, boniu_folder_memory,
                    boniu_folder_createtime);
            list.add(boNiuFolderInfo);
        }
        cursor.close();
        database.close();
        RingLog.e("list = " + list.toString());
        return list;
    }

    /**
     * 判断文件夹是否已存在
     *
     * @return
     */
    public boolean isExists(String boniu_folder_name) {
        List<BoNiuFolderInfo> all = getAll();
        boolean isExists = false;
        if (all != null && all.size() > 0) {
            for (int i = 0; i < all.size(); i++) {
                if (all.get(i).getBoniu_folder_name().equals(boniu_folder_name)) {
                    isExists = true;
                    break;
                }
            }
        }
        return isExists;
    }

    /**
     * 更新文件夹名字
     **/
    public void updateFolderName(int boniu_folder_id, String boniu_folder_name) {
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        // update
        ContentValues values = new ContentValues();
        values.put("boniu_folder_name", boniu_folder_name);
        int updateCount = database.update("boniu_folder", values,
                "boniu_folder_id=" + boniu_folder_id, null);
        RingLog.e("updateCount = " + updateCount);
        database.close();
    }

    /**
     * 更新文件夹大小
     **/
    public void updateFolderSize(int boniu_folder_id, double boniu_folder_memory, String boniu_folder_formatmemory) {
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        // update
        ContentValues values = new ContentValues();
        values.put("boniu_folder_memory", boniu_folder_memory);
        values.put("boniu_folder_formatmemory", boniu_folder_formatmemory);
        int updateCount = database.update("boniu_folder", values,
                "boniu_folder_id=" + boniu_folder_id, null);
        RingLog.e("updateCount = " + updateCount);
        database.close();
    }

    /**
     * 删除一个文件夹
     **/
    public void deleteById(int boniu_folder_id) {
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        // delete
        int deleteCount = database.delete("boniu_folder", "boniu_folder_id=?",
                new String[]{boniu_folder_id + ""});
        RingLog.e("deleteCount = " + deleteCount);
        database.close();
    }
}
