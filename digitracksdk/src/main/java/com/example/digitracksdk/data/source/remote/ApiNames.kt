package com.example.digitracksdk.data.source.remote

class ApiNames {
    companion object {
        // todo this is main app URL
        const val BASE_URL = "https://paperlessonboardinglive.innov.in/"  // for NewDigiTrac
//        const val BASE_URL = "https://ebDigiTrackpaperlessonboarding.fmdigione.com"  // for EBDigiTrac


//        const val BASE_URL_CANDIDATE = "http://paperlessonboardinglive.innov.in/"
        //        const val BASE_URL_CANDIDATE = "http://candidateappserviceapi.innov.in/"
//        const val BASE_URL_CANDIDATE_APP = "http://paperlessonboardinglive.innov.in/"

        // for NewDigiTrac
        const val BASE_URL_CANDIDATE_APP = "https://candidateappapi.innov.in/"
        const val BASE_URL_ASSOCIATE = "https://associateverifciationzoop.innov.in/"
        const val BASE_URL_ASSOCIATE_FOR_PAN_AND_BANK = "https://associateverifciationzoop.fmdigione.com/"
        const val BASE_URL_DIGI_ONE = "https://digioneapi.fmdigione.com/"
        const val BASE_URL_Innov_DIGI_ONE = " https://innov.fmdigione.com/"

        // for EBDigiTrac
//        const val BASE_URL_CANDIDATE_APP = "https://ebDigiTrackpaperlessonboarding.fmdigione.com"
//        const val BASE_URL_ASSOCIATE = "https://ebDigiTrackpaperlessonboarding.fmdigione.com"
//        const val BASE_URL_ASSOCIATE_FOR_PAN_AND_BANK = "https://ebDigiTrackpaperlessonboarding.fmdigione.com"
//        const val BASE_URL_DIGI_ONE = "https://ebDigiTrackpaperlessonboarding.fmdigione.com"
//        const val BASE_URL_Innov_DIGI_ONE = "https://ebDigiTrackpaperlessonboarding.fmdigione.com"



        const val LoginApi = "api/TransferCandidateLogin"
        const val FirebaseTokenUpdateApi = "api/UpdateFireBaseToken"
        const val GeoTrackingUpdateApi = "api/InsertAssociateGeoTrackingNew"
        const val CheckAppVersionApi = "api/GetDigitracVersion"
        const val VerifyLoginOtpApi = "api/LoginOTPValidate"
        const val HomeDashboardApi = "Api/DTGetInformationOfCandidate"
        const val CheckBirthdayApi = "Api/Birthday"
        const val HomeDashboardBannerApi = "api/POBGetBanners"
        const val DTCandidateInfo = "Api/DTCandidateInfo"
        const val GetOpenDemandForCandidates = "Api/GetOpenDemandForCandidates"
        const val GetReferredList = "Api/GetReferredList"
        const val EsicCardApi = "Api/GetEsicCardImage"
        const val EsicMedicalCardApi = "Api/MedicalCardAcceptanceDownload"
        const val MedicalCardApi = "Api/GetMedicalCardImage"
        const val HelpAndSupportListApi = "Api/GetAssociateIssueList"
        const val GetIssueCategoryApi = "Api/GetIssueCategory"
        const val GetIssueSubCategoryApi = "Api/GetIssueSubCategory"
        const val AddAssociateIssueApi = "Api/AssociateIssue"
        const val GetResignationCategoryApi = "Api/GetResignationCategory"
        const val GetResignationRequestApi = "Api/InsertResignationRequest"
        const val GetBranchDetailsListApi = "Api/GetBranchDetails"
        const val GetSkillListApi = "Api/GetSkillset"
        const val JobReferralApi = "Api/JobReferral"
        const val GetTrainingApi = "Api/GetClientTrainingDetails_Video"


        const val GetHolidaysList = "Api/HolidaysList"
        const val LeaveApplicationApi = "Api/LeaveApplication"
        const val GetLeaveTypesApi = "Api/GetLeaveType"
        const val GetLeaveStatusSummaryApi = "Api/GetAssociateLeaveStatusSummary"
        const val GetOtherLettersApi = "api/GetAssociateAllLetters"
        const val GetLeaveBalancedApi = "Api/GetAssociateLeaveBalance"
        const val GetLeaveBalanceStatusApi = "Api/GetLeaveBalanceStatus"
        const val ViewLeaveRequestApi = "Api/ViewAppliedLeave"

        const val GetPolicyAcknowledgeStatus = "Api/GetPolicyAcknowledgeStatus"
        const val GetClientPolicy = "Api/GetClientPolicyDetails"
        //        const val GetViewPolicyApi = "Api/GetClientPolicyImage"
        const val GetViewPolicyApi = "Api/GetClientPolicylink"
        const val InsertAcknowledgeApi = "Api/InsertPolicyAcknowlegement"
        const val DownloadPolicyApi = "api/PolicyDocumentDownload/DownloadPolicy"

        const val IssueDetailsApi = "Api/GetAssociateIssueDetails"
        const val GetReimbursementListApi = "Api/GetAssociateReimbursementVoucherList"
        const val UpdatePendingReimbursementListApi = "Api/UpdatePendingVoucherList"
        const val RejectedReimbursementValidationApi = "Api/ReimbursementRejectionValidation"
        const val CheckReimbursementLimitApi = "Api/CheckReimbursementLimit"
        const val CheckReimbursementLimitV1Api = "Api/CheckReimbursementLimitV1"
        const val UpdateReimbursementDetailsListApi = "Api/UpdateAssociateReimbursementDeatils"
        const val RequestForUpdateReimbursementStatusApi =
            "Api/UpdateAssociateReimbursementApprovalStatus"
        const val GetPendingReimbursementListApi = "Api/PendingVoucherForFinalVoucher"
        const val GetReimbursementDetailsApi = "Api/GetAssociateReimbursementDetails"
        const val GetReimbursementBillApi = "Api/GetAssociateReimbursementDocuments"
        const val InsertReimbursementBillApi = "Api/InsertAssociateReimbursementDocuments"
        const val GetReimbursementCategoryApi = "Api/GetReimbursementCategory"
        const val GetReimbursementModeOfTravelApi = "Api/GetModeOfTravel"
        const val GetReimbursementEndKmApi = "api/AssociateReimbursementEndKM"
        const val GetRejectedVoucherEndKmApi = "api/StartandEndKMForRejectVoucher"
        const val GetReimbursementSubCategoryApi = "Api/GetReimbursementSubCategory"
        const val GetReimbursementValidationApi = "Api/ReimbursementValidation"
        const val InsertReimbursementApi = "Api/InsertAssociateReimbursement"
        const val SaveReimbursementPreApprovalApi = "Api/SaveReimbursementPreApproval"
        const val GetPaySlipMonthsYearsApi = "api/PaySlipMonth"
        const val GetSalaryStatusApi = "Api/GetSalaryReleaseStatus"
        const val DownloadPaySlipApi = "api/PaySlip/DownloadPayslip"
        const val GetCandidateLoiListApi = "Api/GetCandidateLOIList"
        const val ViewCandidateLoiApi = "Api/LOI/POBGetLOIImage"
        const val GetCandidateOfferLetterListApi = "Api/GetCandidateOfferList1"
        const val OfferLetterAcceptRejectApi = "Api/OfferLetterAcceptReject"
        const val DownloadCandidateOfferLetterApi = "Api/DownloadCandOfferLetter"
        const val GetStateListApi = "api/GetStateList1"
        const val GetCityListApi = "api/GetCityList1"
        const val UpdateProfileApi = "Api/UpdateCandidateDetails"
        const val GetAttendanceRegularizationApi = "Api/GetAttendanceRegularization"
        const val GetViewAttendanceApi = "Api/GetDigiTracAttendaceTimeAsheet"
        const val AttendanceRegularizationInsertApi = "api/AttendanceRegularization/Insert"
        const val InsertAttendanceRegularizationApi = "api/InsertAttendanceRegularization"
        const val GetAttendanceRegularizationTypeApi = "api/GetAttnRegularizationType"
        const val InsertMileageRegularizationApi = "api/InsertMileageRegularization"
        const val GetMileageRegularizationListApi = "api/GetMileageRegularizationDetails"
        const val InsertMileageApi = "Api/InsertMileage"
        const val GetMileageDetailsApi = "Api/GetMileageDetails"
        const val GetMileageTrackingFlagApi = "Api/GetMileageTrackingFlag"
        const val GetValidCandidateApi = "Api/GetValidCandidate"
        const val CheckOTPApi = "Api/ValidateOTP/CheckOTP"
        const val InsertBasicDetailsApi = "Api/PaperOnboardingAPI/POBInsertBasicDetails"
        const val InnovIDCreationNewApi = "Api/InnovIDCreationNew"

        // onBoarding

        const val GetEpfDetailsApi = "Api/GetEpfDetails"
        const val GetEsicDetailsApi = "Api/GetESICNoDetailsV1"
        const val GetFamilyDetailsApi = "Api/GetFamilyDetails"
        const val GetEducationDetailsApi = "Api/GetEducationDetails"
        const val GetWorkExperianceDetailsApi = "Api/GetWorkExperianceDetails"
        const val GetCandidateBasicDetailsApi = "Api/GetCandidateBasicDetails"
        const val GetBankDetailsApi = "Api/GetBankDetails"
        const val UpdateBankDetailsApi = "Api/BankDetails"
        const val GetCandidateReferenceDetailsApi = "Api/GetCandidateReferenceDetails"
        const val GetSignatureApi = "Api/GetSignature"
//        soap to rest

        const val GetKycDocumentsListApi = "Api/DTGetKYCDocs"
        const val DTGetPersonalDocsApi = "Api/DTGetPersonalDocs"
        const val GetEducationCategoryApi = "Api/GetEducationCategory"


        // insert Onboarding

        const val UpdateCandidateBasicDetailsApi = "api/UpdateCandidateBasicDetails"
        const val FamilyDetailsApi = "Api/FamilyDetails"
        const val POBInsertEducationInfoApi =
            "Api/PaperlessOnboardingAcademicDetailsAPI/POBInsertEducationInfo"
        const val POBInsertWorkExpApi = "Api/POBInsertWorkExp"
        const val POBInsertEpfApi = "Api/EPF/POBInsertEpf"
        const val POBInsertESICDetailsApi = "Api/POBInsertESICDetails"
        const val CandidateProfileAcknowledgementApi = "Api/CandidateProfileAcknowledgement"
        const val GetReferenceCategoryApi = "Api/GetReferenceCategory"
        const val InsertCandidateReferenceDetailsApi = "Api/InsertCandidateReferenceDetails"
        const val InsertSignatureApi = "Api/CandidateProfileAcknowledgement"
        const val InsertWorkExpApi = "Api/WorkExperience/POBInsertWorkExp"
        // soap to rest

        const val GetKycDocumentListApi = "Api/GetDocumentTypeKyc"
        const val GetAllDocumentWithoutKycApi = "Api/GetAllDocumentWithoutKyc"
        const val InsertDocumentsFileSystemDTApi = "Api/InsertDocumentsFileSystemDT"

        const val GetBankListApi = "Api/GetBanks"
        const val GetChequeValidationApi = "Api/ChequeValidation"

        //Attendance
        const val GetDashboardAttendanceStatusApi = "Api/GetAttendanceStatus4"
        const val GetAttendanceTimeSheetApi = "api/GetAttendance2"
        const val GetGeoFancingApi = "Api/Geofancing"
        const val GetAttendanceStatusApi = "api/GetAttendanceStatus"
        const val UpdateAttendanceStatusApi = "api/UpdateAttendanceStatus"
        const val AttendanceValidationApi = "Api/AttendanceValidation"
        const val CurrentDayAttendanceStatusApi = "Api/GetTodayAttendanceStatus1"
        const val GetAttendanceZoneApi = "Api/GetAssosiateAttendenceZone"
        const val CreateAttendanceTokenApi = "Api/CreateAttendanceToken1"
        const val CreateLogYourVisitTokenApi = "Api/CreateLogYourVisitToken"
        const val CheckValidAttendanceTokenApi = "Api/CheckValidAttendanceToken1"
        const val CheckValidLogYourVisitTokenApi = "Api/CheckValidLogYourVisitToken"
        const val InsertAttendancePicApi = "Api/InsertAssociatePicture"
        const val GetLogYourVisitApi = "api/GetLYVLog"
        const val GetBeaconListApi = "Api/GetBeaconList"

        const val GetNotificationListApi = "Api/GetMobileNotifications"
        const val GetNotificationDetailsApi = "Api/GetMobileNotificationImage"


        const val GetViewTrainingDocumentApi = "Api/GetClientTrainingImage"
        const val GetAadhaarVerificationSendOtpApi = "api/SentOTPForAadhaarVerificationWithInnovID"


        const val GetAadhaarVerificationOtpValidationApi = "api/VerifyOTPForAadhaarVerification"
        const val GetValidateAadhaarApi = "Api/ValidateAadhar"
        const val GetGetAadharVerificationDetailsApi = "Api/GetAadharVerificationDetails"
        const val GetOnboardingDashboardApi = "api/GetOnboardingDashboardDetails"


        const val GetResignationListApi = "Api/GetResignationView"
        const val GetHireCraftPayslipDownloadApi = "Api/GetHireCraftPayslipDownload"

        const val ViewPayout = "Api/ViewReimbursement"
        const val GetRefineUrl = "api/EmployeeInfoes/Token"
        const val GetUploadedDocumentsList = "api/GetCandidateDocList"
        const val GetUploadedDocument = "api/GetDocument"

        const val IncomeTaxDeclarationApi = "api/IncomeTaxDeclaration"
        const val GetForm16Api = "api/getForm16"
        const val GetFinancialYearListApi = "api/GetFinancialyearList"

        const val GetGeoTrackingSummaryList = "Api/GetAssociateGeoTrackingList"

        const val GetGeoTrackingDetails = "api/GetAssociateGeoTrackingDetails"

        const val GetReimbursementListForVoucher = "api/GetReimbursementDetailsforVoucherGeneration"
        const val GetNewReimbursementPendingListApi = "Api/GetAssociateReimbursementVoucherList"
        const val GetNewReimbursementApprovedListApi = "Api/GetAssociateReimbursementVoucherList"
        const val GetNewReimbursementRejectedListApi = "Api/GetAssociateReimbursementVoucherList"
        const val GenerateNewReimbursementVoucher = "api/GenerateVoucherForReimbursementDetails"
        const val InsertNewReimbursementApi = "api/InsertDigitracAssociateReimbursement"
        const val DeleteNewReimbursementListItem = "api/RemibursementDeletion"
        const val GetIncrementLetterApi = "api/GetIncrementLetter"
        const val GetCustomerIdCard = "api/GetCustomerIdCardLink"
//        const val GetCustomerIdCard = "IdCardView/GenerateIdCardByAPI"

        const val GetAttendanceFlag = "Api/getAttendanceFlag"
        const val GetAttendanceMarking = "Api/GetAttendanceMarking"

        const val GetYearDetails = "api/GetYearDetails"
        const val GetMonthDetails = "api/GetMonthDetails"

        const val GetPanVerificationDetailsApi = "Api/GetPanVerificationDetails"
        const val GetPanVerificationApi = "API/PanverificationDetails"

        const val GetBankAccountVerificationDetailsApi = "Api/GetBankAccountVerificationDetails"
        const val GetBankAccountVerificationApi = "API/BankAccountVerification"

        const val GetCibilScore = "api/GetCibilScore"
        const val GetQrCode = "Api/GetQRCodeGenerator"

        const val InsertPFESIC = "api/InserPFESIC"
        const val GetPFESICDetails = "api/GetPFESICDetails"

        const val GetEducationStream = "Api/GetEducationStream"
        const val GetResignationNoticePeriod = "Api/GetResignationNoticePeriod"
        const val RevokeResignation = "api/InsertRevokeresignation"
        const val GetResignationReason = "api/GetResignationReason"

        const val AttendanceCycleApi = "Api/AttendanceCycle"
        const val LeaveTypeHexCodeApi = "Api/LeaveTypeHexCode"
        const val InsertExitInterviewApi = "api/InsertExitInterview"

        const val InductionTrainingApi = "api/InductionTrainingAcknowledgement"
        const val GetSurveyLink = "api/GetSurveyLink"

        const val GetRewardsApi = "api/getAssociateRewardsList"

        const val AadhaarVerificationApi = "api/AdharVerificationByDigiLocker"
        const val AadhaarSaveRequestId = "api/SaveAdharRequestIDDigilocker"
        const val GetAadhaarData = "api/getAdharDetailDigilocker"
    }
}