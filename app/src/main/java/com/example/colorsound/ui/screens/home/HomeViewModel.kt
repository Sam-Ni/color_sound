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
import kotlinx.coroutines.launch
import java.io.File
import java.util.UUID

enum class RecordState {
    Recording, Pausing, Normal
}

class HomeViewModel(private val filesDir: String) : ViewModel() {
    private var recorder: MediaRecorder? = null
    var recordState by mutableStateOf(RecordState.Normal)
        private set

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

    fun onLongClick() {
        when (recordState) {
            RecordState.Recording -> {
                recordState = RecordState.Normal
                stopRecording()
            }
            else -> {}
        }
    }

    fun onClick() {
        when (recordState) {
            RecordState.Recording -> {
                recordState = RecordState.Pausing
                pauseRecording()
            }
            RecordState.Pausing -> {
                recordState = RecordState.Recording
                resumeRecording()
            }
            RecordState.Normal -> {
                recordState = RecordState.Recording
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
            release()
        }
        recorder = null
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as ColorSoundApplication)
                val fileDir = application.filesDir
                HomeViewModel(filesDir = fileDir)
            }
        }
    }
}