package com.example.digitracksdk.presentation.home.jobs_and_refer_fds.job_details_screen

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View.VISIBLE
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.digitracksdk.Constant
import com.innov.digitrac.R
import com.innov.digitrac.databinding.ActivityJobDetailsBinding
import com.example.digitracksdk.presentation.home.jobs_and_refer_fds.jobs.model.LstOpenDemandForCandidatesItem
import com.example.digitracksdk.presentation.home.jobs_and_refer_fds.job_details_screen.adapter.JobDetailsAdapter
import com.example.digitracksdk.presentation.home.jobs_and_refer_fds.job_details_screen.model.JobDetailsModel
import com.example.digitracksdk.presentation.home.jobs_and_refer_fds.job_details_screen.model.JobDetailsType
import com.example.digitracksdk.presentation.home.jobs_and_refer_fds.refer_a_friend.ReferAFriendActivity
import com.example.digitracksdk.utils.AppUtils

class JobDetailsActivity : AppCompatActivity() {
    lateinit var binding: ActivityJobDetailsBinding
    private lateinit var jobDetailsAdapter: JobDetailsAdapter
    var jobDetailsList: ArrayList<JobDetailsModel> = ArrayList()
    lateinit var jobDetails: LstOpenDemandForCandidatesItem
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityJobDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        AppUtils.INSTANCE?.systemBarPadding(binding.root)

        getIntentData()
        setUpJobDetailsData()
        setUpListener()
    }

    private fun setUpJobDetailsData() {
        val skillsList: ArrayList<String> = ArrayList()
        skillsList.clear()
        jobDetails.role?.let { skillsList.add(it) }

        jobDetailsList.clear()
        jobDetailsList.add(
            JobDetailsModel(
                jobDetailsType = JobDetailsType.JOB_DETAILS_TOP,
                title = jobDetails.designation,
                subTitle = jobDetails.industryType,
                location = jobDetails.locationName,
                min_exp = jobDetails.experienceRange,
                profilePic = R.drawable.info2
            )
        )

        jobDetailsList.add(
            JobDetailsModel(
                jobDetailsType = JobDetailsType.JOB_DETAILS_WITH_BULLETS,
                title = getString(R.string.skills),
                points = skillsList
            )
        )

        jobDetailsList.add(
            JobDetailsModel(
                jobDetailsType = JobDetailsType.JOB_DETAILS_WITHOUT_BULLETS,
                title = getString(R.string.salary),
                subTitle = jobDetails.salaryRange
            )
        )

        setUpJobDetailsAdapter()
    }

    private fun setUpJobDetailsAdapter() {
        jobDetailsAdapter = JobDetailsAdapter(this, jobDetailsList)
        binding.recyclerJobDetails.adapter = jobDetailsAdapter
    }

    private fun setUpListener() {
        binding.apply {

            btnReferFriend.setOnClickListener {
                val bundle = Bundle()
                bundle.putString(
                    Constant.CLIENT_REQUIREMENT_ID,
                    jobDetails.rMClientRequirementID.toString()
                )
                val intent = Intent(this@JobDetailsActivity, ReferAFriendActivity::class.java)
                intent.putExtras(bundle)
                startActivity(intent)
            }
        }
    }

    private fun getIntentData() {
        intent.extras?.run {
            jobDetails = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                getSerializable(
                    Constant.JOB_MODEL,
                    LstOpenDemandForCandidatesItem::class.java) as LstOpenDemandForCandidatesItem
            } else
                getSerializable(Constant.JOB_MODEL) as LstOpenDemandForCandidatesItem

            setUpToolbar(jobDetails.designation.toString())
        }
    }

    private fun setUpToolbar(screenTitle: String) {
        binding.toolbar.apply {
            tvTitle.text = screenTitle
            btnBack.setOnClickListener {
                finish()
            }
            divider.visibility = VISIBLE
        }
    }
}