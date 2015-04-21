package com.example.david.motion.effects;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dave on 21/04/15.
 */
public class ParticleGroup {

    List<Particle> particles = new ArrayList<>();
    Paint paint = new Paint();
    int particleNo;
    float xOffset, yOffset;

    public ParticleGroup (int particleNo, float xOffset, float yOffset) {
        this.particleNo = particleNo;
        this.xOffset = xOffset;
        this.yOffset = yOffset;
        paint.setColor(Color.WHITE);
    }

    public void update(float x, float y) {
        float vx = (float) Math.random() * 4 - 2;
        float vy = (float) Math.random() * 4 - 2;
        particles.add(new Particle(x + xOffset, y + yOffset, (float)Math.random()*5 + 2, vx, vy, paint));
        if (particles.size() > particleNo)
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
