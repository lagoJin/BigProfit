package kr.meet.depro.bigprofit.adapter

import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.product_item.view.*
import kr.meet.depro.bigprofit.R
import kr.meet.depro.bigprofit.model.Product

class ProductAdapter(var products: ArrayList<Product>) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

/*    products: ArrayList<Product> = arrayListOf(
            Product("초코우유", 1500,1, "CU",  "http://gs25appimg.gsretail.com/imgsvr/item/GD_8809490180108_001.jpg"),
            Product("바나나맛우유", 1300, 1, "GS25", "http://gs25appimg.gsretail.com/imgsvr/item/GD_8801056096656_003.jpg"),
            Product("파워에이드", 1800, 2, "7-ELEVEN", "http://gs25appimg.gsretail.com/imgsvr/item/GD_8801019608988_001.jpg"),
            Product("붕어싸만코", 1000, 1, "GS25", "http://gs25appimg.gsretail.com/imgsvr/item/GD_8801094793104_001.jpg"),
            Product("붕어싸코", 1000, 1, "CU", "http://gs25appimg.gsretail.com/imgsvr/item/GD_8801094793104_001.jpg"),
            Product("붕어만코", 1000, 1, "GS25", "http://gs25appimg.gsretail.com/imgsvr/item/GD_8801094793104_001.jpg"),
    Product("붕어코", 1000, 1, "7-ELEVEN", "http://gs25appimg.gsretail.com/imgsvr/item/GD_8801094793104_001.jpg"))*/
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ProductViewHolder {
        return ProductViewHolder(p0)
    }

    override fun getItemCount(): Int {
        return products.size
    }

    override fun onBindViewHolder(productViewHolder: ProductViewHolder, position: Int) {
        products[position].let { product ->
            with(productViewHolder) {
                pdName.text = product.name
                price.text = product.price.toString()
                csName.text = product.storeName
                when (csName.text) {
                    "GS25(지에스25)" -> csName.setBackgroundColor(ContextCompat.getColor(csName.context, R.color.gsRed))
                    "CU(씨유)" -> csName.setBackgroundColor(ContextCompat.getColor(csName.context, R.color.cuPurple))
                    "7-ELEVEN(세븐일레븐)" -> csName.setBackgroundColor(ContextCompat.getColor(csName.context, R.color.sevenGreen))
                    "EMART24(이마트24)" -> csName.setBackgroundColor(ContextCompat.getColor(csName.context, R.color.emartYellow))
                    "MINISTOP(미니스톱)" -> csName.setBackgroundColor(ContextCompat.getColor(csName.context, R.color.ministopBlue))
                }
                Glide.with(itemView).load(product.imageUrl).into(itemView.imageView)


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