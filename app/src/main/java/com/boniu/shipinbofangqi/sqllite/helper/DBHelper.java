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
        //当数据库版本需要升级修改时，增加版本号。
        super(context, "boniu_videoplayer.db", null, 2);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table boniu_video(boniu_video_id integer primary key autoincrement, " +
                "boniu_video_name varchar,boniu_video_coverimg varchar,boniu_video_formatmemory varchar," +
                "boniu_video_createtime varchar,boniu_video_memory double,boniu_video_url varchar," +
                "boniu_video_folder_id integer,boniu_video_length varchar,boniu_video_account varchar)");
        db.execSQL("create table boniu_folder(boniu_folder_id integer primary key autoincrement, " +
                "boniu_folder_name varchar, boniu_folder_formatmemory varchar, boniu_folder_memory double," +
                "boniu_folder_createtime varchar,boniu_folder_isdefault integer,boniu_folder_account varchar)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion == oldVersion) {
            return;
        }
        //当新版本高于老版本时，在这里更新表 。如果第3次需要修改数据库时，记得删除上次的修改语句。
        db.execSQL("ALTER TABLE boniu_video ADD boniu_video_account varchar");
        db.execSQL("ALTER TABLE boniu_folder ADD boniu_folder_account varchar");
    }
}
