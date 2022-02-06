package com.lasteyestudios.ipoalerts.data.local


import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.lasteyestudios.ipoalerts.data.local.model.CompanyLocalModel




@Database(entities = [CompanyLocalModel::class], version = 2, exportSchema = false)

abstract class CompanyWishlistDatabase : RoomDatabase() {

    abstract fun companyWishlistDao(): CompanyWishlistDao

    // A companion abject can be accessed directly through the class without a object instance of the class
    companion object {

        // Volatile means writes to this field are immediately made visible to other threads
        @Volatile
        private var INSTANCE: CompanyWishlistDatabase? = null

        // This function gets the instance of the DB
        fun getDatabase(context: Context): CompanyWishlistDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }

            // Synchronised means once a thread calls this block {acquires a lock}, no other thread
            // can access it before the first one releases the lock
            synchronized(this) {
                // FIXME -> DO NOT FORGET TO PROVIDE MIGRATIONS FOR EACH CHANGE IN THE SCHEMA.
                //  THE DB IS NOT GOING TO THROW AN EXCEPTION BUT WILL DROP ALL TABLES.
                //  REMOVE THIS COMMENT ONLY IF THIS BEHAVIOUR IS CHANGED.
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CompanyWishlistDatabase::class.java,
                    "company_wishlist_database"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                return instance
            }
        }
    }
}