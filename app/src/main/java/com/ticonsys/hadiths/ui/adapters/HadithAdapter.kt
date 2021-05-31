package com.ticonsys.hadiths.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.ticonsys.baseadapter.BaseAdapter
import com.ticonsys.hadiths.R
import com.ticonsys.hadiths.data.db.entities.Hadith
import com.ticonsys.hadiths.databinding.SimpleHadithItemBinding
import javax.inject.Inject

class HadithAdapter @Inject constructor(
    private val deviceLanguage: String
) : BaseAdapter<Hadith, SimpleHadithItemBinding>() {
    override fun initializeDiffItemCallback() = object : DiffUtil.ItemCallback<Hadith>() {
        override fun areItemsTheSame(oldItem: Hadith, newItem: Hadith) =
            oldItem.hadithNumber == newItem.hadithNumber

        override fun areContentsTheSame(oldItem: Hadith, newItem: Hadith) = oldItem == newItem

    }

    override fun initializeViewBinding(
        layoutInflater: LayoutInflater,
        parent: ViewGroup
    ) = SimpleHadithItemBinding.inflate(layoutInflater, parent, false)

    override fun onBindViewHolder(holder: BaseViewHolder<SimpleHadithItemBinding>, position: Int) {
        val hadith = differ.currentList[position]
        holder.binding.apply {
            hadith.details.firstOrNull {
                deviceLanguage == it.lang
            }?.let {
                tvHadithTitle.text = it.chapterTitle
                if (!it.grades.isNullOrEmpty()) {
                    tvGrade.text = String.format(
                        root.context.getString(R.string.txt_grade_format),
                        it.grades.first().grade, it.grades.first().gradedBy
                    )
                }
            }
            root.setOnClickListener { view ->
                listener?.let { click ->
                    click(view, hadith)
                }
            }
        }
    }
}