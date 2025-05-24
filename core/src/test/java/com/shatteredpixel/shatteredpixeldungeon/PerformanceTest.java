package com.shatteredpixel.shatteredpixeldungeon;

import org.junit.Test;
import static org.junit.Assert.*;

public class PerformanceTest {

    @Test
    public void testStatisticsUpdatePerformance() {
        // Test statistics data update performance
        long startTime = System.nanoTime();

        // Simulate frequent statistics updates
        for (int i = 0; i < 10000; i++) {
            Statistics.damageDealt += 1;
            Statistics.damageTaken += 1;
            Statistics.enemiesSlain += 1;
        }

        long endTime = System.nanoTime();
        long duration = (endTime - startTime) / 1000000; // 转换为毫秒

        assertTrue("Statistics updates should be completed within a reasonable time",
                duration < 50); // 小于50毫秒

        // Verify data correctness
        assertEquals(10000, Statistics.damageDealt);
        assertEquals(10000, Statistics.damageTaken);
        assertEquals(10000, Statistics.enemiesSlain);
    }

    @Test
    public void testTimeCalculationPerformance() {
        // Test time calculation performance
        Statistics.gameStartMillis = System.currentTimeMillis();
        Statistics.floorStartMillis = System.currentTimeMillis();

        long startTime = System.nanoTime();

        //Simulate frequent time calculations
        for (int i = 0; i < 1000; i++) {
            long gameTime = (System.currentTimeMillis() - Statistics.gameStartMillis) / 1000;
            long floorTime = (System.currentTimeMillis() - Statistics.floorStartMillis) / 1000;

            // Make sure the calculation is actually useful
            assertTrue(gameTime >= 0);
            assertTrue(floorTime >= 0);
        }

        long endTime = System.nanoTime();
        long duration = (endTime - startTime) / 1000000;

        assertTrue("Time calculation should be completed within a reasonable time",
                duration < 100);
    }
}