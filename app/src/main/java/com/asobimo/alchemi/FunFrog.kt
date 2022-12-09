package com.asobimo.alchemi

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import com.asobimo.alchemi.databinding.FragmentFunFrogBinding
import kotlin.random.Random

class FunFrog : Fragment() {
    private lateinit var binding: FragmentFunFrogBinding

    private lateinit var random: Random

    private lateinit var iv_card1:ImageView
    private lateinit var iv_card2:ImageView

    private lateinit var tv_player1:TextView
    private lateinit var tv_player2:TextView

    private lateinit var deal_btn:Button

    var player1_value = 0
    var player2_value = 0

    var cartinciArray = arrayOf(R.drawable.p_i_c_1,
        R.drawable.p_i_c_2,
        R.drawable.p_i_c_3,
        R.drawable.p_i_c_4,
        R.drawable.p_i_c_5
        ,R.drawable.p_i_c_6)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentFunFrogBinding.inflate(inflater, container, false)

        random = Random

        iv_card1 = binding.ivCard1
        iv_card2 = binding.ivCard2

        tv_player1 = binding.tvCard1
        tv_player2 = binding.tvCard2

        deal_btn = binding.bDeal

        deal_btn.setOnClickListener {
            val size = cartinciArray.size
            val card1: Int = random.nextInt(size)
            val card2 = random.nextInt(size)

            newCartinka_image(iv_card1,card1 )
            newCartinka_image(iv_card2,card2 )

            counterPlayer(card1, card2)
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
            }
        })

        return binding.root
    }

    private fun newCartinka_image(cartinca:ImageView,random_value:Int){
        cartinca.setImageResource(cartinciArray[random_value])
    }

    private fun counterPlayer(card1:Int, card2:Int){
        if (card1>card2){
            player1_value++
            tv_player1.text = "Player 1: $player1_value"
        }else{
            player2_value++
            tv_player2.text = "Player 2: $player2_value"
        }
    }
}