package com.example.digitracksdk.presentation.home.client_policy.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.digitracksdk.R
import com.example.digitracksdk.databinding.ClientPolicyItemBinding
import com.example.digitracksdk.presentation.home.client_policy.model.ClientPolicyModel
import com.example.digitracksdk.presentation.home.client_policy.model.PolicyStatusType

class ClientPolicyAdapter(
    val context: Context,
    private var policyList: ArrayList<ClientPolicyModel>,
    val listener: PolicyClickManager
) :
    RecyclerView.Adapter<ClientPolicyAdapter.ViewHolder>() {

    class ViewHolder(var binding: ClientPolicyItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.client_policy_item, parent, false)
        val binding = ClientPolicyItemBinding.bind(view)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = policyList[position]
        holder.binding.apply {
            tvPolicyType.text = data.policyType
            tvPolicyName.text = data.policyName
            tvPolicyDate.text = context.getString(R.string.acknowledged_on) + " "+data.date

            when (data.policyStatusType) {
                PolicyStatusType.ACKNOWLEDGED -> {
                    btnAcknowledge.visibility = VISIBLE
                }
                PolicyStatusType.PENDING -> {
                    btnAcknowledge.visibility = GONE
                }
            }
            btnAcknowledge.setOnClickListener {
                listener.clickOnAckBtn(position)
            }
            btnView.setOnClickListener {
                listener.clickOnViewBtn(position)
            }
        }

    }

    override fun getItemCount(): Int {
        return policyList.size
    }

    interface PolicyClickManager {
        fun clickOnViewBtn(position: Int)
        fun clickOnAckBtn(position: Int)
    }
}