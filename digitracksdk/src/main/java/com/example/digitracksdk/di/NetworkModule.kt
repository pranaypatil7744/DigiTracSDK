package com.example.digitracksdk.di

import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.example.digitracksdk.data.repository.CustomerIdCardUseRepositoryImp
import com.example.digitracksdk.data.repository.HomeBannerRepositoryImp
import com.example.digitracksdk.data.repository.HomeRepositoryImp
import com.example.digitracksdk.data.repository.IncomeTaxRepositoryImp
import com.example.digitracksdk.data.repository.InnovIDCardRepositoryImp
import com.example.digitracksdk.data.repository.JobReferralRepositoryImp
import com.example.digitracksdk.data.repository.LoginRepositoryImp
import com.example.digitracksdk.data.repository.RefineRepositoryImp
import com.example.digitracksdk.data.repository.VerifyLoginOtpRepositoryImp
import com.example.digitracksdk.domain.usecase.reimbursement_usecase.ReimbursementValidationUseCase
import com.google.gson.GsonBuilder
import com.example.digitracksdk.data.repository.*
import com.example.digitracksdk.data.repository.attendance_regularization_repo_imp.AttendanceRegularizationRepositoryImp
import com.example.digitracksdk.data.repository.attendance_repo_imp.AttendanceRepositoryImp
import com.example.digitracksdk.data.repository.attendance_repo_imp.CandidateRepositoryImp
import com.example.digitracksdk.data.repository.client_policies_repo_imp.ClientPoliciesRepositoryImp
import com.example.digitracksdk.data.repository.exit_questionnaire_repo_imp.ExitQuestionnaireRepositoryImp
import com.example.digitracksdk.data.repository.geo_tracking_repo_imp.GeoTrackingRepositoryImp
import com.example.digitracksdk.data.repository.help_and_support_repo_imp.HelpAndSupportRepositoryImp
import com.example.digitracksdk.data.repository.leaves_repo_imp.LeavesRepositoryImp
import com.example.digitracksdk.data.repository.onboarding.AadhaarVerificationDigiLockerRepositoryImp
import com.example.digitracksdk.data.repository.onboarding.PanVerificationRepositoryImp
import com.example.digitracksdk.data.repository.mileage_tracking_repo_imp.MileageTrackingRepositoryImp
import com.example.digitracksdk.data.repository.my_letters_repo_iml.MyLettersRepositoryImp
import com.example.digitracksdk.data.repository.notification_repo_imp.NotificationRepositoryImp
import com.example.digitracksdk.data.repository.onboarding.AadhaarVerificationRepositoryImp
import com.example.digitracksdk.data.repository.onboarding.BankAccountVerificationRepositoryImp
import com.example.digitracksdk.data.repository.onboarding.BankDetailsRepositoryImp
import com.example.digitracksdk.data.repository.onboarding.CibilScoreRepositoryImp
import com.example.digitracksdk.data.repository.onboarding.DocumentsRepositoryImp
import com.example.digitracksdk.data.repository.onboarding.FamilyDetailsRepositoryImp
import com.example.digitracksdk.data.repository.onboarding.KYCRepositoryImp
import com.example.digitracksdk.data.repository.onboarding.OnboardingDashboardRepositoryImp
import com.example.digitracksdk.data.repository.onboarding.PFESICRepositoryImp
import com.example.digitracksdk.data.repository.onboarding.PaperlessViewCandidateDetailsRepositoryImp
import com.example.digitracksdk.data.repository.onboarding.PaperlessViewCandidateReferenceDetailsRepositoryImp
import com.example.digitracksdk.data.repository.onboarding.PaperlessViewEpfRepositoryImp
import com.example.digitracksdk.data.repository.onboarding.PaperlessViewEsicRepositoryImp
import com.example.digitracksdk.data.repository.onboarding.PaperlessViewGetSignatureDetailsRepositoryImp
import com.example.digitracksdk.data.repository.onboarding.PaperlessViewWorkExpDetailsRepositoryImp
import com.example.digitracksdk.data.repository.onboarding.*
import com.example.digitracksdk.data.repository.onboarding.insert.CandidateProfileAckRepositoryImp
import com.example.digitracksdk.data.repository.onboarding.insert.POBInsertEpfRepositoryImp
import com.example.digitracksdk.data.repository.onboarding.insert.POBInsertEsicRepositoryImp
import com.example.digitracksdk.data.repository.onboarding.insert.ReferenceCategoryRepositoryImp
import com.example.digitracksdk.data.repository.onboarding.insert.educational_details.EducationDetailsRepositoryImp
import com.example.digitracksdk.data.repository.pay_slip_repo_imp.PaySlipRepositoryImp
import com.example.digitracksdk.data.repository.pf_esic_insurance_repo_imp.PfEsicInsuranceRepositoryImp
import com.example.digitracksdk.data.repository.profile_repo_iml.ProfileRepositoryImp
import com.example.digitracksdk.data.repository.referral_friend_repo_imp.ReferralFriendRepositoryImp
import com.example.digitracksdk.data.repository.reimbursement_repo_imp.ReimbursementRepositoryImp
import com.example.digitracksdk.data.repository.resignation_repo_imp.ResignationRepositoryImp
import com.example.digitracksdk.data.repository.reward_repo_imp.RewardsRepositoryImp
import com.example.digitracksdk.data.repository.training_repo_imp.TrainingRepositoryImp
import com.example.digitracksdk.data.repository.view_payout.ViewPayoutRepositoryImp
import com.example.digitracksdk.data.source.remote.ApiNames
import com.example.digitracksdk.data.source.remote.ApiService
import com.example.digitracksdk.domain.repository.attendance_regularization_repository.AttendanceRegularizationRepository
import com.example.digitracksdk.domain.repository.attendance_repository.AttendanceRepository
import com.example.digitracksdk.domain.repository.candidate_repository.CandidateRepository
import com.example.digitracksdk.domain.repository.client_policies_repository.ClientPoliciesRepository
import com.example.digitracksdk.domain.repository.customer_id_card_repository.CustomerIdCardRepository
import com.example.digitracksdk.domain.repository.ext_questionnaire_repository.ExitQuestionnaireRepository
import com.example.digitracksdk.domain.repository.geo_tracking_repository.GeoTrackingRepository
import com.example.digitracksdk.domain.repository.help_and_support_repository.HelpAndSupportRepository
import com.example.digitracksdk.domain.repository.home_repository.HomeBannerRepository
import com.example.digitracksdk.domain.repository.home_repository.HomeRepository
import com.example.digitracksdk.domain.repository.home_repository.InnovIDCardRepository
import com.example.digitracksdk.domain.repository.home_repository.JobReferralRepository
import com.example.digitracksdk.domain.repository.income_tax.IncomeTaxRepository
import com.example.digitracksdk.domain.repository.leaves_repository.LeavesRepository
import com.example.digitracksdk.domain.repository.login_repository.LoginRepository
import com.example.digitracksdk.domain.usecase.attendance_usecase.UpdateAttendanceStatusUseCase
import com.example.digitracksdk.domain.repository.mileage_tracking_repository.MileageTrackingRepository
import com.example.digitracksdk.domain.repository.my_letters_repository.MyLettersRepository
import com.example.digitracksdk.domain.repository.notification_repository.NotificationRepository
import com.example.digitracksdk.domain.repository.onboarding.OnboardingDashboardRepository
import com.example.digitracksdk.domain.repository.onboarding.PaperlessViewCandidateDetailsRepository
import com.example.digitracksdk.domain.repository.onboarding.PaperlessViewCandidateReferenceDetailsRepository
import com.example.digitracksdk.domain.repository.onboarding.PaperlessViewEpfRepository
import com.example.digitracksdk.domain.repository.onboarding.PaperlessViewEsicRepository
import com.example.digitracksdk.domain.repository.onboarding.PaperlessViewWorkExpDetailsRepository
import com.example.digitracksdk.domain.repository.onboarding.*
import com.example.digitracksdk.domain.repository.onboarding.aadhaar_verification_digilocker.AadhaarVerificationDigiLockerRepository
import com.example.digitracksdk.domain.repository.onboarding.aadhar_verification.AadhaarVerificationRepository
import com.example.digitracksdk.domain.repository.onboarding.bank.BankDetailsRepository
import com.example.digitracksdk.domain.repository.onboarding.bank_account_verification.BankAccountVerificationRepository
import com.example.digitracksdk.domain.repository.onboarding.cibil_score.CibilScoreRepository
import com.example.digitracksdk.domain.repository.onboarding.documents.DocumentsRepository
import com.example.digitracksdk.domain.repository.onboarding.education_details.EducationDetailsRepository
import com.example.digitracksdk.domain.repository.onboarding.family_details.FamilyDetailsRepository
import com.example.digitracksdk.domain.repository.onboarding.insert.CandidateProfileAckRepository
import com.example.digitracksdk.domain.repository.onboarding.insert.PobInsertEpfRepository
import com.example.digitracksdk.domain.repository.onboarding.insert.PobInsertEsicRepository
import com.example.digitracksdk.domain.repository.onboarding.insert.ReferenceCategoryRepository
import com.example.digitracksdk.domain.repository.onboarding.kyc.KYCRepository
import com.example.digitracksdk.domain.repository.onboarding.pan_verification.PanVerificationRepository
import com.example.digitracksdk.domain.repository.onboarding.pf_uan.PaperlessPFESICRepository
import com.example.digitracksdk.domain.repository.onboarding.signature.PaperlessViewGetSignatureRepository
import com.example.digitracksdk.domain.repository.pay_slip.PaySlipRepository
import com.example.digitracksdk.domain.repository.pf_uan_repository.PfEsicInsuranceRepository
import com.example.digitracksdk.domain.repository.profile_repository.ProfileRepository
import com.example.digitracksdk.domain.repository.refer_friend_repository.ReferFriendRepository
import com.example.digitracksdk.domain.usecase.attendance_usecase.ValidateLogVisitTokenUseCase
import com.example.digitracksdk.domain.repository.refine.RefineRepository
import com.example.digitracksdk.domain.repository.reimbursement_repository.ReimbursementRepository
import com.example.digitracksdk.domain.repository.resignation.ResignationRepository
import com.example.digitracksdk.domain.repository.rewards.RewardsRepository
import com.example.digitracksdk.domain.repository.training_repository.TrainingRepository
import com.example.digitracksdk.domain.repository.verify_login_otp_repository.VerifyLoginOtpRepository
import com.example.digitracksdk.domain.repository.view_payout.ViewPayoutRepository
import com.example.digitracksdk.domain.usecase.attendance_usecase.CurrentDayAtteStatusUseCase
import com.example.digitracksdk.domain.usecase.attendance_usecase.DashboardAttendanceStatusUseCase
import com.example.digitracksdk.domain.usecase.attendance_regularization_usecase.AttendanceRegularizationInsertUseCase
import com.example.digitracksdk.domain.usecase.attendance_usecase.CheckValidAttendanceTokenUseCase
import com.example.digitracksdk.domain.usecase.attendance_usecase.CreateAttendanceTokenUseCase
import com.example.digitracksdk.domain.usecase.attendance_usecase.GetAttendanceTimeSheetUseCase
import com.example.digitracksdk.domain.usecase.attendance_usecase.LeaveHexCodeUseCase
import com.example.digitracksdk.domain.usecase.attendance_usecase.LogYourVisitUseCase
import com.example.digitracksdk.domain.usecase.attendance_usecase.ViewAttendanceUseCase
import com.example.digitracksdk.domain.usecase.attendance_regularization_usecase.AttendanceRegularizationListUseCase
import com.example.digitracksdk.domain.usecase.attendance_regularization_usecase.AttendanceRegularizationTypeUseCase
import com.example.digitracksdk.domain.usecase.attendance_usecase.AttendanceCycleUseCase
import com.example.digitracksdk.domain.usecase.attendance_usecase.AttendanceFlagUseCase
import com.example.digitracksdk.domain.usecase.attendance_usecase.AttendanceGeoFancingUseCase
import com.example.digitracksdk.domain.usecase.attendance_usecase.AttendanceStatusUseCase
import com.example.digitracksdk.domain.usecase.attendance_usecase.AttendanceValidationUseCase
import com.example.digitracksdk.domain.usecase.attendance_usecase.AttendanceZoneUseCase
import com.example.digitracksdk.domain.usecase.attendance_usecase.InsertAttendancePicUseCase
import com.example.digitracksdk.domain.usecase.attendance_regularization_usecase.InsertAttendanceRegularizationUseCase
import com.example.digitracksdk.domain.usecase.attendance_usecase.AttendanceMarkUseCase
import com.example.digitracksdk.domain.usecase.attendance_usecase.CreateLogVisitTokenUseCase
import com.example.digitracksdk.domain.usecase.attendance_usecase.*
import com.example.digitracksdk.domain.usecase.candidate_usecase.CandidateAttendanceUseCase
import com.example.digitracksdk.domain.usecase.client_policies.ClientPoliciesUseCase
import com.example.digitracksdk.domain.usecase.client_policies.InsertAcknowledgeUseCase
import com.example.digitracksdk.domain.usecase.client_policies.PolicyAcknowledgeUseCase
import com.example.digitracksdk.domain.usecase.client_policies.ViewAckPolicyUseCase
import com.example.digitracksdk.domain.usecase.client_policies.ViewPolicyUseCase
import com.example.digitracksdk.domain.usecase.customer_id_card_usecase.CustomerIdCardUseCase
import com.example.digitracksdk.domain.usecase.dashboard.CheckBirthdayUseCase
import com.example.digitracksdk.domain.usecase.dashboard.InductionTrainingUseCase
import com.example.digitracksdk.domain.usecase.dashboard.InnovIDCardUseCase
import com.example.digitracksdk.domain.usecase.help_and_support_usecase.HelpAndSupportListUseCase
import com.example.digitracksdk.domain.usecase.dashboard.JobReferralUseCase
import com.example.digitracksdk.domain.usecase.dashboard.QrCodeUseCase
import com.example.digitracksdk.domain.usecase.dashboard.ReferredUsersUseCase
import com.example.digitracksdk.domain.usecase.dashboard.SurveyLinkUseCase
import com.example.digitracksdk.domain.usecase.exit_questionnaire_usecase.ExitQuestionnaireUseCase
import com.example.digitracksdk.domain.usecase.geo_tracking_usecase.GeoTrackingDetailsUseCase
import com.example.digitracksdk.domain.usecase.geo_tracking_usecase.GeoTrackingUseCase
import com.example.digitracksdk.domain.usecase.help_and_support_usecase.AssociateIssueUseCase
import com.example.digitracksdk.domain.usecase.help_and_support_usecase.IssueCategoryUseCase
import com.example.digitracksdk.domain.usecase.help_and_support_usecase.IssueDetailsUseCase
import com.example.digitracksdk.domain.usecase.help_and_support_usecase.IssueSubCategoryUseCase
import com.example.digitracksdk.domain.usecase.help_and_support_usecase.*
import com.example.digitracksdk.domain.usecase.home_usecase.HomeBannerUseCase
import com.example.digitracksdk.domain.usecase.home_usecase.HomeDashboardUseCase
import com.example.digitracksdk.domain.usecase.leave_usecase.ApplyLeavesUseCase
import com.example.digitracksdk.domain.usecase.leave_usecase.HolidaysListUseCase
import com.example.digitracksdk.domain.usecase.leave_usecase.LeaveStatusSummaryUseCase
import com.example.digitracksdk.domain.usecase.leave_usecase.LeavesBalanceUseCase
import com.example.digitracksdk.domain.usecase.leave_usecase.LeavesTypeUseCase
import com.example.digitracksdk.domain.usecase.my_letters_usecase.DownloadCandidateOfferLetterUseCase
import com.example.digitracksdk.domain.usecase.onboarding.PaperlessViewWorkExpDetailsUseCase
import com.example.digitracksdk.domain.usecase.income_tax_use_case.IncomeTaxDeclarationUseCase
import com.example.digitracksdk.domain.usecase.leave_usecase.BalanceLeaveStatusUseCase
import com.example.digitracksdk.domain.usecase.leave_usecase.LeavesRequestViewUseCase
import com.example.digitracksdk.domain.usecase.leave_usecase.*
import com.example.digitracksdk.domain.usecase.login_usecase.CheckAppVersionUseCase
import com.example.digitracksdk.domain.usecase.mileage_tracking_usecase.InsertMileageRegularizationUseCase
import com.example.digitracksdk.domain.usecase.mileage_tracking_usecase.MileageTrackingListUseCase
import com.example.digitracksdk.domain.usecase.my_letters_usecase.CandidateLoiListUseCase
import com.example.digitracksdk.domain.usecase.my_letters_usecase.CandidateOfferLettersListUseCase
import com.example.digitracksdk.domain.usecase.login_usecase.FirebaseTokenUpdateUseCase
import com.example.digitracksdk.domain.usecase.login_usecase.LoginUseCase
import com.example.digitracksdk.domain.usecase.mileage_tracking_usecase.MileageRegularizationListUseCase
import com.example.digitracksdk.domain.usecase.my_letters_usecase.GetForm16UseCase
import com.example.digitracksdk.domain.usecase.my_letters_usecase.GetIncrementLetterUseCase
import com.example.digitracksdk.domain.usecase.my_letters_usecase.OfferLetterAcceptRejectUseCase
import com.example.digitracksdk.domain.usecase.login_usecase.VerifyLoginOtpUseCase
import com.example.digitracksdk.domain.usecase.mileage_tracking_usecase.InsertMileageTrackingUseCase
import com.example.digitracksdk.domain.usecase.mileage_tracking_usecase.MileageTrackingFlagUseCase
import com.example.digitracksdk.domain.usecase.my_letters_usecase.GetFinancialYearListUseCase
import com.example.digitracksdk.domain.usecase.my_letters_usecase.OtherLettersUseCase
import com.example.digitracksdk.domain.usecase.my_letters_usecase.ViewCandidateLoiUseCase
import com.example.digitracksdk.domain.usecase.mileage_tracking_usecase.*
import com.example.digitracksdk.domain.usecase.my_letters_usecase.*
import com.example.digitracksdk.domain.usecase.notification_usecase.NotificationDetailsUseCase
import com.example.digitracksdk.domain.usecase.notification_usecase.NotificationListUseCase
import com.example.digitracksdk.domain.usecase.onboarding.GetEducationCategoryUseCase
import com.example.digitracksdk.domain.usecase.onboarding.OnboardingDashboardUseCase
import com.example.digitracksdk.domain.usecase.onboarding.PaperlessViewCandidateDetailsUseCase
import com.example.digitracksdk.domain.usecase.onboarding.PaperlessViewCandidateReferenceDetailsUseCase
import com.example.digitracksdk.domain.usecase.onboarding.PaperlessViewEpfUseCase
import com.example.digitracksdk.domain.usecase.onboarding.PaperlessViewEsicUseCase
import com.example.digitracksdk.domain.usecase.onboarding.*
import com.example.digitracksdk.domain.usecase.onboarding.aadhaar_verification.AadhaarVerificationOtpValidationUseCase
import com.example.digitracksdk.domain.usecase.onboarding.aadhaar_verification.AadhaarVerificationSendOtpUseCase
import com.example.digitracksdk.domain.usecase.onboarding.aadhaar_verification.GetAadhaarVerificationDetailsUseCase
import com.example.digitracksdk.domain.usecase.onboarding.aadhaar_verification.ValidateAadhaarUseCase
import com.example.digitracksdk.domain.usecase.onboarding.aadhaar_verification_digilocker.AadhaarDataUseCase
import com.example.digitracksdk.domain.usecase.onboarding.aadhaar_verification_digilocker.AadhaarSaveRequestIdUseCase
import com.example.digitracksdk.domain.usecase.onboarding.aadhaar_verification_digilocker.AadhaarVerificationDigiLockerUseCase
import com.example.digitracksdk.domain.usecase.onboarding.bank_account_verification.BankAccountVerificationDetailsUseCase
import com.example.digitracksdk.domain.usecase.onboarding.bank_account_verification.BankAccountVerificationUseCase
import com.example.digitracksdk.domain.usecase.onboarding.bank_usecase.ChequeValidationUseCase
import com.example.digitracksdk.domain.usecase.onboarding.bank_usecase.GetBankListUseCase
import com.example.digitracksdk.domain.usecase.onboarding.bank_usecase.PaperlessViewBankDetailsUseCase
import com.example.digitracksdk.domain.usecase.onboarding.documents.GetUploadedDocumentUseCase
import com.example.digitracksdk.domain.usecase.profile_usecase.CityListUseCase
import com.example.digitracksdk.domain.usecase.profile_usecase.ValidCandidateUseCase
import com.example.digitracksdk.domain.usecase.onboarding.bank_usecase.UpdateBankDetailsUseCase
import com.example.digitracksdk.domain.usecase.onboarding.cibil_score.GetCibilScoreUseCase
import com.example.digitracksdk.domain.usecase.onboarding.documents.InsertPendingDocumentsUseCase
import com.example.digitracksdk.domain.usecase.onboarding.documents.PendingDocumentListUseCase
import com.example.digitracksdk.domain.usecase.onboarding.documents.UploadedDocumentsListUseCase
import com.example.digitracksdk.domain.usecase.onboarding.documents.ViewDocumentsListUseCase
import com.example.digitracksdk.domain.usecase.onboarding.documents.*
import com.example.digitracksdk.domain.usecase.onboarding.education_details.EducationalStreamUseCase
import com.example.digitracksdk.domain.usecase.onboarding.insert.InsertCandidateReferenceDetailsUseCase
import com.example.digitracksdk.domain.usecase.onboarding.insert.POBEpfUseCase
import com.example.digitracksdk.domain.usecase.onboarding.insert.POBEsicUseCase
import com.example.digitracksdk.domain.usecase.profile_usecase.UpdateProfileUseCase
import com.example.digitracksdk.domain.usecase.onboarding.education_details.PaperlessViewEducationDetailsUseCase
import com.example.digitracksdk.domain.usecase.onboarding.family_details.InsertFamilyDetailsUseCase
import com.example.digitracksdk.domain.usecase.onboarding.insert.CandidateProfileAckUseCase
import com.example.digitracksdk.domain.usecase.onboarding.insert.GetReferenceCategoryUseCase
import com.example.digitracksdk.domain.usecase.onboarding.insert.POBInsertEducationInfoUseCase
import com.example.digitracksdk.domain.usecase.onboarding.insert.PaperlessUpdateCandidateBasicDetailsUseCase
import com.example.digitracksdk.domain.usecase.onboarding.family_details.ViewFamilyDetailsUseCase
import com.example.digitracksdk.domain.usecase.onboarding.insert.*
import com.example.digitracksdk.domain.usecase.onboarding.kyc.KycDocumentPendingListUseCase
import com.example.digitracksdk.domain.usecase.onboarding.kyc.KycDocumentsUseCase
import com.example.digitracksdk.domain.usecase.onboarding.pan_verification.PanVerificationDetailsUseCase
import com.example.digitracksdk.domain.usecase.onboarding.pan_verification.PanVerificationUseCase
import com.example.digitracksdk.domain.usecase.onboarding.pf_uan.GetPFESICUseCase
import com.example.digitracksdk.domain.usecase.onboarding.pf_uan.InsertPFESICUseCase
import com.example.digitracksdk.domain.usecase.onboarding.signature_usecase.InsertSignatureUseCase
import com.example.digitracksdk.domain.usecase.onboarding.signature_usecase.PaperlessViewGetSignatureUseCase
import com.example.digitracksdk.domain.usecase.onboarding.work_exp_usecase.InsertWorkExpUseCase
import com.example.digitracksdk.domain.usecase.pay_slip_usecase.DownloadPaySlipUseCase
import com.example.digitracksdk.domain.usecase.pay_slip_usecase.PaySlipDownloadNewUseCase
import com.example.digitracksdk.domain.usecase.pay_slip_usecase.PaySlipMonthYearUseCase
import com.example.digitracksdk.domain.usecase.profile_usecase.CheckOtpUseCase
import com.example.digitracksdk.domain.usecase.pay_slip_usecase.SalaryReleaseStatusUseCase
import com.example.digitracksdk.domain.usecase.profile_usecase.InsertBasicDetailsUseCase
import com.example.digitracksdk.domain.usecase.profile_usecase.NewInnovIdCreationUseCase
import com.example.digitracksdk.domain.usecase.profile_usecase.StateListUseCase
import com.example.digitracksdk.domain.usecase.pf_esic_insurance_usecase.EsicCardUseCase
import com.example.digitracksdk.domain.usecase.pf_esic_insurance_usecase.EsicMedicalCardUseCase
import com.example.digitracksdk.domain.usecase.pf_esic_insurance_usecase.MedicalCardUseCase
import com.example.digitracksdk.domain.usecase.profile_usecase.*
import com.example.digitracksdk.domain.usecase.refer_friend_usecase.BranchDetailsListUseCase
import com.example.digitracksdk.domain.usecase.refer_friend_usecase.ReferralFriendUseCase
import com.example.digitracksdk.domain.usecase.refer_friend_usecase.SkillListUseCase
import com.example.digitracksdk.domain.usecase.reimbursement_usecase.InsertNewReimbursementUseCase
import com.example.digitracksdk.domain.usecase.reimbursement_usecase.ReimbursementCategoryUseCase
import com.example.digitracksdk.domain.usecase.reimbursement_usecase.ReimbursementEndKmUseCase
import com.example.digitracksdk.domain.usecase.reimbursement_usecase.UpdateReimbursementDetailsUseCase
import com.example.digitracksdk.domain.usecase.refine.RefineUseCase
import com.example.digitracksdk.domain.usecase.reimbursement_usecase.CheckReimbursementLimitUseCase
import com.example.digitracksdk.domain.usecase.reimbursement_usecase.CheckReimbursementLimitV1UseCase
import com.example.digitracksdk.domain.usecase.reimbursement_usecase.DeleteNewReimbursementListItemUseCase
import com.example.digitracksdk.domain.usecase.reimbursement_usecase.GenerateNewReimbursementVoucherUseCase
import com.example.digitracksdk.domain.usecase.reimbursement_usecase.GetMonthDetailsUseCase
import com.example.digitracksdk.domain.usecase.reimbursement_usecase.GetReimbursementListForVoucherUseCase
import com.example.digitracksdk.domain.usecase.reimbursement_usecase.GetYearDetailsUseCase
import com.example.digitracksdk.domain.usecase.reimbursement_usecase.InsertReimbursementUseCase
import com.example.digitracksdk.domain.usecase.reimbursement_usecase.NewReimbursementApprovedListUseCase
import com.example.digitracksdk.domain.usecase.reimbursement_usecase.NewReimbursementPendingListUseCase
import com.example.digitracksdk.domain.usecase.reimbursement_usecase.NewReimbursementRejectedListUseCase
import com.example.digitracksdk.domain.usecase.reimbursement_usecase.PendingReimbursementUseCase
import com.example.digitracksdk.domain.usecase.reimbursement_usecase.ReimbursementBillUseCase
import com.example.digitracksdk.domain.usecase.reimbursement_usecase.ReimbursementDetailsUseCase
import com.example.digitracksdk.domain.usecase.reimbursement_usecase.ReimbursementListUseCase
import com.example.digitracksdk.domain.usecase.reimbursement_usecase.ReimbursementModeOfTravelUseCase
import com.example.digitracksdk.domain.usecase.reimbursement_usecase.ReimbursementSubCategoryUseCase
import com.example.digitracksdk.domain.usecase.reimbursement_usecase.RejectedEndKmUseCase
import com.example.digitracksdk.domain.usecase.reimbursement_usecase.RejectedReimbursementValidationUseCase
import com.example.digitracksdk.domain.usecase.reimbursement_usecase.SaveReimbursementPreApprovalUseCase
import com.example.digitracksdk.domain.usecase.reimbursement_usecase.UpdatePendingReimbursementUseCase
import com.example.digitracksdk.domain.usecase.reimbursement_usecase.UpdateReimbursementStatusUseCase
import com.example.digitracksdk.domain.usecase.reimbursement_usecase.UploadReimbursementBillUseCase
import com.example.digitracksdk.domain.usecase.reimbursement_usecase.*
import com.example.digitracksdk.domain.usecase.resignation_usecase.ResignationCategoryUseCase
import com.example.digitracksdk.domain.usecase.resignation_usecase.ResignationListUseCase
import com.example.digitracksdk.domain.usecase.resignation_usecase.ResignationNoticePeriodUseCase
import com.example.digitracksdk.domain.usecase.resignation_usecase.ResignationReasonUseCase
import com.example.digitracksdk.domain.usecase.resignation_usecase.ResignationUseCase
import com.example.digitracksdk.domain.usecase.resignation_usecase.RevokeResignationUseCase
import com.example.digitracksdk.domain.usecase.rewards_usecase.RewardsUseCase
import com.example.digitracksdk.domain.usecase.training_usecase.TrainingUseCase
import com.example.digitracksdk.domain.usecase.training_usecase.ViewTrainingDocumentUseCase
import com.example.digitracksdk.domain.usecase.view_payout.ViewPayoutUseCase
import okhttp3.Credentials
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

