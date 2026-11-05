package TileGenerator.ui.screens;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import javax.swing.JPanel;
import TileGenerator.logic.controls.GameScreenKeyboardControls;
import TileGenerator.main.Constants;
import TileGenerator.models.Chunk;

@SuppressWarnings("serial")
public class GameScreen extends JPanel {

    Graphics2D currentGraphicalRenderer;
    short leftLoadedChunk, rightLoadedChunk = 0; //The bounds to determine which chunks should be loaded

    GameScreen() {

	setVisible(true);
	setFocusable(true);
	setFocusTraversalKeysEnabled(false);
	addKeyListener(new GameScreenKeyboardControls());

    }

    public void paint(Graphics g) {

	//Main
	currentGraphicalRenderer = (Graphics2D) g;
	//AffineTransform staticOrigin = currentGraphicalRenderer.getTransform(); //To be used later potentially
	AffineTransform dynamicOrigin = new AffineTransform();

	//Setting screen
	dynamicOrigin.translate(getWidth() / 2 - Constants.SCREEN_X, Constants.SCREEN_Y + getHeight() / 2);
	dynamicOrigin.scale(1, -1);

	//Dynamic objects
	currentGraphicalRenderer.setTransform(dynamicOrigin); {

	    renderChunks();

	}

	GameScreenKeyboardControls.useKeyboardControls();
	repaint();

    }

    private void renderTile(int x, int y, byte tileType) { //Renders the centre of tiles at the specified pixels relative to the origin

	//currentGraphicalRenderer.setColor(Constants.COLORS[tileType]);
	//currentGraphicalRenderer.fillRect(x - (Constants.TILE_SIZE / 2), y + (Constants.TILE_SIZE / 2), Constants.TILE_SIZE, Constants.TILE_SIZE);
	currentGraphicalRenderer.drawImage(Constants.TILE_SPRITES[tileType], x - (Constants.TILE_SIZE / 2), y + (Constants.TILE_SIZE / 2), Constants.TILE_SIZE, Constants.TILE_SIZE, null);

    }

    private void renderSingleChunk(int chunkNumber) {

	Chunk chunk = Chunk.getChunk(chunkNumber); //Current chunk
	int lowestTileRendered = Math.max(Math.floorDiv(Constants.SCREEN_Y - getHeight() / 2, Constants.TILE_SIZE) - 1, -Constants.WORLD_HEIGHT); //Lowest block rendered
	int highestTileRendered = Math.min(Math.floorDiv(Constants.SCREEN_Y + getHeight() / 2, Constants.TILE_SIZE), Constants.WORLD_HEIGHT); //Highest block rendered

	for (int x = chunk.getChunkTileX() - Constants.CHUNK_EXTENSION; x <= chunk.getChunkTileX() + Constants.CHUNK_EXTENSION; x++) { //Render full horizontal chunk

	    for (int y = lowestTileRendered; y <= highestTileRendered; y++) { //Render partial vertical chunk

		byte currentTile = chunk.getTileFromAbsoluteCoords(x, y);
		renderTile(x * Constants.TILE_SIZE, y * Constants.TILE_SIZE, currentTile);

	    }

	}

    }

    private void renderChunks() {

	renderSingleChunk(0); //Origin chunk

	//Visibility and loading calculations
	short leftVisibleChunk = (short) ((Constants.SCREEN_X - getWidth() / 2) / (Constants.TILE_SIZE * Constants.CHUNK_WIDTH) - 1);
	short rightVisibleChunk = (short) ((Constants.SCREEN_X + getWidth() / 2) / (Constants.TILE_SIZE * Constants.CHUNK_WIDTH) + 1);
	if (leftVisibleChunk < leftLoadedChunk) leftLoadedChunk = leftVisibleChunk; //Left chunk is visible but not loaded
	if (rightVisibleChunk > rightLoadedChunk) rightLoadedChunk = rightVisibleChunk; //Right chunk is visible but not loaded
	if (leftVisibleChunk > leftLoadedChunk) { //Left chunk is loaded but not visible

	    unloadChunk(leftLoadedChunk);
	    leftLoadedChunk++;

	}
	if (rightVisibleChunk < rightLoadedChunk) { //Right chunk is loaded but not visible

	    unloadChunk(rightLoadedChunk);
	    rightLoadedChunk--;

	}

	//Rendering
	for (int chunkNumber = leftVisibleChunk; chunkNumber <= rightVisibleChunk; chunkNumber++) {

	    renderSingleChunk(chunkNumber);

	}

    }

    private void unloadChunk(short chunkNumber) {

	Constants.WORLD.remove(chunkNumber).save();

    }

}