package gremlins;

import processing.core.PApplet;
import processing.core.PImage;
import processing.data.JSONObject;
import java.util.ArrayList;
import java.util.Random;
import java.io.*;


public class App extends PApplet {

    public static final int WIDTH = 720;
    public static final int HEIGHT = 720;

    public static final int SPRITESIZE = 20;
    public static final int BOTTOMBAR = 60;
    public static final int FPS = 60;
    public static final int GRID_X = 36;
    public static final int GRID_Y = 33;
    public static final Random randomGenerator = new Random();
    public String configPath;
    public PImage[] brickWallImage;
    public PImage stoneWallImage;
    public PImage wizardUpImage;
    public PImage wizardDownImage;
    public PImage wizardRightImage;
    public PImage wizardLeftImage;
    public PImage gremlinImage;
    public PImage slimeImage;
    public PImage doorImage;
    public PImage fireballImage;
    public int lives;
    public float wizardCoolDown;
    public float enemyCoolDown;
    ArrayList<Level> levels;
    public int currentLevel;
    public Wizard wizard;
    public ArrayList<BrickWall> brickWalls;
    public ArrayList<StoneWall> stoneWalls;
    public ArrayList<Gremlin> gremlins;
    public ExitDoor exitDoor;
    public boolean winStatus;
    public boolean loseStatus;
    public int timer;
    public int frameCounter;
    public int brickFrameNum;

    /**
     * Read config file for levels
     */
    public App() {
        this.configPath = "config.json";
    }

    /**
     * Initialise the setting of the window size.
    */
    public void settings() {
        size(WIDTH, HEIGHT);
    }

