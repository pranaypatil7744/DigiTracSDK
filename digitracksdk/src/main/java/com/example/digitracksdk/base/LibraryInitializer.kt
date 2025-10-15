package com.innov.digitrac.base

import android.content.Context
import android.util.Log
import androidx.multidex.MultiDex
import com.example.digitracksdk.di.AppModule
import com.example.digitracksdk.di.NetworkModule
import com.example.digitracksdk.utils.AppUtils
import com.example.digitracksdk.utils.ImageUtils
import com.example.digitracksdk.utils.PreferenceUtils
import io.realm.Realm
import io.realm.RealmConfiguration
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

object LibraryInitializer {

    private const val TAG = "LibraryInitializer"
    private const val DATABASE_NAME = "digitrac.realm"
    private var initialized = false

    fun init(context: Context) {
        if (initialized) return // prevent double initialization
        initialized = true

        MultiDex.install(context)
        initSingletons(context)

        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(context)
            modules(listOf(AppModule, NetworkModule))
        }

        Realm.init(context)
        val realmConfiguration = RealmConfiguration.Builder()
            .name(DATABASE_NAME)
            .schemaVersion(1)
            .migration { realm, oldVersion, newVersion ->
                Log.e(TAG, "Realm Migration from $oldVersion to $newVersion")
            }
            .allowWritesOnUiThread(true)
            .allowQueriesOnUiThread(true)
            .build()
        Realm.setDefaultConfiguration(realmConfiguration)
    }

    private fun initSingletons(context: Context) {
        AppUtils.setInstance()
        AppUtils.INSTANCE?.preferenceUtils = PreferenceUtils(context)
        ImageUtils.setImageInstance()
    }
}
