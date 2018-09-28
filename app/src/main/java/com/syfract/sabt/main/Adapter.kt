package com.syfract.sabt.main

import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import com.syfract.sabt.Model
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import com.syfract.sabt.R

/**
 * Created by shayan4shayan on 4/3/18.
 *
 */
class Adapter(var apps: List<Model.Application>, private val listener: OnListClickListener) : RecyclerView.Adapter<Adapter.ViewHolder>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val holder = ViewHolder(LayoutInflater.from(p0.context).inflate(R.layout.item_list_application_small, p0, false))

        holder.itemView.setOnClickListener { listener.onItemClicked(apps[holder.adapterPosition]) }
        holder.action.setOnClickListener { listener.onItemActionClicked(apps[holder.adapterPosition]) }

        return holder
    }

    override fun getItemCount() = apps.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val app = apps[position]
        holder.name.text = app.packageName
        holder.versionName.text = app.versionName
        Picasso.get().load(app.image).into(holder.image)
        holder.action.text = when {
            !app.isInstalled -> "Install"
            app.isUpdate -> "Update"
            else -> {
                holder.action.visibility = View.GONE
                ""
            }
        }
        Picasso.get().load(app.image).into(holder.image)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        var image: ImageView = itemView.findViewById(R.id.img)
        var name: TextView = itemView.findViewById(R.id.textName)
        var versionName: TextView = itemView.findViewById(R.id.textVersion)
        var action: Button = itemView.findViewById(R.id.btnAction)

    }

    interface OnListClickListener {
        fun onItemClicked(app: Model.Application)
        fun onItemActionClicked(app: Model.Application)
    }
}