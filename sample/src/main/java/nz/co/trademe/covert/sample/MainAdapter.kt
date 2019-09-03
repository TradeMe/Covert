package nz.co.trademe.covert.sample

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import nz.co.trademe.covert.Covert
import nz.co.trademe.covert.sample.viewholder.SwipeableViewHolder

class MainAdapter(
        private val covert: Covert
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        SwipeableViewHolder(
                itemView = LayoutInflater.from(viewGroup.context).inflate(R.layout.viewholder_swipeable, viewGroup, false))


    override fun getItemCount(): Int = 50

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, index: Int) {
        covert.drawCornerFlag(viewHolder)

        (viewHolder as? SwipeableViewHolder)?.apply(SwipeableViewHolder::bind)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int, payloads: List<Any>) {
        // The following is an optimisation for Covert, allowing us to skip re-binding of
        // ViewHolders if only drawing the child
        if (!payloads.contains(Covert.SKIP_FULL_BIND_PAYLOAD)) {
            onBindViewHolder(holder, position)
        }
    }
}
