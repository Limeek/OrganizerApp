package ru.limeek.organizer.presentation.di.modules

import dagger.Module
import dagger.android.AndroidInjectionModule
import dagger.android.ContributesAndroidInjector
import ru.limeek.organizer.presentation.di.modules.activity.*
import ru.limeek.organizer.presentation.di.modules.fragments.CalendarFragmentModule
import ru.limeek.organizer.presentation.di.modules.fragments.EventsFragmentModule
import ru.limeek.organizer.presentation.di.scopes.ActivityScope
import ru.limeek.organizer.presentation.di.scopes.FragmentScope
import ru.limeek.organizer.presentation.views.*

@Module(includes = [AndroidInjectionModule::class])
interface BuilderModule{
    @ActivityScope
    @ContributesAndroidInjector(modules = [MainActivityModule::class])
    fun mainActivityInjector(): MainActivity

    @ActivityScope
    @ContributesAndroidInjector(modules = [EventDetailsActivityModule::class])
    fun eventDetailsActivityInjector(): EventDetailsActivity

    @ActivityScope
    @ContributesAndroidInjector(modules = [LocationActivityModule::class])
    fun locationActivityInjector(): LocationActivity

    @ActivityScope
    @ContributesAndroidInjector(modules = [LocationDetailsActivityModule::class])
    fun locationDetailsActivityInjector(): LocationDetailsActivity

    @FragmentScope
    @ContributesAndroidInjector(modules = [EventsFragmentModule::class])
    fun eventsFragmentModule(): EventsFragment

    @FragmentScope
    @ContributesAndroidInjector(modules = [CalendarFragmentModule::class])
    fun calendarFragmentModule(): CalendarFragment
}