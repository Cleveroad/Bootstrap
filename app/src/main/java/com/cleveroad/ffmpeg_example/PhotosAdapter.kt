package com.cleveroad.ffmpeg_example

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.cleveroad.bootstrap.kotlin.R
import com.cleveroad.bootstrap.kotlin_core.ui.adapter.BaseRecyclerViewAdapter
import com.cleveroad.bootstrap.kotlin_ffmpeg_thumbnails.model.Thumbnail

class PhotosAdapter(context: Context, data: List<Thumbnail> = listOf()) :
        BaseRecyclerViewAdapter<Thumbnail, PhotosAdapter.PhotosViewHolder>(context, data) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = PhotosViewHolder.newInstance(inflater, parent, R.layout.item_photo)

    override fun onBindViewHolder(holder: PhotosViewHolder, position: Int) = holder.bind(getItem(position))

    class PhotosViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        companion object {
            fun newInstance(inflater: LayoutInflater, parent: ViewGroup?, layout: Int) =
                    PhotosViewHolder(inflater.inflate(layout, parent, false))
        }

        private var ivPhoto = itemView.findViewById<ImageView>(R.id.ivPhoto)

        fun bind(thumbnail: Thumbnail) {
            ivPhoto.setImageBitmap(thumbnail.bitmap)
        }
    }
}