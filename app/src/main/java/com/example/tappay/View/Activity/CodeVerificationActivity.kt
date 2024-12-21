package com.example.tappay.View.Activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.tappay.databinding.ActivityCodeVerificationBinding
import com.google.firebase.database.FirebaseDatabase

class CodeVerificationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCodeVerificationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCodeVerificationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val userId = intent.getStringExtra("userId")

        binding.btnRegisterEmailCode.setOnClickListener {
            val otpInput = binding.etEmailCode.text.toString().trim()
            if (otpInput.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập mã OTP!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val database = FirebaseDatabase.getInstance()
            val userRef = database.getReference("users_temp").child(userId.toString())

            userRef.get().addOnSuccessListener { snapshot ->
                val otpStored = snapshot.child("otp").value.toString()

                if (otpInput == otpStored) {
                    userRef.child("verified").setValue(true)
                    val intent = Intent(this, CreatePinActivity::class.java)
                    intent.putExtra("userId", userId)  // Chuyển userId qua activity mới
                    startActivity(intent)
                    finish()  // Đóng activity hiện tại
                } else {
                    Toast.makeText(this, "Mã OTP không chính xác!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
