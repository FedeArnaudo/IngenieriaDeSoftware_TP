public class ShipSoundManager {
    private Ship ship;

    private static final String SOUNDS_PATH = "sounds/";
    private static final String SINGLE_SHOOT_SOUND = SOUNDS_PATH + "single_shoot_sound.wav";
    private static final String DOUBLE_SHOOT_SOUND = SOUNDS_PATH + "double_shoot_sound.wav";
    private static final String LASER_SHOOT_SOUND = SOUNDS_PATH + "laser_shoot_sound.wav";
    private static final String EMPTY_SOUND = SOUNDS_PATH + "empty_bullet.wav";
    private static final String IMPACT_SOUND = SOUNDS_PATH + "impact.wav";
    private static final String POWERUP_SOUND = SOUNDS_PATH + "start_game.wav";
    private static final String SUPER_POWERUP_VOICE_SOUND = SOUNDS_PATH + "super_power_up.wav";
    private static final String BASIC_POWERUP_VOICE_SOUND = SOUNDS_PATH + "basic_power_up.wav";
    private static final String EXPLOSION_SOUND = SOUNDS_PATH + "explosion.wav";
    private static final String METEOR2_VOICE_SOUND = SOUNDS_PATH + "meteor_2_voice.wav";

    public void attachToShip(Ship ship) {
        this.ship = ship;
    }

    public void initializeSounds() {
        ship.setSingleShootSound(new Sound(SINGLE_SHOOT_SOUND));
        ship.setDoubleShootSound(new Sound(DOUBLE_SHOOT_SOUND));
        ship.setLaserShootSound(new Sound(LASER_SHOOT_SOUND));
        ship.setEmptySound(new Sound(EMPTY_SOUND));
        ship.setImpactSound(new Sound(IMPACT_SOUND));
        ship.setPowerUpSound(new Sound(POWERUP_SOUND));
        ship.setSuperPowerUpVoiceSound(new Sound(SUPER_POWERUP_VOICE_SOUND));
        ship.setBasicPowerUpVoiceSound(new Sound(BASIC_POWERUP_VOICE_SOUND));
        ship.setExplosionSound(new Sound(EXPLOSION_SOUND));
        ship.setMeteor2VoiceSound(new Sound(METEOR2_VOICE_SOUND));
    }

    public void updateSounds() {
        if (ship.hasCollided() && ship.getCollisionDebounce() == 10) {
            handleCollisionSound(ship.getCollidedWith());
        }
    }

    private void handleCollisionSound(Obstacle.ObstacleType collisionType) {
        switch (collisionType) {
            case METEOR_1:
            case METEOR_2:
                playImpactSound();
                break;

            case BULLET_POWER_UP:
                playPowerupSound();
                break;

            case BASIC_POWER_UP:
                if (ship.getLastPowerUp().equals(Ship.PowerUpType.NONE)) {
                    playBasicPowerupSounds();
                }
                break;

            case SUPER_POWER_UP:
                if (!ship.getLastPowerUp().equals(Ship.PowerUpType.SUPER)) {
                    playSuperPowerupSounds();
                }
                break;
        }
    }

    private void playImpactSound() {
        ship.getImpactSound().play();
    }

    private void playPowerupSound() {
        ship.getPowerupSound().play();
    }

    private void playBasicPowerupSounds() {
        ship.getPowerupSound().play();
        ship.getBasicPowerupVoiceSound().play();
    }

    private void playSuperPowerupSounds() {
        ship.getPowerupSound().play();
        ship.getSuperPowerupVoiceSound().play();
    }
}
