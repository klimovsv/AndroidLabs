package ru.labs.sergey.lab9

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.net.Uri
import android.text.TextUtils
import android.util.Log
import java.util.*

class TestProvider:ContentProvider(){
    override fun insert(uri: Uri?, values: ContentValues?): Uri {
//        val values = ContentValues()
//        values.put(DbContract.col1, Date().time.toString())
//        helper.writableDatabase?.insert(DbContract.table_name,null,values)
        TODO()
    }

    override fun update(uri: Uri?, values: ContentValues?, selection: String?, selectionArgs: Array<out String>?): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun delete(uri: Uri?, selection: String?, selectionArgs: Array<out String>?): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getType(uri: Uri?): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    companion object {
        const val multipleRow = 1
        const val singleRow = 2
        val matcher: UriMatcher = UriMatcher(UriMatcher.NO_MATCH)

        init {
            matcher.addURI("ru.labs.sergey.lab9.provider","table1", multipleRow)
            matcher.addURI("ru.labs.sergey.lab9.provider","table1/#", singleRow)
        }
    }

    var helper : DbHelper? = null

    override fun onCreate(): Boolean {
        helper = DbHelper(context)
        return true
    }

    override fun query(uri: Uri?,
                       projection: Array<out String>?,
                       selection: String?,
                       selectionArgs: Array<out String>?,
                       sortOrder: String?): Cursor {
        Log.e("TAG","query")
        var tsortOrder = sortOrder
        var tselection = selection
        when(matcher.match(uri)){
            1 -> {
                if (TextUtils.isEmpty(sortOrder)) tsortOrder = "_ID asc"
            }
            2 -> {
                tselection += "_ID = " + uri?.lastPathSegment
            }
        }
        var myDb = helper?.readableDatabase
        return myDb?.query(
                DbContract.table_name,
                null,
                null,
                null,
                null,
                null,
                DbContract.col1
        )!!
    }
}