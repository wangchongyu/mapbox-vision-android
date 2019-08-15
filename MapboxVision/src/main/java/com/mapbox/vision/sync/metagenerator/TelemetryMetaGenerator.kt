package com.mapbox.vision.sync.metagenerator

import com.mapbox.vision.mobile.core.models.VideoClip
import com.mapbox.vision.utils.VisionLogger
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.util.HashMap
import java.util.concurrent.TimeUnit
import org.json.JSONArray
import org.json.JSONObject

class TelemetryMetaGenerator : MetaGenerator {

    companion object {
        private const val TAG = "TelemetryMetaGenerator"
    }

    override fun generateMeta(
        videoClipMap: HashMap<String, VideoClip>,
        saveDirPath: String,
        startRecordCoreMillis: Long
    ) {
        val arr = JSONArray()
        for (videoPart in videoClipMap) {
            val jsonPath = JSONObject()
            val paths = videoPart.key.split("/")
            val name = paths[paths.size - 1]

            val startRecordCoreSeconds = TimeUnit.MILLISECONDS.toSeconds(startRecordCoreMillis)
            val startSeconds = startRecordCoreSeconds + videoPart.value.startSeconds
            val endSeconds = startRecordCoreSeconds + videoPart.value.endSeconds
            jsonPath.put("name", name)
            jsonPath.put("start", startSeconds)
            jsonPath.put("end", endSeconds)
            arr.put(jsonPath)
        }

        try {
            val file = File(saveDirPath, "videos.json")
            val output = BufferedWriter(FileWriter(file))
            output.write(arr.toString())
            output.close()

            file.absolutePath
        } catch (e: Exception) {
            VisionLogger.e(TAG, "Can not create Json file : " + e.localizedMessage)
        }
    }
}
