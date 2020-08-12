package com.machioni.ciceronedaggerdemo.presentation.common

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.machioni.ciceronedaggerdemo.R
import com.machioni.ciceronedaggerdemo.presentation.common.NavigationKeys.CONTAINER_FRAGMENT
import com.machioni.ciceronedaggerdemo.presentation.scene.dashboard.scene1.container.ContainerFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .add(R.id.container, ContainerFragment(), CONTAINER_FRAGMENT)
                .commit()
        }
    }

    override fun onBackPressed() {
        val currentFragment = (supportFragmentManager.findFragmentByTag(CONTAINER_FRAGMENT) as ContainerFragment)
        if (!currentFragment.onBackPressed())
            finish()
    }
}