    /**
     * Load all resources such as images. Initialise the elements such as the player, enemies and map elements.
    */
    public void setup() {
        frameRate(FPS);

        // Load images during setup
        this.wizardUpImage = loadImage(this.getClass().getResource("wizard2.png").getPath().replace("%20", " "));
        this.wizardDownImage = loadImage(this.getClass().getResource("wizard3.png").getPath().replace("%20", " "));
        this.wizardRightImage = loadImage(this.getClass().getResource("wizard1.png").getPath().replace("%20", " "));
        this.wizardLeftImage = loadImage(this.getClass().getResource("wizard0.png").getPath().replace("%20", " "));
        this.stoneWallImage = loadImage(this.getClass().getResource("stonewall.png").getPath().replace("%20", " "));
        brickWallImage = new PImage[5];
        brickWallImage[0] = loadImage(this.getClass().getResource("brickwall_destroyed3.png").getPath().replace("%20", " "));
        brickWallImage[1] = loadImage(this.getClass().getResource("brickwall_destroyed2.png").getPath().replace("%20", " "));
        brickWallImage[2] = loadImage(this.getClass().getResource("brickwall_destroyed1.png").getPath().replace("%20", " "));
        brickWallImage[3] = loadImage(this.getClass().getResource("brickwall_destroyed0.png").getPath().replace("%20", " "));
        brickWallImage[4] = loadImage(this.getClass().getResource("brickwall.png").getPath().replace("%20", " "));
        this.doorImage = loadImage(this.getClass().getResource("door.png").getPath().replace("%20", " "));
        this.gremlinImage = loadImage(this.getClass().getResource("gremlin.png").getPath().replace("%20", " "));
        this.slimeImage = loadImage(this.getClass().getResource("slime.png").getPath().replace("%20", " "));
        this.fireballImage = loadImage(this.getClass().getResource("fireball.png").getPath().replace("%20", " "));

        // Read JSON data for level details and put in Objects of Level
        JSONObject conf = loadJSONObject(new File(this.configPath));
        lives = Integer.parseInt(conf.get("lives").toString());
        levels = new ArrayList<>();
        for (int i = 0; i < conf.getJSONArray("levels").size(); i++) {
            String layout = conf.getJSONArray("levels").getJSONObject(i).get("layout").toString();
            float wizardCoolDown = Float.parseFloat(conf.getJSONArray("levels").getJSONObject(i).get("wizard_cooldown").toString());
            float enemyCoolDown = Float.parseFloat(conf.getJSONArray("levels").getJSONObject(i).get("enemy_cooldown").toString());
            char[][] map = new char[GRID_Y][GRID_X];

            try (BufferedReader br = new BufferedReader(new FileReader(layout))) {
                String line;
                int r = 0;
                while ((line = br.readLine()) != null) {
                    for (int c = 0; c < GRID_X; c++)
                        map[r][c] = line.charAt(c);
                    r++;
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            levels.add(new Level(layout,wizardCoolDown,enemyCoolDown,map));
        }

        stoneWalls = new ArrayList<>();
        brickWalls = new ArrayList<>();
        gremlins = new ArrayList<>();
        for (int r = 0; r < GRID_Y; r++) {
            for (int c = 0; c < GRID_X; c++) {
                if(levels.get(currentLevel).getMap()[r][c]=='X')
                    stoneWalls.add(new StoneWall(c*20, r*20));
                else if(levels.get(currentLevel).getMap()[r][c]=='B')
                    brickWalls.add(new BrickWall(c*20, r*20,5));
                else if(levels.get(currentLevel).getMap()[r][c]=='G')
                    gremlins.add(new Gremlin(c*20, r*20));
                else if(levels.get(currentLevel).getMap()[r][c]=='W')
                    wizard = new Wizard(c*20, r*20);
                else if(levels.get(currentLevel).getMap()[r][c]=='E')
                    exitDoor = new ExitDoor(c*20, r*20);
            }
        }
        timer = second();
        frameCounter = 0;
        brickFrameNum = 0;
    }


    /**
     * Receive key pressed signal from the keyboard.
    */
    public void keyPressed(){
        if(keyCode == UP && isFree(wizard,"UP"))
            wizard.move("UP");
        if(keyCode == DOWN && isFree(wizard,"DOWN"))
            wizard.move("DOWN");
        if(keyCode == RIGHT && isFree(wizard,"RIGHT"))
            wizard.move("RIGHT");
        if(keyCode == LEFT && isFree(wizard,"LEFT"))
            wizard.move("LEFT");
        if(keyCode == ' ')
            wizard.fire();

    }

    /**
     * This method checks whether the wizard is over the exit door or not? Then to rise one level
     * @param wizard is the wizard object
     * @return the status of leveling-up for the wizard
     */
    public boolean checkLevelUpCondtion(Wizard wizard){
        if(wizard.getColumn()==exitDoor.getColumn() && wizard.getRow()==exitDoor.getRow()) {
            if(currentLevel==levels.size()-1)
                winStatus = true;
            else if(currentLevel<levels.size()-1)
                this.currentLevel++;
            return true;
        }
        return false;
    }

    /**
     * This method checks collision happening for a gremlin and the wizard. It acts based on the status to disappear
     * the gremlin and decrease wizard's live
     * @param wizard is the wizard object
     * @param gremlin is the wizard object
     */
    public void checkCollision(Wizard wizard, Gremlin gremlin){
        if((wizard.getColumn()==gremlin.getColumn() && wizard.getRow()==gremlin.getRow()) ||
                wizard.getColumn()==gremlin.getFireColumn() && wizard.getRow()==gremlin.getFireRow()) {
            if(lives==1) {
                lives = 0;
                loseStatus = true;
            }
            else {
                gremlin.disappear(stoneWalls,brickWalls);
                wizard.setRow(wizard.getStartRow());
                wizard.setColumn(wizard.getStartColumn());
                lives--;
            }
        }
    }

    /**
     * This method checks whether the wizard can continue moving toward this direction or not
     * @param wizard is the wizard object
     * @param direction is the direction of wizard precept in key press
     * @return the status of up-coming possible obstacle.
     */
    public boolean isFree(Wizard wizard, String direction){
        // Check whether obstacle is in the next step
        switch (direction) {
            case "UP":
                for (BrickWall brickWall : brickWalls)
                    if (wizard.getRow() - SPRITESIZE == brickWall.getRow() && wizard.getColumn() == brickWall.getColumn())
                        return false;
                for (StoneWall stoneWall : stoneWalls)
                    if (wizard.getRow() - SPRITESIZE == stoneWall.getRow() && wizard.getColumn() == stoneWall.getColumn())
                        return false;
                break;
            case "DOWN":
                for (BrickWall brickWall : brickWalls)
                    if (wizard.getRow() + SPRITESIZE == brickWall.getRow() && wizard.getColumn() == brickWall.getColumn())
                        return false;
                for (StoneWall stoneWall : stoneWalls)
                    if (wizard.getRow() + SPRITESIZE == stoneWall.getRow() && wizard.getColumn() == stoneWall.getColumn())
                        return false;
                break;
            case "RIGHT":
                for (BrickWall brickWall : brickWalls)
                    if (wizard.getColumn() + SPRITESIZE == brickWall.getColumn() && wizard.getRow() == brickWall.getRow())
                        return false;
                for (StoneWall stoneWall : stoneWalls)
                    if (wizard.getColumn() + SPRITESIZE == stoneWall.getColumn() && wizard.getRow() == stoneWall.getRow())
                        return false;
                break;
            case "LEFT":
                for (BrickWall brickWall : brickWalls)
                    if (wizard.getColumn() - SPRITESIZE == brickWall.getColumn() && wizard.getRow() == brickWall.getRow())
                        return false;
                for (StoneWall stoneWall : stoneWalls)
                    if (wizard.getColumn() - SPRITESIZE == stoneWall.getColumn() && wizard.getRow() == stoneWall.getRow())
                        return false;
                break;
        }
        return true;
    }
    
    /**
     * Receive key released signal from the keyboard.*
    */
    public void keyReleased(){
        if(!winStatus && checkLevelUpCondtion(wizard)){
            // based on this condition, wizard gets level-up
            stoneWalls = new ArrayList<>();
            brickWalls = new ArrayList<>();
            gremlins = new ArrayList<>();
            this.wizardCoolDown =levels.get(currentLevel).getWizardCoolDown();
            this.enemyCoolDown =levels.get(currentLevel).getEnemyCoolDown();
            for (int r = 0; r < GRID_Y; r++) {
                for (int c = 0; c < GRID_X; c++) {
                    if(levels.get(currentLevel).getMap()[r][c]=='X')
                        stoneWalls.add(new StoneWall(c*20, r*20));
                    else if(levels.get(currentLevel).getMap()[r][c]=='B')
                        brickWalls.add(new BrickWall(c*20, r*20,4));
                    else if(levels.get(currentLevel).getMap()[r][c]=='G')
                        gremlins.add(new Gremlin(c*20, r*20));
                    else if(levels.get(currentLevel).getMap()[r][c]=='W')
                        wizard = new Wizard(c*20, r*20);
                    else if(levels.get(currentLevel).getMap()[r][c]=='E')
                        exitDoor = new ExitDoor(c*20, r*20);
                }
            }

        }
    }


    /**
     * Draw all elements in the game by current frame. 
	 */
    public void draw() {
        // Layout Background Color
        background(191,153,118);
        fill(255, 255, 255);

        // Timer for gremlins to shoot fires based on enemy cooldown
        int newTimer = second() + minute()*60 + hour()*3600;
        if(newTimer-timer>=levels.get(currentLevel).getEnemyCoolDown()){
            int randomGremlinShoot = randomGenerator.nextInt(gremlins.size()-1);
            gremlins.get(randomGremlinShoot).setFirePermit(true);
            gremlins.get(randomGremlinShoot).setFireDirectionFlag(true);
            timer = second() + minute()*60 + hour()*3600;
        }

        if(winStatus) {
            // if wizard can meet the final exit door
            background(80,200,120);
            textSize(40);
            text("YOU WIN!", (float)(WIDTH / 2) - 80, (float)HEIGHT / 2);
        }
        else if(loseStatus){
            // if Wizard get rid of lives
            background(220,20,60);
            textSize(40);
            text("GAME OVER!", (float)WIDTH / 2 - 85, (float)HEIGHT / 2);
        }
        else {
            // Draw Objects..
            if(wizard.isFirePermit()) {
                // Draw wizard
                image(fireballImage, wizard.getFireColumn(), wizard.getFireRow());
                wizard.firing(stoneWalls, brickWalls,gremlins);
                brickFrameNum = 4;
            }
            for (StoneWall stoneWall : stoneWalls) {
                // Draw stone walls
                image(stoneWallImage, stoneWall.getColumn(), stoneWall.getRow());
            }
            for (int i = 0; i < brickWalls.size(); i++) {
                // Draw brick walls
                if(brickWalls.get(i).getStatus()>0) //if it has not destroyed >> status shows the destruction
                    image(brickWallImage[brickWalls.get(i).getStatus()-1], brickWalls.get(i).getColumn(), brickWalls.get(i).getRow());
                else {
                    // if it has been destroyed
                    if(brickFrameNum >-1) {
                        // Count and load frames of brick destruction
                        frameCounter++;
                        image(brickWallImage[brickFrameNum], brickWalls.get(i).getColumn(), brickWalls.get(i).getRow());
                        if (frameCounter == 3) {
                            // after 3 destruction, frame changes
                            frameCounter = 0;
                            brickFrameNum--;
                        }
                    }else{
                        // after all frames it deletes the brick >> this form of for is compatible with changes in the length of loop
                        brickWalls.remove(i);
                    }
                }
            }
            for (Gremlin gremlin : gremlins) {
                // Draw gremlins and fires
                checkCollision(wizard,gremlin); // check collision with obstacles
                gremlin.move(stoneWalls,brickWalls);
                if(gremlin.isFirePermit()) {
                    // if fire has been permitted by the timer >> fire
                    image(slimeImage, gremlin.getFireColumn(), gremlin.getFireRow());
                    gremlin.fire(stoneWalls, brickWalls);
                }
                // Draw gremlins
                image(gremlinImage, gremlin.getColumn(), gremlin.getRow());
            }
            image(doorImage, exitDoor.getColumn(), exitDoor.getRow());

            // Draw Score and Live Board
            textSize(20);
            text("Lives: ", 10, HEIGHT - BOTTOMBAR + 40);
            for (int i = 0; i < lives; i++) {
                image(wizardRightImage, 70 + i * 20, HEIGHT - BOTTOMBAR + 22);
            }
            text("Level: " + (currentLevel + 1) + "/" + levels.size(), 200, HEIGHT - BOTTOMBAR + 40);

            // load appropriate image for wizard based on directions
            switch (wizard.getDirection()) {
                case "RIGHT":
                    image(wizardRightImage, wizard.getColumn(), wizard.getRow());
                    break;
                case "LEFT":
                    image(wizardLeftImage, wizard.getColumn(), wizard.getRow());
                    break;
                case "UP":
                    image(wizardUpImage, wizard.getColumn(), wizard.getRow());
                    break;
                case "DOWN":
                    image(wizardDownImage, wizard.getColumn(), wizard.getRow());
                    break;
            }
        }

    }

    /**
     * Main Method: Game starts here
     */
    public static void main(String[] args) {
        PApplet.main("gremlins.App");
    }
}
