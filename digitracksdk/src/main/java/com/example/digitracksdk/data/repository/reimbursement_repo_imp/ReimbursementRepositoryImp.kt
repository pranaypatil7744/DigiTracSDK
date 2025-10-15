package com.example.digitracksdk.data.repository.reimbursement_repo_imp

import com.example.digitracksdk.domain.model.new_reimbursement.DeleteNewReimbursementItemRequestModel
import com.example.digitracksdk.domain.model.new_reimbursement.DeleteNewReimbursementItemResponseModel
import com.example.digitracksdk.domain.model.new_reimbursement.GenerateVoucherFromNewReimbursementRequestModel
import com.example.digitracksdk.domain.model.new_reimbursement.GenerateVoucherFromNewReimbursementResponseModel
import com.example.digitracksdk.domain.model.new_reimbursement.GetMonthDetailsResponseModel
import com.example.digitracksdk.domain.model.new_reimbursement.GetMonthYearDetailsRequestModel
import com.example.digitracksdk.domain.model.new_reimbursement.GetYearDetailsResponseModel
import com.example.digitracksdk.domain.model.new_reimbursement.InsertNewReimbursementRequestModel
import com.example.digitracksdk.domain.model.new_reimbursement.InsertNewReimbursementResponseModel
import com.example.digitracksdk.domain.model.reimbursement_model.CheckReimbursementLimitRequestModel
import com.example.digitracksdk.domain.model.reimbursement_model.CheckReimbursementLimitResponseModel
import com.example.digitracksdk.domain.model.reimbursement_model.CheckReimbursementLimitV1RequestModel
import com.example.digitracksdk.domain.model.reimbursement_model.CheckReimbursementLimitV1ResponseModel
import com.example.digitracksdk.domain.model.reimbursement_model.InsertReimbursementRequestModel
import com.example.digitracksdk.domain.model.reimbursement_model.InsertReimbursementResponseModel
import com.example.digitracksdk.domain.model.reimbursement_model.ModeOfTravelRequestModel
import com.example.digitracksdk.domain.model.reimbursement_model.ModeOfTravelResponseModel
import com.example.digitracksdk.domain.model.reimbursement_model.PendingReimbursementRequestModel
import com.example.digitracksdk.domain.model.reimbursement_model.PendingReimbursementResponseModel
import com.example.digitracksdk.domain.model.reimbursement_model.ReimbursementCategoryRequestModel
import com.example.digitracksdk.domain.model.reimbursement_model.ReimbursementCategoryResponseModel
import com.example.digitracksdk.domain.model.reimbursement_model.ReimbursementEndKmRequestModel
import com.example.digitracksdk.domain.model.reimbursement_model.ReimbursementEndKmResponseModel
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
import com.example.digitracksdk.data.source.remote.ApiService
import com.example.digitracksdk.domain.model.GnetIdRequestModel
import com.example.digitracksdk.domain.model.new_reimbursement.GetNewReimbursementListResponseModel
import com.example.digitracksdk.domain.model.reimbursement_model.ReimbursementBillResponseModel
import com.example.digitracksdk.domain.model.reimbursement_model.ReimbursementDetailsRequestModel
import com.example.digitracksdk.domain.model.reimbursement_model.ReimbursementDetailsResponseModel
import com.example.digitracksdk.domain.model.reimbursement_model.UpdateReimbursementStatusRequestModel
import com.example.digitracksdk.domain.model.reimbursement_model.UpdateReimbursementStatusResponseModel
import com.example.digitracksdk.domain.repository.reimbursement_repository.ReimbursementRepository

class ReimbursementRepositoryImp(private val apiService: ApiService) : ReimbursementRepository {
    override suspend fun callReimbursementListApi(request: ReimbursementListRequestModel): ReimbursementListResponseModel {
        return apiService.callReimbursementListApi(request)
    }

    override suspend fun callReimbursementDetailsApi(request: ReimbursementDetailsRequestModel): ReimbursementDetailsResponseModel {
        return apiService.callReimbursementDetailsApi(request)
    }

    override suspend fun callReimbursementBillApi(request: ReimbursementDetailsRequestModel): ReimbursementBillResponseModel {
        return apiService.callReimbursementBillApi(request)
    }

    override suspend fun callReimbursementCategoryApi(request: ReimbursementCategoryRequestModel): ReimbursementCategoryResponseModel {
        return apiService.callReimbursementCategoryApi(request)
    }

    override suspend fun callReimbursementSubCategoryApi(request: ReimbursementSubCategoryRequestModel): ReimbursementSubCategoryResponseModel {
        return apiService.callReimbursementSubCategoryApi(request)
    }

    override suspend fun callReimbursementModeOfTravelApi(request: ModeOfTravelRequestModel): ModeOfTravelResponseModel {
        return apiService.callReimbursementModeOfTravelApi(request)
    }

