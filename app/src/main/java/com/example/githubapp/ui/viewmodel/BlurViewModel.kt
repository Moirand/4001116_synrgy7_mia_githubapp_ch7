package com.example.githubapp.ui.viewmodel

import android.app.Application
import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.example.githubapp.R
import com.example.githubapp.ui.BlurFragment.Companion.KEY_IMAGE_URI
import com.example.githubapp.worker.BlurWorker
import com.example.githubapp.worker.CleanupWorker
import com.example.githubapp.worker.SaveImageToFileWorker
import com.example.githubapp.worker.TAG_OUTPUT

class BlurViewModel(application: Application): ViewModel() {
    companion object {
        private const val IMAGE_MANIPULATION_WORK_NAME = "image_manipulation_work"
    }

    private var imageUri: Uri? = null
    internal var outputUri: Uri? = null
    private val workManager = WorkManager.getInstance(application)
    internal val outputWorkInfos: LiveData<List<WorkInfo>> = workManager.getWorkInfosByTagLiveData(TAG_OUTPUT)

    init {
        imageUri = getImageUri(application.applicationContext)
    }

    fun setImageUri(imageUri: Uri) {
        this.imageUri = imageUri
    }
    private fun createInputDataForUri(): Data {
        val builder = Data.Builder()
        imageUri?.let {
            builder.putString(KEY_IMAGE_URI, imageUri.toString())
        }
        return builder.build()
    }
    private fun uriOrNull(uriString: String?): Uri? =
        if (uriString.isNullOrEmpty()) null else Uri.parse(uriString)
    private fun getImageUri(context: Context): Uri =
        Uri.Builder()
            .scheme(ContentResolver.SCHEME_ANDROID_RESOURCE)
            .authority(context.resources.getResourcePackageName(R.drawable.ic_launcher_background))
            .appendPath(context.resources.getResourceTypeName(R.drawable.ic_launcher_background))
            .appendPath(context.resources.getResourceEntryName(R.drawable.ic_launcher_background))
            .build()
    internal fun setOutputUri(outputImageUri: String?) {
        outputUri = uriOrNull(outputImageUri)
    }
    internal fun cancelWork() {
        workManager.cancelUniqueWork(IMAGE_MANIPULATION_WORK_NAME)
    }
    internal fun applyBlur(blurLevel: Int) {
        var continuation = workManager.beginUniqueWork(
            IMAGE_MANIPULATION_WORK_NAME,
            ExistingWorkPolicy.REPLACE,
            OneTimeWorkRequest.from(CleanupWorker::class.java)
        )

        for (i in 0 until blurLevel) {
            val blurBuilder = OneTimeWorkRequestBuilder<BlurWorker>()
            if (i == 0) {
                blurBuilder.setInputData(createInputDataForUri())
            }
            continuation = continuation.then(blurBuilder.build())
        }

        val constraints = Constraints.Builder()
            .setRequiresCharging(true)
            .build()

        val save = OneTimeWorkRequestBuilder<SaveImageToFileWorker>()
            .setConstraints(constraints)
            .addTag(TAG_OUTPUT)
            .build()
        continuation = continuation.then(save)
        continuation.enqueue()
    }
}