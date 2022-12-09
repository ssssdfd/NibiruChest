package com.asobimo.alchemi.sourcedata

interface FileUtils {
    fun getFromFile():String
    fun insertToFile(something:String)
    fun isExist():Boolean
}