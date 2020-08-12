package com.machioni.ciceronedaggerdemo.presentation.scene.dashboard.scene1.container

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.machioni.ciceronedaggerdemo.R
import com.machioni.ciceronedaggerdemo.presentation.common.*
import kotlinx.android.synthetic.main.fragment_container.*
import ru.terrakok.cicerone.Cicerone
import ru.terrakok.cicerone.Navigator
import ru.terrakok.cicerone.commands.Back
import ru.terrakok.cicerone.commands.Command
import ru.terrakok.cicerone.commands.Replace
import ru.terrakok.cicerone.commands.SystemMessage

class ContainerFragment : Fragment(), BackButtonListener {

    private val homeTabFragment = HomeTabFragment.newInstance()
    private val dashboardTabFragment = DashboardTabFragment.newInstance()
    private val notificationsTabFragment = NotificationsTabFragment.newInstance()

    //will point to the fragment that is currently visible
    private var currentTab: String = NavigationKeys.HOME_TAB_FRAGMENT

    //using cicerone to change tabs is optional. we could change fragments directly without going through
    //the router, but this may protect against lifecycle problems
    private val localCicerone = Cicerone.create()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_container, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupNavigationBar()
        bottomNavigationView.selectedItemId = R.id.navigation_home
    }

    private fun setupNavigationBar() {
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> localCicerone.router.replaceScreen(NavigationKeys.HOME_TAB_FRAGMENT) //envia comandos para o navigator
                R.id.navigation_dashboard -> localCicerone.router.replaceScreen(NavigationKeys.DASHBOARD_TAB_FRAGMENT)
                R.id.navigation_notifications -> localCicerone.router.replaceScreen(NavigationKeys.NOTIFICATIONS_TAB_FRAGMENT)
            }
            true
        }
    }

    //we shouldn`t use the default navigator since it uses replace() to change fragments, and that
    //destroys the previous one. What we want instead is to hide and show so our flow isn`t lost.
    private val navigator = object : Navigator {
        override fun applyCommands(commands: Array<Command>) {
            for (command in commands) applyCommand(command)
        }

        private fun applyCommand(command: Command) {
            when (command) {
                is Back -> onDestroy()
                is SystemMessage -> Toast.makeText(activity, command.message, Toast.LENGTH_SHORT).show()
                is Replace -> {
                    when (command.screenKey) {
                        NavigationKeys.HOME_TAB_FRAGMENT -> changeTab(homeTabFragment)
                        NavigationKeys.DASHBOARD_TAB_FRAGMENT -> changeTab(dashboardTabFragment)
                        NavigationKeys.NOTIFICATIONS_TAB_FRAGMENT -> changeTab(notificationsTabFragment)
                    }
                }
            }
        }

        //we could also use attach() and detach() instead of show() and hide().
        private fun changeTab(targetFragment: TabNavigationFragment) {
            with(fragmentManager?.beginTransaction()) {
                fragmentManager?.fragments?.filter { it != targetFragment }?.forEach {
                    if (it != this@ContainerFragment) {
                        this?.hide(it)
                        it.userVisibleHint = false //since hide doesnt trigger onPause, we use this instead to let the fragment know it is not visible
                    }
                }
                targetFragment.let {
                    if (it.isAdded && currentTab == it.navigationKey) {
                        it.returnToFirstScreen()
                    } else if (it.isAdded) {
                        this?.show(it)
                    } else {
                        this?.add(R.id.flowContainer, it, it.navigationKey)
                    }
                    currentTab = it.navigationKey
                    it.userVisibleHint = true
                }
                this?.commit()
            }
        }
    }

    override fun onBackPressed(): Boolean {
        return if (isAdded) {
            val currentFragment = (fragmentManager?.findFragmentByTag(currentTab) as TabNavigationFragment)
            currentFragment.onBackPressed()
        } else false
    }

    override fun onResume() {
        super.onResume()
        localCicerone.navigatorHolder.setNavigator(navigator)
    }

    override fun onPause() {
        localCicerone.navigatorHolder.removeNavigator()
        super.onPause()
    }

    override fun onDetach() {
        super.onDetach()
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
    }
}