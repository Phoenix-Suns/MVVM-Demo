package com.example.nghiamvvm.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.nghiamvvm.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun fetchBasic(view: View) {
        startActivity(Intent(this@MainActivity, FetchListBasicActivity::class.java))
    }

    fun fetchByDagger(view: View) {
        startActivity(Intent(this@MainActivity, FetchListByDaggerActivity::class.java))
    }

    fun fetchByMVVM(view: View) {
        startActivity(Intent(this@MainActivity, FetchListByMVVMActivity::class.java))

    }
}
