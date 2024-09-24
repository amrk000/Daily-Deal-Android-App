package com.amrk000.dailydeal.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.animation.DecelerateInterpolator
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.amrk000.dailydeal.BuildConfig
import com.amrk000.dailydeal.R
import com.amrk000.dailydeal.databinding.ActivityAboutBinding


class About : AppCompatActivity() {
    private lateinit var binding: ActivityAboutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //view binding init
        binding = ActivityAboutBinding.inflate(layoutInflater)
        setContentView(binding.getRoot())

        enableEdgeToEdge()

        ViewCompat.setOnApplyWindowInsetsListener(binding.getRoot()) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, 0, systemBars.right, 0)
            insets
        }

        binding.aboutCreditCard.setVisibility(View.INVISIBLE)

        binding.aboutAppVersion.setText("Version " + BuildConfig.VERSION_NAME)

        binding.moreapps.setOnClickListener { v ->
            val profilelink = Intent(Intent.ACTION_VIEW)
            profilelink.setData(Uri.parse("https://play.google.com/store/apps/dev?id=5289896800613171020"))
            startActivity(profilelink)
        }

        binding.aboutAppRepoButton.setOnClickListener { v ->
            val repoLink = Intent(Intent.ACTION_VIEW)
            repoLink.setData(Uri.parse("https://github.com/amrk000/Daily-Deal-Android-App"))
            startActivity(repoLink)
        }

        binding.aboutCreditCard.setVisibility(View.VISIBLE)
        binding.aboutCreditCard.y = binding.aboutCreditCard.y + 1000

        binding.aboutCreditCard.animate()
            .translationY(binding.aboutCreditCard.y - 1000f)
            .setInterpolator(DecelerateInterpolator())
            .setStartDelay(500)
            .setDuration(1000)
            .start()

    }
}