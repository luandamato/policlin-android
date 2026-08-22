package br.com.policlinsaude.ownNetwork.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.policlinsaude.R
import br.com.policlinsaude.core.helper.getBitmapFromImage
import br.com.policlinsaude.databinding.CustomViewInfoMedicalGuideListBinding
import br.com.policlinsaude.databinding.ListItemOwnNetworkBinding
import br.com.policlinsaude.medicalGuideList.view.adapter.MedicalGuideListAdapter
import br.com.policlinsaude.medicalGuideList.view.adapter.MedicalGuideQualificationAdapter
import br.com.policlinsaude.model.PresentationEstablishment
import br.com.policlinsaude.model.PresentationQualification
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent

class OwnNetworkAdapter(
    private val onItemClickListener: OnItemClickListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TYPE_ITEM = 0
        private const val TYPE_QUALIFICATION = 1
    }

    var list: MutableList<PresentationEstablishment> = mutableListOf()
    var qualifications: MutableList<PresentationQualification> = mutableListOf()

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int
    ) {
        when (holder) {
            is OwnNetworkViewHolder -> {
                holder.format(list[position])
            }

            is MedicalGuideListAdapter.MedicalGuideListFooterViewHolder -> {
                holder.format()
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {

        return if (viewType == TYPE_ITEM) {

            OwnNetworkViewHolder(
                ListItemOwnNetworkBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )

        } else {

            val binding = CustomViewInfoMedicalGuideListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )

            MedicalGuideListAdapter.MedicalGuideListFooterViewHolder(
                binding,
                qualifications
            )
        }
    }

    override fun getItemCount(): Int {
        return list.size + 1
    }

    override fun getItemViewType(position: Int): Int {
        return if (position < list.size) {
            TYPE_ITEM
        } else {
            TYPE_QUALIFICATION
        }
    }

    fun setEstablishments(
        establishments: MutableList<PresentationEstablishment>,
        qualifications: MutableList<PresentationQualification>
    ) {
        list.clear()
        list.addAll(establishments)

        this.qualifications.clear()
        this.qualifications.addAll(qualifications)

        notifyDataSetChanged()
    }

    interface OnItemClickListener {
        fun onItemClick(establishment: PresentationEstablishment)
    }

    inner class OwnNetworkViewHolder(
        private val binding: ListItemOwnNetworkBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun format(establishment: PresentationEstablishment) {

            val flexBoxLayoutManager =
                FlexboxLayoutManager(binding.root.context).apply {
                    flexDirection = FlexDirection.ROW
                    flexWrap = FlexWrap.WRAP
                    justifyContent = JustifyContent.FLEX_START
                }

            binding.qualificationsRecyclerView.apply {
                adapter = MedicalGuideQualificationAdapter(
                    establishment.qualifications
                )
                layoutManager = flexBoxLayoutManager
            }

            binding.textViewName.text = establishment.title
            binding.textViewSubtitle.text = establishment.subTitle

            binding.textViewTypeEstablishment.text =
                binding.root.context.getString(
                    R.string.msg_type_establishment,
                    establishment.type
                )

            binding.textViewAddress.text =
                binding.root.context.getString(
                    R.string.msg_address_format,
                    establishment.publicPlace,
                    establishment.number,
                    establishment.complement,
                    establishment.neighborhood,
                    establishment.zipCode,
                    establishment.city,
                    establishment.state
                )

            binding.textViewPhoneOne.text = establishment.phoneOne
            binding.textViewPhoneTwo.text = establishment.phoneTwo

            binding.textViewPhoneOne.visibility =
                if (establishment.phoneOne.isEmpty()) {
                    View.GONE
                } else {
                    View.VISIBLE
                }

            binding.textViewPhoneTwo.visibility =
                if (establishment.phoneTwo.isEmpty()) {
                    View.GONE
                } else {
                    View.VISIBLE
                }

            binding.establishmentContainer.setOnClickListener {
                onItemClickListener.onItemClick(establishment)
            }

            try {
                binding.imageViewFront.setImageBitmap(
                    establishment.photoFront.getBitmapFromImage()
                )
                binding.imageViewFront.visibility = View.VISIBLE
            } catch (exception: Exception) {
                binding.imageViewFront.visibility = View.INVISIBLE
            }
        }
    }
}