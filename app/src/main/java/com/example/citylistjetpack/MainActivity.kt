package com.example.citylistjetpack

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.view.animation.OvershootInterpolator
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.animation.doOnEnd
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.citylistjetpack.data.remote.FakeJsonDataInject.injectData
import com.example.citylistjetpack.data.remote.FakeJsonDataInject.loadJSONFromAsset
import com.example.citylistjetpack.domain.model.CityList
import com.example.citylistjetpack.presentation.viewmodel.CityViewModel
import com.example.citylistjetpack.ui.theme.CityListJetpackComposeTheme
import com.example.citylistjetpack.utility.Resource
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: CityViewModel by viewModels()
    @OptIn(ExperimentalMaterial3Api::class)
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //fake data inject
        val json = loadJSONFromAsset(this, "au_cities.json")
        if (json != null) {
            injectData(json);
        }

        installSplashScreen().apply {
            setKeepOnScreenCondition {
                !viewModel.isReady.value
            }
            setOnExitAnimationListener { screen ->
                val zoomX = ObjectAnimator.ofFloat(
                    screen.iconView,
                    View.SCALE_X,
                    0.4f,
                    0.0f
                )
                zoomX.interpolator = OvershootInterpolator()
                zoomX.duration = 500L
                zoomX.doOnEnd { screen.remove() }

                val zoomY = ObjectAnimator.ofFloat(
                    screen.iconView,
                    View.SCALE_Y,
                    0.4f,
                    0.0f
                )
                zoomY.interpolator = OvershootInterpolator()
                zoomY.duration = 500L
                zoomY.doOnEnd { screen.remove() }

                zoomX.start()
                zoomY.start()
            }
        }

        setContent {
            CityListJetpackComposeTheme(

            ) {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                    Scaffold(
                        topBar = {
                            TopAppBar(
                                title = {
                                    Text(
                                        text = "City List",
                                        modifier = Modifier.padding(16.dp),
                                        color = Color.White
                                    )
                                },
                                actions = {

                                },
                                colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = Color.Blue),
                                modifier = Modifier
                                    .background(color = Color.Blue)

                            )
                        }
                    ) {
                        StateScreen(viewModel)
                    }
                }
            }
        }
    }
}

@Composable
fun StateScreen(viewModel: CityViewModel) {
    val isRefreshing by viewModel.isRefreshing.collectAsState()

    PullToRefresh(
        refreshingState = isRefreshing,
        onRefresh = { viewModel.refreshStates() }
    ) {
        Column(modifier = Modifier.fillMaxSize().padding(top = 60.dp)) {
            if(viewModel.state.isLoading){
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(32.dp),
                        color = Color.LightGray
                    )
                }
            }
            if(viewModel.state.error.isNullOrEmpty().not()){
                Text(
                    text = "Error: ${viewModel.state.error}",
                    modifier = Modifier.padding(16.dp),
                    color = Color.Red
                )
            }
            viewModel.state.weatherInfo.let { data ->
                StateList(states = data)
            }

        }
    }
}

@Composable
fun PullToRefresh(
    refreshingState: Boolean,
    onRefresh: () -> Unit,
    content: @Composable () -> Unit
) {
    SwipeRefresh(
        state = rememberSwipeRefreshState(isRefreshing = refreshingState),
        onRefresh = onRefresh
    ) {
        content()
    }
}


@Composable
fun StateList(states: List<CityList>) {
    LazyColumn {
        items(states.size) { state ->



           Box(
               modifier = Modifier
                   .fillMaxWidth()
                   .padding(8.dp)
                   .border(
                       BorderStroke(2.dp, Color.LightGray),
                       shape = RoundedCornerShape(16f)
                   )
           ){
               Column(
                   modifier = Modifier.background(Color.White).padding(8.dp)
               ) {
                   Text(
                       text = states[state].city,
                       fontSize = 18.sp,
                       fontWeight = FontWeight.Bold,
                       style = MaterialTheme.typography.bodyLarge,
                   )
                   Text(
                       text = states[state].capital,
                       fontSize = 16.sp,
                       style = MaterialTheme.typography.bodyMedium,
                   )
                   Text(
                       text = "Population : ".plus(states[state].population),
                       fontSize = 16.sp,
                       style = MaterialTheme.typography.bodyMedium,
                   )
                   Text(
                       text = "Lat : ".plus(states[state].lat).plus(" Lan : ${states[state].lng}"),
                       fontSize = 16.sp,
                       style = MaterialTheme.typography.bodyMedium,
                   )
               }

           }



//            if (expanded) {
//                state.cities.forEach { city ->
//                    Text(
//                        text = city,
//                        style = MaterialTheme.typography.body1,
//                        modifier = Modifier
//                            .padding(start = 32.dp, top = 4.dp, bottom = 4.dp)
//                    )
//                }
//            }
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
    CityListJetpackComposeTheme {
        Greeting("Android")
    }
}