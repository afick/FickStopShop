package hu.ait.fickstopshop.data

import android.content.Context
import androidx.compose.ui.res.stringArrayResource
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import hu.ait.fickstopshop.R
import androidx.compose.ui.res.stringResource

@Entity(tableName = "shoptable")
data class ShopItem(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    @ColumnInfo(name = "title") var title: String,
    @ColumnInfo(name = "category") var category: String,
    @ColumnInfo(name = "description") var description: String,
    @ColumnInfo(name = "price") var price: Double,
    @ColumnInfo(name = "isbought") var isBought: Boolean
)

data class CategoryQtyPrice(
    var category: String,
    var qty: Int,
    var price: Double
)

fun getIcon(category: String, context: Context): Int {
    val categories = context.resources.getStringArray(R.array.dataclasscategories)

    val index = categories.indexOf(category)
    return if (index != -1) {
        when (index) {
            0 -> R.drawable.produce
            1 -> R.drawable.meat
            2 -> R.drawable.dairy
            3 -> R.drawable.bakery
            4 -> R.drawable.pantry
            5 -> R.drawable.frozen
            6 -> R.drawable.health
            7 -> R.drawable.home
            else -> R.drawable.other
        }
    } else {
        R.drawable.other
    }
}

