package com.example.frontend_happygreen.viewmodel

import androidx.lifecycle.ViewModel
import com.example.frontend_happygreen.data.model.BadgeDto
import com.example.frontend_happygreen.data.model.UtenteBadgeDto

class BadgeViewModel : ViewModel() {
    var tuttiBadge: List<BadgeDto> = emptyList()
    var mieiBadge: List<UtenteBadgeDto> = emptyList()

    // getAllBadges(), getUserBadges()... (da fare)
}
