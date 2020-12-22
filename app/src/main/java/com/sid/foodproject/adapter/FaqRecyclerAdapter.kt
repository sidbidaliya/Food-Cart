package com.sid.foodproject.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sid.foodproject.R
import com.sid.foodproject.model.Faq

class FaqRecyclerAdapter(val context: Context, private val faqList : ArrayList<Faq>) : RecyclerView.Adapter<FaqRecyclerAdapter.FaqViewHolder>(){
    class FaqViewHolder(view: View): RecyclerView.ViewHolder(view){
        val ques : TextView = view.findViewById(R.id.txtQues)
        val ans : TextView = view.findViewById(R.id.txtAns)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FaqViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_faq_single_row,parent,false)
        return FaqViewHolder(view)
    }

    override fun getItemCount(): Int {
        return faqList.size
    }
    override fun onBindViewHolder(holder: FaqViewHolder, position: Int) {
        val faq = faqList[position]
        val contact = faqList[position]
        holder.ques.text = faq.ques
        holder.ans.text = faq.answer
    }
}