private const val TIME_OUT = 120L

val NetworkModule = module {

    single(named("normalService")) {
        createService(get(named("normal")))
    }
//    single(named("candidateService")) {
//        createService(get(named("candidate")))
//    }
    single(named("candidateAppService")) {
        createService(get(named("candidateApp")))
    }
    single(named("associateService")) {
        createService(get(named("associate")))
    }
    single(named("associateServiceForPanAndBank")) {
        createService(get(named("associateForPanAndBank")))
    }
    single(named("digiOneService")) {
        createService(get(named("digione")))
    }
    single(named("normal")) {
        createRetrofit(get(), ApiNames.BASE_URL)
    }
//    single(named("candidate")) {
//        createRetrofit(get(), ApiNames.BASE_URL_CANDIDATE)
//    }
    single(named("candidateApp")) {
        createRetrofit(get(), ApiNames.BASE_URL_CANDIDATE_APP)
    }
    single(named("innovFmDigiOne")) {
        createService(get(named("innovdigione")))
    }

    single(named("innovdigione")) {
        createRetrofit(get(), ApiNames.BASE_URL_Innov_DIGI_ONE)
    }
    single(named("associate")) {
        createRetrofit(get(), ApiNames.BASE_URL_ASSOCIATE)
    }
    single(named("associateForPanAndBank")) {
        createRetrofit(get(), ApiNames.BASE_URL_ASSOCIATE_FOR_PAN_AND_BANK)
    }
    single(named("digione")) {
        createRetrofit(get(), ApiNames.BASE_URL_DIGI_ONE)
    }
    single {
        createOkHttpClient()
    }
    single {
        GsonConverterFactory.create()
    }
    single {
        GsonBuilder().create()
    }

}

