package com.shatteredpixel.shatteredpixeldungeon;

import com.shatteredpixel.shatteredpixeldungeon.ui.StatsPanel;
import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

public class StatsIntegrationTest {

    private StatsPanel panel;

    @Before
    public void setUp() {
        Statistics.reset();
    }

    @Test
    public void testStatisticsDataIntegrity() {
        // Test the integrity of the statistics
        Statistics.damageDealt = 100;
        Statistics.damageTaken = 50;
        Statistics.enemiesSlain = 5;
        Statistics.gameStartMillis = System.currentTimeMillis() - 60000; // 1分钟前
        Statistics.floorStartMillis = System.currentTimeMillis() - 30000; // 30秒前

        // Verify data integrity
        assertEquals(100, Statistics.damageDealt);
        assertEquals(50, Statistics.damageTaken);
        assertEquals(5, Statistics.enemiesSlain);

        // Verify time calculation
        long gameTime = (System.currentTimeMillis() - Statistics.gameStartMillis) / 1000;
        long floorTime = (System.currentTimeMillis() - Statistics.floorStartMillis) / 1000;

        assertTrue("Game time should be greater than floor time", gameTime >= floorTime);
        assertTrue("Floor time should be within a reasonable range", floorTime >= 25 && floorTime <= 35);
    }

    @Test
    public void testStatisticsReset() {
        // Test statistics reset function
        Statistics.damageDealt = 100;
        Statistics.damageTaken = 50;
        Statistics.enemiesSlain = 5;

        Statistics.reset();

        assertEquals(0, Statistics.damageDealt);
        assertEquals(0, Statistics.damageTaken);
        assertEquals(0, Statistics.enemiesSlain);
    }
}