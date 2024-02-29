package com.empireley.exarecuperacionardm

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.empireley.exarecuperacionardm.data.Asignatura
import com.empireley.exarecuperacionardm.ui.theme.ExaRecuperacionARDMTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ExaRecuperacionARDMTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Aplicacion()
                }
            }
        }
    }
}

@Composable
fun Aplicacion() {
    Column {
        PanelSuperior()
        ListaCard()
        TextoUsuario()
        PanelInferior()
        BotonInferior()
    }
}

@Composable
fun PanelSuperior(modifier: Modifier = Modifier) {
    Row(
        modifier
            .background(Color.LightGray)
            .fillMaxWidth()
    ) {
        Text(
            text = "Bienvenido a la academia de Adrian/RDM",
            modifier.padding(
                top = 20.dp,
                start = 10.dp
            )
        )
    }
}

@Composable
fun Card(asignatura: Asignatura, modifier: Modifier = Modifier) {
    Column(modifier.padding(8.dp).clip(MaterialTheme.shapes.medium)) {
        Text(
            text = "Asig: " + asignatura.nombre,
            modifier
                .background(Color.Yellow)
                .fillMaxWidth()
                .padding(20.dp)
        )
        Text(
            text = "€/hora: " + asignatura.precioHora,
            modifier
                .background(Color.Cyan)
                .fillMaxWidth()
                .padding(20.dp)
        )
        Row(
            modifier
                .background(Color.LightGray)
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Button(onClick = {
                asignatura.horasContratadas += horasEnteras
                ultimaAccion.value =
                    "Se han añadido " + horasEnteras + " horas de " + asignatura.nombre + " a " + asignatura.precioHora + "€"
                listaAsignaturas.put(asignatura.nombre, asignatura)
                CalcularTotalHoras()
                CalcularTotalPrecio()
            }) {
                Text(text = "+")
            }
            Button(onClick = {
                asignatura.horasContratadas -= horasEnteras
                ultimaAccion.value =
                    "Se han eliminado " + horasEnteras + " horas de " + asignatura.nombre + " a " + asignatura.precioHora + "€"
                if (asignatura.horasContratadas <= 0) {
                    asignatura.horasContratadas = 0
                    listaAsignaturas.remove(asignatura.nombre)
                }
                CalcularTotalHoras()
                CalcularTotalPrecio()
            }) {
                Text(text = "-")
            }
        }
    }
}


@Composable
fun ListaCard(modifier: Modifier = Modifier) {
    LazyVerticalGrid(columns = GridCells.Fixed(2), modifier.height(325.dp)) {
        items(DataSource.asignaturas) { asignatura ->
            Card(asignatura = asignatura)
        }
    }
}

var horasString: MutableState<String> = mutableStateOf("")
var horasEnteras: Int = 1

@Composable
fun TextoUsuario(modifier: Modifier = Modifier) {
    TextField(
        value = horasString.value,
        onValueChange = {
            horasString.value = it
            horasEnteras = horasString.value.toInt()
        },
        modifier
            .fillMaxWidth()
            .padding(16.dp),
        singleLine = true,
        label = { Text(text = "Horas a contratar o a eliminar") },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
    )
}

var ultimaAccion = mutableStateOf("No has hecho ninguna acción")
var ultimaAsignatura: MutableState<String> = mutableStateOf("")

@Composable
fun PanelInferior(modifier: Modifier = Modifier) {
    Column(
        modifier
            .background(Color.LightGray)
            .padding(30.dp)
            .fillMaxWidth()
    ) {
        Text(
            text = "Ultima acción",
            modifier
                .background(Color.Yellow)
                .fillMaxWidth()
        )
        Text(
            text = ultimaAccion.value,
            modifier
                .background(Color.Yellow)
                .fillMaxWidth()
        )
        Text(
            text = "Resumen",
            modifier
                .background(Color.White)
                .fillMaxWidth()
        )
        MostrarListaAsignaturas()
        Text(
            text = "Total horas: " + totalHoras.value,
            modifier
                .background(Color.Yellow)
                .fillMaxWidth()
        )
        Text(
            text = "Total precio: " + totalPrecio.value,
            modifier
                .background(Color.Yellow)
                .fillMaxWidth()
        )
    }
}

@Composable
fun BotonInferior(modifier: Modifier = Modifier) {
    Button(
        onClick = { /*TODO*/ },
        modifier
            .padding(16.dp)
    ) {
        Text(
            text = "Cambiar de pantalla",
            modifier
                .fillMaxWidth(),
            textAlign = TextAlign.Center
        )
    }
}


var listaAsignaturas = mutableStateMapOf<String, Asignatura>()

@Composable
fun MostrarListaAsignaturas(modifier: Modifier = Modifier) {
    if (listaAsignaturas.isEmpty()) {
        ultimaAsignatura.value = "No hay nada que mostrar (defecto)"
        Text(
            text = ultimaAsignatura.value,
            modifier
                .background(Color.White)
                .fillMaxWidth()
        )
    } else {
        for (asignatura in listaAsignaturas.values) {
            ultimaAsignatura.value =
                "Asig: " + asignatura.nombre + " precio/hora: " + asignatura.precioHora + " total horas: " + asignatura.horasContratadas
            Text(
                text = ultimaAsignatura.value,
                modifier
                    .background(Color.White)
                    .fillMaxWidth()
            )
        }
    }
}

var totalHoras = mutableStateOf(0)
private fun CalcularTotalHoras() {
    totalHoras.value = 0
    for (asignatura in listaAsignaturas.values) {
        totalHoras.value += asignatura.horasContratadas
    }
}

var totalPrecio = mutableStateOf(0)
private fun CalcularTotalPrecio() {
    totalPrecio.value = 0
    for (asignatura in listaAsignaturas.values) {
        totalPrecio.value += (asignatura.horasContratadas * asignatura.precioHora)
    }
}


@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
fun GreetingPreview() {
    ExaRecuperacionARDMTheme {
        Aplicacion()
    }
}