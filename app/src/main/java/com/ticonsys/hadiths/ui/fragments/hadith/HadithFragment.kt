package com.ticonsys.hadiths.ui.fragments.hadith

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import com.ticonsys.hadiths.R
import com.ticonsys.hadiths.databinding.FragmentHadithBinding
import com.ticonsys.hadiths.ui.activities.main.MainViewModel
import com.ticonsys.hadiths.ui.adapters.HadithAdapter
import com.ticonsys.hadiths.ui.fragments.base.BaseFragment
import com.ticonsys.hadiths.utils.Status
import com.zxdmjr.material_utils.extensions.toast
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HadithFragment : BaseFragment<FragmentHadithBinding, MainViewModel>(
    R.layout.fragment_hadith
) {
    @Inject
    lateinit var hadithAdapter: HadithAdapter

    override val viewModel by activityViewModels<MainViewModel>()

    private val args by navArgs<HadithFragmentArgs>()

    private val bookName by lazy {
        args.argBookName
    }

    private val bookNumber by lazy {
        args.argBookNumber
    }

    override fun initializeViewBinding(view: View) = FragmentHadithBinding.bind(view)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        subscribeObservers()
    }

    private fun setupRecyclerView() {
        binding.rvHadith.adapter = hadithAdapter
        hadithAdapter.setOnItemClickListener { _, item ->

        }
    }

    private fun subscribeObservers() {
        Log.d("HadithFragment", "subscribeObservers: $bookName, $bookNumber")
        viewModel.getHadith(bookName, bookNumber)
        viewModel.hadithList.observe(viewLifecycleOwner) { resource ->
            when (resource.status) {
                Status.LOADING -> {
                    binding.piHadith.show()
                }
                Status.SUCCESS, Status.ERROR -> {
                    binding.piHadith.hide()
                    hadithAdapter.differ.submitList(resource.data)
                }

            }
        }
    }
}