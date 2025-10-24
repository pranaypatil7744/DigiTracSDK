package com.example.digitracksdk.utils

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.services.rekognition.AmazonRekognition
import com.amazonaws.services.rekognition.AmazonRekognitionClient
import com.amazonaws.services.rekognition.model.BoundingBox
import com.amazonaws.services.rekognition.model.CompareFacesMatch
import com.amazonaws.services.rekognition.model.CompareFacesRequest
import com.amazonaws.services.rekognition.model.Image
import com.amazonaws.util.IOUtils
import com.example.digitracksdk.Constant
import com.example.digitracksdk.R
import java.io.File
import java.io.FileInputStream
import java.nio.ByteBuffer

class FaceRecognitionUtils(private val mContext: Context) {


    fun compareFaces(sourceImage: File, targetImage: File, listener: FaceCallBack) {

        Log.e("Source Size::::", "" + sourceImage.length() / 1024)
        Log.e("Target Size::::", "" + targetImage.length() / 1024)

        val thread = Thread {
            try {
                val similarityThreshold = 70f
                var sourceImageBytes: ByteBuffer? = null
                var targetImageBytes: ByteBuffer? = null
                val rekognitionClient: AmazonRekognition =
                    AmazonRekognitionClient(
                        BasicAWSCredentials(
                            Constant.AwsKeys.aws_access_key, Constant.AwsKeys.aws_secret_key
                        )
                    )

                //Load source and target images and create input parameters
                try {
                    FileInputStream(sourceImage).use { inputStream ->
                        sourceImageBytes = ByteBuffer.wrap(IOUtils.toByteArray(inputStream))
                    }
                } catch (e: Exception) {
                    Toast.makeText(
                        mContext,
                        "Exception:::: Failed to load source image",
                        Toast.LENGTH_LONG
                    ).show()
                }
                try {
                    FileInputStream(targetImage).use { inputStream ->
                        targetImageBytes = ByteBuffer.wrap(IOUtils.toByteArray(inputStream))
                    }
                } catch (e: Exception) {
                    //todo
//                    Toast.makeText(
//                        BaseApplication.mContext,
//                        BaseApplication.mContext.getString(R.string.exception_Failed_to_load_target_image),
//                        Toast.LENGTH_LONG
//                    ).show()
                }
                val source: Image = Image().withBytes(sourceImageBytes)
                val target: Image = Image().withBytes(targetImageBytes)
                val request = CompareFacesRequest().withSourceImage(source).withTargetImage(target)
                    .withSimilarityThreshold(similarityThreshold)

                // Call operation
                val compareFacesResult = rekognitionClient.compareFaces(request)


                // Display results
                val faceDetails = compareFacesResult.faceMatches

                var matched = false

                for (match: CompareFacesMatch in faceDetails) {
                    val face = match.face
                    val position: BoundingBox = face.boundingBox
                    println("Face at " + position.left.toString() + " " + position.top.toString() + " matches with " + face.confidence.toString() + "% confidence.")
                    matched = face.confidence >= 90
                    if (face.confidence >= 90) break
                }

                listener.call(matched)
            } catch (e: java.lang.Exception) {
                listener.call(false)
                e.printStackTrace()
            }
        }
        thread.start()
    }

    interface FaceCallBack {
        fun call(boolean: Boolean)
    }

}