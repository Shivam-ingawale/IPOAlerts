package com.lasteyestudios.ipoalerts.tabs.current

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.lasteyestudios.ipoalerts.data.models.ipolistingmodel.Company
import com.lasteyestudios.ipoalerts.databinding.IpoCompanyItemBinding

class ItemRecyclerAdapter(private val onItemClicked: (searchId: String) -> Unit) :
    ListAdapter<Company?, ItemRecyclerAdapter.ItemAdapterViewHolder>(HomeAdapterDiffCallback) {

    object HomeAdapterDiffCallback : DiffUtil.ItemCallback<Company?>() {
        override fun areItemsTheSame(oldItem: Company, newItem: Company): Boolean {
            return oldItem.growwShortName == newItem.growwShortName
        }

        override fun areContentsTheSame(oldItem: Company, newItem: Company): Boolean {
            return oldItem == newItem
        }

    }

    fun rupeeFormat(value: String): String? {
        value.replace(",", "")
        val lastDigit = value[value.length - 1]
        var result = ""
        val len = value.length - 1
        var nDigits = 0
        for (i in len - 1 downTo 0) {
            result = value[i].toString() + result
            nDigits++
            if (nDigits % 2 == 0 && i > 0) {
                result = ",$result"
            }
        }
        return result + lastDigit
    }

    inner class ItemAdapterViewHolder(private val binding: IpoCompanyItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Company?) {
            item?.let {
                binding.companyName.text = it.growwShortName
                if (it.minPrice != "" || it.minBidQuantity != "") {
                    val s = "₹" + rupeeFormat(((it.minPrice?.toFloat()
                        ?: 1.0f) * (it.minBidQuantity?.toFloat() ?: 1.0f)).toInt().toString())
                    binding.minimumPrice.text = s

                } else {
                    binding.minimumPrice.text = "N/A"
                }

                if (it.issueSize != "") {
                    binding.issueSize.text = it.issueSize
                } else {
                    binding.issueSize.text = "N/A"
                }


                if (it.status == "LISTED") {
                    binding.offerDate.text = it.listingDate
                } else {
                    if (it.biddingStartDate != "") {
                        binding.offerDate.text = it.biddingStartDate
                    } else {
                        binding.offerDate.text = "N/A"
                    }
                }

                val range = it.minPrice + " - " + it.maxPrice
                if (range != " - ") {
                    binding.priceRange.text = range
                } else {
                    binding.priceRange.text = "N/A"
                }
                binding.ipoCard.setOnClickListener { _ ->
                    onItemClicked(it.searchId.toString())
                }
                Glide.with(binding.logoImage.context).load(it.logoUrl).centerCrop().transition(
                    DrawableTransitionOptions.withCrossFade(300)).into(binding.logoImage)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemAdapterViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .let { inflater -> IpoCompanyItemBinding.inflate(inflater, parent, false) }
        return ItemAdapterViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ItemAdapterViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}