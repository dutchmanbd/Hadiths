package com.ticonsys.hadiths.ui.fragments.books

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.ticonsys.hadiths.R
import com.ticonsys.hadiths.databinding.FragmentBooksBinding
import com.ticonsys.hadiths.ui.activities.main.MainViewModel
import com.ticonsys.hadiths.ui.adapters.BookAdapter
import com.ticonsys.hadiths.ui.fragments.base.BaseFragment
import com.ticonsys.hadiths.utils.Resource
import com.ticonsys.hadiths.utils.Status
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
        setupRecyclerView()
        subscribeObservers()
    }

    private fun setupRecyclerView() {
        binding.rvBooks.adapter = bookAdapter
        bookAdapter.setOnItemClickListener { _, item ->
            navigateToChapterFragment(item.name)
        }
    }

    private fun navigateToChapterFragment(name: String) {
        val chapterAction = BooksFragmentDirections.actionBooksFragmentToChapterFragment(name)
        findNavController().navigate(chapterAction)
    }

    private fun subscribeObservers() {
        viewModel.fetchBooks()

        viewModel.books.observe(viewLifecycleOwner) { resource ->
            when (resource.status) {
                Status.LOADING -> {
                    binding.piBook.show()
                }
                Status.SUCCESS, Status.ERROR -> {
                    binding.piBook.hide()
                    bookAdapter.differ.submitList(resource.data)
                }

            }
        }
    }
}