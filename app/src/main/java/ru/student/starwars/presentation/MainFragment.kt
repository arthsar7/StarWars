package ru.student.starwars.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.RadioGroup.OnCheckedChangeListener
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.launch
import ru.student.starwars.R
import ru.student.starwars.databinding.FragmentMainBinding
import ru.student.starwars.domain.entity.Character
import ru.student.starwars.domain.entity.Searchable
import ru.student.starwars.domain.entity.Starship
import ru.student.starwars.presentation.adapter.CharacterAdapter
import ru.student.starwars.presentation.adapter.StarshipAdapter
import javax.inject.Inject


class MainFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val mainViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[MainViewModel::class.java]
    }
    private var _binding: FragmentMainBinding? = null
    private val binding: FragmentMainBinding
        get() = _binding ?: throw IllegalStateException("Binding is null")

    private val component by lazy {
        (requireActivity().application as StarApplication).component
    }

    private lateinit var characterAdapter: CharacterAdapter
    private lateinit var starshipAdapter: StarshipAdapter

    private var searchableList: List<Searchable>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        component.inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setSearchView()
        setRecyclerView()
        setItemListener()
        collectScreenFlow()
        binding.tvCharactersTitle.setOnCheckedChangeListener {
                _, isChecked ->
            if (isChecked) {
                binding.recyclerView.adapter = characterAdapter
                mainViewModel.getCharacters()
            }
        }

        binding.tvStarShipsTitle.setOnCheckedChangeListener {
                _, isChecked ->
            if (isChecked) {
                binding.recyclerView.adapter = starshipAdapter
                mainViewModel.getStarships()
            }
        }
    }

    private fun setItemListener() {
        characterAdapter.onItemClickListener = {
            mainViewModel.changeCharacterFavorite(it)
            val message = if (!it.isFavorite) "добавлен в избранные!" else "удален из избранных"
            Toast.makeText(
                requireContext(),
                "Персонаж ${it.name} $message",
                Toast.LENGTH_SHORT
            ).show()
        }
        starshipAdapter.onItemClickListener = {
            mainViewModel.changeStarshipFavorite(it)
            val message = if (!it.isFavorite) "добавлен в избранные!" else "удален из избранных"
            Toast.makeText(
                requireContext(),
                "Звездолет ${it.name} $message",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun setSearchView() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                filteredList(newText)
                return false
            }
        })
    }

    private fun collectScreenFlow() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                mainViewModel.screenStateFlow.collect {
                    when (it) {
                        is MainScreenState.Characters -> {
                            searchableList = it.characters
                            characterAdapter.submitList(it.characters)
                            binding.progressBar.visibility = View.GONE
                        }

                        is MainScreenState.Loading -> {
                            binding.progressBar.visibility = View.VISIBLE
                        }

                        is MainScreenState.Initial -> {
                        }

                        is MainScreenState.Starships -> {
                            binding.progressBar.visibility = View.GONE
                            starshipAdapter.submitList(it.starships)
                            searchableList = it.starships
                        }
                    }
                }
            }
        }
    }

    private fun filteredList(newText: String) {
        if (newText.length < 2) {
            characterAdapter.submitList(searchableList?.filterIsInstance<Character>())
        }
        val initList = mutableListOf<Searchable>()
        searchableList?.forEach {
            if (it.getSearchableText().lowercase().contains(newText.lowercase())) {
                initList.add(it)
            }
        }
        if (initList.isEmpty()) {
            Toast.makeText(requireContext(), "Не найдено", Toast.LENGTH_SHORT).show()
        } else {
            characterAdapter.submitList(initList.filterIsInstance<Character>())
            starshipAdapter.submitList(initList.filterIsInstance<Starship>())
        }

    }

    private fun setRecyclerView() {
        starshipAdapter = StarshipAdapter()
        characterAdapter = CharacterAdapter()

        with(binding.recyclerView) {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext())
            adapter = characterAdapter
            recycledViewPool.setMaxRecycledViews(
                /* viewType = */ R.layout.search_item,
                /* max = */ CharacterAdapter.MAX_VH_POOL_SIZE
            )
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}