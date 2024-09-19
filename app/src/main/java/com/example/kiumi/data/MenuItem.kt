package com.example.kiumi.data

import android.os.Parcel
import android.os.Parcelable

data class MenuItem(
    val name: String,
    val price: String,
    val calories: String,
    val imageResourceId: Int,
    val isNew: Boolean
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readInt(),
        parcel.readByte() != 0.toByte()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(price)
        parcel.writeString(calories)
        parcel.writeInt(imageResourceId)
        parcel.writeByte(if (isNew) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<MenuItem> {
        override fun createFromParcel(parcel: Parcel): MenuItem {
            return MenuItem(parcel)
        }

        override fun newArray(size: Int): Array<MenuItem?> {
            return arrayOfNulls(size)
        }
    }
}

data class ProposalMenuItem(
    val name: String,
    val price: String,
    val imageResourceId: Int,
    val isNew: Boolean
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readInt(),
        parcel.readByte() != 0.toByte()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(price)
        parcel.writeInt(imageResourceId)
        parcel.writeByte(if (isNew) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ProposalMenuItem> {
        override fun createFromParcel(parcel: Parcel): ProposalMenuItem {
            return ProposalMenuItem(parcel)
        }

        override fun newArray(size: Int): Array<ProposalMenuItem?> {
            return arrayOfNulls(size)
        }
    }
}

data class OrderItem(
    val menuItem: MenuItem,
    var quantity: Int = 1,
    val isSet: Boolean = false,
    val isLargeSet: Boolean = false,
    val side: String = "",
    val drink: String = "",
    val totalPrice: String = "",
    val totalCalories: String = ""
)

data class ProposalOrderItem(
    val menuItem: ProposalMenuItem,
    var quantity: Int = 1,
    val isSet: Boolean = false,
    val isLargeSet: Boolean = false,
    val side: String = "",
    val drink: String = "",
    val totalPrice: String = "",
)
