package com.example.digitracksdk.presentation.resignation

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import com.innov.digitrac.R
import com.innov.digitrac.base.BaseActivity
import com.innov.digitrac.databinding.ActivityResignationBinding
import com.example.digitracksdk.presentation.my_letters.adapter.MyLettersAdapter
import com.example.digitracksdk.presentation.my_letters.model.MyLettersModel
import com.example.digitracksdk.presentation.resignation.add_resignation.AddResignationActivity
import com.example.digitracksdk.presentation.resignation.view_resignation.ViewResignationActivity
import com.example.digitracksdk.utils.AppUtils

class ResignationActivity : BaseActivity(), MyLettersAdapter.MyLettersClickManager {
    private var resignationList :ArrayList<MyLettersModel> = ArrayList()
    lateinit var resignationAdapter: MyLettersAdapter

    lateinit var binding:ActivityResignationBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityResignationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        AppUtils.INSTANCE?.systemBarPadding(binding.root)
        setUpToolbar()
        setUpMyLettersData()
    }

    private fun setUpToolbar() {
        binding.toolbar.apply {
            tvTitle.text = getString(R.string.resignation)
            divider.visibility = View.VISIBLE
            btnBack.setOnClickListener {
                finish()
            }
        }
    }

    private fun setUpMyLettersData() {
        resignationList.clear()
        resignationList.add(MyLettersModel(itemName = getString(R.string.resignation),itemIcon = R.drawable.menu_resignation))
        resignationList.add(MyLettersModel(itemName = getString(R.string.view_resignation),itemIcon = R.drawable.ic_loi))
        setUpMyLettersAdapter()
    }

    private fun setUpMyLettersAdapter() {
        resignationAdapter = MyLettersAdapter(this,resignationList,this)
        binding.recyclerResignation.adapter = resignationAdapter
    }

    override fun onClickItems(position: Int) {
        when (resignationList[position].itemName) {
            getString(R.string.resignation) -> {
                startActivity(Intent(this, AddResignationActivity::class.java))
            }
            getString(R.string.view_resignation) -> {
                startActivity(Intent(this, ViewResignationActivity::class.java))
            }
        }
    }
}