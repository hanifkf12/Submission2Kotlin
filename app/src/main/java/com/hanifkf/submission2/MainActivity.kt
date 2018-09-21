package com.hanifkf.submission2

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    val manager = supportFragmentManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        addFragment(PrevFragment())

        val bottomNav:BottomNavigationView = findViewById(R.id.bottom_nav)

        bottomNav.setOnNavigationItemSelectedListener(BottomNavigationView.OnNavigationItemSelectedListener {
            when (it.itemId){
                R.id.menu_prev ->{
                    addFragment(PrevFragment())
                    return@OnNavigationItemSelectedListener true
                }
                R.id.menu_next ->{
                    addFragment(NextFragment())
                    return@OnNavigationItemSelectedListener true
                }
            }
            false
        })
    }

    fun addFragment(fragment :Fragment){
        val transaction = manager.beginTransaction()
        transaction.replace(R.id.next_prev_frame, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}
