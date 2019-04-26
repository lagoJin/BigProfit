package kr.meet.depro.bigprofit.fragment

import android.os.Build
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kr.meet.depro.bigprofit.R
import kr.meet.depro.bigprofit.activity.MainActivity
import kr.meet.depro.bigprofit.adapter.ProductAdapter
import kr.meet.depro.bigprofit.model.Product

class list1 : Fragment() {
    var productList: ArrayList<Product> = arrayListOf()
    var store = ""
    var count = 0
    val event = 1
    var page = 1
    var adapter = ProductAdapter(productList)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val listItems = inflater.inflate(R.layout.fragment_list1, container, false)
        val rv1pl1 = listItems.findViewById(R.id.recyclerView1pl1) as RecyclerView

        rv1pl1.adapter = adapter
        rv1pl1.layoutManager = GridLayoutManager(activity, 2)

        (activity as MainActivity).productRequest(store,count,event,page)
        adapter.notifyDataSetChanged()
        page++

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            rv1pl1.setOnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
                if (!rv1pl1.canScrollVertically(-1)) {
                    Log.i("scroll", "스크롤 끝")
                    (activity as MainActivity).productRequest(store,count,event,page)
                    adapter.notifyDataSetChanged()
                    page++
                }
            }
        }
        return listItems
    }
}