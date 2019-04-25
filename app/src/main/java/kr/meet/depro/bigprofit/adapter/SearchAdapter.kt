package kr.meet.depro.bigprofit.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import kr.meet.depro.bigprofit.R
import kr.meet.depro.bigprofit.activity.MainActivity
import kr.meet.depro.bigprofit.databinding.ItemSearchBinding
import kr.meet.depro.bigprofit.model.SearchItem

class SearchAdapter(val list: ArrayList<SearchItem>) : RecyclerView.Adapter<SearchAdapter.ViewHolder>() {

    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): ViewHolder {
        context = parent.context
        val inflater = LayoutInflater.from(parent.context)
        val binding: ItemSearchBinding = DataBindingUtil.inflate(inflater, R.layout.item_search, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.tvRvName.text = list[position].name
        holder.binding.tvRvName.setOnClickListener {
            val intent = Intent(context, MainActivity::class.java)
            intent.putParcelableArrayListExtra("list", list)
            val activity = context as Activity
            activity.setResult(Activity.RESULT_OK, intent)
            activity.finish()
        }
    }


    class ViewHolder(internal val binding: ItemSearchBinding) : RecyclerView.ViewHolder(binding.root)

}