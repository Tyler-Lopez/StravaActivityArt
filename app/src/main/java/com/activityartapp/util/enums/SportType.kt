package com.activityartapp.util.enums

import androidx.annotation.StringRes
import com.activityartapp.R
import javax.annotation.concurrent.Immutable

/**
 * Last updated 2/15/2023
 * https://developers.strava.com/docs/reference/#api-models-SportType
  */
@Immutable
enum class SportType(@StringRes val stringRes: Int) {
    ALPINE_SKI(R.string.sport_type_alpine_ski),
    BACKCOUNTRY_SKI(R.string.sport_type_backcountry_ski),
    BADMINTON(R.string.sport_type_badminton),
    CANOEING(R.string.sport_type_canoeing),
    CROSSFIT(R.string.sport_type_crossfit),
    EBIKE_RIDE(R.string.sport_type_ebike_ride),
    ELLIPTICAL(R.string.sport_type_elliptical),
    EMOUNTAINBIKE_RIDE(R.string.sport_type_emountainbike_ride),
    GOLF(R.string.sport_type_golf),
    GRAVEL_RIDE(R.string.sport_type_gravel_ride),
    HAND_CYCLE(R.string.sport_type_hand_cycle),
    HIGH_INTENSITY_INTERVAL_TRAINING(R.string.sport_type_high_intensity_interval_training),
    HIKE(R.string.sport_type_hike),
    ICE_SKATE(R.string.sport_type_ice_skate),
    INLINE_SKATE(R.string.sport_type_inline_skate),
    KAYAKING(R.string.sport_type_kayaking),
    KITE_SURF(R.string.sport_type_kite_surf),
    MOUNTAIN_BIKE_RIDE(R.string.sport_type_mountainbike_ride),
    NORDIC_SKI(R.string.sport_type_nordic_ski),
    PICKLE_BALL(R.string.sport_type_pickle_ball),
    PILATES(R.string.sport_type_pilates),
    RACQUETBALL(R.string.sport_type_racquetball),
    RIDE(R.string.sport_type_ride),
    ROCK_CLIMBING(R.string.sport_type_rock_climbing),
    ROLLER_SKI(R.string.sport_type_roller_ski),
    ROWING(R.string.sport_type_rowing),
    RUN(R.string.sport_type_run),
    SAIL(R.string.sport_type_sail),
    SKATEBOARD(R.string.sport_type_skateboard),
    SNOWBOARD(R.string.sport_type_snowboard),
    SNOWSHOE(R.string.sport_type_snowshoe),
    SOCCER(R.string.sport_type_soccer),
    SQUASH(R.string.sport_type_squash),
    STAIR_STEPPER(R.string.sport_type_stair_stepper),
    STAND_UP_PADDLING(R.string.sport_type_stand_up_paddling),
    SURFING(R.string.sport_type_surfing),
    SWIM(R.string.sport_type_swim),
    TABLE_TENNIS(R.string.sport_type_table_tennis),
    TENNIS(R.string.sport_type_tennis),
    TRAIL_RUN(R.string.sport_type_trail_run),
    VELO_MOBILE(R.string.sport_type_velo_mobile),
    VIRTUAL_RIDE(R.string.sport_type_virtual_ride),
    VIRTUAL_ROW(R.string.sport_type_virtual_row),
    VIRTUAL_RUN(R.string.sport_type_virtual_run),
    WALK(R.string.sport_type_walk),
    WEIGHT_TRAINING(R.string.sport_type_weight_training),
    WHEEL_CHAIR(R.string.sport_type_wheel_chair),
    WIND_SURF(R.string.sport_type_wind_surf),
    WORKOUT(R.string.sport_type_workout),
    YOGA(R.string.sport_type_yoga);

    companion object {
        fun fromSportTypeString(value: String): SportType {
            return when (value) {
                "AlpineSki" -> ALPINE_SKI
                "BackcountrySki" -> BACKCOUNTRY_SKI
                "Badminton" -> BADMINTON
                "Canoeing" -> CANOEING
                "Crossfit" -> CROSSFIT
                "EBikeRide" -> EBIKE_RIDE
                "Elliptical" -> ELLIPTICAL
                "EMountainBikeRide" -> EMOUNTAINBIKE_RIDE
                "Golf" -> GOLF
                "GravelRide" -> GRAVEL_RIDE
                "Handcycle" -> HAND_CYCLE
                "HighIntensityIntervalTraining" -> HIGH_INTENSITY_INTERVAL_TRAINING
                "Hike" -> HIKE
                "IceSkate" -> ICE_SKATE
                "InlineSkate" -> INLINE_SKATE
                "Kayaking" -> KAYAKING
                "Kitesurf" -> KITE_SURF
                "MountainBikeRide" -> MOUNTAIN_BIKE_RIDE
                "NordicSki" -> NORDIC_SKI
                "Pickleball" -> PICKLE_BALL
                "Pilates" -> PILATES
                "Racquetball" -> RACQUETBALL
                "Ride" -> RIDE
                "RockClimbing" -> ROCK_CLIMBING
                "RollerSki" -> ROLLER_SKI
                "Rowing" -> ROWING
                "Run" -> RUN
                "Sail" -> SAIL
                "Skateboard" -> SKATEBOARD
                "Snowboard" -> SNOWBOARD
                "Snowshoe" -> SNOWSHOE
                "Soccer" -> SOCCER
                "Squash" -> SQUASH
                "StairStepper" -> STAIR_STEPPER
                "StandUpPaddling" -> STAND_UP_PADDLING
                "Surfing" -> SURFING
                "Swim" -> SWIM
                "TableTennis" -> TABLE_TENNIS
                "Tennis" -> TENNIS
                "TrailRun" -> TRAIL_RUN
                "Velomobile" -> VELO_MOBILE
                "VirtualRide" -> VIRTUAL_RIDE
                "VirtualRow" -> VIRTUAL_ROW
                "VirtualRun" -> VIRTUAL_RUN
                "Walk" -> WALK
                "WeightTraining" -> WEIGHT_TRAINING
                "Wheelchair" -> WHEEL_CHAIR
                "Windsurf" -> WIND_SURF
                "Yoga" -> YOGA
                else -> WORKOUT
            }
        }
    }
}
