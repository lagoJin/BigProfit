package kr.meet.depro.bigprofit.activity

import android.text.Editable
import android.text.TextWatcher
import kr.meet.depro.bigprofit.R
import kr.meet.depro.bigprofit.adapter.SearchAdapter
import kr.meet.depro.bigprofit.api.ApiClient
import kr.meet.depro.bigprofit.base.BaseActivity
import kr.meet.depro.bigprofit.databinding.ActivitySearchBinding
import kr.meet.depro.bigprofit.model.SearchItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchActivity : BaseActivity<ActivitySearchBinding>(R.layout.activity_search) {

    private var list = ArrayList<SearchItem>()

    override fun initView() {
        val searchAdapter = SearchAdapter(list)
        dataBinding.rvSearchItem.adapter = searchAdapter

        dataBinding.etSearchText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                s?.let {
                    ApiClient.bigPrfitApi.searchItem(it.toString()).enqueue(object : Callback<List<SearchItem>> {
                        override fun onResponse(call: Call<List<SearchItem>>, response: Response<List<SearchItem>>) {
                            if (response.isSuccessful) {
                                list.clear()
                                response.body()!!.forEach { searchItem ->
                                    list.add(searchItem)
                                }
                                searchAdapter.notifyDataSetChanged()
                            }
                        }

                        override fun onFailure(call: Call<List<SearchItem>>, t: Throwable) {

                        }
                    })
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }
        })
    }

    override fun start() {

    }
}