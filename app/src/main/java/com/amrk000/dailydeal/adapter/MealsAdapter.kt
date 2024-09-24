package com.amrk000.dailydeal.adapter

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.amrk000.dailydeal.Model.ItemData
import com.amrk000.dailydeal.R
import com.amrk000.dailydeal.util.ImageBase64Converter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions

class MealsAdapter (private val context: Context, private val showDayLabel: Boolean) : RecyclerView.Adapter<MealsAdapter.ViewHolder>() {

    private val data = ArrayList<ItemData>()
    private lateinit var onItemClickListener: OnItemClickListener

    interface OnItemClickListener {
        fun onItemClick(position: Int, data: ItemData)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name = view.findViewById<TextView>(R.id.item_card_Name)
        val restaurant = view.findViewById<TextView>(R.id.item_card_restaurant)
        val originalPrice = view.findViewById<TextView>(R.id.item_card_originalPrice)
        val discountPrice = view.findViewById<TextView>(R.id.item_card_discountPrice)
        val day = view.findViewById<TextView>(R.id.item_card_day)
        val image = view.findViewById<ImageView>(R.id.item_card_image)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_card_layout, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val meal = data[position]
        holder.name.text = meal.name
        holder.restaurant.text = meal.restaurant
        holder.originalPrice.text = meal.originalPrice.toString() + context.resources.getString(R.string.currency)
        if(meal.discountPrice > 0) holder.discountPrice.text = meal.discountPrice.toString() + context.resources.getString(R.string.currency)
        else holder.discountPrice.text = context.getString(R.string.free)
        holder.day.text = meal.day

        if(!showDayLabel) holder.day.visibility = View.INVISIBLE

        Glide.with(context)
            .load(ImageBase64Converter.decode(context, meal.image))
            .centerCrop()
            .transition(DrawableTransitionOptions.withCrossFade(500))
            .apply(RequestOptions.errorOf(R.drawable.logo))
            .into(holder.image);

        holder.itemView.setOnClickListener {
            if(::onItemClickListener.isInitialized)
                onItemClickListener.onItemClick(position, data[position])
        }

    }

    fun setData(data : ArrayList<ItemData>) {
        this.data.clear()
        this.data.addAll(data)
        notifyDataSetChanged()
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener){
        this.onItemClickListener = onItemClickListener
    }

}