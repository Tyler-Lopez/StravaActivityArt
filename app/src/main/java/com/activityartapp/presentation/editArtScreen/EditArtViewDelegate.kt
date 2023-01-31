package com.activityartapp.presentation.editArtScreen

import androidx.activity.compose.BackHandler
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.activityartapp.R
import com.activityartapp.presentation.common.AppBarScaffold
import com.activityartapp.presentation.common.ScreenBackground
import com.activityartapp.presentation.common.type.SubheadHeavy
import com.activityartapp.presentation.editArtScreen.EditArtHeaderType.*
import com.activityartapp.presentation.editArtScreen.EditArtViewEvent.NavigateUpClicked
import com.activityartapp.presentation.editArtScreen.EditArtViewEvent.SaveClicked
import com.activityartapp.presentation.editArtScreen.EditArtDialogType.*
import com.activityartapp.presentation.editArtScreen.composables.EditArtDialogInfo
import com.activityartapp.presentation.editArtScreen.composables.EditArtDialogNavigateUp
import com.activityartapp.presentation.editArtScreen.subscreens.filters.EditArtFiltersScreen
import com.activityartapp.presentation.editArtScreen.subscreens.preview.EditArtPreview
import com.activityartapp.presentation.editArtScreen.subscreens.resize.EditArtResizeScreen
import com.activityartapp.presentation.editArtScreen.subscreens.sort.EditArtSortScreen
import com.activityartapp.presentation.editArtScreen.subscreens.style.EditArtStyleViewDelegate
import com.activityartapp.presentation.editArtScreen.subscreens.type.EditArtTypeScreen
import com.activityartapp.presentation.ui.theme.White
import com.activityartapp.presentation.ui.theme.spacing
import com.activityartapp.util.classes.YearMonthDay
import com.activityartapp.util.enums.BackgroundType
import com.google.accompanist.pager.ExperimentalPagerApi

private const val DISABLED_ALPHA = 0.5f
private const val ENABLED_ALPHA = 1.0f

/**
 * A complex screen featuring [EditArtTabLayout]
 */
@OptIn(ExperimentalPagerApi::class)
@Composable
fun EditArtViewDelegate(viewModel: EditArtViewModel) {
    viewModel.viewState.collectAsState().value?.apply {
        val continueEnabled = (this@apply as? EditArtViewState.Standby)?.atLeastOneActivitySelected
        AppBarScaffold(
            text = stringResource(R.string.action_bar_edit_art_header),
            onNavigateUp = { viewModel.onEventDebounced(NavigateUpClicked) },
            actions = {
                IconButton(
                    onClick = { viewModel.onEventDebounced(SaveClicked) },
                    enabled = continueEnabled ?: false
                ) {
                    SubheadHeavy(
                        text = stringResource(
                            R.string.button_continue_uppercase
                        ),
                        textColor = White.copy(
                            alpha = if (continueEnabled == true) {
                                ENABLED_ALPHA
                            } else {
                                DISABLED_ALPHA
                            }
                        )
                    )
                }
                Spacer(modifier = Modifier.width(spacing.medium))
            },
            tabLayout = {
                pagerStateWrapper.apply {
                    EditArtTabLayout(
                        pagerHeaders = pagerHeaders,
                        pagerState = pagerState,
                        eventReceiver = viewModel
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
                    ScreenBackground(
                        horizontalAlignment = if (it != PREVIEW) Alignment.Start else Alignment.CenterHorizontally,
                        padding = if (it != PREVIEW) spacing.medium else 0.dp,
                        scrollState = when (it) {
                            FILTERS -> scrollStateFilter
                            STYLE -> scrollStateStyle
                            TYPE -> scrollStateType
                            SORT -> scrollStateSort
                            RESIZE -> scrollStateResize
                            else -> null
                        },
                        scrollingEnabled = it != PREVIEW
                    ) {
                        when (it) {
                            PREVIEW -> EditArtPreview(
                                atLeastOneActivitySelected,
                                styleBackgroundType == BackgroundType.TRANSPARENT,
                                bitmap,
                                viewModel
                            )
                            FILTERS -> YearMonthDay.run {
                                EditArtFiltersScreen(
                                    filterActivitiesCountDate,
                                    filterActivitiesCountDistance,
                                    filterActivitiesCountType,
                                    filterDateSelections,
                                    filterDateSelectionIndex,
                                    filterDistanceSelectedEnd?.let {
                                        filterDistanceSelectedStart?.rangeTo(it)
                                    },
                                    filterDistanceTotalEnd?.let {
                                        filterDistanceTotalStart?.rangeTo(it)
                                    },
                                    filterTypes?.toList(),
                                    viewModel
                                )
                            }
                            STYLE -> EditArtStyleViewDelegate(
                                styleBackgroundColors,
                                styleBackgroundType,
                                styleActivities,
                                styleFont,
                                styleStrokeWidthType,
                                viewModel
                            )
                            TYPE -> EditArtTypeScreen(
                                typeActivitiesDistanceMetersSummed,
                                //         typeAthleteName,
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
                                viewModel
                            )
                            SORT -> EditArtSortScreen(
                                sortTypeSelected = sortTypeSelected,
                                sortDirectionSelected = sortDirectionTypeSelected,
                                eventReceiver = viewModel
                            )
                            RESIZE -> EditArtResizeScreen(
                                //     sizeCustomHeightPx,
                                //    sizeCustomWidthPx,
                                sizeCustomMinPx..sizeCustomMaxPx,
                                sizeResolutionList,
                                sizeResolutionListSelectedIndex,
                                viewModel
                            )
                            null -> error("Invalid pagerState current page.")
                        }
                    }
                }
            }
        }

        /** Only intercept when the dialog box is not visible **/
        BackHandler(enabled = dialogActive == NONE) {
            viewModel.onEvent(NavigateUpClicked)
        }

        println("Here, dialog active is $dialogActive")
        when (dialogActive) {
            INFO_CHECKERED_BACKGROUND -> EditArtDialogInfo(
                body = stringArrayResource(id = R.array.edit_art_dialog_info_background_checkered),
                eventReceiver = viewModel
            )
            INFO_TRANSPARENT -> EditArtDialogInfo(
                body = stringArrayResource(id = R.array.edit_art_dialog_info_background_transparent),
                eventReceiver = viewModel
            )
            NAVIGATE_UP -> EditArtDialogNavigateUp(eventReceiver = viewModel)
            NONE -> {} // No-op
        }
    }
}