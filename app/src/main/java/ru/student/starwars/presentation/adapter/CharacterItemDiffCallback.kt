package ru.student.starwars.presentation.adapter

import androidx.recyclerview.widget.DiffUtil
import ru.student.starwars.domain.entity.Character

class CharacterItemDiffCallback : DiffUtil.ItemCallback<Character>() {
    override fun areItemsTheSame(oldItem: Character, newItem: Character): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Character, newItem: Character): Boolean {
        return oldItem == newItem
    }

}
