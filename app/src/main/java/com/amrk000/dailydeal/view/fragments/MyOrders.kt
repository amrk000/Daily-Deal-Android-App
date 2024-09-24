package com.amrk000.dailydeal.view.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.amrk000.dailydeal.R
import com.amrk000.dailydeal.adapter.MealsAdapter
import com.amrk000.dailydeal.adapter.OrdersHistoryAdapter
import com.amrk000.dailydeal.databinding.FragmentHomeBinding
import com.amrk000.dailydeal.databinding.FragmentMyOrdersBinding
import com.amrk000.dailydeal.viewModel.HomeViewModel
import com.amrk000.dailydeal.viewModel.MyOrdersViewModel
import com.google.gson.Gson
import kotlin.math.log

class MyOrders : Fragment() {
    private lateinit var binding: FragmentMyOrdersBinding
    private lateinit var viewModel: MyOrdersViewModel
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMyOrdersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //view model init
        viewModel = ViewModelProvider(this).get(MyOrdersViewModel::class.java)

        //nav controller init
        navController = Navigation.findNavController(view)

        //init orders recycler View
        val ordersAdapter = OrdersHistoryAdapter(requireContext(), true)
        binding.ordersHistoryRecyclerView.setLayoutManager(LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false))
        binding.ordersHistoryRecyclerView.setAdapter(ordersAdapter)

        //Load data
        viewModel.getUserOrdersHistory()

        viewModel.getUserOrdersHistoryObserver().observe(viewLifecycleOwner){ orders ->
            if(orders.isNotEmpty()) ordersAdapter.setData(orders)
            else binding.ordersHistoryNoData.visibility = View.VISIBLE
        }

    }

}