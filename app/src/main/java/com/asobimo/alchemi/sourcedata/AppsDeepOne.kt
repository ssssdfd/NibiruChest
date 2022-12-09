package com.asobimo.alchemi.sourcedata

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
    suspend fun vernytAppsFlyer(context: Context, app_id:String):MutableMap<String, Any>?= suspendCoroutine {
        var kluch = "QqGQfa4XGkGmmiMDUEgLHE"
        AppsFlyerLib.getInstance().init(kluch, object : AppsFlyerConversionListener {
            override fun onConversionDataSuccess(p0: MutableMap<String, Any>?) {
                val mockAppsData: MutableMap<String, Any> = mutableMapOf()
                mockAppsData["af_status"] = "Non-organic"
                mockAppsData["media_source"] = "testSource"
                mockAppsData["campaign"] = "test1_test2_test3_test4_test5"
                mockAppsData["adset"] = "testAdset"
                mockAppsData["adset_id"] = "testAdsetId"
                mockAppsData["campaign_id"] = "testCampaignId"
                mockAppsData["orig_cost"] = "1.22"
                mockAppsData["af_site_id"] = "testSiteID"
                mockAppsData["adgroup"] = "testAdgroup"
                it.resume(p0)
            }
            override fun onConversionDataFail(p0: String?) {
                it.resume(null)
            }
            override fun onAppOpenAttribution(p0: MutableMap<String, String>?) {}
            override fun onAttributionFailure(p0: String?) {}
        }, context)
        AppsFlyerLib.getInstance().start(context)
    }

    suspend fun vernytDeepLink(context: Context):String = suspendCoroutine {
        AppLinkData.fetchDeferredAppLinkData(context){result->
              it.resume(result?.targetUri.toString())
        }
    }

    fun otpravitOtchet(deepL: String = "null", appF: MutableMap<String, Any>? = null) {
        myScope.launch {
            when {
                appF?.get("campaign").toString() == "null" && deepL == "null" -> {
                    OneSignal.sendTag("key2", "organic")
                }
                deepL != "null" -> {
                    val oldValue = "myapp://"
                    OneSignal.sendTag("key2", deepL.replace(oldValue, "").substringBefore("/"))
                }
                appF?.get("campaign").toString() != "null" -> {
                    OneSignal.sendTag(
                        "key2", appF?.get("campaign").toString().substringBefore("_")
                    )
                }
            }
        }

    }

    suspend fun vernytG00gle(context: Context):String = suspendCoroutine {
        val advertisingIdInfo = AdvertisingIdClient.getAdvertisingIdInfo(context)
        it.resume(advertisingIdInfo.id.toString())
    }

    fun vkcluchitOdinSignal(context: Context){
        OneSignal.initWithContext(context)
        OneSignal.setAppId("2b327a18-607f-4ecb-9830-559bca0d08c0")
        myScope.launch {
            OneSignal.setExternalUserId(vernytG00gle(context))
        }
    }

}