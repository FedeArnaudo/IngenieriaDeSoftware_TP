public class CollisionChecker {
    GamePanel gamePanel;
    public CollisionChecker(GamePanel gamePanel){
        this.gamePanel = gamePanel;
    }

    public  int checkObjects(Entity entity, boolean player){
        int id = 0;
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
                            if(player){
                                id = 1;
                            }
                            else {
                                id = 2;
                            }
                        }
                        break;
                    case "left":
                        entity.solidRectangle.x -= entity.speed;
                        if(entity.solidRectangle.intersects(meteor.solidRectangle)){
                            System.out.println("Tenemos una colision a la izquierda");
                            if(player){
                                id = 1;
                            }
                            else {
                                id = 2;
                            }
                        }
                        break;
                    case "right":
                        entity.solidRectangle.x += entity.speed;
                        if(entity.solidRectangle.intersects(meteor.solidRectangle)){
                            System.out.println("Tenemos una colision a la derecha");
                            if(player){
                                id = 1;
                            }
                            else {
                                id = 2;
                            }
                        }
                        break;
                }
                entity.solidRectangle.x = entity.solidAreaDefaultX;
                entity.solidRectangle.y = entity.solidAreaDefaultY;

                meteor.solidRectangle.x = meteor.solidAreaDefaultX;
                meteor.solidRectangle.y = meteor.solidAreaDefaultY;
            }
        }
        return id;
    }
}
