package ru.student.starwars.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.launch
import ru.student.starwars.databinding.FragmentFavoriteBinding
import ru.student.starwars.presentation.adapter.PeopleAdapter
import ru.student.starwars.presentation.adapter.StarshipAdapter
import javax.inject.Inject

class FavoriteFragment : Fragment() {
    private var _binding: FragmentFavoriteBinding? = null
    private val binding: FragmentFavoriteBinding
        get() = _binding ?: throw IllegalStateException("Binding is null")
    private val component by lazy {
        (requireActivity().application as StarApplication).component
    }
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[FavoriteViewModel::class.java]
    }
    private lateinit var favoritePeopleAdapter: PeopleAdapter
    private lateinit var favoriteStarshipAdapter: StarshipAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        component.inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        binding.tvPeopleTitle.setOnClickListener {
            binding.recyclerView.adapter = favoritePeopleAdapter
            viewModel.getFavoritePeople()
        }
        binding.tvStarShipsTitle.setOnClickListener {
            binding.recyclerView.adapter = favoriteStarshipAdapter
            viewModel.getFavoriteStarships()
        }
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.screenStateFlow.collect {
                    when (it) {
                        is MainScreenState.Initial -> {
                        }
                        is MainScreenState.Loading -> {
                            binding.progressBar.visibility = View.VISIBLE
                        }
                        is MainScreenState.People -> {
                            binding.progressBar.visibility = View.GONE
                            favoritePeopleAdapter.submitList(it.people)
                        }
                        is MainScreenState.Starships -> {
                            binding.progressBar.visibility = View.GONE
                            favoriteStarshipAdapter.submitList(it.starships)
                        }
                    }
                }
            }
        }
    }

    private fun setupRecyclerView() {
        favoritePeopleAdapter = PeopleAdapter()
        favoriteStarshipAdapter = StarshipAdapter()
        with(binding.recyclerView) {
            this.layoutManager = LinearLayoutManager(this@FavoriteFragment.context)
            adapter = favoritePeopleAdapter
        }
        favoritePeopleAdapter.onItemClickListener = {
            viewModel.changeHumanFavorite(it)
        }
        favoriteStarshipAdapter.onItemClickListener = {
            viewModel.changeStarshipFavorite(it)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}