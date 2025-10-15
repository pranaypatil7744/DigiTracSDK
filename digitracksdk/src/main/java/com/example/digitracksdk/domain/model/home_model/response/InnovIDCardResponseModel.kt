package com.example.digitracksdk.domain.model.home_model.response

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Keep
data class InnovIDCardResponseModel(
    @SerializedName("NewESICNo")
    val newESICNo: String? = "",
    @SerializedName("BloodGroup")
    val bloodGroup: String? = "",
    @SerializedName("Email")
    val email: String? = "",
    @SerializedName("Message")
    val message: String? = "",
    @SerializedName("WorkingLocation")
    val workingLocation: String? = "",
    @SerializedName("latitude")
    val latitude: String? = "",
    @SerializedName("StateName")
    val stateName: String? = "",
    @SerializedName("AssociateMiddleName")
    val associateMiddleName: String? = "",
    @SerializedName("Gender")
    val gender: String? = "",
    @SerializedName("LocationName")
    val locationName: String? = "",
    @SerializedName("ClientName")
    val clientName: String? = "",
    @SerializedName("CandidateID")
    val candidateID: String? = "",
    @SerializedName("InnovID")
    var innovID: String? = "",
    @SerializedName("DOB")
    var dob: String? = "",
    @SerializedName("Picture")
    var picture: String? = "",
    @SerializedName("ProfilePercentage")
    var profilePercentage: String? = "",
    @SerializedName("AttendanceFromAnyWhereValue")
    var attendanceFromAnyWhereValue: String? = "",
    @SerializedName("LogYourVisit")
    var logYourVisit: String? = "",
    @SerializedName("dateofJoining")
    var dateofJoining: String? = "",
    @SerializedName("AssociateFullName")
    val associateFullName: String? = "",
    @SerializedName("longitude")
    val longitude: String? = "",
    @SerializedName("Status")
    val status: String? = "",
    @SerializedName("Designation")
    val designation: String? = "",
    @SerializedName("NewPFNo")
    val newPFNo: String? = "",
    @SerializedName("Address2")
    val address2: String? = "",
    @SerializedName("Address3")
    val address3: String? = "",
    @SerializedName("PermanentAddress1")
    val permanentAddress1: String? = "",
    @SerializedName("Address1")
    val address1: String? = "",
    @SerializedName("AssociateID")
    val associateID: String? = "",
    @SerializedName("AssociateLastName")
    val associateLastName: String? = "",
    @SerializedName("CityName")
    val cityName: String? = "",
    @SerializedName("AdharCard")
    val adharCard: String? = "",
    @SerializedName("Hobbies")
    val hobbies: String? = "",
    @SerializedName("NewUANNo")
    val newUANNo: String? = "",
    @SerializedName("PermanentAddress2")
    val permanentAddress2: String? = "",
    @SerializedName("PermanentAddress3")
    val permanentAddress3: String? = "",
    val PermanentStateName: String? = "",
    val PermanentCityName: String? = "",
    val PermanentAddressPIN: String? = "",
    @SerializedName("AssociateFirstName")
    val associateFirstName: String? = "",
    @SerializedName("MaritalStatus")
    val maritalStatus: String? = "",
    @SerializedName("BankAccountName")
    val bankAccountName: String? = "",
    @SerializedName("GNETAssociateID")
    val gNETAssociateID: String? = "",
    @SerializedName("PfUnNo")
    val pfUnNo: String? = "",
    @SerializedName("Pincode")
    val pincode: String? = "",
    @SerializedName("EmergencyNumber")
    val emergencyNumber: String? = "",
    var mobile:String? = null
):Serializable
