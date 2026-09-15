package com.example.database.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.database.AppDatabase
import com.example.database.data.dao.ActionButtonsDao
import com.example.database.data.dao.NavigationItemDao
import com.example.database.data.dao.SliderButtonDao
import com.example.database.data.dao.SliderDao
import com.example.database.data.model.ActionButtons
import com.example.database.data.model.NavigationItem
import com.example.database.data.model.SliderButton
import com.example.database.data.repository.ActionButtonRepositoryImpl
import com.example.database.data.repository.NavigationItemRepositoryImpl
import com.example.database.data.repository.SliderButtonRepositoryImpl
import com.example.database.data.repository.SliderRepositoryImpl
import com.example.database.domain.repository.ActionButtonRepository
import com.example.database.domain.repository.NavigationItemRepository
import com.example.database.domain.repository.SliderButtonRepository
import com.example.database.domain.repository.SliderRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    lateinit var  database: AppDatabase
    @Provides
    fun providesDatabase(@ApplicationContext context: Context): AppDatabase {

         database =  Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            AppDatabase.DATABASE_NAME
        ).fallbackToDestructiveMigration()

            .addCallback(object : RoomDatabase.Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)

                    val ioScope = CoroutineScope(Dispatchers.IO)

                    ioScope.launch {
                        // Get the DAO from the database instance being created
                        val dao = database.navigationItemDao()
                        val buttonDao = database.actionButtonsDao()
//                        val sliderBtn = database.sliderButtonDao()
//                        val slider = database.sliderDao()

                        // Pre-populate the database with navigation items
                        val navigationItems = listOf(
                            NavigationItem(0, "Animation" , "1", true,1),
                            NavigationItem(0, "Settings" , "2", true,2),
//                            NavigationItem(0, "Favourite" , "3", true,3),
                            )

                        navigationItems.forEach { navigationItem ->
                            dao.insertNavigationItem(navigationItem)
                        }
                        val actionBtns = listOf(
                            ActionButtons(
                                actionButtonId = 0,
                                navigationItemId = 1,
                                buttonlabel = "Welcome",
                                action = "",
                                isAdmin = true,
                                isFavorite = true,
                                position = 0
                            ),
                            ActionButtons(
                                actionButtonId = 0,
                                navigationItemId = 1,
                                buttonlabel = "Good_Bye",
                                action = "",
                                isAdmin = true,
                                isFavorite = true,
                                position = 0
                            ),
                            ActionButtons(
                                actionButtonId = 0,
                                navigationItemId = 1,
                                buttonlabel = "Idle",
                                action = "",
                                isAdmin = true,
                                isFavorite = true,
                                position = 0
                            ),
                            ActionButtons(
                                actionButtonId = 0,
                                navigationItemId = 1,
                                buttonlabel = "Function1",
                                action = "",
                                isAdmin = true,
                                isFavorite = true,
                                position = 0
                                ),
                            ActionButtons(
                                actionButtonId = 0,
                                navigationItemId = 1,
                                buttonlabel = "Function2",
                                action = "",
                                isAdmin = true,
                                isFavorite = true,
                                position = 0
                            ),
                            ActionButtons(
                                actionButtonId = 0,
                                navigationItemId = 1,
                                buttonlabel = "Function3",
                                action = "",
                                isAdmin = true,
                                isFavorite = true,
                                position = 0
                            ),
                            ActionButtons(
                                actionButtonId = 0,
                                navigationItemId = 1,
                                buttonlabel = "Function4",
                                action = "",
                                isAdmin = true,
                                isFavorite = true,
                                position = 0
                            ),

                            ActionButtons(
                                actionButtonId = 0,
                                navigationItemId = 1,
                                buttonlabel = "Function5",
                                action = "",
                                isAdmin = true,
                                isFavorite = true,
                                position = 0
                            ),
                        )

                        actionBtns.forEach{ btn ->
                            buttonDao.insertActionButtons(btn)
                        }
                    }
                }
            })
            .build()
        return  database
    }


    @Provides
    fun providesNavigationItemDao(db: AppDatabase) = db.navigationItemDao()

    @Provides
    fun providesActionButtonDao(db: AppDatabase) = db.actionButtonsDao()

    @Provides
    fun providesSliderButtonDao(db: AppDatabase) = db.sliderButtonDao()

    @Provides
    fun providesNavigationItemRepository(navigationItemDao: NavigationItemDao) : NavigationItemRepository {
        return NavigationItemRepositoryImpl(navigationItemDao)
    }

    @Provides
    fun providesActionButtonRepository(actionButtonsDao: ActionButtonsDao): ActionButtonRepository {
        return ActionButtonRepositoryImpl(actionButtonsDao)
    }

    @Provides
    fun providesSliderButtonRepository(sliderButtonDao: SliderButtonDao): SliderButtonRepository {
        return SliderButtonRepositoryImpl(sliderButtonDao)
    }

    @Provides
    fun provideSliderDao(db: AppDatabase) = db.sliderDao()

    @Provides
    fun providesSliderRepository(sliderDao: SliderDao): SliderRepository {
        return SliderRepositoryImpl(sliderDao)
    }
}