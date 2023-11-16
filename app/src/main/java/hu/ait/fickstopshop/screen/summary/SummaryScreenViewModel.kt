package hu.ait.fickstopshop.screen.summary

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.ait.fickstopshop.data.CategoryQtyPrice
import hu.ait.fickstopshop.data.ShopDAO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class SummaryScreenViewModel @Inject constructor(
    private val shopDAO: ShopDAO
) : ViewModel() {

    suspend fun getShopItemsNum(): Int {
        return shopDAO.getItemsNum()
    }

    suspend fun getPriceTotal(): Double {
        return shopDAO.getPriceTotal()
    }

    fun getQtyPricebyCat(order: String, asc: Boolean): Flow<List<CategoryQtyPrice>> {
        if (!asc) {
            return shopDAO.getQtyPricebyCat(order).map { it ->
                when (order) {
                    "Category" -> {
                        it.sortedBy { it.category }
                    }

                    "Price" -> {
                        it.sortedBy { it.price }
                    }

                    else -> {
                        it.sortedBy { it.qty }
                    }
                }.reversed()
            }
        } else {
            return shopDAO.getQtyPricebyCat(order).map { it ->
                when (order) {
                    "Category" -> {
                        it.sortedBy { it.category }
                    }

                    "Price" -> {
                        it.sortedBy { it.price }
                    }

                    else -> {
                        it.sortedBy { it.qty }
                    }
                }
            }
        }
    }
}