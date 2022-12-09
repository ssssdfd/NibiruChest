package com.asobimo.alchemi

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.asobimo.alchemi.intentandstate.MyIntent
import com.asobimo.alchemi.intentandstate.MyState
import com.asobimo.alchemi.sourcedata.MyViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ZargruzkaFrog : Fragment() {
    private val myViewModel:MyViewModel by viewModels()
    private lateinit var progressBar: ProgressBar
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val fragment = inflater.inflate(R.layout.fragment_zargruzka_frog, container, false)
        progressBar = fragment.findViewById(R.id.progress_bar)
        observeStates()
        start()

        return fragment
    }

    private fun start(){
        lifecycleScope.launch {
            if (myViewModel.fileManipulation.file.exists()){
                Log.d("TAAG","File exist")
                myViewModel.myIntent.send(MyIntent.GetUrl)
            }else{
                Log.d("TAAG","File doesn't exist")
                myViewModel.bild_leenk(requireContext())
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
                        navigateToNextFrog(state.url!!)
                    }
                    is MyState.MyDeep -> {
                        Log.d("TAAG", "DeepLink state:${state.url}")
                        navigateToNextFrog(state.url!!)
                    }
                    is MyState.MyFile -> {
                        Log.d("TAAG", "From file:${state.url}")
                        navigateToNextFrog(state.url!!)
                    }
                }
            }
        }
    }
    private fun navigateToNextFrog(yrl:String){
        parentFragmentManager
            .beginTransaction()
            .replace(R.id.containerForFragments, OfferFrog.newInstance(yrl))
            .commit()
    }
}