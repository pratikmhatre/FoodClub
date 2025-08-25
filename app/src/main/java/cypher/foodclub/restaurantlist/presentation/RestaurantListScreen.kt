package cypher.foodclub.restaurantlist.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import cypher.foodclub.R
import cypher.foodclub.core.theme.spacing
import cypher.foodclub.restaurantlist.domain.models.BestDeal
import cypher.foodclub.restaurantlist.domain.models.RestaurantListDisplayModel
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import coil.size.Scale
import cypher.foodclub.core.theme.FoodClubTheme

@Composable
fun RestaurantListScreen(
    modifier: Modifier = Modifier,
    viewModel: RestaurantListViewModel = hiltViewModel(),
    onRestaurantClicked: (String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Box(contentAlignment = Alignment.Center, modifier = modifier.fillMaxSize()) {
        if (uiState.isLoading) {
            CircularProgressIndicator()
        }
        if (uiState.isNetworkError) {
            Text(text = stringResource(R.string.network_error))
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                SearchBar(value = uiState.currentSearchQuery) {
                    viewModel.searchStoredRestaurants(it)
                }
                LazyColumn(verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.large)) {
                    items(items = uiState.displayedRestaurants) {
                        RestaurantItem(modifier = Modifier, restaurant = it, onRestaurantClicked)
                    }
                }
            }
        }
        if (uiState.isSearchError) {
            Text(text = stringResource(R.string.search_error, uiState.currentSearchQuery))
        }
    }
}

@Composable
fun SearchBar(modifier: Modifier = Modifier, value: String, onValueChange: (String) -> Unit) {
    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(Color.LightGray)
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .padding(horizontal = MaterialTheme.spacing.small)
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_search),
                contentDescription = "",
                modifier = Modifier.weight(1f)
            )
            TextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier.weight(9f),
                placeholder = {
                    Text(text = stringResource(R.string.search_restaurants))
                },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                ),
            )
        }
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(Color.LightGray)
        )
    }
}


@Preview(showBackground = true)
@Composable
private fun SearchBarPreview() {
    SearchBar(Modifier.background(Color.White), value = "", onValueChange = {})
}

@Composable
fun RestaurantItem(
    modifier: Modifier = Modifier,
    restaurant: RestaurantListDisplayModel,
    onRestaurantClicked: (String) -> Unit
) {
    Column(
        modifier = modifier
            .padding(
                horizontal = MaterialTheme.spacing.large,
                vertical = MaterialTheme.spacing.small
            )
            .clickable { onRestaurantClicked(restaurant.id) }
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(230.dp)
                .background(Color.LightGray, MaterialTheme.shapes.extraSmall)
        ) {
            AsyncImage(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(MaterialTheme.shapes.extraSmall),
                model = ImageRequest.Builder(LocalContext.current)
                    .data(restaurant.imageUrl)
                    .crossfade(true)
                    .error(R.drawable.img_placeholder_restaurant)
                    .scale(Scale.FIT)
                    .build(),
                contentDescription = restaurant.name,
                contentScale = ContentScale.Crop
            )

            Box(
                modifier = Modifier
                    .padding(
                        MaterialTheme.spacing.small
                    )
                    .background(Color.Red, MaterialTheme.shapes.extraSmall)
            ) {
                var dealText = stringResource(R.string.discount_text,restaurant.bestDeal.bestDiscount)
                if (restaurant.bestDeal.isDealDineInOnly) {
                    dealText += " - " + stringResource(R.string.dine_in_only)
                }

                val dealDuration = if (restaurant.bestDeal.isLimitedTimeDeal) {
                    stringResource(R.string.arrive_before, restaurant.bestDeal.validTill)
                } else {
                    stringResource(R.string.anytime_today)
                }

                Column(modifier = Modifier.padding(vertical = MaterialTheme.spacing.small, horizontal = MaterialTheme.spacing.medium)) {
                    Text(dealText, color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodySmall.copy(
                        lineHeight = 10.sp
                    ))
                    Text(dealDuration, color = Color.White, fontSize = 12.sp, style = MaterialTheme.typography.bodySmall.copy(
                        lineHeight = 10.sp
                    ))
                }
            }
        }
        Column(
            modifier = Modifier.padding(horizontal = MaterialTheme.spacing.medium),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = MaterialTheme.spacing.small),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = restaurant.name, fontWeight = FontWeight.ExtraBold, fontSize = 17.sp)
                Icon(
                    painter = painterResource(R.drawable.ic_heart),
                    contentDescription = "Like button"
                )
            }
            Text(text = restaurant.address, fontSize = 14.sp)
            Text(
                text = restaurant.cuisines,
                fontWeight = FontWeight.Light,
                fontSize = 12.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(text = restaurant.availableOptions, fontSize = 14.sp)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun RestaurantItemPreview() {
    val restaurant = RestaurantListDisplayModel(
        id = "1",
        name = "Pizza Hut",
        imageUrl = "https://imgs.search.brave.com/ubYH0rxytmpW_932HSsWenuQ3O_EflCG0Kzwv-30l7A/rs:fit:0:180:1:0/g:ce/aHR0cHM6Ly9hczEu/ZnRjZG4ubmV0L2pw/Zy8wMi8zMi82Mi81/OC8yMjBfRl8yMzI2/MjU4ODFfQVdsd3N0/Zk9UMTRnR3gxMnUw/VnJkalkxeTJTOW5i/c28uanBn",
        cuisines = "Italian, Indian",
        address = "6-10 Balmer Street, Flemington, Sydney",
        availableOptions = "Dine In",
        bestDeal = BestDeal(
            bestDiscount = "34", isDealDineInOnly = false, isLimitedTimeDeal = false, validTill = ""
        )
    )
    FoodClubTheme {
        RestaurantItem(restaurant = restaurant, onRestaurantClicked = {})
    }
}