package com.example.aoopproject.classes

import android.content.ContentProvider
import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.content.UriMatcher
import android.database.Cursor
import android.database.SQLException
import android.net.Uri
import android.util.Log
import androidx.annotation.Nullable
import java.util.logging.Logger

class FavouritesContentProvider : ContentProvider() {

    companion object {
        val ALL_FAVOURITES = 100;
        val FAVOURITE_ID = 101;
        fun buildUriMatcher(): UriMatcher {
            val uriMatcher = UriMatcher(UriMatcher.NO_MATCH)
            uriMatcher.addURI(Contract.AUTHORITY, "favourite", ALL_FAVOURITES)
            uriMatcher.addURI(
                Contract.AUTHORITY,
                "favourite" + "/#",
                FAVOURITE_ID
            )

            return uriMatcher
        }
    }

    lateinit var mDbHelper: DbHelper;
    override fun onCreate(): Boolean {
        mDbHelper = DbHelper(context!!, null)
        return true
    }

    override fun query(
        uri: Uri,
        p1: Array<out String>?,
        id: String?,
        p3: Array<out String>?,
        p4: String?
    ): Cursor? {
        val uriMatcher = buildUriMatcher()
        val match = uriMatcher.match(uri)
        var result: Cursor? = null
        val db = mDbHelper.writableDatabase
        when(match){
            ALL_FAVOURITES -> {
               result = db.rawQuery("SELECT * FROM ${DbHelper.TABLE_NAME}", null);
            }
            FAVOURITE_ID -> {
                result =  db.rawQuery("SELECT * FROM ${DbHelper.TABLE_NAME} WHERE ${DbHelper.COL_BARCODE} = '$id'", null)
            }
        }
        return result
    }
    override fun getType(p0: Uri): String? {
        return "vnd.android.cursor.dir/favourites"
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        val uriMatcher = buildUriMatcher()
        val match = uriMatcher.match(uri)
        var result: Cursor? = null
        val db = mDbHelper.writableDatabase
        var rowId = db.insert(DbHelper.TABLE_NAME,null,values)
        if(rowId > 0) {
            val _uri = ContentUris.withAppendedId(Contract.BASE_CONTENT_URI, rowId)
            context?.contentResolver?.notifyChange(_uri, null)
            return _uri
    }
        throw SQLException("Failed to add record into $uri")

    }

    override fun delete(uri: Uri, selection: String?, p2: Array<out String>?): Int {
        var count = 0;
        val db = mDbHelper.writableDatabase
        val id = uri.pathSegments[1]
        count = db.delete(DbHelper.TABLE_NAME, "${DbHelper.COL_BARCODE} IS ?", arrayOf(id))
        context?.contentResolver?.notifyChange(uri, null)
        return count
    }

    override fun update(p0: Uri, p1: ContentValues?, p2: String?, p3: Array<out String>?): Int {
        TODO("I don't use this")
    }
}