package com.example.digitracksdk.utils

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Color.WHITE
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.provider.MediaStore.Images.Media.getBitmap
import android.text.TextUtils
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageView
import com.canhub.cropper.options
import com.developers.imagezipper.ImageZipper
import com.example.digitracksdk.Constant
import com.example.digitracksdk.R
import com.example.digitracksdk.base.BaseActivity
import com.example.digitracksdk.utils.PermissionUtils.Companion.getCameraPermission
import com.example.digitracksdk.utils.PermissionUtils.Companion.getStoragePermission
import com.example.digitracksdk.utils.PermissionUtils.Companion.requestCameraPermissions
import com.example.digitracksdk.utils.PermissionUtils.Companion.requestStoragePermissions
import java.io.File
import java.util.*


class AddImageUtils : BaseActivity() {

    var isSelfieCamera:Boolean = false
    var isPdf:Boolean = false
    lateinit var preferenceUtils: PreferenceUtils
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        preferenceUtils = PreferenceUtils(this)
        getIntentData()
    }

    private fun getIntentData() {
        intent.extras?.run {
            isPdf = getBoolean(Constant.IS_PDF, false)
            val isCamera: Boolean = getBoolean(Constant.IS_CAMERA, false)
            isSelfieCamera = getBoolean(Constant.IS_SELFIE_CAMERA,false)
            if (isPdf){
                pickPdfFile()
            }else{
                if (isCamera) {
                    captureImage()
                } else {
                    pickImage()
                }
            }
        }
    }

    private fun pickPdfFile() {
        if (getStoragePermission(this)) {
            preferenceUtils.setValue(Constant.AskedPermission.STORAGE_PERMISSION_COUNT,0)
            openPdfPicker()
        } else {
            requestStoragePermissions(this)
        }
    }

    private fun openPdfPicker() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "application/pdf"
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        pickPdfResult.launch(intent)
    }

    private fun pickImage() {
        if (getStoragePermission(this)) {
            preferenceUtils.setValue(Constant.AskedPermission.STORAGE_PERMISSION_COUNT,0)
            openGallery()
        } else {
            requestStoragePermissions(this)
        }
    }

    private fun captureImage() {
        if (getCameraPermission(this)) {
            preferenceUtils.setValue(Constant.AskedPermission.CAMERA_PERMISSION_COUNT,0)
            openCamera()
        } else {
            requestCameraPermissions(this)
        }
    }

    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val file = ImageUtils.INSTANCE?.createFile(this)
        var uri: Uri? = null
        file?.let {
            mCurrentPhotoPath = file.absolutePath
            uri = FileProvider.getUriForFile(
                this,
                Constant.AUTHORITY,
                it
            )
        }
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
        if (isSelfieCamera){
            intent.putExtra("android.intent.extras.CAMERA_FACING", 1)
        }
