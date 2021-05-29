package com.ticonsys.hadiths.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.ticonsys.baseadapter.BaseAdapter
import com.ticonsys.hadiths.data.db.entities.Book
import com.ticonsys.hadiths.databinding.SimpleBookItemBinding
import javax.inject.Inject

class BookAdapter @Inject constructor(
    private val deviceLanguage: String
) : BaseAdapter<Book, SimpleBookItemBinding>() {
    override fun initializeDiffItemCallback() = object : DiffUtil.ItemCallback<Book>() {
        override fun areItemsTheSame(oldItem: Book, newItem: Book) =
            oldItem.name == newItem.name

        override fun areContentsTheSame(oldItem: Book, newItem: Book) =
            oldItem == newItem

    }

    override fun initializeViewBinding(
        layoutInflater: LayoutInflater,
        parent: ViewGroup
    ) = SimpleBookItemBinding.inflate(layoutInflater, parent, false)

    override fun onBindViewHolder(holder: BaseViewHolder<SimpleBookItemBinding>, position: Int) {
        val book = differ.currentList[position]
        holder.binding.apply {
            if(deviceLanguage == "ar"){
                tvBookName.text = book.collection.last().title
            } else {
                tvBookName.text = book.collection.first().title
            }
            root.setOnClickListener { view ->
                listener?.let { click ->
                    click(view, book)
                }
            }
        }
    }


}