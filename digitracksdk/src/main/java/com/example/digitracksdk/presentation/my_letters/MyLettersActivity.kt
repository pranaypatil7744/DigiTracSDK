package com.example.digitracksdk.presentation.my_letters

import android.content.Intent
import android.os.Bundle
import android.view.View.VISIBLE
import androidx.activity.enableEdgeToEdge
import com.example.digitracksdk.Constant
import com.innov.digitrac.R
import com.innov.digitrac.base.BaseActivity
import com.innov.digitrac.databinding.ActivityMyLettersBinding
import com.example.digitracksdk.domain.model.GnetIdRequestModel
import com.example.digitracksdk.presentation.my_letters.adapter.MyLettersAdapter
import com.example.digitracksdk.presentation.my_letters.candidate_loi.CandidateLoiActivity
import com.example.digitracksdk.presentation.my_letters.form_16.Form16Activity
import com.example.digitracksdk.presentation.my_letters.model.MyLettersModel
import com.example.digitracksdk.presentation.my_letters.offer_letter.OfferLettersActivity
import com.example.digitracksdk.presentation.my_letters.other_letter.OtherLetterActivity
import com.example.digitracksdk.presentation.my_letters.other_letter.OtherLetterViewModel
import com.example.digitracksdk.utils.AppUtils
import com.example.digitracksdk.utils.PreferenceUtils
import com.example.digitracksdk.utils.isNetworkAvailable
import org.koin.android.viewmodel.ext.android.viewModel

class MyLettersActivity : BaseActivity(), MyLettersAdapter.MyLettersClickManager {
    lateinit var binding: ActivityMyLettersBinding
    lateinit var myLettersAdapter: MyLettersAdapter
    private var myLettersList: ArrayList<MyLettersModel> = ArrayList()
    lateinit var preferenceUtils: PreferenceUtils
    private val otherLetterViewModel: OtherLetterViewModel by viewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMyLettersBinding.inflate(layoutInflater)
        preferenceUtils = PreferenceUtils(this)
        setContentView(binding.root)
        AppUtils.INSTANCE?.systemBarPadding(binding.main)

        setUpToolbar()
        setUpMyLettersData()
        setUpObserver()
    }

    private fun setUpObserver() {
        binding.apply {
            with(otherLetterViewModel) {
                getIncrementLettersResponseData.observe(this@MyLettersActivity) {
                    if (it.Status == Constant.SUCCESS && it.ImageArr!=null) {
                        val file = it.ImageArr?.let { it1 ->
                            com.example.digitracksdk.utils.ImageUtils.INSTANCE?.writePDFToFile(it1, "Increment Letter")
                        }
                        file?.let { file->
                            com.example.digitracksdk.utils.ImageUtils.INSTANCE?.openPdfFile(
                                this@MyLettersActivity,
                                file.absolutePath.toString()
                            )
                        }

                    } else {
                        showToast(it.Message.toString())
                    }
                }

                showProgress.observe(this@MyLettersActivity){
                    toggleLoader(it)

                }
                messageData.observe(this@MyLettersActivity){
                    showToast(it.toString())
                }
            }
        }
    }

    private fun setUpMyLettersData() {
        myLettersList.clear()
        myLettersList.add(
            MyLettersModel(
                itemName = getString(R.string.candidate_loi),
                itemIcon = R.drawable.ic_loi
            )
        )
        myLettersList.add(
            MyLettersModel(
                itemName = getString(R.string.offer_letter),
                itemIcon = R.drawable.ic_offer_letter
            )
        )
        myLettersList.add(
            MyLettersModel(
                itemName = getString(R.string.other_letter),
                itemIcon = R.drawable.ic_other_letters
            )
        )
        myLettersList.add(
            MyLettersModel(
                itemName = getString(R.string.form_16),
                itemIcon = R.drawable.ic_offer_letter
            )
        )
        myLettersList.add(
            MyLettersModel(
                itemName = getString(R.string.increment_letter),
                itemIcon = R.drawable.ic_loi
            )
        )
        setUpMyLettersAdapter()
    }

    private fun setUpMyLettersAdapter() {
        myLettersAdapter = MyLettersAdapter(this, myLettersList, this)
        binding.recyclerMyLetters.adapter = myLettersAdapter
    }

    private fun setUpToolbar() {
        binding.toolbar.apply {
            tvTitle.text = getString(R.string.my_letters)
            divider.visibility = VISIBLE
            btnBack.setOnClickListener {
                finish()
            }
        }
    }

    override fun onClickItems(position: Int) {
        when (myLettersList[position].itemName) {
            getString(R.string.candidate_loi) -> {
                startActivity(Intent(this, CandidateLoiActivity::class.java))
            }
            getString(R.string.offer_letter) -> {
                startActivity(Intent(this, OfferLettersActivity::class.java))
            }
            getString(R.string.other_letter) -> {
                startActivity(Intent(this, OtherLetterActivity::class.java))
            }
            getString(R.string.form_16) -> {
                startActivity(Intent(this, Form16Activity::class.java))
            }
            getString(R.string.increment_letter) -> {
                callIncrementLettersApi()
            }
        }
    }

    fun toggleLoader(showLoader: Boolean) {
        toggleFadeView(
            binding.root,
            binding.layoutLoading.root,
            binding.layoutLoading.imageLoading,
            showLoader
        )
    }

    private fun callIncrementLettersApi() {
        binding.apply {
            if (isNetworkAvailable()) {
                toggleLoader(true)
                otherLetterViewModel.callGetIncrementLettersApi(
                    request = GnetIdRequestModel(
                        GNETAssociateId = preferenceUtils.getValue(Constant.PreferenceKeys.GnetAssociateID)
                    )
                )
            } else {
                showToast(getString(R.string.no_internet_connection))
            }
        }
    }
}