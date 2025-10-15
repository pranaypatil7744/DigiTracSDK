package com.example.digitracksdk.domain.repository.my_letters_repository

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
import com.example.digitracksdk.domain.model.CommonRequestModel
import com.example.digitracksdk.domain.model.my_letters.GetIncrementLetterResponseModel
import com.example.digitracksdk.domain.model.my_letters.OfferLetterAcceptRejectRequestModel
import com.example.digitracksdk.domain.model.my_letters.OfferLetterAcceptRejectResponseModel
import com.example.digitracksdk.domain.model.GnetIdRequestModel
import com.example.digitracksdk.domain.model.my_letters.GetFinancialYearsListRequestModel
import com.example.digitracksdk.domain.model.my_letters.GetFinancialYearsListResponseModel

interface MyLettersRepository {

    suspend fun callCandidateLoiListApi(request: CommonRequestModel): CandidateLoiListResponseModel

    suspend fun callViewCandidateLoiApi(request: ViewCandidateLoiRequestModel): ViewCandidateLoiResponseModel

    suspend fun callCandidateOfferLettersListApi(request: CommonRequestModel): CandidateOfferLetterListResponseModel

    suspend fun callDownloadCandidateOfferLetterApi(request: DownloadCandidateOfferLetterRequestModel): DownloadCandidateOfferLetterResponseModel

    suspend fun callOfferLetterAcceptRejectApi(request: OfferLetterAcceptRejectRequestModel): OfferLetterAcceptRejectResponseModel

    suspend fun callOtherLettersApi(request: OtherLettersRequestModel): OtherLettersResponseModel

    suspend fun callGetFinancialYearListApi(request: GetFinancialYearsListRequestModel): GetFinancialYearsListResponseModel

    suspend fun callGetForm16Api(request: GetForm16RequestModel): GetForm16ResponseModel

    suspend fun callGetIncrementLetterApi(request: GnetIdRequestModel): GetIncrementLetterResponseModel

}