package ru.student.starwars.presentation

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.SearchView.OnQueryTextListener
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.launch
import ru.student.starwars.R
import ru.student.starwars.databinding.ActivityMainBinding
import ru.student.starwars.domain.StarScreenState.Initial
import ru.student.starwars.domain.StarScreenState.Loading
import ru.student.starwars.domain.StarScreenState.People
import ru.student.starwars.domain.StarScreenState.ShowHuman
import ru.student.starwars.domain.entity.Human
import ru.student.starwars.presentation.adapter.StarAdapter

class MainActivity : AppCompatActivity() {
    private val viewModel by lazy {
        ViewModelProvider(this)[MainViewModel::class.java]
    }
    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    private lateinit var starAdapter: StarAdapter
    private var peopleList: List<Human>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setSearchView()
        setRecyclerView()
        setItemListener()
        collectPeopleFlow()
        viewModel.getFavoritePeople().observe(this) {
            Log.d("Main", it.toString())
        }
    }

    private fun setItemListener() {
        starAdapter.onItemClickListener = {
            viewModel.changeHumanFavorite(it)
//            val message = if (!it.isFavorite) "добавлен в избранные!" else "удален из избранных" TODO НЕ РАБОТАЕТ
//            Toast.makeText(
//                this@MainActivity,
//                "Персонаж ${it.name} $message",
//                Toast.LENGTH_SHORT
//            ).show()
        }
    }

    private fun setSearchView() {
        binding.searchView.setOnQueryTextListener(object : OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                filteredList(newText)
                return false
            }
        })
    }

    private fun collectPeopleFlow() {
        lifecycleScope.launch {
            viewModel.peopleFlow.collect {
                when (it) {
                    is People -> {
                        peopleList = it.people
                        starAdapter.submitList(it.people)
                        binding.progressBar.visibility = View.GONE
                    }

                    is Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                    is Initial -> {
                    }

                    is ShowHuman -> {

                    }
                }
            }
        }
    }

    private fun filteredList(newText: String) {
        if (newText.length < 2) {
            starAdapter.submitList(peopleList)
        }
        val initList = mutableListOf<Human>()
        peopleList?.forEach {
            if (it.name.lowercase().contains(newText.lowercase())) {
                initList.add(it)
            }
        }
        if (initList.isEmpty()) {
            Toast.makeText(this, "Не найдено", Toast.LENGTH_SHORT).show()
        }
        else {
            starAdapter.submitList(initList)
        }

    }

    private fun setRecyclerView() {
        starAdapter = StarAdapter()
        with(binding.recyclerView) {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = starAdapter
            recycledViewPool.setMaxRecycledViews(
                /* viewType = */ R.layout.search_item,
                /* max = */ StarAdapter.MAX_VH_POOL_SIZE
            )
        }
    }

    private fun log(msg: String) {
        Log.d("MainActivity", msg)
    }
}