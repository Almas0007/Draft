package kz.mobydev.drevmass.utils


interface NavigationHostProvider {
    fun setNavigationVisibility(visible: Boolean)
    fun setNavigationToolBar(isGone:Boolean)

    fun additionalToolBarConfig(btnBackVisible:Boolean , titleVisible:Boolean, btnProfileVisible:Boolean, title:String)
    fun setOnClickBack(id:Int)
    fun setOnClickProfile()
    fun setOnClickExit(visibility: Boolean)
    fun setOnClickSave(visibility: Boolean)

    fun valueFavorite(value: Int)
    fun visibilityTutorialButton(videoLink: String, visibility: Boolean)
}