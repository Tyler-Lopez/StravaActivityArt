package com.activityartapp.presentation.editArtScreen

import androidx.activity.compose.BackHandler
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import com.activityartapp.R
import com.activityartapp.architecture.EventReceiver
import com.activityartapp.presentation.common.AppBarScaffold
import com.activityartapp.presentation.common.ScreenBackground
import com.activityartapp.presentation.common.ScreenMeasurer
import com.activityartapp.presentation.editArtScreen.EditArtHeaderType.*
import com.activityartapp.presentation.editArtScreen.EditArtViewEvent.NavigateUpClicked
import com.activityartapp.presentation.editArtScreen.EditArtViewEvent.SaveClicked
import com.activityartapp.presentation.editArtScreen.composables.EditArtDialogInfo
import com.activityartapp.presentation.editArtScreen.subscreens.filters.EditArtFiltersScreen
import com.activityartapp.presentation.editArtScreen.subscreens.preview.EditArtPreview
import com.activityartapp.presentation.editArtScreen.subscreens.resize.EditArtResizeScreen
import com.activityartapp.presentation.editArtScreen.subscreens.sort.EditArtSortScreen
import com.activityartapp.presentation.editArtScreen.subscreens.style.EditArtStyleScreen
import com.activityartapp.presentation.editArtScreen.subscreens.type.EditArtTypeScreen
import com.activityartapp.presentation.editArtScreen.EditArtViewEvent.ScreenMeasured
import com.activityartapp.presentation.ui.theme.spacing
import com.activityartapp.util.enums.BackgroundType
import com.google.accompanist.pager.ExperimentalPagerApi
import kotlinx.coroutines.flow.StateFlow

