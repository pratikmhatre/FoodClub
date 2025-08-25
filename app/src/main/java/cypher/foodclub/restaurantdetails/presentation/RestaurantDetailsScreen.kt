package cypher.foodclub.restaurantdetails.presentation

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.size.Scale
import cypher.foodclub.R
import cypher.foodclub.core.theme.spacing
import cypher.foodclub.restaurantdetails.domain.models.Deal
import cypher.foodclub.restaurantdetails.domain.models.RestaurantDetailsModel
import cypher.foodclub.restaurantdetails.presentation.states.RestaurantDetailsUiStates

@Composable
fun RestaurantDetailsScreen(
    modifier: Modifier = Modifier,
    restaurantId: String,
    onBackPressed: () -> Unit,
    viewModel: RestaurantDetailsViewModel = hiltViewModel()
) {
    val scrollState = rememberScrollState()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    viewModel.setRestaurantId(restaurantId)
    when (uiState) {

        is RestaurantDetailsUiStates.Success -> {
            val restaurant = (uiState as RestaurantDetailsUiStates.Success).data

            Column(modifier = modifier.verticalScroll(scrollState).fillMaxSize()) {
                AsyncImage(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp),
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(restaurant.imageUrl)
                        .crossfade(true)
                        .error(R.drawable.img_placeholder_restaurant)
                        .scale(Scale.FIT)
                        .build(),
                    contentDescription = restaurant.name,
                    contentScale = ContentScale.Crop
                )
                Column(modifier = Modifier.padding(vertical = MaterialTheme.spacing.small)) {
                    MenuBar()
                    Column(modifier = Modifier.padding(horizontal = MaterialTheme.spacing.extraLarge)) {
                        RestaurantBasicDetails(restaurant = restaurant)
                        restaurant.deals.forEachIndexed { index, it ->
                            DealItem(modifier = Modifier, deal = it)
                            if (index != restaurant.deals.lastIndex) {
                                Divider()
                            }
                        }
                    }
                }
            }

        }

        else -> {}
    }
}

data class MenuItem(val icon: Int, val title: String)

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun MenuBar(modifier: Modifier = Modifier) {
    val menuList = listOf(
        MenuItem(icon = R.drawable.ic_menu, title = stringResource(R.string.menu)),
        MenuItem(icon = R.drawable.ic_call, title = stringResource(R.string.call_us)),
        MenuItem(icon = R.drawable.ic_location, title = stringResource(R.string.location)),
        MenuItem(icon = R.drawable.ic_heart, title = stringResource(R.string.favourite)),
    )
    Column {
        Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
                .fillMaxWidth()
                .padding(vertical = MaterialTheme.spacing.medium),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            menuList.forEach {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(3.dp)
                ) {
                    Icon(painterResource(it.icon), contentDescription = it.title, modifier = Modifier.size(24.dp))
                    Text(it.title, fontSize = 15.sp)
                }
            }
        }
        Divider()
    }
}

@Composable
fun RestaurantBasicDetails(modifier: Modifier = Modifier, restaurant: RestaurantDetailsModel) {
    Column(
        modifier = modifier
            .padding(top = MaterialTheme.spacing.large),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = restaurant.name, fontWeight = FontWeight.ExtraBold, fontSize = 24.sp)
        Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))
        Text(
            text = restaurant.cuisines,
            fontWeight = FontWeight.Light,
            fontSize = 14.sp,
            color = Color.DarkGray,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(MaterialTheme.spacing.large))

        Divider()

        Column(
            modifier = Modifier.padding(
                vertical = MaterialTheme.spacing.extraLarge
            ), verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small)
            ) {
                Icon(
                    painterResource(R.drawable.ic_clock),
                    contentDescription = "",
                    modifier = Modifier.size(26.dp),
                    tint = Color.DarkGray
                )
                Text(text = stringResource(R.string.work_hours, restaurant.workHours.uppercase()), fontSize = 14.sp)
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small)
            ) {
                Icon(
                    painterResource(R.drawable.ic_location_pin),
                    contentDescription = "",
                    modifier = Modifier.size(26.dp),
                    tint = Color.DarkGray
                )
                Text(text = restaurant.address, fontSize = 14.sp)
            }
        }
        Divider()
    }
}

@Composable
fun Divider(modifier: Modifier = Modifier) {
    Spacer(
        modifier = Modifier
            .height(1.dp)
            .background(Color.LightGray)
            .fillMaxWidth()
    )
}

@Composable
fun DealItem(modifier: Modifier = Modifier, deal: Deal) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .padding(vertical = MaterialTheme.spacing.large)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Row(
                verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(
                    MaterialTheme.spacing.small
                )
            ) {
                Icon(painterResource(R.drawable.ic_lightning), contentDescription = "", modifier = Modifier.size(18.dp))
                Text(
                    text = stringResource(R.string.discount_text, deal.discount),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Red
                )
            }
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.extraSmall))
            Text(
                text = if (deal.isLimitedTime) stringResource(
                    R.string.effective_between,
                    deal.effectiveBetween
                ) else stringResource(R.string.anytime),
                fontSize = 14.sp, color = Color.DarkGray
            )
            Text(
                text = if (deal.isMultipleQuantityLeft) stringResource(
                    R.string.deals_left,
                    deal.quantityLeft
                ) else stringResource(R.string.deal_left, deal.quantityLeft),
                fontWeight = FontWeight.Light,
                fontSize = 13.sp, color = Color.DarkGray
            )
        }

        OutlinedButton(
            onClick = {},
            colors = ButtonDefaults.buttonColors(containerColor = Color.White), border = BorderStroke(1.dp, Color.Red),
            modifier = modifier
                .height(40.dp),
            shape = RoundedCornerShape(MaterialTheme.spacing.extraLarge)
        ) {
            Text(stringResource(R.string.redeem), color = Color.Red, fontWeight = FontWeight.Bold)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DealPreview() {
    val deal =
        Deal(
            discount = "30% Off",
            effectiveBetween = "Between 12:00PM - 1:00PM",
            quantityLeft = "10",
            isLimitedTime = true, isMultipleQuantityLeft = false
        )
    DealItem(deal = deal)
}


@Preview(showBackground = true)
@Composable
private fun RestaurantBasicDetailsPreview() {
    val restaurant = RestaurantDetailsModel(
        name = "Saffron Palace",
        imageUrl = "",
        cuisines = "Italian, French, Danish",
        address = "2-9 Hobert St, Windsor, NSW 2067",
        workHours = "12:00PM - 1:00PM",
        deals = emptyList()
    )
    RestaurantBasicDetails(modifier = Modifier, restaurant)
}