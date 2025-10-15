package com.example.digitracksdk.presentation.attendance.attendance_camera

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageFormat
import android.graphics.SurfaceTexture
import android.hardware.Camera
import android.hardware.camera2.*
import android.hardware.camera2.CameraCaptureSession.CaptureCallback
import android.media.Image
import android.media.ImageReader
import android.media.ImageReader.OnImageAvailableListener
import android.os.*
import android.util.Size
import android.util.SparseIntArray
import android.view.Surface
import android.view.SurfaceHolder
import android.view.TextureView.SurfaceTextureListener
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.core.app.ActivityCompat
import com.developers.imagezipper.ImageZipper
import com.example.digitracksdk.utils.FaceRecognitionUtils
import com.example.digitracksdk.utils.ImageUtils
import com.example.digitracksdk.utils.PermissionUtils
import com.example.digitracksdk.utils.PreferenceUtils
import com.example.digitracksdk.Constant
import com.example.digitracksdk.utils.AppUtils
import com.innov.digitrac.base.BaseActivity
import com.innov.digitrac.databinding.ActivityAttendanceCameraBinding
import com.innov.digitrac.utils.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.*
import java.util.*


class AttendanceCameraActivity : BaseActivity(),
    FaceRecognitionUtils.FaceCallBack {
    lateinit var binding: ActivityAttendanceCameraBinding
    lateinit var preferenceUtils: PreferenceUtils

    private var surfaceHolder: SurfaceHolder? = null
    private var camera: Camera? = null

    private var cameraId: String = ""
    private var cameraDevice: CameraDevice? = null
    private var cameraCaptureSessions: CameraCaptureSession? = null
    private var captureRequestBuilder: CaptureRequest.Builder? = null
    private var imageDimension: Size? = null
    private var file: File? = null
    private var mBackgroundHandler: Handler? = null
    private var imageSaved = false
    private var imageCaptured = false

    private val ORIENTATIONS = SparseIntArray()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        preferenceUtils = PreferenceUtils(this)
        binding = ActivityAttendanceCameraBinding.inflate(layoutInflater)
        setContentView(binding.root)
        AppUtils.INSTANCE?.systemBarPadding(binding.root)
        getPermission()
        setUpView()
        setUpData()
        setUpListener()

        CoroutineScope(Dispatchers.Main).launch{
            delay(2000)
            openCamera()
        }
    }

    private fun setUpData() {
        ORIENTATIONS.append(Surface.ROTATION_0, 90)
        ORIENTATIONS.append(Surface.ROTATION_90, 0)
        ORIENTATIONS.append(Surface.ROTATION_180, 270)
        ORIENTATIONS.append(Surface.ROTATION_270, 180)
    }

    private fun setUpListener() {
        binding.apply {
            btnCapture.setOnClickListener {
//                if (camera != null) {
                takePicture()
//                    camera!!.takePicture(null, null, this@AttendanceCameraActivity)
//                }
            }
        }
    }

    var textureListener: SurfaceTextureListener = object : SurfaceTextureListener {
        override fun onSurfaceTextureAvailable(surface: SurfaceTexture, width: Int, height: Int) {
            //open your camera here
            openCamera()
        }

        override fun onSurfaceTextureSizeChanged(surface: SurfaceTexture, width: Int, height: Int) {
            // Transform you image captured size according to the surface width and height
        }

        override fun onSurfaceTextureDestroyed(surface: SurfaceTexture): Boolean {
            return false
        }

        override fun onSurfaceTextureUpdated(surface: SurfaceTexture) {}
    }

    private fun setUpView() {
        binding.apply {
            surfaceCamera.surfaceTextureListener = textureListener
        }
    }

    val captureListener: CaptureCallback = object : CaptureCallback() {
        override fun onCaptureCompleted(
            session: CameraCaptureSession,
            request: CaptureRequest,
            result: TotalCaptureResult
        ) {
            super.onCaptureCompleted(session, request, result)
            //                    Toast.makeText(AndroidCameraApi.this, "Saved:" + file, Toast.LENGTH_SHORT).show();
            imageCaptured = true
            if (imageSaved) {
                compressImage(file)
            }
        }
    }

    private val stateCallback: CameraDevice.StateCallback = object : CameraDevice.StateCallback() {
        override fun onOpened(camera: CameraDevice) {
            //This is called when the camera is open
            cameraDevice = camera
            createCameraPreview()
        }

        override fun onDisconnected(camera: CameraDevice) {
            cameraDevice!!.close()
        }

        override fun onError(camera: CameraDevice, error: Int) {
            cameraDevice!!.close()
            cameraDevice = null
        }
    }


    private fun createCameraPreview() {
        try {
            val texture: SurfaceTexture = binding.surfaceCamera.surfaceTexture!!
            texture.setDefaultBufferSize(imageDimension!!.width, imageDimension!!.height)
            val surface = Surface(texture)
            captureRequestBuilder =
                cameraDevice!!.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)
            captureRequestBuilder!!.addTarget(surface)
            cameraDevice!!.createCaptureSession(
                Arrays.asList<Surface>(surface),
                object : CameraCaptureSession.StateCallback() {
                    override fun onConfigured(cameraCaptureSession: CameraCaptureSession) {
                        //The camera is already closed
                        if (null == cameraDevice) {
                            return
                        }
                        // When the session is ready, we start displaying the preview.
                        cameraCaptureSessions = cameraCaptureSession
                        updatePreview()
                    }

                    override fun onConfigureFailed(cameraCaptureSession: CameraCaptureSession) {
                        Toast.makeText(
                            this@AttendanceCameraActivity,
                            "Configuration change",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                },
                null
            )
        } catch (e: CameraAccessException) {
            e.printStackTrace()
        }
    }

    private fun compressImage(file: File?) {
        val bundle = Bundle()
        bundle.putString(Constant.IntentExtras.EXTRA_FILE_NAME, file?.name)
        bundle.putString(Constant.IntentExtras.EXTRA_FILE_PATH, file?.absolutePath)
        val resultIntent = intent
        resultIntent.putExtras(bundle)
        setResult(Activity.RESULT_OK, resultIntent)
        finish()
//        file?.let { saveImage(file) }
    }

    private fun updatePreview() {
        captureRequestBuilder!!.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO)
        try {
            cameraCaptureSessions!!.setRepeatingRequest(
                captureRequestBuilder!!.build(),
                null,
                mBackgroundHandler
            )
        } catch (e: CameraAccessException) {
            e.printStackTrace()
        }
    }

    private fun takePicture() {
        if (null == cameraDevice) {
            return
        }
        val manager = getSystemService(CAMERA_SERVICE) as CameraManager
        try {
            val characteristics = manager.getCameraCharacteristics(cameraDevice?.id ?: "")
            var jpegSizes: Array<Size>? = null
            if (characteristics != null) {
                jpegSizes =
                    characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)!!
                        .getOutputSizes(ImageFormat.JPEG)
            }
            var width = 640
            var height = 480
            if (jpegSizes != null && jpegSizes.isNotEmpty()) {
                width = jpegSizes[0].width
                height = jpegSizes[0].height
            }
            val reader = ImageReader.newInstance(width, height, ImageFormat.JPEG, 1)
            val outputSurfaces: MutableList<Surface> = ArrayList(2)
            outputSurfaces.add(reader.surface)
            outputSurfaces.add(Surface(binding.surfaceCamera.surfaceTexture))
            val captureBuilder: CaptureRequest.Builder? =
                cameraDevice?.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE)
            captureBuilder?.apply {

                addTarget(reader.surface)
                set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO)
                // Orientation
                val rotation = windowManager.defaultDisplay.orientation
                set<Int>(
                    CaptureRequest.JPEG_ORIENTATION,
                    ORIENTATIONS.get(rotation)
                )
            }

            file = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {

                File.createTempFile(
                    "Punch",
                    ".jpg",
                       this.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
                    )
               /* File(
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                        .toString() + "/Punch.jpg"
                )*/
            } else {
                File(Environment.getExternalStorageDirectory().toString() + "/Punch.jpg")
            }

            val readerListener: OnImageAvailableListener = object : OnImageAvailableListener {

                override fun onImageAvailable(reader: ImageReader) {
                    var image: Image? = null
                    var bytes: ByteArray? = null
                    try {
                        image = reader.acquireLatestImage()
                        val buffer = image.planes[0].buffer
                        bytes = ByteArray(buffer.capacity())
                        buffer[bytes]
                        save(bytes)
                    } catch (e: FileNotFoundException) {
                        e.printStackTrace()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    } finally {
                        image?.close()
                        imageSaved = true
                        if (imageCaptured) {
                            bytes?.let { compressImage(file) }

                        }
                    }
                }

                @Throws(IOException::class)
                private fun save(bytes: ByteArray) {
                    if (!file?.exists()!!) {
                        file?.createNewFile()
                    }
                    FileOutputStream(file).use { output -> output.write(bytes) }
                }
            }

            reader.setOnImageAvailableListener(readerListener, mBackgroundHandler)


            cameraDevice?.createCaptureSession(
                outputSurfaces,
                object : CameraCaptureSession.StateCallback() {
                    override fun onConfigured(session: CameraCaptureSession) {
                        try {
                            session.capture(
                                captureBuilder!!.build(),
                                captureListener,
                                mBackgroundHandler
                            )
                        } catch (e: CameraAccessException) {
                            e.printStackTrace()
                        }
                    }

                    override fun onConfigureFailed(session: CameraCaptureSession) {}
                },
                mBackgroundHandler
            )
        } catch (e: CameraAccessException) {
            e.printStackTrace()
        }
    }

    override fun onResume() {
        binding.apply {
            if (surfaceCamera.isAvailable) {
                openCamera()
            } else {
                surfaceCamera.surfaceTextureListener = textureListener
            }
        }
        super.onResume()
    }

    private fun openCamera() {
        val manager = getSystemService(CAMERA_SERVICE) as CameraManager
        try {
//            cameraId = manager.getCameraIdList()[0];
            cameraId = "1"
            val characteristics = manager.getCameraCharacteristics(cameraId)
            val map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)!!
            imageDimension = map.getOutputSizes(SurfaceTexture::class.java)[0]
            // Add permission for camera and let user grant the permission
            /*  if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(AndroidCameraApi.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CAMERA_PERMISSION);
                return;
            }*/if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.CAMERA
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return
            }
            manager.openCamera(cameraId, stateCallback, null)
        } catch (e: CameraAccessException) {
            e.printStackTrace()
        }
    }

    private fun resetCamera() {
        if (surfaceHolder!!.surface == null) {
            // Return if preview surface does not exist
            return
        }

        // Stop if preview surface is already running.
        camera!!.stopPreview()
        try {
            // Set preview display
            camera!!.setPreviewDisplay(surfaceHolder)
        } catch (e: IOException) {
            e.printStackTrace()
        }

        // Start the camera preview...
        camera!!.startPreview()
    }

    private fun releaseCamera() {
        camera!!.stopPreview()
        camera!!.release()
        camera = null
    }

    private fun saveImage(file: File) {
        try {
//            val fileName = "Attendance" + System.currentTimeMillis() + ".jpg"
//            val file = createFile(bytes, fileName)

            val sourceFile = profileImage()

            showLoader(true)
            FaceRecognitionUtils(this@AttendanceCameraActivity).compareFaces(
                sourceFile,
                file,
                this@AttendanceCameraActivity
            )

        } catch (e: FileNotFoundException) {
            showToast(e.message.toString())
            setResult(Activity.RESULT_CANCELED, intent)
            finish()
            e.printStackTrace()
        } catch (e: IOException) {
            showToast(e.message.toString())
            setResult(Activity.RESULT_CANCELED, intent)
            finish()
            e.printStackTrace()
        }
    }


    //    private fun saveImage(file: File) {
