package com.example.eulerity_assessment.presentation

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.eulerity_assessment.domain.model.Pet
import com.example.eulerity_assessment.ui.theme.Eulerity_AssessmentTheme

class PetDetailsComposeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Eulerity_AssessmentTheme {
                //Retrieve serialized Pet object from intent
                val pet = remember { intent.getSerializableExtra("pet") as? Pet }
                Log.d("PetDetails", "Pet: $pet")
                PetDetailsScreen(pet)
            }
        }
    }
}

@Composable
fun PetDetailsScreen(pet: Pet?) {
    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        Column(modifier = Modifier.padding(16.dp)) {
            pet?.let {
                AsyncImage(
                    model = it.url,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                )
                Text(
                    text = it.title,
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(top = 8.dp)
                )
                Text(
                    text = it.description,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}
