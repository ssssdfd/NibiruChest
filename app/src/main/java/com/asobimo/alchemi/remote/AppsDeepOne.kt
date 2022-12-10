package com.asobimo.alchemi.remote

import android.content.Context
import com.appsflyer.AppsFlyerConversionListener
import com.appsflyer.AppsFlyerLib
import com.facebook.applinks.AppLinkData
import com.google.android.gms.ads.identifier.AdvertisingIdClient
import com.onesignal.OneSignal
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class AppsDeepOne {
    private val myScope = CoroutineScope(Dispatchers.IO)
    suspend fun vernytAppsFlyer(cantext: Context):MutableMap<String, Any>?= suspendCoroutine {
        var kluch = "QqGQfa4XGkGmmiMDUEgLHE"
        AppsFlyerLib.getInstance().init(kluch, object : AppsFlyerConversionListener {
            override fun onConversionDataFail(p0: String?) {
                val value = null
                it.resume(value)
            }
            override fun onConversionDataSuccess(p0: MutableMap<String, Any>?) {
                it.resume(p0)
            }

            override fun onAttributionFailure(something: String?) {}
            override fun onAppOpenAttribution(something: MutableMap<String, String>?) {}
        }, cantext)
        AppsFlyerLib.getInstance().start(cantext)
    }

    suspend fun vernytDeepLink(cantext: Context):String = suspendCoroutine {
        AppLinkData.fetchDeferredAppLinkData(cantext){ result->
             it.resume(result?.targetUri.toString())
        }
    }

    fun otpravitOtchet(deepL: String = "null", appF: MutableMap<String, Any>? = null) {
        val campaign ="campaign"
        val nul = "null"
        myScope.launch {
            when {
                appF?.get(campaign).toString() == nul && deepL == nul -> {
                    OneSignal.sendTag("key2", "organic")
                }
                appF?.get(campaign).toString() != nul -> {
                    OneSignal.sendTag(
                        "key2", appF?.get(campaign).toString().substringBefore("_")
                    )
                }
                deepL != nul -> {
                    val oldValue = "myapp://"
                    OneSignal.sendTag("key2", deepL.replace(oldValue, "").substringBefore("/"))
                }
            }
        }

    }

    suspend fun vernytG00gle(cantext: Context):String = suspendCoroutine {
        val zxcvn = AdvertisingIdClient.getAdvertisingIdInfo(cantext)
        val res = zxcvn.id
        it.resume(res.toString())
    }

    fun vkcluchitOdinSignal(cantext: Context){
        OneSignal.initWithContext(cantext)
        val cifri = "2b327a18-607f-4ecb-9830-559bca0d08c0"
        OneSignal.setAppId(cifri)
        myScope.launch {
            OneSignal.setExternalUserId(vernytG00gle(cantext))
        }
    }

}