@Composable
fun EditArtViewDelegate(viewModel: EditArtViewModel) {
    ScreenMeasurer {
        println("Screen measured.")
        viewModel.onEvent(ScreenMeasured(it))
    }
    CollectViewState(
        flow = viewModel.viewState,
        eventReceiver = viewModel
    )
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun CollectViewState(
    flow: StateFlow<EditArtViewState?>,
    eventReceiver: EventReceiver<EditArtViewEvent>
) {
    flow.collectAsState().value?.apply {
        val continueEnabled =
            (this@apply as? EditArtViewState.Standby)?.atLeastOneActivitySelected
        AppBarScaffold(
            text = stringResource(R.string.action_bar_edit_art_header),
            onNavigateUp = { eventReceiver.onEventDebounced(NavigateUpClicked) },
            actions = {
                IconButton(
                    onClick = { eventReceiver.onEventDebounced(SaveClicked) },
                    enabled = continueEnabled ?: false
                ) {
                    Text(
                        text = stringResource(R.string.button_continue_uppercase),
                        style = MaterialTheme.typography.button,
                        color = continueEnabled?.takeIf { it }?.let {
                            MaterialTheme.colors.onPrimary
                        } ?: Color.Unspecified
                    )
                }
                Spacer(modifier = Modifier.width(spacing.medium))
            },
            tabLayout = {
                pagerStateWrapper.apply {
                    EditArtTabLayout(
                        pagerHeaders = pagerHeaders,
                        pagerState = pagerState,
                        eventReceiver = eventReceiver
                    )
                }
            }
        ) {
            if (this@apply is EditArtViewState.Standby) {
                val activeHeader =
                    EditArtHeaderType.fromOrdinal(pagerStateWrapper.pagerState.currentPage)
                Crossfade(
                    targetState = activeHeader,
                    animationSpec = tween(500, easing = FastOutSlowInEasing)
                ) {
                    when (it) {
                        PREVIEW -> EditArtPreview(
                            atLeastOneActivitySelected,
                            styleBackgroundType == BackgroundType.TRANSPARENT,
                            bitmap,
                            previewZoomFactor,
                            previewOffset,
                            eventReceiver
                        )
                        FILTERS -> EditArtFiltersScreen(
                            filterActivitiesCountDate,
                            filterActivitiesCountDistance,
                            filterActivitiesCountType,
                            filterDateSelections,
                            filterDateSelectionIndex,
                            filterDistanceSelectedEnd?.let { end ->
                                filterDistanceSelectedStart?.rangeTo(end)
                            },
                            filterDistanceTotalEnd?.let { end ->
                                filterDistanceTotalStart?.rangeTo(end)
                            },
                            filterDistancePendingChangeStart,
                            filterDistancePendingChangeEnd,
                            listStateFilter,
                            filterTypes?.toList(),
                            eventReceiver
                        )
                        STYLE -> EditArtStyleScreen(
                            styleBackgroundType,
                            styleBackgroundAngleType,
                            styleBackgroundList.take(styleBackgroundGradientColorCount),
                            styleActivities,
                            styleFont,
                            listStateStyle,
                            styleStrokeWidthType,
                            eventReceiver
                        )
                        TYPE -> ScreenBackground(scrollState = scrollStateType) {
                            EditArtTypeScreen(
                                typeActivitiesDistanceMetersSummed,
                                typeCenterCustomText,
                                typeLeftCustomText,
                                typeRightCustomText,
                                typeFontSelected,
                                typeFontWeightSelected,
                                typeFontItalicized,
                                typeFontSizeSelected,
                                typeMaximumCustomTextLength,
                                typeCenterSelected,
                                typeLeftSelected,
                                typeRightSelected,
                                eventReceiver
                            )
                        }
                        SORT -> ScreenBackground(scrollState = scrollStateSort) {
                            EditArtSortScreen(
                                sortTypeSelected = sortTypeSelected,
                                sortDirectionSelected = sortDirectionTypeSelected,
                                eventReceiver = eventReceiver
                            )
                        }
                        RESIZE -> ScreenBackground(scrollState = scrollStateResize) {
                            EditArtResizeScreen(
                                sizeResolutionList,
                                sizeResolutionListSelectedIndex,
                                eventReceiver
                            )
                        }
                        null -> error("Invalid pagerState current page.")
                    }
                }
            }
        }

        /** Only intercept when the dialog box is not visible **/
        BackHandler(enabled = dialogActive is EditArtDialog.None) {
            eventReceiver.onEvent(NavigateUpClicked)
        }

        when (dialogActive) {
            is EditArtDialog.ConfirmDeleteGradientColor -> {
                EditArtDialogInfo(
                    body = stringArrayResource(id = R.array.edit_art_dialog_info_remove_gradient_color),
                    eventReceiver = eventReceiver,
                    type = EditArtDialogType.REMOVE_AND_DISMISS_BY_CANCEL
                )
            }
            is EditArtDialog.InfoCheckeredBackground -> EditArtDialogInfo(
                body = stringArrayResource(id = R.array.edit_art_dialog_info_background_checkered),
                eventReceiver = eventReceiver,
                type = EditArtDialogType.DISMISS_BY_OK
            )
            is EditArtDialog.InfoGradientBackground -> EditArtDialogInfo(
                body = stringArrayResource(id = R.array.edit_art_dialog_info_background_gradient),
                eventReceiver = eventReceiver,
                type = EditArtDialogType.DISMISS_BY_OK
            )
            is EditArtDialog.InfoTransparent -> EditArtDialogInfo(
                body = stringArrayResource(id = R.array.edit_art_dialog_info_background_transparent),
                eventReceiver = eventReceiver,
                type = EditArtDialogType.DISMISS_BY_OK
            )
            is EditArtDialog.NavigateUp -> EditArtDialogInfo(
                body = stringArrayResource(id = R.array.edit_art_dialog_exit_confirmation_prompt),
                eventReceiver = eventReceiver,
                type = EditArtDialogType.DISCARD_AND_DISMISS_BY_CANCEL
            )
            is EditArtDialog.None -> {} // No-op
        }
    }
}