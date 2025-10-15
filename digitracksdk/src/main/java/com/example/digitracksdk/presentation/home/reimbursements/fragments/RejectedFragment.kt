package com.example.digitracksdk.presentation.home.reimbursements.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.innov.digitrac.base.BaseFragment
import com.innov.digitrac.databinding.FragmentRejectedBinding
import com.example.digitracksdk.presentation.home.reimbursements.adapter.ReimbursementAdapter
import com.example.digitracksdk.presentation.home.reimbursements.model.ReimbursementModel
import com.example.digitracksdk.presentation.home.reimbursements.model.ReimbursementType


class RejectedFragment : BaseFragment(), ReimbursementAdapter.ReimbursementClickManager {


    lateinit var binding: FragmentRejectedBinding
    lateinit var reimbursementDetailsBottomSheet: ReimbursementDetailsBottomSheetFragment
    private var selectedItemPosition:Int = 0
    var list: ArrayList<ReimbursementModel> = ArrayList()

    lateinit var adapter: ReimbursementAdapter

    companion object {
        fun newInstance(): RejectedFragment {
            return RejectedFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentRejectedBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpAdapter()
    }

    private fun setUpAdapter() {
        adapter = ReimbursementAdapter(requireActivity(), list, this)
        binding.recyclerRejectedReimbursement.adapter = adapter
    }

    override fun onItemClick(position: Int, associateReimbursementId: String, createdDate: String) {
        reimbursementDetailsBottomSheet = ReimbursementDetailsBottomSheetFragment()
        reimbursementDetailsBottomSheet.reimbursementType = ReimbursementType.REJECTED
        reimbursementDetailsBottomSheet.date = createdDate
        reimbursementDetailsBottomSheet.associateReimbursementId = associateReimbursementId
        reimbursementDetailsBottomSheet.selectedItemPosition = position
        reimbursementDetailsBottomSheet.show(
            parentFragmentManager,
            "reimbursement bottom sheet"
        )
    }

}