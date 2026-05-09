package com.valentinerutto.rickandmortynative

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.valentinerutto.rickandmortynative.navigation.AppNavGraph
import com.valentinerutto.rickandmortynative.ui.screen.CharacterScreen
import com.valentinerutto.rickandmortynative.ui.theme.RickandMortyNativeTheme
import kotlinx.coroutines.coroutineScope
import org.koin.compose.viewmodel.koinViewModel
import kotlin.coroutines.coroutineContext

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        enableEdgeToEdge()
        setContent {
            RickandMortyNativeTheme {

                    AppNavGraph()


            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    RickandMortyNativeTheme {
        Greeting("Android")
    }
}