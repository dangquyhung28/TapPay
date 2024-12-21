package com.example.tappay.View.Activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.tappay.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBackRegister.setOnClickListener {
            finish()
        }
        binding.btnRegisterRegister.setOnClickListener {
            val username = binding.etUsername.text.toString().trim()
            val email = binding.etEmailRegister.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()
            val confirmPassword = binding.etConfirmPassword.text.toString().trim()
            if (check(username, email, password, confirmPassword)) {
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                    if(task.isSuccessful){
                        val user = FirebaseAuth.getInstance().currentUser
                        user?.sendEmailVerification()?.addOnCompleteListener{ emailTask ->
                            if(emailTask.isSuccessful){
                                saveUserToDatabase(user.uid, username, email, password)

                                Toast.makeText(
                                    this,
                                    "Đăng ký thành công! Vui lòng xác nhận email của bạn.",
                                    Toast.LENGTH_SHORT
                                ).show()

                                val intent = Intent(this, LoginActivity::class.java)
                                startActivity(intent)
                                finish()
                            }else {
                                Toast.makeText(
                                    this,
                                    "Gửi email xác thực thất bại: ${emailTask.exception?.message}",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }else {
                        Toast.makeText(this, "Đăng ký thất bại: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                        Log.d("Đăng ký thất bại", task.exception?.message.toString())
                    }
                }
            }

        }
    }

    private fun saveUserToDatabase(uid: String, username: String, email: String, password: String) {
        val database = FirebaseDatabase.getInstance()
        val userRef = database.getReference("users").child(uid)

        val user = mapOf(
            "username" to username,
            "email" to email,
            "password" to password,
            "verified" to false
        )

        userRef.setValue(user).addOnSuccessListener {
            Log.d("Database", "Người dùng đã được lưu vào Firebase Realtime Database")
        }.addOnFailureListener { e ->
            Log.e("Database", "Lỗi khi lưu người dùng: ${e.message}")
        }

    }

    private fun check(username: String, email: String, password: String, confirmPassword: String): Boolean {
        if (username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show()
            return false
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Email không hợp lệ!", Toast.LENGTH_SHORT).show()
            return false
        }

        if (password != confirmPassword) {
            Toast.makeText(this, "Mật khẩu không khớp!", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }





}
