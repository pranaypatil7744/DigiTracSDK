package com.example.digitracksdk.presentation.home.new_reimbursement.fragments

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import com.example.digitracksdk.Constant
import com.example.digitracksdk.base.BaseFragment
import com.example.digitracksdk.R
import com.example.digitracksdk.base.BaseActivity
import com.example.digitracksdk.databinding.FragmentPendingVouchersListBinding
import com.example.digitracksdk.domain.model.GnetIdRequestModel
import com.example.digitracksdk.domain.model.new_reimbursement.DeleteNewReimbursementItemRequestModel
import com.example.digitracksdk.domain.model.new_reimbursement.GenerateVoucherFromNewReimbursementRequestModel
import com.example.digitracksdk.domain.model.new_reimbursement.LstReimbdetailsforVoucherGenModel
import com.example.digitracksdk.presentation.home.new_reimbursement.BottomSheetAddReimbursementFragment
import com.example.digitracksdk.presentation.home.new_reimbursement.adapter.NewReimbursement1Adapter
import com.example.digitracksdk.presentation.home.pending_reimbursement.adapter.PendingReimbursementAdapter
import com.example.digitracksdk.presentation.home.reimbursements.ReimbursementViewModel
import com.example.digitracksdk.utils.AppUtils
import com.example.digitracksdk.utils.DialogUtils
import com.example.digitracksdk.utils.PreferenceUtils
import com.example.digitracksdk.utils.isNetworkAvailable
import org.koin.android.viewmodel.ext.android.viewModel


