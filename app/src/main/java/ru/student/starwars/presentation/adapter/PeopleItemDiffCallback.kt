package ru.student.starwars.presentation.adapter

import androidx.recyclerview.widget.DiffUtil
import ru.student.starwars.domain.entity.Human

class PeopleItemDiffCallback : DiffUtil.ItemCallback<Human>() {
    override fun areItemsTheSame(oldItem: Human, newItem: Human): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Human, newItem: Human): Boolean {
        return oldItem == newItem
    }

}
