import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.frontend_happygreen.viewmodel.PostViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import android.widget.Toast
import java.io.File
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.ui.graphics.Color

@Composable
fun NewPostScreen(
    gruppoId: Int,
    token: String,
    navController: NavController,
    postViewModel: PostViewModel = viewModel()
) {

    val context = LocalContext.current

    var descrizione by remember { mutableStateOf("") }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var isLoading by remember { mutableStateOf(false) }

    val imagePicker = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        selectedImageUri = uri
    }

    // Debug: Log del token e gruppoId
    LaunchedEffect(token, gruppoId) {
        Log.d("NewPostScreen", "Token: $token")
        Log.d("NewPostScreen", "GruppoId: $gruppoId")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.LightGray)
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Nuovo Post", style = MaterialTheme.typography.titleLarge, color = Color.Black)

        OutlinedTextField(
            value = descrizione,
            onValueChange = { descrizione = it },
            label = { Text("Descrizione") },
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading
        )

        Button(
            onClick = { imagePicker.launch("image/*") },
            enabled = !isLoading
        ) {
            Text("Seleziona immagine (opzionale)")
        }

        selectedImageUri?.let {
            Image(
                painter = rememberAsyncImagePainter(it),
                contentDescription = "Anteprima immagine",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
            )
        }

        Button(
            onClick = {
                if (descrizione.isBlank()) {
                    Toast.makeText(context, "Inserisci una descrizione", Toast.LENGTH_SHORT).show()
                    return@Button
                }

                isLoading = true
                Log.d("NewPostScreen", "Iniziando creazione post...")

                val imageFile = selectedImageUri?.let { uri ->
                    try {
                        File(uri.path!!)
                    } catch (e: Exception) {
                        Log.e("NewPostScreen", "Errore nel processare l'immagine: ${e.message}")
                        null
                    }
                }

                postViewModel.creaPost(
                    gruppoId = gruppoId,
                    descrizione = descrizione,
                    riconoscimento = "Oggetto finto", // da MLKit
                    latitudine = 0.0, // da GPS
                    longitudine = 0.0,
                    token = token,
                    imageFile = imageFile,
                    onSuccess = {
                        Log.d("NewPostScreen", "Post creato con successo!")
                        isLoading = false
                        Toast.makeText(context, "Post creato con successo!", Toast.LENGTH_SHORT).show()
                        navController.popBackStack()
                    },
                    onError = { errorMessage ->
                        Log.e("NewPostScreen", "Errore nella creazione del post: $errorMessage")
                        isLoading = false
                        Toast.makeText(context, "Errore: $errorMessage", Toast.LENGTH_LONG).show()
                    }
                )
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(16.dp),
                    color = MaterialTheme.colorScheme.onPrimary
                )
                Spacer(modifier = Modifier.width(8.dp))
            }
            Text(if (isLoading) "Pubblicando..." else "Pubblica")
        }
    }
}