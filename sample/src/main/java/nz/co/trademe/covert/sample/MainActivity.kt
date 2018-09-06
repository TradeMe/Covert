package nz.co.trademe.covert.sample

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import nz.co.trademe.covert.Covert
import nz.co.trademe.covert.sample.repository.DummySwipeRepository
import nz.co.trademe.covert.sample.viewholder.SwipeableViewHolder

class MainActivity : AppCompatActivity() {

    private val covertConfig = Covert.Config(
            iconRes = R.drawable.ic_star_border_black_24dp,
            iconDefaultColorRes = android.R.color.black,
            actionColorRes = R.color.colorPrimary
    )

    private val repository = DummySwipeRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)

        val covert = Covert.with(covertConfig)
                .setIsActiveCallback {
                    it is SwipeableViewHolder && repository.isActive(it.adapterPosition)
                }
                .doOnSwipe { viewHolder, _ ->
                    repository.toggleActiveState(viewHolder.adapterPosition)
                }
                .attachTo(recyclerView)

        recyclerView.adapter = MainAdapter(covert)
        recyclerView.layoutManager = LinearLayoutManager(this)
    }
}
