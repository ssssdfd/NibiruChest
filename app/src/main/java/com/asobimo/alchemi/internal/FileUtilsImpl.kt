package com.asobimo.alchemi.internal

import android.content.Context
import java.io.File

class FileUtilsImpl(c: Context): FileUtils {
    private var file = File(c.filesDir, "file")

   override fun getFromFile():String = file.inputStream().bufferedReader().useLines {  it.fold(""){ x, y-> "$x$y" } }

    override fun insertToFile(something: String) {
        file.outputStream().write(something.toByteArray())
    }

    override fun isExist(): Boolean {
        return file.exists()
    }
}