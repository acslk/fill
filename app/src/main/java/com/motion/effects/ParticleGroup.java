package com.motion.effects;

import android.graphics.Canvas;

import java.util.ArrayList;
import java.util.List;

public class ParticleGroup {

    List<Particle> particles = new ArrayList<>();
    int particleNo;
    int interval;
    int count = 0;
    float xOffset, yOffset;

    public ParticleGroup (int particleNo, int interval, float xOffset, float yOffset) {
        this.particleNo = particleNo;
        this.interval = interval;
        this.xOffset = xOffset;
        this.yOffset = yOffset;
    }

    public void changeSetting (int particleNo, int interval) {
        this.particleNo = particleNo;
        this.interval = interval;
    }

    public void update(float x, float y) {
        float vx = (float) Math.random() * 2 - 1;
        float vy = (float) Math.random() * 2 - 1;
        count++;
        if (count > interval) {
            particles.add(new Particle(x + xOffset, y + yOffset, (float) Math.random() * 5 + 2, vx, vy));
            count = 0;
        }
        while (particles.size() > particleNo)
            particles.remove(0);
        for (Particle particle : particles) {
            particle.update();
        }
    }

    public void draw (Canvas canvas, float mapX, float mapY, float interpoation) {
        for (Particle particle : particles) {
            particle.draw(canvas, mapX, mapY, interpoation);
        }
    }

}
