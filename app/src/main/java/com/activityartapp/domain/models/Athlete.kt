package com.activityartapp.domain.models

import java.util.concurrent.TimeUnit


/**
 * Interface abstraction based on the following StackOverflow response.
 * https://stackoverflow.com/questions/53508484/clean-architecture-share-same-models-entities-with-different-layers
 */
interface Athlete {
    /** Correspond to "athlete": {
    "id": 94894359,
    "username": "tylerlopez",
    "resource_state": 2,
    "firstname": "Tyler",
    "lastname": "Lopez",
    "bio": "I just walk",
    "city": "",
    "state": "",
    "country": null,
    "sex": "M",
    "premium": false,
    "summit": false,
    "created_at": "2021-11-02T22:12:19Z",
    "updated_at": "2023-01-20T17:47:10Z",
    "badge_type_id": 0,
    "weight": 0.0,
    "profile_medium": "https://dgalywyr863hv.cloudfront.net/pictures/athletes/94894359/22514957/7/medium.jpg",
    "profile": "https://dgalywyr863hv.cloudfront.net/pictures/athletes/94894359/22514957/7/large.jpg",
    "friend": null,
    "follower": null
    }
    **/
    val id: Long
    val userName: String?
    val resourceState: Int?
    val firstName: String?
    val lastName: String?
    val bio: String?
    val city: String?
    val state: String?
    val country: String?
    val sex: String?
    val premium: Boolean?
    val summit: Boolean?
    val createdAt: String?
    val updatedAt: String?
    val badgeTypeId: Int?
    val weight: Double?
    val profileMedium: String?
    val profile: String?
    val friend: Boolean?
    val follower: Boolean?

    val lastCachedYearMonth: Map<Int, Int>
}