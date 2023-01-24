package com.activityartapp.util

import com.activityartapp.domain.models.Activity
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class ActivityFilterUtilsTest {

    private lateinit var activityFilterUtils: ActivityFilterUtils
    private lateinit var activity: Activity
    private lateinit var activities: List<Activity>

    @Before
    fun setUp() {
        activityFilterUtils = ActivityFilterUtils(TimeUtils())
        activity = object : Activity {
            override val athleteId: Long = 0L
            override val averageSpeed: Double = 0.0
            override val distance: Double = 50.0
            override val gearId: String? = null
            override val id: Long = 0L
            override val iso8601LocalDate: String = "2018-05-02T05:15:09Z"
            override val kudosCount: Int = 0
            override val locationCity: String? = null
            override val locationCountry: String? = null
            override val locationState: String? = null
            override val maxSpeed: Double = 0.0
            override val movingTime: Int = 0
            override val name: String = "My Activity"
            override val sufferScore: Int? = null
            override val summaryPolyline: String? = null
            override val type: String = "Walk"
        }
        activities = listOf(activity)
    }

    /** Test [ActivityFilterUtils.filterActivities] **/
    @Test
    fun `Filtering activities when all activities satisfy all criteria is equal to original size`() {
        val includeActivityTypes = listOf("Walk")
        val unixRangeMs = Long.MIN_VALUE..Long.MAX_VALUE
        val distanceRange = Int.MIN_VALUE..Int.MAX_VALUE
        val filteredActivities = activityFilterUtils.filterActivities(
            activities,
            includeActivityTypes,
            unixRangeMs,
            distanceRange
        )
        assertTrue(filteredActivities.size == activities.size)
    }

    @Test
    fun `Filtering activities when all activities satisfy no criteria results in an empty list`() {
        val includeActivityTypes = listOf("Ride")
        val unixRangeMs = 0L..1L
        val distanceRange = 0..10
        val filteredActivities = activityFilterUtils.filterActivities(
            activities,
            includeActivityTypes,
            unixRangeMs,
            distanceRange
        )
        assertTrue(filteredActivities.isEmpty())
    }

    @Test
    fun `Filtering activities when an activity does not satisfy an activity type results in it being removed`() {
        val includeActivityTypes = listOf("Ride")
        val unixRangeMs = Long.MIN_VALUE..Long.MAX_VALUE
        val distanceRange = Int.MIN_VALUE..Int.MAX_VALUE
        val filteredActivities = activityFilterUtils.filterActivities(
            activities,
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
                activity,
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
                activity,
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
                activity,
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
                activity,
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
                activity,
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
                activity,
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
                activity,
                range
            )
        )
    }

    @Test
    fun `Activity distance contained within range that starts less than and ends greater than is true`() {
        val range = 25..55
        assertTrue(
            activityFilterUtils.activityWithinDistanceRange(
                activity,
                range
            )
        )
    }

    @Test
    fun `Activity distance contained within range that starts equal to and ends greater than is true`() {
        val range = 50..55
        assertTrue(
            activityFilterUtils.activityWithinDistanceRange(
                activity,
                range
            )
        )
    }

    @Test
    fun `Activity distance contained within range that starts less than and ends equal to is true`() {
        val range = 25..50
        assertTrue(
            activityFilterUtils.activityWithinDistanceRange(
                activity,
                range
            )
        )
    }

    @Test
    fun `Activity distance contained within range that starts and ends equal to is true`() {
        val range = 50..50
        assertTrue(
            activityFilterUtils.activityWithinDistanceRange(
                activity,
                range
            )
        )
    }

    @Test
    fun `Activity distance not contained within range is false`() {
        val range = 55..60
        assertFalse(
            activityFilterUtils.activityWithinDistanceRange(
                activity,
                range
            )
        )
    }

    /** Test [ActivityFilterUtils.activityTypeContainedWithinTypes] **/
    @Test
    fun `Activity type contained within an empty list of types is false`() {
        val types: List<String> = listOf()
        assertFalse(
            activityFilterUtils.activityTypeContainedWithinTypes(
                activity,
                types
            )
        )
    }

    @Test
    fun `Activity type contained within a list of other types is false`() {
        val types: List<String> = listOf("Ride", "Hike")
        assertFalse(
            activityFilterUtils.activityTypeContainedWithinTypes(
                activity,
                types
            )
        )
    }

    @Test
    fun `Activity type contained within a list of types which include the type is true`() {
        val types: List<String> = listOf("Ride", "Walk", "Hike")
        assertTrue(
            activityFilterUtils.activityTypeContainedWithinTypes(
                activity,
                types
            )
        )
    }

    @Test
    fun `Activity type contained within a list of types that only contains the type is true`() {
        val types: List<String> = listOf("Walk")
        assertTrue(
            activityFilterUtils.activityTypeContainedWithinTypes(
                activity,
                types
            )
        )
    }
}