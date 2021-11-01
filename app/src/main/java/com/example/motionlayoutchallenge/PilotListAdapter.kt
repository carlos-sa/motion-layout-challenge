package com.example.motionlayoutchallenge

import android.app.Activity
import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.position_list_card_item.view.*

data class PilotInfo(val pilotName: String, val time: String, val picture: String)


class PilotListAdapter(
        private val pilotList: MutableList<PilotInfo>
) : RecyclerView.Adapter<PilotListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.position_list_card_item, parent, false)
        view.id = View.generateViewId()
        return ViewHolder(view)
    }

    override fun getItemCount() = pilotList.size


    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = pilotList[position]
        holder.itemView.tag =
                item.pilotName //set Tag for each binded item to identifies it in fragent/viewModel
        holder.pilotName.text = item.pilotName
        holder.pilotPicture.background = getImageResource(item.picture, holder.mView.context)
        holder.pilotTime.text = "1:34:36.552"

    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun getImageResource(name: String, context: Context): Drawable? {
        val resourceId = context.resources.getIdentifier(name, "drawable",
                context.getPackageName());
        return ActivityCompat.getDrawable(context,resourceId)
    }

    inner class ViewHolder(val mView: View) :
            RecyclerView.ViewHolder(mView) {

        val pilotName: TextView = mView.pilot_name_tv
        val pilotTime: TextView = mView.pilot_time_tv
        val pilotPicture: ImageView = mView.pilot_picture_iv

    }


}