//        captureImageResult.launch(intent)

        cropCameraImageResult.launch(options {
            setImageSource(includeGallery = false, includeCamera = true)
        })
    }

    private fun openGallery() {
//        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
//        pickImageResult.launch(intent)
        cropCameraImageResult.launch(options {
            setImageSource(includeGallery = true, includeCamera = false)
        })
    }

    private val cropGalleryImageResult = registerForActivityResult(CropImageContract()) { result ->
        if (result.isSuccessful) {
            val bundle = Bundle()
            if (!TextUtils.isEmpty(mCurrentPhotoPath)) {
                bundle.putString(
                    Constant.IntentExtras.EXTRA_FILE_NAME,
                    mCurrentPhotoPath?.split("/")?.last()
                )
                val imageZipperFile = ImageZipper(this)
                    .setQuality(40)
                    .setMaxWidth(300)
                    .setMaxHeight(700)
                    .compressToFile(File(mCurrentPhotoPath.toString()))
                bundle.putString(Constant.IntentExtras.EXTRA_FILE_PATH, imageZipperFile.absolutePath)
            } else {
                val uri: Uri? = result.uriContent
                var imgBitmap: Bitmap? = null
                val contentResolver = contentResolver
                try {
                    imgBitmap = if (Build.VERSION.SDK_INT < 28) {
                        getBitmap(contentResolver, uri)
                    } else {
                        val source: ImageDecoder.Source =
                            ImageDecoder.createSource(contentResolver, uri!!)
                        ImageDecoder.decodeBitmap(source)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                imgBitmap?.let {
                    bundle.putString(Constant.IntentExtras.EXTRA_FILE_NAME, "document.jpeg")
                    bundle.putString(Constant.IntentExtras.EXTRA_FILE_PATH, uri?.path)
                    bundle.putString(
                        Constant.IntentExtras.EXTRA_IMAGE_BIT_MAP,
                        ImageUtils.INSTANCE?.bitMapToString(it)
                    )
                }
            }
            val resultIntent = intent
            resultIntent.putExtras(bundle)
            setResult(Activity.RESULT_OK, resultIntent)
            finish()

        } else {
            setResult(Activity.RESULT_CANCELED, intent)
            finish()
        }
    }

    private val cropCameraImageResult = registerForActivityResult(CropImageContract()) { result ->
        if (result.isSuccessful) {
            var bitmap: Bitmap? = null
            val contentResolver = contentResolver
            try {
                bitmap = if (Build.VERSION.SDK_INT < 28) {
                    getBitmap(contentResolver, result.uriContent)
                } else {
                    val source: ImageDecoder.Source =
                        ImageDecoder.createSource(contentResolver, result.uriContent!!)
                    ImageDecoder.decodeBitmap(source)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            bitmap?.let {
                val imageAbsolutePath: String? =
                    ImageUtils.INSTANCE?.saveImage(it, this)
                imageAbsolutePath?.let {
                    val bundle = Bundle()
                    bundle.putString(Constant.IntentExtras.EXTRA_FILE_NAME, imageAbsolutePath.split("/").last())
                    val imageZipperFile = ImageZipper(this)
                        .setQuality(30)
                        .setMaxWidth(300)
                        .setMaxHeight(700)
                        .compressToFile(File(imageAbsolutePath))
                    bundle.putString(Constant.IntentExtras.EXTRA_FILE_PATH, imageZipperFile.absolutePath)
                    val resultIntent = Intent()
                    resultIntent.putExtras(bundle)
                    setResult(Activity.RESULT_OK,resultIntent)
                    finish()

                } ?: run {
                    setResult(Activity.RESULT_CANCELED, intent)
                    finish()
                }
            }

        } else {
            setResult(Activity.RESULT_CANCELED, intent)
            finish()
        }
    }

    private val captureImageResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                cropCameraImageResult.launch(
                    options(it.data?.data) {
                        setScaleType(CropImageView.ScaleType.FIT_CENTER)
                        setCropShape(CropImageView.CropShape.RECTANGLE)
                        setGuidelines(CropImageView.Guidelines.ON_TOUCH)
                        setAspectRatio(1, 1)
                        setMaxZoom(4)
                        setAutoZoomEnabled(true)
                        setMultiTouchEnabled(true)
                        setCenterMoveEnabled(true)
                        setShowCropOverlay(true)
                        setAllowFlipping(true)
                        setSnapRadius(3f)
                        setTouchRadius(48f)
                        setInitialCropWindowPaddingRatio(0.1f)
                        setBorderLineThickness(3f)
                        setBorderLineColor(Color.argb(170, 255, 255, 255))
                        setBorderCornerThickness(2f)
                        setBorderCornerOffset(5f)
                        setBorderCornerLength(14f)
                        setBorderCornerColor(WHITE)
                        setGuidelinesThickness(1f)
                        setGuidelinesColor(R.color.white)
                        setBackgroundColor(Color.argb(119, 0, 0, 0))
                        setMinCropWindowSize(24, 24)
                        setMinCropResultSize(20, 20)
                        setMaxCropResultSize(99999, 99999)
                        setActivityTitle("")
                        setActivityMenuIconColor(0)
                        setOutputCompressFormat(Bitmap.CompressFormat.JPEG)
                        setOutputCompressQuality(90)
                        setRequestedSize(0, 0)
                        setRequestedSize(0, 0, CropImageView.RequestSizeOptions.RESIZE_INSIDE)
                        setInitialCropWindowRectangle(null)
                        setInitialRotation(0)
                        setAllowCounterRotation(false)
                        setFlipHorizontally(false)
                        setFlipVertically(false)
                        setCropMenuCropButtonTitle(null)
                        setCropMenuCropButtonIcon(0)
                        setAllowRotation(true)
                        setNoOutputImage(false)
                        setFixAspectRatio(false)
                    }
                )
            } else {
                setResult(Activity.RESULT_CANCELED, intent)
                finish()
            }
        }
    private val pickPdfResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        if (it.resultCode == Activity.RESULT_OK){
            val filePath = it.data?.data as Uri
            val fileName = filePath.toString().split("/").last()
            val resultIntent = Intent()
            val bundle = Bundle()
            bundle.putString(Constant.IntentExtras.EXTRA_FILE_PATH, filePath.toString())
            bundle.putString(Constant.IntentExtras.EXTRA_FILE_NAME, if (fileName.contains(".pdf")) fileName else Date().time.toString()+".pdf")
            resultIntent.putExtras(bundle)
            setResult(Activity.RESULT_OK,resultIntent)
            finish()
        }else{
            setResult(Activity.RESULT_CANCELED, intent)
            finish()
        }
    }

    private val pickImageResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it?.resultCode == Activity.RESULT_OK) {
                cropGalleryImageResult.launch(
                    options(it.data?.data) {
                        setScaleType(CropImageView.ScaleType.FIT_CENTER)
                        setCropShape(CropImageView.CropShape.RECTANGLE)
                        setGuidelines(CropImageView.Guidelines.ON_TOUCH)
                        setAspectRatio(1, 1)
                        setMaxZoom(4)
                        setAutoZoomEnabled(true)
                        setMultiTouchEnabled(true)
                        setCenterMoveEnabled(true)
                        setShowCropOverlay(true)
                        setAllowFlipping(true)
                        setSnapRadius(3f)
                        setTouchRadius(48f)
                        setInitialCropWindowPaddingRatio(0.1f)
                        setBorderLineThickness(3f)
                        setBorderLineColor(Color.argb(170, 255, 255, 255))
                        setBorderCornerThickness(2f)
                        setBorderCornerOffset(5f)
                        setBorderCornerLength(14f)
                        setBorderCornerColor(WHITE)
                        setGuidelinesThickness(1f)
                        setGuidelinesColor(R.color.white)
                        setBackgroundColor(Color.argb(119, 0, 0, 0))
                        setMinCropWindowSize(24, 24)
                        setMinCropResultSize(20, 20)
                        setMaxCropResultSize(99999, 99999)
                        setActivityTitle("")
                        setActivityMenuIconColor(0)
                        setOutputCompressFormat(Bitmap.CompressFormat.JPEG)
                        setOutputCompressQuality(90)
                        setRequestedSize(0, 0)
                        setRequestedSize(0, 0, CropImageView.RequestSizeOptions.RESIZE_INSIDE)
                        setInitialCropWindowRectangle(null)
                        setInitialRotation(0)
                        setAllowCounterRotation(false)
                        setFlipHorizontally(false)
                        setFlipVertically(false)
                        setCropMenuCropButtonTitle(null)
                        setCropMenuCropButtonIcon(0)
                        setAllowRotation(true)
                        setNoOutputImage(false)
                        setFixAspectRatio(false)
                    }
                )
            } else {
                setResult(Activity.RESULT_CANCELED, intent)
                finish()
            }
        }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == Constant.PermissionRequestCodes.CAMERA_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults.first() == PackageManager.PERMISSION_GRANTED) {
                openCamera()
            } else {
                val cameraPerCount = preferenceUtils.getIntValue(Constant.AskedPermission.CAMERA_PERMISSION_COUNT,0)
                preferenceUtils.setValue(Constant.AskedPermission.CAMERA_PERMISSION_COUNT,cameraPerCount.plus(1))
                if (preferenceUtils.getIntValue(Constant.AskedPermission.CAMERA_PERMISSION_COUNT,0) >= 2){
                    DialogUtils.showPermissionDialog(this,getString(R.string.please_grant_the_camera_permission_to_continue),getString(R.string.allow_permission),getString(R.string.go_to_settings),getString(R.string.deny))
                }else{
                    AppUtils.INSTANCE?.showLongToast(
                        this,
                        getString(R.string.please_grant_the_camera_permission_to_continue)
                    )
                    finish()
                }
                setResult(Activity.RESULT_CANCELED, intent)
            }
        } else if (requestCode == Constant.PermissionRequestCodes.STORAGE_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults.first() == PackageManager.PERMISSION_GRANTED) {
                if (isPdf){
                    pickPdfFile()
                }else{
                    openGallery()
                }
            } else {
                val galleryPerCount = preferenceUtils.getIntValue(Constant.AskedPermission.STORAGE_PERMISSION_COUNT,0)
                preferenceUtils.setValue(Constant.AskedPermission.STORAGE_PERMISSION_COUNT,galleryPerCount.plus(1))
                if (preferenceUtils.getIntValue(Constant.AskedPermission.STORAGE_PERMISSION_COUNT,0) >= 2){
                    DialogUtils.showPermissionDialog(this,getString(R.string.please_grant_the_storage_permission_to_continue),getString(R.string.allow_permission),getString(R.string.go_to_settings),getString(R.string.deny))
                }else{
                    AppUtils.INSTANCE?.showLongToast(
                        this,
                        getString(R.string.please_grant_the_storage_permission_to_continue)
                    )
                    finish()
                }
                setResult(Activity.RESULT_CANCELED, intent)
            }
        }
    }

}