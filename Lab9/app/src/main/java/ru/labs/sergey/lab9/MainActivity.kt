package ru.labs.sergey.lab9

import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.pm.PackageManager
import android.database.sqlite.SQLiteDatabase
import android.net.Uri
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*


class MainActivity : AppCompatActivity() {
    var myDb : SQLiteDatabase? = null

    @SuppressLint("SetTextI18n", "ObsoleteSdkInt")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.READ_CONTACTS), 100)
        }

        setContentView(R.layout.activity_main)
        val helper = DbHelper(this)
        myDb = helper.writableDatabase
        ins.setOnClickListener{ run { insert(myDb) } }
        contactsbtn.setOnClickListener{run {
            val cursor = contentResolver.query(
                    ContactsContract.Contacts.CONTENT_URI,
                    null,
                    null,
                    null,
                    null
            )

            contacts.text = ""

            for (i in 0 until cursor!!.count){
                cursor.moveToPosition(i)
                val name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                val id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID))
                if(cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0){
                    val numbers = contentResolver.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            arrayOf(id),
                            null
                    )
                    for ( j in 0 until numbers!!.count){
                        numbers.moveToPosition(j)
                        val number = numbers.getString(numbers.getColumnIndex(
                                ContactsContract.CommonDataKinds.Phone.NUMBER
                        ))
                        contacts.text = contacts.text.toString() + "Name : " + name + "\n" + "Phone : " + number + "\n"
                    }
                }
            }
        }}

        update.setOnClickListener{run {
            text.text = ""
            val cursor = contentResolver.query(
                    Uri.parse("content://ru.labs.sergey.lab9.provider/table1").buildUpon().build(),
                    null,
                    null,
                    null,
                    null
            )
            for (i in 0 until cursor!!.count){
                cursor.moveToPosition(i)
                text.text = text.text.toString() + cursor.getString(cursor.getColumnIndex(DbContract.col1)) + "\n"
            }

            cursor.close()
        }}
    }

    fun insert(db: SQLiteDatabase?){
        val values = ContentValues()
        values.put(DbContract.col1, Date().time.toString())
        db?.insert(DbContract.table_name,null,values)
    }
}