class PendingVouchersListFragment : BaseFragment(), DialogUtils.DialogManager,
    PendingReimbursementAdapter.PendingReimbursementClickManager {

    companion object {
        fun newInstance(): PendingVouchersListFragment {
            return PendingVouchersListFragment()
        }
    }

    lateinit var bottomSheetAddReimbursementFragment: BottomSheetAddReimbursementFragment
    lateinit var binding: FragmentPendingVouchersListBinding
    lateinit var preferenceUtils: PreferenceUtils
    private var gNetAssociateId: String = ""
    private var associateId: String = ""
    private val reimbursementViewModel: ReimbursementViewModel by viewModel()
    lateinit var newReimbursement1Adapter: NewReimbursement1Adapter
    private val reimbursement1List: ArrayList<LstReimbdetailsforVoucherGenModel> = ArrayList()
    var awaitingCount: Int = 0

    private var checkSelected: ArrayList<LstReimbdetailsforVoucherGenModel> = ArrayList()
    var selectedItemPosition: Int = -1
    var isForDelete: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentPendingVouchersListBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        preferenceUtils = PreferenceUtils(requireActivity())
        getPreferenceData()
        setUpObserver()
        setUpAdapter()
        setUpListener()
        callReimbursementListForVoucherApi()

    }

    override fun onResume() {
        super.onResume()
//        callReimbursementListForVoucherApi()
    }


    private fun setUpObserver() {
        binding.apply {
            with(reimbursementViewModel) {
                getReimbursementListForVoucherResponseData.observe(viewLifecycleOwner) {
                    toggleLoader(false)
                    if (it.Status.toString().lowercase() == Constant.SUCCESS.lowercase()) {
                        if (!it.LstReimbdetailsforVoucherGen.isNullOrEmpty()) {
                            showNoDataLayout(false)
                            reimbursement1List.clear()
                            reimbursement1List.addAll(
                                it.LstReimbdetailsforVoucherGen ?: arrayListOf()
                            )
                            setUpAdapter()
                        } else {
                            showNoDataLayout(true)
                        }
                    } else {
                        showNoDataLayout(true)
                        showToast(it.Message.toString())
                    }
                }

                deleteNewReimbursementListItemResponseData.observe(viewLifecycleOwner) {
                    toggleLoader(false)
                    if (it.Status == Constant.SUCCESS) {
                        reimbursement1List.removeAt(selectedItemPosition)
                        newReimbursement1Adapter.notifyItemRemoved(selectedItemPosition)
                        newReimbursement1Adapter.notifyItemRangeChanged(
                            selectedItemPosition,
                            reimbursement1List.size
                        )
                        if (reimbursement1List.isEmpty()) {
                            showNoDataLayout(true)
                        } else {
                            showNoDataLayout(false)
                        }
                    }
                    showToast(it.Message.toString())
                }

                generateNewReimbursementVoucherResponseData.observe(viewLifecycleOwner) {
                    toggleLoader(false)
                    if (it.Status == Constant.SUCCESS) {
                        callReimbursementListForVoucherApi()
                        btnGenerateVoucher.visibility = GONE
                        fabAddReimbursement.visibility = VISIBLE
                    }
                    showToast(it.Message.toString())
                }

                messageData.observe(viewLifecycleOwner) {
                    toggleLoader(false)
                    showToast(it.toString())
                }
            }
        }
    }

    fun toggleLoader(showLoader: Boolean) {
        binding.apply {
            toggleFadeView(
                root,
                contentLoading.root,
                contentLoading.imageLoading,
                showLoader
            )
        }
    }

    private fun showNoDataLayout(show: Boolean) {
        binding.apply {
            noDataLayout(
                noDataLayout = layoutNoData.root,
                recyclerView = recyclerPendingVoucherList,
                show = show
            )
        }
    }

    private fun getPreferenceData() {
        binding.apply {
            preferenceUtils.apply {
                gNetAssociateId = getValue(Constant.PreferenceKeys.GnetAssociateID)
                associateId = getValue(Constant.PreferenceKeys.AssociateID)
            }
        }
    }


    fun callReimbursementListForVoucherApi() {
        binding.apply {
            if (requireActivity().isNetworkAvailable()) {
                layoutNoInternet.root.visibility = GONE
                toggleLoader(true)
                reimbursementViewModel.callGetReimbursementListForVoucherApi(
                    request = GnetIdRequestModel(
                        GNETAssociateId = gNetAssociateId
                    )
                )
            } else {
                layoutNoInternet.root.visibility = VISIBLE
                recyclerPendingVoucherList.visibility = GONE
            }
            layoutNoData.root.visibility = GONE

        }
    }

    private fun setUpListener() {
        binding.apply {
            fabAddReimbursement.setOnClickListener {
                bottomSheetAddReimbursementFragment = BottomSheetAddReimbursementFragment()
                bottomSheetAddReimbursementFragment.show(
                    parentFragmentManager,
                    "add reimbursement fragment"
                )
            }

            layoutNoInternet.btnTryAgain.setOnClickListener {
                callReimbursementListForVoucherApi()
            }

            swipeRefreshLayout.setOnRefreshListener {
                swipeRefreshLayout.isRefreshing = false
                callReimbursementListForVoucherApi()
            }
            btnGenerateVoucher.setOnClickListener {
                checkSelected.clear()
                checkSelected = reimbursement1List.filter {
                    it.isSelected
                } as ArrayList<LstReimbdetailsforVoucherGenModel>
                if (checkSelected.isEmpty()) {
                    showToast(getString(R.string.please_select_at_least_one_voucher))
                } else {
                    val firstItemMonth = AppUtils.INSTANCE?.convertDateFormat(
                        "MM/dd/yyyy HH:mm:ss",
                        checkSelected[0].BillDate.toString(),
                        "MM"
                    )
                    val firstItemYear = AppUtils.INSTANCE?.convertDateFormat(
                        "MM/dd/yyyy HH:mm:ss",
                        checkSelected[0].BillDate.toString(),
                        "yyyy"
                    )
                    var isSameMonth: Boolean = true
                    for (i in checkSelected) {
                        val month = AppUtils.INSTANCE?.convertDateFormat(
                            "MM/dd/yyyy HH:mm:ss",
                            i.BillDate.toString(),
                            "MM"
                        )
                        val year = AppUtils.INSTANCE?.convertDateFormat(
                            "MM/dd/yyyy HH:mm:ss",
                            i.BillDate.toString(),
                            "yyyy"
                        )

                        if (month == firstItemMonth &&
                            year == firstItemYear
                        ) {
                            //
                        } else {
                            isSameMonth = false
                        }
                    }
                    if (!isSameMonth) {
                        showToast(getString(R.string.please_select_same_month_voucher))
                    } else {
                        val totalSameVoucher = reimbursement1List.filter {
                            AppUtils.INSTANCE?.convertDateFormat(
                                "MM/dd/yyyy HH:mm:ss",
                                it.BillDate.toString(),
                                "MM"
                            ) == firstItemMonth
                                    && AppUtils.INSTANCE?.convertDateFormat(
                                "MM/dd/yyyy HH:mm:ss",
                                it.BillDate.toString(),
                                "yyyy"
                            ) == firstItemYear
                        }
                        if (checkSelected.size != totalSameVoucher.size) {
                            isForDelete = false
                            DialogUtils.showPermissionDialog(
                                requireActivity(),
                                title = getString(R.string.alert),
                                msg = getString(R.string.few_vouchers_of_this_month_is_still_pending_for_clubbing_do_you_wish_to_proceed),
                                positiveBtn = getString(R.string.continue_),
                                negativeBtn = getString(R.string.close),
                                isFinish = false,
                                isOtherAction = true,
                                listener = this@PendingVouchersListFragment
                            )
                        } else {
                            val pendingIDList: ArrayList<String> = kotlin.collections.ArrayList()
                            pendingIDList.clear()
                            for (i in checkSelected) {
                                pendingIDList.add(i.AssociateReimbursementDetailId.toString())
                            }
//                            showToast(TextUtils.join(",", pendingIDList))
                            callGenerateVoucherApi(TextUtils.join(",", pendingIDList))
                        }
                    }
                }
            }
        }
    }

    private fun setUpAdapter() {
        binding.apply {
            if (::newReimbursement1Adapter.isInitialized){
                newReimbursement1Adapter.notifyDataSetChanged()
            }else{
                newReimbursement1Adapter = NewReimbursement1Adapter(
                    requireContext(),
                    reimbursement1List,
                    listener = this@PendingVouchersListFragment
                )
                recyclerPendingVoucherList.adapter = newReimbursement1Adapter
            }

        }
    }

    private fun callGenerateVoucherApi(associateReimbursementID: String) {
        if (requireActivity().isNetworkAvailable()) {
            toggleLoader(true)
            reimbursementViewModel.callGenerateNewReimbursementVoucherApi(
                request = GenerateVoucherFromNewReimbursementRequestModel(
                    AssociateID = associateId,
                    AssociateReimbursementDetailID = associateReimbursementID,
                )
            )
        } else {
            showToast(getString(R.string.no_internet_connection))
        }
    }

    override fun clickOnItem(position: Int) {
        binding.apply {
            reimbursement1List[position].isSelected = !reimbursement1List[position].isSelected
            val checkSelected = reimbursement1List.find {
                it.isSelected
            }
            if (checkSelected != null) {
                fabAddReimbursement.visibility = GONE
                btnGenerateVoucher.visibility = VISIBLE
            } else {
                fabAddReimbursement.visibility = VISIBLE
                btnGenerateVoucher.visibility = GONE
            }
            newReimbursement1Adapter.notifyItemChanged(position)
        }
    }

    override fun clickOnRemove(position: Int) {
        selectedItemPosition = position
        isForDelete = true
        DialogUtils.showPermissionDialog(
            requireActivity(),
            title = getString(R.string.alert),
            msg = getString(R.string.are_you_sure_you_want_to_delete),
            positiveBtn = getString(R.string.delete),
            negativeBtn = getString(R.string.close),
            isFinish = false,
            isOtherAction = true,
            listener = this
        )
    }

    override fun onContinueClick() {
        if (isForDelete) {
            callDeleteReimbursementItemApi()
        } else {
            val pendingIDList: ArrayList<String> = kotlin.collections.ArrayList()
            pendingIDList.clear()
            for (i in checkSelected) {
                pendingIDList.add(i.AssociateReimbursementDetailId.toString())
            }
            callGenerateVoucherApi(TextUtils.join(",", pendingIDList))
        }
    }

    private fun callDeleteReimbursementItemApi() {
        binding.apply {
            if (requireActivity().isNetworkAvailable()) {
                toggleLoader(true)
                reimbursementViewModel.callDeleteNewReimbursementItemApi(
                    request =
                    DeleteNewReimbursementItemRequestModel(
                        AssociateId = associateId,
                        AssociateReimbursementDetailId = reimbursement1List[selectedItemPosition].AssociateReimbursementDetailId.toString(),
                        UserID = gNetAssociateId
                    )
                )
            } else {
                showToast(getString(R.string.no_internet_connection))
            }
        }
    }

}