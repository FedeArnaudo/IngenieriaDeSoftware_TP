public class CollisionChecker {
    GamePanel gamePanel;
    public CollisionChecker(GamePanel gamePanel){
        this.gamePanel = gamePanel;
    }

    public boolean checkObjects(Entity entity){
        boolean collision = false;
        for(Meteor meteor: gamePanel.meteors){
            if(meteor != null){
                entity.solidRectangle.x = entity.x + entity.solidRectangle.x;
                entity.solidRectangle.y = entity.y + entity.solidRectangle.y;

                meteor.solidRectangle.x = meteor.x + meteor.solidRectangle.x;
                meteor.solidRectangle.y = meteor.y + meteor.solidRectangle.y;

                switch (entity.direction){
                    case "up":
                        if(entity.solidRectangle.intersects(meteor.solidRectangle)){
                            System.out.println("Tenemos una colision de frente");
                            collision = true;
                        }
                        break;
                    case "left":
                        entity.solidRectangle.x -= entity.speed;
                        if(entity.solidRectangle.intersects(meteor.solidRectangle)){
                            System.out.println("Tenemos una colision a la izquierda");
                            collision = true;
                        }
                        break;
                    case "right":
                        entity.solidRectangle.x += entity.speed;
                        if(entity.solidRectangle.intersects(meteor.solidRectangle)){
                            System.out.println("Tenemos una colision a la derecha");
                            collision = true;
                        }
                        break;
                }
                entity.solidRectangle.x = entity.solidAreaDefaultX;
                entity.solidRectangle.y = entity.solidAreaDefaultY;

                meteor.solidRectangle.x = meteor.solidAreaDefaultX;
                meteor.solidRectangle.y = meteor.solidAreaDefaultY;
            }
        }
        return collision;
    }
}
