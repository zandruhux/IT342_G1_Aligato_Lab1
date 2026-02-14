package com.example.userregistrationandauthentication.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.userregistrationandauthentication.R
import com.example.userregistrationandauthentication.network.ApiClient
import com.example.userregistrationandauthentication.network.AuthApi
import com.example.userregistrationandauthentication.utils.TokenManager
import kotlinx.coroutines.launch

class ProfileActivity : AppCompatActivity() {

    private lateinit var api: AuthApi

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        TokenManager.init(this)

        if (TokenManager.getToken() == null) {
            redirectToLogin()
            return
        }

        setContentView(R.layout.activity_profile)

        api = ApiClient.retrofit.create(AuthApi::class.java)

        val tvFullName = findViewById<TextView>(R.id.tvFullName)
        val tvEmail = findViewById<TextView>(R.id.tvEmail)
        val btnLogout = findViewById<Button>(R.id.btnLogout)

        lifecycleScope.launch {
            try {
                val response = api.getProfile()

                if (response.isSuccessful) {

                    val user = response.body()
                    tvFullName.text = "${user?.firstName} ${user?.lastName}"
                    tvEmail.text = user?.email

                } else if (response.code() == 401) {
                    TokenManager.clear()
                    redirectToLogin()
                } else {
                    Toast.makeText(this@ProfileActivity, "Failed to load profile", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@ProfileActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }

        btnLogout.setOnClickListener {
            TokenManager.clear()
            redirectToLogin()
        }
    }

    private fun redirectToLogin() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }
}
