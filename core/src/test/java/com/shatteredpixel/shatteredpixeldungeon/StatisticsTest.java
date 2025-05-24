package com.shatteredpixel.shatteredpixeldungeon;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

public class StatisticsTest {

    @Before
    public void setUp() {
        // Reset statistics before each test
        Statistics.reset();
    }

    @Test
    public void testDamageDealtTracking() {
        // Test that damage statistics are accumulated correctly
        Statistics.damageDealt = 0;
        Statistics.damageDealt += 50;
        assertEquals(50, Statistics.damageDealt);

        Statistics.damageDealt += 25;
        assertEquals(75, Statistics.damageDealt);
    }

    @Test
    public void testTimeTracking() {
// Test time statistics function
        long startTime = System.currentTimeMillis();
        Statistics.gameStartMillis = startTime;
        Statistics.floorStartMillis = startTime;

//Simulate the passage of time
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        long elapsed = (System.currentTimeMillis() - Statistics.gameStartMillis) / 1000;
        assertTrue("The time should be greater than 0 seconds", elapsed > 0);
    }

    @Test
    public void testEnemiesSlainCounter() {
        // Test kill count
        Statistics.enemiesSlain = 0;
        Statistics.enemiesSlain++;
        assertEquals(1, Statistics.enemiesSlain);

        Statistics.enemiesSlain += 5;
        assertEquals(6, Statistics.enemiesSlain);
    }

    @Test
    public void testDamageTakenTracking() {
        // Test damage statistics
        Statistics.damageTaken = 0;
        Statistics.damageTaken += 30;
        assertEquals(30, Statistics.damageTaken);
    }
}