    override suspend fun callReimbursementEndKmApi(request: ReimbursementEndKmRequestModel): ReimbursementEndKmResponseModel {
        return apiService.callReimbursementEndKmApi(request)
    }

    override suspend fun callRejectedEndKmApi(request: RejectedEndKmRequestModel): RejectedEndKmResponseModel {
        return apiService.callRejectedEndKmApi(request)
    }

    override suspend fun callGetReimbursementListForVoucherApi(request: GnetIdRequestModel): GetNewReimbursementListResponseModel {
        return apiService.callGetReimbursementListForVoucherApi(request)
    }

    override suspend fun callDeleteNewReimbursementListItem(request: DeleteNewReimbursementItemRequestModel): DeleteNewReimbursementItemResponseModel {
        return apiService.callDeleteNewReimbursementListItemApi(request)
    }

    override suspend fun callGenerateNewReimbursementVoucher(request: GenerateVoucherFromNewReimbursementRequestModel): GenerateVoucherFromNewReimbursementResponseModel {
        return apiService.callGenerateNewReimbursementVoucherApi(request)
    }

    override suspend fun callInsertNewReimbursementApi(request: InsertNewReimbursementRequestModel): InsertNewReimbursementResponseModel {
        return apiService.callInsertNewReimbursementApi(request)
    }

    override suspend fun callGetNewReimbursementPendingListApi(request: ReimbursementListRequestModel): ReimbursementListResponseModel {
        return apiService.callGetNewReimbursementPendingListApi(request)
    }

    override suspend fun callGetNewReimbursementApprovedListApi(request: ReimbursementListRequestModel): ReimbursementListResponseModel {
        return apiService.callGetNewReimbursementApprovedListApi(request)
    }

    override suspend fun callGetNewReimbursementRejectedListApi(request: ReimbursementListRequestModel): ReimbursementListResponseModel {
        return apiService.callGetNewReimbursementRejectedListApi(request)
    }

    override suspend fun callGetMonthDetailApi(request: GetMonthYearDetailsRequestModel): GetMonthDetailsResponseModel {
        return apiService.callGetMonthDetailApi(request)
    }

    override suspend fun callGetYearDetailApi(request: GetMonthYearDetailsRequestModel): GetYearDetailsResponseModel {
        return apiService.callGetYearDetailApi(request)
    }

    override suspend fun callReimbursementValidationApi(request: ReimbursementValidationRequestModel): ReimbursementValidationResponseModel {
        return apiService.callReimbursementValidationApi(request)
    }

    override suspend fun callInsertReimbursementApi(request: InsertReimbursementRequestModel): InsertReimbursementResponseModel {
        return apiService.callInsertReimbursementApi(request)
    }

    override suspend fun callUploadReimbursementBillApi(request: UploadReimbursementBillRequestModel): UploadReimbursementBillResponseModel {
        return apiService.callUploadReimbursementBillApi(request)
    }

    override suspend fun callPendingReimbursementListApi(request: PendingReimbursementRequestModel): PendingReimbursementResponseModel {
        return apiService.callPendingReimbursementListApi(request)
    }

    override suspend fun callUpdatePendingReimbursementApi(request: UpdatePendingReimbursementRequestModel): UpdatePendingReimbursementResponseModel {
        return apiService.callUpdatePendingReimbursementApi(request)
    }

    override suspend fun callUpdateReimbursementStatusApi(request: UpdateReimbursementStatusRequestModel): UpdateReimbursementStatusResponseModel {
        return apiService.callUpdateReimbursementStatusApi(request)
    }

    override suspend fun callUpdateReimbursementDetailsApi(request: UpdateReimbursementDetailsRequestModel): UpdateReimbursementDetailsResponseModel {
        return apiService.callUpdateReimbursementDetailsApi(request)
    }

    override suspend fun callRejectedReimbursementValidationApi(request: RejectedReimbursementValidationRequestModel): RejectedReimbursementValidationResponseModel {
        return apiService.callRejectedReimbursementValidationApi(request)
    }

    override suspend fun callCheckReimbursementLimitV1Api(request: CheckReimbursementLimitV1RequestModel): CheckReimbursementLimitV1ResponseModel {
        return apiService.callCheckReimbursementLimitV1Api(request)
    }

    override suspend fun callCheckReimbursementLimitApi(request: CheckReimbursementLimitRequestModel): CheckReimbursementLimitResponseModel {
        return apiService.callCheckReimbursementLimitApi(request)
    }

    override suspend fun callSaveReimbursementPreApprovalApi(request: SaveReimbursementPreApprovalRequestModel): SaveReimbursementPreApprovalResponseModel {
        return apiService.callSaveReimbursementPreApprovalApi(request)
    }
}