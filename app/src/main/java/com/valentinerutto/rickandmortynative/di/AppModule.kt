package com.valentinerutto.rickandmortynative.di

import com.google.gson.Gson
import com.valentinerutto.rickandmortynative.CharacterViewmodel
import com.valentinerutto.rickandmortynative.MyApplication
import com.valentinerutto.rickandmortynative.data.CharacterRepository
import com.valentinerutto.rickandmortynative.data.local.RickandMortyDatabase
import com.valentinerutto.rickandmortynative.data.network.ApiService
import com.valentinerutto.rickandmortynative.data.network.RetrofitClient
import com.valentinerutto.rickandmortynative.data.network.RetrofitClient.createOkClient
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit

val appModule = module {
    single { MyApplication.INSTANCE }
    single { Gson() }
    single { RickandMortyDatabase.getDatabase(context = androidContext()) }

    single {
        CharacterRepository(
            api = get(),
            dao = get()
        )
    }

    viewModel {
        CharacterViewmodel(repository = get())
    }
}

val networkingModule = module {
    single { RetrofitClient.provideOkHttpClient() }
    single { RetrofitClient.provideRetrofit(RetrofitClient.BASE_URL, get()) }

    single { createOkClient() }

    single {
        get<Retrofit>().create(ApiService::class.java)
    }
}

val databaseModule = module {
    single { get<RickandMortyDatabase>().characterDao() }

}