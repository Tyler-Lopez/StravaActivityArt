package com.company.athleteapiart.domain.model

enum class DistanceCondition(
    private val display: String,
) {
    LESS_THAN("Less than"),
    EQUAL_TO("Equal to"),
    GREATER_THAN("Greater than");

    override fun toString(): String = this.display
}

