package com.example.tappay

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.tappay.databinding.ActivityForgotPasswordBinding
import com.google.firebase.auth.FirebaseAuth

class ForgotPasswordActivity : AppCompatActivity() {
    private lateinit var binding : ActivityForgotPasswordBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSendResetEmail.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()

            if (email.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập email!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            FirebaseAuth.getInstance().sendPasswordResetEmail(email).addOnCompleteListener { task ->
                if(task.isSuccessful){
                    Toast.makeText(
                        this,
                        "Liên kết đặt lại mật khẩu đã được gửi đến email của bạn.",
                        Toast.LENGTH_LONG
                    ).show()
                }else {
                    Toast.makeText(
                        this,
                        "Lỗi: ${task.exception?.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

        }
        binding.btnBackForgotPassword.setOnClickListener {
            finish()
        }


    }
}