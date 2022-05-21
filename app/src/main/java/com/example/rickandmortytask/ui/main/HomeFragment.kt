package com.example.rickandmortytask.ui.main

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.rickandmortytask.R
import com.example.rickandmortytask.adapter.ResultAdapter
import com.example.rickandmortytask.databinding.FragmentHomeBinding
import com.example.rickandmortytask.network.ApiClient
import com.example.rickandmortytask.network.service.ApiService
import com.example.rickandmortytask.repository.MainRepository
import com.example.rickandmortytask.repository.factory.MainViewModelFactory
import com.example.rickandmortytask.ui.details.DetailsActivity
import com.example.rickandmortytask.utils.UiStateObject

class HomeFragment : Fragment() {

    private lateinit var viewModel: MainViewModel
    private var _binding: FragmentHomeBinding? = null
    private lateinit var adapter: ResultAdapter
    private lateinit var rvMain: RecyclerView
    private var page = 1

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvMain = binding.rvMain
        rvMain.layoutManager = GridLayoutManager(requireContext(), 1)
        refreshAdapter()

        setupViewModel()
        viewModel.getCharacters(page)
        setupObservers()

        binding.edtSearch.setOnClickListener {
            findNavController().navigate(R.id.searchFragment)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setupObservers(){
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.mainState.collect{
                when(it){
                    is UiStateObject.LOADING -> {
                        // show progress
//                        binding.flProgress.visibility = View.VISIBLE
                        Log.d("@@@", "loading")
                    }
                    is UiStateObject.SUCCESS -> {
                        adapter.list.addAll(it.data.results)
                        adapter.notifyDataSetChanged()
                        rvMain.addOnScrollListener(object : RecyclerView.OnScrollListener(){
                            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                                if (!rvMain.canScrollVertically(1) && it.data.info!!.next != null){
//                                    binding.flProgress.visibility = View.VISIBLE
                                    viewModel.getCharacters(++page)
                                }
                            }
                        })
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
            MainViewModelFactory(MainRepository(ApiClient.createService(ApiService::class.java)))
        )[MainViewModel::class.java]
    }

    private fun refreshAdapter(){
        adapter = ResultAdapter {
            startDetailsActivity(it)
        }
        rvMain.adapter = adapter
    }

    private fun startDetailsActivity(id: Int){
        val intent = Intent(requireContext(), DetailsActivity::class.java)
        intent.putExtra("id", id)
        startActivity(intent)
    }

    fun showKeyboard() {
        val inputMethodManager: InputMethodManager =
            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        inputMethodManager.showSoftInput(binding.edtSearch, InputMethodManager.SHOW_IMPLICIT);
//        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
    }
}