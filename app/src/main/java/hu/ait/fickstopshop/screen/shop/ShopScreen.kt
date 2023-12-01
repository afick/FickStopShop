package hu.ait.fickstopshop.screen.shop

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.outlined.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import hu.ait.fickstopshop.data.ShopItem
import kotlinx.coroutines.launch
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.sp
import hu.ait.fickstopshop.R
import hu.ait.fickstopshop.data.CategoryQtyPrice
import hu.ait.fickstopshop.data.getIcon


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShopScreen(
    modifier: Modifier = Modifier,
    shopViewModel: ShopViewModel = hiltViewModel(),
    onNavigateToSummary: () -> Unit
) {
    var showAddItemDialog by rememberSaveable {
        mutableStateOf(false)
    }

    var tabIndex by rememberSaveable {
        mutableStateOf(0)
    }

    val tabs = stringArrayResource(R.array.tabs).toList()

    val activeTab = tabs[tabIndex]

    val itemList by shopViewModel.getAllItemListByCat(activeTab)
        .collectAsState(initial = emptyList())

    var itemEdit: ShopItem? by rememberSaveable {
        mutableStateOf(null)
    }

    Column {
        TopAppBar(
            title = { Text(text = stringResource(R.string.your_list_text)) },
            colors = TopAppBarDefaults.smallTopAppBarColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            ),
            actions = {
                IconButton(
                    onClick = {
                        showAddItemDialog = true
                        itemEdit = null
                    }, modifier = Modifier.width(60.dp)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(Icons.Filled.Add, null)
                        Text(text = stringResource(R.string.add_text), fontSize = 10.sp)
                    }
                }
                IconButton(onClick = {
                    shopViewModel.clearAllItems(activeTab)
                }, modifier = Modifier.width(60.dp)) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(Icons.Filled.Delete, null)
                        Text(text = stringResource(R.string.all_text), fontSize = 10.sp)
                    }
                }
                IconButton(onClick = {
                    shopViewModel.clearBoughtItems(activeTab)
                }, modifier = Modifier.width(60.dp)) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(Icons.Outlined.Delete, null)
                        Text(text = stringResource(R.string.bought_text), fontSize = 10.sp)
                    }
                }

                IconButton(onClick = {
                    onNavigateToSummary()
                }, modifier = Modifier.width(60.dp)) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(Icons.Filled.Info, null)
                        Text(text = stringResource(R.string.summary_text), fontSize = 10.sp)
                    }
                }
            }
        )
        ScrollableTabRow(selectedTabIndex = tabIndex) {
            tabs.forEachIndexed { index, title ->
                Tab(text = { Text(title) },
                    selected = tabIndex == index,
                    onClick = { tabIndex = index }
                )
            }
        }

        Column(
            modifier = modifier.padding(10.dp)
        ) {
            if (showAddItemDialog) {
                AddNewItemForm(
                    shopViewModel, {
                        showAddItemDialog = false
                    }, itemEdit,
                    if (activeTab != stringResource(R.string.all_text)) activeTab else stringResource(
                        R.string.produce_text
                    )
                )
            }

            if (itemList.isEmpty()) {
                Text(text = stringResource(R.string.add_your_first_item))
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier.fillMaxHeight()
                ) {
                    items(itemList) {
                        ItemCard(it, {
                            shopViewModel.removeShopItem(it)
                        }, { checkState ->
                            shopViewModel.changeShopItemState(it, checkState)
                        }, { editedItem ->
                            itemEdit = editedItem
                            showAddItemDialog = true
                        })
                    }
                }
            }
        }
    }

}

