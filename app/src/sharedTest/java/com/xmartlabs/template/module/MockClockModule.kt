package com.xmartlabs.template.module

import dagger.Module
import dagger.Provides
import org.threeten.bp.*
import java.util.*
import javax.inject.Singleton

/**
 * Created by medina on 22/09/2016.
 */
@Module
class MockClockModule {
  companion object {
    internal val DEFAULT_TIME_ZONE = ZoneId.of("GMT-03:00")
    private val DEFAULT_DATE = LocalDate.of(1991, Month.MARCH, 6)
    private val DEFAULT_TIME = LocalTime.of(11, 45)
    internal val DEFAULT_ZONED_DATE_TIME = ZonedDateTime.of(DEFAULT_DATE, DEFAULT_TIME, DEFAULT_TIME_ZONE)
  }

  @Provides
  @Singleton
  fun provideTimeZone(): TimeZone = TimeZone.getTimeZone(DEFAULT_TIME_ZONE.id)

  @Provides
  @Singleton
  fun provideClock() = Clock.fixed(DEFAULT_ZONED_DATE_TIME.toInstant(), DEFAULT_TIME_ZONE)
}
