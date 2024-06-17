public interface ShootingStrategy {
    void shoot(Ship ship);
    void handleShooting(Ship ship);
    void updateBullets(Ship ship);
}