fun createOkHttpClient(): OkHttpClient {
    val httpLoggingInterceptor = HttpLoggingInterceptor()
    httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
    return OkHttpClient.Builder().addInterceptor(
        ChuckerInterceptor.Builder(context = TODO()) //TODO
            .collector(ChuckerCollector(context = TODO()))
            .maxContentLength(250000L)
            .redactHeaders(emptySet())
            .alwaysReadResponseBody(false)
            .build()
    )
        .connectTimeout(TIME_OUT, TimeUnit.SECONDS)
        .readTimeout(TIME_OUT, TimeUnit.SECONDS)
        .writeTimeout(TIME_OUT, TimeUnit.SECONDS)
        .protocols(arrayListOf(Protocol.HTTP_1_1))
        .addInterceptor(Interceptor { chain ->
            val original = chain.request()

            val mUrl = original.url.toString()

            val requestBuilder: Request.Builder =
                if (mUrl.contains(ApiNames.GetRefineUrl) || mUrl.contains(ApiNames.GetCibilScore)) {
                    basicAuthInterceptor("DigiOneAPI", "Digione@321").let {
                        original.newBuilder()
                            .header("Authorization", it)
                            .header("Content-Type", "application/json")
                            .method(original.method, original.body)
                    }

                } else {
                    original.newBuilder()
                        .header("Authorization", "application/json")
                        .header("Content-Type", "application/json")
                        .method(original.method, original.body)
                }
            val request = requestBuilder.build()
            chain.proceed(request)
        })
        .addInterceptor(httpLoggingInterceptor)
        .build()
}

