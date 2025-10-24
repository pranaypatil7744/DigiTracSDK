package com.example.digitracksdk.presentation.my_letters.other_letter

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.activity.enableEdgeToEdge
import com.example.digitracksdk.Constant
import com.example.digitracksdk.R
import com.example.digitracksdk.base.BaseActivity
import com.example.digitracksdk.databinding.ActivityOtherLetterBinding
import com.example.digitracksdk.domain.model.my_letters.AssociateAllLettersListModel
import com.example.digitracksdk.domain.model.my_letters.OtherLettersRequestModel
import com.example.digitracksdk.presentation.my_letters.offer_letter.adapter.OtherLetterAdapter
import com.example.digitracksdk.utils.AppUtils
import com.example.digitracksdk.utils.PreferenceUtils
import com.example.digitracksdk.utils.isNetworkAvailable
import org.koin.android.viewmodel.ext.android.viewModel

class OtherLetterActivity : BaseActivity(), OtherLetterAdapter.OtherLetterManager {
    lateinit var binding: ActivityOtherLetterBinding
    lateinit var preferenceUtils: PreferenceUtils
    private val otherLetterViewModel: OtherLetterViewModel by viewModel()
    private var otherLettersList: ArrayList<AssociateAllLettersListModel> =
        kotlin.collections.ArrayList()
    lateinit var otherLettersAdapter: OtherLetterAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        preferenceUtils = PreferenceUtils(this)
        binding = ActivityOtherLetterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        AppUtils.INSTANCE?.systemBarPadding(binding.root)

        setUpToolbar()
        setUpListener()
        setUpObserver()
        callOtherLettersApi()
        setUpAdapter()
    }

    private fun setUpObserver() {
        binding.apply {
            with(otherLetterViewModel) {
                otherLetterResponseData.observe(this@OtherLetterActivity) {
                    if (it.Status == Constant.success) {
                        otherLettersList.clear()
                        if (it.AssociateAllLetterslist!!.isNotEmpty()){
                            otherLettersList.addAll(it.AssociateAllLetterslist ?: arrayListOf())
                            showNoDataLayout(false)
                            otherLettersAdapter.notifyDataSetChanged()
                        }
                        else {
                            showNoDataLayout(true)
                        }

                    } else{
                        showNoDataLayout(true)
                        showToast(it.Message.toString())
                    }
                }
                messageData.observe(this@OtherLetterActivity) {
                    showNoDataLayout(true)
                    toggleLoader(false)
                    showToast(it.toString())
                }
            }
        }
    }

    private fun setUpAdapter() {
        binding.apply {
            otherLettersAdapter = OtherLetterAdapter(
                this@OtherLetterActivity,
                otherLettersList,
                this@OtherLetterActivity
            )
            recyclerOtherLetter.adapter = otherLettersAdapter
        }
    }

    private fun setUpListener() {
        binding.apply {
            layoutNoInternet.btnTryAgain.setOnClickListener {
                callOtherLettersApi()
            }
            swipeRefreshLayout.setOnRefreshListener {
                swipeRefreshLayout.isRefreshing = false
                callOtherLettersApi()
            }
        }
    }

    private fun callOtherLettersApi() {
        binding.apply {
            if (isNetworkAvailable()) {
                layoutNoInternet.root.visibility = GONE
                otherLetterViewModel.callOtherLetterApi(
                    request = OtherLettersRequestModel(
                        AssociateID = preferenceUtils.getValue(Constant.PreferenceKeys.AssociateID),
                        InnovID = preferenceUtils.getValue(Constant.PreferenceKeys.INNOV_ID)
                    )
                )
            } else {
                layoutNoInternet.root.visibility = VISIBLE
                recyclerOtherLetter.visibility = GONE
            }
            layoutNoData.root.visibility= GONE
        }
    }

    private fun toggleLoader(showLoader: Boolean) {
        toggleFadeView(
            binding.root,
            binding.contentLoading.root,
            binding.contentLoading.imageLoading,
            showLoader
        )
    }

    private fun showNoDataLayout(show:Boolean){
        binding.apply {
            noDataLayout(
                noDataLayout = layoutNoData.root,
                recyclerView = recyclerOtherLetter,
                show = show
            )
        }    }
    private fun setUpToolbar() {
        binding.toolbar.apply {
            tvTitle.text = getString(R.string.other_letter)
            btnBack.setOnClickListener {
                finish()
            }
            divider.visibility = VISIBLE
        }
    }

    override fun clickOnDownload(position: Int) {
        val filePath = otherLettersList[position].FilePath
        if (!filePath.isNullOrEmpty()) {
            /* val file = filePath.let { it1 ->
                 ImageUtils.INSTANCE?.writePDFToFile(it1,otherLettersList[position].LetterType.toString())
             }
             ImageUtils.INSTANCE?.openPdfFile(
                 this,
                 file?.absolutePath.toString()
             )*/
            val i = Intent(Intent.ACTION_VIEW)
            i.setDataAndType(Uri.parse(filePath), "application/pdf")
            startActivity(i)

        } else {
            showToast(getString(R.string.no_file_found))
        }
    }
}