package com.example.digitracksdk.presentation.home.jobs_and_refer_fds.submit_document

import android.app.Activity
import android.content.ContentUris
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.innov.digitrac.R
import com.innov.digitrac.databinding.ActivitySubmitDocumentsBinding
import com.example.digitracksdk.utils.AppUtils
import java.io.File


class SubmitDocumentsActivity : AppCompatActivity() {
    lateinit var binding:ActivitySubmitDocumentsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySubmitDocumentsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        AppUtils.INSTANCE?.systemBarPadding(binding.root)

        setUpToolbar()
        setUpListener()
    }
    private fun setUpListener() {
        binding.apply {
            btnSubmit.setOnClickListener {
                finish()
            }
            btnUploadFile.setOnClickListener {
                val i = Intent()
                i.action = Intent.ACTION_GET_CONTENT
                i.type = "application/pdf"
                resultPickPdf.launch(i)
            }
            btnDelete.setOnClickListener {
                etUploadImage.setText("")
                btnDelete.visibility = GONE
            }
        }
    }

    var resultPickPdf = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){result->
        if (result.resultCode == Activity.RESULT_OK){
            // Get the Uri of the selected file
            // Get the Uri of the selected file
            val uri: Uri? = result.data?.data
            val uriString: String = uri.toString()
            val myFile = File(uriString)
            val path: String = myFile.absolutePath
            var displayName: String? = null

            if (uriString.startsWith("content://")) {
//                val name = getPDFPath(uri)
//                binding.etUploadImage.setText(name?.split("/")?.last())
                binding.etUploadImage.setText(getString(R.string.document_pdf))
            } else if (uriString.startsWith("file://")) {
                displayName = myFile.name
                binding.etUploadImage.setText(displayName)

            }
            binding.btnDelete.visibility = VISIBLE
        }
    }
    fun getPDFPath(uri: Uri?): String? {
        val id = DocumentsContract.getDocumentId(uri)
        val contentUri = ContentUris.withAppendedId(
            Uri.parse("content://downloads/public_downloads"), java.lang.Long.valueOf(id)
        )
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = contentResolver.query(contentUri, projection, null, null, null)
        val column_index = cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor.moveToFirst()
        return cursor.getString(column_index)
    }

    private fun setUpToolbar() {
        binding.toolbar.apply {
            tvTitle.text = getString(R.string.submit_documents)
            btnBack.setOnClickListener {
                finish()
            }
            divider.visibility = View.VISIBLE
        }
    }
}