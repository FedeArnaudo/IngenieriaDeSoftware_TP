package main.java;

import java.util.ArrayList;

public class CollisionChecker {
    private GamePanel gamePanel;
    public CollisionChecker(GamePanel gamePanel){
        this.gamePanel = gamePanel;
    }

    public Entity detectObjet(Entity entity){
        Entity entityTemp = null;
        try{
            for(Meteor meteor: gamePanel.meteors){
                if(checkIntersect(entity, meteor)){
                    entityTemp = meteor;
                    break;
                }
            }
            if(entityTemp == null){
                // Checkeo si impacto contra otro objeto.
            }
        }
        catch (NullPointerException e){
            System.out.println("No collision detected");
        }
        return entityTemp;
    }

    public boolean checkIntersect(Entity entity1, Entity entity2){
        boolean crashed = false;

        if(entity2 != null){
            entity1.solidRectangle.x = entity1.x + entity1.solidRectangle.x;
            entity1.solidRectangle.y = entity1.y + entity1.solidRectangle.y;

            entity2.solidRectangle.x = entity2.x + entity2.solidRectangle.x;
            entity2.solidRectangle.y = entity2.y + entity2.solidRectangle.y;

            switch (entity1.direction){
                case "up":
                    if(entity1.solidRectangle.intersects(entity2.solidRectangle)){
                        System.out.println("Tenemos una collision de frente");
                        crashed = true;
                    }
                    break;
                case "left":
                    entity1.solidRectangle.x -= entity1.speed;
                    if(entity1.solidRectangle.intersects(entity2.solidRectangle)){
                        System.out.println("Tenemos una collision a la izquierda");
                        crashed = true;
                    }
                    break;
                case "right":
                    entity1.solidRectangle.x += entity1.speed;
                    if(entity1.solidRectangle.intersects(entity2.solidRectangle)){
                        System.out.println("Tenemos una collision a la derecha");
                        crashed = true;
                    }
                    break;
            }
            entity1.solidRectangle.x = entity1.solidAreaDefaultX;
            entity1.solidRectangle.y = entity1.solidAreaDefaultY;

            entity2.solidRectangle.x = entity2.solidAreaDefaultX;
            entity2.solidRectangle.y = entity2.solidAreaDefaultY;
        }
        return crashed;
    }
}
