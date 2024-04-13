package com.sunueric.newsrssfeedapp.ui.screens

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.content.res.Configuration.UI_MODE_TYPE_NORMAL
import android.graphics.Bitmap
import android.util.Log
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.Text
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.sunueric.newsrssfeedapp.R
import com.sunueric.newsrssfeedapp.data.network.models.Item
import com.sunueric.newsrssfeedapp.ui.theme.NewsRSSFeedAppTheme
import com.sunueric.newsrssfeedapp.utils.calculateTimeAgo
import com.sunueric.newsrssfeedapp.utils.generateQRCode
import com.sunueric.newsrssfeedapp.utils.parseDateTime
import kotlinx.coroutines.delay

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun NewsItem(
    companyLogoUrl: String,
    newsCategoryText: String,
    articles: List<Item>
) {
    var currentIndex by remember { mutableIntStateOf(0) }
    var currentArticle by remember { mutableStateOf(articles.firstOrNull()) }
    var newsThumbnailUrl by remember { mutableStateOf("") }
    var newsTitleText by remember { mutableStateOf("") }
    var newsDescriptionText by remember { mutableStateOf("") }
    var progress by remember { mutableFloatStateOf(0f) }
    var pubDate by remember { mutableStateOf("") }
    val alpha = remember {
        Animatable(0f)
    }
    var bitmap by remember { mutableStateOf<Bitmap?>(null) }

    val placeholder = R.drawable.placeholder_arts

    LaunchedEffect(key1 = "newsTicker") {
        while (true) {
            val duration = 5_000 // Duration in milliseconds
            val stepTime = 60L // Time step for updating progress (in milliseconds)
            val steps = duration / stepTime

            // Reset progress to 0 at the start
            progress = 0f

            // Gradually increase progress to 100%
            for (i in 1..steps) {
                delay(stepTime)
                progress = i.toFloat() / steps
            }

            // Once the progress reaches 100%, update the article index
            currentIndex = (currentIndex + 1) % articles.size
            currentArticle = articles[currentIndex]
        }
    }

    LaunchedEffect(currentIndex) {
        // Reset alpha to 0
        alpha.snapTo(0f)

        // Animate the alpha value to 1f
        alpha.animateTo(
            1f,
            animationSpec = tween(2000)
        )
    }

    currentArticle?.let { article ->
        Log.d("NewsItem", "current index: $currentIndex")
        newsDescriptionText = article.description ?: ""
        newsTitleText = article.title ?: ""
        newsThumbnailUrl = article.imageUrl.toString()
        bitmap = generateQRCode(article.articleLink ?: "")
        val pastDateTime = parseDateTime(article.publicationDate ?: "")
        val timeAgo = calculateTimeAgo(pastDateTime)
        pubDate = timeAgo
        Log.d("NewsItem", "newsTitleText: $newsTitleText")
        Log.d("NewsItem", "newsDescriptionText: $newsDescriptionText")
        Log.d("NewsItem", "article link: ${article.articleLink}")
        Log.d(
            "NewsItem",
            "newsThumbnailUrl: $newsThumbnailUrl, news thumbnail url from object: ${article.imageUrl}"
        )
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .alpha(alpha.value)
    ) {
        Image(
            painter = rememberAsyncImagePainter(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(newsThumbnailUrl)
                    // Apply any request customizations here. Example shown below:
                    .apply(block = {
                        // Placeholder image
                        placeholder(placeholder)
                        // Error image
                        error(placeholder)
                    })
                    .build()
            ),
            contentDescription = null,
            modifier = Modifier
                .blur(50.dp)
                .fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent, // Starts transparent
                            Color.Black.copy(alpha = 0.9f), // Gradually introduces a transparent black
                            Color.Black // Ends with solid black
                        )
                    )
                )
        ) {
            ConstraintLayout(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(40.dp)
            ) {
                val (
                    newsThumbnail,
                    newsDescription,
                    newsCategoryContainer,
                    newsTitle,
                    displayProgress,
                    timePosted,
                    qrCode
                ) = createRefs()
                Image(
                    modifier = Modifier
                        .constrainAs(newsThumbnail) {
                            top.linkTo(parent.top)
                            start.linkTo(parent.start)
                        }
                        .size(280.dp)
                        .border(6.dp, Color.White),
                    contentScale = ContentScale.FillBounds,
                    painter = rememberAsyncImagePainter(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(newsThumbnailUrl)
                            // Apply any request customizations here. Example shown below:
                            .apply(block = {
                                // Placeholder image
                                placeholder(placeholder)
                                // Error image
                                error(placeholder)
                            })
                            .build()
                    ),
                    contentDescription = "News Image"
                )

                Row(
                    modifier = Modifier
                        .constrainAs(newsCategoryContainer) {
                            top.linkTo(parent.top)
                            start.linkTo(newsThumbnail.end)
                            end.linkTo(parent.end)
                            width = Dimension.fillToConstraints
                        }
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    Image(
                        modifier = Modifier
                            .size(80.dp)
                            .background(Color.White, CircleShape)
                            .clip(CircleShape),
                        contentScale = ContentScale.Inside,
                        painter = rememberAsyncImagePainter(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(companyLogoUrl)
                                // Apply any request customizations here. Example shown below:
                                .apply(block = {
                                    // Placeholder image
                                    placeholder(placeholder)
                                    // Error image
                                    error(placeholder)
                                })
                                .build()
                        ),
                        contentDescription = "News Image"
                    )

                    Text(
                        modifier = Modifier.padding(start = 30.dp),
                        text = newsCategoryText,
                        style = TextStyle(
                            fontSize = 50.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    )
                }

                Text(
                    modifier = Modifier
                        .padding(start = 20.dp, top = 20.dp)
                        .constrainAs(newsTitle) {
                            top.linkTo(newsCategoryContainer.bottom)
                            start.linkTo(newsThumbnail.end)
                            end.linkTo(parent.end)
                            width = Dimension.fillToConstraints
                        },
                    text = newsTitleText,
                    style = TextStyle(
                        fontSize = 34.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                )

                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier
                        .size(200.dp, 24.dp)
                        .padding(start = 20.dp, top = 20.dp)
                        .constrainAs(displayProgress) {
                            top.linkTo(newsTitle.bottom)
                            start.linkTo(newsThumbnail.end)
                            width = Dimension.fillToConstraints
                        },
                    color = Color.White,
                    trackColor = Color.White.copy(0.25f)
                )

                Text(
                    modifier = Modifier
                        .constrainAs(timePosted) {
                            top.linkTo(displayProgress.bottom)
                            start.linkTo(newsThumbnail.end)
                        }
                        .padding(start = 30.dp, top = 16.dp),
                    text = pubDate,
                    style = TextStyle(
                        fontSize = 18.sp,
                        color = Color.White
                    )
                )

                    Text(
                        modifier = Modifier
                            .constrainAs(newsDescription) {
                            top.linkTo(newsThumbnail.bottom)
                            start.linkTo(parent.start)
                            bottom.linkTo(parent.bottom)
                        },
                        text = newsDescriptionText,
                        style = TextStyle(
                            fontSize = 26.sp,
                            color = Color.White
                        )
                    )

                    bitmap?.let {
                        Image(
                            bitmap = it.asImageBitmap(),
                            contentDescription = "QR Code",
                            modifier = Modifier
                                .constrainAs(qrCode) {
                                    top.linkTo(newsThumbnail.bottom)
                                    start.linkTo(newsDescription.end)
                                    end.linkTo(parent.end)
                                    bottom.linkTo(parent.bottom)
                                }
                                .padding(start = 20.dp)
                                .size(120.dp)
                                .aspectRatio(1f)
                        )
                    }
                }
        }
    }
}

@Preview(
    showBackground = true,
    uiMode = UI_MODE_TYPE_NORMAL or UI_MODE_NIGHT_YES,
    device = Devices.TV_720p)
@Composable
fun PreviewNewsFeed() {
    NewsRSSFeedAppTheme {
        NewsItem("random", "Arts", emptyList())
    }
}