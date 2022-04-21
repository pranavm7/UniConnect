package com.example.uniconnect

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.uniconnect.dto.Post
import com.example.uniconnect.dto.PostUserVM
import com.example.uniconnect.ui.theme.UniConnectTheme

class PostDetailActivity : ComponentActivity() {

    private val postDetailModel: PostDetailModel = PostDetailModel()
    private var post: PostUserVM? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        val extras = intent.extras
        if (extras != null) {
            val postID = extras.getString("POST_ID")
            val uID = extras.getString("USER_ID")

            post = postDetailModel.getPostDetails(postID = postID!!, uID = uID!!)

        }
        if(post != null){Log.w("post:","${post!!.title}\n${post!!.description}")}

        super.onCreate(savedInstanceState)
        setContent {
            UniConnectTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    PostDetailContent(post)
                }
            }
        }
    }
}
@Composable
fun PostDetailContent(post: PostUserVM?) {
    if(post != null){Log.w("post:","${post!!.title}\n${post!!.description}")}
    val context = LocalContext.current
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
            .height(150.dp)
            .background( color = Color(0xFFf4717))//(color = Color.Gray)
    ) {
        if (post != null) {
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
                        .align(Alignment.CenterVertically)
                )
                Column() {
                    Text(
                        modifier = Modifier
                            .padding(horizontal = 8.dp),
                        color = Color.Black,
                        fontSize = 25.sp,
                        text = post.title
                    )
                    Text(
                        modifier = Modifier
                            .padding(horizontal = 8.dp),
                        text = "By " + post.displayName,
                        color = Color.Black,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        modifier = Modifier
                            .padding(horizontal = 8.dp),
                        text = post.description,
                        color = Color.Black,
                        fontSize = 16.sp
                    )
                }
            }
        }
        else{
            Column(modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.White,
                            Color(0xFFf4717f)
                        )
                    )
                ),
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
                            .align(Alignment.CenterVertically)
                    )
                    Column() {
                        Text(
                            modifier = Modifier
                                .padding(horizontal = 8.dp),
                            color = Color.Black,
                            fontSize = 25.sp,
                            text = "Post Title goes here"
                        )
                        Text(
                            modifier = Modifier
                                .padding(horizontal = 8.dp),
                            text = "By " + "Sample Username",
                            color = Color.Black,
                            fontSize = 16.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            modifier = Modifier
                                .padding(horizontal = 8.dp),
                            text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec suscipit nibh sed ante aliquet aliquet. Nulla laoreet, tellus tempor fringilla vulputate, mi nulla vestibulum orci, eu porttitor augue mi sed diam.",
                            color = Color.Black,
                            fontSize = 16.sp
                        )

                    }
                }
                Spacer(modifier = Modifier.height(15.dp))
                Divider(
                    color = Color.Red,
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .fillMaxWidth(),
                    thickness = 3.dp
                )
                Spacer(modifier = Modifier.height(8.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                        .padding(4.dp)
                        .height(150.dp)
                        .background(color = Color.White)
                )
                {
                    Text(
                        modifier = Modifier
                            .padding(horizontal = 8.dp),
                        text = "Comments section with a lazycolumn here",
                        color = Color.Black,
                        fontSize = 16.sp
                    )
                }
            }
        }
    }
}
