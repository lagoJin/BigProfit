package kr.meet.depro.bigprofit.model

import android.os.Parcel
import android.os.Parcelable

data class SearchItem(
    val category: String,
    val createTime: String,
    val event: Int,
    val imageUrl: String,
    val name: String,
    val price: Int,
    val seq: Int,
    val storeName: String
) : Parcelable {
    constructor(source: Parcel) : this(
        source.readString(),
        source.readString(),
        source.readInt(),
        source.readString(),
        source.readString(),
        source.readInt(),
        source.readInt(),
        source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(category)
        writeString(createTime)
        writeInt(event)
        writeString(imageUrl)
        writeString(name)
        writeInt(price)
        writeInt(seq)
        writeString(storeName)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<SearchItem> = object : Parcelable.Creator<SearchItem> {
            override fun createFromParcel(source: Parcel): SearchItem = SearchItem(source)
            override fun newArray(size: Int): Array<SearchItem?> = arrayOfNulls(size)
        }
    }
}