package com.ticonsys.hadiths.ui.fragments.hadith

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.text.HtmlCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import com.ticonsys.hadiths.R
import com.ticonsys.hadiths.data.db.entities.Hadith
import com.ticonsys.hadiths.databinding.FragmentHadithDetailBinding
import com.ticonsys.hadiths.ui.activities.main.MainViewModel
import com.ticonsys.hadiths.ui.fragments.base.BaseFragment
import com.ticonsys.hadiths.utils.Status
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HadithDetailFragment : BaseFragment<FragmentHadithDetailBinding, MainViewModel>(
    R.layout.fragment_hadith_detail
) {
    override val viewModel by activityViewModels<MainViewModel>()

    private val args by navArgs<HadithDetailFragmentArgs>()

    private val bookName by lazy {
        args.argBookName
    }

    private val hadithNumber by lazy {
        args.argHadithNumber
    }

    @Inject
    lateinit var deviceLanguage: String

    override fun initializeViewBinding(view: View) = FragmentHadithDetailBinding.bind(view)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeObservers()
    }

    private fun subscribeObservers() {
        viewModel.getHadithDetail(bookName, hadithNumber)
        viewModel.hadith.observe(viewLifecycleOwner) { resource ->
            updateHadithInfo(resource.data)
        }
    }

    private fun updateHadithInfo(data: Hadith?) {
        data?.let { hadith ->
            hadith.details.firstOrNull {
                deviceLanguage == it.lang
            }?.let {
                binding.tvHadithTitle.text = it.chapterTitle
                binding.tvHadithBody.text =
                    HtmlCompat.fromHtml(it.body, HtmlCompat.FROM_HTML_MODE_COMPACT)

                val gradeText = StringBuilder()
                for (grade in it.grades) {
                    gradeText.append(
                        String.format(
                            getString(R.string.txt_grade_format),
                            grade.grade, grade.gradedBy
                        )
                    )
                }
                binding.tvGrade.text = gradeText
            }

        }
    }
}