//        try {
////            val fileName = "Attendance" + System.currentTimeMillis() + ".jpg"
////            val file = createFile(bytes, fileName)
//            val sourceFile = profileImage()
//            pictureTakenFile = file
//            FaceRecognitionUtils(this@AndroidCameraApi).compareFaces(
//                sourceFile,
//                file,
//                this@AndroidCameraApi
//            )
//        } catch (e: Exception) {
//            InnovUtils.showToastMessage(this, e.message, "S")
//            setResult(RESULT_CANCELED, intent)
//            finish()
//            e.printStackTrace()
//        }
//    }
    private fun showLoader(show: Boolean) {
        toggleFadeView(
            binding.root,
            binding.contentLoading.root,
            binding.contentLoading.imageLoading,
            show
        )
    }

    private fun getPermission() {
        if (PermissionUtils.getCameraPermission(this)) {
            preferenceUtils.setValue(Constant.AskedPermission.CAMERA_PERMISSION_COUNT, 0)
        } else {
            PermissionUtils.requestCameraPermissions(this)
        }
    }

   /* override fun surfaceCreated(p0: SurfaceHolder) {
//        val rotation = windowManager
//            .defaultDisplay.rotation
//        when (rotation) {
//            Surface.ROTATION_0, Surface.ROTATION_180 -> {
//                // Log.i(TAG, "0");
//                camera?.setDisplayOrientation(90)
//            }
//            Surface.ROTATION_90 -> {
//                // Log.i(TAG, "90");
//                camera?.setDisplayOrientation(0)
//            }
//            Surface.ROTATION_270 -> {
//                // Log.i(TAG, "270");
//                camera?.setDisplayOrientation(180)
//            }
//        }
        camera = Camera.open(1)
        camera!!.setDisplayOrientation(90)
        try {
            camera!!.setPreviewDisplay(surfaceHolder)
            val parameters = camera!!.parameters
            parameters.setPictureSize(640, 480)
            parameters.setRotation(270)
            camera!!.parameters = parameters
            camera!!.startPreview()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    override fun surfaceChanged(p0: SurfaceHolder, p1: Int, p2: Int, p3: Int) {
        resetCamera()
    }

    override fun surfaceDestroyed(p0: SurfaceHolder) {
        releaseCamera()
    }

    override fun onPictureTaken(p0: ByteArray?, p1: Camera?) {
        if (p0 != null) {
//            saveImage(p0)
        }
    }*/

    private fun profileImage(): File {
        val sourceBitmap =
            ImageUtils.INSTANCE?.stringToBitMap(preferenceUtils.getValue(Constant.PreferenceKeys.PROFILE_PIC))
        val bos = ByteArrayOutputStream()
        sourceBitmap?.compress(Bitmap.CompressFormat.JPEG, 100 /*ignored for PNG*/, bos)
        val bitmapData = bos.toByteArray()
        return createFile(bitmapData, "Profile.jpg")
    }


    private fun createFile(bitmapData: ByteArray, fileName: String): File {
        val f = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
            fileName
        )
        f.createNewFile()

        val fos = FileOutputStream(f)
        fos.write(bitmapData)
        fos.flush()
        fos.close()

        return ImageZipper(this).setQuality(30).setMaxWidth(300).setMaxHeight(700).compressToFile(f)
    }

    override fun call(b: Boolean) {
        if (b) {
            runOnUiThread {
                showToast("Face matched..")
            }

            val bundle = Bundle()
            bundle.putString(Constant.IntentExtras.EXTRA_FILE_NAME, file?.name)
            bundle.putString(Constant.IntentExtras.EXTRA_FILE_PATH, file?.absolutePath)
            val resultIntent = intent
            resultIntent.putExtras(bundle)
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        } else {
            runOnUiThread {
                showToast("Face did not match. Please try again.")
            }
            // todo match status api
//                        resetCamera()
            val bundle = Bundle()
            bundle.putString(Constant.IntentExtras.EXTRA_FILE_NAME, file?.name)
            bundle.putString(Constant.IntentExtras.EXTRA_FILE_PATH, file?.absolutePath)
            val resultIntent = intent
            resultIntent.putExtras(bundle)
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }

    }

}



