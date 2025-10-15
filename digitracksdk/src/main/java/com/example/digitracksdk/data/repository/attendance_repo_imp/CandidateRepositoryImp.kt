package com.example.digitracksdk.data.repository.attendance_repo_imp

import com.example.digitracksdk.data.source.remote.ApiService
import com.example.digitracksdk.domain.model.attendance_regularization_model.InsertAttendanceRegularizationRequestModel
import com.example.digitracksdk.domain.model.attendance_regularization_model.InsertAttendanceRegularizationResponseModel
import com.example.digitracksdk.domain.repository.candidate_repository.CandidateRepository


class CandidateRepositoryImp constructor(private val apiServiceCandidate: ApiService):
    CandidateRepository {
    override suspend fun callInsertAttendance(request: InsertAttendanceRegularizationRequestModel): InsertAttendanceRegularizationResponseModel
    {
       return  apiServiceCandidate.callAttendanceRegularizationInsertApi(
           request.Empcode,
           request.RequestTypeId,
           request.RegularizationDate,
           request.InTime,
           request.OutTime,
           request.Location,
           request.Remarks,
           request.ToDate
       )
    }

}