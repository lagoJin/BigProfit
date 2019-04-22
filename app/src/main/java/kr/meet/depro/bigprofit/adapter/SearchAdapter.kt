package kr.meet.depro.bigprofit.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import kr.meet.depro.bigprofit.R

class SearchAdapter : RecyclerView.Adapter<SearchAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): ViewHolder {
        val textView = LayoutInflater.from(parent.context).inflate(R.layout.item_search, parent, false) as TextView
        return ViewHolder(textView)
    }

    override fun getItemCount(): Int {
        return 0
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.textView.text = ""
    }


    class ViewHolder(val textView: TextView) : RecyclerView.ViewHolder(textView)

}