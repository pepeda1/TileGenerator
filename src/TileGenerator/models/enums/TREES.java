package TileGenerator.models.enums;

public enum TREES {

    BASIC(5, 6, new byte[] {
	    -2, -2, -2, 0, 0, 0,
	    -2, -2, -2, 0, 0, 0,
	     3,  0,  0, 0, 0, 0,
	    -2, -2, -2, 0, 0, 0,
	    -2, -2, -2, 0, 0, 0,
    }, new byte[] {
	    -1, -1, -1, 12, 12, -1,
	    -1, -1, -1, 12, 12, -1,
	     4, 11, 11, 12, 12, 12,
	    -1, -1, -1, 12, 12, -1,
	    -1, -1, -1, 12, 12, -1,
    }),
    
    PINE(5, 8, new byte[] {
	    -2, -2, -2, -2, -2,  0,  0,  0,
	    -2, -2, -2, -2, -2,  0,  0,  0,
	     7,  0,  0,  0,  0,  0,  0,  0,
	    -2, -2, -2, -2, -2,  0,  0,  0,
	    -2, -2, -2, -2, -2,  0,  0,  0,
    }, new byte[] {
	    -1, -1, -1, -1, -1, 14, 14, -1,
	    -1, -1, -1, -1, -1, 14, 14, 14,
	    13, 13, 13, 13, 13, 13, 13, 14,
	    -1, -1, -1, -1, -1, 14, 14, 14,
	    -1, -1, -1, -1, -1, 14, 14, -1,
    });

    
    private final int width; //The tree structureBox width
    private final int height; //The tree structureBox height
    private final byte[] requiredTiles; //The tiles required to ensure valid placement, stored tilted 90 degrees clockwise
    private final byte[] tiles; //The tiles of the tree, stored tilted 90 degrees clockwise

    TREES(int width, int height, byte[] requiredTiles, byte[] tiles) {

	this.width = width;
	this.height = height;
	this.requiredTiles = requiredTiles;
	this.tiles = tiles;

    }

    public int getWidth() {

	return width;

    }

    public int getHeight() {

	return height;

    }
    
    public byte[] getRequiredTiles() {

	return requiredTiles;

    }

    public byte[] getTiles() {

	return tiles;

    }

}