@Composable
fun ItemCard(
    item: ShopItem,
    onRemoveItem: () -> Unit = {},
    onBoughtChange: (Boolean) -> Unit = {},
    onEditItem: (ShopItem) -> Unit = {}
) {
    val context = LocalContext.current
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        ),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp
        ),
        modifier = Modifier.padding(4.dp)
    ) {
        var expanded by rememberSaveable {
            mutableStateOf(false)
        }

        Column(
            modifier = Modifier
                .padding(16.dp)
                .animateContentSize()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = getIcon(item.category, context = context)),
                    contentDescription = stringResource(R.string.category_text),
                    modifier = Modifier
                        .size(40.dp)
                        .padding(end = 10.dp)
                )
                Column() {
                    Text(text = item.title, modifier = Modifier.fillMaxWidth(0.5f))
                    Text(
                        text = "$${formatNumber(item.price)}",
//                        stringResource(R.string.price_format_text).format(item.price),
                        modifier = Modifier.fillMaxWidth(0.5f)
                    )
                    if (expanded) {
                        Text(text = item.description, modifier = Modifier.fillMaxWidth(0.5f))
                    }
                }

                Spacer(modifier = Modifier.weight(1f))
                Checkbox(
                    checked = item.isBought,
                    onCheckedChange = {
                        onBoughtChange(it)
                    }
                )
                Icon(
                    imageVector = Icons.Filled.Delete,
                    contentDescription = stringResource(R.string.delete_text),
                    modifier = Modifier.clickable {
                        onRemoveItem()
                    },
                    tint = Color.Red
                )
                Icon(
                    imageVector = Icons.Filled.Edit,
                    contentDescription = stringResource(R.string.edit_text),
                    modifier = Modifier.clickable {
                        onEditItem(item)
                    },
                    tint = MaterialTheme.colorScheme.primary
                )
                IconButton(onClick = { expanded = !expanded }) {
                    Icon(
                        imageVector = if (expanded)
                            Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                        contentDescription = if (expanded)
                            stringResource(R.string.collapse_text) else stringResource(R.string.expand_text)
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
                onDismissRequest = { expanded = false }, modifier = Modifier.fillMaxWidth()
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddNewItemForm(
    shopViewModel: ShopViewModel = viewModel(),
    onDialogDismiss: () -> Unit = {},
    itemEdit: ShopItem? = null,
    activeTab: String
) {
    val context = LocalContext.current

    Dialog(onDismissRequest = onDialogDismiss) {
        var itemTitle by rememberSaveable {
            mutableStateOf(itemEdit?.title ?: context.getString(R.string.empty_str))
        }

        var itemDescription by rememberSaveable {
            mutableStateOf(itemEdit?.description ?: context.getString(R.string.empty_str))
        }

        var itemCategory by rememberSaveable {
            mutableStateOf(itemEdit?.category ?: activeTab)
        }

        var itemPrice by rememberSaveable {
            if ((itemEdit?.price) != null) {
                mutableStateOf((itemEdit?.price?.times(100))?.toInt().toString())
            } else {
                mutableStateOf(context.getString(R.string.empty_str))
            }
        }

        if (itemPrice == "0") {
            itemPrice = ""
        }

        var priceErrorState by rememberSaveable {
            mutableStateOf(false)
        }

        var priceErrorText by rememberSaveable {
            mutableStateOf(context.getString(R.string.empty_str))
        }

        var itemBought by rememberSaveable {
            mutableStateOf(itemEdit?.isBought ?: false)
        }

        var titleEmpty by rememberSaveable {
            mutableStateOf(false)
        }

        var descriptionEmpty by rememberSaveable {
            mutableStateOf(false)
        }

        fun validate(text: String): Unit {
            if (text.isEmpty()) {
                priceErrorState = false
                priceErrorText = context.getString(R.string.empty_str)
                return
            }
            val valid = text.toDoubleOrNull()
            if (valid == null) {
                priceErrorState = true
                priceErrorText = context.getString(R.string.please_only_input_numbers_error)
            } else {
                priceErrorText = context.getString(R.string.empty_str)
                priceErrorState = false
            }
        }

        Column(
            Modifier
                .background(
                    color = MaterialTheme.colorScheme.secondaryContainer,
                    shape = MaterialTheme.shapes.medium
                )
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = itemTitle,
                onValueChange = { itemTitle = it; titleEmpty = false },
                label = { Text(text = stringResource(R.string.item_title_text)) },
                modifier = Modifier.fillMaxWidth(),
                trailingIcon = {
                    if (titleEmpty)
                        Icon(
                            Icons.Filled.Warning,
                            stringResource(R.string.error),
                            tint = MaterialTheme.colorScheme.error
                        )
                },
                supportingText = {
                    if (titleEmpty) {
                        Text(
                            text = "Title cannot be empty",
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.padding(start = 10.dp)
                        )
                    }
                },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = if (titleEmpty) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = if (titleEmpty) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
                )
            )


            OutlinedTextField(
                value = itemPrice,
//                value = itemPrice,
                onValueChange = {
                    val pattern = Regex("^\\d+\$")
                    // If it starts with 0 or with any non-number, don't allow it
                    itemPrice = if (it.startsWith("0") || !it.matches(pattern)) {
                        ""
                    } else {
                        it
                    }
                    validate(itemPrice)
                },
                visualTransformation = CurrencyAmountInputVisualTransformation(
                    fixedCursorAtTheEnd = true
                ),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                label = { Text(text = stringResource(R.string.item_price_title)) },
                modifier = Modifier.fillMaxWidth(),
                trailingIcon = {
                    if (priceErrorState)
                        Icon(
                            Icons.Filled.Warning,
                            stringResource(R.string.error),
                            tint = MaterialTheme.colorScheme.error
                        )
                },
                supportingText = {
                    if (priceErrorState) {
                        Text(
                            text = priceErrorText,
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.padding(start = 10.dp)
                        )
                    }
                },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = if (priceErrorState) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = if (priceErrorState) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
                )
            )

            OutlinedTextField(
                value = itemDescription,
                onValueChange = { itemDescription = it; descriptionEmpty = false },
                label = { Text(text = stringResource(R.string.item_description_text)) },
                modifier = Modifier.fillMaxWidth(),
                trailingIcon = {
                    if (descriptionEmpty)
                        Icon(
                            Icons.Filled.Warning,
                            stringResource(R.string.error),
                            tint = MaterialTheme.colorScheme.error
                        )
                },
                supportingText = {
                    if (descriptionEmpty) {
                        Text(
                            text = "Description cannot be empty",
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.padding(start = 10.dp)
                        )
                    }
                },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = if (descriptionEmpty) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = if (descriptionEmpty) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
                )
            )

            SpinnerSample (
                stringArrayResource(R.array.dataclasscategories).toList(),
                preselected = itemCategory,
                onSelectionChanged = { itemCategory = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp)
            )

            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(checked = itemBought, onCheckedChange = { itemBought = it })
                Text(text = stringResource(id = R.string.bought_text))
            }
            Row (
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(onClick = {
                    val ready = itemTitle.isNotEmpty() &&
                            !priceErrorState && itemDescription.isNotEmpty()

                    if (itemPrice.isEmpty()) itemPrice = "0"
                    if (ready) {
                        if (itemEdit == null) {
                            shopViewModel.addShopItem(
                                ShopItem(
                                    0,
                                    title = itemTitle,
                                    description = itemDescription,
                                    category = itemCategory,
                                    price = itemPrice.toDouble() / 100,
                                    isBought = itemBought
                                )
                            )
                        } else {
                            var itemEdited = itemEdit.copy(
                                title = itemTitle,
                                description = itemDescription,
                                category = itemCategory,
                                price = itemPrice.toDouble() / 100,
                                isBought = itemBought
                            )

                            shopViewModel.editShopItem(itemEdited)
                        }

                        onDialogDismiss()
                    } else {
                        if (itemTitle.isEmpty()) {
                            titleEmpty = true
                        }
                        if (itemDescription.isEmpty()) {
                            descriptionEmpty = true
                        }
                    }
                }) {
                    Text(text = stringResource(R.string.save_text))
                }
            }
        }
    }
}
