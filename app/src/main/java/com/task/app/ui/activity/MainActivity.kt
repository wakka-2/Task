package com.task.app.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.task.app.R
import com.task.app.util.base.BaseActivity
import com.task.app.util.base.BaseFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)
        val navController = findNavController(R.id.mainActivityHostFragment)
        NavigationUI.setupWithNavController(toolbar, navController, null)
    }
}