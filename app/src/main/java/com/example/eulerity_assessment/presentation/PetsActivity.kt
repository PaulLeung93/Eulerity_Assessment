package com.example.eulerity_assessment.presentation

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.eulerity_assessment.R
import com.example.eulerity_assessment.domain.model.Pet
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PetsActivity : AppCompatActivity() {
    private lateinit var viewModel: PetsViewModel
    private lateinit var petsAdapter: PetsAdapter

    //Define the ActivityResultLauncher for picking Pet images from gallery
    private lateinit var galleryLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pets_list)

        //Defining toolbar view and set the title to display the current UI framework version
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = getString(R.string.activity_screen)

        //Add searchbar on the toolbar, filters Pet list based on user query
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

        //Floating action button to add Pets images to the list
        val addFAB: FloatingActionButton = findViewById(R.id.add_FAB)
        addFAB.setOnClickListener {
            //Launch the gallery to pick an image
            val intent = Intent(Intent.ACTION_PICK).apply {
                type = "image/*"
            }
            galleryLauncher.launch(intent)
        }

        // Initialize ActivityResultLauncher
        galleryLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val imageUri = result.data?.data
                if (imageUri != null) {
                    showAddPetDialog(imageUri)
                }
            }
        }

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


    //Inflate the Options menu to allow users to sort Pets by ascending/descending order
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_sort, menu)
        return true
    }

    //Add menu options
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

    //Handle pet click and navigate to Compose screen
    private fun onPetClicked(pet: Pet) {
        val intent = Intent(this, PetDetailsComposeActivity::class.java)
        //Send pet data to Compose Activity
        intent.putExtra("pet", pet)
        startActivity(intent)
    }

    //
    private fun addToPetList(imageUri: Uri, title: String, description: String) {
        val newPet = Pet(
            title = title,
            description = description,
            url = imageUri.toString(),
            created = System.currentTimeMillis().toString() //TODO:Convert date to proper format
        )
        viewModel.addPet(newPet)
    }

    private fun showAddPetDialog(imageUri: Uri) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_pet, null)
        val titleEditText: EditText = dialogView.findViewById(R.id.titleEditText)
        val descriptionEditText: EditText = dialogView.findViewById(R.id.descriptionEditText)

        AlertDialog.Builder(this)
            .setTitle("Add New Pet")
            .setView(dialogView)
            .setPositiveButton("Add") { dialog, _ ->
                val title = titleEditText.text.toString()
                val description = descriptionEditText.text.toString()
                addToPetList(imageUri, title, description)
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }
            .show()
    }
}