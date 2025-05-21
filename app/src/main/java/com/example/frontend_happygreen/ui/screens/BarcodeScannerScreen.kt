package com.example.frontend_happygreen.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.frontend_happygreen.ui.components.BarcodeCameraPreview
import com.example.frontend_happygreen.data.model.ProdottoFake
import com.example.frontend_happygreen.data.model.prodottiFinti

@Composable
fun BarcodeScannerScreen() {
    var scannedBarcode by remember { mutableStateOf<String?>(null) }
    var prodotto by remember { mutableStateOf<ProdottoFake?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Fotocamera con scanner
        BarcodeCameraPreview(
            onBarcodeScanned = { code ->
                scannedBarcode = code
                prodotto = prodottiFinti.firstOrNull { it.barcode == code }
            },
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (prodotto != null) {
            Text("Prodotto: ${prodotto!!.nome}", style = MaterialTheme.typography.titleLarge)
            Text("Materiali: ${prodotto!!.materiali}")
            Text("Eco-rating: ${"★".repeat(prodotto!!.ecoRating)}")
            Text("Descrizione: ${prodotto!!.descrizione}")
            Spacer(modifier = Modifier.height(12.dp))
            Text("Alternative sostenibili:", style = MaterialTheme.typography.titleMedium)
            prodotto!!.alternative.forEach {
                Text("- $it")
            }
        } else if (scannedBarcode != null) {
            Text("Prodotto non trovato nel database.")
        }
    }
}
