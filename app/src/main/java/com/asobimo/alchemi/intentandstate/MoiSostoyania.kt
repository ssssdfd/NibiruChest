package com.asobimo.alchemi.intentandstate

sealed class MoiSostoyania{
    class Moi_Apps(val u_r_l:String?):MoiSostoyania()
    object Moi_Load:MoiSostoyania()
    class Moi_Deep(val u_r_l:String?):MoiSostoyania()
    class Moi_File(val u_r_l: String?):MoiSostoyania()
}