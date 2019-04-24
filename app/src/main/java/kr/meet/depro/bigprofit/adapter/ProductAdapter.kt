package kr.meet.depro.bigprofit.adapter

import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.product_item.view.*
import kr.meet.depro.bigprofit.R
import kr.meet.depro.bigprofit.model.Product

class ProductAdapter : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    var products: MutableList<Product> = mutableListOf(Product("초코우유", "1,500원", true, "CU", "http://gs25appimg.gsretail.com/imgsvr/item/GD_8809490180108_001.jpg"),
            Product("바나나맛우유", "1,300원", true, "GS25", "http://gs25appimg.gsretail.com/imgsvr/item/GD_8801056096656_003.jpg"),
            Product("파워에이드", "1,800원", true, "7-ELEVEN", "http://gs25appimg.gsretail.com/imgsvr/item/GD_8801019608988_001.jpg"),
            Product("붕어싸만코", "1,000원", false, "GS25", "http://gs25appimg.gsretail.com/imgsvr/item/GD_8801094793104_001.jpg"))

    //임시로 하드코딩함
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ProductViewHolder {
        return ProductViewHolder(p0)
    }

    override fun getItemCount(): Int {
        return products.size
    }

    override fun onBindViewHolder(productViewHolder: ProductViewHolder, position: Int) {
        products[position].let { product ->
            with(productViewHolder) {
                pdName.text = product.pdName
                price.text = product.price
                csName.text = product.csName
                when (csName.text) {
                    "GS25" -> csName.setBackgroundColor(ContextCompat.getColor(csName.context, R.color.gsRed))
                    "CU" -> csName.setBackgroundColor(ContextCompat.getColor(csName.context, R.color.cuPurple))
                    "7-ELEVEN" -> csName.setBackgroundColor(ContextCompat.getColor(csName.context, R.color.sevenGreen))
                }
                Glide.with(itemView).load(product.imgUrl).into(itemView.imageView)


            }
        }
    }

    inner class ProductViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.product_item, parent, false)) {
        val pdName = itemView.item_pdName
        val price = itemView.item_price
        val csName = itemView.item_csName
    }
}