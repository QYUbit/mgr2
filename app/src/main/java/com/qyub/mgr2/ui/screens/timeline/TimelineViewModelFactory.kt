package com.qyub.mgr2.ui.screens.timeline

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.qyub.mgr2.data.repo.EventRepository

class TimelineViewModelFactory(
    private val repo: EventRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TimelineViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TimelineViewModel(repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}