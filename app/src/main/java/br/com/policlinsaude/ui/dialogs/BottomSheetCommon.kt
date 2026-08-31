package br.com.policlinsaude.ui.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import br.com.policlinsaude.R
import br.com.policlinsaude.databinding.BottomSheetCommonBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

/**
 * BottomSheet compartilhado para seleção de itens (anos, meses, filtros).
 * Migrado/adaptado de `_legacy/.../otherFeatures/components/bottomsheet/BottomSheetCommon.kt`.
 */
class BottomSheetCommon(
    val title: String = "",
    val description: String = "",
    val buttonTitle: String? = null,
    val buttonCancel: String? = null,
    var list: MutableList<String> = arrayListOf(),
    var onClickListenerNext: ((String?) -> Unit) = {},
    var onClickListenerClean: () -> Unit = {},
    var onItemSelected: ((String?) -> Unit) = {},
    var isVisibleClearFilter: Boolean = false,
    var isVisibleButtonApply: Boolean = true
) : BottomSheetDialogFragment() {

    private var _binding: BottomSheetCommonBinding? = null
    private val binding get() = _binding!!

    private var itemSelected: String? = null

    private val adapter by lazy { BottomSheetAdapter(requireContext()) }

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
            textviewNext.text = buttonTitle ?: context?.getString(R.string.apply)
            cleanFilters.text = buttonCancel
            next.isVisible = isVisibleButtonApply
            cleanFilters.isVisible = isVisibleClearFilter

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

    override fun isCancelable(): Boolean = false
}