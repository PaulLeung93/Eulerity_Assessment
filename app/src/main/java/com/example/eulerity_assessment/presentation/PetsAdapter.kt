package com.example.eulerity_assessment.presentation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.eulerity_assessment.R
import com.example.eulerity_assessment.domain.model.Pet

class PetsAdapter(
    private val onPetClick: (Pet) -> Unit
) : RecyclerView.Adapter<PetsAdapter.PetViewHolder>() {

    private var petsList: List<Pet> = emptyList()

    inner class PetViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val image: ImageView = itemView.findViewById(R.id.image)
        private val title: TextView = itemView.findViewById(R.id.title)
        private val description: TextView = itemView.findViewById(R.id.description)

        fun bind(pet: Pet) {
            title.text = pet.title
            description.text = pet.description

            //Loading image with Coil
            image.load(pet.url) {
                crossfade(true)
                placeholder(R.drawable.ic_pet_foreground)
            }
            //Set click listener
            itemView.setOnClickListener { onPetClick(pet) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PetViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_pet, parent, false)
        return PetViewHolder(view)
    }

    override fun onBindViewHolder(holder: PetViewHolder, position: Int) {
        holder.bind(petsList[position])
    }

    override fun getItemCount(): Int = petsList.size

    //Updates the list of pets and notifies adapter the list has changed
    fun submitList(list: List<Pet>) {
        petsList = list
        notifyDataSetChanged()
    }
}
