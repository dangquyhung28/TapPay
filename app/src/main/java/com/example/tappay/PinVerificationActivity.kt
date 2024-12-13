package com.example.tappay

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
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