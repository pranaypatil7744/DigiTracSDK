package com.example.digitracksdk.di

import com.example.digitracksdk.presentation.attendance.AttendanceViewModel
import com.example.digitracksdk.presentation.attendance.CandidateViewModel
import com.example.digitracksdk.presentation.attendance.attendance_regularization.AttendanceRegularizationViewModel
import com.example.digitracksdk.presentation.attendance.mileage_tracking.MileageTrackingViewModel
import com.example.digitracksdk.presentation.home.CustomerIdCardViewModel
import com.example.digitracksdk.presentation.home.IncomeTaxViewModel
import com.example.digitracksdk.presentation.home.RefineViewModel
import com.example.digitracksdk.presentation.home.client_policy.ClientPoliciesViewModel
import com.example.digitracksdk.presentation.home.exit_questionnaire.ExitQuestionnaireViewModel
import com.example.digitracksdk.presentation.home.geo_tracking.GeoTrackingSummaryViewModel
import com.example.digitracksdk.presentation.home.help_and_support.HelpAndSupportViewModel
import com.example.digitracksdk.presentation.home.home_fragment.HomeBannerViewModel
import com.example.digitracksdk.presentation.home.home_fragment.HomeDashboardViewModel
import com.example.digitracksdk.presentation.home.innov_id_card.InnovIDCardViewModel
import com.example.digitracksdk.presentation.home.jobs_and_refer_fds.JobsAndReferFriendViewModel
import com.example.digitracksdk.presentation.home.jobs_and_refer_fds.refer_a_friend.ReferFriendViewModel
import com.example.digitracksdk.presentation.home.notification.NotificationViewModel
import com.example.digitracksdk.presentation.home.reimbursements.ReimbursementViewModel
import com.example.digitracksdk.presentation.home.rewards.RewardsViewModel
import com.example.digitracksdk.presentation.home.training.TrainingViewModel
import com.example.digitracksdk.presentation.home.view_payout.view_model.ViewPayoutViewModel
import com.example.digitracksdk.presentation.leaves.LeavesViewModel
import com.example.digitracksdk.presentation.login.login_fragment.LoginViewModel
import com.example.digitracksdk.presentation.login.verify_otp_fragment.VerifyLoginOtpViewModel
import com.example.digitracksdk.presentation.my_letters.candidate_loi.CandidateLoiViewModel
import com.example.digitracksdk.presentation.my_letters.offer_letter.OfferLetterViewModel
import com.example.digitracksdk.presentation.my_letters.other_letter.OtherLetterViewModel
import com.example.digitracksdk.presentation.my_profile.ProfileViewModel
import com.example.digitracksdk.presentation.onboarding.OnboardingDashboardViewModel
import com.example.digitracksdk.presentation.onboarding.aadhaar_verification_DigiLocker.AadhaarDigiLockerViewModel
import com.example.digitracksdk.presentation.onboarding.aadhar_verification.AadhaarVerificationViewModel
import com.example.digitracksdk.presentation.onboarding.bank_account_verification.BankAccountVerificationViewModel
import com.example.digitracksdk.presentation.onboarding.bank_details.BankDetailsViewModel
import com.example.digitracksdk.presentation.onboarding.candidate_details.PaperlessViewCandidateDetailsViewModel
import com.example.digitracksdk.presentation.onboarding.cibil_score.CibilScoreViewModel
import com.example.digitracksdk.presentation.onboarding.document.DocumentsDetailsViewModel
import com.example.digitracksdk.presentation.onboarding.educational_details.PaperlessViewEducationDetailsViewModel
import com.example.digitracksdk.presentation.onboarding.epf_details.PaperlessViewEpfViewModel
import com.example.digitracksdk.presentation.onboarding.esic_details.PaperlessViewEsicViewModel
import com.example.digitracksdk.presentation.onboarding.family_details_screen.PaperlessViewFamilyDetailsViewModel
import com.example.digitracksdk.presentation.onboarding.kyc_screen.PaperlessKycViewModel
import com.example.digitracksdk.presentation.onboarding.pan_verification.PanVerificationViewModel
import com.example.digitracksdk.presentation.onboarding.pf_uan.PaperlessPFUANViewModel
import com.example.digitracksdk.presentation.onboarding.reference_details_screen.PaperlessViewCandidateReferenceDetailsViewModel
import com.example.digitracksdk.presentation.onboarding.signature_screen.PaperlessViewGetSignatureViewModel
import com.example.digitracksdk.presentation.onboarding.work_experience.PaperlessViewWorkExpDetailsViewModel
import com.example.digitracksdk.presentation.payslip.PaySlipViewModel
import com.example.digitracksdk.presentation.pf_esic_insurance.PfEsicInsuranceViewModel
import com.example.digitracksdk.presentation.resignation.add_resignation.AddResignationViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module


