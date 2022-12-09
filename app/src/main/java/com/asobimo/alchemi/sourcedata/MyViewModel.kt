package com.asobimo.alchemi.sourcedata

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.appsflyer.AppsFlyerLib
import com.asobimo.alchemi.R
import com.asobimo.alchemi.intentandstate.MyIntent
import com.asobimo.alchemi.intentandstate.MyState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
import okhttp3.HttpUrl
import java.util.*
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class MyViewModel(application: Application): AndroidViewModel(application) {
    val fileManipulation = FileUtils(application)
    val myIntent = Channel<MyIntent>(Channel.UNLIMITED)
    private val appsDeepOne = AppsDeepOne()
    private val _state = MutableStateFlow<MyState>(MyState.MyLoad)
    val state: StateFlow<MyState>
        get() = _state


    init { handleIntent() }

    private fun handleIntent(){
        viewModelScope.launch {
            myIntent.consumeAsFlow().collect{state->
                when(state){
                    is MyIntent.GetUrl -> vernytIzFile()
                }
            }
        }
    }

    private fun vernytIzFile(){
        viewModelScope.launch {
            val url = fileManipulation.getFile()
            _state.value = MyState.MyFile(url)
        }

    }

     fun polozhitVFile(something:String){
        fileManipulation.insert(something)
    }

     fun bild_leenk(kontekst: Context){
        appsDeepOne.vkcluchitOdinSignal(kontekst)
         viewModelScope.launch(Dispatchers.IO) {
             val google = appsDeepOne.vernytG00gle(kontekst)
             val deepLink = appsDeepOne.vernytDeepLink(kontekst)
             if (deepLink!="null"){
                 appsDeepOne.otpravitOtchet(deepL = deepLink)
                 val deep = create_yrl(kontekst,deepLink, null,google)
                 _state.value = MyState.MyDeep(deep)
             }else{
                 val appF = appsDeepOne.vernytAppsFlyer(kontekst,"229")
                 appsDeepOne.otpravitOtchet(appF = appF)
                 val app = create_yrl(kontekst,"null",appF,google)
                 _state.value = MyState.MyApps(app)
             }
         }
    }
    private fun create_yrl(kontekst: Context, deepLink: String, data: MutableMap<String, Any>?, gId: String ):String{
        val x = HttpUrl.Builder()
        x.apply {
            scheme("https")
            host("catslovefood.today")
            addPathSegment("nibiru.php")
            addQueryParameter(kontekst.resources.getString(R.string.secure_get_parametr), kontekst.resources.getString(
                R.string.secure_key
            ))
            addQueryParameter(kontekst.resources.getString(R.string.dev_tmz_key), TimeZone.getDefault().id)
            addQueryParameter(kontekst.resources.getString(R.string.gadid_key), gId)
            addQueryParameter(kontekst.resources.getString(R.string.deeplink_key), deepLink)
            addQueryParameter(kontekst.resources.getString(R.string.source_key), if (deepLink != "null") "deeplink" else data?.get("media_source").toString())
            if (deepLink != "null") { addQueryParameter(kontekst.resources.getString(R.string.adset_id_key), "null") }
            else { addQueryParameter(kontekst.resources.getString(R.string.af_id_key), AppsFlyerLib.getInstance().getAppsFlyerUID(kontekst)) }
            addQueryParameter(kontekst.resources.getString(R.string.adset_id_key), data?.get("adset_id").toString())
            addQueryParameter(kontekst.resources.getString(R.string.campaign_id_key), data?.get("campaign_id").toString())
            addQueryParameter(kontekst.resources.getString(R.string.app_campaign_key), data?.get("campaign").toString())
            addQueryParameter(kontekst.resources.getString(R.string.adset_key), data?.get("adset").toString())
            addQueryParameter(kontekst.resources.getString(R.string.adgroup_key), data?.get("adgroup").toString())
            addQueryParameter(kontekst.resources.getString(R.string.orig_cost_key), data?.get("orig_cost").toString())
            addQueryParameter(kontekst.resources.getString(R.string.af_siteid_key), data?.get("af_siteid").toString())
        }.toString()
        return x.toString()
    }
}


