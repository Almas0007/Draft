package kz.mobydev.drevmass.utils

import kz.mobydev.drevmass.R

enum class EnumBottomNavigationLayout(val position: Int, val layoutId : Int) {

    HOME(0, R.layout.fragment_product),
    DASHBOARD(1, R.layout.fragment_lessons),
    NOTIFICATION(2, R.layout.fragment_favorite);


    companion object {
        private val map = EnumBottomNavigationLayout.values().associateBy(EnumBottomNavigationLayout::position);
        fun getLayoutIdFromPosition(position: Int) = map[position]!!.layoutId
    }
}