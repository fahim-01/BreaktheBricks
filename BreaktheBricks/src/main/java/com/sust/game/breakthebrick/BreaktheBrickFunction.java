package com.sust.game.breakthebrick;

import com.almasb.fxgl.app.FXGL;
import com.almasb.fxgl.entity.*;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.particle.ParticleComponent;
import com.almasb.fxgl.particle.ParticleEmitter;
import com.almasb.fxgl.particle.ParticleEmitters;
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.physics.HitBox;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.physics.box2d.dynamics.BodyType;
import com.almasb.fxgl.physics.box2d.dynamics.FixtureDef;
import com.sust.game.breakthebrick.control.BallComponent;
import com.sust.game.breakthebrick.control.BatComponent;
import com.sust.game.breakthebrick.control.BrickComponent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class BreaktheBrickFunction implements TextEntityFactory {

    @SpawnSymbol('1')
    public Entity newBrick(SpawnData data) {
        return Entities.builder()
                .from(data)
                .type(BreaktheBrickElement.BRICK)
                .viewFromNodeWithBBox(FXGL.getAssetLoader().loadTexture("brick_blue.png", 232 / 3, 104 / 3))
                .with(new PhysicsComponent(), new CollidableComponent(true))
                .with(new BrickComponent())
                .build();
    }

    @SpawnSymbol('9')
    public Entity newBat(SpawnData data) {
        PhysicsComponent physics = new PhysicsComponent();
        physics.setBodyType(BodyType.KINEMATIC);

        return Entities.builder()
                .from(data)
                .type(BreaktheBrickElement.BAT)
                .at(FXGL.getSettings().getWidth() / 2 - 50, 10)
                .viewFromNodeWithBBox(FXGL.getAssetLoader().loadTexture("bat.png", 464 / 3, 102 / 3))
                .with(physics, new CollidableComponent(true))
                .with(new BatComponent())
                .build();
    }

    @SpawnSymbol('2')
    public Entity newBall(SpawnData data) {
        PhysicsComponent physics = new PhysicsComponent();
        physics.setBodyType(BodyType.DYNAMIC);
        physics.setFixtureDef(new FixtureDef().restitution(1f).density(0.03f));

        ParticleEmitter emitter = ParticleEmitters.newFireEmitter();
        emitter.setNumParticles(2);
        emitter.setEmissionRate(0.0);

        return Entities.builder()
                .from(data)
                .type(BreaktheBrickElement.BALL)
                .bbox(new HitBox("Main", BoundingShape.circle(9)))
                .viewFromNode(new Circle(10, Color.CHARTREUSE))
                .with(physics, new CollidableComponent(true))
                .with(new BallComponent(), new ParticleComponent(emitter))
                .build();
    }

    @Override
    public char emptyChar() {
        return '0';
    }

    @Override
    public int blockWidth() {
        return 39;
    }

    @Override
    public int blockHeight() {
        return 40;
    }
}
