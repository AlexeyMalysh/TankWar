package com.example.tankwar;

import android.content.Context;

import com.example.tankwar.GameObjects.Enemy;
import com.example.tankwar.GameObjects.GameObject;
import com.example.tankwar.GameObjects.Player;

import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CopyOnWriteArrayList;

public class EnemySpawner {

    private int maxEnemies = 2;
    private int enemiesPerWave = 1;
    private int enemiesDestroyed = 0;
    private CopyOnWriteArrayList<Enemy> enemies;
    private final Context context;
    private final Player player;

    public EnemySpawner(Context context, Player player) {
        this.enemies = new CopyOnWriteArrayList<>();
        this.context = context;
        this.player = player;
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                spawnEnemies();
            }
        }, 5000, 5000);
    }

    public void update(List<GameObject> objects) {

        CopyOnWriteArrayList<Enemy> activeEnemies = new CopyOnWriteArrayList<>();

        for (Enemy enemy : enemies) {
            enemy.update(objects);

            // Any enemy that has not been destroyed will be looped through again
            if (!enemy.isDisposed()) {
                activeEnemies.add(enemy);
                continue;
            }

            enemiesDestroyed++;

            // Increase max enemies after every 10 kills
            if (enemiesDestroyed % 10 == 0) {
                incrementMaxEnemies();
            }

            // Increase enemies spawn after every 20 kills
            if (enemiesDestroyed % 20 == 0) {
                incrementWave();
            }
        }

        setEnemies(activeEnemies);
    }

    private void spawnEnemies() {
        // If at max enemies do not attempt to spawn more
        if (enemies.size() >= maxEnemies) return;

        int enemiesToAdd;

        // If adding new wave of enemies does not exceed max amount of enemies, add new wave
        if (enemies.size() + enemiesPerWave <= maxEnemies) {
            enemiesToAdd = enemiesPerWave;
        } else {
            // If unable to add full wave of enemies, add remainder of enemies to reach max amount of enemies
            enemiesToAdd = maxEnemies - enemies.size();
        }

        for (int i = 0; i < enemiesToAdd; i++) {
            spawn();
        }
    }

    private void spawn() {
        float[] coords = generateSpawnCoords();
        enemies.add(new Enemy(context, player, coords[0], coords[1]));
    }

    private void incrementWave() {
        // After 3 enemies it becomes too difficult
        if (enemiesPerWave < 3) {
            enemiesPerWave += 1;
        }
    }

    public void incrementMaxEnemies() {
        maxEnemies += 1;
    }

    public CopyOnWriteArrayList<Enemy> getEnemies() {
        return this.enemies;
    }

    public void setEnemies(CopyOnWriteArrayList<Enemy> enemies) {
        this.enemies = enemies;
    }

    public int getMaxEnemies() {
        return maxEnemies;
    }

    public int getEnemiesPerWave() {
        return enemiesPerWave;
    }

    private float[] generateSpawnCoords() {
        Random rand = new Random();

        int n = rand.nextInt(4);

        int x;
        int y;

        switch (n) {
            case 0:
                //Enemy will spawn at top
                x = rand.nextInt(MainActivity.getScreenWidth());
                y = MainActivity.getScreenHeight() + 100;
                break;
            case 1:
                // Enemy will spawn at bottom
                x = rand.nextInt(MainActivity.getScreenWidth());
                y = -100;
                break;
            case 2:
                // Enemy will spawn at left
                x = -100;
                y = rand.nextInt(MainActivity.getScreenHeight());
                break;
            case 3:
                // Enemy will spawn at right
                x = MainActivity.getScreenWidth() + 100;
                y = rand.nextInt(MainActivity.getScreenHeight());
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + n);
        }

        return new float[]{x, y};
    }

}
