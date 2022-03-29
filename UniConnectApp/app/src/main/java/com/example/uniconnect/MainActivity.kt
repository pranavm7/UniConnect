package com.example.uniconnect

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.uniconnect.ui.theme.UniConnectTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            UniConnectTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
                    PostDetails("Android")
                }
            }
        }
    }
}

@Composable
fun PostDetails(name: String) {
    var title by remember {mutableStateOf("")}
    var description by remember { mutableStateOf("")}
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    UniConnectTheme {
        PostDetails("Android")
    }
}