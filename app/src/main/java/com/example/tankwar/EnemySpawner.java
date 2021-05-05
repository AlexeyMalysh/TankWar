package com.example.tankwar;

import android.content.Context;

import com.example.tankwar.GameObjects.Enemy;
import com.example.tankwar.GameObjects.Player;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CopyOnWriteArrayList;

public class EnemySpawner {

    private int maxEnemies = 2;
    private int enemiesPerWave = 1;
    private CopyOnWriteArrayList<Enemy> enemies;
    private Context context;
    private Player player;

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

    private void spawnEnemies() {
        // If at max enemies do not attempt to spawn more
        if (enemies.size() >= maxEnemies) {
            return;
        }

        int enemiesToAdd;

        // If adding new wave of enemies does not exceed max amount of enemies, add new wave
        if (enemies.size() + enemiesPerWave <= maxEnemies) {
            enemiesToAdd = enemiesPerWave;
        } else {
            // If unable to add full wave of enemies add remainder of enemies to reach max amount of enemies
            enemiesToAdd = maxEnemies - enemies.size();
        }

        for (int i = 0; i < enemiesToAdd; i++) {
            spawn();
        }
    }

    private void spawn() {
        enemies.add(new Enemy(context, player));
    }

    public CopyOnWriteArrayList<Enemy> getEnemies() {
        return this.enemies;
    }

    public void increaseIntensity() {
        incrementWave();
        incrementMaxEnemies();
    }

    private void incrementWave() {
        // Maximum enemies spawn at a time is 3 as it becomes too difficult at higher numbers (subject to change)
        if (enemiesPerWave < 3) {
            enemiesPerWave += 1;
        }
    }

    private void incrementMaxEnemies() {
        // Maximum enemies on screen is 10 as it becomes too difficult at higher numbers (subject to change)
        if (maxEnemies < 10) {
            maxEnemies += 2;
        }
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

}
