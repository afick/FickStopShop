package hu.ait.fickstopshop.screen.shop

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.ait.fickstopshop.data.ShopDAO
import hu.ait.fickstopshop.data.ShopItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShopViewModel @Inject constructor(
    private val shopDAO: ShopDAO
): ViewModel() {

    fun getAllItemListByCat(category: String): Flow<List<ShopItem>> {
        return if (category == "All") {
            shopDAO.getAllItems()
        } else {
            shopDAO.getItemsByCat(category)
        }
    }

    fun addShopItem(item: ShopItem) {
        viewModelScope.launch {
            shopDAO.insert(item)
        }
    }

    fun editShopItem(item: ShopItem) {
        viewModelScope.launch {
            shopDAO.update(item)
        }
    }

    fun removeShopItem(item: ShopItem) {
        viewModelScope.launch {
            shopDAO.delete(item)
        }
    }

    fun changeShopItemState (item: ShopItem, value: Boolean) {
        val newItem = item.copy()
        newItem.isBought = value
        viewModelScope.launch {
            shopDAO.update(newItem)
        }
    }

    fun clearAllItems(category: String) {
        viewModelScope.launch {
            if (category == "All")
                shopDAO.deleteAllItems()
            else
                shopDAO.deleteAllItemsByCat(category)
        }
    }

    fun clearBoughtItems(category: String) {
        viewModelScope.launch {
            if (category == "All")
                shopDAO.deleteBoughtItems()
            else
                shopDAO.deleteBoughtItemsByCat(category)
        }
    }


}