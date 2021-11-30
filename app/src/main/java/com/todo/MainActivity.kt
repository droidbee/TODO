package com.todo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import com.todo.databinding.ActivityMainBinding
import com.todo.utils.networkhelper.NetworkStatus
import com.todo.utils.networkhelper.NetworkStatusHelper
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding:ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=DataBindingUtil.setContentView(this,R.layout.activity_main)

        //Monitoring Internet Availability
        NetworkStatusHelper(this@MainActivity).observe(this, Observer {
            when(it){
                NetworkStatus.Available-> Snackbar.make(binding.root, R.string.todo_app_online, Snackbar.LENGTH_LONG).show()
                NetworkStatus.Unavailable-> Snackbar.make(binding.root, R.string.no_internet_connection, Snackbar.LENGTH_LONG).show()
            }
        })
    }
}