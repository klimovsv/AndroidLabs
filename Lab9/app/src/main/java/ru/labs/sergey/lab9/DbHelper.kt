package ru.labs.sergey.lab9

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper


class DbHelper(context: Context) : SQLiteOpenHelper(context, dbname, null, version) {
    companion object {
        val dbname : String = "database.db"
        val version : Int = 1
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTable : String = "create table " + DbContract.table_name + "("+
                DbContract._ID + " integer primary key autoincrement, " +
                DbContract.col1 + " text not null);"
        db?.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE " + DbContract.table_name + ";")
        onCreate(db)
    }
}