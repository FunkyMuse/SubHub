package com.crazylegend.subhub.di.contracts

import com.crazylegend.subhub.di.providers.AdaptersProvider
import com.crazylegend.subhub.di.providers.AppProvider
import com.crazylegend.subhub.di.providers.LifecycleProvider
import com.crazylegend.subhub.di.providers.PermissionsProvider


/**
 * Created by crazy on 5/2/20 to long live and prosper !
 */
interface InjectionContracts {
    var appProvider: AppProvider
    var lifecycleProvider: LifecycleProvider
    var adaptersProvider: AdaptersProvider
    var permissionsProvider: PermissionsProvider
}