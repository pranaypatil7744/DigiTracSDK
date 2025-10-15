package com.example.digitracksdk.presentation.home.training.document_fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.innov.digitrac.R
import com.innov.digitrac.base.BaseFragment
import com.innov.digitrac.databinding.FragmentTrainingDocumentBinding
import com.example.digitracksdk.domain.model.training_model.ViewTrainingDocumentRequestModel
import com.example.digitracksdk.presentation.home.training.TrainingViewModel
import com.example.digitracksdk.presentation.home.training.document_fragment.adapter.TrainingDocumentAdapter
import com.example.digitracksdk.presentation.home.training.document_fragment.model.TrainingDocumentModel
import com.example.digitracksdk.utils.isNetworkAvailable
import org.koin.android.viewmodel.ext.android.viewModel

class TrainingDocumentFragment : BaseFragment(), TrainingDocumentAdapter.TrainingClickManager {
    lateinit var binding: FragmentTrainingDocumentBinding
    var selectedItemPosition = 0
    lateinit var trainingDocumentAdapter: TrainingDocumentAdapter
    private val trainingViewModel: TrainingViewModel by viewModel()
    var trainingDocList: ArrayList<TrainingDocumentModel> = ArrayList()

    companion object {
        fun newInstance(): TrainingDocumentFragment {
            return TrainingDocumentFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentTrainingDocumentBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setObserver()
        setUpTrainingAdapter()
    }

    private fun setObserver() {
        binding.apply {
            with(trainingViewModel) {
                viewTrainingDocumentResponseData.observe(this@TrainingDocumentFragment.requireActivity()
                ) {
                    toggleLoader(false)
                    if (!it.ClientTrainingImageArr.isNullOrEmpty()) {
                        val file = it.ClientTrainingImageArr?.let { it1 ->
                            com.example.digitracksdk.utils.ImageUtils.INSTANCE?.writePDFToFile(
                                it1,
                                pdfName = trainingDocList[selectedItemPosition].docName.toString()
                            )
                        }
                        com.example.digitracksdk.utils.ImageUtils.INSTANCE?.openPdfFile(
                            this@TrainingDocumentFragment.requireContext(),
                            file?.absolutePath.toString()
                        )
                    } else {
                        showToast(getString(R.string.no_policy_found))
                    }
                }

                trainingMessage.observe(viewLifecycleOwner) {
                    toggleLoader(false)
                    showToast(it)
                }
            }
        }
    }

    fun setUpTrainingAdapter() {
        if (::trainingDocumentAdapter.isInitialized) {
            trainingDocumentAdapter.notifyDataSetChanged()
        } else {
            trainingDocumentAdapter =
                TrainingDocumentAdapter(this.requireContext(), trainingDocList, this)
            binding.recyclerTrainingDocument.adapter = trainingDocumentAdapter
        }
    }

    override fun clickOnViewDocument(position: Int) {
        selectedItemPosition = position
        if (context?.isNetworkAvailable() == true) {
            toggleLoader(true)
            trainingViewModel.callViewTrainingDocumentApi(
                request = ViewTrainingDocumentRequestModel(
                    ClientTrainingID = trainingDocList[position].clientTrainingID ?: 0
                )
            )
        } else {
            showToast(getString(R.string.no_internet_connection))
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
}