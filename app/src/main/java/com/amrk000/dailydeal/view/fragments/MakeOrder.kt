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
import com.amrk000.dailydeal.Model.ItemData
import com.amrk000.dailydeal.Model.UserData
import com.amrk000.dailydeal.R
import com.amrk000.dailydeal.databinding.FragmentDashboardBinding
import com.amrk000.dailydeal.databinding.FragmentMakeOrderBinding
import com.amrk000.dailydeal.util.ImageBase64Converter
import com.amrk000.dailydeal.viewModel.DashboardViewModel
import com.amrk000.dailydeal.viewModel.MakeOrderViewModel
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.google.gson.Gson

class MakeOrder : Fragment() {
    private lateinit var binding: FragmentMakeOrderBinding
    private lateinit var viewModel: MakeOrderViewModel
    private lateinit var navController: NavController

    private var itemData: ItemData? = null
    private var userData: UserData? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMakeOrderBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //view model init
        viewModel = ViewModelProvider(this).get(MakeOrderViewModel::class.java)

        //nav controller init
        navController = Navigation.findNavController(view)

        binding.makeOrderCancelButton.setOnClickListener {
            navController.popBackStack()
        }

        //get passed item data
        if(arguments!=null) itemData = Gson().fromJson(arguments?.getString("passedData"), ItemData::class.java)

        if(itemData==null){
            navController.popBackStack()
            Toast.makeText(requireContext(),
                getString(R.string.error_getting_order_data), Toast.LENGTH_LONG).show()
            return
        }

        userData = viewModel.getUserData()

        //Render Data
        Glide.with(requireContext())
            .load(ImageBase64Converter.decode(requireContext() ,itemData?.image.toString()))
            .centerCrop()
            .transition(DrawableTransitionOptions.withCrossFade(500))
            .apply(RequestOptions.errorOf(R.drawable.logo))
            .into(binding.makeOrderCardImage)

        binding.makeOrderCardName.text = itemData?.name
        binding.makeOrderCardRestaurant.text = itemData?.restaurant
        binding.makeOrderCardOriginalPrice.text = itemData?.originalPrice.toString() + resources.getString(R.string.currency)
        binding.makeOrderCardDiscountPrice.text = itemData?.discountPrice.toString() + resources.getString(R.string.currency)

        binding.makeOrderItemDescription.text = itemData?.description

        binding.makeOrderDeliveryAddress.text = userData?.address


        //check if user ordered today
        viewModel.checkIfUserOrderedToday()

        viewModel.orderedTodayCheckObserver().observe(viewLifecycleOwner){ orderedToday ->
            if(orderedToday){
                binding.makeOrderNote.text = getString(R.string.you_got_your_meal_deal_to_day)
                binding.makeOrderConfirmButton.isEnabled = false
                binding.makeOrderConfirmButton.text = getString(R.string.you_can_order_tomorrow)
            }
        }

        //set listener
        binding.makeOrderConfirmButton.setOnClickListener {
            viewModel.makeOrder(itemData?.id!!)
        }

        binding.makeOrderCancelButton.setOnClickListener {
            navController.popBackStack()
        }

        viewModel.getMakeOrderObserver().observe(viewLifecycleOwner){ confirmed ->
            if(confirmed){
                navController.popBackStack()
                Toast.makeText(requireContext(),
                    getString(R.string.order_placed_successfully), Toast.LENGTH_LONG).show()
            }
            else Toast.makeText(requireContext(),
                getString(R.string.error_making_order), Toast.LENGTH_LONG).show()
        }

    }
}