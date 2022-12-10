package com.asobimo.alchemi.internal

interface FileUtils {
    fun getFromFile():String
    fun insertToFile(something:String)
    fun isExist():Boolean
}