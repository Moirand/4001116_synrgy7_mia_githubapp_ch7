package com.example.githubapp.ui

import android.Manifest.permission.CAMERA
import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import com.example.githubapp.databinding.FragmentProfileBinding
import java.io.File

class ProfileFragment : Fragment() {
    private val binding by lazy { FragmentProfileBinding.inflate(layoutInflater) }
    private lateinit var uri: Uri
    private val permissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions(),
        ::handlePermissionResult,
    )

    private val galleryResult = registerForActivityResult(
        ActivityResultContracts.GetContent(),
        ::handleGalleryResult,
    )

    private val cameraResult = registerForActivityResult(
        ActivityResultContracts.TakePicture(),
        ::handleCameraResult,
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = binding.root
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnChangeProfilePicture.setOnClickListener {
            if (isWriteExternalStorageGranted() && isReadExternalStorageGranted() && isCameraGranted()) {
                chooseImageDialog()
            } else {
                askPermission()
            }
        }
    }

    private fun handlePermissionResult(result: Map<String, Boolean>) {
        if (result.containsValue(false)) {
            Toast.makeText(requireContext(), "permission ditolak", Toast.LENGTH_SHORT).show()
            requireActivity().onBackPressed()
        } else {
            Toast.makeText(requireContext(), "permission diterima", Toast.LENGTH_SHORT).show()
        }
    }
    private fun askPermission() {
        permissionRequest.launch(
            arrayOf(
                WRITE_EXTERNAL_STORAGE,
                READ_EXTERNAL_STORAGE,
                CAMERA,
            )
        )
    }
    private fun isWriteExternalStorageGranted(): Boolean =
        ContextCompat.checkSelfPermission(
            requireContext(),
            WRITE_EXTERNAL_STORAGE
        ) == PERMISSION_GRANTED
    private fun isReadExternalStorageGranted(): Boolean =
        ContextCompat.checkSelfPermission(
            requireContext(),
            READ_EXTERNAL_STORAGE
        ) == PERMISSION_GRANTED
    private fun isCameraGranted(): Boolean =
        ContextCompat.checkSelfPermission(requireContext(), CAMERA) == PERMISSION_GRANTED
    private fun handleGalleryResult(result: Uri?) {
        binding.ivProfile.loadImageUrl(requireContext(), result)
    }
    private fun handleCameraResult(result: Boolean) {
        if (result) {
            binding.ivProfile.loadImageUrl(requireContext(), uri)
        }
    }
    private fun chooseImageDialog() {
        AlertDialog.Builder(requireContext())
            .setMessage("Pilih Gambar")
            .setPositiveButton("Gallery") { _, _ -> openGallery() }
            .setNegativeButton("Camera") { _, _ -> openCamera() }
            .show()
    }
    private fun openGallery() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        galleryResult.launch("image/*")
    }
    private fun openCamera() {
        val photoFile = File.createTempFile(
            "SYNRGY_",
            ".jpg",
            requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        )

        uri = FileProvider.getUriForFile(
            requireContext(),
            "com.example.githubapp.provider",
            photoFile
        )
        cameraResult.launch(uri)
    }
}