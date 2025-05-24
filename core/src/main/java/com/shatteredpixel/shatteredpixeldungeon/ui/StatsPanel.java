package com.shatteredpixel.shatteredpixeldungeon.ui;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.watabou.noosa.BitmapText;
import com.watabou.noosa.ColorBlock;
import com.watabou.noosa.Game;
import com.watabou.noosa.ui.Component;

public class StatsPanel extends Component {

    private ColorBlock bg;
    private BitmapText[] textLines;
    private float elapsed = 0f;
    private ColorBlock border;
    private static long gameStartTime = 0;
    private static long currentFloorStartTime = 0;
    private static int lastFloor = -1;

    private static final float PANEL_WIDTH = 105;
    private static final float PANEL_HEIGHT = 78;
    private static final float LINE_HEIGHT = 11;
    private static final float BORDER_WIDTH = 2;

    public StatsPanel() {
        super();

        border = new ColorBlock(PANEL_WIDTH + BORDER_WIDTH * 2,
                PANEL_HEIGHT + BORDER_WIDTH * 2,
                0xFF404040);
        add(border);

        bg = new ColorBlock(PANEL_WIDTH, PANEL_HEIGHT, 0xAA808080);
        add(bg);


        textLines = new BitmapText[6];
        for (int i = 0; i < 6; i++) {
            textLines[i] = new BitmapText(PixelScene.pixelFont);
            textLines[i].hardlight(0xFFFFFF);
            add(textLines[i]);
        }

        if (gameStartTime == 0) {
            gameStartTime = System.currentTimeMillis();
        }

        currentFloorStartTime = System.currentTimeMillis();
        lastFloor = Dungeon.depth;

        setStatsText();
    }

    @Override
    public void update() {
        super.update();
        elapsed += Game.elapsed;

        if (Dungeon.depth != lastFloor) {
            currentFloorStartTime = System.currentTimeMillis();
            lastFloor = Dungeon.depth;
        }

        if (elapsed >= 1f) {
            elapsed = 0f;
            setStatsText();
        }
    }

    private void setStatsText() {
        long now = System.currentTimeMillis();

        long floorTime = (now - currentFloorStartTime) / 1000;

        long totalTime = (now - gameStartTime) / 1000;

        if (Statistics.gameStartMillis > 0 && Statistics.gameStartMillis <= now) {
            long statsTotal = (now - Statistics.gameStartMillis) / 1000;
            if (statsTotal > 0 && statsTotal < 999999) {
                totalTime = statsTotal;
            }
        }
        if (Statistics.floorStartMillis > 0 && Statistics.floorStartMillis <= now) {
            long statsFloor = (now - Statistics.floorStartMillis) / 1000;
            if (statsFloor >= 0 && statsFloor < 999999) {
                floorTime = statsFloor;
            }
        }

        textLines[0].text("DMG DEALT: " + Statistics.damageDealt);
        textLines[1].text("DMG TAKEN: " + Statistics.damageTaken);
        textLines[2].text("KILLS: " + Statistics.enemiesSlain);
        textLines[3].text("FLOOR: " + Dungeon.depth);
        textLines[4].text("F-TIME: " + formatTime(floorTime));
        textLines[5].text("T-TIME: " + formatTime(totalTime));

        for (BitmapText line : textLines) {
            line.measure();
        }
    }

    private String formatTime(long seconds) {
        if (seconds < 0) seconds = 0;
        if (seconds > 359999) seconds = 359999; // 99:59:59 max

        long hours = seconds / 3600;
        long minutes = (seconds % 3600) / 60;
        long secs = seconds % 60;

        if (hours > 0) {
            return String.format("%02d:%02d:%02d", hours, minutes, secs);
        } else {
            return String.format("%02d:%02d", minutes, secs);
        }
    }

    public static void resetGameTime() {
        gameStartTime = System.currentTimeMillis();
        currentFloorStartTime = gameStartTime;
        lastFloor = 1;
    }

    public static void resetFloorTime() {
        currentFloorStartTime = System.currentTimeMillis();
    }

    @Override
    protected void layout() {
        super.layout();
        if (camera != null) {
            float x = 5;
            float y = 5;

            border.x = x - BORDER_WIDTH;
            border.y = y - BORDER_WIDTH;
            border.size(PANEL_WIDTH + BORDER_WIDTH * 2, PANEL_HEIGHT + BORDER_WIDTH * 2);

            bg.x = x;
            bg.y = y;
            bg.size(PANEL_WIDTH, PANEL_HEIGHT);

            for (int i = 0; i < textLines.length; i++) {
                textLines[i].x = x + 5;
                textLines[i].y = y + 5 + (i * LINE_HEIGHT);
            }
        }
    }
}