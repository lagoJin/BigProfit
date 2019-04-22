package kr.meet.depro.bigprofit.activity

import android.text.Editable
import android.text.TextWatcher
import kr.meet.depro.bigprofit.R
import kr.meet.depro.bigprofit.base.BaseActivity
import kr.meet.depro.bigprofit.databinding.ActivitySearchBinding

class SearchActivity : BaseActivity<ActivitySearchBinding>(R.layout.activity_search) {

    override fun initView() {
        dataBinding.etSearchText.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {

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