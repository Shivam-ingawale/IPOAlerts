package com.lasteyestudios.ipoalerts.tabs.allotment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.lasteyestudios.ipoalerts.data.models.availableallotmentmodel.AvailableAllotmentModel
import com.lasteyestudios.ipoalerts.databinding.AllotmentItemBinding
import com.lasteyestudios.ipoalerts.tabs.allotment.AllotmentAdapter.ItemAdapterViewHolder

class AllotmentAdapter(private val onAllotmentItemClicked: (companyId: String, companyName: String) -> Unit) :
    ListAdapter<AvailableAllotmentModel?, ItemAdapterViewHolder>(HomeAdapterDiffCallback) {

    object HomeAdapterDiffCallback : DiffUtil.ItemCallback<AvailableAllotmentModel?>() {
        override fun areItemsTheSame(
            oldItem: AvailableAllotmentModel,
            newItem: AvailableAllotmentModel,
        ): Boolean {
            return oldItem.company_id == newItem.company_id
        }

        override fun areContentsTheSame(
            oldItem: AvailableAllotmentModel,
            newItem: AvailableAllotmentModel,
        ): Boolean {
            return oldItem == newItem
        }

    }

    fun closingDate(s: String?): String {
        if (!s.isNullOrBlank()) {
            val i = s.indexOf("T")
            return s.substring(0, i)
        }
        return ""
    }

    inner class ItemAdapterViewHolder(private val binding: AllotmentItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: AvailableAllotmentModel?) {
            if (item?.company_id != null) {
                binding.apply {
                    closingDate.text = closingDate(item.closing_date)
                    companyName.text = item.companyname
                    faceValue.text = item.diff
                    priceRange.text = item.offer_price
                    issueSize.text = item.total_shares
                    secretary.text = item.COMPANY_SE
                    allotmentCard.setOnClickListener {
                        onAllotmentItemClicked(item.company_id, item.companyname!!)
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemAdapterViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .let { inflater -> AllotmentItemBinding.inflate(inflater, parent, false) }
        return ItemAdapterViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ItemAdapterViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}