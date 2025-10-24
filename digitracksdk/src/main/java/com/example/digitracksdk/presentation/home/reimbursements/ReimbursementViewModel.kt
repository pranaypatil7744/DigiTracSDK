package com.example.digitracksdk.presentation.home.reimbursements

import android.text.TextUtils
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.digitracksdk.domain.model.new_reimbursement.GetMonthDetailsResponseModel
import com.example.digitracksdk.domain.model.reimbursement_model.ModeOfTravelRequestModel
import com.example.digitracksdk.domain.model.reimbursement_model.ModeOfTravelResponseModel
import com.example.digitracksdk.domain.model.reimbursement_model.ReimbursementEndKmRequestModel
import com.example.digitracksdk.domain.model.reimbursement_model.ReimbursementEndKmResponseModel
import com.example.digitracksdk.domain.usecase.reimbursement_usecase.ReimbursementValidationUseCase
import com.example.digitracksdk.Constant
import com.example.digitracksdk.R
import com.example.digitracksdk.domain.model.ApiError
import com.example.digitracksdk.domain.model.new_reimbursement.DeleteNewReimbursementItemRequestModel
import com.example.digitracksdk.domain.model.new_reimbursement.DeleteNewReimbursementItemResponseModel
import com.example.digitracksdk.domain.model.new_reimbursement.GenerateVoucherFromNewReimbursementRequestModel
import com.example.digitracksdk.domain.model.new_reimbursement.GenerateVoucherFromNewReimbursementResponseModel
import com.example.digitracksdk.domain.model.new_reimbursement.GetMonthYearDetailsRequestModel
import com.example.digitracksdk.domain.model.new_reimbursement.GetYearDetailsResponseModel
import com.example.digitracksdk.domain.model.new_reimbursement.InsertNewReimbursementRequestModel
import com.example.digitracksdk.domain.model.new_reimbursement.InsertNewReimbursementResponseModel
import com.example.digitracksdk.domain.model.reimbursement_model.AssociateReimbursementDetailListModel
import com.example.digitracksdk.domain.model.reimbursement_model.CheckReimbursementLimitRequestModel
import com.example.digitracksdk.domain.model.reimbursement_model.CheckReimbursementLimitResponseModel
import com.example.digitracksdk.domain.model.reimbursement_model.CheckReimbursementLimitV1RequestModel
import com.example.digitracksdk.domain.model.reimbursement_model.CheckReimbursementLimitV1ResponseModel
import com.example.digitracksdk.domain.model.reimbursement_model.InsertReimbursementRequestModel
import com.example.digitracksdk.domain.model.reimbursement_model.InsertReimbursementResponseModel
import com.example.digitracksdk.domain.model.reimbursement_model.PendingReimbursementRequestModel
import com.example.digitracksdk.domain.model.reimbursement_model.PendingReimbursementResponseModel
import com.example.digitracksdk.domain.model.reimbursement_model.ReimbursementCategoryRequestModel
import com.example.digitracksdk.domain.model.reimbursement_model.ReimbursementCategoryResponseModel
import com.example.digitracksdk.domain.model.reimbursement_model.ReimbursementListRequestModel
import com.example.digitracksdk.domain.model.reimbursement_model.ReimbursementListResponseModel
import com.example.digitracksdk.domain.model.reimbursement_model.ReimbursementSubCategoryRequestModel
import com.example.digitracksdk.domain.model.reimbursement_model.ReimbursementSubCategoryResponseModel
import com.example.digitracksdk.domain.model.reimbursement_model.ReimbursementValidationRequestModel
import com.example.digitracksdk.domain.model.reimbursement_model.ReimbursementValidationResponseModel
import com.example.digitracksdk.domain.model.reimbursement_model.RejectedEndKmRequestModel
import com.example.digitracksdk.domain.model.reimbursement_model.RejectedEndKmResponseModel
import com.example.digitracksdk.domain.model.reimbursement_model.RejectedReimbursementValidationRequestModel
import com.example.digitracksdk.domain.model.reimbursement_model.RejectedReimbursementValidationResponseModel
import com.example.digitracksdk.domain.model.reimbursement_model.SaveReimbursementPreApprovalRequestModel
import com.example.digitracksdk.domain.model.reimbursement_model.SaveReimbursementPreApprovalResponseModel
import com.example.digitracksdk.domain.model.reimbursement_model.UpdatePendingReimbursementRequestModel
import com.example.digitracksdk.domain.model.reimbursement_model.UpdatePendingReimbursementResponseModel
import com.example.digitracksdk.domain.model.reimbursement_model.UpdateReimbursementDetailsRequestModel
import com.example.digitracksdk.domain.model.reimbursement_model.UpdateReimbursementDetailsResponseModel
import com.example.digitracksdk.domain.model.reimbursement_model.UploadReimbursementBillRequestModel
import com.example.digitracksdk.domain.model.reimbursement_model.UploadReimbursementBillResponseModel
import com.example.digitracksdk.domain.model.GnetIdRequestModel
import com.example.digitracksdk.domain.model.new_reimbursement.GetNewReimbursementListResponseModel
import com.example.digitracksdk.domain.model.reimbursement_model.ReimbursementBillResponseModel
import com.example.digitracksdk.domain.model.reimbursement_model.ReimbursementDetailsRequestModel
import com.example.digitracksdk.domain.model.reimbursement_model.ReimbursementDetailsResponseModel
import com.example.digitracksdk.domain.model.reimbursement_model.UpdateReimbursementStatusRequestModel
import com.example.digitracksdk.domain.model.reimbursement_model.UpdateReimbursementStatusResponseModel
import com.example.digitracksdk.domain.model.new_reimbursement.*
import com.example.digitracksdk.domain.model.reimbursement_model.*
import com.example.digitracksdk.domain.usecase.base.UseCaseResponse
import com.example.digitracksdk.domain.usecase.reimbursement_usecase.CheckReimbursementLimitUseCase
import com.example.digitracksdk.domain.usecase.reimbursement_usecase.CheckReimbursementLimitV1UseCase
import com.example.digitracksdk.domain.usecase.reimbursement_usecase.DeleteNewReimbursementListItemUseCase
import com.example.digitracksdk.domain.usecase.reimbursement_usecase.GenerateNewReimbursementVoucherUseCase
import com.example.digitracksdk.domain.usecase.reimbursement_usecase.GetMonthDetailsUseCase
import com.example.digitracksdk.domain.usecase.reimbursement_usecase.GetReimbursementListForVoucherUseCase
import com.example.digitracksdk.domain.usecase.reimbursement_usecase.GetYearDetailsUseCase
import com.example.digitracksdk.domain.usecase.reimbursement_usecase.InsertNewReimbursementUseCase
import com.example.digitracksdk.domain.usecase.reimbursement_usecase.InsertReimbursementUseCase
import com.example.digitracksdk.domain.usecase.reimbursement_usecase.NewReimbursementApprovedListUseCase
import com.example.digitracksdk.domain.usecase.reimbursement_usecase.NewReimbursementPendingListUseCase
import com.example.digitracksdk.domain.usecase.reimbursement_usecase.NewReimbursementRejectedListUseCase
import com.example.digitracksdk.domain.usecase.reimbursement_usecase.PendingReimbursementUseCase
import com.example.digitracksdk.domain.usecase.reimbursement_usecase.ReimbursementBillUseCase
import com.example.digitracksdk.domain.usecase.reimbursement_usecase.ReimbursementCategoryUseCase
import com.example.digitracksdk.domain.usecase.reimbursement_usecase.ReimbursementDetailsUseCase
import com.example.digitracksdk.domain.usecase.reimbursement_usecase.ReimbursementEndKmUseCase
import com.example.digitracksdk.domain.usecase.reimbursement_usecase.ReimbursementListUseCase
import com.example.digitracksdk.domain.usecase.reimbursement_usecase.ReimbursementModeOfTravelUseCase
import com.example.digitracksdk.domain.usecase.reimbursement_usecase.ReimbursementSubCategoryUseCase
import com.example.digitracksdk.domain.usecase.reimbursement_usecase.RejectedEndKmUseCase
import com.example.digitracksdk.domain.usecase.reimbursement_usecase.RejectedReimbursementValidationUseCase
import com.example.digitracksdk.domain.usecase.reimbursement_usecase.SaveReimbursementPreApprovalUseCase
import com.example.digitracksdk.domain.usecase.reimbursement_usecase.UpdatePendingReimbursementUseCase
import com.example.digitracksdk.domain.usecase.reimbursement_usecase.UpdateReimbursementDetailsUseCase
import com.example.digitracksdk.domain.usecase.reimbursement_usecase.UpdateReimbursementStatusUseCase
import com.example.digitracksdk.domain.usecase.reimbursement_usecase.UploadReimbursementBillUseCase
import com.example.digitracksdk.domain.usecase.reimbursement_usecase.*
import com.example.digitracksdk.listener.ValidationListener
import com.example.digitracksdk.utils.AppUtils
import kotlinx.coroutines.cancel

