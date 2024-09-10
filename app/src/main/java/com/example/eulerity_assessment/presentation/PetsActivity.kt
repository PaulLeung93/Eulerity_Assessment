package com.example.eulerity_assessment.presentation

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
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

        viewModel = ViewModelProvider(this).get(PetsViewModel::class.java)
        petsAdapter = PetsAdapter { pet -> onPetClicked(pet) }

        val recyclerView: RecyclerView = findViewById(R.id.petsRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = petsAdapter

        // Observe LiveData and sends list to adapter
        viewModel.pets.observe(this) { petsList ->
            petsAdapter.submitList(petsList)
        }
    }

    // Handle pet click and navigate to Compose screen
    private fun onPetClicked(pet: Pet) {
        val intent = Intent(this, PetDetailsComposeActivity::class.java)
        intent.putExtra("pet", pet) // Send pet data to Compose Activity
        startActivity(intent)
    }
}