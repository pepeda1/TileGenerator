package TileGenerator.main;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import javax.imageio.ImageIO;
import TileGenerator.models.Chunk;
import TileGenerator.ui.screens.GameWindow;

public final class Constants {

    /*		TEMPORARY		*/
    public static final int SEED = new Random().nextInt(); //TEMPORARY UNTIL PROPER SEED SELECTION SYSTEM IMPLEMENTED
    public static final short PLAYER_SPEED = 50; //TEMPORARY UNTIL ACTUAL PLAYER IS ADDED

    
    /*		CORE			*/
    public static final Random RANDOM_NUMBER = new Random(SEED); //The random variable to be used to generate the world
    public static final Map<Short, Chunk> WORLD = new HashMap<Short, Chunk>(); //The list of chunks
    
    
    /*		WORLD SETTINGS		*/
    public static final byte SEA_LEVEL = -5; //The block at which the highest sea block is located
    public static final byte WORLD_HEIGHT = 127; //Max height of the world in tiles. Actual height goes from -WORLD_HEIGHT to +WORLD_HEIGHT
    public static final byte[] NATURAL_ELEVATIONS = {-WORLD_HEIGHT, -50, SEA_LEVEL, 15, 50, WORLD_HEIGHT}; //Natural elevation bounds (inclusive) for the biomes Ocean/Shore/Plains/Hills/Mountains
    public static final byte MAX_BIOME_WIDTH = 40; //The maximum width of a biome in chunks, doubled for the origin biome
    public static final byte ORIGIN_BLOCK_HEIGHT_VALUE = 0; //The y-value of the starting chunk 
    public static final byte DIRT_DEPTH = 5; //The depth of the dirt/grass/sand level
    
    
    /*		CHUNK SETTINGS		*/
    public static final byte CHUNK_WIDTH = 21; //Width of each chunk in tiles, has to be an odd number so that it has a centre
    public static final byte CHUNK_EXTENSION = (CHUNK_WIDTH - 1) / 2; //The x-position of the block in the origin chunk that is furthest to the right
    
    
    /*		UI			*/
    public static final byte TILE_SIZE = 8; //Length of a single side of a tile in pixels
    public static final Map<Character, Boolean> KEYS_PRESSED = new HashMap<Character, Boolean>(); //The keys that are currently being pressed
    public static GameWindow GAME_WINDOW; //The JFrame window
    public static int SCREEN_X = 0; //Where the user camera is located along the world x-axis in pixels relative to the origin
    public static int SCREEN_Y = 0; //Where the user camera is located along the world y-axis in pixels relative to the origin
    public static final File TILE_SPRITES_FILE = new File("resources\\images\\tiles.png"); //The file with all the tiles
    
    /**
     * FULL LIST OF INDEXES AND TILES:
     * <p>
     * 
     * SPECIAL IDENTIFIERS: <br>
     * -1 -> Unchanged <br>
     * -2 -> Any tiles <br>
     * 
     * <p>
     * 
     * TILES: <br>
     * 0 -> Air <br>
     * 1 -> Water <br>
     * 2 -> Stone <br>
     * 3 -> Grass <br>
     * 4 -> Dirt <br>
     * 5 -> Sand <br>
     * 6 -> Sandstone <br>
     * 7 -> Snow <br>
     * 8 -> Ice <br>
     * 9 -> Kelp <br>
     * 10 -> Coral <br>
     * 11 -> Oak log <br>
     * 12 -> Oak leaves <br>
     * 13 -> Pine log <br>
     * 14 -> Pine leaves <br>
    */
    public static final Image[] TILE_SPRITES = retrieveTileSprites(); //The list of tile sprites
    
    
    
    private static Image[] retrieveTileSprites() {

	try {
	    
	    BufferedImage imagesInterface = ImageIO.read(TILE_SPRITES_FILE);
	    Image[] tileSprites = new Image[256];
	    for (int i = 0; i < 256; i++) {
		
		tileSprites[i] = imagesInterface.getSubimage((i / 16) * 8, (i % 16) * 8, 8, 8);
		
	    }
	    return tileSprites;
	    
	} catch (IOException e) {e.printStackTrace();}
	
	return null;

    }

}
