package com.policlinsaude.newfeature.components.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.policlinsaude.newfeature.R
import com.policlinsaude.newfeature.databinding.BottomSheetCommonBinding

class BottomSheetCommon(
    val title: String = "",
    val description: String = "",
    val buttonTitle: String? = null,
    val buttonCancel: String? = null,
    var list: MutableList<String> = arrayListOf(),
    var onClickListenerNext: ((listener: String?) -> Unit) = {},
    var onClickListenerClean: (() -> Unit) = {},
    var onItemSelected: ((item: String?) -> Unit) = {},
    var isVisibleClearFilter: Boolean? = true,
    var isVisibleButtonApply: Boolean? = true
): BottomSheetDialogFragment() {

    private var _binding: BottomSheetCommonBinding? = null
    val binding get() = _binding!!

    private var itemSelected: String? = null

    private val adapter by lazy {
        BottomSheetAdapter(requireContext())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = BottomSheetCommonBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupView()
        setupListeners()
    }

    private fun setupView() {
        with(binding) {
            textviewTitle.apply {
                text = title
                isVisible = title.isNotEmpty()
            }
            textviewDescription.apply {
                text = description
                isVisible = description.isNotEmpty()
            }
            textviewNext. text = buttonTitle ?: context?.getString(R.string.apply)
            cleanFilters.text = buttonCancel
            next.isVisible = isVisibleButtonApply ?: true
            cleanFilters.isVisible = isVisibleClearFilter ?: false

            recyclerView.adapter = adapter
            adapter.update(list)
            adapter.onSelectItemListener = {
                itemSelected = it
                onItemSelected.invoke(it)
            }
        }

        dialog?.setOnShowListener { dialog ->
            val d = dialog as BottomSheetDialog
            val bottomSheetInternal = d.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            bottomSheetInternal?.let {
                BottomSheetBehavior.from(it).peekHeight = binding.root.height
            }

        }
    }

    private fun setupListeners() {
        with(binding) {
            next.setOnClickListener {
                itemSelected?.let {
                    onClickListenerNext.invoke(itemSelected)
                    dismissAllowingStateLoss()
                }
            }

            cleanFilters.setOnClickListener {
                onClickListenerClean.invoke()
                dismissAllowingStateLoss()
            }
        }
    }

    override fun isCancelable() = false


}