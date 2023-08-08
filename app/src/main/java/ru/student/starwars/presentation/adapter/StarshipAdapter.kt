package ru.student.starwars.presentation.adapter
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ListAdapter
import ru.student.starwars.R
import ru.student.starwars.databinding.StarshipItemBinding
import ru.student.starwars.domain.entity.Starship

class StarshipAdapter: ListAdapter<Starship, StarshipViewHolder>(StarshipItemDiffCallback()) {
    var onItemClickListener: ((Starship) -> Unit)? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StarshipViewHolder {
        val binding = StarshipItemBinding.inflate(
            /* inflater = */ LayoutInflater.from(parent.context),
            /* parent = */ parent,
            /* attachToParent = */ false
        )
        return StarshipViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StarshipViewHolder, position: Int) {
        val item = getItem(position)
        holder.binding.titleTv.text = item.name
        holder.binding.modelTitleTv.text = item.model
        holder.binding.passengersCountTv.text = item.passengers
        holder.binding.manufacturerTv.text = item.manufacturer
        holder.binding.favoriteBtn.setOnClickListener {
            onItemClickListener?.invoke(item)
        }
        holder.binding.favoriteBtn.background = ContextCompat
            .getDrawable(
                holder.itemView.context,
                if(item.isFavorite) R.drawable.ic_star_set else R.drawable.ic_star
            )
    }
}