package com.example.module_14

import android.app.Application
import com.jakewharton.threetenabp.AndroidThreeTen

class SkillboxAplication: Application() {
    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this)
    }
}