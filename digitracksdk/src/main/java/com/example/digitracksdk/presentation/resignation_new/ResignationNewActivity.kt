package com.example.digitracksdk.presentation.resignation_new

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import com.innov.digitrac.R
import com.innov.digitrac.base.BaseActivity
import com.innov.digitrac.databinding.ActivityResignationNewBinding
import com.example.digitracksdk.presentation.my_letters.adapter.MyLettersAdapter
import com.example.digitracksdk.presentation.my_letters.model.MyLettersModel
import com.example.digitracksdk.presentation.resignation.view_resignation.ViewResignationActivity
import com.example.digitracksdk.presentation.resignation_new.add_resignation_new.AddResignationNewActivity
import com.example.digitracksdk.utils.AppUtils

class ResignationNewActivity : BaseActivity() ,  MyLettersAdapter.MyLettersClickManager {
    private var resignationList: ArrayList<MyLettersModel> = ArrayList()
    private lateinit var resignationAdapter: MyLettersAdapter
    lateinit var binding: ActivityResignationNewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityResignationNewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        AppUtils.INSTANCE?.systemBarPadding(binding.root)
        setUpToolbar()
        setUpData()
    }

    private fun setUpData() {

        resignationList.clear()
        resignationList.addAll(
            ArrayList<MyLettersModel>().apply {
                clear()
                add(
                    MyLettersModel(
                        itemName = getString(R.string.resignation_new),
                        itemIcon = R.drawable.menu_resignation
                    )
                )
                add(
                    MyLettersModel(
                        itemName = getString(R.string.view_resignation),
                        itemIcon = R.drawable.ic_loi
                    )
                )
            }
        )
        setUpAdapter()
    }

    private fun setUpAdapter() {
        resignationAdapter = MyLettersAdapter(this,resignationList,this)
        binding.recyclerResignationNew.adapter = resignationAdapter
    }

    private fun setUpToolbar() {
        binding.toolbar.apply {
            tvTitle.text = getString(R.string.resignation_new)
            divider.visibility = View.VISIBLE
            btnBack.setOnClickListener {
                finish()
            }
        }

    }

    override fun onClickItems(position: Int) {

        when (resignationList[position].itemName) {
            getString(R.string.resignation_new) -> {
                startActivity(Intent(this, AddResignationNewActivity::class.java))
            }
            getString(R.string.view_resignation) -> {
                startActivity(Intent(this, ViewResignationActivity::class.java))
            }
        }
    }
}