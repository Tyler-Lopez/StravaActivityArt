package com.company.activityart.presentation.edit_art_screen.subscreens.resize

import android.util.Size
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.RadioButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.company.activityart.R
import com.company.activityart.architecture.EventReceiver
import com.company.activityart.presentation.common.type.Body
import com.company.activityart.presentation.common.type.SubheadHeavy
import com.company.activityart.presentation.edit_art_screen.EditArtViewEvent
import com.company.activityart.presentation.edit_art_screen.EditArtViewEvent.ArtMutatingEvent.SizeChanged
import com.company.activityart.presentation.edit_art_screen.subscreens.filters.Section
import com.company.activityart.presentation.edit_art_screen.subscreens.resize.ResolutionType.ComputerResolutionType
import com.company.activityart.presentation.edit_art_screen.subscreens.resize.ResolutionType.PrintResolutionType
import com.company.activityart.presentation.ui.theme.spacing

@Composable
fun EditArtResizeScreen(
    selectedSize: Size,
    eventReceiver: EventReceiver<EditArtViewEvent>
) {
    Column(Modifier.verticalScroll(rememberScrollState())) {
        Section(
            header = stringResource(R.string.edit_art_resize_header),
            description = stringResource(R.string.edit_art_resize_description),
        ) {

            ComputerResolutionType
                .values()
                .toMutableList<ResolutionType>()
                .plus(PrintResolutionType.values().toList())
                .forEach {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(spacing.medium)
                    ) {
                        RadioButton(
                            selected = selectedSize.run { it.widthPx == width && it.heightPx == height },
                            onClick = {
                                eventReceiver.onEvent(SizeChanged(it.widthPx, it.heightPx))
                            }
                        )
                        Column(verticalArrangement = Arrangement.spacedBy(spacing.small)) {
                            Body(
                                text = when (it) {
                                    is ComputerResolutionType -> stringResource(it.stringResourceId)
                                    is PrintResolutionType -> stringResource(
                                        it.stringResourceId,
                                        it.widthValue,
                                        "\"",
                                        it.heightValue,
                                        "\""
                                    )
                                }
                            )
                            SubheadHeavy(
                                text = stringResource(
                                    R.string.edit_art_resize_pixels_placeholder,
                                    it.widthPx,
                                    it.heightPx
                                )
                            )
                        }
                    }
                }
        }
    }
}

private sealed interface ResolutionType {

    val widthPx: Int
    val heightPx: Int
    val stringResourceId: Int

    enum class ComputerResolutionType(
        override val stringResourceId: Int,
        override val widthPx: Int,
        override val heightPx: Int
    ) : ResolutionType {
        COMPUTER_WALLPAPER(R.string.edit_art_resize_option_computer_wallpaper, 1920, 1080),
        COMPUTER_ULTRA_WIDE(
            R.string.edit_art_resize_option_computer_ultra_wide_wallpaper,
            3440,
            1440
        ),
    }

    enum class PrintResolutionType(
        override val widthPx: Int,
        override val heightPx: Int,
        val widthValue: Int,
        val heightValue: Int
    ) : ResolutionType {

        PHOTO_8X8(2400, 2400, 8, 8),
        PHOTO_8X10(2400, 3000, 8, 10),
        PHOTO_8X12(2400, 3600, 8, 12),
        PHOTO_10X20(3000, 6000, 10, 20),
        PHOTO_11X14(3300, 4200, 11, 14),
        PHOTO_12X18(3600, 5400, 12, 18),
        PHOTO_12X24(3600, 7200, 12, 24),
        PHOTO_12X36(3600, 10800, 12, 36),
        PHOTO_16X16(4800, 4800, 16, 16),
        PHOTO_16X20(4800, 6000, 16, 20),
        PHOTO_16X24(4800, 7200, 16, 24),
        PHOTO_20X20(6000, 6000, 20, 20),
        PHOTO_20X30(6000, 9000, 20, 30),
        PHOTO_20X40(6000, 12000, 20, 40);

        override val stringResourceId: Int = R.string.edit_art_resize_option_print
    }
}
