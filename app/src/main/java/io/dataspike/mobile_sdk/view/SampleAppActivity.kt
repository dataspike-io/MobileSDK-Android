package io.dataspike.mobile_sdk.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import io.dataspike.mobile_sdk.R
import io.dataspike.mobile_sdk.view.fragments.HomeFragment

internal class SampleAppActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportFragmentManager.beginTransaction().add(R.id.container, HomeFragment()).commit()
    }
}