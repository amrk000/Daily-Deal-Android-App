package com.amrk000.dailydeal.view.fragments

import android.R
import android.app.Activity
import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import com.amrk000.dailydeal.Model.ItemData
import com.amrk000.dailydeal.databinding.FragmentMealEditorBinding
import com.amrk000.dailydeal.util.ImageBase64Converter
import com.amrk000.dailydeal.util.TextFieldValidator
import com.amrk000.dailydeal.viewModel.MealEditorViewModel
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.google.android.material.chip.Chip
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.gson.Gson


class MealEditor : Fragment() {
    private lateinit var binding: FragmentMealEditorBinding
    private lateinit var viewModel: MealEditorViewModel
    private lateinit var navController: NavController

    private var itemData: ItemData? = null

    private lateinit var image: Uri
    private lateinit var imagePicker: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMealEditorBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //view model init
        viewModel = ViewModelProvider(this).get(MealEditorViewModel::class.java)

        //nav controller init
        navController = Navigation.findNavController(view)

        imagePicker = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult(),
            ActivityResultCallback<ActivityResult> { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    if(result.data!=null){
                        image = result.data?.data!!
                        Glide.with(requireContext())
                            .load(image)
                            .centerCrop()
                            .transition(DrawableTransitionOptions.withCrossFade(500))
                            .into(binding.mealEditorMealImage)
                    }
                }
            })

        //get passed item data
        if(arguments!=null) itemData = Gson().fromJson(arguments?.getString("passedData"), ItemData::class.java)

        //check data
        if(itemData == null) {
            //Mode: Create
            binding.mealEditorTitle.text = getString(com.amrk000.dailydeal.R.string.create_meal)
            binding.mealEditorSaveButton.text = getString(com.amrk000.dailydeal.R.string.create)
            binding.mealEditorDeleteButton.visibility = View.GONE

            binding.mealEditorSaveButton.setOnClickListener {
                if(allValid()){

                    val day = when(binding.chipGroup2.checkedChipId){
                        com.amrk000.dailydeal.R.id.dashboard_chip_SATURDAY -> "SATURDAY"
                        com.amrk000.dailydeal.R.id.dashboard_chip_SUNDAY -> "SUNDAY"
                        com.amrk000.dailydeal.R.id.dashboard_chip_MONDAY -> "MONDAY"
                        com.amrk000.dailydeal.R.id.dashboard_chip_TUESDAY -> "TUESDAY"
                        com.amrk000.dailydeal.R.id.dashboard_chip_WEDNESDAY -> "WEDNESDAY"
                        com.amrk000.dailydeal.R.id.dashboard_chip_THURSDAY -> "THURSDAY"
                        com.amrk000.dailydeal.R.id.dashboard_chip_FRIDAY -> "FRIDAY"
                        else -> "SATURDAY"
                    }

                    var imageBase64 = ""

                    if(::image.isInitialized) imageBase64 = ImageBase64Converter.encode(requireContext(), image, false)

                    viewModel.createMeal(
                        ItemData(
                            name= binding.mealEditorMealNameField.editText?.text.toString(),
                            description = binding.mealEditorMealDescField.editText?.text.toString(),
                            restaurant = binding.mealEditorMealRestaurantField.editText?.text.toString(),
                            image = imageBase64,
                            day = day.uppercase(),
                            originalPrice = binding.mealEditorMealOriginalPriceField.editText?.text.toString().toDouble(),
                            discountPrice = binding.mealEditorMealDiscountPriceField.editText?.text.toString().toDouble()
                        )
                    )

                }
            }

        }
        else {
            //Mode: Edit
            binding.mealEditorTitle.text = getString(com.amrk000.dailydeal.R.string.edit_meal)
            binding.mealEditorSaveButton.text = getString(com.amrk000.dailydeal.R.string.update)

            //render data
            Glide.with(requireContext())
                .load(ImageBase64Converter.decode(requireContext() ,itemData?.image.toString()))
                .centerCrop()
                .transition(DrawableTransitionOptions.withCrossFade(500))
                .apply(RequestOptions.errorOf(com.amrk000.dailydeal.R.drawable.logo))
                .into(binding.mealEditorMealImage)

            for(childView in binding.chipGroup2.children){
                val chip = childView as Chip
                if(chip.text.toString().uppercase() == itemData?.day){
                    chip.isChecked = true
                    break
                }
            }

            binding.mealEditorMealNameField.editText?.setText(itemData?.name)
            binding.mealEditorMealDescField.editText?.setText(itemData?.description)
            binding.mealEditorMealRestaurantField.editText?.setText(itemData?.restaurant)
            binding.mealEditorMealOriginalPriceField.editText?.setText(itemData?.originalPrice.toString())
            binding.mealEditorMealDiscountPriceField.editText?.setText(itemData?.discountPrice.toString())

            //set listener
            binding.mealEditorSaveButton.setOnClickListener {
                if(allValid() && viewModel.canEditMeal()){

                    itemData?.day = view.findViewById<Chip>(binding.chipGroup2.checkedChipId).text.toString()

                    if(::image.isInitialized) itemData?.image = ImageBase64Converter.encode(requireContext(), image, false)

                    itemData?.name = binding.mealEditorMealNameField.editText?.text.toString()
                    itemData?.description = binding.mealEditorMealDescField.editText?.text.toString()
                    itemData?.restaurant = binding.mealEditorMealRestaurantField.editText?.text.toString()
                    itemData?.originalPrice = binding.mealEditorMealOriginalPriceField.editText?.text.toString().toDouble()
                    itemData?.discountPrice = binding.mealEditorMealDiscountPriceField.editText?.text.toString().toDouble()

                    viewModel.updateMeal(itemData!!)

                }
            }

            binding.mealEditorDeleteButton.setOnClickListener {
                if(viewModel.canDeleteMeal()){
                    val alertDialogBuilder = MaterialAlertDialogBuilder(requireContext())
                    alertDialogBuilder.setCancelable(true)
                    alertDialogBuilder.setTitle(getString(com.amrk000.dailydeal.R.string.delete_this_meal))
                    alertDialogBuilder.setPositiveButton(getString(R.string.yes)) { dialog, which ->
                        viewModel.deleteMeal(itemData!!)
                    }
                    alertDialogBuilder.setNegativeButton(getString(R.string.cancel)) { dialog, which ->
                        dialog.dismiss()
                    }
                    alertDialogBuilder.create()
                    alertDialogBuilder.show()
                }
            }

        }

        binding.mealEditorEditMealImageButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            imagePicker.launch(intent)
        }

        binding.mealEditorCancelButton.setOnClickListener {
            navController.popBackStack()
        }

        viewModel.getCreateObserver().observe(viewLifecycleOwner) { created ->
            if(created){
                navController.popBackStack()
                Toast.makeText(requireContext(),
                    getString(com.amrk000.dailydeal.R.string.meal_created_successfully), Toast.LENGTH_LONG).show()
            }
            else Toast.makeText(requireContext(),
                getString(com.amrk000.dailydeal.R.string.error_creating_meal), Toast.LENGTH_LONG).show()
        }

        viewModel.getUpdateObserver().observe(viewLifecycleOwner) { updated ->
            if(updated){
                navController.popBackStack()
                Toast.makeText(requireContext(),
                    getString(com.amrk000.dailydeal.R.string.meal_updated_successfully), Toast.LENGTH_LONG).show()
            }
            else Toast.makeText(requireContext(),
                getString(com.amrk000.dailydeal.R.string.error_updating_meal), Toast.LENGTH_LONG).show()
        }

        viewModel.getDeleteObserver().observe(viewLifecycleOwner) { deleted ->
            if(deleted){
                navController.popBackStack()
                Toast.makeText(requireContext(),
                    getString(com.amrk000.dailydeal.R.string.meal_deleted_successfully), Toast.LENGTH_LONG).show()
            }
            else Toast.makeText(requireContext(),
                getString(com.amrk000.dailydeal.R.string.error_deleting_meal), Toast.LENGTH_LONG).show()
        }

    }

    fun allValid(): Boolean{
        var allValid = true

        //Name Field
        if(binding.mealEditorMealNameField.editText?.text.toString().isEmpty()){
            binding.mealEditorMealNameField.error =
                getString(com.amrk000.dailydeal.R.string.field_can_t_be_empty)
            allValid = false
        }
        else binding.mealEditorMealNameField.error = null

        //Description Field
        if(binding.mealEditorMealDescField.editText?.text.toString().isEmpty()){
            binding.mealEditorMealDescField.error = getString(com.amrk000.dailydeal.R.string.field_can_t_be_empty)
            allValid = false
        }
        else binding.mealEditorMealDescField.error = null

        //Restaurant Field
        if(binding.mealEditorMealRestaurantField.editText?.text.toString().isEmpty()){
            binding.mealEditorMealRestaurantField.error = getString(com.amrk000.dailydeal.R.string.field_can_t_be_empty)
            allValid = false
        }
        else binding.mealEditorMealRestaurantField.error = null

        //Original price Field
        if(binding.mealEditorMealOriginalPriceField.editText?.text.toString().isEmpty()){
            binding.mealEditorMealOriginalPriceField.error = getString(com.amrk000.dailydeal.R.string.field_can_t_be_empty)
            allValid = false
        }
        else binding.mealEditorMealOriginalPriceField.error = null

        //Discount price Field
        if(binding.mealEditorMealDiscountPriceField.editText?.text.toString().isEmpty()){
            binding.mealEditorMealDiscountPriceField.error = getString(com.amrk000.dailydeal.R.string.field_can_t_be_empty)
            allValid = false
        }
        else binding.mealEditorMealDiscountPriceField.error = null

        return allValid
    }

}