package com.todo.di

import android.content.Context
import androidx.room.Room
import com.todo.db.TaskDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ApplicationModule {


    @Singleton // Tell Dagger-Hilt to create a singleton accessible everywhere in ApplicationCompenent (i.e. everywhere in the application)
    @Provides
    fun provideYourDatabase(@ApplicationContext app: Context) =
        Room.databaseBuilder(
        app.applicationContext,
        TaskDatabase::class.java,
        "task_database"
        )
        .fallbackToDestructiveMigration()
        .build()


    @Singleton
    @Provides
    fun provideYourDao(db: TaskDatabase) = db.taskDao

}