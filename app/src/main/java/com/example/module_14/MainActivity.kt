package com.example.module_14

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            showMainFragment()}
    }

    private fun showMainFragment(){
        supportFragmentManager.beginTransaction()
            .add(R.id.container, MainFragment())
            .addToBackStack(null)
            .commit()
    }
}