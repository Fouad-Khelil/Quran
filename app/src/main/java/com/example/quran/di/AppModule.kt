package com.example.quran.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.quran.data.data_source.local.qurandb.QuranDb
//import com.example.quran.data.data_source.remote.QuranApi
import com.example.quran.data.repository.DataStoreRepository
import com.example.quran.data.repository.QuranAndThikrRepositoryImpl
import com.example.quran.data.repository.ReciterRepositoryImpl
import com.example.quran.exoplayer.QuranServiceConnection
import com.example.quran.R
import com.example.quran.data.repository.NamesOfAllahRepositoryImpl
import com.example.quran.sensor_manager.AccelerometerSensor
import com.example.quran.sensor_manager.MagneticFieldSensor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideQuranDatabase(@ApplicationContext context: Context): QuranDb {
        return Room.databaseBuilder(
            context,
            QuranDb::class.java,
            QuranDb.DATABASE_NAME
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideQuranRepository(db: QuranDb): QuranAndThikrRepositoryImpl {
        return QuranAndThikrRepositoryImpl(db)
    }

    @Provides
    @Singleton
    fun provideReciterRepository(db: QuranDb): ReciterRepositoryImpl {
        return ReciterRepositoryImpl(db)
    }

    @Provides
    @Singleton
    fun provideNamesOfAllahRepository(db: QuranDb): NamesOfAllahRepositoryImpl {
        return NamesOfAllahRepositoryImpl(db)
    }

    @Provides
    @Singleton
    fun provideDataStoreRepository(
        @ApplicationContext context: Context
    ) = DataStoreRepository(context = context)

//    @Singleton
//    @Provides
//    fun provideQuranApi() : QuranApi {
//        return Retrofit.Builder()
//            .addConverterFactory(GsonConverterFactory.create())
//            .baseUrl(Constants.BASE_URL)
//            .build()
//            .create(QuranApi::class.java)
//    }

    @Singleton
    @Provides
    fun provideMusicServiceConnection(
        @ApplicationContext context: Context
    ) = QuranServiceConnection(
        context
    )


    @Singleton
    @Provides
    fun provideGlideInstance(
        @ApplicationContext context: Context
    ) = Glide.with(context).setDefaultRequestOptions(
        RequestOptions()
            .placeholder(R.drawable.img_error)
            .error(R.drawable.img_error)
            .diskCacheStrategy(DiskCacheStrategy.DATA)
    )

    @Provides
    @Singleton
    fun provideAccelerometerSensor(app: Application): AccelerometerSensor {
        return AccelerometerSensor(app)
    }


    @Provides
    @Singleton
    fun provideContext(        @ApplicationContext context: Context
    ): Context {
        return context
    }

    @Provides
    @Singleton
    fun provideMagneticFieldSensor(app: Application): MagneticFieldSensor {
        return MagneticFieldSensor(app)
    }

}


//    @Provides
//    @Singleton
//        fun provideDbBuiler(quranRepo :QuranAndThikrRepositoryImpl , @ApplicationContext context: Context): DbBuilder {
//        return DbBuilder(quranRepo , context)
//    }