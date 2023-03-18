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
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.activityartapp.R
import com.activityartapp.architecture.EventReceiver
import com.activityartapp.architecture.StatePusher
import com.activityartapp.presentation.common.AppBarScaffold
import com.activityartapp.presentation.common.ScreenBackground
import com.activityartapp.presentation.common.ScreenMeasurer
import com.activityartapp.presentation.editArtScreen.EditArtHeaderType.*
import com.activityartapp.presentation.editArtScreen.EditArtViewEvent.*
import com.activityartapp.presentation.editArtScreen.composables.EditArtDialogInfo
import com.activityartapp.presentation.editArtScreen.subscreens.filters.EditArtFiltersScreen
import com.activityartapp.presentation.editArtScreen.subscreens.preview.EditArtPreview
import com.activityartapp.presentation.editArtScreen.subscreens.resize.EditArtResizeScreen
import com.activityartapp.presentation.editArtScreen.subscreens.sort.EditArtSortScreen
import com.activityartapp.presentation.editArtScreen.subscreens.style.EditArtStyleScreen
import com.activityartapp.presentation.editArtScreen.subscreens.type.EditArtTypeScreen
import com.activityartapp.presentation.ui.theme.spacing
import com.activityartapp.util.classes.YearMonthDay
import com.activityartapp.util.enums.BackgroundType
import com.google.accompanist.pager.ExperimentalPagerApi

@Composable
fun EditArtViewDelegate(viewModel: EditArtViewModel) {
    CollectViewState(
        flow = viewModel,
        eventReceiver = viewModel
    )
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun CollectViewState(
    flow: StatePusher<EditArtViewState>,
    eventReceiver: EventReceiver<EditArtViewEvent>
) {
    flow.viewState.collectAsState().value?.apply {
        /* Triggers a recomposition only when all activity counts do not equal zero
        or at least one does. */
        val atLeastOneActivitySelected = derivedStateOf {
            (this as? EditArtViewState.Standby)?.run {
                (filterActivitiesCountDate.value != 0)
                    .and(filterActivitiesCountDate.value != 0)
                    .and(filterActivitiesCountDistance.value != 0)
            } ?: false
        }

        AppBarScaffold(
            text = stringResource(R.string.action_bar_edit_art_header),
            onNavigateUp = { eventReceiver.onEventDebounced(NavigateUpClicked) },
            actions = {
                IconButton(
                    onClick = { eventReceiver.onEventDebounced(SaveClicked) },
                    enabled = atLeastOneActivitySelected.value
                ) {
                    Text(
                        text = stringResource(R.string.button_continue_uppercase),
                        style = MaterialTheme.typography.button,
                        color = if (atLeastOneActivitySelected.value) {
                            MaterialTheme.colors.onPrimary
                        } else {
                            Color.Unspecified
                        }
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

                val activeHeader = EditArtHeaderType
                    .fromOrdinal(pagerStateWrapper.pagerState.currentPage)

                val backgroundIsTransparent = derivedStateOf {
                    styleBackgroundType.value == BackgroundType.TRANSPARENT
                }

                Crossfade(
                    targetState = activeHeader,
                    animationSpec = tween(500, easing = FastOutSlowInEasing)
                ) {
                    ScreenBackground(
                        horizontalAlignment = if (it != PREVIEW) Alignment.Start else Alignment.CenterHorizontally,
                        padding = 0.dp,
                        scrollState = when (it) {
                            TYPE -> scrollStateType
                            SORT -> scrollStateSort
                            else -> null
                        },
                        scrollingEnabled = it != PREVIEW && it != STYLE && it != FILTERS && it != RESIZE
                    ) {
                        when (it) {
                            PREVIEW -> EditArtPreview(
                                atLeastOneActivitySelected,
                                previewBitmap,
                                previewBitmapZoomed,
                                sizeResolutionList[sizeResolutionListSelectedIndex.value].run {
                                    Size(width = sizeWidthPx, height = sizeHeightPx)
                                }
                            ) { zoomEvent -> eventReceiver.onEvent(zoomEvent) }
                            FILTERS -> YearMonthDay.run {
                                EditArtFiltersScreen(
                                    filterActivitiesCountDate,
                                    filterActivitiesCountDistance,
                                    filterActivitiesCountType,
                                    filterDateSelections,
                                    filterDateSelectionIndex,
                                    filterDistanceSelectedStart,
                                    filterDistanceSelectedEnd,
                                    filterDistanceTotalStart,
                                    filterDistanceTotalEnd,
                                    filterDistancePendingChangeStart,
                                    filterDistancePendingChangeEnd,
                                    listStateFilter,
                                    filterTypes,
                                    eventReceiver
                                )
                            }
                            STYLE -> EditArtStyleScreen(
                                styleBackgroundType,
                                styleBackgroundAngleType,
                                styleBackgroundGradientColorCount,
                                styleBackgroundList,
                                styleActivities,
                                styleFont,
                                listStateStyle,
                                styleStrokeWidthType,
                                eventReceiver
                            )
                            TYPE -> EditArtTypeScreen(
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
                            SORT -> EditArtSortScreen(
                                sortTypeSelected = sortTypeSelected,
                                sortDirectionSelected = sortDirectionTypeSelected,
                                eventReceiver = eventReceiver
                            )
                            RESIZE -> EditArtResizeScreen(
                                sizeResolutionList,
                                sizeResolutionListSelectedIndex,
                                eventReceiver
                            )
                            null -> error("Invalid pagerState current page.")
                        }
                    }
                }
                ScreenMeasurer {
                    eventReceiver.onEvent(PreviewSpaceMeasured(size = it))
                }
            }

        }

        DialogHandler(
            dialogActive = dialogActive,
            eventReceiver = eventReceiver
        )
    }
}

@Composable
private fun DialogHandler(
    dialogActive: State<EditArtDialog>,
    eventReceiver: EventReceiver<EditArtViewEvent>
) {
    /** Only intercept when the dialog box is not visible **/
    BackHandler(enabled = dialogActive.value is EditArtDialog.None) {
        eventReceiver.onEvent(NavigateUpClicked)
    }

    when (dialogActive.value) {
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