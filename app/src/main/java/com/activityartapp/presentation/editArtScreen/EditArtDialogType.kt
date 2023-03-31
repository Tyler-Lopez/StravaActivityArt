package com.activityartapp.presentation.editArtScreen

sealed interface EditArtDialog {
    data class ConfirmDeleteActivityColorRule(val toDeleteIndex: Int) : EditArtDialog
    data class ConfirmDeleteGradientColor(val toDeleteIndex: Int) : EditArtDialog
    object InfoCheckeredBackground : EditArtDialog
    object InfoGradientBackground : EditArtDialog
    object InfoTransparent : EditArtDialog
    object NavigateUp : EditArtDialog
    object None : EditArtDialog
}

enum class EditArtDialogType {
    DISMISS_BY_OK, REMOVE_ACTIVITY_COLOR_RULE_AND_DISMISS_BY_CANCEL, REMOVE_GRADIENT_AND_DISMISS_BY_CANCEL, DISCARD_AND_DISMISS_BY_CANCEL
}