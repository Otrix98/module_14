package com.example.module_14

import android.annotation.SuppressLint
import android.os.Build
import android.view.View
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_map.*
import org.threeten.bp.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

abstract class BaseMapHolder (
    override val containerView: View,
    onItemClicked: (position: Int) -> Unit
): RecyclerView.ViewHolder(containerView), LayoutContainer {


    init {
        containerView.setOnClickListener {
            onItemClicked(adapterPosition)
        }
    }



    private val formatter = org.threeten.bp.format.DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy:")
        .withZone(org.threeten.bp.ZoneId.systemDefault())

    protected fun bindMainInfo(
        id: Long,
        map: String,
        avatarLink: String,
        time: Instant
    ) {
        idTextView.text = "id : ${ id.toString()}"
        mapTextView.text = map
        timeTextView.text = formatter.format(time)


        Glide.with(itemView)
            .load(avatarLink)
            .error(R.drawable.ic_baseline_error)
            .into(imageView)
    }
}