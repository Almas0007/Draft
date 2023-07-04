package kz.mobydev.drevmass.presentation.profile.user

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import javax.inject.Inject
import kz.mobydev.drevmass.ActivityViewModel
import kz.mobydev.drevmass.App
import kz.mobydev.drevmass.MainActivity
import kz.mobydev.drevmass.R
import kz.mobydev.drevmass.data.preferences.PreferencesDataSource
import kz.mobydev.drevmass.databinding.FragmentUserBinding
import kz.mobydev.drevmass.model.UserInfoPostRequest
import kz.mobydev.drevmass.utils.onChange
import kz.mobydev.drevmass.utils.provideNavigationHost
import kz.mobydev.drevmass.utils.showCustomToast
import kz.mobydev.drevmass.utils.viewModelProvider


class UserFragment : Fragment() {
    private val appComponents by lazy { App.appComponents }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

     fun getViewModel(): UserViewModel {
        return viewModelProvider(viewModelFactory)
    }
    private fun getActivityViewModel(): ActivityViewModel {
        return viewModelProvider(viewModelFactory)
    }


    @Inject
    lateinit var shared: PreferencesDataSource

    private lateinit var binding: FragmentUserBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        appComponents.inject(this)
        binding = FragmentUserBinding.inflate(inflater, container, false)
        return binding.root
    }
    private var selectName = ""
    private var selectEmail = ""
    private var selectPassword = ""
    private var selectConfirmPassword = ""
    private var selectMale = true
    private var selectHeight = ""
    private var selectWeight = ""
    private var selectBirthday = ""
    private var selectActivity = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observe()
        selectInPageFirst()
        binding.btnDeleteAccount.setOnClickListener {
            shared.setToken("")
            findNavController().navigate(R.id.action_userFragment_to_splashFragment)
        }


    }

    fun send(click:Boolean){
        if (click) {
            val gender = if (selectMale) 1 else 0
            val userInfo = UserInfoPostRequest(
                name = selectName,
                email = selectEmail,
                password = selectPassword,
                passwordConfirmation = selectConfirmPassword,
                information = UserInfoPostRequest.Information(
                    gender = gender,
                    height = selectHeight.toIntOrNull() ?: 0,
                    weight = selectWeight.toDoubleOrNull() ?: 0.0,
                    birth = selectBirthday,
                    activity = selectActivity
                )
            )
            shared.setPassword(selectPassword)
            getViewModel().updateUser(shared.getToken(), userInfoPostRequest = userInfo)
        }
    }

    private fun observe() {
        getViewModel().getUser(shared.getToken())
        getViewModel().userInfo.observe(viewLifecycleOwner) { userInfo ->
            binding.let { it ->
                selectName = userInfo.name
                selectEmail = userInfo.email
                selectPassword = shared.getPassword()
                selectConfirmPassword = shared.getPassword()
                if (userInfo.information != null) {
                    selectMale = userInfo.information.gender == 0
                    selectHeight = userInfo.information.height.toString()
                    selectWeight = userInfo.information.weight.toString()
                    selectBirthday = userInfo.information.birth
                    selectActivity = userInfo.information.activity
                    it.tvEditTextNameRegIn.setText(userInfo.name)
                    it.tvEditTextEmailRegIn.setText(userInfo.email)
                    it.tvEditTextHeight.setText(userInfo.information.height)
                    it.tvEditTextWeight.setText(userInfo.information.weight)


                // Заполнение пола
                if (userInfo.information.gender == 0) {
                    // Мужской пол
                    it.btnMan.setTextColor(Color.WHITE)
                    it.btnMan.setBackgroundResource(R.drawable.background_50dp_woody)
                    it.btnWoman.setBackgroundResource(R.drawable.background_50dp_grey_50)
                    selectMale = false
                } else {
                    // Женский пол
                    it.btnMan.setTextColor(ContextCompat.getColor(requireContext(), R.color.grey_black))
                    it.btnWoman.setTextColor(Color.WHITE)
                    it.btnMan.setBackgroundResource(R.drawable.background_50dp_grey_50)
                    it.btnWoman.setBackgroundResource(R.drawable.background_50dp_woody)
                    selectMale = true
                }

                // Заполнение даты рождения
                val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val date = dateFormat.parse(userInfo.information.birth)
                val calendar = Calendar.getInstance()
                calendar.time = date
                val year = calendar.get(Calendar.YEAR)
                val month = calendar.get(Calendar.MONTH)
                val day = calendar.get(Calendar.DAY_OF_MONTH)
                it.btnSelectCalendar.text = "$day.$month.$year"
                selectBirthday = userInfo.information.birth

                // Заполнение активности
                when (userInfo.information.activity) {
                    0 -> {
                        it.btnLow.setBackgroundResource(R.drawable.background_50dp_woody)
                        it.btnMedium.setBackgroundResource(R.drawable.background_50dp_grey_50)
                        it.btnHigh.setBackgroundResource(R.drawable.background_50dp_grey_50)
                        it.btnLow.setTextColor(Color.WHITE)
                        it.btnMedium.setTextColor(ContextCompat.getColor(requireContext(), R.color.grey_black))
                        it.btnHigh.setTextColor(ContextCompat.getColor(requireContext(), R.color.grey_black))
                        selectActivity = 0
                    }
                    1 -> {
                        it.btnLow.setBackgroundResource(R.drawable.background_50dp_grey_50)
                        it.btnMedium.setBackgroundResource(R.drawable.background_50dp_woody)
                        it.btnHigh.setBackgroundResource(R.drawable.background_50dp_grey_50)
                        it.btnLow.setTextColor(ContextCompat.getColor(requireContext(), R.color.grey_black))
                        it.btnMedium.setTextColor(Color.WHITE)
                        it.btnHigh.setTextColor(ContextCompat.getColor(requireContext(), R.color.grey_black))
                        selectActivity = 1
                    }
                    2 -> {
                        it.btnLow.setBackgroundResource(R.drawable.background_50dp_grey_50)
                        it.btnMedium.setBackgroundResource(R.drawable.background_50dp_grey_50)
                        it.btnHigh.setBackgroundResource(R.drawable.background_50dp_woody)
                        it.btnLow.setTextColor(ContextCompat.getColor(requireContext(), R.color.grey_black))
                        it.btnMedium.setTextColor(ContextCompat.getColor(requireContext(), R.color.grey_black))
                        it.btnHigh.setTextColor(Color.WHITE)
                        selectActivity = 2
                    }
                }
            }}
        }
    }




    private fun selectInPageFirst() {
        binding.let { it ->
            binding.tvEditTextNameRegIn.onChange {
                selectName = it
            }
            binding.tvEditTextEmailRegIn.onChange {
                selectEmail = it
            }
            binding.tvEditTextPasswordRegIn.onChange {
                if (it=="") {
                    binding.tvHelperTextPasswordRegIn.setTextColor(ContextCompat.getColor(requireContext(), R.color.grey_200))
                    selectPassword = shared.getPassword()
                } else {
                    if (it.length< 2) {
                        Toast(appComponents.context()).showCustomToast("Пароль должен быть не менее 8 символов", appComponents.context(),this@UserFragment)
                    }
                    else if (it.length<7){
                        binding.tvHelperTextPasswordRegIn.setTextColor(ContextCompat.getColor(requireContext(), R.color.red))
                    }
                    else {
                        binding.tvHelperTextPasswordRegIn.setTextColor(ContextCompat.getColor(requireContext(), R.color.grey_200))
                        selectPassword = it
                        if (selectPassword != selectConfirmPassword) {
                            binding.tvHelperTextREPasswordRegIn.setTextColor(ContextCompat.getColor(requireContext(), R.color.red))
                        } else {
                            binding.tvHelperTextREPasswordRegIn.setTextColor(ContextCompat.getColor(requireContext(), R.color.grey_200))
                        }
                    }
                }

            }
            binding.tvEditTextREPasswordRegIn.onChange {
                if (it=="" && selectPassword == shared.getPassword()) {
                    selectConfirmPassword = shared.getPassword()
                    binding.tvHelperTextREPasswordRegIn.setTextColor(ContextCompat.getColor(requireContext(), R.color.grey_200))
                } else {
                    if (it.length< 2) {
                        Toast(appComponents.context()).showCustomToast("Пароль должен быть не менее 8 символов", appComponents.context(),this@UserFragment)
                    }
                    else if (it.length<7){
                        binding.tvHelperTextREPasswordRegIn.setTextColor(ContextCompat.getColor(requireContext(), R.color.red))
                    }
                    else if (it != selectPassword){
                        Toast(appComponents.context()).showCustomToast("Пароли должны совподать", appComponents.context(),this@UserFragment)
                    }
                    else {
                        selectConfirmPassword = it
                        binding.tvHelperTextREPasswordRegIn.setTextColor(ContextCompat.getColor(requireContext(), R.color.grey_200))
                    }
                }
            }

            binding.btnMan.setOnClickListener {
                binding.btnMan.setTextColor(Color.WHITE)
                binding.btnMan.setBackgroundResource(R.drawable.background_50dp_woody)
                binding.btnWoman.setBackgroundResource(R.drawable.background_50dp_grey_50)
                selectMale = false
            }
            binding.btnWoman.setOnClickListener {
                binding.btnMan.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.grey_black
                    )
                )
                binding.btnWoman.setTextColor(Color.WHITE)
                binding.btnMan.setBackgroundResource(R.drawable.background_50dp_grey_50)
                binding.btnWoman.setBackgroundResource(R.drawable.background_50dp_woody)
                selectMale = true
            }
            it.tvEditTextHeight.onChange {
                selectHeight = it
            }
            it.tvEditTextWeight.onChange {
                selectWeight = it
            }


            it.btnSelectCalendar.setOnClickListener {
                val builder = MaterialDatePicker.Builder.datePicker()
                    .setTheme(R.style.MaterialDatePickerTheme)


                val datePicker = builder.build()


                datePicker.show(childFragmentManager, "datePicker")

                datePicker.addOnPositiveButtonClickListener {
                    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                    val dateString = dateFormat.format(Date(it))
                    val calendar = Calendar.getInstance()
                    calendar.time = Date(it)
                    val year = calendar.get(Calendar.YEAR)
                    val month = calendar.get(Calendar.MONTH)
                    val day = calendar.get(Calendar.DAY_OF_MONTH)

                    binding.btnSelectCalendar.text = "$day.$month.$year"
                    selectBirthday = dateString ?: "2000-01-01"
                }
            }

            it.btnLow.setOnClickListener {
                binding.btnLow.setBackgroundResource(R.drawable.background_50dp_woody)
                binding.btnMedium.setBackgroundResource(R.drawable.background_50dp_grey_50)
                binding.btnHigh.setBackgroundResource(R.drawable.background_50dp_grey_50)
                binding.btnLow.setTextColor(Color.WHITE)
                binding.btnMedium.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.grey_black
                    )
                )
                binding.btnHigh.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.grey_black
                    )
                )
                selectActivity = 0
            }
            it.btnMedium.setOnClickListener {
                binding.btnLow.setBackgroundResource(R.drawable.background_50dp_grey_50)
                binding.btnMedium.setBackgroundResource(R.drawable.background_50dp_woody)
                binding.btnHigh.setBackgroundResource(R.drawable.background_50dp_grey_50)
                binding.btnLow.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.grey_black
                    )
                )
                binding.btnMedium.setTextColor(Color.WHITE)
                binding.btnHigh.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.grey_black
                    )
                )
                selectActivity = 1
            }
            it.btnHigh.setOnClickListener {
                binding.btnLow.setBackgroundResource(R.drawable.background_50dp_grey_50)
                binding.btnMedium.setBackgroundResource(R.drawable.background_50dp_grey_50)
                binding.btnHigh.setBackgroundResource(R.drawable.background_50dp_woody)
                binding.btnLow.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.grey_black
                    )
                )
                binding.btnMedium.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.grey_black
                    )
                )
                binding.btnHigh.setTextColor(Color.WHITE)
                selectActivity = 2
            }

        }
    }
    override fun onResume() {
        super.onResume()
        provideNavigationHost()?.apply {
            setNavigationVisibility(false)
            setNavigationToolBar(false)
            setOnClickBack(R.id.action_userFragment_to_profileFragment)
            setOnClickSave(true)
            additionalToolBarConfig(
                true,
                titleVisible = true,
                btnProfileVisible = false,
                title = "Профиль"
            )
        }
    }

    override fun onPause() {
        super.onPause()
        provideNavigationHost()?.apply {
            setNavigationVisibility(false)
            setNavigationToolBar(true)
            setOnClickBack(R.id.action_userFragment_to_profileFragment)
            setOnClickSave(true)
            additionalToolBarConfig(
                true,
                titleVisible = true,
                btnProfileVisible = false,
                title = "Профиль"
            )
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        provideNavigationHost()?.apply {
            send(true)
            setOnClickSave(false)
        }
    }
}
