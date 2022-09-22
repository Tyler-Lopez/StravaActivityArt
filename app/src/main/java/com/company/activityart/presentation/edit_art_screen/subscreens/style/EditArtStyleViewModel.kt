package com.company.activityart.presentation.edit_art_screen.subscreens.style

import com.company.activityart.architecture.BaseChildViewModel
import com.company.activityart.presentation.edit_art_screen.ColorWrapper
import com.company.activityart.presentation.edit_art_screen.ColorWrapper.Companion.INITIAL_BG_COLOR
import com.company.activityart.presentation.edit_art_screen.EditArtViewEvent
import com.company.activityart.presentation.edit_art_screen.EditArtViewEvent.StylesBackgroundChanged
import com.company.activityart.presentation.edit_art_screen.subscreens.style.ColorType.*
import com.company.activityart.presentation.edit_art_screen.subscreens.style.EditArtStyleViewEvent.ColorChanged
import com.company.activityart.presentation.edit_art_screen.subscreens.style.StyleType.BACKGROUND
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class EditArtStyleViewModel @Inject constructor(
) : BaseChildViewModel<
        EditArtStyleViewState,
        EditArtStyleViewEvent,
        EditArtViewEvent
        >() {

    init {
        pushState(EditArtStyleViewState(colorBackground = INITIAL_BG_COLOR))
    }

    override fun onEvent(event: EditArtStyleViewEvent) {
        when (event) {
            is ColorChanged -> onColorChanged(event)
        }
    }

    private fun onColorChanged(event: ColorChanged) {
        (lastPushedState)?.run {
            event.run {
                when (styleType) {
                    BACKGROUND -> {
                        copy(colorBackground = colorBackground.copyWithEvent(event).apply {
                            onParentEvent(StylesBackgroundChanged(this))
                        })
                    }
                }
            }
        }?.push()
    }

    /** @return Copy of [ColorWrapper] which reflects a change in blue, green, or red values. **/
    private fun ColorWrapper.copyWithEvent(event: ColorChanged): ColorWrapper {
        return event.run {
            when (colorType) {
                ALPHA -> copy(alpha = changedTo)
                BLUE -> copy(blue = changedTo)
                GREEN -> copy(green = changedTo)
                RED -> copy(red = changedTo)
            }
        }
    }
}