package com.amrk000.dailydeal.data.local

import android.content.Context
import androidx.core.content.contentValuesOf
import androidx.room.Database
import androidx.room.OnConflictStrategy
import androidx.room.Room.databaseBuilder
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.amrk000.dailydeal.Model.ItemData
import com.amrk000.dailydeal.Model.Order
import com.amrk000.dailydeal.Model.Permissions
import com.amrk000.dailydeal.Model.UserData
import com.amrk000.dailydeal.R
import com.google.gson.Gson
import kotlinx.coroutines.coroutineScope


@Database(entities = [UserData::class, ItemData::class, Order::class, Permissions::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun dao(): AppDao

    companion object {
        private lateinit var database: AppDatabase

        fun get(context: Context): AppDatabase {
            if (!::database.isInitialized) {
                database = databaseBuilder(
                    context,
                    AppDatabase::class.java,
                    "AppDatabase"
                ).fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build()
            }

            return database
        }
    }
}