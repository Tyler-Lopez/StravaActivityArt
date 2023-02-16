package com.activityartapp.util

import com.activityartapp.domain.models.Activity
import com.activityartapp.presentation.editArtScreen.DateSelection
import com.activityartapp.util.enums.SportType
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import kotlin.math.roundToInt

class ActivityFilterUtilsTest {

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
        private const val ACTIVITY_TWO_DISTANCE = 5.0
        private const val ACTIVITY_TWO_ISO8601_LOCAL_DATE = "2019-05-02T05:15:09Z"
        private val ACTIVITY_TWO_TYPE = SportType.RIDE
        private const val ACTIVITY_THREE_DISTANCE = 5.0
        private const val ACTIVITY_THREE_ISO8601_LOCAL_DATE = "2020-05-02T05:15:09Z"
        private val ACTIVITY_THREE_TYPE = SportType.RIDE
        private const val ACTIVITY_FOUR_DISTANCE = 5.0
        private const val ACTIVITY_FOUR_ISO8601_LOCAL_DATE = "2022-05-02T05:15:09Z"
        private val ACTIVITY_FOUR_TYPE = SportType.RIDE

    }
    private lateinit var activityFilterUtils: ActivityFilterUtils
    private lateinit var activityOne: Activity
    private lateinit var activityTwo: Activity
    private lateinit var activityThree: Activity
    private lateinit var activityFour: Activity
    private lateinit var activitiesIncludingOnlyActivityOne: List<Activity>
    private lateinit var activities: List<Activity>

    @Before
    fun setUp() {
        activityFilterUtils = ActivityFilterUtils(TimeUtils())
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
        activities = listOf(activityOne, activityTwo, activityThree, activityFour)
    }

    /** Test [ActivityFilterUtils.filterActivities] **/
    @Test
    fun `Filtering activities when all activities satisfy all criteria is equal to original size`() {
        val includeActivityTypes = listOf(SportType.WALK)
        val unixRangeMs = Long.MIN_VALUE..Long.MAX_VALUE
        val distanceRange = Int.MIN_VALUE..Int.MAX_VALUE
        val filteredActivities = activityFilterUtils.filterActivities(
            activitiesIncludingOnlyActivityOne,
            includeActivityTypes,
            unixRangeMs,
            distanceRange
        )
        assertTrue(filteredActivities.size == activitiesIncludingOnlyActivityOne.size)
    }

    @Test
    fun `Filtering activities when all activities satisfy no criteria results in an empty list`() {
        val includeActivityTypes = listOf(ACTIVITY_ONE_TYPE)
        val unixRangeMs = 0L..1L
        val distanceRange = 0..10
        val filteredActivities = activityFilterUtils.filterActivities(
            activitiesIncludingOnlyActivityOne,
            includeActivityTypes,
            unixRangeMs,
            distanceRange
        )
        assertTrue(filteredActivities.isEmpty())
    }

    @Test
    fun `Filtering activities when an activity does not satisfy an activity type results in it being removed`() {
        val includeActivityTypes = listOf(ACTIVITY_TWO_TYPE)
        val unixRangeMs = Long.MIN_VALUE..Long.MAX_VALUE
        val distanceRange = Int.MIN_VALUE..Int.MAX_VALUE
        val filteredActivities = activityFilterUtils.filterActivities(
            activitiesIncludingOnlyActivityOne,
            includeActivityTypes,
            unixRangeMs,
            distanceRange
        )
        assertTrue(filteredActivities.isEmpty())
    }

    /** Test [ActivityFilterUtils.activityWithinUnixMs] **/
    @Test
    fun `Activity date contained within all possible milliseconds is true`() {
        val range = Long.MIN_VALUE..Long.MAX_VALUE
        assertTrue(
            activityFilterUtils.activityWithinUnixMs(
                activityOne,
                range
            )
        )
    }

    @Test
    fun `Activity date contained within range that starts less than and ends greater than it is true`() {
        // Timestamp in milliseconds = 1525238109000
        val range = 1525238108000..1525238109999
        assertTrue(
            activityFilterUtils.activityWithinUnixMs(
                activityOne,
                range
            )
        )
    }

    @Test
    fun `Activity date contained within range that starts less than and ends equal to it is true`() {
        // Timestamp in milliseconds = 1525238109000
        val range = 1525238108000..1525238109000
        assertTrue(
            activityFilterUtils.activityWithinUnixMs(
                activityOne,
                range
            )
        )
    }

    @Test
    fun `Activity date contained within range that starts equal to and ends greater than it is true`() {
        // Timestamp in milliseconds = 1525238109000
        val range = 1525238109000..1525238109999
        assertTrue(
            activityFilterUtils.activityWithinUnixMs(
                activityOne,
                range
            )
        )
    }

    @Test
    fun `Activity date contained within range that starts ends equal to it than is true`() {
        // Timestamp in milliseconds = 1525238109000
        val range = 1525238109000..1525238109000
        assertTrue(
            activityFilterUtils.activityWithinUnixMs(
                activityOne,
                range
            )
        )
    }

    @Test
    fun `Activity date not contained within range is false`() {
        // Timestamp in milliseconds = 1525238109000
        val range = 0L..1L
        assertFalse(
            activityFilterUtils.activityWithinUnixMs(
                activityOne,
                range
            )
        )
    }


    /** Test [ActivityFilterUtils.activityWithinDistanceRange] **/
    @Test
    fun `Activity distance contained within all possible integers is true`() {
        val range = Int.MIN_VALUE..Int.MAX_VALUE
        assertTrue(
            activityFilterUtils.activityWithinDistanceRange(
                activityOne,
                range
            )
        )
    }

    @Test
    fun `Activity distance contained within range that starts less than and ends greater than is true`() {
        val range = (ACTIVITY_ONE_DISTANCE - 1).toInt()..(ACTIVITY_ONE_DISTANCE + 1).toInt()
        assertTrue(
            activityFilterUtils.activityWithinDistanceRange(
                activityOne,
                range
            )
        )
    }

    @Test
    fun `Activity distance contained within range that starts equal to and ends greater than is true`() {
        val range = ACTIVITY_ONE_DISTANCE.toInt()..(ACTIVITY_ONE_DISTANCE + 1).toInt()
        assertTrue(
            activityFilterUtils.activityWithinDistanceRange(
                activityOne,
                range
            )
        )
    }

    @Test
    fun `Activity distance contained within range that starts less than and ends equal to is true`() {
        val range = (ACTIVITY_ONE_DISTANCE - 1).toInt()..ACTIVITY_ONE_DISTANCE.toInt()
        assertTrue(
            activityFilterUtils.activityWithinDistanceRange(
                activityOne,
                range
            )
        )
    }

    @Test
    fun `Activity distance contained within range that starts and ends equal to is true`() {
        val range = ACTIVITY_ONE_DISTANCE.roundToInt()..ACTIVITY_ONE_DISTANCE.roundToInt()
        assertTrue(
            activityFilterUtils.activityWithinDistanceRange(
                activityOne,
                range
            )
        )
    }

    @Test
    fun `Activity distance not contained within range is false`() {
        val range = (ACTIVITY_ONE_DISTANCE + 1).toInt()..(ACTIVITY_ONE_DISTANCE + 1).toInt()
        assertFalse(
            activityFilterUtils.activityWithinDistanceRange(
                activityOne,
                range
            )
        )
    }

    /** Test [ActivityFilterUtils.activityTypeContainedWithinTypes] **/
    @Test
    fun `Activity type contained within an empty list of types is false`() {
        val types: List<SportType> = listOf()
        assertFalse(
            activityFilterUtils.activityTypeContainedWithinTypes(
                activityOne,
                types
            )
        )
    }

    @Test
    fun `Activity type contained within a list of other types is false`() {
        val types: List<SportType> = listOf(SportType.RIDE, SportType.HIKE)
        assertFalse(
            activityFilterUtils.activityTypeContainedWithinTypes(
                activityOne,
                types
            )
        )
    }

    @Test
    fun `Activity type contained within a list of types which include the type is true`() {
        val types: List<SportType> = listOf(SportType.RIDE, SportType.WALK, SportType.HIKE)
        assertTrue(
            activityFilterUtils.activityTypeContainedWithinTypes(
                activityOne,
                types
            )
        )
    }

    @Test
    fun `Activity type contained within a list of types that only contains the type is true`() {
        val types: List<SportType> = listOf(SportType.WALK)
        assertTrue(
            activityFilterUtils.activityTypeContainedWithinTypes(
                activityOne,
                types
            )
        )
    }

    /** Test [ActivityFilterUtils.getPossibleDateSelections] **/
    @Test
    fun `Getting possible date selections when activities is empty and the previous custom range is null returns null`() {
        assert(
            activityFilterUtils.getPossibleDateSelections(
                activities = emptyList(),
                customRangeSelectedPreviousMs = null,
            ) == null
        )
    }

    @Test
    fun `Possible date selections when activities is empty and the previous custom range is not null returns null`() {
        assert(
            activityFilterUtils.getPossibleDateSelections(
                activities = emptyList(),
                customRangeSelectedPreviousMs = 0L..1L,
            ) == null
        )
    }

    @Test
    fun `Possible date selections when activities is not empty and the previous custom range is not null returns null`() {
        assert(
            activityFilterUtils.getPossibleDateSelections(
                activities = emptyList(),
                customRangeSelectedPreviousMs = 0L..1L,
            ) == null
        )
    }

    @Test
    fun `Possible date selections of a single activity in 2018 and a previous range of null`() {
        val actual = activityFilterUtils.getPossibleDateSelections(
            activities = activitiesIncludingOnlyActivityOne,
            customRangeSelectedPreviousMs = null,
        )
        val expected = listOf(
            DateSelection.All,
            DateSelection.Year(2018),
            DateSelection.Custom(
                1525238109000,
                1525238109000,
                1525238109000,
                1525238109000
            )
        )
        assertTrue(actual == expected)
    }

    @Test
    fun `Possible date selections of a multiple activities across various years and a previous range of null`() {
        val actual = activityFilterUtils.getPossibleDateSelections(
            activities = activities,
            customRangeSelectedPreviousMs = null,
        )
        val expected: List<DateSelection> = listOf(
            DateSelection.All,
            DateSelection.Year(2018),
            DateSelection.Year(2019),
            DateSelection.Year(2020),
            DateSelection.Year(2022),
            DateSelection.Custom(
                1525238109000,
                1651468509000,
                1525238109000,
                1651468509000
            )
        )
        assertTrue(actual == expected)
    }

    @Test
    fun `Possible date selections respects previous range when entirely within new date selections`() {
        val actual = activityFilterUtils.getPossibleDateSelections(
            activities = activities,
            customRangeSelectedPreviousMs = 1555238109000..1601468509000,
        )
        println(actual?.last())
        val expected: List<DateSelection> = listOf(
            DateSelection.All,
            DateSelection.Year(2018),
            DateSelection.Year(2019),
            DateSelection.Year(2020),
            DateSelection.Year(2022),
            DateSelection.Custom(
                1555238109000,
                1601468509000,
                1525238109000,
                1651468509000
            )
        )
        assertTrue(actual == expected)
    }

    @Test
    fun `Possible date selections respects previous range only within new date selections on start`() {
        val actual = activityFilterUtils.getPossibleDateSelections(
            activities = activities,
            customRangeSelectedPreviousMs = 1155238109000..1601468509000,
        )
        println(actual?.last())
        val expected: List<DateSelection> = listOf(
            DateSelection.All,
            DateSelection.Year(2018),
            DateSelection.Year(2019),
            DateSelection.Year(2020),
            DateSelection.Year(2022),
            DateSelection.Custom(
                1525238109000,
                1601468509000,
                1525238109000,
                1651468509000
            )
        )
        assertTrue(actual == expected)
    }

    @Test
    fun `Possible date selections respects previous range only within new date selections on end`() {
        val actual = activityFilterUtils.getPossibleDateSelections(
            activities = activities,
            customRangeSelectedPreviousMs = 1555238109000..1751468509000,
        )
        println(actual?.last())
        val expected: List<DateSelection> = listOf(
            DateSelection.All,
            DateSelection.Year(2018),
            DateSelection.Year(2019),
            DateSelection.Year(2020),
            DateSelection.Year(2022),
            DateSelection.Custom(
                1555238109000,
                1651468509000,
                1525238109000,
                1651468509000
            )
        )
        assertTrue(actual == expected)
    }
}