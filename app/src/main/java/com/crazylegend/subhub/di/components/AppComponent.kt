package com.crazylegend.subhub.di.components

import android.app.Application
import com.crazylegend.subhub.AppLevel
import com.crazylegend.subhub.core.AbstractAVM
import com.crazylegend.subhub.di.modules.AppModule
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton


/**
 * Created by crazy on 5/2/20 to long live and prosper !
 */

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {

    fun lifecycleFactory(): LifecycleComponent.Factory
    fun inject(app: AppLevel)
    fun inject(abstractAVM: AbstractAVM)

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance application: Application): AppComponent
    }
}