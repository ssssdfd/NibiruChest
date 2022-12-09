package com.asobimo.alchemi.intentandstate

sealed class MyState {
    class MyApps(val url:String?):MyState()
    object MyLoad:MyState()
    class MyDeep(val url:String?):MyState()
    class MyFile(val url: String?):MyState()
}