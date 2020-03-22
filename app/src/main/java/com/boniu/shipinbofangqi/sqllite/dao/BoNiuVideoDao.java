package com.boniu.shipinbofangqi.sqllite.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.boniu.shipinbofangqi.log.RingLog;
import com.boniu.shipinbofangqi.mvp.model.entity.BoNiuVideoInfo;
import com.boniu.shipinbofangqi.sqllite.helper.DBHelper;
import com.boniu.shipinbofangqi.util.QMUILangHelper;

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
public class BoNiuVideoDao {

    private DBHelper dbHelper;

    public BoNiuVideoDao(Context context) {
        dbHelper = new DBHelper(context);
    }

    /**
     * 添加一条视频
     */
    public void add(BoNiuVideoInfo boNiuVideoInfo) {
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        // insert
        ContentValues values = new ContentValues();
        values.put("boniu_video_name", boNiuVideoInfo.getBoniu_video_name());
        values.put("boniu_video_coverimg", boNiuVideoInfo.getBoniu_video_coverimg());
        values.put("boniu_video_formatmemory", boNiuVideoInfo.getBoniu_video_formatmemory());
        values.put("boniu_video_createtime", boNiuVideoInfo.getBoniu_video_createtime());
        values.put("boniu_video_memory", boNiuVideoInfo.getBoniu_video_memory());
        values.put("boniu_video_url", boNiuVideoInfo.getBoniu_video_url());
        values.put("boniu_video_folder_id", boNiuVideoInfo.getBoniu_video_folder_id());
        values.put("boniu_video_length", boNiuVideoInfo.getBoniu_video_length());
        long id = database.insert("boniu_video", null, values);
        RingLog.e("id = " + id);
        // 保存产生的id
        boNiuVideoInfo.setBoniu_video_id((int) id);
        database.close();
    }

