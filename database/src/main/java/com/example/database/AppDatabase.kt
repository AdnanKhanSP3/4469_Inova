package com.example.database

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.database.data.dao.ActionButtonsDao
import com.example.database.data.dao.NavigationItemDao
import com.example.database.data.dao.SliderButtonDao
import com.example.database.data.dao.SliderDao
import com.example.database.data.model.ActionButtons
import com.example.database.data.model.NavigationItem
import com.example.database.data.model.SliderButton
import com.example.database.data.model.Sliders

@Database(
    entities =[NavigationItem::class, ActionButtons::class ,
        Sliders::class , SliderButton::class],
    version = 2,
    autoMigrations = [
//        AutoMigration(from = 1, to = 2),
//        AutoMigration(from = 2, to = 3)
    ]
)
abstract class AppDatabase: RoomDatabase(){

    abstract fun navigationItemDao(): NavigationItemDao
    abstract fun actionButtonsDao(): ActionButtonsDao
    abstract fun sliderDao(): SliderDao
    abstract fun sliderButtonDao(): SliderButtonDao

    companion object{
        const val  DATABASE_NAME = "app_db"
    }
}