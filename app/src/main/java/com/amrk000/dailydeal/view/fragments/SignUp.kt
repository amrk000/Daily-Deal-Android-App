package com.amrk000.dailydeal.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.amrk000.dailydeal.Model.UserData
import com.amrk000.dailydeal.R
import com.amrk000.dailydeal.databinding.FragmentSignUpBinding
import com.amrk000.dailydeal.util.TextFieldValidator
import com.amrk000.dailydeal.viewModel.SignUpViewModel
import java.util.Locale


class SignUp : Fragment() {
    private lateinit var binding: FragmentSignUpBinding
    private lateinit var viewModel: SignUpViewModel
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSignUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //view model init
        viewModel = ViewModelProvider(this).get(SignUpViewModel::class.java)

        //nav controller init
        navController = Navigation.findNavController(view)

        binding.signUpSignInButton.setOnClickListener {
            navController.navigate(R.id.action_signUp_to_signIn)
        }

        binding.signUpSignButton.setOnClickListener {
            if(allValid()) {
                viewModel.signUpUser(
                    UserData(
                        name = binding.signUpNameField.editText?.text.toString(),
                        email = binding.signUpEmailField.editText?.text.toString().lowercase(),
                        password = binding.signUpPasswordField.editText?.text.toString(),
                        address = binding.signUpAddressField.editText?.text.toString(),
                        phone = binding.signUpAddressField.editText?.text.toString()
                    )
                )

            }
        }

        viewModel.getSignUpUserObserver().observe(viewLifecycleOwner){ result ->
            if(result != null){
                Toast.makeText(context,
                    getString(R.string.account_created_successfully), Toast.LENGTH_LONG).show()
                navController.navigate(R.id.action_signUp_to_signIn)
            } else{
                Toast.makeText(context,
                    getString(R.string.your_already_have_an_account_with_this_email), Toast.LENGTH_LONG).show()
            }

        }

    }

    fun allValid(): Boolean{
        var allValid = true

        //Name Field
        if(! TextFieldValidator.isValidName(binding.signUpNameField.editText?.text.toString())){
            binding.signUpNameField.error = getString(R.string.please_enter_your_full_name)
            allValid = false
        }
        else binding.signUpNameField.error = null

        //Email Field
        if(! TextFieldValidator.isValidEmail(binding.signUpEmailField.editText?.text.toString())){
            binding.signUpEmailField.error = getString(R.string.please_enter_a_valid_email)
            allValid = false
        }
        else binding.signUpEmailField.error = null

        //confirm address Field
        if(binding.signUpAddressField.editText?.text.toString().length < 12){
            binding.signUpAddressField.error =
                getString(R.string.please_enter_your_full_delivery_address)
            allValid = false
        }
        else binding.signUpAddressField.error = null

        //phone Field
        if(! TextFieldValidator.isValidPhone(binding.signUpPhoneNumberField.editText?.text.toString())){
            binding.signUpPhoneNumberField.error = getString(R.string.please_enter_a_valid_number)
            allValid = false
        }
        else binding.signUpPhoneNumberField.error = null

        //password Field
        if(! TextFieldValidator.isValidPassword(binding.signUpPasswordField.editText?.text.toString())){
            binding.signUpPasswordField.error = getString(R.string.password_should_be_8_chars_with_upper_lower_and_symbols)
            allValid = false
        }
        else binding.signUpPasswordField.error = null

        //confirm password Field
        if(! binding.signUpPasswordField.editText?.text.toString().equals(binding.signUpConfirmPasswordField.editText?.text.toString())){
            binding.signUpConfirmPasswordField.error = getString(R.string.password_not_matching)
            allValid = false
        }
        else binding.signUpConfirmPasswordField.error = null

        return allValid
    }
}