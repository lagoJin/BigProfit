package kr.meet.depro.bigprofit.adapter

import android.content.Context
import android.databinding.DataBindingUtil
import android.graphics.Color
import android.provider.CalendarContract
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.Glide
import kr.meet.depro.bigprofit.R
import kr.meet.depro.bigprofit.activity.SearchActivity
import kr.meet.depro.bigprofit.databinding.ItemSearchBinding
import kr.meet.depro.bigprofit.databinding.ProductItemBinding
import kr.meet.depro.bigprofit.model.SearchItem

class SearchAdapter(private val list: ArrayList<SearchItem>) :
    RecyclerView.Adapter<SearchAdapter.ViewHolder>() {

    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): ViewHolder {
        context = parent.context
        val inflater = LayoutInflater.from(parent.context)
        val binding: ProductItemBinding = DataBindingUtil.inflate(inflater, R.layout.product_item, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.itemPdName.text = list[position].name
        holder.binding.itemPrice.text = "${list[position].price}원"
        Glide.with(context)
            .load(list[position].imageUrl)
            .into(holder.binding.imageView)

        when {
            list[position].storeName.contains("CU") -> {
                holder.binding.itemCsName.text = "CU"
                holder.binding.itemCsName.setBackgroundColor(Color.parseColor("#8c31aa"))
            }
            list[position].storeName.contains("MINISTOP") -> {
                holder.binding.itemCsName.text = "MINISTOP"
                holder.binding.itemCsName.setBackgroundColor(Color.parseColor("#15449d"))
            }
            list[position].storeName.contains("EMART24") -> {
                holder.binding.itemCsName.text = "EMART24"
                holder.binding.itemCsName.setBackgroundColor(Color.parseColor("#ffb717"))
            }
        }
    }

    class ViewHolder(internal val binding: ProductItemBinding) : RecyclerView.ViewHolder(binding.root)

}