package ru.labs.sergey.filemanager.manager

import android.app.ListActivity
import android.content.ComponentCallbacks2
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.io.IOException

class MainActivity : ListActivity() {
    var adapter : ArrayAdapter<String>? = null
    var files : List<File>? = null
    var fileNames : List<String>? = null
    var currentDir : File? = null

    fun updateFiles(){
        files = currentDir!!.listFiles()?.toList()
        if(files == null) fileNames = List(0,{i -> ""})
        else{
            fileNames = files?.map { file: File ->
                var type = "dir"
                if(file.isFile) type = "file"
                "${file.name} (${type})"
            }
        }
    }

    fun updateAdapter(){
        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1,fileNames)
        list.adapter = adapter
    }

    fun update(dir : File){
        currentDir = dir
        directory.text = currentDir!!.absolutePath
        updateFiles()
        updateAdapter()
    }

    fun isExternalStorageWritable(): Boolean {
        if (Environment.MEDIA_MOUNTED == Environment.getExternalStorageState()){
            return true
        }
        return false
    }

    fun goHome(){
        if(isExternalStorageWritable()){
//            update(getAlbumDir())
            update(filesDir)
        }else{
            update(filesDir)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        homebtn.setOnClickListener{
            goHome()
        }

        backbtn.setOnClickListener{
            if(currentDir!!.parentFile != null) update(currentDir!!.parentFile)
            else Toast.makeText(this,"Cannot access directory", Toast.LENGTH_LONG).show()
        }

        filebtn.setOnClickListener{
            try{
                File(currentDir,System.currentTimeMillis().toString() + "_myfile.txt").createNewFile()
            }catch (e:IOException){
                Toast.makeText(this,e.message,Toast.LENGTH_LONG).show()
            }
            update(currentDir!!)
        }

        dirbtn.setOnClickListener{
            try{
                File(currentDir,System.currentTimeMillis().toString() + "_mydirectory").mkdir()
            }catch (e:IOException){
                Toast.makeText(this,e.message,Toast.LENGTH_LONG).show()
            }
            update(currentDir!!)
        }

        goHome()

    }

    override fun onListItemClick(l: ListView?, v: View?, position: Int, id: Long) {
        if(files!![position].isDirectory){
            update(files!![position])
        }else{
            Toast.makeText(this,"This is not a directory",Toast.LENGTH_LONG).show()
        }
    }

    fun getAlbumDir() : File {
        val dir = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).absolutePath)
        if(!dir.mkdirs()){
            Toast.makeText(this,"Directory not created",Toast.LENGTH_LONG).show()
        }
        return dir
    }
}