fun basicAuthInterceptor(user: String, password: String): String {
    return Credentials.basic(user, password)
}

fun createRetrofit(okHttpClient: OkHttpClient, url: String): Retrofit {
    return Retrofit.Builder()
        .baseUrl(url)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create()).build()
}

fun createService(retrofit: Retrofit): ApiService {
    return retrofit.create(ApiService::class.java)
}

fun createLoginRepository(normalService: ApiService, digiOneService: ApiService): LoginRepository {
    return LoginRepositoryImp(normalService, digiOneService)
}

fun createLoginUseCase(loginRepository: LoginRepository): LoginUseCase {
    return LoginUseCase(loginRepository)
}

fun createFirebaseTokenUpdateUseCase(loginRepository: LoginRepository): FirebaseTokenUpdateUseCase {
    return FirebaseTokenUpdateUseCase(loginRepository)
}

fun createCheckAppVersionUseCase(loginRepository: LoginRepository): CheckAppVersionUseCase {
    return CheckAppVersionUseCase(loginRepository)
}

fun createVerifyLoginOtpRepository(apiService: ApiService): VerifyLoginOtpRepository {
    return VerifyLoginOtpRepositoryImp(apiService)
}

fun createVerifyLoginOtpUseCase(verifyLoginOtpRepository: VerifyLoginOtpRepository): VerifyLoginOtpUseCase {
    return VerifyLoginOtpUseCase(verifyLoginOtpRepository)
}

fun createHomeDashboardRepository(apiService: ApiService): HomeRepository {
    return HomeRepositoryImp(apiService)
}

fun createHomeHomeDashboardUseCase(homeRepository: HomeRepository): HomeDashboardUseCase {
    return HomeDashboardUseCase(homeRepository)
}

fun createInductionTrainingUseCase(homeRepository: HomeRepository): InductionTrainingUseCase {
    return InductionTrainingUseCase(homeRepository)
}

fun createSurveyLinkUseCase(homeRepository: HomeRepository): SurveyLinkUseCase {
    return SurveyLinkUseCase(homeRepository)
}

fun createCheckBirthdayUseCase(homeRepository: HomeRepository): CheckBirthdayUseCase {
    return CheckBirthdayUseCase(homeRepository)
}

fun createHomeBannerRepository(apiService: ApiService): HomeBannerRepository {
    return HomeBannerRepositoryImp(apiService)
}

fun createHomeBannerUseCase(homeBannerRepository: HomeBannerRepository): HomeBannerUseCase {
    return HomeBannerUseCase(homeBannerRepository)
}

fun createInnovIDCardRepository(apiService: ApiService): InnovIDCardRepository {
    return InnovIDCardRepositoryImp(apiService)
}

fun createInnovIDCardUseCase(innovIDCardRepository: InnovIDCardRepository): InnovIDCardUseCase {
    return InnovIDCardUseCase(innovIDCardRepository)
}

fun createQrCodeUseCase(innovIDCardRepository: InnovIDCardRepository): QrCodeUseCase {
    return QrCodeUseCase(innovIDCardRepository)
}


fun createJobReferralRepository(apiService: ApiService): JobReferralRepository {
    return JobReferralRepositoryImp(apiService)
}

fun createJobReferralUseCase(jobReferralRepository: JobReferralRepository): JobReferralUseCase {
    return JobReferralUseCase(jobReferralRepository)
}

fun createReferredUsersUseCase(jobReferralRepository: JobReferralRepository): ReferredUsersUseCase {
    return ReferredUsersUseCase(jobReferralRepository)
}

fun createPfEsicInsuranceRepository(apiService: ApiService): PfEsicInsuranceRepository {
    return PfEsicInsuranceRepositoryImp(apiService)
}

fun createEsicMedicalCardUseCase(pfEsicInsuranceRepository: PfEsicInsuranceRepository): EsicMedicalCardUseCase {
    return EsicMedicalCardUseCase(pfEsicInsuranceRepository)
}

fun createEsicCardUseCase(pfEsicInsuranceRepository: PfEsicInsuranceRepository): EsicCardUseCase {
    return EsicCardUseCase(pfEsicInsuranceRepository)
}

fun createMedicalCardUseCase(pfEsicInsuranceRepository: PfEsicInsuranceRepository): MedicalCardUseCase {
    return MedicalCardUseCase(pfEsicInsuranceRepository)
}

fun createHelpAndSupportRepository(apiService: ApiService): HelpAndSupportRepository {
    return HelpAndSupportRepositoryImp(apiService)
}

fun createHelpAndSupportListUseCase(helpAndSupportRepository: HelpAndSupportRepository): HelpAndSupportListUseCase {
    return HelpAndSupportListUseCase(helpAndSupportRepository)
}

fun createAssociateIssueUseCase(helpAndSupportRepository: HelpAndSupportRepository): AssociateIssueUseCase {
    return AssociateIssueUseCase(helpAndSupportRepository)
}

fun createIssueCategoryUseCase(helpAndSupportRepository: HelpAndSupportRepository): IssueCategoryUseCase {
    return IssueCategoryUseCase(helpAndSupportRepository)
}

fun createIssueSubCategoryUseCase(helpAndSupportRepository: HelpAndSupportRepository): IssueSubCategoryUseCase {
    return IssueSubCategoryUseCase(helpAndSupportRepository)
}

