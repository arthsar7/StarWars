package ru.student.starwars.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ListAdapter
import com.bumptech.glide.Glide
import ru.student.starwars.R
import ru.student.starwars.databinding.SearchItemBinding
import ru.student.starwars.domain.entity.Gender
import ru.student.starwars.domain.entity.Human

class PeopleAdapter: ListAdapter<Human, PeopleViewHolder>(StarItemDiffCallback()) {
    var onItemClickListener: ((Human) -> Unit)? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PeopleViewHolder {
        val binding = SearchItemBinding.inflate(
            /* inflater = */ LayoutInflater.from(parent.context),
            /* parent = */ parent,
            /* attachToParent = */ false
        )
        return PeopleViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PeopleViewHolder, position: Int) {
        val item = getItem(position)
        holder.binding.titleTv.text = item.name
        Glide.with(holder.itemView.context).load(item.imageUrl).into(holder.binding.logoIv)
        holder.binding.favoriteBtn.background = ContextCompat
            .getDrawable(
                holder.itemView.context,
                if(item.isFavorite) R.drawable.ic_star_set else R.drawable.ic_star
            )
        holder.binding.favoriteBtn.setOnClickListener {
            onItemClickListener?.invoke(item)
        }

        holder.binding.starshipCountTv.text = item.starshipsCount.toString()

        holder.binding.sexIv.setImageDrawable(
            ContextCompat.getDrawable(
                holder.itemView.context,
                when(item.gender) {
                    Gender.MALE -> R.drawable.ic_male
                    Gender.FEMALE -> R.drawable.ic_female
                    Gender.NEUTRAL -> R.drawable.ic_neutral
                }
            )
        )
    }
    companion object {
        const val MAX_VH_POOL_SIZE = 20
    }
}