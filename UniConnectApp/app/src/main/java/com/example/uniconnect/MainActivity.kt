package com.example.uniconnect

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.uniconnect.dto.Post
import com.example.uniconnect.dto.User
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


    private var firebaseUser: FirebaseUser? = FirebaseAuth.getInstance().currentUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            //viewModel.fetchUniversities()
            firebaseUser?.let {
                val user = User(it.uid, "")
                viewModel.user = user
                viewModel.listenToThisUserPost()
            }
            val universities by viewModel.universities.observeAsState(initial = emptyList())
            UniConnectTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
                    PostDetails("Android")
                }
            }
        }
    }


    @Composable
    fun PostDetails(name: String) {
        var title by remember () {mutableStateOf("")}
        var description by remember { mutableStateOf("")}
        //var postID by remember { mutableStateOf("")}
        val context = LocalContext.current
        Column(
            Modifier.fillMaxHeight(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(id = R.string.post_title),
                style = MaterialTheme.typography.h6,
                modifier = Modifier.padding(start = 20.dp)
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp)
                    .padding(horizontal = 30.dp),
                horizontalArrangement = Arrangement.Center,
            ) {
                OutlinedTextField(
                    value = title,
                    onValueChange = {title = it},
                    label = { Text(stringResource(R.string.title))},
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier

                        .fillMaxWidth()
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(500.dp)
                    .padding(horizontal = 30.dp),
                horizontalArrangement = Arrangement.Center,
            ) {
                OutlinedTextField(
                    value = description,
                    onValueChange = {description = it},
                    label = { Text(stringResource(R.string.description))},
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier
                        .fillMaxSize()
                )
            }
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 30.dp, horizontal = 30.dp)
            ){
                Button(
                    onClick = {
                        var post = Post(title = title, description = description)

                        viewModel.savePost(post)
                        Toast.makeText(context, ", $title, $description", Toast.LENGTH_LONG).show()
                    },
                    shape = RoundedCornerShape(20.dp)
                ){
                    Text(text = "Post", Modifier.padding(start = 10.dp))
                }

                Button (onClick = {
                    signOn()
                },
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Text(text = "Logon", Modifier.padding(start = 10.dp))
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
            firebaseUser = FirebaseAuth.getInstance().currentUser
            firebaseUser?.let {
                val user = User(it.uid, it.displayName)
                viewModel.user = user
                viewModel.saveUser()
                viewModel.listenToThisUserPost()
            }
        }
        else {
            Log.e("MainActivity.kt", "Error Logging in " + response?.error?.errorCode)
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


