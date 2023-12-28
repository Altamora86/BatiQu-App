package com.latihan.capstoneproject.ui.register

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.latihan.capstoneproject.databinding.ActivityRegisterBinding
import com.latihan.capstoneproject.ui.login.LoginActivity

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        supportActionBar?.hide()

        binding.btnCreateAcc.setOnClickListener{
            val username = binding.edtUsername.text.toString()
            val email = binding.edtEmail.text.toString()
            val password = binding.edtPassword.text.toString()

            if(username.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()){
                binding.progressBar.visibility = View.VISIBLE
                firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener{
                    binding.progressBar.visibility = View.GONE
                    if(it.isSuccessful){
                        val intent = Intent(this, LoginActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                        startActivity(intent)
                        finish()
                    }
                    else{
                        Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }else{
                Toast.makeText(this, "Empty fields is not allowed", Toast.LENGTH_SHORT)
                    .show()
            }
        }
        playAnimation()
        binding.btnBackToLogin.setOnClickListener {
            val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }
    }
    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.imageLogo, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val btnbacktologin = ObjectAnimator.ofFloat(binding.btnBackToLogin, View.ALPHA, 1f).setDuration(500)
        val imagelogo = ObjectAnimator.ofFloat(binding.imageLogo, View.ALPHA, 1f).setDuration(500)
        val tvReg = ObjectAnimator.ofFloat(binding.tvReg, View.ALPHA, 1f).setDuration(500)
        val edtEmail = ObjectAnimator.ofFloat(binding.edtEmail, View.ALPHA, 1f).setDuration(500)
        val edtUsername = ObjectAnimator.ofFloat(binding.edtUsername, View.ALPHA, 1f).setDuration(500)
        val edtPassword = ObjectAnimator.ofFloat(binding.edtPassword, View.ALPHA, 1f).setDuration(500)
        val btnCreateAcc = ObjectAnimator.ofFloat(binding.btnCreateAcc, View.ALPHA, 1f).setDuration(500)
        val together = AnimatorSet().apply {
            playTogether(edtEmail, edtUsername, edtPassword)
        }

        AnimatorSet().apply {
            playSequentially(btnbacktologin, imagelogo, tvReg, btnCreateAcc, together)
            startDelay = 500
        }.start()
    }
}