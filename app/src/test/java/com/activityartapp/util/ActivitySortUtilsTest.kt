package com.activityartapp.util

import com.activityartapp.domain.models.Activity
import com.activityartapp.util.enums.EditArtSortDirectionType
import com.activityartapp.util.enums.EditArtSortType
import com.activityartapp.util.enums.SportType
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class ActivitySortUtilsTest {

    companion object {
        private const val CONSTANT_ATHLETE_ID = 0L
        private const val CONSTANT_AVERAGE_SPEED = 0.0
        private const val CONSTANT_GEAR_ID = "0"
        private const val CONSTANT_ID = 0L
        private const val CONSTANT_KUDOS_COUNT = 0
        private const val CONSTANT_LOCATION_CITY = ""
        private const val CONSTANT_LOCATION_COUNTRY = ""
        private const val CONSTANT_LOCATION_STATE = ""
        private const val CONSTANT_MAX_SPEED = 0.0
        private const val CONSTANT_MOVING_TIME = 0
        private const val CONSTANT_NAME = ""
        private const val CONSTANT_SUFFER_SCORE = 0
        private const val CONSTANT_SUMMARY_POLYLINE = ""

        private const val ACTIVITY_ONE_DISTANCE = 5.0
        private const val ACTIVITY_ONE_ISO8601_LOCAL_DATE = "2018-05-02T05:15:09Z"
        private val ACTIVITY_ONE_TYPE = SportType.WALK
        private const val ACTIVITY_TWO_DISTANCE = 10.0
        private const val ACTIVITY_TWO_ISO8601_LOCAL_DATE = "2019-05-02T05:15:09Z"
        private val ACTIVITY_TWO_TYPE = SportType.RIDE
        private const val ACTIVITY_THREE_DISTANCE = 15.0
        private const val ACTIVITY_THREE_ISO8601_LOCAL_DATE = "2020-05-02T05:15:09Z"
        private val ACTIVITY_THREE_TYPE = SportType.KAYAKING
        private const val ACTIVITY_FOUR_DISTANCE = 5.0
        private const val ACTIVITY_FOUR_ISO8601_LOCAL_DATE = "2022-05-02T05:15:09Z"
        private val ACTIVITY_FOUR_TYPE = SportType.RIDE
    }

    private lateinit var activitySortUtils: ActivitySortUtils
    private lateinit var activityOne: Activity
    private lateinit var activityTwo: Activity
    private lateinit var activityThree: Activity
    private lateinit var activityFour: Activity
    private lateinit var activitiesIncludingOnlyActivityOne: List<Activity>
    private lateinit var activities: List<Activity>

    @Before
    fun setUp() {
        activitySortUtils = ActivitySortUtils(TimeUtils())
        activityOne = object : Activity {
            override val athleteId: Long = CONSTANT_ATHLETE_ID
            override val averageSpeed: Double = CONSTANT_AVERAGE_SPEED
            override val distance: Double = ACTIVITY_ONE_DISTANCE
            override val gearId: String = CONSTANT_GEAR_ID
            override val id: Long = CONSTANT_ID
            override val iso8601LocalDate: String = ACTIVITY_ONE_ISO8601_LOCAL_DATE
            override val kudosCount: Int = CONSTANT_KUDOS_COUNT
            override val locationCity: String = CONSTANT_LOCATION_CITY
            override val locationCountry: String = CONSTANT_LOCATION_COUNTRY
            override val locationState: String = CONSTANT_LOCATION_STATE
            override val maxSpeed: Double = CONSTANT_MAX_SPEED
            override val movingTime: Int = CONSTANT_MOVING_TIME
            override val name: String = CONSTANT_NAME
            override val sufferScore: Int = CONSTANT_SUFFER_SCORE
            override val summaryPolyline: String = CONSTANT_SUMMARY_POLYLINE
            override val sportType: SportType = ACTIVITY_ONE_TYPE
        }
        activityTwo = object : Activity {
            override val athleteId: Long = CONSTANT_ATHLETE_ID
            override val averageSpeed: Double = CONSTANT_AVERAGE_SPEED
            override val distance: Double = ACTIVITY_TWO_DISTANCE
            override val gearId: String = CONSTANT_GEAR_ID
            override val id: Long = CONSTANT_ID
            override val iso8601LocalDate: String = ACTIVITY_TWO_ISO8601_LOCAL_DATE
            override val kudosCount: Int = CONSTANT_KUDOS_COUNT
            override val locationCity: String = CONSTANT_LOCATION_CITY
            override val locationCountry: String = CONSTANT_LOCATION_COUNTRY
            override val locationState: String = CONSTANT_LOCATION_STATE
            override val maxSpeed: Double = CONSTANT_MAX_SPEED
            override val movingTime: Int = CONSTANT_MOVING_TIME
            override val name: String = CONSTANT_NAME
            override val sufferScore: Int = CONSTANT_SUFFER_SCORE
            override val summaryPolyline: String = CONSTANT_SUMMARY_POLYLINE
            override val sportType: SportType = ACTIVITY_TWO_TYPE
        }
        activityThree = object : Activity {
            override val athleteId: Long = CONSTANT_ATHLETE_ID
            override val averageSpeed: Double = CONSTANT_AVERAGE_SPEED
            override val distance: Double = ACTIVITY_THREE_DISTANCE
            override val gearId: String = CONSTANT_GEAR_ID
            override val id: Long = CONSTANT_ID
            override val iso8601LocalDate: String = ACTIVITY_THREE_ISO8601_LOCAL_DATE
            override val kudosCount: Int = CONSTANT_KUDOS_COUNT
            override val locationCity: String = CONSTANT_LOCATION_CITY
            override val locationCountry: String = CONSTANT_LOCATION_COUNTRY
            override val locationState: String = CONSTANT_LOCATION_STATE
            override val maxSpeed: Double = CONSTANT_MAX_SPEED
            override val movingTime: Int = CONSTANT_MOVING_TIME
            override val name: String = CONSTANT_NAME
            override val sufferScore: Int = CONSTANT_SUFFER_SCORE
            override val summaryPolyline: String = CONSTANT_SUMMARY_POLYLINE
            override val sportType: SportType = ACTIVITY_THREE_TYPE
        }
        activityFour = object : Activity {
            override val athleteId: Long = CONSTANT_ATHLETE_ID
            override val averageSpeed: Double = CONSTANT_AVERAGE_SPEED
            override val distance: Double = ACTIVITY_FOUR_DISTANCE
            override val gearId: String = CONSTANT_GEAR_ID
            override val id: Long = CONSTANT_ID
            override val iso8601LocalDate: String = ACTIVITY_FOUR_ISO8601_LOCAL_DATE
            override val kudosCount: Int = CONSTANT_KUDOS_COUNT
            override val locationCity: String = CONSTANT_LOCATION_CITY
            override val locationCountry: String = CONSTANT_LOCATION_COUNTRY
            override val locationState: String = CONSTANT_LOCATION_STATE
            override val maxSpeed: Double = CONSTANT_MAX_SPEED
            override val movingTime: Int = CONSTANT_MOVING_TIME
            override val name: String = CONSTANT_NAME
            override val sufferScore: Int = CONSTANT_SUFFER_SCORE
            override val summaryPolyline: String = CONSTANT_SUMMARY_POLYLINE
            override val sportType: SportType = ACTIVITY_FOUR_TYPE
        }
        activitiesIncludingOnlyActivityOne = listOf(activityOne)
        activities = listOf(activityOne, activityThree, activityTwo, activityFour)
    }

    /** Test [ActivitySortUtils.sortActivities] **/
    @Test
    fun `Sorting an empty list on each sort type and direction returns an empty list`() {
        val emptyList: List<Activity> = emptyList()
        EditArtSortType.values().forEach { type ->
            EditArtSortDirectionType.values().forEach { direction ->
                assertTrue(
                    activitySortUtils.sortActivities(
                        emptyList,
                        type,
                        direction
                    ) == emptyList<List<Activity>>()
                )
            }
        }
    }

    @Test
    fun `Sorting a list of a single activity on each sort type and direction returns a list of that activity`() {
        EditArtSortType.values().forEach { type ->
            EditArtSortDirectionType.values().forEach { direction ->
                assertTrue(
                    activitySortUtils.sortActivities(
                        activitiesIncludingOnlyActivityOne,
                        type,
                        direction
                    ) == listOf(activityOne)
                )
            }
        }
    }

    @Test
    fun `Sorting a list on date ascending returns correct order`() {
        val ordered = activitySortUtils.sortActivities(
            activities,
            EditArtSortType.DATE,
            EditArtSortDirectionType.ASCENDING
        )
        assert(ordered[0] == activityOne)
        assert(ordered[1] == activityTwo)
        assert(ordered[2] == activityThree)
        assert(ordered[3] == activityFour)
    }

    @Test
    fun `Sorting a list on date descending returns correct order`() {
        val ordered = activitySortUtils.sortActivities(
            activities,
            EditArtSortType.DATE,
            EditArtSortDirectionType.DESCENDING
        )
        assert(ordered[3] == activityOne)
        assert(ordered[2] == activityTwo)
        assert(ordered[1] == activityThree)
        assert(ordered[0] == activityFour)
    }

    @Test
    fun `Sorting a list on distance ascending returns correct order and is stable`() {
        val ordered = activitySortUtils.sortActivities(
            activities,
            EditArtSortType.DISTANCE,
            EditArtSortDirectionType.ASCENDING
        )
        /** One before four is preserving relative order when the attribute is the same **/
        assert(ordered[0] == activityOne)
        assert(ordered[1] == activityFour)
        assert(ordered[2] == activityTwo)
        assert(ordered[3] == activityThree)
    }

    @Test
    fun `Sorting a list on distance descending returns correct order and is stable`() {
        val ordered = activitySortUtils.sortActivities(
            activities,
            EditArtSortType.DISTANCE,
            EditArtSortDirectionType.DESCENDING
        )
        /** One after four is preserving relative order when the attribute is the same **/
        assert(ordered[3] == activityOne)
        assert(ordered[2] == activityFour)
        assert(ordered[1] == activityTwo)
        assert(ordered[0] == activityThree)
    }

    @Test
    fun `Sorting a list on type ascending returns correct order and is stable`() {
        val ordered = activitySortUtils.sortActivities(
            activities,
            EditArtSortType.TYPE,
            EditArtSortDirectionType.ASCENDING
        )
        /** Two before four is preserving relative order when the attribute is the same **/
        assert(ordered[0] == activityThree)
        assert(ordered[1] == activityTwo)
        assert(ordered[2] == activityFour)
        assert(ordered[3] == activityOne)
    }

    @Test
    fun `Sorting a list on type descending returns correct order and is stable`() {
        val ordered = activitySortUtils.sortActivities(
            activities,
            EditArtSortType.TYPE,
            EditArtSortDirectionType.DESCENDING
        )
        /** Four before two is preserving relative order when the attribute is the same **/
        assert(ordered[3] == activityThree)
        assert(ordered[2] == activityTwo)
        assert(ordered[1] == activityFour)
        assert(ordered[0] == activityOne)
    }
}