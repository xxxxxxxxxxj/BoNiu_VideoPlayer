package com.boniu.shipinbofangqi.sqllite.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.boniu.shipinbofangqi.log.RingLog;
import com.boniu.shipinbofangqi.mvp.model.entity.BoNiuFolderInfo;
import com.boniu.shipinbofangqi.sqllite.helper.DBHelper;
import com.boniu.shipinbofangqi.util.CommonUtil;

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
    private Context mContext;
    private DBHelper dbHelper;

    public BoNiuFolderDao(Context context) {
        dbHelper = new DBHelper(context);
        this.mContext = context;
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
        values.put("boniu_folder_isdefault", boNiuFolderInfo.getBoniu_folder_isdefault());
        values.put("boniu_folder_account", boNiuFolderInfo.getBoniu_folder_account());
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
        String sql = "select * from boniu_folder where boniu_folder_account=? order by boniu_folder_id desc";
        Cursor cursor = database.rawQuery(sql, new String[]{CommonUtil.getCellPhone(mContext)});
        while (cursor.moveToNext()) {
            int boniu_folder_id = cursor.getInt(0);
            String boniu_folder_name = cursor.getString(1);
            String boniu_folder_formatmemory = cursor.getString(2);
            double boniu_folder_memory = cursor.getDouble(3);
            String boniu_folder_createtime = cursor.getString(4);
            int boniu_folder_isdefault = cursor.getInt(5);
            String boniu_folder_account = cursor.getString(6);
            BoNiuFolderInfo boNiuFolderInfo = new BoNiuFolderInfo(boniu_folder_id,
                    boniu_folder_name, boniu_folder_formatmemory, boniu_folder_memory,
                    boniu_folder_createtime, boniu_folder_isdefault, boniu_folder_account);
            list.add(boNiuFolderInfo);
        }
        cursor.close();
        database.close();
        RingLog.e("list = " + list.toString());
        return list;
    }

    /**
     * 查询所有文件夹
     *
     * @return
     */
    public List<BoNiuFolderInfo> getAllByPage(int page) {
        List<BoNiuFolderInfo> list = new ArrayList<BoNiuFolderInfo>();
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        // query
        String sql = "select * from boniu_folder where boniu_folder_account=? order by boniu_folder_id desc limit 10 offset ?";
        Cursor cursor = database.rawQuery(sql, new String[]{CommonUtil.getCellPhone(mContext),String.valueOf((page - 1) * 10)});
        while (cursor.moveToNext()) {
            int boniu_folder_id = cursor.getInt(0);
            String boniu_folder_name = cursor.getString(1);
            String boniu_folder_formatmemory = cursor.getString(2);
            double boniu_folder_memory = cursor.getDouble(3);
            String boniu_folder_createtime = cursor.getString(4);
            int boniu_folder_isdefault = cursor.getInt(5);
            String boniu_folder_account = cursor.getString(6);
            BoNiuFolderInfo boNiuFolderInfo = new BoNiuFolderInfo(boniu_folder_id,
                    boniu_folder_name, boniu_folder_formatmemory, boniu_folder_memory,
                    boniu_folder_createtime, boniu_folder_isdefault,boniu_folder_account);
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
    public List<BoNiuFolderInfo> getAllByFolderId(int local_boniu_folder_id) {
        List<BoNiuFolderInfo> list = new ArrayList<BoNiuFolderInfo>();
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        // query
        String sql = "select * from boniu_folder where boniu_folder_id!=? and boniu_folder_account=? order by boniu_folder_id desc";
        Cursor cursor = database.rawQuery(sql, new String[]{String.valueOf(local_boniu_folder_id),CommonUtil.getCellPhone(mContext)});
        while (cursor.moveToNext()) {
            int boniu_folder_id = cursor.getInt(0);
            String boniu_folder_name = cursor.getString(1);
            String boniu_folder_formatmemory = cursor.getString(2);
            double boniu_folder_memory = cursor.getDouble(3);
            String boniu_folder_createtime = cursor.getString(4);
            int boniu_folder_isdefault = cursor.getInt(5);
            String boniu_folder_account = cursor.getString(6);
            BoNiuFolderInfo boNiuFolderInfo = new BoNiuFolderInfo(boniu_folder_id,
                    boniu_folder_name, boniu_folder_formatmemory, boniu_folder_memory,
                    boniu_folder_createtime, boniu_folder_isdefault,boniu_folder_account);
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
    public List<BoNiuFolderInfo> getAllByFolderIdByPage(int local_boniu_folder_id, int page) {
        List<BoNiuFolderInfo> list = new ArrayList<BoNiuFolderInfo>();
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        // query
        String sql = "select * from boniu_folder where boniu_folder_id!=? and boniu_folder_account=? order by boniu_folder_id desc limit 10 offset ?";
        Cursor cursor = database.rawQuery(sql, new String[]{String.valueOf(local_boniu_folder_id) ,CommonUtil.getCellPhone(mContext), String.valueOf((page - 1) * 10)});
        while (cursor.moveToNext()) {
            int boniu_folder_id = cursor.getInt(0);
            String boniu_folder_name = cursor.getString(1);
            String boniu_folder_formatmemory = cursor.getString(2);
            double boniu_folder_memory = cursor.getDouble(3);
            String boniu_folder_createtime = cursor.getString(4);
            int boniu_folder_isdefault = cursor.getInt(5);
            String boniu_folder_account = cursor.getString(6);
            BoNiuFolderInfo boNiuFolderInfo = new BoNiuFolderInfo(boniu_folder_id,
                    boniu_folder_name, boniu_folder_formatmemory, boniu_folder_memory,
                    boniu_folder_createtime, boniu_folder_isdefault,boniu_folder_account);
            list.add(boNiuFolderInfo);
        }
        cursor.close();
        database.close();
        RingLog.e("list = " + list.toString());
        return list;
    }

    /**
     * 查询所有文件夹的名字
     *
     * @return
     */
    public List<String> getAllFolderName() {
        List<String> list = new ArrayList<String>();
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        // query
        String sql = "select boniu_folder_name from boniu_folder where boniu_folder_account=?";
        Cursor cursor = database.rawQuery(sql, new String[]{CommonUtil.getCellPhone(mContext)});
        while (cursor.moveToNext()) {
            String boniu_folder_name = cursor.getString(0);
            list.add(boniu_folder_name);
        }
        cursor.close();
        database.close();
        RingLog.e("list = " + list.toString());
        return list;
    }

    /**
     * 查询所有文件夹的状态
     *
     * @return
     */
    public List<Integer> getAllFolderDefault() {
        List<Integer> list = new ArrayList<Integer>();
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        // query
        String sql = "select boniu_folder_isdefault from boniu_folder where boniu_folder_account=?";
        Cursor cursor = database.rawQuery(sql,  new String[]{CommonUtil.getCellPhone(mContext)});
        while (cursor.moveToNext()) {
            int boniu_folder_isdefault = cursor.getInt(0);
            list.add(boniu_folder_isdefault);
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
        List<String> all = getAllFolderName();
        boolean isExists = false;
        if (all != null && all.size() > 0 && all.contains(boniu_folder_name)) {
            isExists = true;
        }
        return isExists;
    }

    /**
     * 判断默认文件夹是否已存在
     *
     * @return
     */
    public boolean isExists() {
        List<Integer> all = getAllFolderDefault();
        boolean isExists = false;
        if (all != null && all.size() > 0 && all.contains(1)) {
            isExists = true;
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
