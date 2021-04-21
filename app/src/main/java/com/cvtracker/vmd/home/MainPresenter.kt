package com.cvtracker.vmd.home

import com.cvtracker.vmd.R
import com.cvtracker.vmd.data.DisplayItem
import com.cvtracker.vmd.data.SearchEntry
import com.cvtracker.vmd.master.AnalyticsHelper
import com.cvtracker.vmd.master.DataManager
import com.cvtracker.vmd.master.PrefHelper
import kotlinx.coroutines.*
import timber.log.Timber

class MainPresenter(private val view: MainContract.View) : MainContract.Presenter {

    private var jobSearch: Job? = null

    override fun loadInitialState() {
        PrefHelper.favEntry.let { entry ->
            if (entry == null) {
                view.showEmptyState()
            }
            view.displaySelectedSearchEntry(entry)
        }
    }

    override fun loadCenters() {
        GlobalScope.launch(Dispatchers.Main) {
            PrefHelper.favEntry?.let { entry ->
                try {
                    view.setLoading(true)
                    DataManager.getCenters(entry.entryDepartmentCode).let {
                        val list = mutableListOf<DisplayItem>()
                        list.add(DisplayItem.LastUpdated(it.lastUpdated))
                        if (it.availableCenters.isNotEmpty()) {
                            list.add(DisplayItem.AvailableCenterHeader(it.availableCenters.size, it.availableCenters.sumBy { it.appointmentCount }))
                            list.addAll(it.availableCenters.onEach { it.available = true })
                        }
                        if (it.unavailableCenters.isNotEmpty()) {
                            if (it.availableCenters.isEmpty()) {
                                list.add(DisplayItem.UnavailableCenterHeader(R.string.no_slots_available_center_header))
                            } else {
                                list.add(DisplayItem.UnavailableCenterHeader(R.string.no_slots_available_center_header_others))
                            }
                            list.addAll(it.unavailableCenters.onEach { it.available = false })
                        }
                        view.showCenters(list)
                        AnalyticsHelper.logEventSearch(entry, it, AnalyticsHelper.FilterType.ByDate)
                    }
                } catch (e: Exception) {
                    Timber.e(e)
                    view.showCentersError()
                } finally {
                    view.setLoading(false)
                }
            }
        }
    }

    override fun onSearchEntrySelected(searchEntry: SearchEntry) {
        if (searchEntry.entryCode != PrefHelper.favEntry?.entryCode) {
            PrefHelper.favEntry = searchEntry
            view.showCenters(emptyList())
        }
        loadCenters()
    }

    override fun getSavedSearchEntry(): SearchEntry? {
        return PrefHelper.favEntry
    }

    override fun onCenterClicked(center: DisplayItem.Center) {
        view.openLink(center.url)
        AnalyticsHelper.logEventRdvClick(center, AnalyticsHelper.FilterType.ByDate)
    }

    override fun onSearchUpdated(search: String) {
        jobSearch?.cancel()
        if (search.length < 2) {
            /** reset entry list **/
            view.setupSelector(emptyList())
            return
        }
        jobSearch = GlobalScope.launch(Dispatchers.Main) {
            /** Wait a bit then we are sure the user want to do this one **/
            delay(250)
            val list = mutableListOf<SearchEntry>()
            if (search.substring(0, 1).toIntOrNull() != null) {
                /** Search by code **/
                list.addAll(DataManager.getDepartmentsByCode(search))
                list.addAll(DataManager.getCitiesByPostalCode(search))
            } else {
                /** Search by name **/
                list.addAll(DataManager.getDepartmentsByName(search))
                list.addAll(DataManager.getCitiesByName(search))
            }
            view.setupSelector(list)
        }
    }

}