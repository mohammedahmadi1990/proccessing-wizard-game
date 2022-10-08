package gremlins;

public class Level {

    private final String layout;
    private final float wizardCoolDown;
    private final float enemyCoolDown;

    private char[][] map;

    /**
     * Constructor of the Level class to grab the address of map and other details for levels of games
     * @param layout is the local address of the map for the level
     * @param wizardCoolDown is the amount of time that the wizard can shoot a fire in different levels
     * @param enemyCoolDown is the amount of time that the gremlins can shoot a fire in different levels
     * @param map is the read data from the input map containing coordination of objects in the game
     */
    public Level(String layout, float wizardCoolDown, float enemyCoolDown, char[][] map) {
        this.layout = layout;
        this.wizardCoolDown = wizardCoolDown;
        this.enemyCoolDown = enemyCoolDown;
        this.map = map;
    }

    /**
     * Getter method to get the amount of cool down for the wizard
     * @return the amount of wizardCoolDown
     */
    public float getWizardCoolDown() {
        return wizardCoolDown;
    }

    /**
     * Getter method to get the amount of cool down for gremlins
     * @return the amount of enemyCoolDown
     */
    public float getEnemyCoolDown() {
        return enemyCoolDown;
    }

    /**
     * Getter method to get the 2D array of Map
     * @return the array of map
     */
    public char[][] getMap() {
        return map;
    }

}
