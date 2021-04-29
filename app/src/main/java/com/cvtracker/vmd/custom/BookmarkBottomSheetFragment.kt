package com.cvtracker.vmd.custom

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cvtracker.vmd.R
import com.cvtracker.vmd.data.Bookmark
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.bottomsheet_center_bookmark.*

class BookmarkBottomSheetFragment : BottomSheetDialogFragment() {

    var listener: ((Bookmark) -> Unit)? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bottomsheet_center_bookmark, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpViews()
    }

    private fun setUpViews() {
        when (arguments?.getSerializable(EXTRA_CURRENT_BOOKMARK) as? Bookmark) {
            Bookmark.NOTIFICATION -> notificationView.setText(R.string.bookmark_notification_keep)
            Bookmark.FAVORITE -> favoriteView.setText(R.string.bookmark_favorite_keep)
            Bookmark.NONE -> noneView.setText(R.string.bookmark_none_keep)
        }

        notificationView.setOnClickListener {
            dismissAllowingStateLoss()
            listener?.invoke(Bookmark.NOTIFICATION)
        }
        favoriteView.setOnClickListener {
            dismissAllowingStateLoss()
            listener?.invoke(Bookmark.FAVORITE)
        }
        noneView.setOnClickListener {
            dismissAllowingStateLoss()
            listener?.invoke(Bookmark.NONE)
        }
    }

    companion object {

        private const val EXTRA_CURRENT_BOOKMARK = "EXTRA_CURRENT_BOOKMARK"

        fun newInstance(currentBookmark: Bookmark): BookmarkBottomSheetFragment =
            BookmarkBottomSheetFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(EXTRA_CURRENT_BOOKMARK, currentBookmark)
                }
            }
    }
}