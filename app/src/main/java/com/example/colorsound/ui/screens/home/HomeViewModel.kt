package com.example.colorsound.ui.screens.home

import android.media.MediaRecorder
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.colorsound.ColorSoundApplication
import com.example.colorsound.data.local.LocalRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File
import java.util.UUID

enum class RecordState {
    Recording, Pausing, Normal
}

data class HomeUiState(
    val recordState: RecordState = RecordState.Normal,
    val showSaveDialog: Boolean = false,
)

class HomeViewModel(
    private val filesDir: String,
    private val repository: LocalRepository
) : ViewModel() {
    private var recorder: MediaRecorder? = null

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private val recordState
        get() = uiState.value.recordState

    /* TODO */
    fun onCancelClick() {
        _uiState.update { it.copy(showSaveDialog = false) }
    }

    /* TODO */
    fun onSaveClick() {
        _uiState.update { it.copy(showSaveDialog = false) }
    }

    private fun pauseRecording() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            recorder?.pause()
        }
    }

    private fun resumeRecording() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            recorder?.resume()
        }
    }

    private fun updateRecordState(newState: RecordState) {
        _uiState.update { it.copy(recordState = newState) }
    }

    fun onLongClick() {
        when (recordState) {
            RecordState.Normal -> onClick()
            else -> {
                _uiState.update { it.copy(showSaveDialog = true) }
                updateRecordState(RecordState.Normal)
                stopRecording()
            }
        }
    }

    fun onClick() {
        when (recordState) {
            RecordState.Recording -> {
                updateRecordState(RecordState.Pausing)
                pauseRecording()
            }
            RecordState.Pausing -> {
                updateRecordState(RecordState.Pausing)
                resumeRecording()
            }
            RecordState.Normal -> {
                updateRecordState(RecordState.Recording)
                startRecording()
            }
        }
    }

    private fun generateFilePath() = filesDir + File.separator + UUID.randomUUID() + ".mp3"

    private fun startRecording() {
        viewModelScope.launch {
            recorder = MediaRecorder().apply {
                setAudioSource(MediaRecorder.AudioSource.MIC)
                setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
                setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
                setOutputFile(generateFilePath())
                prepare()
                start()
            }
        }
    }

    private fun stopRecording() {
        recorder?.apply {
            stop()
            reset()
            release()
        }
        recorder = null
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as ColorSoundApplication)
                val fileDir = application.filesDir
                val repository = application.container.databaseRepository
                HomeViewModel(filesDir = fileDir, repository)
            }
        }
    }
}