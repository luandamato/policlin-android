package br.com.policlinsaude.units.view

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.policlinsaude.R
import br.com.policlinsaude.core.base.BaseFragmentWithInject
import br.com.policlinsaude.model.PresentationEstablishment
import br.com.policlinsaude.model.PresentationQualification
import br.com.policlinsaude.units.navigator.UnitsNavigator
import br.com.policlinsaude.units.view.adapter.UnitsAdapter
import kotlinx.android.synthetic.main.fragment_units.*
import javax.inject.Inject

class UnitsFragment : BaseFragmentWithInject(), UnitsAdapter.OnItemClickListener {

    companion object {

        private const val EXTRA_ESTABLISHMENTS_UNITS = "extra_establishments_units"
        private const val EXTRA_QUALIFICATIONS_UNITS = "extra_qualifications_units"
        fun newInstance(list: List<PresentationEstablishment>,
                        qualifications: List<PresentationQualification>): UnitsFragment {
            val fragment = UnitsFragment()
            val bundle = Bundle()
            bundle.putParcelableArray(EXTRA_ESTABLISHMENTS_UNITS, list.toTypedArray())
            bundle.putParcelableArray(EXTRA_QUALIFICATIONS_UNITS, qualifications.toTypedArray())
            fragment.arguments = bundle
            return fragment
        }
    }

    @Inject
    lateinit var adapter: UnitsAdapter

    @Inject
    lateinit var navigator: UnitsNavigator

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        super.onCreateView(inflater, container, savedInstanceState)

     //   val view = inflater.inflate(R.layout.fragment_units, container, false)
     //   val toolbar: Toolbar = view.findViewById(R.id.toolbar)

      //  (activity as MenuActivity).setupFragmentToolbar(toolbar, R.string.title_unities)

        Log.d("UNIDADES","onCreateView do UnitsFragment" )
        return inflater.inflate(R.layout.fragment_units, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

     //   adapter.setEstablishments((arguments!!.getParcelableArray(EXTRA_ESTABLISHMENTS_UNITS) as Array<PresentationEstablishment>).toMutableList(),
     //           (arguments!!.getParcelableArray(EXTRA_QUALIFICATIONS_UNITS) as Array<PresentationQualification>).toMutableList())

        recyclerViewUnitsFrag.layoutManager = LinearLayoutManager(context)
        recyclerViewUnitsFrag.adapter = adapter
    }

    override fun onItemClick(establishment: PresentationEstablishment) {
        Log.d("UNIDADES","establishment na seleção: " + establishment.toString() )
        navigator.goToDetails(establishment)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        // do nothing
    }
}