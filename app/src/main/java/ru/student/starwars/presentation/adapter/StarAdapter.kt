package ru.student.starwars.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.bumptech.glide.Glide
import ru.student.starwars.databinding.SearchItemBinding
import ru.student.starwars.domain.entity.Human

class StarAdapter: ListAdapter<Human, StarViewHolder>(StarItemDiffCallback()) {
    var onItemClickListener: ((Human) -> Unit)? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StarViewHolder {
        val binding = SearchItemBinding.inflate(
            /* inflater = */ LayoutInflater.from(parent.context),
            /* parent = */ parent,
            /* attachToParent = */ false
        )
        return StarViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StarViewHolder, position: Int) {
        val item = getItem(position)
        holder.binding.titleTv.text = item.name
        Glide.with(holder.itemView.context).load(item.imageUrl).into(holder.binding.logoIv)
        holder.binding.root.setOnClickListener {
            onItemClickListener?.invoke(item)
        }
    }
    companion object {
        const val MAX_VH_POOL_SIZE = 20
    }
}