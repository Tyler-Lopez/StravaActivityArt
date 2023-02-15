package com.activityartapp.util

import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class ParseNumberFromStringUtilsTest {

    private lateinit var parseNumberFromStringUtils: ParseNumberFromStringUtils

    @Before
    fun setUp() {
        parseNumberFromStringUtils = ParseNumberFromStringUtils()
    }

    @Test
    fun `Parsing an empty string returns 0 when 0 is the lower bound`() {
        val toParse = String()
        val parsedResult = parseNumberFromStringUtils.parse(toParse, 0.0..5.0)
        assertEquals(parsedResult, 0.0, 0.0001)
    }

    @Test
    fun `Parsing a string with only non-numeric characters returns 0 when 0 is the lower bound`() {
        val toParse = "-.@fda!&"
        val parsedResult = parseNumberFromStringUtils.parse(toParse, 0.0..5.0)
        assertEquals(parsedResult, 0.0, 0.0001)
    }

    @Test
    fun `Parsing a number with a negative sign in the wrong place just returns the positive number`() {
        val toParse = "5-0"
        val parsedResult = parseNumberFromStringUtils.parse(toParse, 0.0..100.0)
        assertEquals(parsedResult, 50.0, 0.0001)
    }


    @Test
    fun `Parsing a number with a negative sign in the correct place returns a positive number`() {
        val toParse = "-50"
        val parsedResult = parseNumberFromStringUtils.parse(toParse, -100.0..100.0)
        assertEquals(parsedResult, -50.0, 0.0001)
    }

    @Test
    fun `Parsing a number infinitely small returns the upper bound`() {
        val toParse = buildString {
            append('-')
            for (i in 0..10000) {
                append('5')
            }
        }
        val parsedResult = parseNumberFromStringUtils.parse(toParse, 0.0..5.0)
        assertEquals(parsedResult, 0.0, 0.0001)
    }

    @Test
    fun `Parsing a number infinitely large returns the upper bound`() {
        val toParse = buildString {
            for (i in 0..10000) {
                append('5')
            }
        }
        val parsedResult = parseNumberFromStringUtils.parse(toParse, 0.0..5.0)
        assertEquals(parsedResult, 5.0, 0.0001)
    }

    @Test
    fun `Parsing a number formatted correctly returns a correct result`() {
        val toParse = "13.1"
        val parsedResult = parseNumberFromStringUtils.parse(toParse, 0.0..20.0)
        assertEquals(parsedResult, 13.1, 0.0001)
    }

    @Test
    fun `Parsing a number formatted with too many decimals returns a correct result`() {
        val toParse = "13.1000.0.0.0.0.0.0.0.0.0.0.~~~~~"
        val parsedResult = parseNumberFromStringUtils.parse(toParse, 0.0..20.0)
        assertEquals(parsedResult, 13.1, 0.0001)
    }

    @Test
    fun `Parsing a number formatted with too many decimals ignores the first and returns a correct result`() {
        val toParse = "1...............9.2.3.4.5"
        val parsedResult = parseNumberFromStringUtils.parse(toParse, 0.0..20.0)
        assertEquals(parsedResult, 1.92345, 0.00001)
    }
}