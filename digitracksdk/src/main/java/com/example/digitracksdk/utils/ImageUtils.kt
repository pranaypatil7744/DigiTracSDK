package com.example.digitracksdk.utils

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.graphics.pdf.PdfRenderer
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.os.ParcelFileDescriptor
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.widget.ImageView
import androidx.core.content.FileProvider
import com.blankj.utilcode.util.PathUtils
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.DownsampleStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.digitracksdk.Constant
import com.innov.digitrac.R
import com.squareup.picasso.Picasso
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class ImageUtils {
    companion object {
        var INSTANCE: ImageUtils? = null

        fun setImageInstance() {
            if (INSTANCE == null) {
                INSTANCE = ImageUtils()
            }
        }
    }

    fun stringToBitMap(encodedString: String?): Bitmap? {
        return try {
            val encodeByte =
                Base64.decode(encodedString, Base64.DEFAULT)
            BitmapFactory.decodeByteArray(
                encodeByte, 0,
                encodeByte.size
            )
        } catch (e: java.lang.Exception) {
            e.message
            null
        }
    }

    fun bitMapToString(bitmap: Bitmap): String? {
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos)
        val b = baos.toByteArray()
        return Base64.encodeToString(b, Base64.DEFAULT)
    }

    fun loadLocalGIFImage(imageView: ImageView, image: Int) {
        try {
            imageView.run {
                Glide.with(context)
                    .asGif()
                    .load(image)
                    .override(width, height)
                    .placeholder(R.drawable.loader)
                    .downsample(DownsampleStrategy.CENTER_INSIDE)
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .into(this)
            }
        } catch (e: Exception) {
        }
    }

    fun setCapturedUserImage(imageView: ImageView?, file: File?) {
        imageView?.run {
            Glide.with(context)
                .load(file)
                .into(imageView)
        }
    }

    fun loadBitMap(imageView: ImageView?, bitmap: Bitmap?, placeholder: Drawable? = null) {
        imageView?.context?.let {
            val requestOptions =
                if (placeholder == null) RequestOptions().error(R.drawable.dummy_image_placeholder)
                else RequestOptions().placeholder(placeholder)

            Glide.with(it)
                .load(bitmap)
                .apply(requestOptions)
                .into(imageView)
        }
    }

    fun loadRemoteImage(imageView: ImageView, imageUrl: String) {
//        imageView.context?.let {
//            Glide.with(it)
//                .load(imageUrl)
//                .placeholder(R.drawable.dummy_image_placeholder)
//                .dontAnimate()
//                .into(imageView)
//        }
        imageView.rootView?.let { Glide.with(it) }
            ?.load(imageUrl)
            ?.error(R.drawable.dummy_image_placeholder)
            ?.into(imageView)
    }

    fun loadBannerImage(imageView: ImageView?, imageUrl: String?) {
        imageView?.context?.let {
            Picasso.get()
                .load(imageUrl)
                .into(imageView)
        }
    }

    fun loadProfilePic(imageView: ImageView?, imageUrl: String?) {
        imageView?.context?.let {
            Glide.with(it)
                .load(imageUrl)
                .placeholder(R.drawable.profile_placeholder)
                .into(imageView)
        }
    }

    fun loadLocalImage(imageView: ImageView?, image: File?) {
        try {
            imageView?.run {
                Glide.with(context)
                    .load(image)
                    .override(width, height)
                    .downsample(DownsampleStrategy.CENTER_INSIDE)
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .into(this)
            }
        } catch (e: Exception) {
        }
    }


    fun saveImage(myBitmap: Bitmap, context: Context): String {
        /**
         * Returns absolute Path of Image
         */
        val pictureFile = createFile(context)
        val bytes = ByteArrayOutputStream()
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 30, bytes)
        val byteArray = bytes.toByteArray()
        try {
            val fos = FileOutputStream(pictureFile)
            fos.write(byteArray)
            fos.close()
        } catch (error: Exception) {
            Log.e("Image", "File" + pictureFile.name + "not saved: " + error.message)
        }
        return pictureFile.absolutePath
    }

    fun createFile(context: Context): File {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
        val storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        )
    }

    fun writePDFToFile(imageStr: String, pdfName: String): File? {
        var file: File? = null
        try {

            val fileName = "$pdfName.pdf"
//            file =
//                File(Environment.getStorageDirectory().absolutePath + "/Letter of Intent.pdf")
//            file.createNewFile()
            file = if (android.os.Build.VERSION.SDK_INT >= 29) {
                val filePath: String =
                    PathUtils.getExternalAppDownloadPath().plus("/$fileName")
                File(filePath)
            } else {
                val filePath: String = PathUtils.getExternalDownloadsPath().plus("/$fileName")

                File(filePath)
            }
            val fop = FileOutputStream(file)
            fop.write(Base64.decode(imageStr.toByteArray(), Base64.DEFAULT))
            fop.flush()
            fop.close()
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return file
    }

    fun displayFile(
        file: File,
        imageView: com.github.chrisbanes.photoview.PhotoView,
        context: Context
    ) {
        var mFileDescriptor: ParcelFileDescriptor? = null
        try {

            mFileDescriptor =
                try {
                    context.contentResolver.openFileDescriptor(Uri.fromFile(file), "r", null)
                } catch (e: Exception) {
                    null
                }

            // PdfRenderer enables rendering a PDF document
            var mPdfRenderer: PdfRenderer? = null
            mPdfRenderer = PdfRenderer(mFileDescriptor!!)

            // Open page with specified index
            val mCurrentPage = mPdfRenderer.openPage(0)

            /* int newWidth = (int) (getResources().getDisplayMetrics().widthPixels * mCurrentPage.getWidth() / 72 * currentZoomLevel / 40);
            int newHeight = (int) (getResources().getDisplayMetrics().heightPixels * mCurrentPage.getHeight() / 72 * currentZoomLevel / 64);*/
            val bitmap: Bitmap = Bitmap.createBitmap(
                mCurrentPage.width,
                mCurrentPage.height,
                Bitmap.Config.ARGB_8888
            )

            // Pdf page is rendered on Bitmap
            mCurrentPage.render(
                bitmap, null, null,
                PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY
            )
            // Set rendered bitmap to ImageView (pdfView in my case)
            imageView.setImageBitmap(bitmap)
            mCurrentPage.close()
            mPdfRenderer.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun openPdfFile(context: Context, filePath: String) {
        val file = File(filePath)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            val path = FileProvider.getUriForFile(context, Constant.AUTHORITY, file)
            val i = Intent(Intent.ACTION_VIEW)
            i.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            i.setDataAndType(path, "application/pdf")
            i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            context.startActivity(i)
        } else {
            val path = Uri.fromFile(file)
            val i = Intent(Intent.ACTION_VIEW)
            i.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            i.setDataAndType(path, "application/pdf")
            context.startActivity(i)
        }
    }


    fun getImageUri(context: Context, bitmap: Bitmap): Uri? {
        val contentValues = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, "Innov_Image_${System.currentTimeMillis()}.png")
            put(MediaStore.Images.Media.MIME_TYPE, "image/png")
            put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + "/Innov")
            put(MediaStore.Images.Media.IS_PENDING, 1)
        }

        val contentResolver = context.contentResolver
        val uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

        uri?.let {
            contentResolver.openOutputStream(it)?.use { outputStream ->
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
            }
            contentValues.clear()
            contentValues.put(MediaStore.Images.Media.IS_PENDING, 0)
            contentResolver.update(uri, contentValues, null, null)
        }

        return uri
    }


}