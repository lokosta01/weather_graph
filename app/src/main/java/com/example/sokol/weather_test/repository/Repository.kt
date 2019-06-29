package com.example.sokol.weather_test.storage.repository

interface Repository {
   companion object {
      const val DATABASE = "database"
      const val NETWORK = "network"
   }
}