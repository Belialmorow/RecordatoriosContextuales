package com.example.recordatorios_contextuales.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.recordatorios_contextuales.model.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Actividad1Screen() {
    var prioridadSeleccionada by remember { mutableStateOf<Prioridad?>(null) }

    val listaFiltrada = remember(prioridadSeleccionada) {
        RecordatorioConfig.filtrarRecordatorios(
            lista = RecordatorioConfig.recordatoriosSimulados,
            prioridadFiltro = prioridadSeleccionada
        )
    }

    val opcionesFiltro = listOf<Prioridad?>(null, Prioridad.Alta, Prioridad.Media, Prioridad.Baja)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Recordatorios Contextuales") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {
            // Filtros por prioridad (Consigna 3)
            LazyRow(
                modifier = Modifier.padding(vertical = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(opcionesFiltro) { opcion ->
                    FilterChip(
                        selected = prioridadSeleccionada == opcion,
                        onClick = { prioridadSeleccionada = opcion },
                        label = { Text(opcion?.etiqueta ?: "Todas") }
                    )
                }
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                items(listaFiltrada, key = { it.id }) { item ->
                    TarjetaRecordatorioInicial(recordatorio = item)
                }
            }
        }
    }
}

@Composable
fun TarjetaRecordatorioInicial(recordatorio: Recordatorio) {
    val colorCategoria = RecordatorioConfig.coloresPorCategoria[recordatorio.categoria] ?: Color.Gray

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = recordatorio.categoria.nombre,
                    color = Color.White,
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier
                        .background(colorCategoria, RoundedCornerShape(6.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                )
                Text(
                    text = "Prioridad: ${recordatorio.prioridad.etiqueta}",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.SemiBold
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = recordatorio.titulo,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Estado: ${recordatorio.estado.etiqueta}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.secondary
            )
            if (recordatorio.ubicacion != null || recordatorio.aviso != null) {
                Spacer(modifier = Modifier.height(4.dp))
                val contexto = listOfNotNull(
                    recordatorio.ubicacion?.let { "Lugar: $it" },
                    recordatorio.aviso?.let { "Aviso: $it" }
                ).joinToString(" | ")
                Text(
                    text = contexto,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.outline
                )
            }
        }
    }
}

