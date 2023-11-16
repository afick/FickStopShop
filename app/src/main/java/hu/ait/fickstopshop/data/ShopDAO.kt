package hu.ait.fickstopshop.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ShopDAO {
    @Query("SELECT * from shoptable")
    fun getAllItems(): Flow<List<ShopItem>>

    @Query("SELECT * from shoptable where category = :category")
    fun getItemsByCat(category: String): Flow<List<ShopItem>>

    @Query("SELECT * from shoptable WHERE id = :id")
    fun getItem(id: Int): Flow<ShopItem>

    @Query("SELECT COUNT(*) from shoptable")
    suspend fun getItemsNum(): Int

    @Query("SELECT COUNT(*) from shoptable WHERE category = :category")
    suspend fun getItemsNumByCat(category: String): Int

    @Query("SELECT SUM(price) from shoptable")
    suspend fun getPriceTotal(): Double

    @Query("SELECT SUM(price) from shoptable WHERE category = :category")
    suspend fun getPriceTotalByCat(category: String): Double

    @Query("SELECT category, COUNT(*) as qty, SUM(price) as price from shoptable GROUP BY category ORDER BY :order")
    fun getQtyPricebyCat(order: String): Flow<List<CategoryQtyPrice>>

    @Query("SELECT DISTINCT category from shoptable ORDER BY category ASC")
    fun getCategories(): Flow<List<String>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(item: ShopItem)

    @Update
    suspend fun update(item: ShopItem)

    @Delete
    suspend fun delete(item: ShopItem)

    @Query("DELETE from shoptable")
    suspend fun deleteAllItems()

    @Query("DELETE from shoptable WHERE isbought = 1")
    suspend fun deleteBoughtItems()

    @Query("DELETE from shoptable WHERE category = :category")
    suspend fun deleteAllItemsByCat(category: String)

    @Query("DELETE from shoptable WHERE isbought = 1 and category = :category")
    suspend fun deleteBoughtItemsByCat(category: String)
}