package com.example.colorsound.ui.screens.home

import android.media.MediaMetadataRetriever
import android.media.MediaRecorder
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.colorsound.ColorSoundApplication
import com.example.colorsound.data.local.LocalRepository
import com.example.colorsound.model.Sound
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

enum class RecordState {
    Recording, Pausing, Normal
}

data class HomeUiState(
    val recordState: RecordState = RecordState.Normal,
    val showSaveDialog: Boolean = false,
    val saveName: String = "",
    val color: Int = 0,
    val soundList: List<Sound> = emptyList(),
    val search: String = "",
)

class HomeViewModel(
    private val filesDir: String,
    private val repository: LocalRepository
) : ViewModel() {
    private var recorder: MediaRecorder? = null
    private lateinit var filePath: String

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private val recordState
        get() = uiState.value.recordState

    init {
        viewModelScope.launch {
            repository.observeSounds().collect { sounds ->
                _uiState.update { it.copy(soundList = sounds) }
            }
        }
    }

    fun updateSearch(search: String) {
        _uiState.update { it.copy(search = search) }
    }

    fun updateChoice(color: Int) {
        _uiState.update {
            it.copy(color = color)
        }
    }

    fun updateSaveName(name: String) {
        _uiState.update { it.copy(saveName = name) }
    }

    private fun stopAndDelete() {
        stopRecording()
        val file = File(filePath)
        file.delete()
    }

    /* TODO */
    fun onCancelClick() {
        _uiState.update { it.copy(showSaveDialog = false) }
        stopAndDelete()
        reset()
    }

    private fun getDuration(): String {
        val mmr = MediaMetadataRetriever().apply {
            setDataSource(filePath)
        }
        val durationInMill: String =
            mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION).toString()
        val second = durationInMill.toInt() / 1000
        return if (second < 3600) {
            String.format("%01d:%02d", second / 60, second % 60)
        } else {
            String.format("%d:%02d:%02d", second / 3600, second % 300 / 60, second % 60)
        }
    }

    fun onSaveClick() {
        _uiState.update { it.copy(showSaveDialog = false) }
        stopRecording()
        getDuration()
        viewModelScope.launch {
            val sound = uiState.value
            repository.insertSound(
                Sound(0, sound.color, sound.saveName, getCurrentDate(), filePath, getDuration())
            )
        }
        reset()
    }

    private fun reset() {
        updateSaveName("")
        updateChoice(0)
    }

    private fun getCurrentDate(): String {
        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        return current.format(formatter)
    }

    private fun pauseRecording() {
        recorder?.pause()
    }

    private fun resumeRecording() {
        recorder?.resume()
    }

    private fun updateRecordState(newState: RecordState) {
        _uiState.update { it.copy(recordState = newState) }
    }

    fun onLongClick() {
        when (recordState) {
            RecordState.Normal -> onClick()
            else -> {
                pauseRecording()
                _uiState.update { it.copy(showSaveDialog = true) }
                updateRecordState(RecordState.Normal)
//                stopRecording()
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
                updateRecordState(RecordState.Recording)
                resumeRecording()
            }
            RecordState.Normal -> {
                updateRecordState(RecordState.Recording)
                startRecording()
            }
        }
    }

    private fun generateFilePath(): String {
        filePath = filesDir + File.separator + UUID.randomUUID() + ".mp3"
        return filePath
    }

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