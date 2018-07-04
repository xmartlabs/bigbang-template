package com.xmartlabs.template.di

import android.app.Application
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import com.xmartlabs.template.database.AppDataBase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DbModule {
  @Singleton
  @Provides
  fun provideDb(app: Application) = Room
      .databaseBuilder(app, AppDataBase::class.java, "templatedb.db")
      .fallbackToDestructiveMigration()
      .build()

  @Singleton
  @Provides
  fun provideRoomDb(db: AppDataBase): RoomDatabase = db
}
