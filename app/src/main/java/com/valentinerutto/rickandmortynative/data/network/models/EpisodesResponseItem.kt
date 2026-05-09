package com.valentinerutto.rickandmortynative.data.network.models

data class EpisodesResponseItem(
    val air_date: String,
    val characters: List<String>,
    val created: String,
    val episode: String,
    val id: Int,
    val name: String,
    val url: String
)