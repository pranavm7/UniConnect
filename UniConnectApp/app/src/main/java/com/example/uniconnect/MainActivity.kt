package com.example.uniconnect

import android.app.Activity.RESULT_OK
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.uniconnect.dto.Post
import com.example.uniconnect.ui.theme.UniConnectTheme
import org.koin.androidx.viewmodel.ext.android.viewModel
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModel<MainViewModel>()
    //private var inTitle : String = ""
    //private var inDescription : String = ""


    private var user: FirebaseUser? = FirebaseAuth.getInstance().currentUser

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

    private fun signOn() {
        val providers = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().build()
        )
        val signInIntent = AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(providers)
            .build()
        signInLauncher.launch(signInIntent)
    }


    private val signInLauncher =
        registerForActivityResult(FirebaseAuthUIActivityResultContract())
        { res -> this.signInResult(res)}

    private fun signInResult(result: FirebaseAuthUIAuthenticationResult) {
        val response = result.idpResponse
        if (result.resultCode == RESULT_OK) {
            user = FirebaseAuth.getInstance().currentUser
        }
        else {
            Log.e("MainActivity.kt", "Error Logging in " + response?.error?.errorCode)
        }
    }
}

    @Composable
    fun PostDetails(name: String) {
        var title by remember () {mutableStateOf("")}
        var description by remember { mutableStateOf("")}
        //var postID by remember { mutableStateOf("")}
        val context = LocalContext.current
        Column{
            OutlinedTextField(
                value = title,
                onValueChange = {title = it},
                label = { Text(stringResource(R.string.title))}
            )
            OutlinedTextField(
                value = description,
                onValueChange = {description =it},
                label = { Text(stringResource(R.string.description))}
            )
            Button(
                onClick = {
                    var post = Post(title = title, description = description)

                    viewModel.savePost(post)
                    Toast.makeText(context, ", $title, $description", Toast.LENGTH_LONG).show()
                }
            ){Text(text = "Post")}
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun DefaultPreview() {
        UniConnectTheme {
            PostDetails("Android")
        }
    }
}

