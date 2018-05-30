package com.xmartlabs.template.di

import android.app.Application
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import com.xmartlabs.template.db.Db
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DbModule {
  @Singleton
  @Provides
  fun provideDb(app: Application) = Room
      .databaseBuilder(app, Db::class.java, "templatedb.db")
      .fallbackToDestructiveMigration()
      .build()

  @Singleton
  @Provides
  fun provideRoomDb(db: Db): RoomDatabase = db

  @Singleton
  @Provides
  fun provideUserDao(db: Db) = db.userDao()
}