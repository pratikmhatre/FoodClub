package cypher.foodclub

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.size.Scale
import cypher.foodclub.core.theme.FoodClubTheme
import cypher.foodclub.core.theme.spacing
import cypher.foodclub.core.utils.navigation.Screens
import cypher.foodclub.restaurantdetails.presentation.RestaurantDetailsScreen
import cypher.foodclub.restaurantlist.presentation.RestaurantListScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            FoodClubTheme {
                Scaffold(modifier = Modifier, topBar = { TopBar() }) { innerPadding ->
                    AppNavigation(modifier = Modifier.padding(innerPadding), navHostController = navController)
                }
            }
        }
    }


    @Composable
    fun AppNavigation(
        modifier: Modifier = Modifier,
        navHostController: NavHostController
    ) {
        NavHost(navController = navHostController, startDestination = Screens.RestaurantListScreen) {
            composable<Screens.RestaurantListScreen> {
                RestaurantListScreen(
                    modifier = modifier,
                    onRestaurantClicked = {
                        navHostController.navigate(Screens.RestaurantDetailsScreen(it))
                    })
            }
            composable<Screens.RestaurantDetailsScreen> {
                val args = it.toRoute<Screens.RestaurantDetailsScreen>()
                RestaurantDetailsScreen(modifier = modifier, restaurantId = args.id, onBackPressed = {
                    navHostController.popBackStack()
                })
            }
        }
    }


    @OptIn(ExperimentalMaterial3Api::class)
    @Preview(showBackground = true)
    @Composable
    fun TopBar(modifier: Modifier = Modifier) {
        TopAppBar(
            modifier = modifier.padding(horizontal = MaterialTheme.spacing.medium),
            title = {

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                    AsyncImage(
                        modifier = Modifier
                            .size(30.dp),
                        model = ImageRequest.Builder(LocalContext.current)
                            .data("https://play-lh.googleusercontent.com/TYPL9CGB7ggYvoV2cuT9JLs0siYInwcRaPs0Gg5cwECatfZYNZvUESR2FhNcRqIkLw=w480-h960-rw")
                            .crossfade(true)
                            .build(),
                        contentDescription = "",
                    )
                }

            },
            navigationIcon = {
                Icon(
                    painter = painterResource(R.drawable.ic_user),
                    contentDescription = "User",
                    modifier = Modifier.size(28.dp)
                )
            },
            actions = {
                Icon(
                    painter = painterResource(R.drawable.ic_settings),
                    contentDescription = "Settings",
                    modifier = Modifier.size(28.dp)
                )
            }
        )
    }

}