class ReimbursementViewModel constructor(
    private val reimbursementListUseCase: ReimbursementListUseCase,
    private val uploadReimbursementBillUseCase: UploadReimbursementBillUseCase,
    private val reimbursementDetailsUseCase: ReimbursementDetailsUseCase,
    private val reimbursementBillUseCase: ReimbursementBillUseCase,
    private val insertReimbursementUseCase: InsertReimbursementUseCase,
    private val reimbursementCategoryUseCase: ReimbursementCategoryUseCase,
    private val reimbursementEndKmUseCase: ReimbursementEndKmUseCase,
    private val reimbursementModeOfTravelUseCase: ReimbursementModeOfTravelUseCase,
    private val reimbursementSubCategoryUseCase: ReimbursementSubCategoryUseCase,
    private val reimbursementValidationUseCase: ReimbursementValidationUseCase,
    private val pendingReimbursementUseCase: PendingReimbursementUseCase,
    private val updatePendingReimbursementUseCase: UpdatePendingReimbursementUseCase,
    private val updateReimbursementStatusUseCase: UpdateReimbursementStatusUseCase,
    private val updateReimbursementDetailsUseCase: UpdateReimbursementDetailsUseCase,
    private val checkReimbursementValidationUseCase: CheckReimbursementLimitV1UseCase,
    private val rejectedReimbursementValidationUseCase: RejectedReimbursementValidationUseCase,
    private val checkReimbursementLimitUseCase: CheckReimbursementLimitUseCase,
    private val saveReimbursementPreApprovalUseCase: SaveReimbursementPreApprovalUseCase,
    private val rejectedEndKmUseCase: RejectedEndKmUseCase,
    private val deleteNewReimbursementListItemUseCase: DeleteNewReimbursementListItemUseCase,
    private val generateNewReimbursementVoucherUseCase: GenerateNewReimbursementVoucherUseCase,
    private val getReimbursementListForVoucherUseCase: GetReimbursementListForVoucherUseCase,
    private val insertNewReimbursementUseCase: InsertNewReimbursementUseCase,
    private val newReimbursementPendingListUseCase: NewReimbursementPendingListUseCase,
    private val newReimbursementApprovedListUseCase: NewReimbursementApprovedListUseCase,
    private val newReimbursementRejectedListUseCase: NewReimbursementRejectedListUseCase,
    private val getMonthDetailsUseCase: GetMonthDetailsUseCase,
    private val getYearDetailsUseCase: GetYearDetailsUseCase

) : ViewModel() {
    val reimbursementListResponseData = MutableLiveData<ReimbursementListResponseModel>()
    val newReimbursementPendingListResponseData = MutableLiveData<ReimbursementListResponseModel>()
    val newReimbursementRejectedListResponseData = MutableLiveData<ReimbursementListResponseModel>()
    val newReimbursementApprovedListResponseData = MutableLiveData<ReimbursementListResponseModel>()
    val rejectedEndKmResponseData = MutableLiveData<RejectedEndKmResponseModel>()
    val saveReimbursementPreApprovalResponseData =
        MutableLiveData<SaveReimbursementPreApprovalResponseModel>()
    val checkReimbursementLimitResponseData =
        MutableLiveData<CheckReimbursementLimitResponseModel>()
    val checkReimbursementValidationV1ResponseData =
        MutableLiveData<CheckReimbursementLimitV1ResponseModel>()
    val rejectedReimbursementValidationResponseData =
        MutableLiveData<RejectedReimbursementValidationResponseModel>()
    val updateReimbursementStatusResponseData =
        MutableLiveData<UpdateReimbursementStatusResponseModel>()
    val updateReimbursementDetailsResponseData =
        MutableLiveData<UpdateReimbursementDetailsResponseModel>()
    val updatePendingReimbursementResponseData =
        MutableLiveData<UpdatePendingReimbursementResponseModel>()
    val pendingReimbursementListResponseData = MutableLiveData<PendingReimbursementResponseModel>()
    val uploadReimbursementBillResponseData =
        MutableLiveData<UploadReimbursementBillResponseModel>()
    val reimbursementDetailsResponseData = MutableLiveData<ReimbursementDetailsResponseModel>()
    val reimbursementBillResponseData = MutableLiveData<ReimbursementBillResponseModel>()
    val insertReimbursementResponseData = MutableLiveData<InsertReimbursementResponseModel>()
    val insertNewReimbursementResponseData = MutableLiveData<InsertNewReimbursementResponseModel>()
    val reimbursementCategoryResponseData = MutableLiveData<ReimbursementCategoryResponseModel>()
    val reimbursementSubCategoryResponseData =
        MutableLiveData<ReimbursementSubCategoryResponseModel>()
    val reimbursementEndKmResponseData = MutableLiveData<ReimbursementEndKmResponseModel>()
    val reimbursementModeOfTravelResponseData = MutableLiveData<ModeOfTravelResponseModel>()
    val reimbursementValidationResponseData =
        MutableLiveData<ReimbursementValidationResponseModel>()
    val getReimbursementListForVoucherResponseData =
        MutableLiveData<GetNewReimbursementListResponseModel>()
    val generateNewReimbursementVoucherResponseData =
        MutableLiveData<GenerateVoucherFromNewReimbursementResponseModel>()
    val deleteNewReimbursementListItemResponseData =
        MutableLiveData<DeleteNewReimbursementItemResponseModel>()
    val getMonthDetailsResponseData = MutableLiveData<GetMonthDetailsResponseModel>()
    val getYearDetailsResponseData = MutableLiveData<GetYearDetailsResponseModel>()
    val messageData = MutableLiveData<String>()
    val showProgressbar = MutableLiveData<Boolean>()

    var validationListener: ValidationListener? = null

    fun validateApplyReimbursement(
        request: AssociateReimbursementDetailListModel,
        flagPocketExpense: Boolean = false,
        localEndKm: Int,
        isFromRejected: Boolean = false,
        startKmApi: Int = 0,
        endKmApi: Int = 0,
        isOldReimbursement : Boolean = false
    ) {
        if (request.ReimbursementCategoryId == 0) {
            validationListener?.onValidationFailure(
                Constant.ListenerConstants.EXPENSE_TYPE_ERROR,
                R.string.please_select_expense_type
            )
            return
        }
        if (!isOldReimbursement) {
            if (request.ClaimYear.isNullOrEmpty()) {
                validationListener?.onValidationFailure(
                    Constant.ListenerConstants.CLAIM_YEAR_TYPE_ERROR,
                    R.string.please_select_claim_year
                )
                return
            }
            if (request.ClaimMonth.isNullOrEmpty()) {
                validationListener?.onValidationFailure(
                    Constant.ListenerConstants.CLAIM_MONTH_TYPE_ERROR,
                    R.string.please_select_claim_month
                )
                return
            }
        }
        if (request.ReimbursementCategoryId != ReimbursementCategory.NO_REIMBURSEMENT.value) {

            if (request.ReimbursementCategoryId != ReimbursementCategory.TRAVELLING_SELF.value ||
                request.ReimbursementCategoryId != ReimbursementCategory.DAILY_MILEAGE.value
            ) {
                if (request.ReimbursementCategoryId == ReimbursementCategory.RELOCATION_ALLOWANCE.value) {
                    if (TextUtils.isEmpty(request.BillNo)) {
                        validationListener?.onValidationFailure(
                            Constant.ListenerConstants.BILL_NUMBER_ERROR,
                            R.string.please_enter_reference_number
                        )
                        return
                    }
                } else {
                    if (TextUtils.isEmpty(request.BillNo)) {
                        validationListener?.onValidationFailure(
                            Constant.ListenerConstants.BILL_NUMBER_ERROR,
                            R.string.please_enter_bill_number
                        )
                        return
                    }
                }
                if (AppUtils.INSTANCE?.checkSpecialSymbol(request.BillNo.toString()) == true) {
                    validationListener?.onValidationFailure(
                        Constant.ListenerConstants.BILL_NUMBER_ERROR,
                        R.string.special_symbols_not_allowed_here
                    )
                    return
                }

            }

            if (TextUtils.isEmpty(request.ClaimDate)) {
                validationListener?.onValidationFailure(
                    Constant.ListenerConstants.CLAIM_DATE_ERROR,
                    R.string.please_choose_claim_date
                )
                return
            }

            if (request.ReimbursementCategoryId == ReimbursementCategory.TRAVELLING_SELF.value ||
                request.ReimbursementCategoryId == ReimbursementCategory.DAILY_MILEAGE.value
            ) {
                if (TextUtils.isEmpty(request.BillDate)) {
                    validationListener?.onValidationFailure(
                        Constant.ListenerConstants.BILL_DATE_ERROR,
                        R.string.please_choose_activity_date
                    )
                    return
                }
            } else {
                if (TextUtils.isEmpty(request.BillDate)) {
                    validationListener?.onValidationFailure(
                        Constant.ListenerConstants.BILL_DATE_ERROR,
                        R.string.please_choose_bill_date
                    )
                    return
                }
            }

            if (request.ReimbursementCategoryId == ReimbursementCategory.TRAVELLING_PUBLIC.value ||
                request.ReimbursementCategoryId == ReimbursementCategory.LODGING_BOARDING.value ||
                request.ReimbursementCategoryId == ReimbursementCategory.NIGHT_HALT_WITH_FOOD.value
            ) {
                if (TextUtils.isEmpty(request.FromDate)) {
                    validationListener?.onValidationFailure(
                        Constant.ListenerConstants.FROM_DATE_ERROR,
                        R.string.please_choose_bill_from
                    )
                    return
                }
                if (TextUtils.isEmpty(request.ToDate)) {
                    validationListener?.onValidationFailure(
                        Constant.ListenerConstants.TO_DATE_ERROR,
                        R.string.please_choose_bill_to
                    )
                    return
                }
            }

            if (request.ReimbursementCategoryId == ReimbursementCategory.TRAVELLING_PUBLIC.value ||
                request.ReimbursementCategoryId == ReimbursementCategory.TRAVELLING_SELF.value ||
                request.ReimbursementCategoryId == ReimbursementCategory.NIGHT_HALT_WITH_FOOD.value ||
                request.ReimbursementCategoryId == ReimbursementCategory.DAILY_MILEAGE.value
            ) {
                if (TextUtils.isEmpty(request.FromLocation)) {
                    validationListener?.onValidationFailure(
                        Constant.ListenerConstants.JOURNEY_FROM_ERROR,
                        R.string.please_enter_journey_from
                    )
                    return
                }
                if (TextUtils.isEmpty(request.ToLocation)) {
                    validationListener?.onValidationFailure(
                        Constant.ListenerConstants.JOURNEY_TO_ERROR,
                        R.string.please_enter_journey_to
                    )
                    return
                }
            }

            if (TextUtils.isEmpty(request.Amount.toString())) {
                if (request.ReimbursementCategoryId == ReimbursementCategory.TRAVELLING_SELF.value ||
                    request.ReimbursementCategoryId == ReimbursementCategory.DAILY_MILEAGE.value
                ) {
                    validationListener?.onValidationFailure(
                        Constant.ListenerConstants.BASE_AMT_ERROR,
                        R.string.please_enter_bill_amount
                    )
                    return
                } else {
                    validationListener?.onValidationFailure(
                        Constant.ListenerConstants.BASE_AMT_ERROR,
                        R.string.please_enter_base_amount
                    )
                    return
                }
            }
            if ((request.Amount.toString()
                    .toDoubleOrNull() ?: 0.0) <= 0.0 || (request.Amount.toString()
                    .toDoubleOrNull() ?: 0.0) <= 0
            ) {
                if (request.ReimbursementCategoryId != ReimbursementCategory.TRAVELLING_SELF.value ||
                    request.ReimbursementCategoryId != ReimbursementCategory.DAILY_MILEAGE.value
                ) {
                    validationListener?.onValidationFailure(
                        Constant.ListenerConstants.BASE_AMT_ERROR,
                        R.string.please_enter_valid_base_amount
                    )
                    return
                }
            }
            if (flagPocketExpense) {
                validationListener?.onValidationFailure(
                    Constant.ListenerConstants.GROSS_AMT_ERROR,
                    R.string.out_of_pocket_expense_type_bill_amount_is_not_allowed_more_than_rs_100
                )
                return
            }

//        if (
//            request.ReimbursementCategoryId != ReimbursementCategory.TRAVELLING_SELF.value ||
//                    request.ReimbursementCategoryId != ReimbursementCategory.DAILY_MILEAGE.value
//        ) {
//            if (TextUtils.isEmpty(request.TaxAmount)) {
//                validationListener?.onValidationFailure(
//                    Constant.ListenerConstants.TAX_AMT_ERROR,
//                    R.string.please_enter_tax_amount
//                )
//                return
//            }
//        }
            if ((request.TaxAmount.toString().toDoubleOrNull() ?: 0.0) > 0) {
                if ((request.Amount.toString().toDoubleOrNull()
                        ?: 0.0) < (request.TaxAmount.toString()
                        .toDoubleOrNull() ?: 0.0)
                ) {
                    validationListener?.onValidationFailure(
                        Constant.ListenerConstants.TAX_AMT_ERROR,
                        R.string.tax_amount_not_be_greater_than_base_bill_amount
                    )
                    return
                }
            }
            if (request.ReimbursementCategoryId == ReimbursementCategory.TRAVELLING_SELF.value ||
                request.ReimbursementCategoryId == ReimbursementCategory.DAILY_MILEAGE.value
            ) {

                if (TextUtils.isEmpty(request.StartKm)) {
                    validationListener?.onValidationFailure(
                        Constant.ListenerConstants.START_KM_ERROR,
                        R.string.please_enter_start_km
                    )
                    return
                }
                if (TextUtils.isEmpty(request.EndKm)) {
                    validationListener?.onValidationFailure(
                        Constant.ListenerConstants.END_KM_ERROR,
                        R.string.please_enter_end_km
                    )
                    return
                }

                if(request.EndKm.toInt()==0)
                {
                    validationListener?.onValidationFailure(
                        Constant.ListenerConstants.END_KM_ERROR,
                        R.string.end_km_not_valid
                    )
                    return
                }

                if (request.StartKm.toInt() > request.EndKm.toInt()) {
                    validationListener?.onValidationFailure(
                        Constant.ListenerConstants.START_KM_ERROR,
                        R.string.start_km_cannot_be_greater_than_end_km
                    )
                    return
                }
                if (isFromRejected) {
                    if (request.StartKm.toInt() < startKmApi && startKmApi != 0) {
                        validationListener?.onValidationFailure(
                            Constant.ListenerConstants.START_KM_ERROR,
                            R.string.start_km_not_valid
                        )
                        return
                    }
                    if (request.EndKm.toInt() > endKmApi && endKmApi != 0) {
                        validationListener?.onValidationFailure(
                            Constant.ListenerConstants.END_KM_ERROR,
                            R.string.end_km_not_valid
                        )
                        return
                    }
                } else {
                    if (request.StartKm.toInt() < localEndKm) {
                        validationListener?.onValidationFailure(
                            Constant.ListenerConstants.START_KM_ERROR,
                            R.string.start_km_not_valid
                        )
                        return
                    }
                }
            }

            if ((request.ReimbursementCategoryId == ReimbursementCategory.TRAVELLING_PUBLIC.value ||
                        request.ReimbursementCategoryId == ReimbursementCategory.TRAVELLING_SELF.value ||
                        request.ReimbursementCategoryId == ReimbursementCategory.NIGHT_HALT_WITH_FOOD.value ||
                        request.ReimbursementCategoryId == ReimbursementCategory.FUEL_BILL.value ||
                        request.ReimbursementCategoryId == ReimbursementCategory.DAILY_MILEAGE.value ||
                        request.ReimbursementCategoryId == ReimbursementCategory.RELOCATION_ALLOWANCE.value ||
                        request.ReimbursementCategoryId == ReimbursementCategory.BASF_OTHERS.value)
            ) {
                if (request.ModeOfTravelId == 0) {
                    validationListener?.onValidationFailure(
                        Constant.ListenerConstants.TRAVEL_MODE_ERROR,
                        R.string.please_select_travel_mode
                    )
                    return
                }
            }

            if (TextUtils.isEmpty(request.Remark)) {
                validationListener?.onValidationFailure(
                    Constant.ListenerConstants.REMARK_ERROR,
                    R.string.please_enter_remark
                )
                return
            }

        } else {
            if (TextUtils.isEmpty(request.BillDate)) {
                validationListener?.onValidationFailure(
                    Constant.ListenerConstants.BILL_DATE_ERROR,
                    R.string.please_choose_bill_date
                )
                return
            }
        }
        validationListener?.onValidationSuccess(Constant.success, R.string.success)

    }

    fun callDeleteNewReimbursementItemApi(request: DeleteNewReimbursementItemRequestModel) {
        deleteNewReimbursementListItemUseCase.invoke(
            viewModelScope,
            request,
            object : UseCaseResponse<DeleteNewReimbursementItemResponseModel> {
                override fun onSuccess(result: DeleteNewReimbursementItemResponseModel) {
                    deleteNewReimbursementListItemResponseData.value = result
                }

                override fun onError(apiError: ApiError?) {
                    messageData.value = apiError?.message.toString()
                }
            })
    }

    fun callInsertNewReimbursementApi(request: InsertNewReimbursementRequestModel) {
        insertNewReimbursementUseCase.invoke(
            viewModelScope,
            request,
            object : UseCaseResponse<InsertNewReimbursementResponseModel> {
                override fun onSuccess(result: InsertNewReimbursementResponseModel) {
                    insertNewReimbursementResponseData.value = result
                }

                override fun onError(apiError: ApiError?) {
                    messageData.value = apiError?.message.toString()
                }
            })
    }

    fun callGenerateNewReimbursementVoucherApi(request: GenerateVoucherFromNewReimbursementRequestModel) {
        generateNewReimbursementVoucherUseCase.invoke(
            viewModelScope,
            request,
            object : UseCaseResponse<GenerateVoucherFromNewReimbursementResponseModel> {
                override fun onSuccess(result: GenerateVoucherFromNewReimbursementResponseModel) {
                    generateNewReimbursementVoucherResponseData.value = result
                }

                override fun onError(apiError: ApiError?) {
                    messageData.value = apiError?.message.toString()
                }
            })
    }

    fun callGetReimbursementListForVoucherApi(request: GnetIdRequestModel) {
        getReimbursementListForVoucherUseCase.invoke(
            viewModelScope,
            request,
            object : UseCaseResponse<GetNewReimbursementListResponseModel> {
                override fun onSuccess(result: GetNewReimbursementListResponseModel) {
                    getReimbursementListForVoucherResponseData.value = result
                }

                override fun onError(apiError: ApiError?) {
                    messageData.value = apiError?.message.toString()
                }
            })
    }

    fun callGetNewReimbursementPendingListApi(request: ReimbursementListRequestModel) {
        newReimbursementPendingListUseCase.invoke(
            viewModelScope,
            request,
            object : UseCaseResponse<ReimbursementListResponseModel> {
                override fun onSuccess(result: ReimbursementListResponseModel) {
                    newReimbursementPendingListResponseData.value = result
                }

                override fun onError(apiError: ApiError?) {
                    messageData.value = apiError?.message.toString()
                }
            })
    }

    fun callGetNewReimbursementApprovedListApi(request: ReimbursementListRequestModel) {
        newReimbursementApprovedListUseCase.invoke(
            viewModelScope,
            request,
            object : UseCaseResponse<ReimbursementListResponseModel> {
                override fun onSuccess(result: ReimbursementListResponseModel) {
                    newReimbursementApprovedListResponseData.value = result
                }

                override fun onError(apiError: ApiError?) {
                    messageData.value = apiError?.message.toString()
                }
            })
    }

    fun callGetNewReimbursementRejectedListApi(request: ReimbursementListRequestModel) {
        newReimbursementRejectedListUseCase.invoke(
            viewModelScope,
            request,
            object : UseCaseResponse<ReimbursementListResponseModel> {
                override fun onSuccess(result: ReimbursementListResponseModel) {
                    newReimbursementRejectedListResponseData.value = result
                }

                override fun onError(apiError: ApiError?) {
                    messageData.value = apiError?.message.toString()
                }
            })
    }

    fun callRejectedReimbursementValidationApi(request: RejectedReimbursementValidationRequestModel) {
        showProgressbar.value = true
        rejectedReimbursementValidationUseCase.invoke(
            viewModelScope,
            request,
            object : UseCaseResponse<RejectedReimbursementValidationResponseModel> {
                override fun onSuccess(result: RejectedReimbursementValidationResponseModel) {
                    showProgressbar.value = false
                    rejectedReimbursementValidationResponseData.value = result
                }

                override fun onError(apiError: ApiError?) {
                    showProgressbar.value = false
                    messageData.value = apiError?.message.toString()
                }
            })
    }

    fun callRejectedEndKmApi(request: RejectedEndKmRequestModel) {
        showProgressbar.value = true
        rejectedEndKmUseCase.invoke(
            viewModelScope,
            request,
            object : UseCaseResponse<RejectedEndKmResponseModel> {
                override fun onSuccess(result: RejectedEndKmResponseModel) {
                    showProgressbar.value = false
                    rejectedEndKmResponseData.value = result
                }

                override fun onError(apiError: ApiError?) {
                    showProgressbar.value = false
                    messageData.value = apiError?.message.toString()
                }
            })
    }

    fun callSaveReimbursementPreApprovalApi(request: SaveReimbursementPreApprovalRequestModel) {
        showProgressbar.value = true
        saveReimbursementPreApprovalUseCase.invoke(
            viewModelScope,
            request,
            object : UseCaseResponse<SaveReimbursementPreApprovalResponseModel> {
                override fun onSuccess(result: SaveReimbursementPreApprovalResponseModel) {
                    showProgressbar.value = false
                    saveReimbursementPreApprovalResponseData.value = result
                }

                override fun onError(apiError: ApiError?) {
                    showProgressbar.value = false
                    messageData.value = apiError?.message.toString()
                }
            })
    }

    fun callCheckReimbursementValidationV1Api(request: CheckReimbursementLimitV1RequestModel) {
        showProgressbar.value = true
        checkReimbursementValidationUseCase.invoke(
            viewModelScope,
            request,
            object : UseCaseResponse<CheckReimbursementLimitV1ResponseModel> {
                override fun onSuccess(result: CheckReimbursementLimitV1ResponseModel) {
                    showProgressbar.value = false
                    checkReimbursementValidationV1ResponseData.value = result
                }

                override fun onError(apiError: ApiError?) {
                    showProgressbar.value = false
                    messageData.value = apiError?.message.toString()
                }
            })
    }


    fun callCheckReimbursementLimitApi(request: CheckReimbursementLimitRequestModel) {
        showProgressbar.value = true
        checkReimbursementLimitUseCase.invoke(
            viewModelScope,
            request,
            object : UseCaseResponse<CheckReimbursementLimitResponseModel> {
                override fun onSuccess(result: CheckReimbursementLimitResponseModel) {
                    showProgressbar.value = false
                    checkReimbursementLimitResponseData.value = result
                }

                override fun onError(apiError: ApiError?) {
                    showProgressbar.value = false
                    messageData.value = apiError?.message.toString()
                }
            })
    }

    fun callUpdateReimbursementStatusApi(request: UpdateReimbursementStatusRequestModel) {
        showProgressbar.value = true
        updateReimbursementStatusUseCase.invoke(
            viewModelScope,
            request,
            object : UseCaseResponse<UpdateReimbursementStatusResponseModel> {
                override fun onSuccess(result: UpdateReimbursementStatusResponseModel) {
                    showProgressbar.value = false
                    updateReimbursementStatusResponseData.value = result
                }

                override fun onError(apiError: ApiError?) {
                    showProgressbar.value = false
                    messageData.value = apiError?.message.toString()
                }
            })
    }

    fun callUpdateReimbursementDetailsApi(request: UpdateReimbursementDetailsRequestModel) {
        showProgressbar.value = true
        updateReimbursementDetailsUseCase.invoke(
            viewModelScope,
            request,
            object : UseCaseResponse<UpdateReimbursementDetailsResponseModel> {
                override fun onSuccess(result: UpdateReimbursementDetailsResponseModel) {
                    showProgressbar.value = false
                    updateReimbursementDetailsResponseData.value = result
                }

                override fun onError(apiError: ApiError?) {
                    showProgressbar.value = false
                    messageData.value = apiError?.message.toString()
                }
            })
    }

    fun callUpdatePendingReimbursementApi(request: UpdatePendingReimbursementRequestModel) {
        showProgressbar.value = true
        updatePendingReimbursementUseCase.invoke(
            viewModelScope,
            request,
            object : UseCaseResponse<UpdatePendingReimbursementResponseModel> {
                override fun onSuccess(result: UpdatePendingReimbursementResponseModel) {
                    showProgressbar.value = false
                    updatePendingReimbursementResponseData.value = result
                }

                override fun onError(apiError: ApiError?) {
                    showProgressbar.value = false
                    messageData.value = apiError?.message.toString()
                }
            })
    }

    fun callPendingReimbursementListApi(request: PendingReimbursementRequestModel) {
        showProgressbar.value = true
        pendingReimbursementUseCase.invoke(
            viewModelScope,
            request,
            object : UseCaseResponse<PendingReimbursementResponseModel> {
                override fun onSuccess(result: PendingReimbursementResponseModel) {
                    showProgressbar.value = false
                    pendingReimbursementListResponseData.value = result
                }

                override fun onError(apiError: ApiError?) {
                    showProgressbar.value = false
                    messageData.value = apiError?.getErrorMessage()
                }
            })
    }

    fun callUploadReimbursementBillApi(request: UploadReimbursementBillRequestModel) {
        showProgressbar.value = true
        uploadReimbursementBillUseCase.invoke(
            viewModelScope,
            request,
            object : UseCaseResponse<UploadReimbursementBillResponseModel> {
                override fun onSuccess(result: UploadReimbursementBillResponseModel) {
                    showProgressbar.value = false
                    uploadReimbursementBillResponseData.value = result
                }

                override fun onError(apiError: ApiError?) {
                    showProgressbar.value = false
                    messageData.value = apiError?.getErrorMessage()
                }
            })
    }

    fun callReimbursementEndKmApi(request: ReimbursementEndKmRequestModel) {
        showProgressbar.value = true
        reimbursementEndKmUseCase.invoke(
            viewModelScope,
            request,
            object : UseCaseResponse<ReimbursementEndKmResponseModel> {
                override fun onSuccess(result: ReimbursementEndKmResponseModel) {
                    showProgressbar.value = false
                    reimbursementEndKmResponseData.value = result
                }

                override fun onError(apiError: ApiError?) {
                    showProgressbar.value = false
                    messageData.value = apiError?.getErrorMessage()
                }
            })
    }

    fun callReimbursementModeOfTravelApi(request: ModeOfTravelRequestModel) {
        showProgressbar.value = true
        reimbursementModeOfTravelUseCase.invoke(
            viewModelScope,
            request,
            object : UseCaseResponse<ModeOfTravelResponseModel> {
                override fun onSuccess(result: ModeOfTravelResponseModel) {
                    showProgressbar.value = false
                    reimbursementModeOfTravelResponseData.value = result
                }

                override fun onError(apiError: ApiError?) {
                    showProgressbar.value = false
                    messageData.value = apiError?.getErrorMessage()
                }
            })
    }

    fun callReimbursementValidationApi(request: ReimbursementValidationRequestModel) {
        showProgressbar.value = true
        reimbursementValidationUseCase.invoke(
            viewModelScope,
            request,
            object : UseCaseResponse<ReimbursementValidationResponseModel> {
                override fun onSuccess(result: ReimbursementValidationResponseModel) {
                    showProgressbar.value = false
                    reimbursementValidationResponseData.value = result
                }

                override fun onError(apiError: ApiError?) {
                    showProgressbar.value = false
                    messageData.value = apiError?.getErrorMessage()
                }
            })
    }

    fun callReimbursementSubCategoryApi(request: ReimbursementSubCategoryRequestModel) {
        showProgressbar.value = true
        reimbursementSubCategoryUseCase.invoke(
            viewModelScope,
            request,
            object : UseCaseResponse<ReimbursementSubCategoryResponseModel> {
                override fun onSuccess(result: ReimbursementSubCategoryResponseModel) {
                    showProgressbar.value = false
                    reimbursementSubCategoryResponseData.value = result
                }

                override fun onError(apiError: ApiError?) {
                    showProgressbar.value = false
                    messageData.value = apiError?.getErrorMessage()
                }
            })
    }

    fun callReimbursementCategoryApi(request: ReimbursementCategoryRequestModel) {
        showProgressbar.value = true
        reimbursementCategoryUseCase.invoke(
            viewModelScope,
            request,
            object : UseCaseResponse<ReimbursementCategoryResponseModel> {
                override fun onSuccess(result: ReimbursementCategoryResponseModel) {
                    showProgressbar.value = false
                    reimbursementCategoryResponseData.value = result
                }

                override fun onError(apiError: ApiError?) {
                    showProgressbar.value = false
                    messageData.value = apiError?.getErrorMessage()
                }
            })
    }

    fun callInsertReimbursementApi(request: InsertReimbursementRequestModel) {
        showProgressbar.value = true
        insertReimbursementUseCase.invoke(
            viewModelScope,
            request,
            object : UseCaseResponse<InsertReimbursementResponseModel> {
                override fun onSuccess(result: InsertReimbursementResponseModel) {
                    showProgressbar.value = false
                    insertReimbursementResponseData.value = result
                }

                override fun onError(apiError: ApiError?) {
                    showProgressbar.value = false
                    messageData.value = apiError?.getErrorMessage()
                }
            })
    }

    fun callReimbursementListApi(request: ReimbursementListRequestModel) {
        showProgressbar.value = true
        reimbursementListUseCase.invoke(
            viewModelScope,
            request,
            object : UseCaseResponse<ReimbursementListResponseModel> {
                override fun onSuccess(result: ReimbursementListResponseModel) {
                    showProgressbar.value = false
                    reimbursementListResponseData.value = result
                }

                override fun onError(apiError: ApiError?) {
                    showProgressbar.value = false
                    messageData.value = apiError?.getErrorMessage()
                }
            })
    }

    fun callReimbursementDetailsApi(request: ReimbursementDetailsRequestModel) {
        showProgressbar.value = true
        reimbursementDetailsUseCase.invoke(
            viewModelScope,
            request,
            object : UseCaseResponse<ReimbursementDetailsResponseModel> {
                override fun onSuccess(result: ReimbursementDetailsResponseModel) {
                    showProgressbar.value = false
                    reimbursementDetailsResponseData.value = result
                }

                override fun onError(apiError: ApiError?) {
                    showProgressbar.value = false
                    messageData.value = apiError?.getErrorMessage()
                }
            })
    }

    fun callReimbursementBillApi(request: ReimbursementDetailsRequestModel) {
        showProgressbar.value = true
        reimbursementBillUseCase.invoke(
            viewModelScope,
            request,
            object : UseCaseResponse<ReimbursementBillResponseModel> {
                override fun onSuccess(result: ReimbursementBillResponseModel) {
                    showProgressbar.value = false
                    reimbursementBillResponseData.value = result
                }

                override fun onError(apiError: ApiError?) {
                    showProgressbar.value = false
                    messageData.value = apiError?.getErrorMessage()
                }
            })
    }

    fun callGetMonthDetailsApi(request: GetMonthYearDetailsRequestModel) {
//        showProgressbar.value = true
        getMonthDetailsUseCase.invoke(
            viewModelScope,
            request,
            object : UseCaseResponse<GetMonthDetailsResponseModel> {
                override fun onSuccess(result: GetMonthDetailsResponseModel) {
//                    showProgressbar.value = false
                    getMonthDetailsResponseData.value = result
                }

                override fun onError(apiError: ApiError?) {
//                    showProgressbar.value = false
                    messageData.value = apiError?.getErrorMessage()
                }
            })
    }

    fun callGetYearDetailsApi(request: GetMonthYearDetailsRequestModel) {
//        showProgressbar.value = true
        getYearDetailsUseCase.invoke(
            viewModelScope,
            request,
            object : UseCaseResponse<GetYearDetailsResponseModel> {
                override fun onSuccess(result: GetYearDetailsResponseModel) {
//                    showProgressbar.value = false
                    getYearDetailsResponseData.value = result
                }

                override fun onError(apiError: ApiError?) {
//                    showProgressbar.value = false
                    messageData.value = apiError?.getErrorMessage()
                }
            })
    }

    override fun onCleared() {
        viewModelScope.cancel()
        super.onCleared()
    }
}