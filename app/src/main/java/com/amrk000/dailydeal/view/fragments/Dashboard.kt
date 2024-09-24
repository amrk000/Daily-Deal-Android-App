package com.amrk000.dailydeal.view.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.amrk000.dailydeal.Model.ItemData
import com.amrk000.dailydeal.R
import com.amrk000.dailydeal.adapter.MealsAdapter
import com.amrk000.dailydeal.databinding.FragmentDashboardBinding
import com.amrk000.dailydeal.viewModel.DashboardViewModel
import com.google.gson.Gson


class Dashboard : Fragment() {
    private lateinit var binding: FragmentDashboardBinding
    private lateinit var viewModel: DashboardViewModel
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //view model init
        viewModel = ViewModelProvider(this).get(DashboardViewModel::class.java)

        //nav controller init
        navController = Navigation.findNavController(view)

        //Load User Privileges
        val userName = viewModel.getUserData()?.name
        val userRole = viewModel.getUserData()?.role

        binding.dashboardSecondLabel.text = getString(R.string.user_role, userName, userRole)

        //init meals recycler View
        val mealsAdapter = MealsAdapter(requireContext(), true)
        binding.dashboardRecyclerView.setLayoutManager(GridLayoutManager(context,2, LinearLayoutManager.VERTICAL, false))
        binding.dashboardRecyclerView.setAdapter(mealsAdapter)

        //Load Data
        viewModel.getAllItems()

        viewModel.getItemsDataObserver().observe(viewLifecycleOwner){ items ->
            if(items.isNotEmpty()) mealsAdapter.setData(items)
            else binding.dashboardNoData.visibility = View.VISIBLE
        }

        //UI Listeners
        binding.chipGroup.setOnCheckedChangeListener { group, checkedId ->
            when(checkedId){
                R.id.dashboard_chip_all -> viewModel.getAllItems()
                R.id.dashboard_chip_SATURDAY -> viewModel.getItemsByDay("SATURDAY")
                R.id.dashboard_chip_SUNDAY -> viewModel.getItemsByDay("SUNDAY")
                R.id.dashboard_chip_MONDAY -> viewModel.getItemsByDay("MONDAY")
                R.id.dashboard_chip_TUESDAY -> viewModel.getItemsByDay("TUESDAY")
                R.id.dashboard_chip_WEDNESDAY -> viewModel.getItemsByDay("WEDNESDAY")
                R.id.dashboard_chip_THURSDAY -> viewModel.getItemsByDay("THURSDAY")
                R.id.dashboard_chip_FRIDAY -> viewModel.getItemsByDay("FRIDAY")

            }
        }

        mealsAdapter.setOnItemClickListener(object: MealsAdapter.OnItemClickListener{
            override fun onItemClick(position: Int, data: ItemData) {
                val bundle = Bundle()
                bundle.putString("passedData", Gson().toJson(data))
                navController.navigate(R.id.action_dashboard_to_mealEditor, bundle)
            }

        })

        binding.dashboardCreateMealButton.setOnClickListener {
            if(viewModel.canCreateMeal()) navController.navigate(R.id.action_dashboard_to_mealEditor)
            else Toast.makeText(context,
                getString(R.string.don_t_have_permission_to_create_meals, userRole), Toast.LENGTH_SHORT).show()
        }

    }

}