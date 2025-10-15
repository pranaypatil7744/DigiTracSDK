package com.example.digitracksdk

import java.text.DecimalFormat

class Constant {

    class AwsKeys{
        companion object{
            const val aws_access_key = "YOUR_ACCESS_KEY_HERE"
            const val aws_secret_key = "YOUR_SECRET_KEY_HERE"
        }
    }


    class PreferenceKeys {
        companion object {
            const val FIRST_NAME = "FIRST_NAME"
            const val LAST_NAME = "LAST_NAME"
            const val FIREBASE_TOKEN = "FIREBASE_TOKEN"
            const val IN_OUT_FLAG = "IN_OUT_FLAG"
            const val INNOV_ID = "INNOV_ID"
            const val TOKEN_ID = "TOKEN_ID"
            const val IS_LOGIN = "IS_LOGIN"
            const val IS_BASF_CLIENT = "IS_BASF_CLIENT"
            const val EMPLOYEE_ID = "EMPLOYEE_ID"
            const val PROFILE_PIC = "PROFILE_PIC"
            const val IsAssociate = "IsAssociate"
            const val IsGPSAttendance = "IsGPSAttendance"
            const val IsViewPayouts = "IsViewPayouts"
            const val IsNewReimbursement = "IsNewReimbursement"
            const val IsPendingReimbursement = "IsPendingReimbursement"
            const val IsPendingReimbursementNew = "IsPendingReimbursementNew"
            const val IsIncomeTaxDeclaration = "IsIncomeTaxDeclaration"
            const val ShowAwakeTracking = "ShowAwakeTracking"
            const val IsChatBoardEnabled = "IsChatBoardEnabled"
            const val CloseBirthdayBanner = "CloseBirthdayBanner"
            const val CandidateID = "CandidateID"
            const val AssociateID = "AssociateID"
            const val MiddleName = "MiddleName"
            const val DOB = "DOB"
            const val MobileNo = "MobileNo"
            const val ChecksumValue = "ChecksumValue"
            const val ProfilePercentage = "ProfilePercentage"
            const val IsCheckSum = "IsCheckSum"
            const val AttendanceFromAnyWhere = "AttendanceFromAnyWhere"
            const val LogYourVisit = "LogYourVisit"
            const val RegID = "RegID"
            const val GnetAssociateID = "GnetAssociateID"
            const val Isjoiningpending = "Isjoiningpending"
            const val DateofJoining = "DateofJoining"
            const val EmergencyContactName = "EmergencyContactName"
            const val EmergencyContactNo = "EmergencyContactNo"
            const val ActualDOJ = "ActualDOJ"
            const val IsDigiTracResignation = "IsDigiTracResignation"
            const val IsRewards = "IsRewards"
            const val IsDigiTracReimbursement = "IsDigiTracReimbursement"
            const val IsAttendanceCamera = "IsAttendanceCamera"
            const val ClientID = "ClientID"
            const val NotificationCount = "NotificationCount"
            const val onboardingStatus = "onboardingStatus"
            const val Letters = "Letters"
            const val IsCustomeridCard = "IsCustomeridCard"
            const val IsInnovIdCard = "IsInnovIdCard"
            const val IsGeoTracking = "IsGeoTracking"
            const val IsLeave = "IsLeave"
            const val IsTraining = "IsTraining"
            const val IsClientPolicies = "IsClientPolicies"
            const val IsRefyneApplicable = "IsRefyneApplicable"
            const val IsPaySlip = "IsPaySlip"
            const val IsAadharVerification = "IsAadharVerification"
            const val IsPANVerification = "IsPANVerification"
            const val IsCibilScore = "IsCibilScore"
            const val IsBankVerification = "IsBankVerification"
            const val IsDocumentDownLoad = "IsDocumentDownLoad"
            const val DailyMileageLimit = "DailyMileageLimit"
            const val DailyMileageFourWheelar = "DailyMileageFourWheelar"
            const val AllowForAttendance = "AllowForAttendance"
            const val LastWorkingDate = "LastWorkingDate"
            const val ReimbursementAllowDays = "ReimbursementAllowDays"
            const val FULL_NAME = "FULL_NAME"
            const val GeoTrackingID = "GeoTrackingID"
            const val NewResignation = "NewResignationDD"
            const val AttendanceCalendar = "AttendanceCalendar"
            const val QuickAttendance = "QuickAttendance"
            const val SurveyPopup ="SurveyPopup"
            const val AllowPopUpClosed ="AllowPopUpClosed"


        }
    }

