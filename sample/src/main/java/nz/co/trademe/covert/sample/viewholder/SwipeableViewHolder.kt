package nz.co.trademe.covert.sample.viewholder

import android.support.v7.widget.RecyclerView
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.viewholder_swipeable.view.*

class SwipeableViewHolder(
        itemView: View
) : RecyclerView.ViewHolder(itemView) {

    fun bind() {
        Glide.with(itemView)
                .load("http://i.imgur.com/34vQcpZ.png")
                .apply(RequestOptions.circleCropTransform())
                .into(itemView.profileImageView)
    }
}