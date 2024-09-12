package com.example.eulerity_assessment.presentation

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import coil.compose.AsyncImage
import coil.imageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.example.eulerity_assessment.R
import com.example.eulerity_assessment.domain.model.Pet
import com.example.eulerity_assessment.ui.theme.Eulerity_AssessmentTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

class PetDetailsComposeActivity : ComponentActivity() {

    //Launch permissions for saving image
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                Toast.makeText(this, "Permission Granted: You can save the image", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Permission is required to save images", Toast.LENGTH_SHORT).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Eulerity_AssessmentTheme {
                //Retrieve serialized Pet object from intent
                val pet = remember { intent.getSerializableExtra("pet") as? Pet }
                Log.d("PetDetails", "Pet: $pet")
                PetDetailsScreen(pet,this)
            }
        }
    }

    fun storagePermission(): Boolean {
        return if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            false
        }
        else {
            true
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PetDetailsScreen(pet: Pet?, activity: PetDetailsComposeActivity) {
    val context = LocalContext.current

    Box(modifier = Modifier.fillMaxSize()) {

        Image(
            modifier = Modifier.fillMaxSize(),
            painter = painterResource(id = R.drawable.background3),
            contentDescription = null,
            contentScale = ContentScale.Crop
        )

        Scaffold(
            topBar = {
                TopAppBar(
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = colorResource(R.color.colorPrimary),
                        titleContentColor = colorResource(R.color.colorOnPrimary),
                    ),
                    title = { Text("Compose Screen") }
                )
            },
            containerColor = Color.Transparent
        ) { innerPadding ->
            Column(modifier = Modifier.padding(innerPadding).padding(16.dp)) {
                pet?.let {
                    val imageUrl = it.url
                    AsyncImage(
                        model = it.url,
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(0.5f),
                            contentScale = ContentScale.Crop
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.75f)
                            .clip(RoundedCornerShape(15.dp))
                            .background(Color.White)
                            .align(Alignment.CenterHorizontally)
                            .padding(10.dp)

                    ) {
                        Column {
                            Text(
                                text = it.title,
                                style = MaterialTheme.typography.headlineMedium,
                                modifier = Modifier.padding(top = 4.dp).align(Alignment.CenterHorizontally)
                            )
                            Text(
                                text = it.description,
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.padding(top = 4.dp).align(Alignment.CenterHorizontally),
                            )
                        }
                    }

                    Button(
                        modifier = Modifier.padding(top = 8.dp).align(Alignment.CenterHorizontally),
                        onClick = {
                        if (activity.storagePermission()) {
                            saveImage(context, imageUrl)
                        }
                    }) {
                        Text("Save image")
                    }

                }
            }
        }
    }
}

private suspend fun loadBitmap(context: Context, imageUrl: String): Bitmap? {
    val loader = context.imageLoader
    val request = ImageRequest.Builder(context)
        .data(imageUrl)
        .allowHardware(false)
        .build()
    return when (val result = loader.execute(request)) {
        is SuccessResult -> (result.drawable as? android.graphics.drawable.BitmapDrawable)?.bitmap
        else -> null
    }
}

private fun saveImage(context: Context, imageUrl: String) {

    CoroutineScope(Dispatchers.IO).launch {
        val bitmap = loadBitmap(context, imageUrl)
        if (bitmap != null) {
            val directory = File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                "PetImages"
            )
            if (!directory.exists()) {
                directory.mkdirs()
            }
            val file = File(directory, "pet_image_${System.currentTimeMillis()}.jpg")
            FileOutputStream(file).use { outputStream ->
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            }
            withContext(Dispatchers.Main) {
                Toast.makeText(context, "Image saved to: ${file.absolutePath}", Toast.LENGTH_LONG).show()
            }
        } else {
            withContext(Dispatchers.Main) {
                Toast.makeText(context, "Failed to save image", Toast.LENGTH_SHORT).show()
            }
        }
    }
}