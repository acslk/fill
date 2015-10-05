//package com.motion.collidables;
//
//import android.content.Context;
//import android.graphics.Canvas;
//import android.graphics.drawable.Drawable;
//
//import com.motion.R;
//import com.motion.game.Ball;
//import com.motion.game.Game;
//import com.motion.game.MovingObj;
//
//public class SlideBlock extends Collidable {
//
//    private static final float THRESHOLD = Ball.Default_MAXV;
//
//    private static Drawable baseImage;
//    float vx = 0, vy = 0;
//    float multiplier;
//    MovingObj.Direction slideDirection = MovingObj.Direction.NULL;
//
//    public SlideBlock(int x, int y, int width, int height, float multiplier) {
//        super(x,y,width,height);
//        this.multiplier = multiplier;
//    }
//
//    @Override
//    public void collide(Ball ball, Ball lastBall) {
//        float ballvx = ball.vx;
//        float ballvy = ball.vy;
//        MovingObj.Direction ballFrom = bounceOff(ball, lastBall);
//        if (slideDirection == MovingObj.Direction.NULL) {
//            switch (ballFrom) {
//                case LEFT:
//                    if (ballvx >= THRESHOLD) {
//                        vx = multiplier * ballvx;
//                        slideDirection = Direction.RIGHT;
//                    }
//                    break;
//                case RIGHT:
//                    if (ballvx <= -THRESHOLD) {
//                        vx = multiplier * ballvx;
//                        slideDirection = Direction.LEFT;
//                    }
//                    break;
//                case TOP:
//                    if (ballvy >= THRESHOLD) {
//                        vy = multiplier * ballvy;
//                        slideDirection = Direction.BOTTOM;
//                    }
//                    break;
//                case BOTTOM:
//                    if (ballvy <= -THRESHOLD) {
//                        vy = multiplier * ballvy;
//                        slideDirection = Direction.TOP;
//                    }
//                    break;
//            }
//        }
//    }
//
//    @Override
//    public void update(Game game) {
//        x += vx;
//        y += vy;
////        if (slideDirection != Direction.NULL) {
////            for (Collidable c : game.collidables) {
////                if (c != this && c.containsRectObj(this)) {
////                    switch (slideDirection) {
////                        case LEFT:
////                            x = c.x + c.width;
////                            break;
////                        case RIGHT:
////                            x = c.x - width;
////                            break;
////                        case TOP:
////                            y = c.y + c.height;
////                            break;
////                        case BOTTOM:
////                            y = c.y - height;
////                            break;
////                    }
////                    slideDirection = Direction.NULL;
////                    vx = 0;
////                    vy = 0;
////                    break;
////                }
////            }
////        }
//    }
//
//    @Override
//    public void draw(Canvas canvas, float mapX, float mapY, float interpoation) {
//        baseImage.setAlpha(255);
//        baseImage.setBounds(getOffsetRect(mapX, mapY));
//        baseImage.draw(canvas);
//    }
//
//    public static void loadResource (Context context) {
//        baseImage = context.getResources().getDrawable(R.drawable.movable);
//    }
//}
