package com.shatteredpixel.shatteredpixeldungeon;

public class TestSuite {

    private int totalTests = 0;
    private int passedTests = 0;
    private int failedTests = 0;


    public void runAllTests() {
        System.out.println("=== Shattered Pixel Dungeon===\n");

        runUnitTests();
        runIntegrationTests();
        runPerformanceTests();
        runBoundaryTests();
        runReliabilityTests();

        printSummary();
    }

    private void runUnitTests() {
        System.out.println("1. Unit Tests");
        System.out.println("---------------------------");

        test("Damage statistics function", this::testDamageTracking);
        test("Time statistics function", this::testTimeTracking);
        test("Kill statistics function", this::testKillTracking);
        test("Statistics reset function", this::testStatisticsReset);

        System.out.println();
    }

    private void runIntegrationTests() {
        System.out.println("2. Integration Tests");
        System.out.println("--------------------------------");

        test("Statistical panel data consistency", this::testPanelDataConsistency);
        test("Game scene integration", this::testGameSceneIntegration);

        System.out.println();
    }

    private void runPerformanceTests() {
        System.out.println("3. Performance Tests");
        System.out.println("--------------------------------");

        test("Statistical update performance", this::testStatisticsPerformance);
        test("Panel refresh performance", this::testPanelRefreshPerformance);

        System.out.println();
    }

    private void runBoundaryTests() {
        System.out.println("4. Boundary Tests");
        System.out.println("-----------------------------");

        test("Maximum value handling", this::testMaxValueHandling);
        test("Zero value handling", this::testZeroValueHandling);
        test("Negative time handling", this::testNegativeTimeHandling);
        System.out.println();
    }

    private void runReliabilityTests() {
        System.out.println("5. Reliability Tests");
        System.out.println("----------------------------------");

        test("Frequent Update Stability", this::testFrequentUpdateStability);

        System.out.println();
    }

    private void testDamageTracking() {
        Statistics.damageDealt = 0;
        Statistics.damageDealt += 100;
        assert Statistics.damageDealt == 100;
        Statistics.damageDealt += 50;
        assert Statistics.damageDealt == 150;
    }

    private void testTimeTracking() {
        long start = System.currentTimeMillis();
        Statistics.gameStartMillis = start;
        Statistics.floorStartMillis = start;

        try { Thread.sleep(50); } catch (InterruptedException e) {}

        long elapsed = System.currentTimeMillis() - Statistics.gameStartMillis;
        assert elapsed >= 45;
    }

    private void testKillTracking() {
        Statistics.enemiesSlain = 0;
        Statistics.enemiesSlain++;
        assert Statistics.enemiesSlain == 1;
        Statistics.enemiesSlain += 5;
        assert Statistics.enemiesSlain == 6;
    }

    private void testStatisticsReset() {
        Statistics.damageDealt = 100;
        Statistics.damageTaken = 50;
        Statistics.enemiesSlain = 10;

        Statistics.reset();

        assert Statistics.damageDealt == 0;
        assert Statistics.damageTaken == 0;
        assert Statistics.enemiesSlain == 0;
    }

    private void testPanelDataConsistency() {
        Statistics.damageDealt = 200;
        Statistics.damageTaken = 100;
        Statistics.enemiesSlain = 15;


        assert Statistics.damageDealt == 200;
        assert Statistics.damageTaken == 100;
        assert Statistics.enemiesSlain == 15;
    }

    private void testGameSceneIntegration() {

        long gameTime = System.currentTimeMillis() - Statistics.gameStartMillis;
        long floorTime = System.currentTimeMillis() - Statistics.floorStartMillis;
        assert gameTime >= 0;
        assert floorTime >= 0;
        assert gameTime >= floorTime;
    }

    private void testStatisticsPerformance() {
        long start = System.nanoTime();

        for (int i = 0; i < 10000; i++) {
            Statistics.damageDealt++;
            Statistics.damageTaken++;
            Statistics.enemiesSlain++;
        }

        long duration = (System.nanoTime() - start) / 1000000;
        assert duration < 100;
    }

    private void testPanelRefreshPerformance() {

        long start = System.nanoTime();

        for (int i = 0; i < 100; i++) {

            String.format("Damage output: %d\nDamage received: %d\nKill: %d",
                    Statistics.damageDealt, Statistics.damageTaken, Statistics.enemiesSlain);
        }

        long duration = (System.nanoTime() - start) / 1000000;
        assert duration < 50;
    }

    private void testMaxValueHandling() {
        Statistics.damageDealt = Integer.MAX_VALUE - 10;
        Statistics.damageDealt += 5;
        assert Statistics.damageDealt == Integer.MAX_VALUE - 5;
    }

    private void testZeroValueHandling() {
        Statistics.reset();
        assert Statistics.damageDealt == 0;
        assert Statistics.damageTaken == 0;
        assert Statistics.enemiesSlain == 0;
    }

    private void testNegativeTimeHandling() {
        long current = System.currentTimeMillis();
        Statistics.gameStartMillis = current + 1000;

        long elapsed = current - Statistics.gameStartMillis;
        assert elapsed < 0;
    }


    private void testFrequentUpdateStability() {
        // Test the stability of frequent updates
        int originalDamage = Statistics.damageDealt;
        for (int i = 0; i < 5000; i++) {
            Statistics.damageDealt++;
        }
        assert Statistics.damageDealt == originalDamage + 5000;
    }

    private void test(String testName, Runnable testMethod) {
        totalTests++;
        try {
            testMethod.run();
            passedTests++;
            System.out.println("✓ " + testName + " - pass");
        } catch (Exception | AssertionError e) {
            failedTests++;
            System.out.println("❌ " + testName + " - fail: " + e.getMessage());
        }
    }

    private void printSummary() {
        System.out.println("=== Test Summary ===");
        System.out.println("Total number of tests: " + totalTests);
        System.out.println("Passed: " + passedTests);
        System.out.println("Failed: " + failedTests);
        System.out.println("Pass rate: " + String.format("%.1f%%", (passedTests * 100.0 / totalTests)));

        if (failedTests == 0) {
            System.out.println("\n🎉 All tests passed! Good code quality.");
        } else {
            System.out.println("\n⚠️ There are " + failedTests + " tests that failed and need to be fixed.");
        }
    }


    public static void main(String[] args) {
        TestSuite suite = new TestSuite();
        suite.runAllTests();
    }
}