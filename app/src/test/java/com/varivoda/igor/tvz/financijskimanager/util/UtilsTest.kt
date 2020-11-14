package com.varivoda.igor.tvz.financijskimanager.util

import junit.framework.Assert.assertEquals
import org.junit.Test

class UtilsTest {

    @Test
    fun testGetCurrentYear(){
        assertEquals("2020",getCurrentYear())
    }

    @Test
    fun testGetMonthWithZero(){
        assertEquals("06",getMonthWithZero(6))
    }

    @Test
    fun testGetMonthAndYearWithDefaultValues(){
        assertEquals("11.2020.", getMonthAndYearFormatted())
    }

    @Test
    fun testGetMonthAndYear(){
        assertEquals("03.2019.", getMonthAndYearFormatted(3,2019))
    }
}