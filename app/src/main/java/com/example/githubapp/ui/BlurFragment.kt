package com.example.githubapp.ui

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.Observer
import androidx.work.WorkInfo
import com.example.githubapp.R
import com.example.githubapp.databinding.FragmentBlurBinding
import com.example.githubapp.loadImageUrl
import com.example.githubapp.ui.viewmodel.BlurViewModel
import com.nareshchocha.filepickerlibrary.models.PickMediaConfig
import com.nareshchocha.filepickerlibrary.models.PickMediaType
import com.nareshchocha.filepickerlibrary.ui.FilePicker
import org.koin.androidx.viewmodel.ext.android.viewModel

class BlurFragment : Fragment() {
    companion object {
        const val KEY_IMAGE_URI = "KEY_IMAGE_URI"
    }

    private val filePickerResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult(),
        ::handleFilePickerResult,
    )

    private lateinit var binding: FragmentBlurBinding
    private val viewmodel: BlurViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentBlurBinding.inflate(inflater, container, false).also {
            binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.goButton.setOnClickListener { viewmodel.applyBlur(blurLevel) }

        binding.seeFileButton.setOnClickListener {
            viewmodel.outputUri?.let { currentUri ->
                val actionView = Intent(Intent.ACTION_VIEW, currentUri)
                actionView.resolveActivity(requireActivity().packageManager)?.run {
                    startActivity(actionView)
                }
            }
        }

        binding.cancelButton.setOnClickListener { viewmodel.cancelWork() }

        binding.pickFileButton.setOnClickListener { openFilePicker() }

        viewmodel.outputWorkInfos.observe(viewLifecycleOwner, workInfoObserver())
    }

    private fun workInfoObserver(): Observer<List<WorkInfo>> {
        return Observer { listOfWorkInfo ->
            if (listOfWorkInfo.isEmpty()) {
                return@Observer
            }

            val workInfo = listOfWorkInfo[0]

            if (workInfo.state.isFinished) {
                showWorkFinished()
                val outputImageUri = workInfo.outputData.getString(KEY_IMAGE_URI)

                if (!outputImageUri.isNullOrEmpty()) {
                    viewmodel.setOutputUri(outputImageUri)
                    binding.seeFileButton.visibility = View.VISIBLE
                    binding.imageView.loadImageUrl(requireContext(), Uri.parse(outputImageUri))
                }
            } else {
                showWorkInProgress()
            }
        }
    }

    private fun showWorkInProgress() {
        with(binding) {
            progressBar.visibility = View.VISIBLE
            cancelButton.visibility = View.VISIBLE
            goButton.visibility = View.GONE
            seeFileButton.visibility = View.GONE
        }
    }

    private fun showWorkFinished() {
        with(binding) {
            progressBar.visibility = View.GONE
            cancelButton.visibility = View.GONE
            goButton.visibility = View.VISIBLE
        }
    }

    private val blurLevel: Int
        get() =
            when (binding.radioBlurGroup.checkedRadioButtonId) {
                R.id.radio_blur_lv_1 -> 1
                R.id.radio_blur_lv_2 -> 2
                R.id.radio_blur_lv_3 -> 3
                else -> 1
            }

    private fun openFilePicker() {
        filePickerResult.launch(
            FilePicker.Builder(requireContext())
                .pickMediaBuild(
                    PickMediaConfig(
                        mPickMediaType = PickMediaType.ImageOnly,
                        allowMultiple = false,
                    ),
                ),
        )
    }

    private fun handleFilePickerResult(result: ActivityResult) {
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.data?.let {
                binding.imageView.loadImageUrl(requireContext(), it)
                viewmodel.setImageUri(it)
            }
        }
    }
}