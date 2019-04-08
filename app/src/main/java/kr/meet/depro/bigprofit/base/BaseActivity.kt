package kr.meet.depro.bigprofit.base

import android.app.Activity
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.v7.app.AppCompatActivity

abstract class BaseActivity<B: ViewDataBinding>(private val layoutId: Int) : AppCompatActivity() {

    companion object {
        private val arrayList = ArrayList<Activity>()
    }

    lateinit var dataBinding : B

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataBinding = DataBindingUtil.setContentView(this, layoutId)
        arrayList.add(this)
        initView()
        start()

    }
    abstract fun initView()

    abstract fun start()

    override fun onDestroy() {
        arrayList.remove(this)
        super.onDestroy()
    }
}
