package com.example.tappay.View.Activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.tappay.databinding.ActivityPinVerificationBinding

class PinVerificationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPinVerificationBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPinVerificationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBackPinVerification.setOnClickListener {
            finish()
        }


    }
}