fun createIssueDetailsUseCase(helpAndSupportRepository: HelpAndSupportRepository): IssueDetailsUseCase {
    return IssueDetailsUseCase(helpAndSupportRepository)
}

fun createResignationRepository(apiService: ApiService): ResignationRepository {
    return ResignationRepositoryImp(apiService)
}

fun createResignationCategoryUseCase(resignationRepository: ResignationRepository): ResignationCategoryUseCase {
    return ResignationCategoryUseCase(resignationRepository)
}

fun createResignationListUseCase(resignationRepository: ResignationRepository): ResignationListUseCase {
    return ResignationListUseCase(resignationRepository)
}

fun createResignationUseCase(resignationRepository: ResignationRepository): ResignationUseCase {
    return ResignationUseCase(resignationRepository)
}

fun createResignationNoticePeriodUseCase(resignationRepository: ResignationRepository): ResignationNoticePeriodUseCase {
    return ResignationNoticePeriodUseCase(resignationRepository)
}

fun createRevokeResignationUseCase(resignationRepository: ResignationRepository): RevokeResignationUseCase {

    return RevokeResignationUseCase(resignationRepository)
}

fun createResignationReasonUseCase(resignationRepository: ResignationRepository): ResignationReasonUseCase {
    return ResignationReasonUseCase(resignationRepository)
}

fun createReferralFriendRepository(apiService: ApiService): ReferFriendRepository {
    return ReferralFriendRepositoryImp(apiService)
}

fun createReferralFriendUseCase(referFriendRepository: ReferFriendRepository): ReferralFriendUseCase {
    return ReferralFriendUseCase(referFriendRepository)
}

fun createSkillListUseCase(referFriendRepository: ReferFriendRepository): SkillListUseCase {
    return SkillListUseCase(referFriendRepository)
}

fun createBranchDetailsListUseCase(referFriendRepository: ReferFriendRepository): BranchDetailsListUseCase {
    return BranchDetailsListUseCase(referFriendRepository)
}

fun createTrainingRepository(apiService: ApiService): TrainingRepository {
    return TrainingRepositoryImp(apiService)
}

fun createTrainingUseCase(trainingRepository: TrainingRepository): TrainingUseCase {
    return TrainingUseCase(trainingRepository)
}

fun createViewTrainingDocumentUseCase(trainingRepository: TrainingRepository): ViewTrainingDocumentUseCase {
    return ViewTrainingDocumentUseCase(trainingRepository)
}

fun createClientPoliciesRepository(apiService: ApiService): ClientPoliciesRepository {
    return ClientPoliciesRepositoryImp(apiService)
}

fun createClientPoliciesUseCase(clientPoliciesRepository: ClientPoliciesRepository): ClientPoliciesUseCase {
    return ClientPoliciesUseCase(clientPoliciesRepository)
}

fun createInsertAcknowledgeUseCase(clientPoliciesRepository: ClientPoliciesRepository): InsertAcknowledgeUseCase {
    return InsertAcknowledgeUseCase(clientPoliciesRepository)
}

fun createViewPolicyUseCase(clientPoliciesRepository: ClientPoliciesRepository): ViewPolicyUseCase {
    return ViewPolicyUseCase(clientPoliciesRepository)
}

fun createViewAckPolicyUseCase(clientPoliciesRepository: ClientPoliciesRepository): ViewAckPolicyUseCase {
    return ViewAckPolicyUseCase(clientPoliciesRepository)
}

fun createPolicyAcknowledgementUseCase(clientPoliciesRepository: ClientPoliciesRepository): PolicyAcknowledgeUseCase {
    return PolicyAcknowledgeUseCase(clientPoliciesRepository)
}


fun createLeavesRepository(apiService: ApiService): LeavesRepository {
    return LeavesRepositoryImp(apiService)
}

fun createHolidaysListUseCase(leavesRepository: LeavesRepository): HolidaysListUseCase {
    return HolidaysListUseCase(leavesRepository)
}

fun createBalanceLeaveStatusUseCase(leavesRepository: LeavesRepository): BalanceLeaveStatusUseCase {
    return BalanceLeaveStatusUseCase(leavesRepository)
}

fun createLeaveStatusSummaryUseCase(leavesRepository: LeavesRepository): LeaveStatusSummaryUseCase {
    return LeaveStatusSummaryUseCase(leavesRepository)
}

fun createApplyLeavesUseCase(leavesRepository: LeavesRepository): ApplyLeavesUseCase {
    return ApplyLeavesUseCase(leavesRepository)
}

fun createLeavesBalanceUseCase(leavesRepository: LeavesRepository): LeavesBalanceUseCase {
    return LeavesBalanceUseCase(leavesRepository)
}

fun createLeavesRequestViewUseCase(leavesRepository: LeavesRepository): LeavesRequestViewUseCase {
    return LeavesRequestViewUseCase(leavesRepository)
}

fun createLeavesTypeUseCase(leavesRepository: LeavesRepository): LeavesTypeUseCase {
    return LeavesTypeUseCase(leavesRepository)
}

fun createReimbursementRepository(apiService: ApiService): ReimbursementRepository {
    return ReimbursementRepositoryImp(apiService)
}

fun createNewReimbursementPendingListUseCase(reimbursementRepository: ReimbursementRepository): NewReimbursementPendingListUseCase {
    return NewReimbursementPendingListUseCase(reimbursementRepository)
}

fun createNewReimbursementApprovedListUseCase(reimbursementRepository: ReimbursementRepository): NewReimbursementApprovedListUseCase {
    return NewReimbursementApprovedListUseCase(reimbursementRepository)
}

fun createNewReimbursementRejectedListUseCase(reimbursementRepository: ReimbursementRepository): NewReimbursementRejectedListUseCase {
    return NewReimbursementRejectedListUseCase(reimbursementRepository)
}

fun createReimbursementListUseCase(reimbursementRepository: ReimbursementRepository): ReimbursementListUseCase {
    return ReimbursementListUseCase(reimbursementRepository)
}

fun createInsertNewReimbursementUseCase(reimbursementRepository: ReimbursementRepository): InsertNewReimbursementUseCase {
    return InsertNewReimbursementUseCase(reimbursementRepository)
}

fun createRejectedEndKmUseCase(reimbursementRepository: ReimbursementRepository): RejectedEndKmUseCase {
    return RejectedEndKmUseCase(reimbursementRepository)
}

fun createSaveReimbursementPreApprovalUseCase(reimbursementRepository: ReimbursementRepository): SaveReimbursementPreApprovalUseCase {
    return SaveReimbursementPreApprovalUseCase(reimbursementRepository)
}

fun createCheckReimbursementLimitUseCase(reimbursementRepository: ReimbursementRepository): CheckReimbursementLimitUseCase {
    return CheckReimbursementLimitUseCase(reimbursementRepository)
}

fun createRejectedReimbursementValidationUseCase(reimbursementRepository: ReimbursementRepository): RejectedReimbursementValidationUseCase {
    return RejectedReimbursementValidationUseCase(reimbursementRepository)
}

fun createRCheckReimbursementLimitV1UseCase(reimbursementRepository: ReimbursementRepository): CheckReimbursementLimitV1UseCase {
    return CheckReimbursementLimitV1UseCase(reimbursementRepository)
}

fun createUpdateReimbursementUseCase(reimbursementRepository: ReimbursementRepository): UpdatePendingReimbursementUseCase {
    return UpdatePendingReimbursementUseCase(reimbursementRepository)
}

fun createUpdateReimbursementStatusUseCase(reimbursementRepository: ReimbursementRepository): UpdateReimbursementStatusUseCase {
    return UpdateReimbursementStatusUseCase(reimbursementRepository)
}

fun createUpdateReimbursementDetailsUseCase(reimbursementRepository: ReimbursementRepository): UpdateReimbursementDetailsUseCase {
    return UpdateReimbursementDetailsUseCase(reimbursementRepository)
}

fun createPendingReimbursementListUseCase(reimbursementRepository: ReimbursementRepository): PendingReimbursementUseCase {
    return PendingReimbursementUseCase(reimbursementRepository)
}

fun createUploadReimbursementBillUseCase(reimbursementRepository: ReimbursementRepository): UploadReimbursementBillUseCase {
    return UploadReimbursementBillUseCase(reimbursementRepository)
}

fun createReimbursementDetailsUseCase(reimbursementRepository: ReimbursementRepository): ReimbursementDetailsUseCase {
    return ReimbursementDetailsUseCase(reimbursementRepository)
}

fun createReimbursementBillUseCase(reimbursementRepository: ReimbursementRepository): ReimbursementBillUseCase {
    return ReimbursementBillUseCase(reimbursementRepository)
}

fun createInsertReimbursementUseCase(reimbursementRepository: ReimbursementRepository): InsertReimbursementUseCase {
    return InsertReimbursementUseCase(reimbursementRepository)
}

fun createReimbursementCategoryUseCase(reimbursementRepository: ReimbursementRepository): ReimbursementCategoryUseCase {
    return ReimbursementCategoryUseCase(reimbursementRepository)
}

fun createReimbursementEndKmUseCase(reimbursementRepository: ReimbursementRepository): ReimbursementEndKmUseCase {
    return ReimbursementEndKmUseCase(reimbursementRepository)
}

fun createReimbursementModeOfTravelUseCase(reimbursementRepository: ReimbursementRepository): ReimbursementModeOfTravelUseCase {
    return ReimbursementModeOfTravelUseCase(reimbursementRepository)
}

