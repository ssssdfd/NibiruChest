package com.asobimo.alchemi.sourcedata

import android.content.Context
import java.io.File

class FileUtils(c: Context) {
    private var _file = File(c.filesDir, "file")
    val file
        get() = _file

    fun getFile():String = _file.inputStream().bufferedReader().useLines {  it.fold(""){x, y-> "$x$y" } }

    fun insert(data:String){
        _file.outputStream().write(data.toByteArray())
    }
}