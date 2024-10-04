package com.jerson.soundeyes.feature_app.presentation.mobNetClassifier

import android.content.Context
import android.graphics.BitmapFactory
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jerson.soundeyes.R
import com.jerson.soundeyes.feature_app.consts.Const
import com.jerson.soundeyes.feature_app.data.api.MobileNetClassifier
import com.jerson.soundeyes.feature_app.domain.use_case.MobNetClassifierUseCase
import com.jerson.soundeyes.feature_app.presentation.utils.Util
import com.jerson.soundeyes.feature_app.presentation.yoloClassifier.YoloEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import javax.inject.Inject



@HiltViewModel
class MobNetClassifierViewModel @Inject constructor(
    private val mobNetClassifierUseCase: MobNetClassifierUseCase,
    @ApplicationContext private val context: Context
) : ViewModel(){
    private val _state = mutableStateOf(MobNetState())
    val state: State<MobNetState> = _state

    init {
        _state.value = _state.value.copy(
            labels = Util.loadLabels(context, Const.LABELS_MOBNET)
        )
    }



    fun onEvent(event: MobNetEvent) {
        when (event) {
            is MobNetEvent.ClassifyImage -> { // Modifique para aceitar um Bitmap
                viewModelScope.launch {
                    _state.value = _state.value.copy(
                        result = mobNetClassifierUseCase.invoke(event.bitmap) // Usa o bitmap do evento
                    )
                }
            }
        }
    }

}
