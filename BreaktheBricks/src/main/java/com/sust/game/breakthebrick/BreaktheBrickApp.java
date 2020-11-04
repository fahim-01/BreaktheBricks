package com.sust.game.breakthebrick;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.entity.Entities;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.Level;
import com.almasb.fxgl.entity.RenderLayer;
import com.almasb.fxgl.entity.components.IrremovableComponent;
import com.almasb.fxgl.entity.view.EntityView;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.parser.text.TextLevelParser;
import com.almasb.fxgl.physics.CollisionHandler;
import com.almasb.fxgl.settings.GameSettings;
import com.sust.game.breakthebrick.control.BallComponent;
import com.sust.game.breakthebrick.control.BatComponent;
import com.sust.game.breakthebrick.control.BrickComponent;
import javafx.scene.effect.BlendMode;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.*;
import javafx.scene.shape.Rectangle;

public class BreaktheBrickApp extends GameApplication {

    private BatComponent getBatControl() {
        return getGameWorld().getSingleton(BreaktheBrickElement.BAT).get().getComponent(BatComponent.class);
    }

    private BallComponent getBallControl() {
        return getGameWorld().getSingleton(BreaktheBrickElement.BALL).get().getComponent(BallComponent.class);
    }

    @Override
    protected void initSettings(GameSettings settings) {
        settings.setTitle("Break the Brick");
        settings.setVersion("20%");
        settings.setWidth(600);
        settings.setHeight(810);
    }

    @Override
    protected void initInput() {
        getInput().addAction(new UserAction("Move Left") {
            @Override
            protected void onAction() {
                getBatControl().left();
            }
        }, KeyCode.LEFT);

        getInput().addAction(new UserAction("Move Right") {

            @Override
            protected void onAction() {
                getBatControl().right();
            }
        }, KeyCode.RIGHT);
    }

    @Override
    protected void initGame() {
        initLevel();

    }

    private void initLevel() {
        initBackground();

        TextLevelParser parser = new TextLevelParser(new BreaktheBrickFunction());
        Level level = parser.parse("levels/level1.txt");
        getGameWorld().setLevel(level);
    }

    private void initBackground() {
        Rectangle bg0 = new Rectangle(getWidth(), getHeight(),
                new LinearGradient(getWidth() / 2, 0, getWidth() / 2, getHeight(),
                        false, CycleMethod.NO_CYCLE,
                        new Stop(0.2, Color.DEEPSKYBLUE), new Stop(0.8, Color.BLACK)));

        Rectangle bg1 = new Rectangle(getWidth(), getHeight(), Color.color(0, 0, 0, 0.2));
        bg1.setBlendMode(BlendMode.EXCLUSION);

        EntityView bg = new EntityView();
        bg.addNode(bg0);
        bg.addNode(bg1);

        Entities.builder()
                .viewFromNode(bg)
                .renderLayer(RenderLayer.BACKGROUND)
                .with(new IrremovableComponent())
                .buildAndAttach(getGameWorld());

        Entity screenBounds = Entities.makeScreenBounds(40);
        screenBounds.addComponent(new IrremovableComponent());

        getGameWorld().addEntity(screenBounds);
    }

    @Override
    protected void initPhysics() {
        getPhysicsWorld().setGravity(0, 0);

        getPhysicsWorld().addCollisionHandler(new CollisionHandler(BreaktheBrickElement.BALL, BreaktheBrickElement.BRICK) {

            @Override
            protected void onCollisionBegin(Entity ball, Entity brick) {
                brick.getComponent(BrickComponent.class).onHit();
            }
        });
    }

    @Override
    protected void initUI() {

        getBallControl().release();

    }

    public static void main(String[] args) {
        launch(args);
    }
}