fun createReimbursementSubCategoryUseCase(reimbursementRepository: ReimbursementRepository): ReimbursementSubCategoryUseCase {
    return ReimbursementSubCategoryUseCase(reimbursementRepository)
}

fun createReimbursementValidationUseCase(reimbursementRepository: ReimbursementRepository): ReimbursementValidationUseCase {
    return ReimbursementValidationUseCase(reimbursementRepository)
}

fun createGetReimbursementListForVoucherUseCase(reimbursementRepository: ReimbursementRepository): GetReimbursementListForVoucherUseCase {
    return GetReimbursementListForVoucherUseCase(reimbursementRepository)
}

fun createDeleteNewReimbursementListItemUseCase(reimbursementRepository: ReimbursementRepository): DeleteNewReimbursementListItemUseCase {
    return DeleteNewReimbursementListItemUseCase(reimbursementRepository)
}

fun createGenerateNewReimbursementVoucherUseCase(reimbursementRepository: ReimbursementRepository): GenerateNewReimbursementVoucherUseCase {
    return GenerateNewReimbursementVoucherUseCase(reimbursementRepository)
}

fun createGetMonthDetailsUseCase(reimbursementRepository: ReimbursementRepository): GetMonthDetailsUseCase {
    return GetMonthDetailsUseCase(reimbursementRepository)
}

fun createGetYearDetailsUseCase(reimbursementRepository: ReimbursementRepository): GetYearDetailsUseCase {
    return GetYearDetailsUseCase(reimbursementRepository)
}

fun createPaySlipRepository(
    apiServiceCandidate: ApiService,
    apiServiceNormal: ApiService
): PaySlipRepository {
    return PaySlipRepositoryImp(apiServiceCandidate, apiServiceNormal)
}

fun createDownloadPaySlipUseCase(paySlipRepository: PaySlipRepository): DownloadPaySlipUseCase {
    return DownloadPaySlipUseCase(paySlipRepository)
}

fun createDownloadPaySlipNewUseCase(paySlipRepository: PaySlipRepository): PaySlipDownloadNewUseCase {
    return PaySlipDownloadNewUseCase(paySlipRepository)
}

fun createPaySlipMonthYearUseCase(paySlipRepository: PaySlipRepository): PaySlipMonthYearUseCase {
    return PaySlipMonthYearUseCase(paySlipRepository)
}

fun createSalaryReleaseStatusUseCase(paySlipRepository: PaySlipRepository): SalaryReleaseStatusUseCase {
    return SalaryReleaseStatusUseCase(paySlipRepository)
}

fun createMyLettersRepository(apiService: ApiService): MyLettersRepository {
    return MyLettersRepositoryImp(apiService)
}

fun createGetIncrementLettersUseCase(myLettersRepository: MyLettersRepository): GetIncrementLetterUseCase {
    return GetIncrementLetterUseCase(myLettersRepository)
}

fun createCandidateLoiListUseCase(myLettersRepository: MyLettersRepository): CandidateLoiListUseCase {
    return CandidateLoiListUseCase(myLettersRepository)
}

fun createOtherLettersUseCase(myLettersRepository: MyLettersRepository): OtherLettersUseCase {
    return OtherLettersUseCase(myLettersRepository)
}

fun createOfferLetterAcceptRejectUseCase(myLettersRepository: MyLettersRepository): OfferLetterAcceptRejectUseCase {
    return OfferLetterAcceptRejectUseCase(myLettersRepository)
}

fun createCandidateOfferLettersListUseCase(myLettersRepository: MyLettersRepository): CandidateOfferLettersListUseCase {
    return CandidateOfferLettersListUseCase(myLettersRepository)
}

fun createDownloadCandidateOfferLetterUseCase(myLettersRepository: MyLettersRepository): DownloadCandidateOfferLetterUseCase {
    return DownloadCandidateOfferLetterUseCase(myLettersRepository)
}

fun createViewCandidateLoiUseCase(myLettersRepository: MyLettersRepository): ViewCandidateLoiUseCase {
    return ViewCandidateLoiUseCase(myLettersRepository)
}

fun createGetForm16UseCase(myLettersRepository: MyLettersRepository): GetForm16UseCase {
    return GetForm16UseCase(myLettersRepository)
}

fun createGetFinancialYearListUseCase(myLettersRepository: MyLettersRepository): GetFinancialYearListUseCase {
    return GetFinancialYearListUseCase(myLettersRepository)
}

fun createProfileRepository(apiService: ApiService): ProfileRepository {
    return ProfileRepositoryImp(apiService)
}

fun createStateListUseCase(profileRepository: ProfileRepository): StateListUseCase {
    return StateListUseCase(profileRepository)
}

fun createCityListUseCase(profileRepository: ProfileRepository): CityListUseCase {
    return CityListUseCase(profileRepository)
}

fun createUpdateProfileUseCase(profileRepository: ProfileRepository): UpdateProfileUseCase {
    return UpdateProfileUseCase(profileRepository)
}

fun createInsertBasicDetailsUseCase(profileRepository: ProfileRepository): InsertBasicDetailsUseCase {
    return InsertBasicDetailsUseCase(profileRepository)
}

fun createCheckOtpUseCase(profileRepository: ProfileRepository): CheckOtpUseCase {
    return CheckOtpUseCase(profileRepository)
}

fun createValidCandidateUseCase(profileRepository: ProfileRepository): ValidCandidateUseCase {
    return ValidCandidateUseCase(profileRepository)
}

fun createNewInnovIdCreationUseCase(profileRepository: ProfileRepository): NewInnovIdCreationUseCase {
    return NewInnovIdCreationUseCase(profileRepository)
}

fun createAttendanceRegularizationRepository(
    apiServiceNormal: ApiService,
    apiServiceCandidateApp: ApiService
): AttendanceRegularizationRepository {
    return AttendanceRegularizationRepositoryImp(apiServiceNormal, apiServiceCandidateApp)
}

fun createAttendanceRegularizationInsertUseCase(attendanceRegularizationRepository: AttendanceRegularizationRepository): AttendanceRegularizationInsertUseCase {
    return AttendanceRegularizationInsertUseCase(attendanceRegularizationRepository)
}

fun createInsertAttendanceRegularizationUseCase(attendanceRegularizationRepository: AttendanceRegularizationRepository): InsertAttendanceRegularizationUseCase {
    return InsertAttendanceRegularizationUseCase(attendanceRegularizationRepository)
}

fun createAttendanceRegularizationTypeUseCase(attendanceRegularizationRepository: AttendanceRegularizationRepository): AttendanceRegularizationTypeUseCase {
    return AttendanceRegularizationTypeUseCase(attendanceRegularizationRepository)
}

fun createAttendanceRegularizationListUseCase(attendanceRegularizationRepository: AttendanceRegularizationRepository): AttendanceRegularizationListUseCase {
    return AttendanceRegularizationListUseCase(attendanceRegularizationRepository)
}

fun createMileageTrackingRepository(apiService: ApiService): MileageTrackingRepository {
    return MileageTrackingRepositoryImp(apiService)
}

fun createInsertMileageRegularizationUseCase(mileageTrackingRepository: MileageTrackingRepository): InsertMileageRegularizationUseCase {
    return InsertMileageRegularizationUseCase(mileageTrackingRepository)
}

fun createMileageRegularizationListUseCase(mileageTrackingRepository: MileageTrackingRepository): MileageRegularizationListUseCase {
    return MileageRegularizationListUseCase(mileageTrackingRepository)
}

fun createInsertMileageTrackingUseCase(mileageTrackingRepository: MileageTrackingRepository): InsertMileageTrackingUseCase {
    return InsertMileageTrackingUseCase(mileageTrackingRepository)
}

fun createMileageTrackingFlagUseCase(mileageTrackingRepository: MileageTrackingRepository): MileageTrackingFlagUseCase {
    return MileageTrackingFlagUseCase(mileageTrackingRepository)
}

fun createMileageTrackingListUseCase(mileageTrackingRepository: MileageTrackingRepository): MileageTrackingListUseCase {
    return MileageTrackingListUseCase(mileageTrackingRepository)
}


// onboarding
fun createPaperlessViewEpfRepository(apiService: ApiService): PaperlessViewEpfRepository {
    return PaperlessViewEpfRepositoryImp(apiService)
}

fun createPaperlessViewEpfUseCase(repository: PaperlessViewEpfRepository): PaperlessViewEpfUseCase {
    return PaperlessViewEpfUseCase(repository)
}

fun createPaperlessViewEsicRepository(apiService: ApiService): PaperlessViewEsicRepository {
    return PaperlessViewEsicRepositoryImp(apiService)
}

fun createPaperlessViewEsicUseCase(repository: PaperlessViewEsicRepository): PaperlessViewEsicUseCase {
    return PaperlessViewEsicUseCase(repository)
}


fun createPaperlessViewFamilyDetailsRepository(apiService: ApiService): FamilyDetailsRepository {
    return FamilyDetailsRepositoryImp(apiService)
}

fun createPaperlessViewFamilyDetailsUseCase(repository: FamilyDetailsRepository): ViewFamilyDetailsUseCase {
    return ViewFamilyDetailsUseCase(repository)
}

fun createPaperlessInsertFamilyUseCase(repository: FamilyDetailsRepository): InsertFamilyDetailsUseCase {
    return InsertFamilyDetailsUseCase(repository)
}

fun createPaperlessPFESICRepository(apiService: ApiService): PaperlessPFESICRepository {
    return PFESICRepositoryImp(apiService)
}

