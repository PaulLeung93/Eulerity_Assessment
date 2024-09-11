package com.example.eulerity_assessment.presentation

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.eulerity_assessment.R
import com.example.eulerity_assessment.domain.model.Pet
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PetsActivity : AppCompatActivity() {
    private lateinit var viewModel: PetsViewModel
    private lateinit var petsAdapter: PetsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pets_list)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = getString(R.string.activity_screen)

        val searchbarView: SearchView = findViewById(R.id.searchbar)
        searchbarView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    viewModel.filterPets(it)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let {
                    viewModel.filterPets(it)
                }
                return true
            }
        })

        viewModel = ViewModelProvider(this).get(PetsViewModel::class.java)
        petsAdapter = PetsAdapter { pet -> onPetClicked(pet) }

        val recyclerView: RecyclerView = findViewById(R.id.petsRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = petsAdapter

        //Observe LiveData and sends list to adapter
        viewModel.pets.observe(this) { petsList ->
            petsAdapter.submitList(petsList)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_sort, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.sort_ascending -> {
                //Sort Pets in ascending order by title
                viewModel.sortPetsAscending()
                true
            }
            R.id.sort_descending -> {
                //Sort Pets in ascending order by title
                viewModel.sortPetsDescending()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


    // Handle pet click and navigate to Compose screen
    private fun onPetClicked(pet: Pet) {
        val intent = Intent(this, PetDetailsComposeActivity::class.java)
        intent.putExtra("pet", pet) // Send pet data to Compose Activity
        startActivity(intent)
    }
}