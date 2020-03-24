package com.duyin.quickscan.api;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.duyin.quickscan.baen.ScanResult;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;

/**
 * 作者：zhangzhongping on 17/2/17 16:45
 * 邮箱：android_dy@163.com
 */
public class QuickScanService implements QuickScanListener {

    public static QuickScanService quickScanService;

    public synchronized static QuickScanService getQuickScanService() {
        if (quickScanService == null) {
            quickScanService = new QuickScanService();
        }
        return quickScanService;
    }

    @Override
    public Observable<List<ScanResult>> getAllResult(final Context context, final String end) {
        return Observable.create(new Observable.OnSubscribe<List<ScanResult>>() {
            @Override
            public void call(Subscriber<? super List<ScanResult>> subscriber) {
                subscriber.onNext(queryFiles(context, end));
            }
        });
    }

    private List<ScanResult> queryFiles(Context context, String end) {
        String[] projection = new String[]{MediaStore.Files.FileColumns._ID,
                MediaStore.Files.FileColumns.DATA,
                MediaStore.Files.FileColumns.SIZE,
                MediaStore.Files.FileColumns.DATE_ADDED,
                MediaStore.Files.FileColumns.DATE_MODIFIED
        };
        Cursor cursor = context.getContentResolver().query(
                Uri.parse("content://media/external/file"),
                projection,
                MediaStore.Files.FileColumns.DATA + " like ?",
                new String[]{"%"+end},
                null);

        List<ScanResult> list = new ArrayList<>();
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int idIndex = cursor.getColumnIndex(MediaStore.Files.FileColumns._ID);
                int sizeIndex = cursor.getColumnIndex(MediaStore.Files.FileColumns.SIZE);
                int dateAddedIndex = cursor.getColumnIndex(MediaStore.Files.FileColumns.DATE_ADDED);
                int dateModifiedIndex = cursor.getColumnIndex(MediaStore.Files.FileColumns.DATE_MODIFIED);
                int dataIndex = cursor.getColumnIndex(MediaStore.Files.FileColumns.DATA);
                do {
                    String _ID = cursor.getString(idIndex);
                    String SIZE = cursor.getString(sizeIndex);
                    String DATE_ADDED = cursor.getString(dateAddedIndex);
                    String DATE_MODIFIED = cursor.getString(dateModifiedIndex);
                    String DATA = cursor.getString(dataIndex);
                    int dot = DATA.lastIndexOf("/");
                    ScanResult scanResult = new ScanResult(DATA.substring(dot + 1), DATA, _ID, SIZE, DATE_ADDED, DATE_MODIFIED);
                    list.add(scanResult);/*
                    if (name.lastIndexOf(".") > 0)
                        name = name.substring(0, name.lastIndexOf("."));
                    if (!name.startsWith(".")) {
                        ScanResult books = new ScanResult();
                        books.setName(name);
                        books.setPath(path);
                        list.add(books);
                    }*/
                } while (cursor.moveToNext());
            }
        }
        cursor.close();
        return list;
    }
}