fun createPaperlessInsertPFESICUseCase(repository: PaperlessPFESICRepository): InsertPFESICUseCase {
    return InsertPFESICUseCase(repository)
}

fun createPaperlessGetPFESICUseCase(repository: PaperlessPFESICRepository): GetPFESICUseCase {
    return GetPFESICUseCase(repository)
}

fun createPaperlessViewEducationDetailsRepository(apiService: ApiService): EducationDetailsRepository {
    return EducationDetailsRepositoryImp(apiService)
}

fun createPaperlessViewEducationDetailsUseCase(repository: EducationDetailsRepository): PaperlessViewEducationDetailsUseCase {
    return PaperlessViewEducationDetailsUseCase(repository)
}

fun createPaperlessGetEducationCategoryUseCase(repository: EducationDetailsRepository): GetEducationCategoryUseCase {
    return GetEducationCategoryUseCase(repository)
}

fun createPOBInsertEducationInfoUseCase(repository: EducationDetailsRepository): POBInsertEducationInfoUseCase {
    return POBInsertEducationInfoUseCase(repository)
}

fun createEducationalStreamUseCase(repository: EducationDetailsRepository): EducationalStreamUseCase {
    return EducationalStreamUseCase(repository)
}

fun createPaperlessViewWorkExpDetailsRepository(apiService: ApiService): PaperlessViewWorkExpDetailsRepository {
    return PaperlessViewWorkExpDetailsRepositoryImp(apiService)
}

fun createInsertWorkExpUseCase(repository: PaperlessViewWorkExpDetailsRepository): InsertWorkExpUseCase {
    return InsertWorkExpUseCase(repository)
}

fun createPaperlessViewWorkExpDetailsUseCase(repository: PaperlessViewWorkExpDetailsRepository): PaperlessViewWorkExpDetailsUseCase {
    return PaperlessViewWorkExpDetailsUseCase(repository)
}

fun createPaperlessViewCandidateDetailsRepository(apiService: ApiService): PaperlessViewCandidateDetailsRepository {
    return PaperlessViewCandidateDetailsRepositoryImp(apiService)
}

fun createPaperlessViewCandidateDetailsUseCase(repository: PaperlessViewCandidateDetailsRepository): PaperlessViewCandidateDetailsUseCase {
    return PaperlessViewCandidateDetailsUseCase(repository)
}


fun createPaperlessViewBankDetailsRepository(apiService: ApiService): BankDetailsRepository {
    return BankDetailsRepositoryImp(apiService)
}

fun createPaperlessViewBankDetailsUseCase(repository: BankDetailsRepository): PaperlessViewBankDetailsUseCase {
    return PaperlessViewBankDetailsUseCase(repository)
}


fun createChequeValidationUseCase(repository: BankDetailsRepository): ChequeValidationUseCase {
    return ChequeValidationUseCase(repository)
}

fun createUpdateBankDetailsUseCase(repository: BankDetailsRepository): UpdateBankDetailsUseCase {
    return UpdateBankDetailsUseCase(repository)
}

fun createGetBankListUseCase(repository: BankDetailsRepository): GetBankListUseCase {
    return GetBankListUseCase(repository)
}

fun createPaperlessViewCandidateReferenceDetailsRepository(apiService: ApiService): PaperlessViewCandidateReferenceDetailsRepository {
    return PaperlessViewCandidateReferenceDetailsRepositoryImp(apiService)
}

fun createPaperlessViewCandidateReferenceDetailsUseCase(repository: PaperlessViewCandidateReferenceDetailsRepository): PaperlessViewCandidateReferenceDetailsUseCase {
    return PaperlessViewCandidateReferenceDetailsUseCase(repository)
}

fun createPaperlessViewGetSignatureDetailsRepository(apiService: ApiService): PaperlessViewGetSignatureRepository {
    return PaperlessViewGetSignatureDetailsRepositoryImp(apiService)
}

fun createPaperlessViewGetSignatureDetailsUseCase(repository: PaperlessViewGetSignatureRepository): PaperlessViewGetSignatureUseCase {
    return PaperlessViewGetSignatureUseCase(repository)
}

fun createInsertSignatureUseCase(repository: PaperlessViewGetSignatureRepository): InsertSignatureUseCase {
    return InsertSignatureUseCase(repository)
}

// insert onboarding

fun createPaperlessInsertEpfRepository(apiService: ApiService): PobInsertEpfRepository {
    return POBInsertEpfRepositoryImp(apiService)
}

fun createPaperlessInsertEpfUseCase(repository: PobInsertEpfRepository): POBEpfUseCase {
    return POBEpfUseCase(repository)
}

fun createPaperlessInsertEsicRepository(apiService: ApiService): PobInsertEsicRepository {
    return POBInsertEsicRepositoryImp(apiService)
}

fun createPaperlessInsertEsicUseCase(repository: PobInsertEsicRepository): POBEsicUseCase {
    return POBEsicUseCase(repository)
}

fun createPaperlessGetReferenceCategoryUseCase(repository: ReferenceCategoryRepository): GetReferenceCategoryUseCase {
    return GetReferenceCategoryUseCase(repository)
}

fun createPaperlessInsertCandidateReferenceDetailsRepository(apiService: ApiService): ReferenceCategoryRepository {
    return ReferenceCategoryRepositoryImp(apiService)
}

fun createPaperlessInsertCandidateReferenceDetailsUseCase(repository: ReferenceCategoryRepository): InsertCandidateReferenceDetailsUseCase {
    return InsertCandidateReferenceDetailsUseCase(repository)
}

fun createDocumentsRepository(apiService: ApiService): DocumentsRepository {
    return DocumentsRepositoryImp(apiService)
}

fun createUploadedDocumentsListUseCase(documentsRepository: DocumentsRepository): UploadedDocumentsListUseCase {
    return UploadedDocumentsListUseCase(documentsRepository)
}

fun createGetDocumentUseCase(documentsRepository: DocumentsRepository): GetUploadedDocumentUseCase {
    return GetUploadedDocumentUseCase(documentsRepository)
}

fun createViewDocumentsListUseCase(documentsRepository: DocumentsRepository): ViewDocumentsListUseCase {
    return ViewDocumentsListUseCase(documentsRepository)
}

fun createPendingDocumentListUseCase(documentsRepository: DocumentsRepository): PendingDocumentListUseCase {
    return PendingDocumentListUseCase(documentsRepository)
}

fun createInsertPendingDocumentsUseCase(documentsRepository: DocumentsRepository): InsertPendingDocumentsUseCase {
    return InsertPendingDocumentsUseCase(documentsRepository)
}

fun createAttendanceRepository(apiService: ApiService): AttendanceRepository {
    return AttendanceRepositoryImp(apiService)
}

fun createAttendanceGoeFancingUseCase(attendanceRepository: AttendanceRepository): AttendanceGeoFancingUseCase {
    return AttendanceGeoFancingUseCase(attendanceRepository)
}

fun createUpdateAttendanceStatusUseCase(attendanceRepository: AttendanceRepository): UpdateAttendanceStatusUseCase {
    return UpdateAttendanceStatusUseCase(attendanceRepository)
}

fun createCreateLogVisitTokenUseCase(attendanceRepository: AttendanceRepository): CreateLogVisitTokenUseCase {
    return CreateLogVisitTokenUseCase(attendanceRepository)
}

fun createValidateLogVisitTokenUseCase(attendanceRepository: AttendanceRepository): ValidateLogVisitTokenUseCase {
    return ValidateLogVisitTokenUseCase(attendanceRepository)
}

fun createPaperlessUpdateCandidateBasicDetailsUseCase(repository: PaperlessViewCandidateDetailsRepository): PaperlessUpdateCandidateBasicDetailsUseCase {
    return PaperlessUpdateCandidateBasicDetailsUseCase(repository)
}

fun createKYCDocsRepository(apiService: ApiService): KYCRepository {
    return KYCRepositoryImp(apiService)
}

fun createKycDocumentPendingListUseCase(repository: KYCRepository): KycDocumentPendingListUseCase {
    return KycDocumentPendingListUseCase(repository)
}

fun createPaperlessDTGetKYCDocsUseCase(repository: KYCRepository): KycDocumentsUseCase {
    return KycDocumentsUseCase(repository)
}

fun createPaperlessCandidateProfileAckRepository(apiService: ApiService): CandidateProfileAckRepository {
    return CandidateProfileAckRepositoryImp(apiService)
}

fun createPaperlessCandidateProfileAckUseCase(repository: CandidateProfileAckRepository): CandidateProfileAckUseCase {
    return CandidateProfileAckUseCase(repository)
}

fun createDashboardAttendanceStatusUseCase(attendanceRepository: AttendanceRepository): DashboardAttendanceStatusUseCase {
    return DashboardAttendanceStatusUseCase(attendanceRepository)
}

fun createGetAttendanceTimeSheetUseCase(attendanceRepository: AttendanceRepository): GetAttendanceTimeSheetUseCase {
    return GetAttendanceTimeSheetUseCase(attendanceRepository)
}

fun createAttendanceStatusUseCase(attendanceRepository: AttendanceRepository): AttendanceStatusUseCase {
    return AttendanceStatusUseCase(attendanceRepository)
}

fun createAttendanceFlagUseCase(attendanceRepository: AttendanceRepository): AttendanceFlagUseCase {
    return AttendanceFlagUseCase(attendanceRepository)
}

fun createAttendanceMarkUseCase(attendanceRepository: AttendanceRepository): AttendanceMarkUseCase {
    return AttendanceMarkUseCase(attendanceRepository)
}

