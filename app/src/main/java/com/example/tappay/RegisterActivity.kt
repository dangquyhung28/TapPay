package com.example.tappay

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.tappay.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import javax.mail.Message
import javax.mail.Transport
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage
import java.util.Properties

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
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val user = FirebaseAuth.getInstance().currentUser

                            // Tạo mã OTP và gửi qua email
                            val otp = generateOTP()
                            sendOtpEmail(email, otp)

                            // Lưu mã OTP vào Firebase
                            val database = FirebaseDatabase.getInstance()
                            val userRef = database.getReference("users_temp").child(user!!.uid)

                            val tempUser = mapOf(
                                "username" to username,
                                "email" to email,
                                "password" to password,
                                "verified" to false,
                                "otp" to otp
                            )

                            userRef.setValue(tempUser).addOnSuccessListener {
                                // Chuyển tới màn hình nhập mã OTP
                                val intent = Intent(this, CodeVerificationActivity::class.java)
                                intent.putExtra("userId", user.uid)
                                startActivity(intent)
                                finish()
                            }
                        } else {
                            Toast.makeText(this, "Đăng ký thất bại: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                            Log.d("Đăng ký thất bại", task.exception?.message.toString())
                        }
                    }
            }
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

    private fun generateOTP(): String {
        return (100000..999999).random().toString()  // Tạo mã OTP ngẫu nhiên 6 chữ số
    }

    private fun sendOtpEmail(email: String, otp: String) {
        val properties = Properties().apply {
            put("mail.smtp.auth", "true")
            put("mail.smtp.starttls.enable", "true")
            put("mail.smtp.host", "smtp.gmail.com")
            put("mail.smtp.port", "587")
        }

        val session = javax.mail.Session.getInstance(properties, object : javax.mail.Authenticator() {
            override fun getPasswordAuthentication(): javax.mail.PasswordAuthentication {
                return javax.mail.PasswordAuthentication("your_email@gmail.com", "your_app_password")
            }
        })

        try {
            val message = MimeMessage(session).apply {
                setFrom(InternetAddress("your_email@gmail.com"))
                setRecipients(Message.RecipientType.TO, InternetAddress.parse(email))
                subject = "Mã OTP của bạn"
                setText("Mã OTP của bạn là: $otp")
            }

            // Gửi email
            Transport.send(message)
            Log.d("OTP", "Mã OTP đã được gửi đến $email")
        } catch (e: Exception) {
            e.printStackTrace()
            Log.d("OTP", "Lỗi khi gửi OTP: ${e.message}")
        }
    }

}
