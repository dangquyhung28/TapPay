package com.example.tappay

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.tappay.View.Fragment.AccountFragment
import com.example.tappay.View.Fragment.HomeFragment
import com.example.tappay.View.Fragment.NotificationFragment
import com.example.tappay.View.Fragment.TopUpFragment
import com.example.tappay.View.Fragment.TransferFragment
import com.example.tappay.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        replaceFragment(HomeFragment())

        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            when(item.itemId){
                R.id.nav_home -> replaceFragment(HomeFragment())
                R.id.nav_transfer -> replaceFragment(TransferFragment())
                R.id.nav_scanQr -> replaceFragment(TopUpFragment())
                R.id.nav_notification -> replaceFragment(NotificationFragment())
                R.id.nav_account -> replaceFragment(AccountFragment())

            }
            true

        }


    }
    private fun replaceFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, fragment)
        transaction.commit()
    }
}