package com.example.uniconnect

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.uniconnect.ui.theme.UniConnectTheme
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import com.example.uniconnect.dto.PostUserVM

class ListPostActivity : ComponentActivity() {

    private val listPostModel: ListPostModel = ListPostModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            UniConnectTheme {
                val allPostsAllUsers by listPostModel.allPostsAllUsers.observeAsState(initial = emptyList())
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    HomeContent(allPostsAllUsers)
                }
            }
        }
    }

    @Composable
    fun HomeContent(allPostsAllUsers: List<PostUserVM> = ArrayList<PostUserVM>()) {
        Scaffold(
            topBar = {
                TopAppBar(title = { Text("Latest Posts") })
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {
                        val intent = Intent(this, MainActivity::class.java)
                        this.startActivity(intent)
                    },
                    modifier = Modifier.height(70.dp).width(70.dp),
                    backgroundColor = Color.Red,
                    content = {
                        Icon(imageVector = Icons.Filled.Add,
                            contentDescription = "")
                    }
                )
            },
            content = {
                Surface() {
                    LazyColumn {
                        items(allPostsAllUsers) { post ->
                            DisplayPost(post)
                        }
                    }
                }
            }
        )
    }

    @Composable
    fun DisplayPost(post: PostUserVM) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp)
                .height(150.dp)
                .background(color = Color.Gray)
        ) {
            Row(
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .fillMaxWidth()
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_launcher_foreground),
                    contentDescription = "user icon",
                    modifier = Modifier
                        .padding(horizontal = 2.dp)
                        .align(CenterVertically)
                )
                Column () {
                    Text(
                        modifier = Modifier
                            .padding(horizontal = 8.dp),
                        text = post.title,
                        color = Color.White,
                        fontSize = 25.sp
                    )
                    Text(
                        modifier = Modifier
                            .padding(horizontal = 8.dp),
                        text = "By " + post.displayName,
                        color = Color.White,
                        fontSize = 16.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        modifier = Modifier
                            .padding(horizontal = 8.dp),
                        text = post.description,
                        color = Color.White,
                        fontSize = 16.sp
                    )
                }
            }
        }
    }
}