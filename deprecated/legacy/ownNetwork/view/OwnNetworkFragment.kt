package br.com.policlinsaude.ui.legacy.ownNetwork.view

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.policlinsaude.R
import br.com.policlinsaude.model.PresentationEstablishment
import br.com.policlinsaude.model.PresentationQualification
import br.com.policlinsaude.ownNetwork.navigator.OwnNetworkNavigator
import br.com.policlinsaude.ownNetwork.view.adapter.OwnNetworkAdapter
import kotlinx.android.synthetic.main.fragment_own_network.*
import javax.inject.Inject

class OwnNetworkFragment : BaseFragmentWithInject(), OwnNetworkAdapter.OnItemClickListener {

    companion object {

        private const val EXTRA_ESTABLISHMENTS = "extra_establishments"
        private const val EXTRA_QUALIFICATIONS = "extra_qualifications"
        fun newInstance(list: List<PresentationEstablishment>,
                        qualifications: List<PresentationQualification>): OwnNetworkFragment {
            val fragment = OwnNetworkFragment()
            val bundle = Bundle()
            bundle.putParcelableArray(EXTRA_ESTABLISHMENTS, list.toTypedArray())
            bundle.putParcelableArray(EXTRA_QUALIFICATIONS, qualifications.toTypedArray())
            fragment.arguments = bundle
            Log.d("FILTRO", "DENTRO do newInstance OwnNetworkFragment")
            return fragment
        }
    }

    @Inject
    lateinit var adapter: OwnNetworkAdapter

    @Inject
    lateinit var navigator: OwnNetworkNavigator

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_own_network, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter.setEstablishments((arguments!!.getParcelableArray(EXTRA_ESTABLISHMENTS) as Array<PresentationEstablishment>).toMutableList(),
                (arguments!!.getParcelableArray(EXTRA_QUALIFICATIONS) as Array<PresentationQualification>).toMutableList())

        recyclerView.layoutManager =
            LinearLayoutManager(context)
        recyclerView.adapter = adapter
    }

    override fun onItemClick(establishment: PresentationEstablishment) {

        navigator.goToDetails(establishment)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
    }
}