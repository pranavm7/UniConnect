package com.example.uniconnect

import android.Manifest
import android.content.ContentValues.TAG
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.ContactsContract
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import coil.compose.AsyncImage
import com.example.uniconnect.dto.Photo
import com.example.uniconnect.dto.Post
import com.example.uniconnect.dto.User
import com.example.uniconnect.ui.theme.UniConnectTheme
import org.koin.androidx.viewmodel.ext.android.viewModel
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import java.io.File
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : ComponentActivity() {
    private var uri: Uri? = null
    private lateinit var currentImagePath: String
    private val viewModel: MainViewModel by viewModel<MainViewModel>()
    private var strUri by mutableStateOf("")
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
        val context = LocalContext.current

        Column {
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text(stringResource(R.string.title)) }
            )
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text(stringResource(R.string.description)) }
            )
            Row(modifier = Modifier.padding(all = 2.dp)) {


                Button(
                    onClick = {
                        var post = Post(title = title, description = description)

                        viewModel.savePost(post)
                        //Toast.makeText(context, ", $title, $description", Toast.LENGTH_LONG).show()
                    }
                ) { Text(text = "Post") }
                Button(onClick = { signOn() })
                { Text(text = "Logon") }
                Button(onClick = { takePhoto() })
                { Text(text = "Photo") }
            }
            AsyncImage(model = strUri, contentDescription= "Description Image")
        }
    }

    private fun takePhoto() {
        if(hasCameraPermission() == PERMISSION_GRANTED && hasExternalStoragePermission() == PERMISSION_GRANTED){
            invokeCamera()
        }else{
            requestMultiplePermissionsLauncher.launch(arrayOf(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA
            ))
        }
    }

    private val requestMultiplePermissionsLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()){
        resultsMap ->
        var permissionGranted = false
        resultsMap.forEach{
            if (it.value == true){
                permissionGranted = it.value
            }else{
                permissionGranted = false
                return@forEach
            }
        }
        if(permissionGranted){
            invokeCamera()
        }else{
            Toast.makeText(this,getString(R.string.cameraPermissionDenied), Toast.LENGTH_LONG).show()
        }
    }

    private fun invokeCamera() {
        val file = createImageFile()
        try {
            uri = FileProvider.getUriForFile(this, "com.example.uniconnect.fileprovider", file)
        }catch (e: Exception){
            Log.e(TAG, "Error: ${e.message}")
            var foo = e.message
        }
        getCameraImage.launch(uri)
    }

    private fun createImageFile() : File {
        val timestamp = SimpleDateFormat("yyyMMdd_HHmmss").format(Date())
        val imageDirectory = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "Specimen_${timestamp}",
            ".jpg",
            imageDirectory
        ).apply{
            currentImagePath = absolutePath
        }
    }

    private val getCameraImage = registerForActivityResult(ActivityResultContracts.TakePicture()){
        success ->
        if (success){
            Log.i(TAG, "Image Location: $uri")
            strUri = uri.toString()
            val photo = Photo(localUri = uri.toString())
            viewModel.photos.add(photo)
        }else{
            Log.i(TAG, "Image not saved. $uri")
        }
    }

    fun hasCameraPermission() = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
    fun hasExternalStoragePermission() = ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE)

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


