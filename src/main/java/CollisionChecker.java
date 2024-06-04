package main.java;

public class CollisionChecker {
    GamePanel gamePanel;

    public CollisionChecker(GamePanel gamePanel){
        this.gamePanel = gamePanel;
    }

    public void detectObjet(Entity entity){
        if(checkCollisionWithMeteor1(entity)){
            return;
        } else if(checkCollisionWithMeteor2(entity)){
            return;
        } else if (checkCollisionWithBulletPowerup(entity)) {
            return;
        } else if (checkCollisionWithBasicPowerup(entity)) {
            return;
        }else if (checkCollisionWithSuperPowerup(entity)) {
            return;
        }
    }

    private boolean checkCollisionWithMeteor1(Entity entity){
        for(Obstacle meteor_1 : gamePanel.getMeteors1()){
            if(checkIntersect(entity, meteor_1)){
                if (entity instanceof Bullet){
                    ((Bullet) entity).collide(meteor_1.getObstacleType(), gamePanel.getMeteors1(), gamePanel.getMeteors1().indexOf(meteor_1));
                    return true;
                } else if (entity instanceof Ship) {
                    ((Ship) entity).collide(meteor_1.getObstacleType(), gamePanel.getMeteors1(), gamePanel.getMeteors1().indexOf(meteor_1));
                    return true;
                }
            }
        }

        return false;
    }

    private boolean checkCollisionWithMeteor2(Entity entity){
        for(Obstacle meteor_2 : gamePanel.getMeteors2()){
            if(checkIntersect(entity, meteor_2)){
                if (entity instanceof Bullet){
                    ((Bullet) entity).collide(meteor_2.getObstacleType(), gamePanel.getMeteors2(), gamePanel.getMeteors2().indexOf(meteor_2));
                    return true;
                } else if (entity instanceof Ship) {
                    ((Ship) entity).collide(meteor_2.getObstacleType(), gamePanel.getMeteors2(), gamePanel.getMeteors2().indexOf(meteor_2));
                    return true;
                }
            }
        }

        return false;
    }

    private boolean checkCollisionWithBulletPowerup(Entity entity){
        for(Obstacle bulletPowerUp : gamePanel.getBulletPowerUps()){
            if(checkIntersect(entity, bulletPowerUp)){
                if (entity instanceof Bullet){
                    ((Bullet) entity).collide(bulletPowerUp.getObstacleType(), gamePanel.getBulletPowerUps(), gamePanel.getBulletPowerUps().indexOf(bulletPowerUp));
                    return true;
                } else if (entity instanceof Ship) {
                    ((Ship) entity).collide(bulletPowerUp.getObstacleType(), gamePanel.getBulletPowerUps(), gamePanel.getBulletPowerUps().indexOf(bulletPowerUp));
                    return true;
                }
            }
        }

        return false;
    }

    private boolean checkCollisionWithBasicPowerup(Entity entity){
        for(Obstacle basicPowerUp : gamePanel.getBasicPowerUps()){
            if(checkIntersect(entity, basicPowerUp)){
                if (entity instanceof Bullet){
                    ((Bullet) entity).collide(basicPowerUp.getObstacleType(), gamePanel.getBasicPowerUps(), gamePanel.getBasicPowerUps().indexOf(basicPowerUp));
                    return true;
                } else if (entity instanceof Ship) {
                    ((Ship) entity).collide(basicPowerUp.getObstacleType(), gamePanel.getBasicPowerUps(), gamePanel.getBasicPowerUps().indexOf(basicPowerUp));
                    return true;
                }
            }
        }

        return false;
    }

    private boolean checkCollisionWithSuperPowerup(Entity entity){
        for(Obstacle superPowerUp : gamePanel.getSuperPowerUps()){
            if(checkIntersect(entity, superPowerUp)){
                if (entity instanceof Bullet){
                    ((Bullet) entity).collide(superPowerUp.getObstacleType(), gamePanel.getSuperPowerUps(), gamePanel.getSuperPowerUps().indexOf(superPowerUp));
                    return true;
                } else if (entity instanceof Ship) {
                    ((Ship) entity).collide(superPowerUp.getObstacleType(), gamePanel.getSuperPowerUps(), gamePanel.getSuperPowerUps().indexOf(superPowerUp));
                    return true;
                }
            }
        }

        return false;
    }

    private boolean checkIntersect(Entity entity1, Entity entity2){
        boolean crashed = false;

        if(entity2 != null){
            entity1.getSolidRectangle().setLocation(entity1.getX() + (int)entity1.getSolidRectangle().getX(),entity1.getY() + (int)entity1.getSolidRectangle().getY());
            entity2.getSolidRectangle().setLocation(entity2.getX() + (int)entity2.getSolidRectangle().getX(),entity2.getY() + (int)entity2.getSolidRectangle().getY());

            switch (entity1.getDirection()){
                case "up":
                    entity1.getSolidRectangle().setLocation((int)entity1.getSolidRectangle().getX(), (int)entity1.getSolidRectangle().getY() - entity1.getSpeed());
                    if(entity1.getSolidRectangle().intersects(entity2.getSolidRectangle())){
                        //System.out.println("Front collision");
                        crashed = true;
                    }
                    break;

                case "down":
                    entity1.getSolidRectangle().setLocation((int)entity1.getSolidRectangle().getX(), (int)entity1.getSolidRectangle().getY() + entity1.getSpeed());
                    if(entity1.getSolidRectangle().intersects(entity2.getSolidRectangle())){
                        //System.out.println("Back collision");
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
