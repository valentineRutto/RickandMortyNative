package com.valentinerutto.rickandmortynative.di

import com.google.gson.Gson
import com.valentinerutto.rickandmortynative.MyApplication
import com.valentinerutto.rickandmortynative.data.network.ApiService
import com.valentinerutto.rickandmortynative.data.network.RetrofitClient
import com.valentinerutto.rickandmortynative.data.network.RetrofitClient.createOkClient
import org.koin.dsl.module
import retrofit2.Retrofit

val appModule = module {
    single { MyApplication.INSTANCE }
    single { Gson() }

}

val networkingModule = module {
    single { RetrofitClient.provideOkHttpClient() }
    single { RetrofitClient.provideRetrofit(RetrofitClient.BASE_URL, get()) }

    single { createOkClient() }

    single {
        get<Retrofit>().create(ApiService::class.java)
    }
}