package hu.ait.fickstopshop.screen.summary

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role.Companion.Image
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import hu.ait.fickstopshop.data.getIcon
import hu.ait.fickstopshop.screen.shop.ShopViewModel
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.outlined.ArrowDropDown
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import hu.ait.fickstopshop.R
import hu.ait.fickstopshop.data.CategoryQtyPrice
import hu.ait.fickstopshop.screen.shop.formatNumber
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SummaryScreen(
    modifier: Modifier = Modifier,
    summaryScreenViewModel: SummaryScreenViewModel = hiltViewModel(),
    onNavigateToShopScreen: () -> Unit = {}
) {
    val context = LocalContext.current

    var coroutineScope = rememberCoroutineScope()

    var orderBy by rememberSaveable {
        mutableStateOf(context.getString(R.string.price_text))
    }

    var orderAsc by rememberSaveable {
        mutableStateOf(false)
    }

    val catList by summaryScreenViewModel.getQtyPricebyCat(orderBy, orderAsc)
        .collectAsState(initial = emptyList())

    var totalQty by rememberSaveable {
        mutableStateOf(0)
    }

    var totalPrice by rememberSaveable {
        mutableStateOf(0.0)
    }


    LaunchedEffect(Unit) {
        coroutineScope.launch {
            totalQty = summaryScreenViewModel.getShopItemsNum()
            totalPrice = summaryScreenViewModel.getPriceTotal()
        }
    }

    Column {
        TopAppBar(
            title = { Text(text = stringResource(id = R.string.summary_text)) },
            navigationIcon = {
                IconButton(
                    onClick = {
                        onNavigateToShopScreen()
                    }
                ) { Icon(Icons.Filled.ArrowBack, null) }
            },
            colors = TopAppBarDefaults.smallTopAppBarColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            ),
            actions = {
                SpinnerSample(
                    stringArrayResource(id = R.array.orderlist).toList(),
                    preselected = orderBy,
                    onSelectionChanged = {
                        orderBy = it
                    },
                    modifier = Modifier
                        .width(150.dp)
                )
                IconButton(onClick = {
                    orderAsc = !orderAsc
                }) {
                    Icon(
                        if (orderAsc) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                        null
                    )
                }
            }
        )
        Column(
            modifier = modifier.padding(horizontal = 20.dp)
        ) {
            CategoryRow(
                category = stringResource(R.string.all_items_text),
                qty = totalQty,
                price = totalPrice,
                style = TextStyle(
                    fontSize = 18.sp,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold
                )
            )
            Spacer(modifier = Modifier.padding(10.dp))
            Divider(
                color = Color.Black, modifier = Modifier
                    .fillMaxWidth()
                    .width(1.dp)
            )
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxHeight()
            ) {
                items(catList) {
                    CategoryRow(
                        category = it.category,
                        qty = it.qty,
                        price = it.price
                    )
                }
            }


        }
    }
}

@Composable
fun SpinnerSample(
    list: List<String>,
    preselected: String,
    onSelectionChanged: (data: String) -> Unit,
    modifier: Modifier = Modifier
) {
    var selected by remember { mutableStateOf(preselected) }
    var expanded by remember { mutableStateOf(false) } // initial value
    OutlinedCard(
        modifier = modifier.clickable {
            expanded = !expanded
        }) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.Top,
        ) {
            Text(
                text = selected,
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            )
            Icon(Icons.Outlined.ArrowDropDown, null, modifier = Modifier.padding(8.dp))
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                list.forEach { listEntry ->
                    DropdownMenuItem(
                        onClick = {
                            selected = listEntry
                            expanded = false
                            onSelectionChanged(selected)
                        },
                        text = {
                            Text(
                                text = listEntry,
                                modifier = Modifier
                            )
                        },
                    )
                }
            }
        }
    }

}

@Composable
fun CategoryRow(
    category: String,
    qty: Int,
    price: Double,
    style: TextStyle = TextStyle(
        fontSize = 18.sp,
        textAlign = TextAlign.Center,
        fontWeight = FontWeight.Normal
    )
) {
    val context = LocalContext.current
    Row(
        modifier = Modifier
            .padding(top = 24.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = getIcon(category, context = context)),
            contentDescription = stringResource(id = R.string.category_text),
            modifier = Modifier
                .size(40.dp)
                .padding(end = 10.dp)
        )
        Text(
            text = category,
            textAlign = style.textAlign,
            fontSize = style.fontSize,
            fontWeight = style.fontWeight
        )
        Spacer(modifier = Modifier.weight(1f))
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.qty_header),
                textAlign = style.textAlign,
                fontSize = style.fontSize,
                fontWeight = style.fontWeight
            )
            Text(
                text = stringResource(R.string.qty_text, qty),
                textAlign = style.textAlign,
                fontSize = style.fontSize,
                fontWeight = style.fontWeight
            )
        }
        Spacer(modifier = Modifier.padding(16.dp))
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.total_price_header),
                textAlign = style.textAlign,
                fontSize = style.fontSize,
                fontWeight = style.fontWeight
            )
            Text(
                text = "$${formatNumber(price)}",
                textAlign = style.textAlign,
                fontSize = style.fontSize,
                fontWeight = style.fontWeight
            )
        }
    }
}
