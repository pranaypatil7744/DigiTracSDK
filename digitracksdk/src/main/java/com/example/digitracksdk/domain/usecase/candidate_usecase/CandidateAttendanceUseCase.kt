package com.example.digitracksdk.domain.usecase.candidate_usecase

import com.example.digitracksdk.domain.model.attendance_regularization_model.InsertAttendanceRegularizationRequestModel
import com.example.digitracksdk.domain.model.attendance_regularization_model.InsertAttendanceRegularizationResponseModel
import com.example.digitracksdk.domain.repository.candidate_repository.CandidateRepository
import com.example.digitracksdk.domain.usecase.base.UseCase

class CandidateAttendanceUseCase constructor(private val candidateRepository: CandidateRepository) :
    UseCase<InsertAttendanceRegularizationResponseModel, Any?>() {
    override suspend fun run(params: Any?): InsertAttendanceRegularizationResponseModel {
        return candidateRepository.callInsertAttendance(params as InsertAttendanceRegularizationRequestModel)
    }
}