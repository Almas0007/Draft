package kz.mobydev.drevmass.presentation.profile.doc

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import javax.inject.Inject
import kz.mobydev.drevmass.App
import kz.mobydev.drevmass.R
import kz.mobydev.drevmass.data.preferences.PreferencesDataSource
import kz.mobydev.drevmass.databinding.FragmentAboutBinding
import kz.mobydev.drevmass.databinding.FragmentDocumentationBinding
import kz.mobydev.drevmass.utils.RulesDrevmassApp
import kz.mobydev.drevmass.utils.provideNavigationHost

class DocumentationFragment : Fragment() {
    private val appComponents by lazy { App.appComponents }

    @Inject
    lateinit var shared: PreferencesDataSource

    private lateinit var binding: FragmentDocumentationBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        appComponents.inject(this)
        binding = FragmentDocumentationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = DocumentationAdapter()
        adapter.submitList(RulesDrevmassApp.rulesList)
        binding.listView.adapter = adapter
    }


    override fun onResume() {
        super.onResume()
        provideNavigationHost()?.apply {
            setNavigationVisibility(false)
            setNavigationToolBar(false)
            setOnClickBack(R.id.action_documentationFragment_to_profileFragment)
            additionalToolBarConfig(
                true,
                titleVisible = true,
                btnProfileVisible = false,
                title = "Правовая информация"
            )
        }
    }

    override fun onPause() {
        super.onPause()
        provideNavigationHost()?.apply {
            setNavigationVisibility(false)
            setNavigationToolBar(true)
            setOnClickBack(R.id.action_documentationFragment_to_profileFragment)
            additionalToolBarConfig(
                true,
                titleVisible = true,
                btnProfileVisible = false,
                title = "Правовая информация"
            )
        }
    }
}