    class Relation{
        companion object{
            const val Father ="Father"
            const val Mother ="Mother"
            const val Son ="Son"
            const val Wife ="Wife"
            const val Daughter ="Daughter"
            const val Husband ="Husband"
            const val Guardian ="Guardian"
        }
    }

    class PermissionRequestCodes {
        companion object {
            const val PERMISSION_REQUEST_ACCESS_FINE_LOCATION = 11000
            const val REQUEST_CODE_CHECK_SETTINGS = 1000
            const val STORAGE_PERMISSION_CODE = 100
            const val CAMERA_PERMISSION_CODE = 101
            const val CALL_PHONE_PERMISSION_CODE = 102
            const val NOTIFICATION_PERMISSION_CODE =103
            const val CHANNEL_ID = "104"
        }
    }

    class AskedPermission{
        companion object{
            const val IS_ASKED_CAMERA_PERMISSION = "IS_ASKED_CAMERA_PERMISSION"
            const val CAMERA_PERMISSION_COUNT = "CAMERA_PERMISSION_COUNT"
            const val IS_ASKED_STORAGE_PERMISSION = "IS_ASKED_STORAGE_PERMISSION"
            const val STORAGE_PERMISSION_COUNT = "STORAGE_PERMISSION_COUNT"
            const val IS_ASKED_LOCATION_PERMISSION = "IS_ASKED_LOCATION_PERMISSION"
            const val LOCATION_PERMISSION_COUNT = "LOCATION_PERMISSION_COUNT"
            const val IS_ASKED_CALL_PERMISSION = "IS_ASKED_CALL_PERMISSION"
            const val CALL_PERMISSION_COUNT = "CALL_PERMISSION_COUNT"
        }
    }

    class MonthList{
        companion object{
            const val JAN = "JAN"
            const val FEB = "FEB"
            const val MAR = "MAR"
            const val APR = "APR"
            const val MAY = "MAY"
            const val JUN = "JUN"
            const val JUL = "JUL"
            const val AUG = "AUG"
            const val SEP = "SEP"
            const val OCT = "OCT"
            const val NOV = "NOV"
            const val DEC = "DEC"
        }
    }

    class IntentExtras {
        companion object {
            const val EXTRA_LATITUDE = "latitude"
            const val EXTRA_LONGITUDE = "longitude"
            const val EXTRA_PICKUP_ADDRESS = "pickup_address"
            const val EXTRA_FILE_NAME = "EXTRA_FILE_NAME"
            const val EXTRA_FILE_PATH = "EXTRA_FILE_PATH"
            const val EXTRA_IMAGE_BIT_MAP = "EXTRA_IMAGE_BIT_MAP"
            const val GEO_TRACKING_ID = "GEO_TRACKING_ID"
            const val LOCATION_LIST="LOCATION_LIST"
            const val IS_SERVICE_ONGOING="IS_SERVICE_ONGOING"
        }
    }

