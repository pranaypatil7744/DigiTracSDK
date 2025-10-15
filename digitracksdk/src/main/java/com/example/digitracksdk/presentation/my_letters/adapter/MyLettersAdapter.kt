package com.example.digitracksdk.presentation.my_letters.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View.INVISIBLE
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.innov.digitrac.R
import com.innov.digitrac.databinding.ItemMyLettersBinding
import com.example.digitracksdk.presentation.my_letters.model.MyLettersModel

class MyLettersAdapter(val context: Context, private val myLettersList: ArrayList<MyLettersModel>, var listener: MyLettersClickManager):RecyclerView.Adapter<MyLettersAdapter.ViewHolder>() {

    class ViewHolder(var binding:ItemMyLettersBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_my_letters,parent,false)
        val binding = ItemMyLettersBinding.bind(view)
        return ViewHolder(binding)
    }

    //TODO Note: this adapter also used for PfEsicInsurance,Leaves

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
      val data = myLettersList[position]
        holder.binding.apply {
            tvItemName.text = data.itemName
            data.itemIcon?.let { imgIcon.setImageResource(it) }
            if (position == myLettersList.size - 1){
                divider.visibility = INVISIBLE
            }
        }
        holder.itemView.setOnClickListener {
            listener.onClickItems(position)
        }
    }

    override fun getItemCount(): Int {
      return myLettersList.size
    }

    interface MyLettersClickManager{
        fun onClickItems(position: Int)
    }
}