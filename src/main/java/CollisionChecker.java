package main.java;

public class CollisionChecker {
    GamePanel gamePanel;

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
            entity1.getSolidRectangle().setLocation(entity1.getX() + (int)entity1.getSolidRectangle().getX(),entity1.getY() + (int)entity1.getSolidRectangle().getY());
            entity2.getSolidRectangle().setLocation(entity2.getX() + (int)entity2.getSolidRectangle().getX(),entity2.getY() + (int)entity2.getSolidRectangle().getY());

            switch (entity1.getDirection()){
                case "up":
                    if(entity1.getSolidRectangle().intersects(entity2.getSolidRectangle())){
                        //System.out.println("Front collision");
                        crashed = true;
                    }
                    break;

                case "left":
                    entity1.getSolidRectangle().setLocation((int)entity1.getSolidRectangle().getX() - entity1.getSpeed(), (int)entity1.getSolidRectangle().getY());
                    if(entity1.getSolidRectangle().intersects(entity2.getSolidRectangle())){
                        //System.out.println("Left collision");
                        crashed = true;
                    }
                    break;

                case "right":
                    entity1.getSolidRectangle().setLocation((int)entity1.getSolidRectangle().getX() + entity1.getSpeed(), (int)entity1.getSolidRectangle().getY());
                    if(entity1.getSolidRectangle().intersects(entity2.getSolidRectangle())){
                        //System.out.println("Right collision");
                        crashed = true;
                    }
                    break;
            }

            entity1.getSolidRectangle().setLocation(entity1.getSolidAreaDefaultX(),entity1.getSolidAreaDefaultY());
            entity2.getSolidRectangle().setLocation(entity2.getSolidAreaDefaultX(),entity2.getSolidAreaDefaultY());
        }

        return crashed;
    }
}
