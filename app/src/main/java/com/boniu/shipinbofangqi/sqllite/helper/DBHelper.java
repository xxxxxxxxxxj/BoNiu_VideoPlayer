package com.boniu.shipinbofangqi.sqllite.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * <p>
 * Title:DBHelper
 * </p>
 * <p>
 * Description:数据库操作的工具类
 * </p>
 * <p>
 * Company:北京昊唐科技有限公司
 * </p>
 *
 * @author 徐俊
 * @date 2016-8-18 下午3:15:07
 */
public final class DBHelper extends SQLiteOpenHelper {

    public DBHelper(Context context) {
        super(context, "boniu_videoplayer.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table boniu_video(boniu_video_id integer primary key autoincrement, " +
                "boniu_video_name varchar,boniu_video_coverimg varchar,boniu_video_formatmemory varchar," +
                "boniu_video_createtime varchar,boniu_video_memory double,boniu_video_url varchar,boniu_video_folder_id integer,boniu_video_length varchar)");
        db.execSQL("create table boniu_folder(boniu_folder_id integer primary key autoincrement, " +
                "boniu_folder_name varchar, boniu_folder_formatmemory varchar, boniu_folder_memory double,boniu_folder_createtime varchar,boniu_folder_isdefault integer)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
