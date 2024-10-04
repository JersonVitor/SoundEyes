package com.jerson.soundeyes.di

import android.content.Context
import com.jerson.soundeyes.feature_app.data.api.MobileNetClassifier
import com.jerson.soundeyes.feature_app.data.api.YOLOClassifier
import com.jerson.soundeyes.feature_app.data.repository.MobNetRepositoryImpl
import com.jerson.soundeyes.feature_app.data.repository.YoloRepositoryImpl
import com.jerson.soundeyes.feature_app.domain.repository.MobNetRepository
import com.jerson.soundeyes.feature_app.domain.repository.YoloRepository
import com.jerson.soundeyes.feature_app.domain.use_case.YoloClassifierUseCase
import com.jerson.soundeyes.feature_app.domain.use_case.CloseClassifierUseCase
import com.jerson.soundeyes.feature_app.domain.use_case.MobNetClassifierUseCase
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
    fun providesYoloClassifier(@ApplicationContext context: Context): YOLOClassifier{
        YOLOClassifier.init(context)
        return YOLOClassifier

    }

    @Provides
    @Singleton
    fun providesMobNetClassifier(@ApplicationContext context: Context): MobileNetClassifier{
        MobileNetClassifier.init(context)
        return MobileNetClassifier
    }

    @Provides
    @Singleton
    fun providesMobileNetRepository(mobileNetClassifier: MobileNetClassifier): MobNetRepository{
        return MobNetRepositoryImpl(mobileNetClassifier)
    }

    @Provides
    @Singleton
    fun providesYoloRepository(yoloClassifier: YOLOClassifier): YoloRepository{
        return YoloRepositoryImpl(yoloClassifier)
    }

    @Provides
    @Singleton
    fun providesMobNetClassifierUseCase(mobNetRepository: MobNetRepository): MobNetClassifierUseCase{
        return MobNetClassifierUseCase(mobNetRepository)
    }

    @Provides
    @Singleton
    fun providesYoloClassifierUseCase(yoloRepository: YoloRepository): YoloClassifierUseCase{
        return YoloClassifierUseCase(yoloRepository)
    }

    @Provides
    @Singleton
    fun providesCloseClassifyUseCase(yoloRepository: YoloRepository): CloseClassifierUseCase {
        return CloseClassifierUseCase(yoloRepository)
    }

}