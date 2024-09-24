package com.amrk000.dailydeal.view.fragments

import android.accounts.AccountManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import com.amrk000.dailydeal.Model.UserData
import com.amrk000.dailydeal.R
import com.amrk000.dailydeal.databinding.FragmentSignInBinding
import com.amrk000.dailydeal.databinding.FragmentSignUpBinding
import com.amrk000.dailydeal.util.TextFieldValidator
import com.amrk000.dailydeal.util.UserAccountManager
import com.amrk000.dailydeal.view.MainActivity
import com.amrk000.dailydeal.viewModel.SignInViewModel
import com.amrk000.dailydeal.viewModel.SignUpViewModel

class SignIn : Fragment() {
    private lateinit var binding: FragmentSignInBinding
    private lateinit var viewModel: SignInViewModel
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSignInBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //view model init
        viewModel = ViewModelProvider(this).get(SignInViewModel::class.java)

        //nav controller init
        navController = Navigation.findNavController(view)

        binding.signInSignUpButton.setOnClickListener {
            navController.navigate(R.id.action_signIn_to_signUp)
        }

        binding.signInSignButton.setOnClickListener {
            if(allValid()) {
                viewModel.signInUser(
                    binding.signInEmailField.editText?.text.toString().lowercase(),
                    binding.signInPasswordField.editText?.text.toString(),
                )
            }
        }

        viewModel.getSignInUserObserver().observe(viewLifecycleOwner){ userData ->
            if(userData != null){
                (activity as MainActivity).showAdminDashboard(viewModel.canUserAccessAdminDashboard())
                navController.navigate(R.id.action_signIn_to_home,null, NavOptions.Builder().setPopUpTo(R.id.home, true).build())
            } else{
                Toast.makeText(context,
                    getString(R.string.wrong_email_or_password), Toast.LENGTH_LONG).show()
            }

        }

    }

    fun allValid(): Boolean{
        var allValid = true

        //Email Field
        if(! TextFieldValidator.isValidEmail(binding.signInEmailField.editText?.text.toString())){
            binding.signInEmailField.error = getString(R.string.please_enter_a_valid_email)
            allValid = false
        }
        else binding.signInEmailField.error = null

        //password Field
        if(! TextFieldValidator.isValidPassword(binding.signInPasswordField.editText?.text.toString())){
            binding.signInPasswordField.error =
                getString(R.string.password_should_be_8_chars_with_upper_lower_and_symbols)
            allValid = false
        }
        else binding.signInPasswordField.error = null

        return allValid
    }
}