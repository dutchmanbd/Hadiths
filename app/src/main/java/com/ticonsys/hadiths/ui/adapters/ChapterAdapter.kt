package com.ticonsys.hadiths.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.ticonsys.baseadapter.BaseAdapter
import com.ticonsys.hadiths.R
import com.ticonsys.hadiths.data.db.entities.Chapter
import com.ticonsys.hadiths.databinding.SimpleChapterItemBinding
import javax.inject.Inject

class ChapterAdapter @Inject constructor(
    private val deviceLanguage: String
) : BaseAdapter<Chapter, SimpleChapterItemBinding>() {
    override fun initializeDiffItemCallback() = object : DiffUtil.ItemCallback<Chapter>() {
        override fun areItemsTheSame(oldItem: Chapter, newItem: Chapter) =
            oldItem.bookNumber == newItem.bookNumber

        override fun areContentsTheSame(oldItem: Chapter, newItem: Chapter) = oldItem == newItem

    }

    override fun initializeViewBinding(
        layoutInflater: LayoutInflater,
        parent: ViewGroup
    ) = SimpleChapterItemBinding.inflate(layoutInflater, parent, false)

    override fun onBindViewHolder(holder: BaseViewHolder<SimpleChapterItemBinding>, position: Int) {
        val chapter = differ.currentList[position]
        holder.binding.apply {
            chapter.titles.firstOrNull {
                deviceLanguage == it.lang
            }?.let {
                tvHadithName.text = it.name
                tvNumberOfHadith.text = String.format(
                    root.context.getString(R.string.txt_number_of_hadith),
                    chapter.numberOfHadith
                )
            }
            root.setOnClickListener { view ->
                listener?.let { click ->
                    click(view, chapter)
                }
            }
        }
    }
}