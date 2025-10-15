package com.example.digitracksdk.presentation.home.reimbursements.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.innov.digitrac.base.BaseFragment
import com.innov.digitrac.databinding.FragmentApprovedBinding
import com.example.digitracksdk.presentation.home.reimbursements.adapter.ReimbursementAdapter
import com.example.digitracksdk.presentation.home.reimbursements.model.ReimbursementModel
import com.example.digitracksdk.presentation.home.reimbursements.model.ReimbursementType


class ApprovedFragment : BaseFragment(), ReimbursementAdapter.ReimbursementClickManager {


    lateinit var binding: FragmentApprovedBinding
    lateinit var reimbursementDetailsBottomSheet: ReimbursementDetailsBottomSheetFragment
    var list: ArrayList<ReimbursementModel> = ArrayList()

    companion object {
        fun newInstance(): ApprovedFragment {
            return ApprovedFragment()
        }
    }

    lateinit var adapter: ReimbursementAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentApprovedBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpAdapter()
    }

    private fun setUpAdapter() {
        adapter = ReimbursementAdapter(requireActivity(), list, this)
        binding.recyclerApprovedReimbursement.adapter = adapter
    }

    override fun onItemClick(position: Int, associateReimbursementId: String, createdDate: String) {
//        val list =
//            (context as ReimbursementActivity).callReimbursementDetailsApi(associateReimbursementId)
//        if (!list.isNullOrEmpty()) {
//            reimbursementDetailsBottomSheet = ReimbursementDetailsBottomSheetFragment()
//            reimbursementDetailsBottomSheet.reimbursementType = ReimbursementType.APPROVED
//            reimbursementDetailsBottomSheet.date = createdDate
//            reimbursementDetailsBottomSheet.associateReimbursementId = associateReimbursementId
//            reimbursementDetailsBottomSheet.reimbursementDetailsList.addAll(list)
//            reimbursementDetailsBottomSheet.show(
//                parentFragmentManager,
//                "reimbursement bottom sheet"
//            )
//        } else {
//            showToast(getString(R.string.no_data_found))
//        }
        reimbursementDetailsBottomSheet = ReimbursementDetailsBottomSheetFragment()
        reimbursementDetailsBottomSheet.reimbursementType = ReimbursementType.APPROVED
        reimbursementDetailsBottomSheet.date = createdDate
        reimbursementDetailsBottomSheet.associateReimbursementId = associateReimbursementId
        reimbursementDetailsBottomSheet.show(
            parentFragmentManager,
            "reimbursement bottom sheet"
        )
    }

}