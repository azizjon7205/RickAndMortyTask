package com.example.rickandmortytask.ui.details

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.rickandmortytask.databinding.ActivityDetailsBinding
import com.example.rickandmortytask.model.Results
import com.example.rickandmortytask.network.ApiClient
import com.example.rickandmortytask.network.service.ApiService
import com.example.rickandmortytask.repository.MainRepository
import com.example.rickandmortytask.repository.factory.DetailsViewModelFactory
import com.example.rickandmortytask.repository.factory.MainViewModelFactory
import com.example.rickandmortytask.utils.UiStateObject

class DetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailsBinding
    private lateinit var viewModel: DetailsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN or WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN)
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        clearLightStatusBar()

        setupViewModel()
        setupObservers()
        setUpUI()
    }

    private fun setUpUI(){
        viewModel.getSingleCharacter(intent.getIntExtra("id", 0))

        binding.ivBack.setOnClickListener {
            finish()
        }
    }

    @SuppressLint("ResourceType")
    private fun loadData(results: Results){
        Glide.with(this)
            .load(results.image).error(Color.LTGRAY).placeholder(Color.LTGRAY).into(binding.ivProfile)

        binding.tvName.text = results.name
        binding.tvStatus.text = results.status
        binding.tvGender.text = results.gender
        binding.tvSpecies.text = results.species
        binding.tvOriginLocation.text = results.origin!!.name
        binding.tvLocation.text = results.location!!.name

    }

    private fun setupObservers(){
        lifecycleScope.launchWhenCreated {
            viewModel.detailState.collect{
                when(it){
                    is UiStateObject.LOADING -> {
                        // show progress
                        binding.flProgress.visibility = View.VISIBLE
                    }
                    is UiStateObject.SUCCESS -> {
                        binding.flProgress.visibility = View.GONE
                        loadData(it.data)
                        Log.d("Tag", it.data.toString())
                    }
                    is UiStateObject.ERROR -> {
                        Log.d("Tag", it.message)
                    }
                    else -> Unit
                }
            }
        }
    }

    private fun setupViewModel(){
        viewModel = ViewModelProvider(
            this,
            DetailsViewModelFactory(MainRepository(ApiClient.createService(ApiService::class.java)))
        )[DetailsViewModel::class.java]
    }

    fun clearLightStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            window.statusBarColor = Color.TRANSPARENT
        }

    }
}