fun createAttendanceValidationUseCase(attendanceRepository: AttendanceRepository): AttendanceValidationUseCase {
    return AttendanceValidationUseCase(attendanceRepository)
}

fun createCurrentDayAtteStatusUseCase(attendanceRepository: AttendanceRepository): CurrentDayAtteStatusUseCase {
    return CurrentDayAtteStatusUseCase(attendanceRepository)
}

fun createAttendanceZoneUseCase(attendanceRepository: AttendanceRepository): AttendanceZoneUseCase {
    return AttendanceZoneUseCase(attendanceRepository)
}

fun createCreateAttendanceTokenUseCase(attendanceRepository: AttendanceRepository): CreateAttendanceTokenUseCase {
    return CreateAttendanceTokenUseCase(attendanceRepository)
}

fun createCheckValidAttendanceTokenUseCase(attendanceRepository: AttendanceRepository): CheckValidAttendanceTokenUseCase {
    return CheckValidAttendanceTokenUseCase(attendanceRepository)
}

fun createInsertAttendancePicUseCase(attendanceRepository: AttendanceRepository): InsertAttendancePicUseCase {
    return InsertAttendancePicUseCase(attendanceRepository)
}

fun createLogYourVisitUseCase(attendanceRepository: AttendanceRepository): LogYourVisitUseCase {
    return LogYourVisitUseCase(attendanceRepository)
}

fun createViewAttendanceUseCase(attendanceRepository: AttendanceRepository): ViewAttendanceUseCase {
    return ViewAttendanceUseCase(attendanceRepository)
}

fun createAttendanceCycleUseCase(attendanceRepository: AttendanceRepository): AttendanceCycleUseCase {
    return AttendanceCycleUseCase(attendanceRepository)
}

fun createLeaveHexCodeUseCase(attendanceRepository: AttendanceRepository): LeaveHexCodeUseCase {
    return LeaveHexCodeUseCase(attendanceRepository)
}

fun createNotificationRepository(apiService: ApiService): NotificationRepository {
    return NotificationRepositoryImp(apiService)
}

fun createNotificationListUseCase(notificationRepository: NotificationRepository): NotificationListUseCase {
    return NotificationListUseCase(notificationRepository)
}

fun createNotificationDetailsUseCase(notificationRepository: NotificationRepository): NotificationDetailsUseCase {
    return NotificationDetailsUseCase(notificationRepository)
}

fun createAadhaarVerificationRepository(
    apiServiceAssociate: ApiService,
    apiServiceNormal: ApiService
): AadhaarVerificationRepository {
    return AadhaarVerificationRepositoryImp(apiServiceAssociate, apiServiceNormal)
}


fun createAadhaarVerificationDigiLockerRepository(
    apiServiceAssociate: ApiService,
    apiServiceNormal: ApiService
): AadhaarVerificationDigiLockerRepository {
    return AadhaarVerificationDigiLockerRepositoryImp(apiServiceAssociate, apiServiceNormal)
}

fun createAadhaarDigiLockerUseCase(aadhaarVerificationDigiLockerRepository: AadhaarVerificationDigiLockerRepository): AadhaarVerificationDigiLockerUseCase {
    return AadhaarVerificationDigiLockerUseCase(aadhaarVerificationDigiLockerRepository)
}

fun createAadhaarSaveRequestIDUseCase(aadhaarVerificationDigiLockerRepository: AadhaarVerificationDigiLockerRepository): AadhaarSaveRequestIdUseCase {

    return AadhaarSaveRequestIdUseCase(aadhaarVerificationDigiLockerRepository)
}

fun createGetAadhaarDataUseCase(aadhaarVerificationDigiLockerRepository: AadhaarVerificationDigiLockerRepository): AadhaarDataUseCase {

    return AadhaarDataUseCase(aadhaarVerificationDigiLockerRepository)
}

fun createAadhaarVerificationSendOtpUseCase(aadhaarVerificationRepository: AadhaarVerificationRepository): AadhaarVerificationSendOtpUseCase {
    return AadhaarVerificationSendOtpUseCase(aadhaarVerificationRepository)
}

fun createAadhaarVerificationOtpValidationUseCase(aadhaarVerificationRepository: AadhaarVerificationRepository): AadhaarVerificationOtpValidationUseCase {
    return AadhaarVerificationOtpValidationUseCase(aadhaarVerificationRepository)
}

fun createValidateAadhaarUseCase(aadhaarVerificationRepository: AadhaarVerificationRepository): ValidateAadhaarUseCase {
    return ValidateAadhaarUseCase(aadhaarVerificationRepository)
}

fun createGetAadhaarVerificationDetailsUseCase(aadhaarVerificationRepository: AadhaarVerificationRepository): GetAadhaarVerificationDetailsUseCase {
    return GetAadhaarVerificationDetailsUseCase(aadhaarVerificationRepository)
}

fun createPanVerificationDetailsRepository(
    apiServiceAssociateForPanAndBank: ApiService,
    apiServiceNormal: ApiService
): PanVerificationRepository {
    return PanVerificationRepositoryImp(apiServiceAssociateForPanAndBank, apiServiceNormal)
}

fun createPanVerificationDetailsUseCase(panVerificationRepository: PanVerificationRepository): PanVerificationDetailsUseCase {
    return PanVerificationDetailsUseCase(panVerificationRepository)
}

fun createPanVerificationUseCase(panVerificationRepository: PanVerificationRepository): PanVerificationUseCase {
    return PanVerificationUseCase(panVerificationRepository)
}

fun createBankAccountVerificationRepository(
    apiServiceAssociateForPanAndBank: ApiService,
    apiServiceNormal: ApiService
): BankAccountVerificationRepository {
    return BankAccountVerificationRepositoryImp(apiServiceAssociateForPanAndBank, apiServiceNormal)
}

fun createBanKAccountVerificationDetailsUseCase(repository: BankAccountVerificationRepository): BankAccountVerificationDetailsUseCase {
    return BankAccountVerificationDetailsUseCase(repository)
}

fun createBankAccountVerificationUseCase(bankAccountVerificationRepository: BankAccountVerificationRepository): BankAccountVerificationUseCase {
    return BankAccountVerificationUseCase(bankAccountVerificationRepository)
}

fun createCibilScoreRepository(apiService: ApiService): CibilScoreRepository {
    return CibilScoreRepositoryImp(apiService)
}

fun createGetCibilScoreUseCase(cibilScoreRepository: CibilScoreRepository): GetCibilScoreUseCase {
    return GetCibilScoreUseCase(cibilScoreRepository)
}

fun createCandidateRepository(basicCandidate: ApiService): CandidateRepository {
    return CandidateRepositoryImp(basicCandidate)
}

fun createCandidateAttendanceUseCase(candidateRepository: CandidateRepository): CandidateAttendanceUseCase {
    return CandidateAttendanceUseCase(candidateRepository)
}

fun createOnboardingDashboardRepository(apiService: ApiService): OnboardingDashboardRepository {
    return OnboardingDashboardRepositoryImp(apiService)
}

fun createOnboardingDashboardUseCase(onboardingDashboardRepository: OnboardingDashboardRepository): OnboardingDashboardUseCase {
    return OnboardingDashboardUseCase(onboardingDashboardRepository)
}

fun createViewPayoutRepository(apiService: ApiService): ViewPayoutRepository {
    return ViewPayoutRepositoryImp(apiService)
}

fun createViewPayoutUseCase(viewPayoutRepository: ViewPayoutRepository): ViewPayoutUseCase {
    return ViewPayoutUseCase(viewPayoutRepository)
}

fun createRefineRepository(apiService: ApiService): RefineRepository {
    return RefineRepositoryImp(apiService)
}

fun createRefineUseCase(refineRepository: RefineRepository): RefineUseCase {
    return RefineUseCase(refineRepository)
}

fun createIncomeTaxDeclarationRepository(apiService: ApiService): IncomeTaxRepository {
    return IncomeTaxRepositoryImp(apiService)
}

fun createIncomeTaxDeclarationUseCase(incomeTaxRepository: IncomeTaxRepository): IncomeTaxDeclarationUseCase {
    return IncomeTaxDeclarationUseCase(incomeTaxRepository)
}


fun createGeoTrackingSummaryListRepository(apiService: ApiService): GeoTrackingRepository {
    return GeoTrackingRepositoryImp(apiService)
}

fun createGeoTrackingSummaryUseCase(repository: GeoTrackingRepository): GeoTrackingUseCase {
    return GeoTrackingUseCase(repository)
}

fun createGeoTrackingUseCase(repository: GeoTrackingRepository): GeoTrackingDetailsUseCase {
    return GeoTrackingDetailsUseCase(repository)
}

fun createCustomerIdCardRepository(apiService: ApiService): CustomerIdCardRepository {
    return CustomerIdCardUseRepositoryImp(apiService)
}

fun createCustomerIdCardUseCase(repository: CustomerIdCardRepository): CustomerIdCardUseCase {
    return CustomerIdCardUseCase(repository)

}

fun createExitQuestionnaireRepository(apiService: ApiService): ExitQuestionnaireRepository {
    return ExitQuestionnaireRepositoryImp(apiService)
}

fun createExitQuestionnaireUseCase(repository: ExitQuestionnaireRepository): ExitQuestionnaireUseCase {
    return ExitQuestionnaireUseCase(repository)
}


fun createRewardsRepository(apiService: ApiService): RewardsRepository {
    return RewardsRepositoryImp(apiService)
}

fun createRewardsUseCase(repository: RewardsRepository): RewardsUseCase {

    return RewardsUseCase(repository)
}