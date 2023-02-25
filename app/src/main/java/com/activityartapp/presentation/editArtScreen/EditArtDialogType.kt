package com.activityartapp.presentation.editArtScreen

sealed interface EditArtDialog {
    data class ConfirmDeleteGradientColor(val toDeleteIndex: Int) : EditArtDialog
    object InfoCheckeredBackground : EditArtDialog
    object InfoGradientBackground : EditArtDialog
    object InfoTransparent : EditArtDialog
    object NavigateUp : EditArtDialog
    object None : EditArtDialog
}

enum class EditArtDialogType {
    DISMISS_BY_OK, REMOVE_AND_DISMISS_BY_CANCEL, DISCARD_AND_DISMISS_BY_CANCEL
}