val AppModule = module {
    viewModel { LoginViewModel(get(), get(), get()) }
    viewModel { HomeBannerViewModel(get()) }
    viewModel { HomeDashboardViewModel(get(), get(), get(), get()) }
    viewModel { VerifyLoginOtpViewModel(get()) }
    viewModel { InnovIDCardViewModel(get(), get()) }
    viewModel { RewardsViewModel(get()) }
    viewModel { JobsAndReferFriendViewModel(get(), get()) }
    viewModel { PfEsicInsuranceViewModel(get(), get(), get()) }
    viewModel { HelpAndSupportViewModel(get(), get(), get(), get(), get()) }
    viewModel { AddResignationViewModel(get(), get(), get(), get(), get(), get()) }
    viewModel { ReferFriendViewModel(get(), get(), get()) }
    viewModel { TrainingViewModel(get(), get()) }
    viewModel { ClientPoliciesViewModel(get(), get(), get(), get(), get()) }
    viewModel { LeavesViewModel(get(), get(), get(), get(), get(), get(), get()) }
    viewModel {
        ReimbursementViewModel(
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(), get(), get(), get(), get(), get(), get(), get(), get(), get()
        )
    }
    viewModel { PaySlipViewModel(get(), get(), get(), get()) }
    viewModel { CandidateLoiViewModel(get(), get()) }
    viewModel { OtherLetterViewModel(get(), get(), get(), get()) }
    viewModel { OfferLetterViewModel(get(), get(), get()) }
    viewModel { ProfileViewModel(get(), get(), get(), get(), get(), get(), get()) }
    viewModel { AttendanceRegularizationViewModel(get(), get(), get(), get()) }
    viewModel { MileageTrackingViewModel(get(), get(), get(), get(), get()) }
    viewModel { ExitQuestionnaireViewModel(get()) }
    viewModel {
        AttendanceViewModel(
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get()
        )
    }

    // Onboarding
    viewModel { OnboardingDashboardViewModel(get()) }
    viewModel { PaperlessViewEpfViewModel(get(), get()) }
    viewModel { PaperlessViewEsicViewModel(get(), get()) }
    viewModel { PaperlessViewFamilyDetailsViewModel(get(), get()) }
    viewModel { PaperlessViewEducationDetailsViewModel(get(), get(), get(), get()) }
    viewModel { PaperlessViewWorkExpDetailsViewModel(get(), get()) }
    viewModel { PaperlessViewCandidateDetailsViewModel(get(), get()) }
    viewModel { BankDetailsViewModel(get(), get(), get(), get()) }
    viewModel { PaperlessViewCandidateReferenceDetailsViewModel(get(), get(), get()) }
    viewModel { PaperlessViewGetSignatureViewModel(get(), get()) }
    viewModel { DocumentsDetailsViewModel(get(), get(), get(), get(), get()) }

    viewModel { NotificationViewModel(get(), get()) }
    viewModel { PaperlessKycViewModel(get(), get(), get()) }
    viewModel { PaperlessPFUANViewModel(get(), get()) }

    viewModel { AadhaarVerificationViewModel(get(), get(), get(), get()) }
    viewModel { AadhaarDigiLockerViewModel(get(), get(), get()) }

    viewModel { PanVerificationViewModel(get(), get()) }
    viewModel { BankAccountVerificationViewModel(get(), get()) }
    viewModel { CibilScoreViewModel(get()) }

    viewModel { ViewPayoutViewModel(get()) }
    viewModel { RefineViewModel(get()) }
    viewModel { IncomeTaxViewModel(get()) }

    viewModel { CustomerIdCardViewModel(get()) }

    viewModel { GeoTrackingSummaryViewModel(get(), get()) }
    viewModel { CandidateViewModel(get()) }
    // insert

    single { createHomeBannerUseCase(get()) }
    single { createHomeBannerRepository(get(named("normalService"))) }

    single { createLoginRepository(get(named("normalService")), get(named("digiOneService"))) }
    single { createLoginUseCase(get()) }
    single { createFirebaseTokenUpdateUseCase(get()) }
    single { createCheckAppVersionUseCase(get()) }

    single { createVerifyLoginOtpUseCase(get()) }
    single { createVerifyLoginOtpRepository(get(named("normalService"))) }

    single { createHomeDashboardRepository(get(named("normalService"))) }
    single { createCheckBirthdayUseCase(get()) }
    single { createHomeHomeDashboardUseCase(get()) }
    single { createInductionTrainingUseCase(get()) }
    single { createSurveyLinkUseCase(get()) }

    single { createInnovIDCardUseCase(get()) }
    single { createQrCodeUseCase(get()) }
    single { createInnovIDCardRepository(get(named("normalService"))) }

    single { createPfEsicInsuranceRepository(get(named("normalService"))) }
    single { createEsicMedicalCardUseCase(get()) }
    single { createEsicCardUseCase(get()) }
    single { createMedicalCardUseCase(get()) }
    single { createAssociateIssueUseCase(get()) }


    single { createJobReferralRepository(get(named("normalService"))) }
    single { createJobReferralUseCase(get()) }
    single { createReferredUsersUseCase(get()) }



    single { createHelpAndSupportRepository(get(named("normalService"))) }
    single { createIssueDetailsUseCase(get()) }
    single { createHelpAndSupportListUseCase(get()) }
    single { createIssueCategoryUseCase(get()) }
    single { createIssueSubCategoryUseCase(get()) }

    single { createResignationRepository(get(named("normalService"))) }
    single { createResignationCategoryUseCase(get()) }
    single { createResignationListUseCase(get()) }
    single { createResignationUseCase(get()) }
    single { createResignationNoticePeriodUseCase(get()) }
    single { createRevokeResignationUseCase(get()) }
    single { createResignationReasonUseCase(get()) }

    single { createReferralFriendRepository(get(named("normalService"))) }
    single { createBranchDetailsListUseCase(get()) }
    single { createSkillListUseCase(get()) }
    single { createReferralFriendUseCase(get()) }

    single { createTrainingRepository(get(named("normalService"))) }
    single { createTrainingUseCase(get()) }
    single { createViewTrainingDocumentUseCase(get()) }

    single { createClientPoliciesRepository(get(named("normalService"))) }
    single { createClientPoliciesUseCase(get()) }
    single { createInsertAcknowledgeUseCase(get()) }
    single { createViewPolicyUseCase(get()) }
    single { createViewAckPolicyUseCase(get()) }
    single { createPolicyAcknowledgementUseCase(get()) }


    single { createLeavesRepository(get(named("normalService"))) }
    single { createHolidaysListUseCase(get()) }
    single { createBalanceLeaveStatusUseCase(get()) }
    single { createLeaveStatusSummaryUseCase(get()) }
    single { createApplyLeavesUseCase(get()) }
    single { createLeavesBalanceUseCase(get()) }
    single { createLeavesRequestViewUseCase(get()) }
    single { createLeavesTypeUseCase(get()) }

    single { createReimbursementRepository(get(named("normalService"))) }
    single { createReimbursementListUseCase(get()) }
    single { createRejectedEndKmUseCase(get()) }
    single { createSaveReimbursementPreApprovalUseCase(get()) }
    single { createCheckReimbursementLimitUseCase(get()) }
    single { createRCheckReimbursementLimitV1UseCase(get()) }
    single { createRejectedReimbursementValidationUseCase(get()) }
    single { createUpdateReimbursementUseCase(get()) }
    single { createUpdateReimbursementStatusUseCase(get()) }
    single { createUpdateReimbursementDetailsUseCase(get()) }
    single { createPendingReimbursementListUseCase(get()) }
    single { createUploadReimbursementBillUseCase(get()) }
    single { createReimbursementDetailsUseCase(get()) }
    single { createReimbursementBillUseCase(get()) }
    single { createInsertReimbursementUseCase(get()) }
    single { createReimbursementCategoryUseCase(get()) }
    single { createReimbursementEndKmUseCase(get()) }
    single { createReimbursementModeOfTravelUseCase(get()) }
    single { createReimbursementSubCategoryUseCase(get()) }
    single { createReimbursementValidationUseCase(get()) }
    single { createGetReimbursementListForVoucherUseCase(get()) }
    single { createDeleteNewReimbursementListItemUseCase(get()) }
    single { createGenerateNewReimbursementVoucherUseCase(get()) }
    single { createInsertNewReimbursementUseCase(get()) }
    single { createNewReimbursementPendingListUseCase(get()) }
    single { createNewReimbursementApprovedListUseCase(get()) }
    single { createNewReimbursementRejectedListUseCase(get()) }
    single { createGetMonthDetailsUseCase(get()) }
    single { createGetYearDetailsUseCase(get()) }

    single { createPaySlipRepository(get(named("normalService")), get(named("normalService"))) }
    single { createPaySlipMonthYearUseCase(get()) }
    single { createDownloadPaySlipUseCase(get()) }
    single { createDownloadPaySlipNewUseCase(get()) }
    single { createSalaryReleaseStatusUseCase(get()) }

    single { createMyLettersRepository(get(named("normalService"))) }
    single { createCandidateLoiListUseCase(get()) }
    single { createOtherLettersUseCase(get()) }
    single { createOfferLetterAcceptRejectUseCase(get()) }
    single { createCandidateOfferLettersListUseCase(get()) }
    single { createDownloadCandidateOfferLetterUseCase(get()) }
    single { createViewCandidateLoiUseCase(get()) }
    single { createGetForm16UseCase(get()) }
    single { createGetFinancialYearListUseCase(get()) }
    single { createGetIncrementLettersUseCase(get()) }

    single { createProfileRepository(get(named("normalService"))) }
    single { createStateListUseCase(get()) }
    single { createCityListUseCase(get()) }
    single { createUpdateProfileUseCase(get()) }
    single { createInsertBasicDetailsUseCase(get()) }
    single { createCheckOtpUseCase(get()) }
    single { createValidCandidateUseCase(get()) }
    single { createNewInnovIdCreationUseCase(get()) }


    single {
        createAttendanceRegularizationRepository(
            get(named("normalService")),
            get(named("normalService"))
        )
    }
    single { createAttendanceRegularizationInsertUseCase(get()) }


    single { createInsertAttendanceRegularizationUseCase(get()) }
    single { createAttendanceRegularizationTypeUseCase(get()) }
    single { createAttendanceRegularizationListUseCase(get()) }

    single { createMileageTrackingRepository(get(named("normalService"))) }
    single { createInsertMileageRegularizationUseCase(get()) }
    single { createMileageRegularizationListUseCase(get()) }
    single { createInsertMileageTrackingUseCase(get()) }
    single { createMileageTrackingFlagUseCase(get()) }
    single { createMileageTrackingListUseCase(get()) }


    // onboarding

    single { createPaperlessViewEpfUseCase(get()) }
    single { createPaperlessViewEpfRepository(get(named("normalService"))) }
    single { createPaperlessViewEsicUseCase(get()) }
    single { createPaperlessViewEsicRepository(get(named("normalService"))) }

    single { createPaperlessViewFamilyDetailsRepository(get(named("normalService"))) }
    single { createPaperlessViewFamilyDetailsUseCase(get()) }
    single { createPaperlessInsertFamilyUseCase(get()) }

    single { createPaperlessPFESICRepository(get(named("normalService"))) }
    single { createPaperlessInsertPFESICUseCase(get()) }
    single { createPaperlessGetPFESICUseCase(get()) }

    single { createPaperlessViewEducationDetailsRepository(get(named("normalService"))) }
    single { createPaperlessViewEducationDetailsUseCase(get()) }
    single { createPaperlessGetEducationCategoryUseCase(get()) }
    single { createPOBInsertEducationInfoUseCase(get()) }
    single { createEducationalStreamUseCase(get()) }

    single { createPaperlessViewWorkExpDetailsUseCase(get()) }
    single { createInsertWorkExpUseCase(get()) }

    single { createPaperlessViewWorkExpDetailsRepository(get(named("normalService"))) }


    single { createPaperlessViewCandidateDetailsRepository(get(named("normalService"))) }
    single { createPaperlessViewCandidateDetailsUseCase(get()) }
    single { createPaperlessUpdateCandidateBasicDetailsUseCase(get()) }

    single { createPaperlessViewBankDetailsRepository(get(named("normalService"))) }
    single { createPaperlessViewBankDetailsUseCase(get()) }
    single { createChequeValidationUseCase(get()) }
    single { createUpdateBankDetailsUseCase(get()) }
    single { createGetBankListUseCase(get()) }

    single { createPaperlessViewCandidateReferenceDetailsUseCase(get()) }
    single { createPaperlessViewCandidateReferenceDetailsRepository(get(named("normalService"))) }
    single { createPaperlessViewGetSignatureDetailsUseCase(get()) }
    single { createInsertSignatureUseCase(get()) }

    single { createPaperlessViewGetSignatureDetailsRepository(get(named("normalService"))) }
    single { createPaperlessInsertEpfUseCase(get()) }

    single { createPaperlessInsertEpfRepository(get(named("normalService"))) }
    single { createPaperlessInsertEsicUseCase(get()) }

    single { createPaperlessInsertEsicRepository(get(named("normalService"))) }

    single { createPaperlessInsertCandidateReferenceDetailsUseCase(get()) }

    single { createPaperlessInsertCandidateReferenceDetailsRepository(get(named("normalService"))) }
    single { createPaperlessGetReferenceCategoryUseCase(get()) }

    single { createKYCDocsRepository(get(named("normalService"))) }
    single { createPaperlessDTGetKYCDocsUseCase(get()) }
    single { createKycDocumentPendingListUseCase(get()) }

    single { createDocumentsRepository(get(named("normalService"))) }
    single { createViewDocumentsListUseCase(get()) }
    single { createPendingDocumentListUseCase(get()) }
    single { createInsertPendingDocumentsUseCase(get()) }
    single { createUploadedDocumentsListUseCase(get()) }
    single { createGetDocumentUseCase(get()) }

    single { createAttendanceRepository(get(named("normalService"))) }
    single { createAttendanceGoeFancingUseCase(get()) }
    single { createUpdateAttendanceStatusUseCase(get()) }
    single { createCreateLogVisitTokenUseCase(get()) }
    single { createValidateLogVisitTokenUseCase(get()) }
    single { createDashboardAttendanceStatusUseCase(get()) }
    single { createGetAttendanceTimeSheetUseCase(get()) }
    single { createAttendanceStatusUseCase(get()) }
    single { createAttendanceFlagUseCase(get()) }
    single { createAttendanceMarkUseCase(get()) }
    single { createAttendanceValidationUseCase(get()) }
    single { createCurrentDayAtteStatusUseCase(get()) }
    single { createAttendanceZoneUseCase(get()) }
    single { createCreateAttendanceTokenUseCase(get()) }
    single { createCheckValidAttendanceTokenUseCase(get()) }
    single { createInsertAttendancePicUseCase(get()) }
    single { createLogYourVisitUseCase(get()) }
    single { createViewAttendanceUseCase(get()) }
    single { createAttendanceCycleUseCase(get()) }
    single { createLeaveHexCodeUseCase(get()) }

    single { createNotificationRepository(get(named("normalService"))) }
    single { createNotificationListUseCase(get()) }
    single { createNotificationDetailsUseCase(get()) }

    single {
        createAadhaarVerificationRepository(
            get(named("associateService")),
            get(named("normalService"))
        )
    }



    single {
        createAadhaarVerificationDigiLockerRepository(
            get(named("associateService")),
            get(named("normalService"))
        )
    }

    single { createAadhaarDigiLockerUseCase(get()) }
    single { createAadhaarSaveRequestIDUseCase(get()) }
    single { createGetAadhaarDataUseCase(get()) }

    single { createAadhaarVerificationSendOtpUseCase(get()) }
    single { createAadhaarVerificationOtpValidationUseCase(get()) }
    single { createValidateAadhaarUseCase(get()) }
    single { createGetAadhaarVerificationDetailsUseCase(get()) }

    single {
        createPanVerificationDetailsRepository(
            get(named("associateServiceForPanAndBank")),
            get(named("normalService"))
        )
    }
    single { createPanVerificationDetailsUseCase(get()) }
    single { createPanVerificationUseCase(get()) }


    single {
        createBankAccountVerificationRepository(
            get(named("associateServiceForPanAndBank")),
            get(named("normalService"))

        )
    }
    single { createBanKAccountVerificationDetailsUseCase(get()) }
    single { createBankAccountVerificationUseCase(get()) }

    single { createCibilScoreRepository(get(named("digiOneService"))) }
    single { createGetCibilScoreUseCase(get()) }

    single { createCandidateRepository(get(named("candidateAppService"))) }
    single { createCandidateAttendanceUseCase(get()) }

    single { createOnboardingDashboardRepository(get(named("normalService"))) }
    single { createOnboardingDashboardUseCase(get()) }

    single { createViewPayoutRepository(get(named("normalService"))) }
    single { createViewPayoutUseCase(get()) }

    single { createRefineRepository(get(named("digiOneService"))) }
    single { createRefineUseCase(get()) }

    single { createIncomeTaxDeclarationRepository(get(named("digiOneService"))) }
    single { createIncomeTaxDeclarationUseCase(get()) }

    single { createGeoTrackingSummaryListRepository(get(named("normalService"))) }
    single { createGeoTrackingSummaryUseCase(get()) }
    single { createGeoTrackingUseCase(get()) }


    single { createCustomerIdCardRepository(get(named("digiOneService"))) }
    single { createCustomerIdCardUseCase(get()) }


    single { createExitQuestionnaireRepository(get(named("normalService"))) }
    single { createExitQuestionnaireUseCase(get()) }

    single { createRewardsRepository(get(named("normalService"))) }
    single { createRewardsUseCase(get()) }

}


