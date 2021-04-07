package com.covidtracker.vitemadose.home

import com.covidtracker.vitemadose.data.Department
import com.covidtracker.vitemadose.data.DisplayItem
import java.util.*

interface MainContract {

    interface View {

        /**
         * Display main list of centers (available/unavailable)
         */
        fun showCenters(list: List<DisplayItem>, lastUpdatedDate: Date)

        /**
         * Setup department selectors with retrieved departments
         */
        fun setupSelector(items: List<Department>, indexSelected: Int)

        /**
         * Open link
         */
        fun openLink(url: String)

        fun showCentersError()
    }

    interface Presenter {

        /**
         * Load all departments available
         */
        fun loadDepartments()

        /**
         * Load centers for the saved department
         */
        fun loadCenters()

        /**
         * Called when a center is clicked
         */
        fun onCenterClicked(center: DisplayItem.Center)

        /**
         * Called when a department is selected via the selector
         */
        fun onDepartmentSelected(department: Department)
    }
}