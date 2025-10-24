package com.example.digitracksdk.utils

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.widget.NumberPicker
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.multidex.BuildConfig
import com.google.android.material.textfield.TextInputEditText
import com.example.digitracksdk.R
import com.example.digitracksdk.databinding.*

class DialogUtils {
    companion object {
        private lateinit var attendanceCheckInDialog: Dialog
        private lateinit var markYourAttendanceDialog: Dialog
        private lateinit var completeYourProfileDialog: Dialog
        private lateinit var logOutDialog: Dialog
        private lateinit var reimbursementPreApprovalDialog: Dialog
        private lateinit var yearPickerDialog: Dialog
        private lateinit var signUpCompletedDialog: Dialog
        private lateinit var birthdayWishDialog: Dialog
        private lateinit var pfUanDialog: Dialog
        private lateinit var acknowledgeDialog: Dialog
        private lateinit var policyAcknowledgeDialog: Dialog
        private lateinit var permissionDialog: Dialog
        private lateinit var surveyDialog: Dialog

        fun showReimbursementPreApprovalDialog(
            context: Context,
            listener: DialogManager,
            approvedAmt: String
        ) {
            val binding =
                DialogReimbursementPreApprovalBinding.inflate(LayoutInflater.from(context))
            reimbursementPreApprovalDialog = Dialog(context)
            reimbursementPreApprovalDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            reimbursementPreApprovalDialog.setContentView(binding.root)
            reimbursementPreApprovalDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            reimbursementPreApprovalDialog.window?.attributes?.windowAnimations =
                R.style.DialogTheme
            reimbursementPreApprovalDialog.window?.setLayout(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
            )
            reimbursementPreApprovalDialog.setCancelable(false)
            reimbursementPreApprovalDialog.setCanceledOnTouchOutside(false)
            binding.apply {
                etApprovalAdditionalAmt.setText(approvedAmt)
                etApprovalDate.setOnClickListener {
                    listener.onChooseDateClick(etApprovalDate)
                }
                btnAttachment.setOnClickListener {
                    if (etApprovalDate.text.toString().trim().isEmpty()) {
                        Toast.makeText(
                            context,
                            context.getString(R.string.please_choose_approval_date),
                            Toast.LENGTH_LONG
                        ).show()
                    } else if (etApprovalName.text.toString().trim().isEmpty()) {
                        Toast.makeText(
                            context,
                            context.getString(R.string.please_enter_approver_name),
                            Toast.LENGTH_LONG
                        ).show()
                    } else {
                        listener.onAttachmentClick(
                            etApprovalDate.text.toString().trim(),
                            etApprovalName.text.toString().trim(),
                            etApprovalAdditionalAmt.text.toString()
                        )
                    }
                }
            }
            reimbursementPreApprovalDialog.show()
        }

        fun showYearPickerDialog(context: Context, listener: DialogManager) {
            val binding = DialogYearPickerBinding.inflate(LayoutInflater.from(context))
            yearPickerDialog = Dialog(context)
            yearPickerDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            yearPickerDialog.setContentView(binding.root)
            yearPickerDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            yearPickerDialog.window?.attributes?.windowAnimations = R.style.DialogTheme
            yearPickerDialog.window?.setLayout(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
            )
            yearPickerDialog.setCancelable(false)
            yearPickerDialog.setCanceledOnTouchOutside(false)
            val year = AppUtils.INSTANCE?.getCurrentYear()
            binding.apply {
                btnSubmit.setOnClickListener {
                    yearPickerDialog.dismiss()
                    listener.onSelectYear(numberPickerPassingYear.value.toString())
                }
                imgClose.setOnClickListener {
                    yearPickerDialog.dismiss()
                    listener.onSelectYear("")
                }
                numberPickerPassingYear.apply {
                    maxValue = year!! + 0
                    minValue = year - 50
                    wrapSelectorWheel = false
                    value = year
                    descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS
                    setOnValueChangedListener { numberPicker, old, new ->
                        numberPickerPassingYear.value = new
                    }
                }
            }
            yearPickerDialog.show()
        }

        fun showPermissionDialog(
            activity: Activity,
            msg: String,
            title: String,
            positiveBtn: String,
            negativeBtn: String,
            isFinish: Boolean = true,
            isOtherAction: Boolean = false,
            listener: DialogManager? = null
        ) {
            val binding = DialogPermissionBinding.inflate(LayoutInflater.from(activity))
            permissionDialog = Dialog(activity)
            permissionDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            permissionDialog.setContentView(binding.root)
            permissionDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            permissionDialog.window?.attributes?.windowAnimations = R.style.DialogTheme
            permissionDialog.window?.setLayout(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
            )
            permissionDialog.setCancelable(false)
            permissionDialog.setCanceledOnTouchOutside(false)

            binding.apply {
                tvDeny.setOnClickListener {
                    permissionDialog.dismiss()
                    if (isFinish) {
                        activity.finish()
                    } else {
                        permissionDialog.dismiss()
                    }
                }
                tvGoToSettings.setOnClickListener {
                    if (isOtherAction) {
                        permissionDialog.dismiss()
                        listener?.onContinueClick()
                    } else {
                        val intent = Intent()
                        intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                        val uri = Uri.fromParts(
                            "package",
                            BuildConfig.APPLICATION_ID, null
                        )
                        intent.data = uri
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        activity.startActivity(intent)
                        if (isFinish) {
                            activity.finish()
                        } else {
                            permissionDialog.dismiss()
                        }
                    }
                }
                tvMsg.text = msg
                tvAllowPermission.text = title
                tvDeny.text = negativeBtn
                tvGoToSettings.text = positiveBtn
            }
            permissionDialog.show()
        }

        fun showSignUpCompletedDialog(
            context: Context,
            listener: DialogManager,
            name: String,
            innovId: String
        ) {
            val binding = DialogSignUpCompletedBinding.inflate(LayoutInflater.from(context))
            signUpCompletedDialog = Dialog(context)
            signUpCompletedDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            signUpCompletedDialog.setContentView(binding.root)
            signUpCompletedDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            signUpCompletedDialog.window?.attributes?.windowAnimations = R.style.DialogTheme
            signUpCompletedDialog.window?.setLayout(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
            )
            signUpCompletedDialog.setCancelable(false)
            signUpCompletedDialog.setCanceledOnTouchOutside(false)

            binding.apply {
                btnOk.setOnClickListener {
                    listener.onOkClick()
                }
                tvWelcome.text =
                    context.getString(R.string.welcome) + " $name " + context.getString(R.string.to_innovsource)
                tvInnovID.text = innovId
            }
            signUpCompletedDialog.show()
        }

        fun showAttendanceCheckInDialog(
            context: Context,
            listener: DialogManager,
            title: String,
            img: Int
        ) {
            val binding = DialogAttendanceCheckInBinding.inflate(LayoutInflater.from(context))
            attendanceCheckInDialog = Dialog(context)
            attendanceCheckInDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            attendanceCheckInDialog.setContentView(binding.root)
            attendanceCheckInDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            attendanceCheckInDialog.window?.attributes?.windowAnimations = R.style.DialogTheme
            attendanceCheckInDialog.window?.setLayout(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
            )
            attendanceCheckInDialog.setCancelable(false)
            attendanceCheckInDialog.setCanceledOnTouchOutside(false)
            binding.tvSuccessfully.text = title
            binding.imgFinger.setImageResource(img)
            binding.btnOk.setOnClickListener {
                listener.onOkClick()
            }
            attendanceCheckInDialog.show()
        }

        fun showPfUanDialog(
            context: Context,
            name: String,
            uanNo: String
        ) {
            val binding = DialogPfUanBinding.inflate(LayoutInflater.from(context))
            pfUanDialog = Dialog(context)
            pfUanDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            pfUanDialog.setContentView(binding.root)
            pfUanDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            pfUanDialog.window?.attributes?.windowAnimations = R.style.DialogTheme
            pfUanDialog.window?.setLayout(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
            )
            pfUanDialog.setCancelable(false)
            pfUanDialog.setCanceledOnTouchOutside(false)
            binding.tvPfUanNoValue.text = uanNo
            binding.tvNameValue.text = name
            binding.imgClose.setOnClickListener {
                pfUanDialog.dismiss()
            }
            pfUanDialog.show()
        }

        fun showMarkAttendanceDialog(
            context: Context,
            listener: DialogManager,
            msg: String,
            btnText: String,
            img: Int
        ) {
            val binding = DialogMarkAttendanceBinding.inflate(LayoutInflater.from(context))
            markYourAttendanceDialog = Dialog(context)
            markYourAttendanceDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            markYourAttendanceDialog.setContentView(binding.root)
            markYourAttendanceDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            markYourAttendanceDialog.window?.attributes?.windowAnimations = R.style.DialogTheme
            markYourAttendanceDialog.window?.setLayout(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
            )
            binding.apply {
                tvYourAtt.text = msg
                btnMarkAttendance.text = btnText
                btnMarkAttendance.setOnClickListener {
                    listener.onMarkAttendanceClick()
                }
                imgFinger.setImageResource(img)
                btnClose.setOnClickListener {
                    markYourAttendanceDialog.dismiss()
                }
            }
            markYourAttendanceDialog.setCancelable(false)
            markYourAttendanceDialog.setCanceledOnTouchOutside(false)
            markYourAttendanceDialog.show()
        }


        fun showSurveyDialog(context: Context, listener: DialogManager, closedIcon: Int?) {
            val binding = DialogComplateYourProfileBinding.inflate(LayoutInflater.from(context))
            surveyDialog = Dialog(context)
            surveyDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            surveyDialog.setContentView(binding.root)
            surveyDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            surveyDialog.window?.attributes?.windowAnimations = R.style.DialogTheme
            surveyDialog.window?.setLayout(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
            )
            surveyDialog.setCancelable(false)
            surveyDialog.setCanceledOnTouchOutside(false)
            binding.apply {
                if (closedIcon == 1) {
                    imgClose.visibility = View.VISIBLE
                } else {
                    imgClose.visibility = View.INVISIBLE
                }
                tvCompleteYourProfile.text = context.getString(R.string.coc)
                btnContinue.setOnClickListener {
                    listener.onContinueClick()
                }
                imgClose.setOnClickListener {
                    surveyDialog.dismiss()
                }
            }

            surveyDialog.show()
        }

        fun showCompleteYourProfileDialog(context: Context, listener: DialogManager) {
            val binding = DialogComplateYourProfileBinding.inflate(LayoutInflater.from(context))
            completeYourProfileDialog = Dialog(context)
            completeYourProfileDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            completeYourProfileDialog.setContentView(binding.root)
                completeYourProfileDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            completeYourProfileDialog.window?.attributes?.windowAnimations = R.style.DialogTheme
            completeYourProfileDialog.window?.setLayout(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
            )
            completeYourProfileDialog.setCancelable(false)
            completeYourProfileDialog.setCanceledOnTouchOutside(false)

            binding.btnContinue.setOnClickListener {
                listener.onContinueClick()
            }
            binding.imgClose.setOnClickListener {
                completeYourProfileDialog.dismiss()
            }
            completeYourProfileDialog.show()
        }

        fun showAcknowledgeDialog(context: Context, listener: DialogManager) {
            acknowledgeDialog = Dialog(context)
            val binding = DialogAcknowledgeAddBinding.inflate(LayoutInflater.from(context))
            acknowledgeDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            acknowledgeDialog.setContentView(binding.root)
            acknowledgeDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            acknowledgeDialog.window?.attributes?.windowAnimations = R.style.DialogTheme
            acknowledgeDialog.window?.setLayout(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
            )
            acknowledgeDialog.setCancelable(false)
            acknowledgeDialog.setCanceledOnTouchOutside(false)

            binding.imgClose.setOnClickListener {
                acknowledgeDialog.dismiss()
            }
            binding.btnConfirm.setOnClickListener {
                val isCheck = binding.checkAck.isChecked
                listener.onConfirmClick(isCheck)
            }
            acknowledgeDialog.show()
        }

        fun showPolicyAcknowledgeDialog(
            context: Context,
            message: String,
            listener: DialogManager
        ) {
            policyAcknowledgeDialog = Dialog(context)
            val binding = DialogPolicyAcknowledgeBinding.inflate(LayoutInflater.from(context))
            policyAcknowledgeDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            policyAcknowledgeDialog.setContentView(binding.root)
            policyAcknowledgeDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            policyAcknowledgeDialog.window?.attributes?.windowAnimations = R.style.DialogTheme
            policyAcknowledgeDialog.window?.setLayout(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
            )
            policyAcknowledgeDialog.setCancelable(false)
            policyAcknowledgeDialog.setCanceledOnTouchOutside(false)

            binding.apply {
                tvAcknowledgeMessage.text = message

                imgClose.setOnClickListener {
                    policyAcknowledgeDialog.dismiss()
                }

                btnConfirm.setOnClickListener {
                    listener.onPolicyAcknowledgeClick()
                }
            }

            policyAcknowledgeDialog.show()
        }

        fun showLogOutDialog(
            context: Context,
            listener: DialogManager,
            msg: String,
            title: String? = null,
            confirm: String? = null,
            isCloseHide: Boolean = false
        ) {
            val binding = DialogLogOutBinding.inflate(LayoutInflater.from(context))
            logOutDialog = Dialog(context)
            logOutDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            logOutDialog.setContentView(binding.root)
            logOutDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            logOutDialog.window?.attributes?.windowAnimations = R.style.DialogTheme
            logOutDialog.window?.setLayout(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
            )
            logOutDialog.setCancelable(false)
            logOutDialog.setCanceledOnTouchOutside(false)

            binding.imgClose.apply {
                visibility = if (isCloseHide) View.INVISIBLE else View.VISIBLE
                isEnabled = !isCloseHide
            }

            binding.btnYes.setOnClickListener {
                logOutDialog.dismiss()
                listener.onOkClick()
            }
            binding.imgClose.setOnClickListener {
                logOutDialog.dismiss()
            }
            binding.apply {
                confirm?.let { btnYes.text = it }
                title?.let { tvLogOut.text = it }
                tvAreYou.text = msg
            }
            logOutDialog.show()
        }

        fun showBirthdayDialog(
            context: Context,
            name: String,
            msg: String,
            image: Bitmap?,
            background: Bitmap?
        ) {
            val binding = DialogBirthdayWishBinding.inflate(LayoutInflater.from(context))
            birthdayWishDialog = Dialog(context)
            birthdayWishDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            birthdayWishDialog.setContentView(binding.root)
            birthdayWishDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            birthdayWishDialog.window?.attributes?.windowAnimations = R.style.DialogTheme
            birthdayWishDialog.window?.setLayout(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
            )
            birthdayWishDialog.setCancelable(false)
            birthdayWishDialog.setCanceledOnTouchOutside(false)

            binding.apply {
                btnClose.setOnClickListener {
                    birthdayWishDialog.dismiss()
                }
                tvName.text = name
                tvMsg.text = msg
                ImageUtils.INSTANCE?.loadBitMap(
                    imgBanner, background,
                    ContextCompat.getDrawable(context, R.drawable.bg_birthday_wish)
                )
                ImageUtils.INSTANCE?.loadBitMap(
                    imgProfilePic, image,
                    ContextCompat.getDrawable(context, R.drawable.profile_placeholder)
                )

            }
            birthdayWishDialog.show()
        }

        fun closeAcknowledgeDialog() {
            if (Companion::acknowledgeDialog.isInitialized) {
                acknowledgeDialog.dismiss()
            }
        }

        fun closeAttendanceMarkDialog() {
            if (Companion::markYourAttendanceDialog.isInitialized) {
                markYourAttendanceDialog.dismiss()
            }
        }

        fun closeCompleteYourProfileDialog() {
            if (Companion::completeYourProfileDialog.isInitialized) {
                completeYourProfileDialog.dismiss()
            }
        }

        fun closeReimbursementPreApprovalDialog() {
            if (Companion::reimbursementPreApprovalDialog.isInitialized) {
                reimbursementPreApprovalDialog.dismiss()
            }
        }

        fun closePolicyAcknowledgeDialog() {
            if (Companion::policyAcknowledgeDialog.isInitialized) {
                policyAcknowledgeDialog.dismiss()
            }
        }

        fun closeSurveyDialog() {
            if (Companion::surveyDialog.isInitialized) {
                surveyDialog.dismiss()
            }
        }

    }

    interface DialogManager {
        fun onOkClick() {}
        fun onSelectYear(Year: String) {}
        fun onContinueClick() {}
        fun onConfirmClick(isCheck: Boolean) {}
        fun onMarkAttendanceClick() {}
        fun onAttachmentClick(date: String, name: String, amt: String) {}
        fun onChooseDateClick(view: TextInputEditText) {}

        fun onPolicyAcknowledgeClick() {}

    }
}