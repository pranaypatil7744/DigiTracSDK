package com.example.digitracksdk.data.repository.my_letters_repo_iml

import com.example.digitracksdk.domain.model.my_letters.CandidateLoiListResponseModel
import com.example.digitracksdk.domain.model.my_letters.CandidateOfferLetterListResponseModel
import com.example.digitracksdk.domain.model.my_letters.DownloadCandidateOfferLetterRequestModel
import com.example.digitracksdk.domain.model.my_letters.DownloadCandidateOfferLetterResponseModel
import com.example.digitracksdk.domain.model.my_letters.GetForm16RequestModel
import com.example.digitracksdk.domain.model.my_letters.GetForm16ResponseModel
import com.example.digitracksdk.domain.model.my_letters.OtherLettersRequestModel
import com.example.digitracksdk.domain.model.my_letters.OtherLettersResponseModel
import com.example.digitracksdk.domain.model.my_letters.ViewCandidateLoiRequestModel
import com.example.digitracksdk.domain.model.my_letters.ViewCandidateLoiResponseModel
import com.example.digitracksdk.data.source.remote.ApiService
import com.example.digitracksdk.domain.model.CommonRequestModel
import com.example.digitracksdk.domain.model.my_letters.GetIncrementLetterResponseModel
import com.example.digitracksdk.domain.model.my_letters.OfferLetterAcceptRejectRequestModel
import com.example.digitracksdk.domain.model.my_letters.OfferLetterAcceptRejectResponseModel
import com.example.digitracksdk.domain.model.GnetIdRequestModel
import com.example.digitracksdk.domain.model.my_letters.GetFinancialYearsListRequestModel
import com.example.digitracksdk.domain.model.my_letters.GetFinancialYearsListResponseModel
import com.example.digitracksdk.domain.repository.my_letters_repository.MyLettersRepository

class MyLettersRepositoryImp(private val apiService: ApiService) : MyLettersRepository {
    override suspend fun callCandidateLoiListApi(request: CommonRequestModel): CandidateLoiListResponseModel {
        return apiService.callCandidateLoiListApi(request)
    }

    override suspend fun callViewCandidateLoiApi(request: ViewCandidateLoiRequestModel): ViewCandidateLoiResponseModel {
        return apiService.callViewCandidateLoiApi(request)
    }

    override suspend fun callCandidateOfferLettersListApi(request: CommonRequestModel): CandidateOfferLetterListResponseModel {
        return apiService.callCandidateOfferLettersListApi(request)
    }

    override suspend fun callDownloadCandidateOfferLetterApi(request: DownloadCandidateOfferLetterRequestModel): DownloadCandidateOfferLetterResponseModel {
        return apiService.callDownloadCandidateOfferLetterApi(request)
    }

    override suspend fun callOfferLetterAcceptRejectApi(request: OfferLetterAcceptRejectRequestModel): OfferLetterAcceptRejectResponseModel {
        return apiService.callOfferLetterAcceptRejectApi(request)
    }

    override suspend fun callOtherLettersApi(request: OtherLettersRequestModel): OtherLettersResponseModel {
        return apiService.callOtherLettersApi(request)
    }

    override suspend fun callGetFinancialYearListApi(request: GetFinancialYearsListRequestModel): GetFinancialYearsListResponseModel {
        return apiService.callGetFinancialYearListApi(request)
    }

    override suspend fun callGetForm16Api(request: GetForm16RequestModel): GetForm16ResponseModel {
        return apiService.callGetForm16Api(request)
    }

    override suspend fun callGetIncrementLetterApi(request: GnetIdRequestModel): GetIncrementLetterResponseModel {
        return apiService.callGetIncrementLetterApi(request)
    }

}