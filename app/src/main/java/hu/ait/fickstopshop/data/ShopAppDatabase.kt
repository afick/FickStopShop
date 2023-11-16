package hu.ait.fickstopshop.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import hu.ait.fickstopshop.R


// Update version if changing the structure of the table
@Database(entities = [ShopItem::class], version = 1, exportSchema = false)
abstract class ShopAppDatabase : RoomDatabase() {

    abstract fun shopDao(): ShopDAO

    companion object {
        @Volatile
        private var Instance: ShopAppDatabase? = null

        fun getDatabase(context: Context): ShopAppDatabase {
            // if the Instance is not null, return it, otherwise create a new database instance.
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, ShopAppDatabase::class.java,
                    context.getString(R.string.shop_database_db))
                    // Setting this option in your app's database builder means that Room
                    // permanently deletes all data from the tables in your database when it
                    // attempts to perform a migration with no defined migration path.
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { Instance = it }
            }
        }
    }
}