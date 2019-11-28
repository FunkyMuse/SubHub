package com.crazylegend.subhub.di.activity

import androidx.appcompat.app.AppCompatActivity
import com.crazylegend.subhub.di.core.CoreComponent
import com.crazylegend.subhub.vms.PickedDirVM


/**
 * Created by crazy on 11/27/19 to long live and prosper !
 */
interface ActivityComponent : CoreComponent {
    val instance: AppCompatActivity
    val pickedDirVM: PickedDirVM
}