package com.example.bmitry2

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns


class DBHelper(context: Context) : SQLiteOpenHelper(context, "WeightDB", null, 1) {

    object FeedReaderContract {
        // Table contents are grouped together in an anonymous object.
        object FeedEntry : BaseColumns {
            const val TABLE_NAME = "History"
            const val COLUMN_NAME_WEIGHT = "Weight"
            const val COLUMN_NAME_DATA = "Data"
        }
    }

    companion object {
        private const val SQL_CREATE_ENTRIES =
            "CREATE TABLE ${FeedReaderContract.FeedEntry.TABLE_NAME} (" +
                    "${BaseColumns._ID} INTEGER PRIMARY KEY," +
                    "${FeedReaderContract.FeedEntry.COLUMN_NAME_WEIGHT} TEXT," +
                    "${FeedReaderContract.FeedEntry.COLUMN_NAME_DATA} TEXT)"

        private const val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS ${FeedReaderContract.FeedEntry.TABLE_NAME}"
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_ENTRIES)
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        TODO("Not yet implemented")
    }


    fun insertData(weight: String, data: String): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(FeedReaderContract.FeedEntry.COLUMN_NAME_WEIGHT, weight)
            put(FeedReaderContract.FeedEntry.COLUMN_NAME_DATA, data)
        }
        return db.insert(FeedReaderContract.FeedEntry.TABLE_NAME, null, values)
    }



}