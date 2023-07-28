package br.com.policlinsaude.home.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.appcompat.widget.Toolbar
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import br.com.domain.model.Banner
import br.com.domain.model.Person
import br.com.domain.model.UserConnected
import br.com.domain.model.ValidateButtons
import br.com.policlinsaude.R
import br.com.policlinsaude.core.base.BaseFragmentWithInject
import br.com.policlinsaude.core.helper.DialogHelper
import br.com.policlinsaude.home.navigator.HomeNavigator
import br.com.policlinsaude.home.presenter.HomePresenter
import br.com.policlinsaude.home.view.adapter.HomeAdapter
import br.com.policlinsaude.home.view.adapter.HomePageAdapter
import br.com.policlinsaude.home.view.model.PresentationHomeOptionEnum
import br.com.policlinsaude.preferences.presenter.PreferencesPresenter
import com.policlinsaude.newfeature.features.ScheduleCentral.ui.activities.ScheduleCentralActivity
import com.policlinsaude.newfeature.features.Token.ui.TokenActivity
import com.policlinsaude.newfeature.features.coparticipation.ui.activities.ResearchCoParticipationActivity
import com.policlinsaude.newfeature.features.extractor.ui.activities.FactorExtractorActivity
import com.policlinsaude.newfeature.features.guidAuthorizer.ui.activities.GuideAuthorizerActivity
import com.policlinsaude.newfeature.features.incometax.ui.activities.IncomeTaxActivity
import com.policlinsaude.newfeature.features.notifications.ui.activities.NotificationActivity
import com.policlinsaude.newfeature.features.tickets.ui.activities.TicketsActivity
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.app_bar_home.*
import kotlinx.android.synthetic.main.app_bar_home.view.*
import kotlinx.android.synthetic.main.fragment_home.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class HomeFragment: BaseFragmentWithInject(), HomeView, HomeAdapter.OnItemClickListener {

    private var isCoPartFM: Boolean = false

    lateinit var toolbar: Toolbar

    companion object {
        private const val CO_PART_FM = "fm"
        private const val IS_CO_PART_FM = "CO_PART_FM"

        fun newInstance(): HomeFragment {
            return HomeFragment()
        }
    }

    @Inject
    lateinit var presenter: HomePresenter
    @Inject
    lateinit var homePageAdapter: HomePageAdapter

    @Inject
    lateinit var homeAdapter: HomeAdapter

    @Inject
    lateinit var homeNavigator: HomeNavigator

    private var autoScrollObservable: Observable<Long>? = null
    private var autoScrollDisposable: Disposable? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        super.onCreateView(inflater, container, savedInstanceState)
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        toolbar = view.findViewById(R.id.toolbar)
        val nameTextView: TextView = view.findViewById(R.id.person_name_text_view)

        (activity as MenuActivity).setupFragmentToolbar(toolbar, null)
        (activity as MenuActivity).toggle.apply {
            drawerArrowDrawable.gapSize = 10.0f
        }

        if ((activity as MenuActivity).isGuest) {
            nameTextView.setText(R.string.text_guest)
        }

        toolbar.apply {
            navigationIcon = ContextCompat.getDrawable(requireContext(),  R.drawable.ic_menu)
        }

        //activity?.window?.statusBarColor = ContextCompat.getColor(requireContext(), R.color.Bordo)

        return view
    }

    override fun onResume() {
        super.onResume()
        setupAutoScroll()
    }

    override fun onPause() {
        super.onPause()
        autoScrollDisposable?.dispose()
        autoScrollDisposable = null
        autoScrollObservable = null
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        activity?.let {
            return it.onOptionsItemSelected(item)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView.adapter = homeAdapter
        recyclerView.layoutManager =
            GridLayoutManager(context, 2)

        viewPager.adapter = homePageAdapter
        homeAdapter.setup(PresentationHomeOptionEnum.values().toMutableList())
        presenter.onValidateButtons()
        presenter.onViewAttached()

        alert_menu.setOnClickListener {
            startActivity(Intent(context, NotificationActivity::class.java))
        }

    }

    override fun renderPerson(person: Person) {
        view?.findViewById<TextView>(R.id.person_name_text_view)?.text =
                getString(R.string.text_person_home_title, person.name, person.descriptionPlan)

        presenter.onValidateConnectedUser(person.plan.register, person.plan.order)
    }

    override fun onItemClick(option: PresentationHomeOptionEnum) {
        when (option) {
            PresentationHomeOptionEnum.MEDICAL_GUIDE -> homeNavigator.goToMedicalGuideOptions()
            PresentationHomeOptionEnum.HEALTH_INSURANCE -> {
                if ((activity as MenuActivity).isGuest) {
                    presenter.onMenuClickedAsGuest()
                } else {
                    homeNavigator.goToHealthInsurancePhoto()
                }
            }
            PresentationHomeOptionEnum.OWN_NETWORK -> homeNavigator.goToOwnNetwork()
            PresentationHomeOptionEnum.FAVORITES -> {
                if ((activity as MenuActivity).isGuest) {
                    presenter.onMenuClickedAsGuest()
                } else {
                    homeNavigator.goToFavorites()
                }
            }

            PresentationHomeOptionEnum.TICKET -> {
                if ((activity as MenuActivity).isGuest) {
                    presenter.onMenuClickedAsGuest()
                } else {
                    val intent = Intent(context, TicketsActivity::class.java)
                    startActivity(intent)
                }
            }

            PresentationHomeOptionEnum.RESEARCH_VALUES_CO_PARTICIPATION -> {
                if ((activity as MenuActivity).isGuest) {
                    presenter.onMenuClickedAsGuest()
                } else {
                    val intent = Intent(context, ResearchCoParticipationActivity::class.java)
                    intent.putExtra(IS_CO_PART_FM, isCoPartFM)
                    startActivity(intent)
                }
            }

            PresentationHomeOptionEnum.FACTOR_EXTRACTOR -> {
                if ((activity as MenuActivity).isGuest) {
                    presenter.onMenuClickedAsGuest()
                } else {
                    val intent = Intent(context, FactorExtractorActivity::class.java)
                    intent.putExtra(IS_CO_PART_FM, isCoPartFM)
                    startActivity(intent)
                }
            }

            PresentationHomeOptionEnum.INCOME_TAX -> {
                if ((activity as MenuActivity).isGuest) {
                    presenter.onMenuClickedAsGuest()
                } else {
                    val intent = Intent(context, IncomeTaxActivity::class.java)
                    startActivity(intent)
                }
            }


            PresentationHomeOptionEnum.GUIDE_AUTHORIZER -> {
                if ((activity as MenuActivity).isGuest) {
                    presenter.onMenuClickedAsGuest()
                } else {
                    val intent = Intent(context, GuideAuthorizerActivity::class.java)
                    startActivity(intent)
                }
            }

            PresentationHomeOptionEnum.SCHEDULE -> {
                if ((activity as MenuActivity).isGuest) {
                    presenter.onMenuClickedAsGuest()
                } else {
                    val intent = Intent(context, ScheduleCentralActivity::class.java)
                    startActivity(intent)
                }
            }

            PresentationHomeOptionEnum.SERVICE_TOKEN -> {
                if ((activity as MenuActivity).isGuest) {
                    presenter.onMenuClickedAsGuest()
                } else {
                    val intent = Intent(context, TokenActivity::class.java)
                    startActivity(intent)
                }
            }
        }
    }

    override fun showLoginDialog() {
        (activity as MenuView).showLoginDialog()
    }

    override fun showUpdateDialog(throwable: Throwable) {
        (activity as MenuActivity).showUpdateDialog(throwable)
    }

    override fun showUserNotConnectedDialog(item: UserConnected) {
        (activity as MenuView).showUserNotConnectedDialog(item.msgExterna.orEmpty())
    }

    override fun renderEmptyBanners() {
        // todo what sould I show?
    }

    override fun renderBanners(banners: List<Banner>) {
        homePageAdapter.setBanners(banners)
        pageIndicatorView.count = banners.size
    }

    override fun showButtons(buttons: ValidateButtons) {
        if(activity != null) {
            if(activity is MenuActivity) {
                if (!(activity as MenuActivity).isGuest) {
                    if (!buttons.boleto)
                        homeAdapter.removeTicket()

                    if (buttons.copartFM.isEmpty())
                        homeAdapter.removeExtracts()

                    if(!buttons.IR)
                        homeAdapter.removeIncomeTax()

                    if(!buttons.central)
                        homeAdapter.removeIncomeSchedule()

                    if(!buttons.autorizador)
                        homeAdapter.removeAuthorizer()

                    if(!buttons.gerarToken)
                        homeAdapter.removeToken()

                    isCoPartFM = buttons.copartFM.lowercase() == CO_PART_FM
                }
            }
        }
    }

    private fun setupAutoScroll() {
        autoScrollObservable = Observable.intervalRange(1, Long.MAX_VALUE, 0, 5, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
        autoScrollDisposable = autoScrollObservable?.subscribe { _ ->
            val nextItem =
                if (viewPager.currentItem + 1 == viewPager.adapter?.count || viewPager.adapter?.count == 0) 0 else viewPager.currentItem + 1
            viewPager.setCurrentItem(nextItem, true)
        }
    }

    override fun showBannerLoading() {
        bannerProgress.visibility = View.VISIBLE
    }

    override fun hideBannerLoading() {
        bannerProgress.visibility = View.GONE
    }
}