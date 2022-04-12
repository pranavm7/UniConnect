package com.example.uniconnect

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import com.example.uniconnect.dto.PostUserVM
import com.example.uniconnect.dto.User


data class ListItem(val name: String)

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

                    MessageList(allPostsAllUsers)
                }
            }
        }
    }

    @Composable
    fun MessageList(allPostsAllUsers: List<PostUserVM> = ArrayList<PostUserVM>()) {
        var newMessages = ArrayList<ListItem>()
        allPostsAllUsers.forEach { it ->
            newMessages.add(ListItem(name = it.title))
        }
        LazyColumn {
            items(newMessages) { item ->
                DisplayItem(item)
            }
        }
    }

    /*@Composable
    fun DisplayList(items: List<ListItem>) {
        val scrollState = rememberScrollState()
        Column(Modifier.verticalScroll(scrollState)) {
            repeat(items.size) {
                ListItem(items[it]);
            }
        }
        /*Box() {
            items.forEach { item ->
                ListItem(item)
            }
        }*/
    }*/

    @Composable
    fun DisplayItem(item: ListItem) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp)
                .height(60.dp)
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
                        .padding(horizontal = 8.dp)
                        .align(CenterVertically)
                )
                Text(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .align(CenterVertically),
                    text = item.name,
                    color = Color.White,
                    fontSize = 16.sp
                )
            }
        }
    }
}