package ru.limeek.organizer.presentation.di.modules

import dagger.Module
import dagger.android.AndroidInjectionModule
import dagger.android.ContributesAndroidInjector
import ru.limeek.organizer.presentation.di.modules.fragments.EventDetailsFragmentModule
import ru.limeek.organizer.presentation.di.modules.fragments.LocationFragmentModule
import ru.limeek.organizer.presentation.di.modules.fragments.LocationDetailsFragmentModule
import ru.limeek.organizer.presentation.di.modules.fragments.MainFragmentModule
import ru.limeek.organizer.presentation.di.modules.fragments.CalendarFragmentModule
import ru.limeek.organizer.presentation.di.modules.fragments.EventsFragmentModule
import ru.limeek.organizer.presentation.di.scopes.ActivityScope
import ru.limeek.organizer.presentation.di.scopes.FragmentScope
import ru.limeek.organizer.presentation.views.*

@Module(includes = [AndroidInjectionModule::class])
interface BuilderModule{

    @ActivityScope
    @ContributesAndroidInjector
    fun mainActivityInjector(): MainActivity

    @FragmentScope
    @ContributesAndroidInjector(modules = [LocationFragmentModule::class])
    fun locationActivityInjector(): LocationFragment

    @FragmentScope
    @ContributesAndroidInjector(modules = [LocationDetailsFragmentModule::class])
    fun locationDetailsActivityInjector(): LocationDetailsFragment

    @FragmentScope
    @ContributesAndroidInjector(modules = [EventsFragmentModule::class])
    fun eventsFragmentModule(): EventsFragment

    @FragmentScope
    @ContributesAndroidInjector(modules = [CalendarFragmentModule::class])
    fun calendarFragmentModule(): CalendarFragment

    @FragmentScope
    @ContributesAndroidInjector(modules = [MainFragmentModule::class])
    fun mainFragmentInjector(): MainFragment

    @FragmentScope
    @ContributesAndroidInjector(modules = [EventDetailsFragmentModule::class])
    fun eventDetailsFragmentInjector(): EventDetailsFragment

}