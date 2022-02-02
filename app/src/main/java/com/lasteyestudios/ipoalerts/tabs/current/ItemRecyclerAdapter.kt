package com.lasteyestudios.ipoalerts.tabs.current

import android.content.Context
import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.lasteyestudios.ipoalerts.R
import com.lasteyestudios.ipoalerts.data.models.ipolistingmodel.Company
import com.lasteyestudios.ipoalerts.databinding.IpoCompanyItemBinding
import com.lasteyestudios.ipoalerts.utils.CURRENT_FRAGMENT_HORIZONTAL

class ItemRecyclerAdapter(
    private val context: Context,
    private val onItemClicked: (searchId: String, growwShortName: String, liked:Boolean) -> Unit,
    private val deleteWatchlistCompany: (deleteSymbol: String) -> Unit,
    private val addWatchlistCompany: (company: Company) -> Unit,
    private val from: String?,
) :
    ListAdapter<Company?, ItemRecyclerAdapter.ItemAdapterViewHolder>(HomeAdapterDiffCallback) {

    object HomeAdapterDiffCallback : DiffUtil.ItemCallback<Company?>() {
        override fun areItemsTheSame(oldItem: Company, newItem: Company): Boolean {
            return oldItem.growwShortName == newItem.growwShortName
        }

        override fun areContentsTheSame(oldItem: Company, newItem: Company): Boolean {
            return oldItem == newItem
        }

    }

    private fun rupeeFormatDecimal(value: String): String {
        value.replace(",", "")
        if (value == "" || value == "null" || value == "0") {
            return ""
        }
        if (value.contains(".")) {
            val f = value.indexOf(".")
            val dotAfterDigit = value.substring(f, value.length)
            val lastDigit = value[f - 1]
            var result = ""
            val len = f - 1
            var nDigits = 0
            for (i in len - 1 downTo 0) {
                result = value[i].toString() + result
                nDigits++
                if (nDigits % 2 == 0 && i > 0) {
                    result = ",$result"
                }
            }
            val temp = result + lastDigit + dotAfterDigit
            return "₹$temp"

        }
        return ""
    }

    fun rupeeFormat(value: String?): String? {
        if (value == "" || value == null) {
            return ""
        }
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
        val temp = result + lastDigit
        if (temp != null) {
            return "₹$temp"
        }
        return temp
    }

    inner class ItemAdapterViewHolder(private val binding: IpoCompanyItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Company?) {

            if (from == CURRENT_FRAGMENT_HORIZONTAL) {
                val totalWidth: Int = Resources.getSystem().displayMetrics.widthPixels
                val itemWidth = (totalWidth * 0.90) / 2
                val itemHeight = (itemWidth * 16.0) / 9
                val x = (itemHeight).toInt()
//                binding.root.layoutParams.height = x
                binding.root.layoutParams.width = itemWidth.toInt()
            }

            item?.let {
                if ((it.status != "LISTED" || it.liked)) {
                    binding.wishlistHeart.visibility = View.VISIBLE
                    if (it.liked) {
                        binding.wishlistHeart.setImageDrawable(ContextCompat.getDrawable(context,
                            R.drawable.ic_watch_list))
                    } else {
                        binding.wishlistHeart.setImageDrawable(ContextCompat.getDrawable(context,
                            R.drawable.ic_heart))
                    }
                    binding.wishlistHeart.setOnClickListener { view ->
                        it.liked = !it.liked
                        if (it.liked) {
                            addWatchlistCompany(item)
                        } else {
                            deleteWatchlistCompany(item.symbol.toString())
                        }
                        if (it.liked) {
                            binding.wishlistHeart.setImageDrawable(ContextCompat.getDrawable(context,
                                R.drawable.ic_watch_list))
                        } else {
                            binding.wishlistHeart.setImageDrawable(ContextCompat.getDrawable(context,
                                R.drawable.ic_heart))
                        }
                    }
                }
                binding.companyName.text = it.growwShortName
                if (it.minPrice != "" || it.minBidQuantity != "") {
                    val s = rupeeFormat(((it.minPrice?.toFloat()
                        ?: 1.0f) * (it.minBidQuantity?.toFloat() ?: 1.0f)).toInt().toString())
                    binding.minimumPrice.text = s

                } else {
                    binding.minimumPrice.text = context.getString(R.string.n_a)
                }

                if (it.issueSize != "") {
                    binding.issueSize.text = it.issueSize
                } else {
                    binding.issueSize.text = context.getString(R.string.n_a)
                }


                if (it.status == "LISTED") {
                    binding.offerDate.text = it.listingDate
                } else {
                    if (it.biddingStartDate != "") {
                        binding.offerDate.text = it.biddingStartDate
                    } else {
                        binding.offerDate.text = context.getString(R.string.n_a)
                    }
                }

                val range =
                    rupeeFormatDecimal(it.minPrice.toString()) + " - " + rupeeFormatDecimal(it.maxPrice.toString())
                if (range != " - ") {
                    binding.priceRange.text = range
                } else {
                    binding.priceRange.text = context.getString(R.string.n_a)
                }
                binding.priceRange.isSelected = true

                binding.ipoCard.setOnClickListener { _ ->
                    if (it.searchId != null && it.growwShortName != null) {
                        onItemClicked(it.searchId.toString(), it.growwShortName.toString(),
                        it.liked)
                    }
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