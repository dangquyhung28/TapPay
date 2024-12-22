package com.example.tappay.View.Activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.tappay.MainActivity
import com.example.tappay.databinding.ActivityCreatePinBinding
import com.google.firebase.database.FirebaseDatabase

class CreatePinActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCreatePinBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreatePinBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val userId = intent.getStringExtra("userId")

        binding.btnComfirmCreatePin.setOnClickListener {
            val pin = binding.etCreatePin.text.toString().trim()

            if (pin.isEmpty() || pin.length != 4) {
                Toast.makeText(this, "Mã PIN phải có 4 chữ số!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Lưu mã PIN vào Firebase
            val database = FirebaseDatabase.getInstance()
            val userRef = database.getReference("users").child(userId.toString())

            userRef.child("pin").setValue(pin).addOnSuccessListener {
                Toast.makeText(this, "Tạo mã PIN thành công!", Toast.LENGTH_SHORT).show()

                // Chuyển sang màn hình chính của ứng dụng
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()  // Đóng màn hình tạo mã PIN
            }.addOnFailureListener {
                Toast.makeText(this, "Lỗi khi tạo mã PIN. Vui lòng thử lại.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
