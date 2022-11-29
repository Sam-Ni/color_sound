package com.example.colorsound.ui.vm.service

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
import com.example.colorsound.ui.vm.data.*
import com.example.colorsound.util.Injecter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class RecordService : ViewModel() {
    private var recorder: MediaRecorder? = null
    private lateinit var filePath: String
    private val filesDir: String = Injecter.get("FilesDir")
    private val repository: LocalRepository = Injecter.get("DatabaseRepository")
    private val recordData: MutableStateFlow<RecordData> = Injecter.get("RecordData")
    private val dialogData: MutableStateFlow<SaveSoundDialogData> =
        Injecter.get("SaveSoundDialogData")
    private val maskData: MutableStateFlow<MaskData> = Injecter.get("MaskData")
    private val listData: MutableStateFlow<LocalSoundListData> =
        Injecter.get("LocalSoundListData")

    fun onSaveClick() {
        dialogData.update { it.copy(showSaveDialog = false) }
        getDuration()
        viewModelScope.launch {
            val sound = Sound(
                0,
                dialogData.value.color,
                dialogData.value.saveName,
                getCurrentDateTime(),
                filePath,
                getDuration()
            )
            repository.insertSound(sound)

            val original = listData.value.soundList.toMutableList()
            original.add(0, sound)
            updateSoundList(original)
        }
        resetDialogInfo()
    }

    fun onCancelClick() {
        dialogData.update { it.copy(showSaveDialog = false) }
        deleteAudio(filePath)
        resetDialogInfo()
    }

    fun onRecordBtnLongClick() {
        when (recordData.value.recordState) {
            RecordState.Normal -> onRecordBtnClick()
            else -> {
                stopRecording()
                dialogData.update { it.copy(showSaveDialog = true) }
                updateRecordState(RecordState.Normal)
                maskData.update { it.copy(isMask = false) }
            }
        }
    }

    fun onRecordBtnClick() {
        when (recordData.value.recordState) {
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
                maskData.update { it.copy(isMask = true) }
            }
        }
    }

    fun updateSaveName(name: String) {
        dialogData.update { it.copy(saveName = name) }
    }

    fun updateChoice(color: Int) {
        dialogData.update { it.copy(color = color) }
    }

    private fun deleteAudio(fileUrl: String) {
        val file = File(fileUrl)
        file.delete()
    }

    private fun updateSoundList(soundList: List<Sound>) {
        listData.update { it.copy(soundList = soundList) }
    }

    private fun getCurrentDateTime(): String {
        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        return current.format(formatter)
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

    private fun resetDialogInfo() {
        dialogData.update { it.copy(saveName = "", color = 0) }
    }

    private fun updateRecordState(newState: RecordState) {
        recordData.update { it.copy(recordState = newState) }
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

    private fun pauseRecording() {
        recorder?.pause()
    }

    private fun resumeRecording() {
        recorder?.resume()
    }

    private fun stopRecording() {
        recorder?.apply {
            stop()
            reset()
            release()
        }
        recorder = null
    }
}