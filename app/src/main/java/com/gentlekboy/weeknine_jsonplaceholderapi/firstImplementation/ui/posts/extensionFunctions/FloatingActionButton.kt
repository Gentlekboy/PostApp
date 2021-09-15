package com.gentlekboy.weeknine_jsonplaceholderapi.firstImplementation.ui.posts

import android.view.View
import androidx.core.widget.NestedScrollView
import com.google.android.material.floatingactionbutton.FloatingActionButton

//Listen to scroll and display or hide floating action button
fun floatingActionButtonVisibility(nestedScrollView: NestedScrollView, fab: FloatingActionButton){
    var previousScrollY = 0
    nestedScrollView.viewTreeObserver.addOnScrollChangedListener {
        if (nestedScrollView.scrollY > previousScrollY && fab.visibility != View.VISIBLE) {
            fab.show()
        } else if (nestedScrollView.scrollY < previousScrollY && fab.visibility == View.VISIBLE) {
            fab.hide()
        }
        previousScrollY = nestedScrollView.scrollY
    }
}