    /**
     * 查询所有没有保存到文件夹的视频
     *
     * @return
     */
    public List<BoNiuVideoInfo> getAll() {
        List<BoNiuVideoInfo> list = new ArrayList<BoNiuVideoInfo>();
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        // query
        String sql = "select * from boniu_video where boniu_video_folder_id<=0 order by boniu_video_id desc";
        Cursor cursor = database.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            int boniu_video_id = cursor.getInt(0);
            String boniu_video_name = cursor.getString(1);
            String boniu_video_coverimg = cursor.getString(2);
            String boniu_video_formatmemory = cursor.getString(3);
            String boniu_video_createtime = cursor.getString(4);
            double boniu_video_memory = cursor.getDouble(5);
            String boniu_video_url = cursor.getString(6);
            int boniu_video_folder_id = cursor.getInt(7);
            String boniu_video_length = cursor.getString(8);
            BoNiuVideoInfo BoNiuVideoInfo = new BoNiuVideoInfo(boniu_video_id,
                    boniu_video_name, boniu_video_coverimg, boniu_video_formatmemory,
                    boniu_video_createtime, boniu_video_memory, boniu_video_url, boniu_video_folder_id, boniu_video_length);
            list.add(BoNiuVideoInfo);
        }
        cursor.close();
        database.close();
        RingLog.e("list = " + list.toString());
        return list;
    }

    /**
     * 查询所有没有保存到文件夹的视频
     *
     * @return
     */
    public List<BoNiuVideoInfo> getAllByPage(int page) {
        List<BoNiuVideoInfo> list = new ArrayList<BoNiuVideoInfo>();
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        // query
        String sql = "select * from boniu_video where boniu_video_folder_id<=0 order by boniu_video_id desc limit 10 offset ?";
        Cursor cursor = database.rawQuery(sql, new String[]{String.valueOf((page - 1) * 10)});
        while (cursor.moveToNext()) {
            int boniu_video_id = cursor.getInt(0);
            String boniu_video_name = cursor.getString(1);
            String boniu_video_coverimg = cursor.getString(2);
            String boniu_video_formatmemory = cursor.getString(3);
            String boniu_video_createtime = cursor.getString(4);
            double boniu_video_memory = cursor.getDouble(5);
            String boniu_video_url = cursor.getString(6);
            int boniu_video_folder_id = cursor.getInt(7);
            String boniu_video_length = cursor.getString(8);
            BoNiuVideoInfo BoNiuVideoInfo = new BoNiuVideoInfo(boniu_video_id,
                    boniu_video_name, boniu_video_coverimg, boniu_video_formatmemory,
                    boniu_video_createtime, boniu_video_memory, boniu_video_url, boniu_video_folder_id, boniu_video_length);
            list.add(BoNiuVideoInfo);
        }
        cursor.close();
        database.close();
        RingLog.e("list = " + list.toString());
        return list;
    }

    /**
     * 查询一个文件夹下所有的视频
     *
     * @return
     */
    public List<BoNiuVideoInfo> getAllByFolderId(int local_boniu_video_folder_id) {
        List<BoNiuVideoInfo> list = new ArrayList<BoNiuVideoInfo>();
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        // query
        String sql = "select * from boniu_video where boniu_video_folder_id=? order by boniu_video_id desc";
        Cursor cursor = database.rawQuery(sql, new String[]{String.valueOf(local_boniu_video_folder_id)});
        while (cursor.moveToNext()) {
            int boniu_video_id = cursor.getInt(0);
            String boniu_video_name = cursor.getString(1);
            String boniu_video_coverimg = cursor.getString(2);
            String boniu_video_formatmemory = cursor.getString(3);
            String boniu_video_createtime = cursor.getString(4);
            double boniu_video_memory = cursor.getDouble(5);
            String boniu_video_url = cursor.getString(6);
            int boniu_video_folder_id = cursor.getInt(7);
            String boniu_video_length = cursor.getString(8);
            BoNiuVideoInfo BoNiuVideoInfo = new BoNiuVideoInfo(boniu_video_id,
                    boniu_video_name, boniu_video_coverimg, boniu_video_formatmemory,
                    boniu_video_createtime, boniu_video_memory, boniu_video_url, boniu_video_folder_id, boniu_video_length);
            list.add(BoNiuVideoInfo);
        }
        cursor.close();
        database.close();
        RingLog.e("list = " + list.toString());
        return list;
    }

    /**
     * 查询一个文件夹下所有的视频
     *
     * @return
     */
    public List<BoNiuVideoInfo> getAllByFolderIdByPage(int local_boniu_video_folder_id, int page) {
        List<BoNiuVideoInfo> list = new ArrayList<BoNiuVideoInfo>();
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        // query
        String sql = "select * from boniu_video where boniu_video_folder_id=? order by boniu_video_id desc limit 10 offset ?";
        Cursor cursor = database.rawQuery(sql, new String[]{String.valueOf(local_boniu_video_folder_id), String.valueOf((page - 1) * 10)});
        while (cursor.moveToNext()) {
            int boniu_video_id = cursor.getInt(0);
            String boniu_video_name = cursor.getString(1);
            String boniu_video_coverimg = cursor.getString(2);
            String boniu_video_formatmemory = cursor.getString(3);
            String boniu_video_createtime = cursor.getString(4);
            double boniu_video_memory = cursor.getDouble(5);
            String boniu_video_url = cursor.getString(6);
            int boniu_video_folder_id = cursor.getInt(7);
            String boniu_video_length = cursor.getString(8);
            BoNiuVideoInfo BoNiuVideoInfo = new BoNiuVideoInfo(boniu_video_id,
                    boniu_video_name, boniu_video_coverimg, boniu_video_formatmemory,
                    boniu_video_createtime, boniu_video_memory, boniu_video_url, boniu_video_folder_id, boniu_video_length);
            list.add(BoNiuVideoInfo);
        }
        cursor.close();
        database.close();
        RingLog.e("list = " + list.toString());
        return list;
    }

    /**
     * 获取文件夹的大小
     *
     * @return
     */
    public double getSizeByFolderId(int local_boniu_video_folder_id) {
        double totalSize = 0;
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        // query
        String sql = "select boniu_video_memory from boniu_video where boniu_video_folder_id=? order by boniu_video_id desc";
        Cursor cursor = database.rawQuery(sql, new String[]{String.valueOf(local_boniu_video_folder_id)});
        while (cursor.moveToNext()) {
            double boniu_video_memory = cursor.getDouble(0);
            totalSize = QMUILangHelper.add(totalSize, boniu_video_memory);
        }
        cursor.close();
        database.close();
        RingLog.e("totalSize = " + totalSize);
        return totalSize;
    }

    /**
     * 更新视频名字
     **/
    public void updateVideoName(int boniu_video_id, String boniu_video_name) {
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        // update
        ContentValues values = new ContentValues();
        values.put("boniu_video_name", boniu_video_name);
        int updateCount = database.update("boniu_video", values,
                "boniu_video_id=" + boniu_video_id, null);
        RingLog.e("updateCount = " + updateCount);
        database.close();
    }

    /**
     * 更新视频所属文件夹
     **/
    public void updateVideoFolder(int boniu_video_id, int boniu_video_folder_id) {
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        // update
        ContentValues values = new ContentValues();
        values.put("boniu_video_folder_id", boniu_video_folder_id);
        int updateCount = database.update("boniu_video", values,
                "boniu_video_id=" + boniu_video_id, null);
        RingLog.e("updateCount = " + updateCount);
        database.close();
    }

    /**
     * 删除一条视频
     **/
    public void deleteById(int boniu_video_id) {
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        // delete
        int deleteCount = database.delete("boniu_video", "boniu_video_id=?",
                new String[]{boniu_video_id + ""});
        RingLog.e("deleteCount = " + deleteCount);
        database.close();
    }

    /**
     * 根据文件夹ID删除多条视频
     **/
    public void deleteByFolderId(int boniu_video_folder_id) {
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        // delete
        int deleteCount = database.delete("boniu_video", "boniu_video_folder_id=?",
                new String[]{boniu_video_folder_id + ""});
        RingLog.e("deleteCount = " + deleteCount);
        database.close();
    }
}
