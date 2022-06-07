package br.com.policlinsaude.home.view

import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import br.com.domain.model.Banner
import br.com.domain.model.Person
import br.com.domain.model.UserConnected
import br.com.policlinsaude.R
import br.com.policlinsaude.core.base.BaseFragmentWithInject
import br.com.policlinsaude.core.helper.DialogHelper
import br.com.policlinsaude.home.navigator.HomeNavigator
import br.com.policlinsaude.home.presenter.HomePresenter
import br.com.policlinsaude.home.view.adapter.HomeAdapter
import br.com.policlinsaude.home.view.adapter.HomePageAdapter
import br.com.policlinsaude.home.view.model.PresentationHomeOptionEnum
import br.com.policlinsaude.preferences.presenter.PreferencesPresenter
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_home.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class HomeFragment : BaseFragmentWithInject(), HomeView, HomeAdapter.OnItemClickListener {

    companion object {

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
        val toolbar: Toolbar = view.findViewById(R.id.toolbar)
        val nameTextView: TextView = view.findViewById(R.id.person_name_text_view)

        (activity as MenuActivity).setupFragmentToolbar(toolbar, null)
        (activity as MenuActivity).toggle.drawerArrowDrawable.gapSize = 12.0f
        if ((activity as MenuActivity).isGuest) {
            nameTextView.setText(R.string.text_guest)
        }

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

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        activity?.let {
            return it.onOptionsItemSelected(item)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView.adapter = homeAdapter
        recyclerView.layoutManager = GridLayoutManager(context, 2)

        viewPager.adapter = homePageAdapter

        presenter.onViewAttached()


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
            else -> {
            }
        }
    }

    override fun showLoginDialog() {
        (activity as MenuView).showLoginDialog()
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

    private fun setupAutoScroll() {
        autoScrollObservable = Observable.intervalRange(1, Long.MAX_VALUE, 0, 5, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
        autoScrollDisposable = autoScrollObservable?.subscribe({ _ ->
            val nextItem = if (viewPager.currentItem + 1 == viewPager.adapter?.count || viewPager.adapter?.count == 0) 0 else viewPager.currentItem + 1
            viewPager.setCurrentItem(nextItem, true)
        })
    }

    override fun showBannerLoading() {
        bannerProgress.visibility = View.VISIBLE
    }

    override fun hideBannerLoading() {
        bannerProgress.visibility = View.GONE
    }
}