package br.com.policlinsaude.ui.activities.ownNetwork

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.policlinsaude.data.models.PresentationEstablishment
import br.com.policlinsaude.data.models.PresentationQualification
import br.com.policlinsaude.databinding.FragmentOwnNetworkBinding

/**
 * Fragment de la lista de establecimientos de una página (tab) de Rede Propia.
 *
 * Migrado de `_legacy/.../ownNetwork/view/OwnNetworkFragment.kt`. No tiene
 * ViewModel propio: recibe los datos de la página y delega el click a la
 * Activity (que coordina via OwnNetworkViewModel y realiza la navegación).
 */
class OwnNetworkFragment : Fragment() {

    companion object {
        fun newInstance(
            establishments: List<PresentationEstablishment>,
            qualifications: List<PresentationQualification>,
            onItemClick: (PresentationEstablishment) -> Unit
        ): OwnNetworkFragment {
            val fragment = OwnNetworkFragment()
            fragment.establishments = establishments.toMutableList()
            fragment.qualifications = qualifications.toMutableList()
            fragment.onItemClick = onItemClick
            return fragment
        }
    }

    private lateinit var establishments: MutableList<PresentationEstablishment>
    private lateinit var qualifications: MutableList<PresentationQualification>
    private lateinit var onItemClick: (PresentationEstablishment) -> Unit

    private lateinit var binding: FragmentOwnNetworkBinding

    private val adapter by lazy {
        OwnNetworkAdapter { establishment ->
            onItemClick(establishment)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOwnNetworkBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@OwnNetworkFragment.adapter
        }
        adapter.update(establishments, qualifications)
    }
}