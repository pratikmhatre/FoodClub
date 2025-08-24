package cypher.foodclub.di

import cypher.foodclub.core.data.RestaurantsRepositoryImpl
import cypher.foodclub.core.data.network.ApiList
import cypher.foodclub.core.domain.RestaurantsRepository
import cypher.foodclub.core.utils.AppConstants
import cypher.foodclub.core.utils.RetrofitInstance
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Singleton
    @Provides
    fun provideApiList(): ApiList {
        return RetrofitInstance.create(AppConstants.BASE_URL, ApiList::class.java)
    }

    @Singleton
    @Provides
    fun provideRestaurantsRepository(apiList: ApiList): RestaurantsRepository {
        return RestaurantsRepositoryImpl(apiList)
    }
}