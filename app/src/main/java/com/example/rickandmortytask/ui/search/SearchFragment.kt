package com.example.rickandmortytask.ui.search

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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
import com.example.rickandmortytask.databinding.FragmentSearchBinding
import com.example.rickandmortytask.network.ApiClient
import com.example.rickandmortytask.network.service.ApiService
import com.example.rickandmortytask.repository.MainRepository
import com.example.rickandmortytask.repository.factory.MainViewModelFactory
import com.example.rickandmortytask.repository.factory.SearchViewModelFactory
import com.example.rickandmortytask.ui.details.DetailsActivity
import com.example.rickandmortytask.ui.main.MainViewModel
import com.example.rickandmortytask.utils.UiStateObject

class SearchFragment : Fragment() {

    private lateinit var viewModel: SearchViewModel
    private var _binding: FragmentSearchBinding? = null

    private lateinit var adapter: ResultAdapter
    private lateinit var rvSearch: RecyclerView
    private var page = 1

    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun initViews(){
        setupViewModel()
        setupObservers()
        rvSearch = binding.rvSearch
        rvSearch.layoutManager = GridLayoutManager(requireContext(), 1)
        refreshAdapter()

        binding.ivBack.setOnClickListener {
            requireActivity().onBackPressed()
        }

        binding.ivClear.setOnClickListener {
            binding.edtSearch.text.clear()
        }

        binding.edtSearch.requestFocus()
        showKeyboard()
        binding.edtSearch.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s!!.isEmpty()){
                    binding.llSearchResults.visibility = View.GONE
                    binding.tvSearchNote.visibility = View.VISIBLE
                } else{
                    viewModel.getFilteredCharacters(s.toString())
                    binding.llSearchResults.visibility = View.VISIBLE
                    binding.tvSearchNote.visibility = View.GONE
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setupObservers(){
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.searchState.collect{
                when(it){
                    is UiStateObject.LOADING -> {
                        // show progress
//                        binding.flProgress.visibility = View.VISIBLE
                    }
                    is UiStateObject.SUCCESS -> {
                        adapter.list.clear()
                        adapter.list.addAll(it.data.results)
                        adapter
                        adapter.notifyDataSetChanged()
                        Log.d("Tag@", it.data.toString())
//                        binding.flProgress.visibility = View.GONE
                    }
                    is UiStateObject.ERROR -> {
                        binding.tvSearchNote.text = getString(R.string.str_search_not_found_message)
                        binding.llSearchResults.visibility = View.GONE
                        binding.tvSearchNote.visibility = View.VISIBLE
                        Log.d("Tag@", it.message)
                    }
                    else -> Unit
                }
            }
        }
    }

    private fun setupViewModel(){
        viewModel = ViewModelProvider(
            this,
            SearchViewModelFactory(MainRepository(ApiClient.createService(ApiService::class.java)))
        )[SearchViewModel::class.java]
    }

    private fun refreshAdapter(){
        adapter = ResultAdapter {
            startDetailsActivity(it)
        }
        rvSearch.adapter = adapter
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