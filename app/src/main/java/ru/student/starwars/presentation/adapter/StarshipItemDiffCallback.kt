package ru.student.starwars.presentation.adapter

import androidx.recyclerview.widget.DiffUtil
import ru.student.starwars.domain.entity.Starship

class StarshipItemDiffCallback: DiffUtil.ItemCallback<Starship>() {
    override fun areItemsTheSame(oldItem: Starship, newItem: Starship): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Starship, newItem: Starship): Boolean {
        return oldItem == newItem
    }
}