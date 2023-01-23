package com.example.bmitry2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView

class weight_history : AppCompatActivity() {

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weight_history)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        val dbHelper = DBHelper(this)
        val db = dbHelper.readableDatabase

        val projection = arrayOf(
            DBHelper.FeedReaderContract.FeedEntry.COLUMN_NAME_WEIGHT,
            DBHelper.FeedReaderContract.FeedEntry.COLUMN_NAME_DATA
        )

        val cursor = db.query(
            DBHelper.FeedReaderContract.FeedEntry.TABLE_NAME,
            projection,
            null,
            null,
            null,
            null,
            null
        )
        val weightList = ArrayList<String>()
        while (cursor.moveToNext()) {
            val weight =
                cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.FeedReaderContract.FeedEntry.COLUMN_NAME_WEIGHT))
            val data =
                cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.FeedReaderContract.FeedEntry.COLUMN_NAME_DATA))
            weightList.add("Weight: $weight , Date: $data")
        }
        cursor.close()

        val listView = findViewById<ListView>(R.id.list_view)
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, weightList)
        listView.adapter = adapter
    }
}