    class ListenerConstants {
        companion object {
            const val RESIGNATION_CATEGORY_ID_ERROR = "RESIGNATION_CATEGORY_ID_ERROR"
            const val LAST_WORKING_DATE_ERROR = "LAST_WORKING_DATE_ERROR"
            const val REASON_ERROR = "REASON_ERROR"
            const val IMAGE_ERROR = "IMAGE_ERROR"
            const val NAME_ERROR = "NAME_ERROR"
            const val EMERGENCY_CON_NAME_ERROR = "EMERGENCY_CON_NAME_ERROR"
            const val EMERGENCY_CON_NUMBER_ERROR = "EMERGENCY_CON_NUMBER_ERROR"
            const val MIDDLE_NAME_ERROR = "MIDDLE_NAME_ERROR"
            const val LAST_NAME_ERROR = "LAST_NAME_ERROR"
            const val AADHAR_NUMBER_ERROR = "AADHAR_NUMBER_ERROR"

            const val TERM_CON_ERROR = "TERM_CON_ERROR"
            const val FATHER_NAME_ERROR = "FATHER_NAME_ERROR"
            const val MOBILE_ERROR = "MOBILE_ERROR"
            const val MARITAL_STATUS_ERROR = "MARITAL_STATUS_ERROR"
            const val DOB_ERROR = "DOB_ERROR"
            const val BLOOD_GROUP_ERROR = "BLOOD_GROUP_ERROR"
            const val GENDER_ERROR = "GENDER_ERROR"
            const val SKILL_ERROR = "SKILL_ERROR"
            const val ADDRESS_1_ERROR = "ADDRESS_1_ERROR"
            const val ADDRESS_2_ERROR = "ADDRESS_2_ERROR"
            const val ADDRESS_3_ERROR = "ADDRESS_3_ERROR"
            const val PIN_ERROR = "PIN_ERROR"
            const val PER_PIN_ERROR = "PER_PIN_ERROR"
            const val PER_ADDRESS_1_ERROR = "PER_ADDRESS_1_ERROR"
            const val PER_ADDRESS_2_ERROR = "PER_ADDRESS_2_ERROR"
            const val PER_ADDRESS_3_ERROR = "PER_ADDRESS_3_ERROR"
            const val STATE_ERROR = "STATE_ERROR"
            const val PER_STATE_ERROR = "PER_STATE_ERROR"
            const val CITY_ERROR = "CITY_ERROR"
            const val PER_CITY_ERROR = "PER_CITY_ERROR"
            const val LOCATION_ERROR = "LOCATION_ERROR"
            const val PHONE_ERROR = "PHONE_ERROR"
            const val BRANCH_ERROR = "BRANCH_ERROR"
            const val IS_FRESHER_ERROR = "IS_FRESHER_ERROR"
            const val TOTAL_YEAR_ERROR = "TOTAL_YEAR_ERROR"
            const val RELEVANT_YEAR_ERROR = "RELEVANT_YEAR_ERROR"
            const val RELEVANT_MONTH_ERROR = "RELEVANT_MONTH_ERROR"
            const val RELEVANT_EXP_ERROR = "RELEVANT_EXP_ERROR"
            const val TOTAL_MONTH_ERROR = "TOTAL_MONTH_ERROR"
            const val CURRENTLY_EMPLOYED_ERROR = "CURRENTLY_EMPLOYED_ERROR"
            const val COMPANY_NAME_ERROR = "COMPANY_NAME_ERROR"
            const val LEAVE_TYPE_ERROR = "LEAVE_TYPE_ERROR"
            const val EXPENSE_TYPE_ERROR = "EXPENSE_TYPE_ERROR"
            const val CLAIM_YEAR_TYPE_ERROR = "CLAIM_YEAR_TYPE_ERROR"
            const val CLAIM_MONTH_TYPE_ERROR = "CLAIM_MONTH_TYPE_ERROR"
            const val LEAVE_SUB_TYPE_ERROR = "LEAVE_SUB_TYPE_ERROR"
            const val FROM_DATE_ERROR = "FROM_DATE_ERROR"
            const val TO_DATE_ERROR = "TO_DATE_ERROR"
            const val BILL_DATE_ERROR = "BILL_DATE_ERROR"
            const val CLAIM_DATE_ERROR = "CLAIM_DATE_ERROR"
            const val BILL_NUMBER_ERROR = "BILL_NUMBER_ERROR"
            const val JOURNEY_FROM_ERROR = "JOURNEY_FROM_ERROR"
            const val JOURNEY_TO_ERROR = "JOURNEY_TO_ERROR"
            const val BASE_AMT_ERROR = "BASE_AMT_ERROR"
            const val TAX_AMT_ERROR = "TAX_AMT_ERROR"
            const val GROSS_AMT_ERROR = "GROSS_AMT_ERROR"
            const val START_KM_ERROR = "START_KM_ERROR"
            const val END_KM_ERROR = "END_KM_ERROR"
            const val TRAVEL_MODE_ERROR = "TRAVEL_MODE_ERROR"
            const val REMARK_ERROR = "REMARK_ERROR"
            const val IN_TIME_ERROR = "IN_TIME_ERROR"
            const val OUT_TIME_ERROR = "OUT_TIME_ERROR"
            const val REQUEST_TYPE_ERROR = "REQUEST_TYPE_ERROR"
            const val REFERENCE_TYPE_ERROR = "REFERENCE_TYPE_ERROR"
            const val OPEN_READING_ERROR ="OPEN_READING_ERROR"
            const val CLOSE_READING_ERROR ="CLOSE_READING_ERROR"
            const val EPF_ERROR = "EPF_ERROR"
            const val NOMINEE_ERROR = "NOMINEE_ERROR"
            const val IS_RESIDING_WITH_YOU_ERROR = "IS_RESIDING_WITH_YOU_ERROR"
            const val RELATIONSHIP_ERROR = "RELATIONSHIP_ERROR"
            const val EPF_SCHEME_ERROR = "EPF_SCHEME_ERROR"
            const val EPF_PROVIDED_FUND_ERROR = "EPF_PROVIDED_FUND_ERROR"
            const val UAN_NO_ERROR = "UAN_NO_ERROR"
            const val PF_MEMBER_ID_ERROR = "PF_MEMBER_ID_ERROR"
            const val JOINING_DATE_ERROR = "JOINING_DATE_ERROR"
            const val EXIT_DATE_ERROR = "EXIT_DATE_ERROR"
            const val LAST_CTC_ERROR = "LAST_CTC_ERROR"
            const val DESIGNATION_ERROR = "DESIGNATION_ERROR"
            // onboarding
            const val ESIC_NO_ERROR = "ESIC_NO_ERROR"
            const val ESIC_CHECK_ERROR = "ESIC_CHECK_ERROR"
            const val INSURANCE_NO_ERROR = "INSURANCE_NO_ERROR"
            const val EMP_CODE_ERROR = "EMP_CODE_ERROR"
            const val BRANCH_OFFICE_ERROR = "BRANCH_OFFICE_ERROR"
            const val DISPENSARY_NAME_ERROR = "DISPENSARY_NAME_ERROR"

            // insert UAN/PF

            const val PF_UAN_NO_ERROR = "PF_UAN_NO_ERROR"


            // insert Family

            const val FAMILY_NAME_ERROR = "FAMILY_NAME_ERROR"
            const val FAMILY_DOB_ERROR = "FAMILY_DOB_ERROR"
            const val FAMILY_OCCUPATION_ERROR = "FAMILY_OCCUPATION_ERROR"
            const val FAMILY_PF_ERROR = "FAMILY_PF_ERROR"
            const val FAMILY_ESIC_ERROR = "FAMILY_ESIC_ERROR"
            const val FAMILY_INSURANCE_ERROR = "FAMILY_INSURANCE_ERROR"
            const val FAMILY_GRATUITY_ERROR = "FAMILY_GRATUITY_ERROR"

            // insert reference Details
            const val REF_NO = "FAMILY_INSURANCE_ERROR"
            const val REF_EMAIL = "FAMILY_GRATUITY_ERROR"

            // insert Education Details
            const val EDUCATION_CATEGORY_NAME_ERROR = "EDUCATION_CATEGORY_ERROR"
            const val EDUCATIONAL_STREAM_NAME_ERROR = "EDUCATION_STREAM_NAME_ERROR"
            const val EDUCATIONAL_STREAM_ERROR = "EDUCATIONAL_ERROR"

            const val EDUCATION_PASSING_YEAR_ERROR = "EDUCATION_PASSING_YEAR_ERROR"
            const val EDUCATION_BOARD_NAME_ERROR = "EDUCATION_BOARD_NAME_ERROR"
            const val EDUCATION_SCHOOL_NAME_ERROR = "EDUCATION_SCHOOL_NAME_ERROR"
            const val EDUCATION_MARKS_OBTAINED_ERROR = "EDUCATION_MARKS_OBTAINED_ERROR"

            //bank
            const val PAN_CARD_ERROR = "PAN_CARD_ERROR"
            const val BANK_NAME_ERROR = "BANK_NAME_ERROR"
            const val ACCOUNT_NUMBER_ERROR = "ACCOUNT_NUMBER_ERROR"
            const val IFSC_ERROR = "IFSC_ERROR"
            const val CHEQUE_ERROR = "CHEQUE_ERROR"

            // Candidate details
            const val CANDIDATE_AADHAAR_ERROR = "CANDIDATE_AADHAAR_ERROR"
            const val CANDIDATE_EMERGENCY_CONTACT_NAME_ERROR = "CANDIDATE_EMERGENCY_CONTACT_NAME_ERROR"
            const val CANDIDATE_EMERGENCY_CONTACT_NO_ERROR = "CANDIDATE_EMERGENCY_CONTACT_NO_ERROR"
            const val CANDIDATE_EMAIL_ERROR = "CANDIDATE_EMAIL_ERROR"
            const val CANDIDATE_BLOOD_GROUP_ERROR = "CANDIDATE_BLOOD_GROUP_ERROR"
            const val CANDIDATE_MARITAL_STATUS_ERROR = "CANDIDATE_MARITAL_STATUS_ERROR"
            const val CANDIDATE_NO_OF_CHILDREN_ERROR = "CANDIDATE_NO_OF_CHILDREN_ERROR"
            const val CANDIDATE_HOUSE_NAME_ERROR = "CANDIDATE_HOUSE_NAME_ERROR"
            const val CANDIDATE_STREET_ERROR = "CANDIDATE_STREET_ERROR"
            const val CANDIDATE_LANDMARK_ERROR = "CANDIDATE_LANDMARK_ERROR"
            const val CANDIDATE_LOCATION_ERROR = "CANDIDATE_LOCATION_ERROR"
            const val CANDIDATE_PINCODE_ERROR = "CANDIDATE_PINCODE_ERROR"

            // Exit Questionnaire
            const val EXIT_QUESTIONNAIRE_1_ERROR = "EXIT_QUESTIONNAIRE_1_ERROR"
            const val EXIT_QUESTIONNAIRE_2_ERROR = "EXIT_QUESTIONNAIRE_2_ERROR"
            const val EXIT_QUESTIONNAIRE_3_ERROR = "EXIT_QUESTIONNAIRE_3_ERROR"
            const val EXIT_QUESTIONNAIRE_4_ERROR = "EXIT_QUESTIONNAIRE_4_ERROR"
            const val EXIT_QUESTIONNAIRE_5_ERROR = "EXIT_QUESTIONNAIRE_5_ERROR"
            const val  EXIT_REMARK_ERROR = "EXIT_REMARK_ERROR"


        }
    }
    companion object {
        const val CHANNEL_ID= "CHANNEL_ID"
        val decFormat = DecimalFormat("######.00")
        const val DATA = "DATA"
        const val success = "success"
        const val failed = "failed"
        const val UNKNOWN_ERROR_MESSAGE = "Unknown Error!"
        const val EMPTY = "Empty"
        const val SUCCESS = "Success"
        const val Closed = "Closed"
        const val Single = "Single"
        const val Married = "Married"
        const val Male = "Male"
        const val Female = "Female"
        const val Divorced = "Divorced"
        const val Widow = "Widow"
        const val alert = "alert"
        const val Yes = "Yes"
        const val No = "No"
        const val UPDATED = "Updated"
        const val IN = "IN"
        const val OUT = "OUT"
        const val Other = "Other"
        const val KYC = "KYC"
        const val LYV = "LYV"
        const val IN_TIME = "IN_TIME"
        const val ATTENDANCE_SWITCH_KEY = "ATTENDANCE_SWITCH_KEY"
        const val ATTENDANCE_DATE = "ATTENDANCE_DATE"
        const val Data_Saved_Succesfully = "Data Saved succesfully"
        const val SELECTED_LANGUAGE = "SELECTED_LANGUAGE"
        const val JOB_MODEL = "JOB_MODEL"
        const val BASIC_DETAILS_MODEL = "BASIC_DETAILS_MODEL"
        const val ATTENDANCE_MODEL = "ATTENDANCE_MODEL"
        const val WORK_EXP_MODEL = "WORK_EXP_MODEL"
        const val REFERRAL_MODEL = "REFERRAL_MODEL"
        const val SHARE_IMAGE = "SHARE_IMAGE"
        const val ITEM_NAME = "ITEM_NAME"
        const val SCREEN_NAME = "SCREEN_NAME"
        const val IS_CAMERA = "IS_CAMERA"
        const val IS_SELFIE_CAMERA = "IS_SELFIE_CAMERA"
        const val WEB_URL = "WEB_URL"
        const val INNOV_URL = "https://innov.in/"
        const val DIGI_ASSIST = "https://www.smatbot.com/smatbot/FirstMeridian?id=3182"
        const val IMAGE_PATH = "IMAGE_PATH"
        const val IS_PDF = "IS_PDF"
        const val IS_CURRENTLY_EMPLOYED = "IS_CURRENTLY_EMPLOYED"
        const val TOTAL_EXP_MONTH = "TOTAL_EXP_MONTH"
        const val RELEVANT_EXP_MONTH = "RELEVANT_EXP_MONTH"
        const val TOTAL_EXP_YEAR = "TOTAL_EXP_YEAR"
        const val RELEVANT_EXP_YEAR = "RELEVANT_EXP_YEAR"
        const val IS_VIEW = "IS_VIEW"
        const val IS_IMAGE = "IS_IMAGE"
        const val AUTHORITY = "com.innov.digitrac.fileprovider"
        const val CLIENT_REQUIREMENT_ID = "CLIENT_REQUIREMENT_ID"
        const val IS_CHECK_OUT = "IS_CHECK_OUT"
        const val JOB_PROFILE = "JOB_PROFILE"
        const val SET_CHECK_OUT = "SET_CHECK_OUT"
        const val IS_FOR_CHECK_OUT = "IS_FOR_CHECK_OUT"
        const val IS_FOR_EDIT = "IS_FOR_EDIT"
        const val IS_FIRST_TIME = "IS_FIRST_TIME"
        const val isFirstOpenCheckIn = "isFirstOpenCheckIn"
        const val isFirstOpenCheckInDate = "isFirstOpenCheckInDate"
        const val isFirstOpenCheckOut = "isFirstOpenCheckOut"
        const val isFirstOpenCheckOutDate = "isFirstOpenCheckOutDate"
        const val IS_FOR_EDIT_BILL = "IS_FOR_EDIT_BILL"
        const val IS_FROM_ADD = "IS_FROM_ADD"
        const val IS_FROM_DOC = "IS_FROM_DOC"
        const val IS_FROM_OFFER_LETTER = "IS_FROM_OFFER_LETTER"
        const val IS_FROM_LOG_YOUR_VISIT = "IS_FROM_LOG_YOUR_VISIT"
        const val IS_FROM_SIGN_UP = "IS_FROM_SIGN_UP"
        const val PROFILE_MODEL = "PROFILE_MODEL"
        const val IMEIStatus = "IMEIStatus"
        const val IMEI = "IMEI"
        const val Valid = "Valid"
        const val Invalid = "Invalid"
        const val Approved = "Approved"
        const val Accepted = "Accepted"
        const val Accept = "Accept"
        const val Pending = "Pending"
        const val Rejected = "Rejected"
        const val Reject = "Reject"
        const val THIRTY_SEC = 30000L
        const val IsAssociateID = "IsAssociateID"
        const val STORAGE_PERMISSION_CODE = 100
        const val SMS_READ_REQUEST_CODE = 200
        const val VIEW_ANIMATE_DURATION = 400L
        const val VIEW_ANIMATE_DELAY = 400L
        const val VIEW_TYPE = "VIEW_TYPE"
        const val DOB_FORMAT = "dd/mm/yyyy"
        const val SIMPLE_DATE_FORMAT ="dd-MMM-yyyy"
        const val IN_OUT_FLAG = "IN_OUT_FLAG"
        const val associateReimbursementId = "associateReimbursementId"
        const val CandidateDocumentMappingID = "CandidateDocumentMappingID"
        const val POLICY_ID = "POLICY_ID"
        const val POLICY_STATUS = "POLICY_STATUS"
    }

    class Languages{
        companion object{
            const val English = "en"
            const val Hindi = "hi"
            const val Marathi = "mr"
            const val Gujarati = "gu"
        }
    }

    enum class ViewType(val value: Int) {
        ADD(1),
        EDIT(2),
    }

    enum class Gender(val value: Int) {
        MALE(1),
        FEMALE(2)
    }

}