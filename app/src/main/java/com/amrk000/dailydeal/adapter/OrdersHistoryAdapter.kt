package com.amrk000.dailydeal.adapter

import DateTimeHelper
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.amrk000.dailydeal.Model.UserOrderData
import com.amrk000.dailydeal.R
import com.amrk000.dailydeal.util.ImageBase64Converter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions

class OrdersHistoryAdapter (private val context: Context, private val showDayLabel: Boolean) : RecyclerView.Adapter<OrdersHistoryAdapter.ViewHolder>() {

    private val data = ArrayList<UserOrderData>()

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name = view.findViewById<TextView>(R.id.orders_item_Name)
        val restaurant = view.findViewById<TextView>(R.id.orders_item_restaurant)
        val day = view.findViewById<TextView>(R.id.orders_item_day)
        val date = view.findViewById<TextView>(R.id.orders_item_date)
        val image = view.findViewById<ImageView>(R.id.orders_item_image)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.orders_item_card_layout, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val order = data[position]
        holder.name.text = order.name
        holder.restaurant.text = order.restaurant
        holder.date.text = DateTimeHelper.utcToLocalFormatted(order.date)
        holder.day.text = order.day

        if(!showDayLabel) holder.day.visibility = View.INVISIBLE

        Glide.with(context)
            .load(ImageBase64Converter.decode(context, order.image))
            .centerCrop()
            .transition(DrawableTransitionOptions.withCrossFade(500))
            .apply(RequestOptions.errorOf(R.drawable.logo))
            .into(holder.image);

    }

    fun setData(data : ArrayList<UserOrderData>) {
        this.data.clear()
        this.data.addAll(data)
        notifyDataSetChanged()
    }


}