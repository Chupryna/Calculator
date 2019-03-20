package com.example.calculator.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.calculator.R
import com.example.calculator.room.Expression
import kotlinx.android.synthetic.main.history_item.view.*

class RVAdapterHistory(list: List<Expression>) : androidx.recyclerview.widget.RecyclerView.Adapter<RVAdapterHistory.HistoryViewHolder>() {

    private val listHistory: List<Expression> = list

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.history_item, parent, false)
        return HistoryViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listHistory.size
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        holder.textExpression.text = listHistory[position].expression
        holder.textResult.text = listHistory[position].result
        holder.textDate.text = listHistory[position].date
    }

    class HistoryViewHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {
        val textExpression: TextView = itemView.textExpressionHistory
        val textResult: TextView = itemView.textResultHistory
        val textDate: TextView = itemView.textDateHistory
    }
}