// AppModule.kt
package com.example.eulerity_assessment.di

import com.example.eulerity_assessment.data.PetRepository
import com.example.eulerity_assessment.data.PetsAPI
import com.example.eulerity_assessment.data.PetsRepositoryImpl
import com.example.eulerity_assessment.domain.SortPetsUseCase
import com.example.eulerity_assessment.domain.SortPetsUseCaseImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://eulerity-hackathon.appspot.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun providePetsAPI(retrofit: Retrofit): PetsAPI {
        return retrofit.create(PetsAPI::class.java)
    }

    @Provides
    @Singleton
    fun providePetsRepository(petsAPI: PetsAPI): PetRepository {
        return PetsRepositoryImpl(petsAPI)
    }

    @Provides
    @Singleton
    fun provideSortPetsUseCase(): SortPetsUseCase {
        return SortPetsUseCaseImpl()
    }
}
