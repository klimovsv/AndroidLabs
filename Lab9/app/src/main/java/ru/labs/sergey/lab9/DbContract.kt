package ru.labs.sergey.lab9

open class KBaseColumns{
    val _ID = "_id"
}

object DbContract : KBaseColumns(){
    val col1 : String = "col1"
    val table_name : String = "table1"
}