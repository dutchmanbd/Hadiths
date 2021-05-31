package com.ticonsys.hadiths.ui.fragments.chapters

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.ticonsys.hadiths.R
import com.ticonsys.hadiths.databinding.FragmentChapterBinding
import com.ticonsys.hadiths.ui.activities.main.MainViewModel
import com.ticonsys.hadiths.ui.adapters.ChapterAdapter
import com.ticonsys.hadiths.ui.fragments.base.BaseFragment
import com.ticonsys.hadiths.utils.Status
import com.zxdmjr.material_utils.extensions.toast
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ChapterFragment : BaseFragment<FragmentChapterBinding, MainViewModel>(
    R.layout.fragment_chapter
) {
    @Inject
    lateinit var chapterAdapter: ChapterAdapter

    override val viewModel by activityViewModels<MainViewModel>()

    private val args by navArgs<ChapterFragmentArgs>()

    private val bookName by lazy {
        args.argBookName
    }

    override fun initializeViewBinding(view: View) = FragmentChapterBinding.bind(view)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        subscribeObservers()
    }

    private fun setupRecyclerView() {
        binding.rvChapters.adapter = chapterAdapter
        chapterAdapter.setOnItemClickListener { _, item ->
            navigateToHadithFragment(item.bookNumber)
        }
    }

    private fun navigateToHadithFragment(bookNumber: String) {
        val hadithAction = ChapterFragmentDirections.actionChapterFragmentToHadithFragment(
            bookName, bookNumber
        )
        findNavController().navigate(hadithAction)
    }

    private fun subscribeObservers() {
        viewModel.fetchChapters(bookName)
        viewModel.chapters.observe(viewLifecycleOwner) { resource ->
            when (resource.status) {
                Status.LOADING -> {
                    binding.piChapter.show()
                }
                Status.SUCCESS, Status.ERROR -> {
                    binding.piChapter.hide()
                    chapterAdapter.differ.submitList(resource.data)
                }

            }

        }
    }
}