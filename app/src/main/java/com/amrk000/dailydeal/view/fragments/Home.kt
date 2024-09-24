package com.amrk000.dailydeal.view.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.amrk000.dailydeal.Model.ItemData
import com.amrk000.dailydeal.Model.UserData
import com.amrk000.dailydeal.R
import com.amrk000.dailydeal.adapter.MealsAdapter
import com.amrk000.dailydeal.databinding.FragmentHomeBinding
import com.amrk000.dailydeal.util.ImageBase64Converter
import com.amrk000.dailydeal.util.UserAccountManager
import com.amrk000.dailydeal.view.About
import com.amrk000.dailydeal.viewModel.HomeViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.gson.Gson
import java.time.LocalDate

class Home : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var viewModel: HomeViewModel
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //view model init
        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        //nav controller init
        navController = Navigation.findNavController(view)

        //load cached user profile
        val userData = viewModel.getUserData() ?: return

        binding.homeGreetings.text = binding.homeGreetings.text.toString().replace(getString(R.string.name), userData.name.split(" ").first())

        binding.homeSecondLabel.text = binding.homeSecondLabel.text.toString().replace(getString(R.string.today), LocalDate.now().dayOfWeek.name.lowercase())

        binding.homeSignOutButton.setOnClickListener {
            val alertDialogBuilder = MaterialAlertDialogBuilder(requireContext())
            alertDialogBuilder.setCancelable(true)
            alertDialogBuilder.setTitle(getString(R.string.sign_out))
            alertDialogBuilder.setPositiveButton(getString(R.string.yes)) { dialog, which ->
                viewModel.signOut()

                navController.navigate(
                    R.id.action_home_to_signIn,
                    null,
                    NavOptions.Builder().setPopUpTo(R.id.signIn, true).build()
                )

            }
            alertDialogBuilder.setNegativeButton(getString(R.string.cancel)) { dialog, which ->
                dialog.dismiss()
            }
            alertDialogBuilder.create()
            alertDialogBuilder.show()
        }

        binding.homeAboutButton.setOnClickListener {
            startActivity(Intent(context, About::class.java))
        }

        //init meals recycler View
        val mealsAdapter = MealsAdapter(requireContext(), false)
        binding.homeRecyclerView.setLayoutManager(GridLayoutManager(context,2,LinearLayoutManager.VERTICAL, false));
        binding.homeRecyclerView.setAdapter(mealsAdapter)

        //Load Data
        viewModel.getTodayItems()

        viewModel.getItemsDataObserver().observe(viewLifecycleOwner){ items ->
            if(items.isNotEmpty()) mealsAdapter.setData(items)
            else binding.homeNoData.visibility = View.VISIBLE
        }

        mealsAdapter.setOnItemClickListener(object: MealsAdapter.OnItemClickListener{
            override fun onItemClick(position: Int, data: ItemData) {
                val bundle = Bundle()
                bundle.putString("passedData", Gson().toJson(data))
                navController.navigate(R.id.action_home_to_makeOrder, bundle)
            }
        })
    }


}