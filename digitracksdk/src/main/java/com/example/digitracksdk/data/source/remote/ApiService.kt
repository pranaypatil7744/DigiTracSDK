package com.example.digitracksdk.data.source.remote

import com.example.digitracksdk.domain.model.attendance_model.AttendanceCycleRequestModel
import com.example.digitracksdk.domain.model.attendance_model.AttendanceCycleResponseModel
import com.example.digitracksdk.domain.model.attendance_model.AttendanceStatusResponseModel
import com.example.digitracksdk.domain.model.attendance_model.AttendanceZoneResponseModel
import com.example.digitracksdk.domain.model.client_policies.ClientPoliciesRequestModel
import com.example.digitracksdk.domain.model.client_policies.ClientPoliciesResponseModel
import com.example.digitracksdk.domain.model.help_and_support.IssueCategoryRequestModel
import com.example.digitracksdk.domain.model.help_and_support.IssueCategoryResponseModel
import com.example.digitracksdk.domain.model.leaves.LeavesTypeResponseModel
import com.example.digitracksdk.domain.model.onboarding.documents.InsertPendingDocumentsRequestModel
import com.example.digitracksdk.domain.model.onboarding.documents.InsertPendingDocumentsResponseModel
import com.example.digitracksdk.domain.model.onboarding.documents.PendingDocumentsListRequestModel
import com.example.digitracksdk.domain.model.onboarding.documents.PendingDocumentsListResponseModel
import com.example.digitracksdk.domain.model.pay_slip.PaySlipMonthYearRequestModel
import com.example.digitracksdk.domain.model.pay_slip.PaySlipMonthYearsResponseModel
import com.example.digitracksdk.domain.model.CommonRequestModel
import com.example.digitracksdk.domain.model.GnetIdRequestModel
import com.example.digitracksdk.domain.model.customer_id_card.CustomerIdCardResponseModel
import com.example.digitracksdk.domain.model.geo_tracking_model.GeoTrackingDetailsRequestModel
import com.example.digitracksdk.domain.model.geo_tracking_model.GeoTrackingDetailsResponseModel
import com.example.digitracksdk.domain.model.geo_tracking_model.GeoTrackingSummaryListRequestModel
import com.example.digitracksdk.domain.model.geo_tracking_model.GeoTrackingSummaryListResponseModel
import com.innov.digitrac.domain.model.home_model.HomeBannerRequestModel
import com.innov.digitrac.domain.model.home_model.HomeBannerResponseModel
import com.example.digitracksdk.domain.model.home_model.request.InnovIDCardRequestModel
import com.example.digitracksdk.domain.model.leaves.ApplyLeaveRequestModel
import com.example.digitracksdk.domain.model.leaves.ApplyLeaveResponseModel
import com.example.digitracksdk.domain.model.my_letters.OtherLettersRequestModel
import com.example.digitracksdk.domain.model.my_letters.OtherLettersResponseModel
import com.example.digitracksdk.domain.model.onboarding.EmpIDRequestModel
import com.example.digitracksdk.domain.model.onboarding.GetEducationCategoryResponseModel
import com.example.digitracksdk.domain.model.home_model.response.CheckBirthdayResponseModel
import com.example.digitracksdk.domain.model.home_model.response.HomeDashboardRequestModel
import com.example.digitracksdk.domain.model.home_model.response.HomeDashboardResponseModel
import com.example.digitracksdk.domain.model.home_model.response.InnovIDCardResponseModel
import com.example.digitracksdk.domain.model.income_tax.IncomeTaxDeclarationRequestModel
import com.example.digitracksdk.domain.model.income_tax.IncomeTaxDeclarationResponseModel
import com.example.digitracksdk.domain.model.notification.NotificationDetailsRequestModel
import com.example.digitracksdk.domain.model.notification.NotificationDetailsResponseModel
import com.example.digitracksdk.domain.model.notification.NotificationListResponseModel
import com.example.digitracksdk.domain.model.onboarding.kyc.KycDocumentListResponseModel
import com.example.digitracksdk.domain.model.onboarding.kyc.KycDocumentPendingListRequestModel
import com.example.digitracksdk.domain.model.onboarding.kyc.KycDocumentPendingListResponseModel
import com.example.digitracksdk.domain.model.onboarding.signature_model.InsertSignatureRequestModel
import com.example.digitracksdk.domain.model.onboarding.signature_model.InsertSignatureResponseModel
import com.example.digitracksdk.domain.model.onboarding.work_experience.InsertWorkExpRequestModel
import com.example.digitracksdk.domain.model.onboarding.work_experience.InsertWorkExpResponseModel
import com.example.digitracksdk.domain.model.refine.RefineRequest
import com.example.digitracksdk.domain.model.refine.RefineResponseModel
import com.example.digitracksdk.domain.model.training_model.TrainingRequestModel
import com.example.digitracksdk.domain.model.training_model.TrainingResponseModel
import com.example.digitracksdk.domain.model.training_model.ViewTrainingDocumentRequestModel
import com.example.digitracksdk.domain.model.training_model.ViewTrainingDocumentResponseModel
import com.example.digitracksdk.domain.model.uploaded_documents.GetDocumentRequestModel
import com.example.digitracksdk.domain.model.uploaded_documents.GetDocumentResponseModel
import com.example.digitracksdk.domain.model.uploaded_documents.UploadedDocumentsRequestModel
import com.example.digitracksdk.domain.model.uploaded_documents.UploadedDocumentsResponseModel
import com.example.digitracksdk.domain.model.verify_otp_model.LoginOtpVerifyRequestModel
import com.example.digitracksdk.domain.model.verify_otp_model.LoginOtpVerifyResponseModel
import com.example.digitracksdk.domain.model.view_payout.ViewPayoutRequest
import com.example.digitracksdk.domain.model.view_payout.ViewPayoutResponseModel
import com.example.digitracksdk.domain.usecase.attendance_usecase.InsertAttendancePicRequestModel
import com.example.digitracksdk.domain.usecase.attendance_usecase.InsertAttendancePicResponseModel
import com.example.digitracksdk.presentation.home.jobs_and_refer_fds.jobs.model.JobReferralRequestModel
import com.example.digitracksdk.presentation.home.jobs_and_refer_fds.jobs.model.JobReferralResponseModel
import com.example.digitracksdk.presentation.home.jobs_and_refer_fds.jobs.model.ReferredUsersRequestModel
import com.example.digitracksdk.presentation.home.jobs_and_refer_fds.jobs.model.ReferredUsersResponseModel
import com.example.digitracksdk.presentation.pf_esic_insurance.model.PfEsicRequestModel
import com.example.digitracksdk.presentation.pf_esic_insurance.model.PfEsicResponseModel
import com.example.digitracksdk.domain.model.attendance_model.AttendanceFlagRequestModel
import com.example.digitracksdk.domain.model.attendance_model.AttendanceFlagResponseModel
import com.example.digitracksdk.domain.model.attendance_model.AttendanceGeoFancingResponseModel
import com.example.digitracksdk.domain.model.attendance_model.AttendanceTimeSheetRequestModel
import com.example.digitracksdk.domain.model.attendance_model.AttendanceTimeSheetResponseModel
import com.example.digitracksdk.domain.model.attendance_model.AttendanceValidationRequestModel
import com.example.digitracksdk.domain.model.attendance_model.AttendanceValidationResponseModel
import com.example.digitracksdk.domain.model.attendance_model.CheckValidAttendanceTokenRequestModel
import com.example.digitracksdk.domain.model.attendance_model.CheckValidAttendanceTokenResponseModel
import com.example.digitracksdk.domain.model.attendance_model.CheckValidLogYourVisitRequestModel
import com.example.digitracksdk.domain.model.attendance_model.CheckValidLogYourVisitResponseModel
import com.example.digitracksdk.domain.model.attendance_model.CreateAttendanceTokenRequestModel
import com.example.digitracksdk.domain.model.attendance_model.CreateAttendanceTokenResponseModel
import com.example.digitracksdk.domain.model.attendance_model.CreateLogYourVisitTokenRequestModel
import com.example.digitracksdk.domain.model.attendance_model.CreateLogYourVisitTokenResponseModel
import com.example.digitracksdk.domain.model.attendance_model.CurrentDayAttendanceStatusRequestModel
import com.example.digitracksdk.domain.model.attendance_model.CurrentDayAttendanceStatusResponseModel
import com.example.digitracksdk.domain.model.attendance_model.DashboardAttendanceStatusResponseModel
import com.example.digitracksdk.domain.model.attendance_model.LeaveHexCodeRequestModel
import com.example.digitracksdk.domain.model.attendance_model.LeaveHexCodeResponseModel
import com.example.digitracksdk.domain.model.attendance_model.LogYourVisitRequestModel
import com.example.digitracksdk.domain.model.attendance_model.LogYourVisitResponseModel
import com.example.digitracksdk.domain.model.attendance_model.UpdateAttendanceStatusRequestModel
import com.example.digitracksdk.domain.model.attendance_model.UpdateAttendanceStatusResponseModel
import com.example.digitracksdk.domain.model.attendance_model.ViewAttendanceRequestModel
import com.example.digitracksdk.domain.model.attendance_model.ViewAttendanceResponseModel
import com.example.digitracksdk.domain.model.attendance_regularization_model.AttendanceRegularizationListRequestModel
import com.example.digitracksdk.domain.model.attendance_regularization_model.AttendanceRegularizationListResponseModel
import com.example.digitracksdk.domain.model.attendance_regularization_model.AttendanceRegularizationTypeResponseModel
import com.example.digitracksdk.domain.model.attendance_regularization_model.InsertAttendanceRegularizationRequestModel
import com.example.digitracksdk.domain.model.attendance_regularization_model.InsertAttendanceRegularizationResponseModel
import com.example.digitracksdk.domain.model.client_policies.AcknowledgeRequestModel
import com.example.digitracksdk.domain.model.client_policies.AcknowledgeResponseModel
import com.example.digitracksdk.domain.model.client_policies.PolicyAcknowledgeRequestModel
import com.example.digitracksdk.domain.model.client_policies.PolicyAcknowledgeResponseModel
import com.example.digitracksdk.domain.model.client_policies.ViewAckPolicyRequestModel
import com.example.digitracksdk.domain.model.client_policies.ViewAckPolicyResponseModel
import com.example.digitracksdk.domain.model.client_policies.ViewPolicyRequestModel
import com.example.digitracksdk.domain.model.client_policies.ViewPolicyResponseModel
import com.example.digitracksdk.domain.model.leaves.HolidaysListResponseModel
import com.example.digitracksdk.domain.model.leaves.LeaveRequestViewRequestModel
import com.example.digitracksdk.domain.model.leaves.LeaveRequestViewResponseModel
import com.example.digitracksdk.domain.model.mileage_tracking_model.InsertMileageRegularizationRequestModel
import com.example.digitracksdk.domain.model.mileage_tracking_model.InsertMileageRegularizationResponseModel
import com.example.digitracksdk.domain.model.my_letters.CandidateOfferLetterListResponseModel
import com.example.digitracksdk.domain.model.my_letters.GetForm16RequestModel
import com.example.digitracksdk.domain.model.my_letters.GetForm16ResponseModel
import com.example.digitracksdk.domain.model.new_reimbursement.GetMonthDetailsResponseModel
import com.example.digitracksdk.domain.model.onboarding.documents.ViewDocumentsResponseModel
import com.example.digitracksdk.domain.model.onboarding.insert.CandidateProfileAckModel
import com.example.digitracksdk.domain.model.onboarding.insert.CandidateProfileAckResponseModel
import com.example.digitracksdk.domain.model.onboarding.insert.InsertEducationInfoRequestModel
import com.example.digitracksdk.domain.model.onboarding.insert.InsertEducationInfoResponseModel
import com.example.digitracksdk.domain.model.onboarding.insert.POBInsertESICDetailsModel
import com.example.digitracksdk.domain.model.onboarding.insert.POBInsertEsicResponseModel
import com.example.digitracksdk.domain.model.customer_id_card.CustomerIdCardRequestModel
import com.example.digitracksdk.domain.model.help_and_support.AssociateIssueRequestModel
import com.example.digitracksdk.domain.model.help_and_support.AssociateIssueResponseModel
import com.example.digitracksdk.domain.model.help_and_support.HelpAndSupportListRequestModel
import com.example.digitracksdk.domain.model.help_and_support.HelpAndSupportListResponseModel
import com.example.digitracksdk.domain.model.help_and_support.IssueSubCategoryRequestModel
import com.example.digitracksdk.domain.model.help_and_support.IssueSubCategoryResponseModel
import com.example.digitracksdk.domain.model.exit_questionnaire_model.ExitQuestionnaireRequestModel
import com.example.digitracksdk.domain.model.exit_questionnaire_model.ExitQuestionnaireResponseModel
import com.example.digitracksdk.domain.model.help_and_support.IssueDetailsRequestModel
import com.example.digitracksdk.domain.model.help_and_support.IssueDetailsResponseModel
import com.example.digitracksdk.domain.model.home_model.InductionTrainingResponseModel
import com.example.digitracksdk.domain.model.home_model.SurveyLinkRequestModel
import com.example.digitracksdk.domain.model.home_model.SurveyLinkResponseModel
import com.example.digitracksdk.domain.model.login_model.CheckVersionResponseModel
import com.example.digitracksdk.domain.model.mileage_tracking_model.InsertMileageTrackingRequestModel
import com.example.digitracksdk.domain.model.mileage_tracking_model.InsertMileageTrackingResponseModel
import com.example.digitracksdk.domain.model.mileage_tracking_model.MileageRegularizationListRequestModel
import com.example.digitracksdk.domain.model.mileage_tracking_model.MileageRegularizationListResponseModel
import com.example.digitracksdk.domain.model.my_letters.CandidateLoiListResponseModel
import com.example.digitracksdk.domain.model.my_letters.ViewCandidateLoiRequestModel
import com.example.digitracksdk.domain.model.my_letters.ViewCandidateLoiResponseModel
import com.example.digitracksdk.domain.model.new_reimbursement.DeleteNewReimbursementItemRequestModel
import com.example.digitracksdk.domain.model.new_reimbursement.DeleteNewReimbursementItemResponseModel
import com.example.digitracksdk.domain.model.new_reimbursement.GenerateVoucherFromNewReimbursementRequestModel
import com.example.digitracksdk.domain.model.new_reimbursement.GenerateVoucherFromNewReimbursementResponseModel
import com.example.digitracksdk.domain.model.new_reimbursement.GetMonthYearDetailsRequestModel
import com.example.digitracksdk.domain.model.new_reimbursement.GetYearDetailsResponseModel
import com.example.digitracksdk.domain.model.onboarding.InnovIDRequestModel
import com.example.digitracksdk.domain.model.onboarding.PaperlessViewEpfResponseModel
import com.example.digitracksdk.domain.model.onboarding.PaperlessViewFamilyDetailsResponseModel
import com.example.digitracksdk.domain.model.onboarding.PaperlessViewWorkExpResponseModel
import com.example.digitracksdk.domain.model.onboarding.aadhaar_verification.GetAadhaarVerificationDetailsResponseModel
import com.example.digitracksdk.domain.model.onboarding.aadhaar_verification.ValidateAadhaarRequestModel
import com.example.digitracksdk.domain.model.onboarding.aadhaar_verification.ValidateAadhaarResponseModel
import com.example.digitracksdk.domain.model.home_model.innov_id_card.QrCodeResponseModel
import com.example.digitracksdk.domain.model.leaves.BalanceLeaveStatusRequestModel
import com.example.digitracksdk.domain.model.leaves.BalanceLeaveStatusResponseModel
import com.example.digitracksdk.domain.model.leaves.LeaveBalanceRequestModel
import com.example.digitracksdk.domain.model.leaves.LeaveBalanceResponseModel
import com.example.digitracksdk.domain.model.leaves.LeaveStatusSummaryResponseModel
import com.example.digitracksdk.domain.model.login_model.FirebaseTokenUpdateRequestModel
import com.example.digitracksdk.domain.model.login_model.FirebaseTokenUpdateResponseModel
import com.example.digitracksdk.domain.model.login_model.LoginRequestModel
import com.example.digitracksdk.domain.model.login_model.LoginResponseModel
import com.example.digitracksdk.domain.model.mileage_tracking_model.MileageTrackingFlagRequestModel
import com.example.digitracksdk.domain.model.mileage_tracking_model.MileageTrackingFlagResponseModel
import com.example.digitracksdk.domain.model.mileage_tracking_model.MileageTrackingListRequestModel
import com.example.digitracksdk.domain.model.mileage_tracking_model.MileageTrackingListResponseModel
import com.example.digitracksdk.domain.model.my_letters.DownloadCandidateOfferLetterRequestModel
import com.example.digitracksdk.domain.model.my_letters.DownloadCandidateOfferLetterResponseModel
import com.example.digitracksdk.domain.model.my_letters.GetFinancialYearsListRequestModel
import com.example.digitracksdk.domain.model.my_letters.GetFinancialYearsListResponseModel
import com.example.digitracksdk.domain.model.my_letters.GetIncrementLetterResponseModel
import com.example.digitracksdk.domain.model.my_letters.OfferLetterAcceptRejectRequestModel
import com.example.digitracksdk.domain.model.my_letters.OfferLetterAcceptRejectResponseModel
import com.example.digitracksdk.domain.model.new_reimbursement.GetNewReimbursementListResponseModel
import com.example.digitracksdk.domain.model.new_reimbursement.InsertNewReimbursementRequestModel
import com.example.digitracksdk.domain.model.new_reimbursement.InsertNewReimbursementResponseModel
import com.example.digitracksdk.domain.model.onboarding.OnboardingDashboardResponseModel
import com.example.digitracksdk.domain.model.onboarding.PaperlessViewCandidateDetailsResponseModel
import com.example.digitracksdk.domain.model.onboarding.PaperlessViewEducationDetailsResponseModel
import com.example.digitracksdk.domain.model.onboarding.PaperlessViewEsicResponseModel
import com.example.digitracksdk.domain.model.onboarding.PaperlessViewGetCandidateReferenceDetailsResponseModel
import com.example.digitracksdk.domain.model.onboarding.PaperlessViewGetSignatureResponseModel
import com.example.digitracksdk.domain.model.onboarding.aadhaar_verification.AadhaarVerificationOtpValidationRequestModel
import com.example.digitracksdk.domain.model.onboarding.aadhaar_verification.AadhaarVerificationOtpValidationResponseModel
import com.example.digitracksdk.domain.model.onboarding.aadhaar_verification.AadhaarVerificationSendOtpRequestModel
import com.example.digitracksdk.domain.model.onboarding.aadhaar_verification.AadhaarVerificationSendOtpResponseModel
import com.example.digitracksdk.domain.model.onboarding.bank_account_verification.BankAccountVerificationDetailsRequestModel
import com.example.digitracksdk.domain.model.onboarding.bank_account_verification.BankAccountVerificationDetailsResponseModel
import com.example.digitracksdk.domain.model.onboarding.bank_account_verification.BankAccountVerificationRequestModel
import com.example.digitracksdk.domain.model.onboarding.bank_account_verification.BankAccountVerificationResponseModel
import com.example.digitracksdk.domain.model.onboarding.bank_model.UpdateBankDetailsRequestModel
import com.example.digitracksdk.domain.model.onboarding.bank_model.UpdateBankDetailsResponseModel
import com.example.digitracksdk.domain.model.onboarding.insert.InsertCandidateReferenceDetailsModel
import com.example.digitracksdk.domain.model.onboarding.insert.InsertCandidateReferenceDetailsResponseModel
import com.example.digitracksdk.domain.model.onboarding.cibil_score.GetCibilScoreRequestModel
import com.example.digitracksdk.domain.model.onboarding.cibil_score.GetCibilScoreResponseModel
import com.example.digitracksdk.domain.model.onboarding.educational_details.EducationalStreamRequestModel
import com.example.digitracksdk.domain.model.onboarding.educational_details.EducationalStreamResponseModel
import com.example.digitracksdk.domain.model.onboarding.insert.POBInsertEpfModel
import com.example.digitracksdk.domain.model.onboarding.insert.POBInsertEpfResponseModel
import com.example.digitracksdk.domain.model.onboarding.insert.PaperlessFamilyDetailsModel
import com.example.digitracksdk.domain.model.onboarding.insert.PaperlessFamilyDetailsResponseModel
import com.example.digitracksdk.domain.model.onboarding.insert.PaperlessUpdateCandidateBasicDetailsRequestModel
import com.example.digitracksdk.domain.model.onboarding.insert.PaperlessUpdateCandidateBasicDetailsResponseModel
import com.example.digitracksdk.domain.model.onboarding.pan_verification.GetPanCardVerificationDetailsRequestModel
import com.example.digitracksdk.domain.model.onboarding.pan_verification.GetPanCardVerificationDetailsResponseModel
import com.example.digitracksdk.domain.model.onboarding.pan_verification.PanCardVerificationRequestModel
import com.example.digitracksdk.domain.model.onboarding.pan_verification.PanCardVerificationResponseModel
import com.example.digitracksdk.domain.model.pay_slip.SalaryReleaseStatusRequestModel
import com.example.digitracksdk.domain.model.pay_slip.SalaryReleaseStatusResponseModel
import com.example.digitracksdk.domain.model.profile_model.CityListRequestModel
import com.example.digitracksdk.domain.model.profile_model.CityListResponseModel
import com.example.digitracksdk.domain.model.refer_a_friend.SkillListResponseModel
import com.example.digitracksdk.domain.model.reimbursement_model.CheckReimbursementLimitRequestModel
import com.example.digitracksdk.domain.model.reimbursement_model.CheckReimbursementLimitResponseModel
import com.example.digitracksdk.domain.model.reimbursement_model.CheckReimbursementLimitV1RequestModel
import com.example.digitracksdk.domain.model.reimbursement_model.CheckReimbursementLimitV1ResponseModel
import com.example.digitracksdk.domain.model.reimbursement_model.InsertReimbursementRequestModel
import com.example.digitracksdk.domain.model.reimbursement_model.InsertReimbursementResponseModel
import com.example.digitracksdk.domain.model.reimbursement_model.ModeOfTravelRequestModel
import com.example.digitracksdk.domain.model.reimbursement_model.ModeOfTravelResponseModel
import com.example.digitracksdk.domain.model.reimbursement_model.ReimbursementEndKmRequestModel
import com.example.digitracksdk.domain.model.reimbursement_model.ReimbursementEndKmResponseModel
import com.example.digitracksdk.domain.model.reimbursement_model.UploadReimbursementBillRequestModel
import com.example.digitracksdk.domain.model.reimbursement_model.UploadReimbursementBillResponseModel
import com.example.digitracksdk.domain.model.resignation.RevokeResignationRequestModel
import com.example.digitracksdk.domain.model.resignation.RevokeResignationResponseModel
import com.example.digitracksdk.domain.model.onboarding.pf_uan.GetPFESICRequestModel
import com.example.digitracksdk.domain.model.onboarding.pf_uan.GetPFESICResponseModel
import com.example.digitracksdk.domain.model.onboarding.pf_uan.InsertPFESICRequestModel
import com.example.digitracksdk.domain.model.onboarding.pf_uan.InsertPFESICResponseModel
import com.example.digitracksdk.domain.model.pay_slip.PaySlipDownloadRequestModelNew
import com.example.digitracksdk.domain.model.pay_slip.PaySlipDownloadResponseModelNew
import com.example.digitracksdk.domain.model.profile_model.CheckOtpRequestModel
import com.example.digitracksdk.domain.model.profile_model.CheckOtpResponseModel
import com.example.digitracksdk.domain.model.profile_model.StateListResponseModel
import com.example.digitracksdk.domain.model.profile_model.ValidCandidateRequestModel
import com.example.digitracksdk.domain.model.profile_model.ValidCandidateResponseModel
import com.example.digitracksdk.domain.model.refer_a_friend.ReferralFriendRequestModel
import com.example.digitracksdk.domain.model.refer_a_friend.ReferralFriendResponseModel
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
import com.example.digitracksdk.domain.model.reimbursement_model.UpdateReimbursementDetailsRequestModel
import com.example.digitracksdk.domain.model.reimbursement_model.UpdateReimbursementDetailsResponseModel
import com.example.digitracksdk.domain.model.resignation.ResignationCategoryResponseModel
import com.example.digitracksdk.domain.model.resignation.ResignationListRequestModel
import com.example.digitracksdk.domain.model.resignation.ResignationListResponseModel
import com.example.digitracksdk.domain.model.resignation.ResignationNoticePeriodRequestModel
import com.example.digitracksdk.domain.model.resignation.ResignationNoticePeriodResponseModel
import com.example.digitracksdk.domain.model.resignation.ResignationReasonRequestModel
import com.example.digitracksdk.domain.model.resignation.ResignationReasonResponseModel
import com.example.digitracksdk.domain.model.rewards.RewardRequestModel
import com.example.digitracksdk.domain.model.rewards.RewardResponseModel
import com.innov.digitrac.paperless.aadhaar_new.model.DigiLockerRequestModel
import com.innov.digitrac.paperless.aadhaar_new.model.DigiLockerResponseModel
import com.example.digitracksdk.domain.model.onboarding.aadhaar_verification_digilocker.GetAadhaarDetailDigiLockerRequestModel
import com.example.digitracksdk.domain.model.onboarding.aadhaar_verification_digilocker.GetAadhaarDetailDigiLockerResponseModel
import com.example.digitracksdk.domain.model.onboarding.aadhaar_verification_digilocker.SaveDigiLockerRequestIDRequestModel
import com.example.digitracksdk.domain.model.onboarding.aadhaar_verification_digilocker.SaveDigiLockerRequestIDResponseModel
import com.example.digitracksdk.domain.model.onboarding.bank_model.ChequeValidationResponseModel
import com.example.digitracksdk.domain.model.onboarding.bank_model.GetBankListResponseModel
import com.example.digitracksdk.domain.model.onboarding.bank_model.PaperlessViewBankDetailsResponseModel
import com.example.digitracksdk.domain.model.onboarding.insert.GetReferenceCategoryModel
import com.example.digitracksdk.domain.model.onboarding.insert.POBInsertWorkExpModel
import com.example.digitracksdk.domain.model.onboarding.insert.POBInsertWorkExpResponseModel
import com.example.digitracksdk.domain.model.pay_slip.PaySlipDownloadResponseModel
import com.example.digitracksdk.domain.model.profile_model.InsertBasicDetailsRequestModel
import com.example.digitracksdk.domain.model.profile_model.InsertBasicDetailsResponseModel
import com.example.digitracksdk.domain.model.profile_model.NewInnovIdCreationRequestModel
import com.example.digitracksdk.domain.model.profile_model.NewInnovIdCreationResponseModel
import com.example.digitracksdk.domain.model.profile_model.UpdateProfileRequestModel
import com.example.digitracksdk.domain.model.profile_model.UpdateProfileResponseModel
import com.example.digitracksdk.domain.model.refer_a_friend.BranchDetailsRequestModel
import com.example.digitracksdk.domain.model.refer_a_friend.BranchDetailsResponseModel
import com.example.digitracksdk.domain.model.reimbursement_model.ReimbursementBillResponseModel
import com.example.digitracksdk.domain.model.reimbursement_model.ReimbursementDetailsRequestModel
import com.example.digitracksdk.domain.model.reimbursement_model.ReimbursementDetailsResponseModel
import com.example.digitracksdk.domain.model.reimbursement_model.UpdatePendingReimbursementRequestModel
import com.example.digitracksdk.domain.model.reimbursement_model.UpdatePendingReimbursementResponseModel
import com.example.digitracksdk.domain.model.reimbursement_model.UpdateReimbursementStatusRequestModel
import com.example.digitracksdk.domain.model.reimbursement_model.UpdateReimbursementStatusResponseModel
import com.example.digitracksdk.domain.model.resignation.ResignationRequestModel
import com.example.digitracksdk.domain.model.resignation.ResignationResponseModel
import com.innov.digitrac.webservice_api.request_resonse.AttendanceMarkRequestModel
import com.innov.digitrac.webservice_api.request_resonse.AttendanceMarkResponseModel
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {
    @POST(ApiNames.LoginApi)
    suspend fun callLoginApi(@Body request: LoginRequestModel): LoginResponseModel

    @POST(ApiNames.VerifyLoginOtpApi)
    suspend fun callVerifyLoginOtp(@Body request: LoginOtpVerifyRequestModel): LoginOtpVerifyResponseModel

    @POST(ApiNames.HomeDashboardApi)
    suspend fun callHomeDashboardApi(@Body request: HomeDashboardRequestModel): HomeDashboardResponseModel

    @POST(ApiNames.HomeDashboardBannerApi)
    suspend fun callHomeDashboardBannerApi(@Body request: HomeBannerRequestModel): HomeBannerResponseModel

    @POST(ApiNames.DTCandidateInfo)
    suspend fun callCandidateInfo(@Body request: InnovIDCardRequestModel): InnovIDCardResponseModel

    @POST(ApiNames.GetOpenDemandForCandidates)
    suspend fun callJobListingApi(@Body request: JobReferralRequestModel): JobReferralResponseModel

    @POST(ApiNames.GetReferredList)
    suspend fun callReferredUserList(@Body request: ReferredUsersRequestModel): ReferredUsersResponseModel

    @POST(ApiNames.EsicCardApi)
    suspend fun callEsicCardApi(@Body request: PfEsicRequestModel): PfEsicResponseModel

    @POST(ApiNames.EsicMedicalCardApi)
    suspend fun callEsicMedicalCardApi(@Body request: PfEsicRequestModel): PfEsicResponseModel

    @POST(ApiNames.MedicalCardApi)
    suspend fun callMedicalCardApi(@Body request: PfEsicRequestModel): PfEsicResponseModel

    @POST(ApiNames.HelpAndSupportListApi)
    suspend fun callHelpAndSupportList(@Body request: HelpAndSupportListRequestModel): HelpAndSupportListResponseModel

    @POST(ApiNames.GetIssueCategoryApi)
    suspend fun callIssueCategoryApi(@Body request: IssueCategoryRequestModel): ArrayList<IssueCategoryResponseModel>

    @POST(ApiNames.GetIssueSubCategoryApi)
    suspend fun callIssueSubCategoryApi(@Body request: IssueSubCategoryRequestModel): ArrayList<IssueSubCategoryResponseModel>

    @POST(ApiNames.AddAssociateIssueApi)
    suspend fun callAssociateIssueApi(@Body request: AssociateIssueRequestModel): AssociateIssueResponseModel

    @POST(ApiNames.GetResignationCategoryApi)
    suspend fun callResignationCategoryApi(): ResignationCategoryResponseModel

    //TODO use multipart here
    @POST(ApiNames.GetResignationRequestApi)
    suspend fun callResignationRequestApi(@Body request: ResignationRequestModel): ResignationResponseModel

    @POST(ApiNames.GetBranchDetailsListApi)
    suspend fun callBranchDetailsListApi(@Body request: BranchDetailsRequestModel): BranchDetailsResponseModel

    @POST(ApiNames.GetSkillListApi)
    suspend fun callSkillListApi(): SkillListResponseModel

    @POST(ApiNames.JobReferralApi)
    suspend fun callJobReferralApi(@Body request: ReferralFriendRequestModel): ReferralFriendResponseModel

    @POST(ApiNames.GetTrainingApi)
    suspend fun callTrainingApi(@Body request: TrainingRequestModel): TrainingResponseModel


    @POST(ApiNames.GetPolicyAcknowledgeStatus)
    suspend fun callPolicyAcknowledgeStatusApi(@Body request: PolicyAcknowledgeRequestModel) : PolicyAcknowledgeResponseModel
    @POST(ApiNames.GetClientPolicy)
    suspend fun callClientPolicyApi(@Body request: ClientPoliciesRequestModel): ClientPoliciesResponseModel
    @POST(ApiNames.GetViewPolicyApi)
    suspend fun callViewPolicyApi(@Body request: ViewPolicyRequestModel): ViewPolicyResponseModel
    @POST(ApiNames.InsertAcknowledgeApi)
    suspend fun callInsertAcknowledgeApi(@Body request: AcknowledgeRequestModel): AcknowledgeResponseModel

    @POST(ApiNames.DownloadPolicyApi)
    suspend fun callViewAckPolicy(@Body requestModel: ViewAckPolicyRequestModel) : ViewAckPolicyResponseModel

    @POST(ApiNames.GetHolidaysList)
    suspend fun callHolidaysListApi(@Body request: CommonRequestModel): HolidaysListResponseModel

    @POST(ApiNames.GetLeaveTypesApi)
    suspend fun callLeavesTypeApi(@Body request: CommonRequestModel): LeavesTypeResponseModel

    @POST(ApiNames.GetOtherLettersApi)
    suspend fun callOtherLettersApi(@Body request: OtherLettersRequestModel): OtherLettersResponseModel

    @POST(ApiNames.GetLeaveBalancedApi)
    suspend fun callLeaveBalanceApi(@Body request: LeaveBalanceRequestModel): LeaveBalanceResponseModel

    @POST(ApiNames.GetLeaveBalanceStatusApi)
    suspend fun callLeaveBalanceStatusApi(@Body request: BalanceLeaveStatusRequestModel): BalanceLeaveStatusResponseModel

    @POST(ApiNames.GetLeaveStatusSummaryApi)
    suspend fun callLeaveStatusSummaryApi(@Body request: BalanceLeaveStatusRequestModel): LeaveStatusSummaryResponseModel

    @POST(ApiNames.ViewLeaveRequestApi)
    suspend fun callViewLeaveRequestApi(@Body request: LeaveRequestViewRequestModel): LeaveRequestViewResponseModel

    @POST(ApiNames.LeaveApplicationApi)
    suspend fun callApplyLeaveApi(@Body request: ApplyLeaveRequestModel): ApplyLeaveResponseModel


    @POST(ApiNames.IssueDetailsApi)
    suspend fun callIssueDetailsApi(@Body request: IssueDetailsRequestModel): IssueDetailsResponseModel

    @POST(ApiNames.GetReimbursementListApi)
    suspend fun callReimbursementListApi(@Body request: ReimbursementListRequestModel): ReimbursementListResponseModel

    @POST(ApiNames.GetPendingReimbursementListApi)
    suspend fun callPendingReimbursementListApi(@Body request: PendingReimbursementRequestModel): PendingReimbursementResponseModel

    @POST(ApiNames.UpdatePendingReimbursementListApi)
    suspend fun callUpdatePendingReimbursementApi(@Body request: UpdatePendingReimbursementRequestModel): UpdatePendingReimbursementResponseModel

    @POST(ApiNames.RejectedReimbursementValidationApi)
    suspend fun callRejectedReimbursementValidationApi(@Body request: RejectedReimbursementValidationRequestModel): RejectedReimbursementValidationResponseModel

    @POST(ApiNames.CheckReimbursementLimitV1Api)
    suspend fun callCheckReimbursementLimitV1Api(@Body request: CheckReimbursementLimitV1RequestModel): CheckReimbursementLimitV1ResponseModel

    @POST(ApiNames.CheckReimbursementLimitApi)
    suspend fun callCheckReimbursementLimitApi(@Body request: CheckReimbursementLimitRequestModel): CheckReimbursementLimitResponseModel


    @POST(ApiNames.RequestForUpdateReimbursementStatusApi)
    suspend fun callUpdateReimbursementStatusApi(@Body request: UpdateReimbursementStatusRequestModel): UpdateReimbursementStatusResponseModel

    @POST(ApiNames.UpdateReimbursementDetailsListApi)
    suspend fun callUpdateReimbursementDetailsApi(@Body request: UpdateReimbursementDetailsRequestModel): UpdateReimbursementDetailsResponseModel


    @POST(ApiNames.GetReimbursementDetailsApi)
    suspend fun callReimbursementDetailsApi(@Body request: ReimbursementDetailsRequestModel): ReimbursementDetailsResponseModel

    @POST(ApiNames.GetReimbursementBillApi)
    suspend fun callReimbursementBillApi(@Body request: ReimbursementDetailsRequestModel): ReimbursementBillResponseModel

    @POST(ApiNames.InsertReimbursementBillApi)
    suspend fun callUploadReimbursementBillApi(@Body request: UploadReimbursementBillRequestModel): UploadReimbursementBillResponseModel

    @POST(ApiNames.GetReimbursementCategoryApi)
    suspend fun callReimbursementCategoryApi(@Body request: ReimbursementCategoryRequestModel): ReimbursementCategoryResponseModel

    @POST(ApiNames.GetReimbursementSubCategoryApi)
    suspend fun callReimbursementSubCategoryApi(@Body request: ReimbursementSubCategoryRequestModel): ReimbursementSubCategoryResponseModel

    @POST(ApiNames.GetReimbursementModeOfTravelApi)
    suspend fun callReimbursementModeOfTravelApi(@Body request: ModeOfTravelRequestModel): ModeOfTravelResponseModel

    @POST(ApiNames.GetReimbursementEndKmApi)
    suspend fun callReimbursementEndKmApi(@Body request: ReimbursementEndKmRequestModel): ReimbursementEndKmResponseModel

    @POST(ApiNames.GetRejectedVoucherEndKmApi)
    suspend fun callRejectedEndKmApi(@Body request: RejectedEndKmRequestModel): RejectedEndKmResponseModel

    @POST(ApiNames.GetReimbursementValidationApi)
    suspend fun callReimbursementValidationApi(@Body request: ReimbursementValidationRequestModel): ReimbursementValidationResponseModel

    @POST(ApiNames.InsertReimbursementApi)
    suspend fun callInsertReimbursementApi(@Body request: InsertReimbursementRequestModel): InsertReimbursementResponseModel

    @POST(ApiNames.SaveReimbursementPreApprovalApi)
    suspend fun callSaveReimbursementPreApprovalApi(@Body request: SaveReimbursementPreApprovalRequestModel): SaveReimbursementPreApprovalResponseModel


    /**
     *  old way method for payslip
     */
//    @GET(ApiNames.GetPaySlipMonthsYearsApi)
//    suspend fun callPaySlipMonthYearApi(@Query("InnovID") InnovID: String): PaySlipMonthYearsResponseModel
    @POST(ApiNames.GetPaySlipMonthsYearsApi)
    suspend fun callPaySlipMonthYearApi(@Body request: PaySlipMonthYearRequestModel): PaySlipMonthYearsResponseModel

    @POST(ApiNames.GetSalaryStatusApi)
    suspend fun callSalaryReleaseStatusApi(@Body request: SalaryReleaseStatusRequestModel): SalaryReleaseStatusResponseModel

    @GET(ApiNames.DownloadPaySlipApi)
    suspend fun callDownloadPaySlipApi(
        @Query("InnovID") InnovID: String,
        @Query("Month") Month: String,
        @Query("Year") Year: String
    ): PaySlipDownloadResponseModel

    @POST(ApiNames.GetCandidateLoiListApi)
    suspend fun callCandidateLoiListApi(@Body request: CommonRequestModel): CandidateLoiListResponseModel

    @POST(ApiNames.ViewCandidateLoiApi)
    suspend fun callViewCandidateLoiApi(@Body request: ViewCandidateLoiRequestModel): ViewCandidateLoiResponseModel

    @POST(ApiNames.GetCandidateOfferLetterListApi)
    suspend fun callCandidateOfferLettersListApi(@Body request: CommonRequestModel): CandidateOfferLetterListResponseModel

    @POST(ApiNames.DownloadCandidateOfferLetterApi)
    suspend fun callDownloadCandidateOfferLetterApi(@Body request: DownloadCandidateOfferLetterRequestModel): DownloadCandidateOfferLetterResponseModel

    @POST(ApiNames.GetStateListApi)
    suspend fun callStateListApi(@Body request: CommonRequestModel): StateListResponseModel

    @POST(ApiNames.GetCityListApi)
    suspend fun callCityListApi(@Body request: CityListRequestModel): CityListResponseModel

    @POST(ApiNames.UpdateProfileApi)
    suspend fun callUpdateProfileApi(@Body request: UpdateProfileRequestModel): UpdateProfileResponseModel

    @POST(ApiNames.GetAttendanceRegularizationApi)
    suspend fun callAttendanceRegularizationListApi(@Body request: AttendanceRegularizationListRequestModel): AttendanceRegularizationListResponseModel

    @POST(ApiNames.GetViewAttendanceApi)
    suspend fun callViewAttendanceApi(@Body request: ViewAttendanceRequestModel): ViewAttendanceResponseModel

    @POST(ApiNames.GetAttendanceRegularizationTypeApi)
    suspend fun callAttendanceRegularizationTypeApi(@Body request: CommonRequestModel): AttendanceRegularizationTypeResponseModel

    @GET(ApiNames.AttendanceRegularizationInsertApi)
    suspend fun callAttendanceRegularizationInsertApi(
        @Query("Empcode") Empcode: String,
        @Query("RequestTypeId") RequestTypeId: Int,
        @Query("RegularizationDate") RegularizationDate: String,
        @Query("InTime") InTime: String,
        @Query("OutTime") OutTime: String,
        @Query("Location") Location: String,
        @Query("Remarks") Remarks: String,
        @Query("ToDate") ToDate: String
    ): InsertAttendanceRegularizationResponseModel


    @POST(ApiNames.InsertAttendanceRegularizationApi)
    suspend fun callInsertAttendanceRegularizationApi(@Body request: InsertAttendanceRegularizationRequestModel): InsertAttendanceRegularizationResponseModel

    @POST(ApiNames.InsertMileageRegularizationApi)
    suspend fun callInsertMileageRegularizationApi(@Body request: InsertMileageRegularizationRequestModel): InsertMileageRegularizationResponseModel

    @POST(ApiNames.GetMileageRegularizationListApi)
    suspend fun callMileageRegularizationListApi(@Body request: MileageRegularizationListRequestModel): MileageRegularizationListResponseModel

    @POST(ApiNames.InsertMileageApi)
    suspend fun callInsertMileageTrackingApi(@Body request: InsertMileageTrackingRequestModel): InsertMileageTrackingResponseModel

    @POST(ApiNames.GetMileageDetailsApi)
    suspend fun callMileageTrackingListApi(@Body request: MileageTrackingListRequestModel): MileageTrackingListResponseModel

    @POST(ApiNames.GetMileageTrackingFlagApi)
    suspend fun callMileageTrackingFlagApi(@Body request: MileageTrackingFlagRequestModel): MileageTrackingFlagResponseModel

    @POST(ApiNames.GetValidCandidateApi)
    suspend fun callValidCandidateApi(@Body request: ValidCandidateRequestModel): ValidCandidateResponseModel

    @POST(ApiNames.CheckOTPApi)
    suspend fun callCheckOtpApi(@Body request: CheckOtpRequestModel): CheckOtpResponseModel

    @POST(ApiNames.InsertBasicDetailsApi)
    suspend fun callInsertBasicDetailsApi(@Body request: InsertBasicDetailsRequestModel): InsertBasicDetailsResponseModel

    @POST(ApiNames.InnovIDCreationNewApi)
    suspend fun callInnovIDCreationNewApi(@Body request: NewInnovIdCreationRequestModel): NewInnovIdCreationResponseModel


    // Get OnBoarding

    @POST(ApiNames.GetEpfDetailsApi)
    suspend fun callGetEpfDetailsApi(@Body request: InnovIDRequestModel): PaperlessViewEpfResponseModel

    @POST(ApiNames.GetEsicDetailsApi)
    suspend fun callGetEsicDetailsApi(@Body request: InnovIDRequestModel): PaperlessViewEsicResponseModel

    @POST(ApiNames.GetFamilyDetailsApi)
    suspend fun callGetFamilyDetailsApi(@Body request: InnovIDRequestModel): PaperlessViewFamilyDetailsResponseModel

    @POST(ApiNames.GetEducationDetailsApi)
    suspend fun callGetEducationDetailsApi(@Body request: InnovIDRequestModel): PaperlessViewEducationDetailsResponseModel

    @POST(ApiNames.GetWorkExperianceDetailsApi)
    suspend fun callGetWorkExpDetailsApi(@Body request: InnovIDRequestModel): PaperlessViewWorkExpResponseModel

    @POST(ApiNames.GetCandidateBasicDetailsApi)
    suspend fun callGetCandidateDetailsApi(@Body request: InnovIDRequestModel): PaperlessViewCandidateDetailsResponseModel

    @POST(ApiNames.GetBankDetailsApi)
    suspend fun callGetBankDetailsApi(@Body request: InnovIDRequestModel): PaperlessViewBankDetailsResponseModel

    @POST(ApiNames.UpdateBankDetailsApi)
    suspend fun callUpdateBankDetailsApi(@Body request: UpdateBankDetailsRequestModel): UpdateBankDetailsResponseModel

    @POST(ApiNames.GetCandidateReferenceDetailsApi)
    suspend fun callGetCandidateReferenceDetailsApi(@Body request: InnovIDRequestModel): PaperlessViewGetCandidateReferenceDetailsResponseModel

    @POST(ApiNames.GetSignatureApi)
    suspend fun callGetSignatureApi(@Body request: InnovIDRequestModel): PaperlessViewGetSignatureResponseModel
//soap to rest

    @POST(ApiNames.GetKycDocumentsListApi)
    suspend fun callDTGetKYCDocsApi(@Body request: EmpIDRequestModel): KycDocumentListResponseModel

    @POST(ApiNames.DTGetPersonalDocsApi)
    suspend fun callViewDocumentsListApi(@Body request: EmpIDRequestModel): ViewDocumentsResponseModel

    @POST(ApiNames.GetEducationCategoryApi)
    suspend fun callGetEducationCategoryApi(): GetEducationCategoryResponseModel

    // Insert OnBoarding
    @POST(ApiNames.UpdateCandidateBasicDetailsApi)
    suspend fun callUpdateCandidateBasicDetailsApi(@Body request: PaperlessUpdateCandidateBasicDetailsRequestModel): PaperlessUpdateCandidateBasicDetailsResponseModel

    @POST(ApiNames.FamilyDetailsApi)
    suspend fun callFamilyDetailsApi(@Body request: PaperlessFamilyDetailsModel): PaperlessFamilyDetailsResponseModel

    @POST(ApiNames.POBInsertEducationInfoApi)
    suspend fun callPOBInsertEducationInfoApi(@Body request: InsertEducationInfoRequestModel): InsertEducationInfoResponseModel

    @POST(ApiNames.POBInsertWorkExpApi)
    suspend fun callPOBInsertWorkExpApi(@Body request: POBInsertWorkExpModel): POBInsertWorkExpResponseModel

    @POST(ApiNames.POBInsertEpfApi)
    suspend fun callPOBInsertEpfApi(@Body request: POBInsertEpfModel): POBInsertEpfResponseModel

    @POST(ApiNames.POBInsertESICDetailsApi)
    suspend fun callPOBInsertESICDetailsApi(@Body request: POBInsertESICDetailsModel): POBInsertEsicResponseModel

    @POST(ApiNames.CandidateProfileAcknowledgementApi)
    suspend fun callCandidateProfileAckApi(@Body request: CandidateProfileAckModel): CandidateProfileAckResponseModel

    @POST(ApiNames.GetReferenceCategoryApi)
    suspend fun callGetReferenceCategoryApi(): GetReferenceCategoryModel

    @POST(ApiNames.InsertCandidateReferenceDetailsApi)
    suspend fun callInsertCandidateReferenceDetailsApi(@Body request: InsertCandidateReferenceDetailsModel): InsertCandidateReferenceDetailsResponseModel

    //soap to rest

    @POST(ApiNames.GetKycDocumentListApi)
    suspend fun callKycPendingDocListApi(@Body request: KycDocumentPendingListRequestModel): KycDocumentPendingListResponseModel

    @POST(ApiNames.GetAllDocumentWithoutKycApi)
    suspend fun callGetPendingDocumentsListApi(@Body request: PendingDocumentsListRequestModel): PendingDocumentsListResponseModel

    @POST(ApiNames.InsertDocumentsFileSystemDTApi)
    suspend fun callInsertPendingDocumentsFileApi(@Body request: InsertPendingDocumentsRequestModel): InsertPendingDocumentsResponseModel

    @POST(ApiNames.InsertSignatureApi)
    suspend fun callInsertSignatureApi(@Body request: InsertSignatureRequestModel): InsertSignatureResponseModel

    @POST(ApiNames.InsertWorkExpApi)
    suspend fun callInsertWorkExpApi(@Body request: InsertWorkExpRequestModel): InsertWorkExpResponseModel

    @POST(ApiNames.GetBankListApi)
    suspend fun callBankListApi(): GetBankListResponseModel

    @POST(ApiNames.GetChequeValidationApi)
    suspend fun callChequeValidationApi(@Body request: CommonRequestModel): ChequeValidationResponseModel

    //attendance
    @POST(ApiNames.GetGeoFancingApi)
    suspend fun callAttendanceGeoFancingApi(@Body request: CommonRequestModel): AttendanceGeoFancingResponseModel

    @POST(ApiNames.GetDashboardAttendanceStatusApi)
    suspend fun callDashboardAttendanceStatusApi(@Body request: CommonRequestModel): DashboardAttendanceStatusResponseModel

    @POST(ApiNames.GetAttendanceStatusApi)
    suspend fun callAttendanceStatusApi(@Body request: GnetIdRequestModel): AttendanceStatusResponseModel
    @POST(ApiNames.GetAttendanceFlag)
    suspend fun callAttendanceFlagApi(@Body request: AttendanceFlagRequestModel): AttendanceFlagResponseModel
    @POST(ApiNames.GetAttendanceMarking)
    suspend fun callAttendanceMarkApi(@Body request: AttendanceMarkRequestModel): AttendanceMarkResponseModel

    @POST(ApiNames.UpdateAttendanceStatusApi)
    suspend fun callUpdateAttendanceStatusApi(@Body request: UpdateAttendanceStatusRequestModel): UpdateAttendanceStatusResponseModel

    @POST(ApiNames.GetAttendanceTimeSheetApi)
    suspend fun callAttendanceTimeSheetApi(@Body request: AttendanceTimeSheetRequestModel): AttendanceTimeSheetResponseModel

    @POST(ApiNames.AttendanceValidationApi)
    suspend fun callAttendanceValidationApi(@Body request: AttendanceValidationRequestModel): AttendanceValidationResponseModel

    @POST(ApiNames.CurrentDayAttendanceStatusApi)
    suspend fun callCurrentDayAttendanceStatusApi(@Body request: CurrentDayAttendanceStatusRequestModel): CurrentDayAttendanceStatusResponseModel

    @POST(ApiNames.GetAttendanceZoneApi)
    suspend fun callAttendanceZoneApi(@Body request: CommonRequestModel): AttendanceZoneResponseModel

    @POST(ApiNames.CreateAttendanceTokenApi)
    suspend fun callCreateAttendanceTokenApi(@Body request: CreateAttendanceTokenRequestModel): CreateAttendanceTokenResponseModel

    @POST(ApiNames.CreateLogYourVisitTokenApi)
    suspend fun callCreateLogYourVisitTokenApi(@Body request: CreateLogYourVisitTokenRequestModel): CreateLogYourVisitTokenResponseModel

    @POST(ApiNames.CheckValidLogYourVisitTokenApi)
    suspend fun callValidateLogYourVisitTokenApi(@Body request: CheckValidLogYourVisitRequestModel): CheckValidLogYourVisitResponseModel


    @POST(ApiNames.CheckValidAttendanceTokenApi)
    suspend fun callCheckValidAttendanceTokenApi(@Body request: CheckValidAttendanceTokenRequestModel): CheckValidAttendanceTokenResponseModel

    @POST(ApiNames.InsertAttendancePicApi)
    suspend fun callInsertAttendancePicApi(@Body request: InsertAttendancePicRequestModel): InsertAttendancePicResponseModel

    @POST(ApiNames.GetLogYourVisitApi)
    suspend fun callGetLogYourVisitApi(@Body request: LogYourVisitRequestModel): LogYourVisitResponseModel

    @POST(ApiNames.GetNotificationListApi)
    suspend fun callNotificationListApi(@Body request: CommonRequestModel): NotificationListResponseModel

    @POST(ApiNames.GetNotificationDetailsApi)
    suspend fun callNotificationDetailsApi(@Body request: NotificationDetailsRequestModel): NotificationDetailsResponseModel

    @POST(ApiNames.OfferLetterAcceptRejectApi)
    suspend fun callOfferLetterAcceptRejectApi(@Body request: OfferLetterAcceptRejectRequestModel): OfferLetterAcceptRejectResponseModel

    @POST(ApiNames.FirebaseTokenUpdateApi)
    suspend fun callFirebaseTokenUpdateApi(@Body request: FirebaseTokenUpdateRequestModel): FirebaseTokenUpdateResponseModel

    @POST(ApiNames.CheckAppVersionApi)
    suspend fun callCheckAppVersionApi(): CheckVersionResponseModel

    @POST(ApiNames.CheckBirthdayApi)
    suspend fun callCheckBirthdayApi(@Body request: CommonRequestModel): CheckBirthdayResponseModel

    @POST(ApiNames.InductionTrainingApi)
    suspend fun  callInductionTrainingApi(@Body request: CommonRequestModel) : InductionTrainingResponseModel
    @POST(ApiNames.GetViewTrainingDocumentApi)
    suspend fun callViewTrainingDocumentApi(@Body request: ViewTrainingDocumentRequestModel): ViewTrainingDocumentResponseModel

    @POST(ApiNames.GetAadhaarVerificationSendOtpApi)
    suspend fun callAadhaarVerificationSendOtpApi(@Body request: AadhaarVerificationSendOtpRequestModel): AadhaarVerificationSendOtpResponseModel

    @POST(ApiNames.GetAadhaarVerificationOtpValidationApi)
    suspend fun callAadhaarVerificationOtpValidationApi(@Body request: AadhaarVerificationOtpValidationRequestModel): AadhaarVerificationOtpValidationResponseModel
    @POST(ApiNames.GetValidateAadhaarApi)
    suspend fun callValidateAadhaarApi(@Body request: ValidateAadhaarRequestModel): ValidateAadhaarResponseModel
    @POST(ApiNames.GetGetAadharVerificationDetailsApi)
    suspend fun callGetAadhaarVerificationDetailsApi(@Body request: ValidateAadhaarRequestModel): GetAadhaarVerificationDetailsResponseModel
    @POST(ApiNames.GetPanVerificationApi)
    suspend fun callPanVerificationApi(@Body request: PanCardVerificationRequestModel): PanCardVerificationResponseModel
    @POST(ApiNames.GetPanVerificationDetailsApi)
    suspend fun callPanVerificationDetailsApi(@Body request: GetPanCardVerificationDetailsRequestModel) : GetPanCardVerificationDetailsResponseModel

    @POST(ApiNames.GetBankAccountVerificationApi)
    suspend fun callBankAccountVerificationApi(@Body request: BankAccountVerificationRequestModel): BankAccountVerificationResponseModel
    @POST(ApiNames.GetBankAccountVerificationDetailsApi)
    suspend fun callGetBankAccountVerificationDetailsApi(@Body request : BankAccountVerificationDetailsRequestModel) : BankAccountVerificationDetailsResponseModel

    @POST(ApiNames.GetCibilScore)
    suspend fun callGetCibilScoreApi(@Body request: GetCibilScoreRequestModel): GetCibilScoreResponseModel

    @POST(ApiNames.GetOnboardingDashboardApi)
    suspend fun callOnboardingDashboardApi(@Body request: CommonRequestModel): OnboardingDashboardResponseModel

    @POST(ApiNames.GetHireCraftPayslipDownloadApi)
    suspend fun callPaySlipDownloadApiNew(@Body request: PaySlipDownloadRequestModelNew): PaySlipDownloadResponseModelNew

    @POST(ApiNames.GetResignationListApi)
    suspend fun callResignationListApi(@Body request: ResignationListRequestModel): ResignationListResponseModel

    @POST(ApiNames.ViewPayout)
    suspend fun callViewPayoutApi(@Body request: ViewPayoutRequest): ViewPayoutResponseModel

    @POST(ApiNames.GetRefineUrl)
    suspend fun callGetRefineApi(@Body request: RefineRequest): RefineResponseModel

    @POST(ApiNames.GetUploadedDocumentsList)
    suspend fun callGetUploadedDocumentsListApi(@Body request: UploadedDocumentsRequestModel): UploadedDocumentsResponseModel

    @POST(ApiNames.GetUploadedDocument)
    suspend fun callGetUploadedDocument(@Body request: GetDocumentRequestModel): GetDocumentResponseModel

    @POST(ApiNames.IncomeTaxDeclarationApi)
    suspend fun callIncomeTaxDeclarationApi(@Body request: IncomeTaxDeclarationRequestModel): IncomeTaxDeclarationResponseModel

    @POST(ApiNames.GetForm16Api)
    suspend fun callGetForm16Api(@Body request: GetForm16RequestModel): GetForm16ResponseModel

    @POST(ApiNames.GetFinancialYearListApi)
    suspend fun callGetFinancialYearListApi(@Body request: GetFinancialYearsListRequestModel): GetFinancialYearsListResponseModel

    @POST(ApiNames.GetGeoTrackingSummaryList)
    suspend fun callGetGeoTrackingSummaryListApi(@Body request: GeoTrackingSummaryListRequestModel): GeoTrackingSummaryListResponseModel

    @POST(ApiNames.GetGeoTrackingDetails)
    suspend fun callGeoTrackingDetailsApi(@Body request: GeoTrackingDetailsRequestModel): GeoTrackingDetailsResponseModel

    @POST(ApiNames.GetReimbursementListForVoucher)
    suspend fun callGetReimbursementListForVoucherApi(@Body request: GnetIdRequestModel): GetNewReimbursementListResponseModel

    @POST(ApiNames.GetNewReimbursementPendingListApi)
    suspend fun callGetNewReimbursementPendingListApi(@Body request: ReimbursementListRequestModel): ReimbursementListResponseModel

    @POST(ApiNames.GetNewReimbursementApprovedListApi)
    suspend fun callGetNewReimbursementApprovedListApi(@Body request: ReimbursementListRequestModel): ReimbursementListResponseModel

    @POST(ApiNames.GetNewReimbursementRejectedListApi)
    suspend fun callGetNewReimbursementRejectedListApi(@Body request: ReimbursementListRequestModel): ReimbursementListResponseModel

    @POST(ApiNames.DeleteNewReimbursementListItem)
    suspend fun callDeleteNewReimbursementListItemApi(@Body request: DeleteNewReimbursementItemRequestModel): DeleteNewReimbursementItemResponseModel

    @POST(ApiNames.GenerateNewReimbursementVoucher)
    suspend fun callGenerateNewReimbursementVoucherApi(@Body request: GenerateVoucherFromNewReimbursementRequestModel): GenerateVoucherFromNewReimbursementResponseModel

    @POST(ApiNames.GetIncrementLetterApi)
    suspend fun callGetIncrementLetterApi(@Body request: GnetIdRequestModel): GetIncrementLetterResponseModel

    @POST(ApiNames.GetCustomerIdCard)
    suspend fun callCustomerIdCardApi(@Body request : CustomerIdCardRequestModel): CustomerIdCardResponseModel

    @POST(ApiNames.InsertNewReimbursementApi)
    suspend fun callInsertNewReimbursementApi(@Body request: InsertNewReimbursementRequestModel): InsertNewReimbursementResponseModel
    @POST(ApiNames.GetMonthDetails)
    suspend fun callGetMonthDetailApi(@Body request: GetMonthYearDetailsRequestModel): GetMonthDetailsResponseModel
    @POST(ApiNames.GetYearDetails)
    suspend fun callGetYearDetailApi(@Body request: GetMonthYearDetailsRequestModel): GetYearDetailsResponseModel

    @POST(ApiNames.GetQrCode)
    suspend fun callQrCodeApi(@Query("GnetAssociateID") GnetAssociateID: String): QrCodeResponseModel

    @POST(ApiNames.InsertPFESIC)
    suspend fun callInsertPFESICApi(@Body request : InsertPFESICRequestModel) : InsertPFESICResponseModel

    @POST(ApiNames.GetPFESICDetails)
    suspend fun callGetPFESICApi(@Body request : GetPFESICRequestModel) : GetPFESICResponseModel

    @POST(ApiNames.GetEducationStream)
    suspend fun callEducationalStream(@Body request : EducationalStreamRequestModel) : EducationalStreamResponseModel

    @POST(ApiNames.GetResignationNoticePeriod)
    suspend fun callGetResignationNoticePeriodApi(@Body request : ResignationNoticePeriodRequestModel) : ResignationNoticePeriodResponseModel

    @POST(ApiNames.RevokeResignation)
    suspend fun callRevokeResignationApi(@Body request  : RevokeResignationRequestModel) : RevokeResignationResponseModel

    @POST(ApiNames.GetResignationReason)
    suspend fun callResignationReasonApi(@Body request : ResignationReasonRequestModel) : ResignationReasonResponseModel

    @POST(ApiNames.AttendanceCycleApi)
    suspend fun callAttendanceCycleApi(@Body request : AttendanceCycleRequestModel) : AttendanceCycleResponseModel

    @POST(ApiNames.LeaveTypeHexCodeApi)
    suspend fun  callLeaveTypeHexCodeApi(@Body request : LeaveHexCodeRequestModel) : LeaveHexCodeResponseModel

    @POST(ApiNames.InsertExitInterviewApi)
    suspend fun callInsetExitInterviewApi(@Body request : ExitQuestionnaireRequestModel) : ExitQuestionnaireResponseModel

    @POST(ApiNames.GetSurveyLink)
    suspend  fun callSurveyLinkApi(@Body request : SurveyLinkRequestModel) : SurveyLinkResponseModel

    @POST(ApiNames.GetRewardsApi)
    suspend fun callRewardsApi(@Body request: RewardRequestModel): RewardResponseModel

    @POST(ApiNames.AadhaarVerificationApi)
    suspend fun callAadhaarVerificationApi(@Body request: DigiLockerRequestModel): DigiLockerResponseModel

    @POST(ApiNames.AadhaarSaveRequestId)
    suspend fun callAadhaarSaveRequestIdApi(@Body request: SaveDigiLockerRequestIDRequestModel): SaveDigiLockerRequestIDResponseModel

    @POST(ApiNames.GetAadhaarData)
    suspend fun callGetAadhaarDataApi(@Body request: GetAadhaarDetailDigiLockerRequestModel): GetAadhaarDetailDigiLockerResponseModel




}