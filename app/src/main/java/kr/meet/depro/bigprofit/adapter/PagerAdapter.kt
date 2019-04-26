package kr.meet.depro.bigprofit.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.PagerAdapter
import kr.meet.depro.bigprofit.fragment.list1
import kr.meet.depro.bigprofit.fragment.list2

class PagerAdapter(fragmentManager: FragmentManager) : FragmentStatePagerAdapter(fragmentManager) {

    var page1: Fragment = list1()
    var page2: Fragment = list2()
    var pageArray: ArrayList<Fragment> = arrayListOf(page1, page2)
    var pageTitleArray: ArrayList<String> = arrayListOf("1+1", "2+1")

    override fun getItem(position: Int): Fragment? {
        return pageArray[position]
    }

    override fun getItemPosition(`object`: Any): Int {
        return PagerAdapter.POSITION_NONE
    }

    override fun getCount(): Int {
        return pageArray.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return pageTitleArray[position]
    }
}