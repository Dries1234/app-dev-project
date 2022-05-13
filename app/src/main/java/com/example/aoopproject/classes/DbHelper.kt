package com.example.aoopproject.classes

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DbHelper(context: Context, factory: SQLiteDatabase.CursorFactory?) : SQLiteOpenHelper(context, DB_NAME,factory, DB_VERSION) {
    companion object{
        private const val DB_NAME = "favourites.db"
        private const val DB_VERSION = 1

        const val TABLE_NAME = "favourites"

        const val COL_ID = "id"
        const val COL_BARCODE = "barcode"
        const val COL_NAME = "name"

    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("""
           CREATE TABLE $TABLE_NAME(
            $COL_ID INT PRIMARY KEY,
            $COL_BARCODE TEXT ,
            $COL_NAME TEXT
           ) 
        """)
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        db?.execSQL("""
           DROP TABLE IF EXISTS $TABLE_NAME 
        """)
    }

    fun addFavourite(barcode: String, name: String){
        val values = ContentValues()
        values.put(COL_BARCODE,barcode)
        values.put(COL_NAME,name)
        val db = this.writableDatabase
        db.insert(TABLE_NAME,null,values)
    }

    fun deleteFavourite(barcode: String){
        val db = this.writableDatabase
        db.delete(TABLE_NAME, "$COL_BARCODE IS ?", arrayOf(barcode))
    }

    fun getFavourite(barcode : String): Cursor {
        val db = this.readableDatabase
        return db.rawQuery("SELECT * FROM $TABLE_NAME WHERE $COL_BARCODE = '$barcode'", null)
    }
}