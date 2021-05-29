package com.ticonsys.hadiths.ui.fragments.books

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.activityViewModels
import com.ticonsys.hadiths.R
import com.ticonsys.hadiths.databinding.FragmentBooksBinding
import com.ticonsys.hadiths.ui.activities.main.MainViewModel
import com.ticonsys.hadiths.ui.adapters.BookAdapter
import com.ticonsys.hadiths.ui.fragments.base.BaseFragment
import com.ticonsys.hadiths.utils.Resource
import com.zxdmjr.material_utils.extensions.toast
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class BooksFragment : BaseFragment<FragmentBooksBinding, MainViewModel>(
    R.layout.fragment_books
) {

    @Inject
    lateinit var bookAdapter: BookAdapter

    override val viewModel by activityViewModels<MainViewModel>()

    companion object {
        private const val TAG = "BooksFragment"
    }

    override fun initializeViewBinding(view: View) = FragmentBooksBinding.bind(view)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d(TAG, "onViewCreated: ${Locale.getDefault().displayLanguage}")
        Log.d(TAG, "onViewCreated: ${Locale.getDefault().language}")
        setupRecyclerView()
        subscribeObservers()
    }

    private fun setupRecyclerView() {
        binding.rvBooks.adapter = bookAdapter
        bookAdapter.setOnItemClickListener { _, item ->
            fetchChapters(item.name)
        }
    }

    private fun fetchChapters(name: String) {
        viewModel.fetchChapters(name).observe(viewLifecycleOwner){ resource ->
            when(resource){
                is Resource.Error -> {
                    binding.piBook.hide()
                    requireContext().toast(resource.msg)
                }
                is Resource.Loading -> {
                    binding.piBook.show()
                }
                is Resource.Success -> {
                    binding.piBook.hide()
                    Log.d(TAG, "fetchChapters: ${resource.data.size}")
                }
            }
        }
    }

    private fun subscribeObservers() {
        viewModel.fetchBooks().observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Error -> {
                    binding.piBook.hide()
                    requireContext().toast(resource.msg)
                }
                is Resource.Loading -> {
                    binding.piBook.show()
                }
                is Resource.Success -> {
                    binding.piBook.hide()
                    bookAdapter.differ.submitList(resource.data)
                }
            }
        }
    }
}