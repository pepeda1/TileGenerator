package TileGenerator.models;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import TileGenerator.logic.generation.WorldGenerator;
import TileGenerator.main.Constants;

public class Chunk {

    private short chunkNumber; //Number of chunk
    private int chunkTileX; //The x position of the chunk in tiles
    private byte biomeHeightMap; //The biome height of the chunk, between -2 and 2
    private byte biomeHeightMapChangeProbability; //The chance the biome height changes in this chunk. If the biome height changes it is reset to MAX_BIOME_WIDTH, otherwise it is decreased by 1
    private byte blockHeightValue; //The y-position of the highest block on the far edge of the chunk
    private byte biomeTemperatureMap; //The biome temperature of the chunk, between -1 and 1
    private byte biomeTemperatureMapChangeProbability; //The chance the biome temperature changes in this chunk. If the biome temperature changes it is reset to MAX_BIOME_WIDTH, otherwise it is decreased by 1
    private byte[] tiles = new byte[Constants.CHUNK_WIDTH * ((Constants.WORLD_HEIGHT * 2) + 1)]; //The tiles stored in this chunk, from bottom-left to top-left before moving horizontally to the right

    public Chunk(short chunkNum) { //Normal chunks

	chunkNumber = chunkNum;
	chunkTileX = chunkNumber * Constants.CHUNK_WIDTH;

    }

    public Chunk() { //Origin chunk

	chunkNumber = 0;
	chunkTileX = 0;
	biomeHeightMap = 0;
	biomeHeightMapChangeProbability = Constants.MAX_BIOME_WIDTH;
	blockHeightValue = Constants.ORIGIN_BLOCK_HEIGHT_VALUE;
	biomeTemperatureMap = 0;
	biomeTemperatureMapChangeProbability = Constants.MAX_BIOME_WIDTH;

    }

    public short getChunkNumber() {

	return chunkNumber;

    }

    public int getChunkTileX() { 

	return chunkTileX;

    }

    public byte getBiomeHeightMap() {

	return biomeHeightMap;

    }

    public void setBiomeHeightMap(byte newBiomeHeightMap) {

	biomeHeightMap = newBiomeHeightMap;

    }

    public byte getBiomeHeightMapChangeProbability() {

	return biomeHeightMapChangeProbability;

    }

    public void setBiomeHeightMapChangeProbability(byte newBiomeHeightMapChangeProbability) {

	biomeHeightMapChangeProbability = newBiomeHeightMapChangeProbability;

    }

    public void resetBiomeHeightMapChangeProbability() {

	biomeHeightMapChangeProbability = Constants.MAX_BIOME_WIDTH;

    }

    public void setToPreviousBiomeHeightMapChangeProbability() {

	biomeHeightMapChangeProbability = getPreviousBiomeHeightMapChangeProbability();

    }

    public byte createBiomeHeightMapChangeProbability() { //Sets BHMCP to previous and returns new one after updating

	setToPreviousBiomeHeightMapChangeProbability();
	return updateBiomeHeightMapChangeProbability();

    }

    public byte updateBiomeHeightMapChangeProbability() { //If (probability=1 [guaranteed]) then ([return 1] and [reset probability]) else ([return probability] and [decrease probability])

	return biomeHeightMapChangeProbability == 1 ? (byte) ((biomeHeightMapChangeProbability = Constants.MAX_BIOME_WIDTH) + 1 - Constants.MAX_BIOME_WIDTH) : biomeHeightMapChangeProbability--;

    }

    public byte getBiomeTemperatureMap() {

	return biomeTemperatureMap;

    }

    public void setBiomeTemperatureMap(byte newBiomeTemperatureMap) {

	biomeTemperatureMap = newBiomeTemperatureMap;

    }

    public byte getBiomeTemperatureMapChangeProbability() {

	return biomeTemperatureMapChangeProbability;

    }

    public void setBiomeTemperatureMapChangeProbability(byte newBiomeTemperatureMapChangeProbability) {

	biomeTemperatureMapChangeProbability = newBiomeTemperatureMapChangeProbability;

    }

    public void resetBiomeTemperatureMapChangeProbability() {

	biomeTemperatureMapChangeProbability = Constants.MAX_BIOME_WIDTH;

    }

    public void setToPreviousBiomeTemperatureMapChangeProbability() {

	biomeTemperatureMapChangeProbability = getPreviousBiomeTemperatureMapChangeProbability();

    }

    public byte createBiomeTemperatureMapChangeProbability() { //Sets BTMCP to previous and returns new one after updating

	setToPreviousBiomeTemperatureMapChangeProbability();
	return updateBiomeTemperatureMapChangeProbability();

    }

    public byte updateBiomeTemperatureMapChangeProbability() { //If (probability=1 [guaranteed]) then ([return 1] and [reset probability]) else ([return probability] and [decrease probability])

	return biomeTemperatureMapChangeProbability == 1 ? (byte) ((biomeTemperatureMapChangeProbability = Constants.MAX_BIOME_WIDTH) + 1 - Constants.MAX_BIOME_WIDTH) : biomeTemperatureMapChangeProbability--;

    }

    public byte getBlockHeightValue() {

	return blockHeightValue;

    }

    public void setBlockHeightValue(byte newValue) {

	blockHeightValue = newValue;

    }

    public byte[] getTiles() {

	return tiles;

    }

    public int getIndexFromRelativeCoords(int x, int y) {

	return (x + Constants.CHUNK_EXTENSION) * (Constants.WORLD_HEIGHT * 2 + 1) + (y + Constants.WORLD_HEIGHT); //To account for negatives

    }

    public int getIndexFromAbsoluteCoords(int x, int y ) {

	return (x - chunkTileX + Constants.CHUNK_EXTENSION) * (Constants.WORLD_HEIGHT * 2 + 1) + (y + Constants.WORLD_HEIGHT); //To account for negatives

    }

    public byte getTileFromRelativeCoords(int x, int y) {

	return tiles[getIndexFromRelativeCoords(x, y)];

    }

    public byte getTileFromAbsoluteCoords(int x, int y) {

	return tiles[getIndexFromAbsoluteCoords(x, y)];

    }

    public void setTileFromIndex(int index, byte tile) {

	tiles[index] = tile;

    }

    public void setTileFromRelativeCoords(int x, int y, int tile) {

	setTileFromIndex(getIndexFromRelativeCoords(x, y), (byte) tile);

    }

    public void setTileFromAbsoluteCoords(int x, int y, int tile) {

	setTileFromIndex(getIndexFromAbsoluteCoords(x, y), (byte) tile);

    }

    public byte[] getGroupOfTilesFromRelativeCoords(int x, int y, int width, int height) { //Gets the rectangular group of tiles en masse, with (x,y) being the group's bottom-left corner

	byte[] tilesGroup = new byte[width * height];

	for (int i = 0; i < width * height; i++) {

	    tilesGroup[i] = getTileFromRelativeCoords(x + (i /  height), y + (i % height));

	}

	return tilesGroup;

    }

    public void setGroupOfTilesFromRelativeCoords(int x, int y, byte[] tiles, int height) { //Sets the rectangular group of tiles en masse, with (x,y) being the group's bottom-left corner

	for (int i = 0; i < tiles.length; i++) {

	    if (tiles[i] == -1) continue; //Unchanged
	    setTileFromRelativeCoords(x + (i / height), y + (i % height), tiles[i]);

	}

    }

    public boolean doesRegionMatchTemplate(int x, int y, int width, int height, byte[] templateTiles) { //Check that all tiles in the selection match the given template

	//Checking the region fits fully in the chunk
	if (x + width - 1 > Constants.CHUNK_EXTENSION) return false;
	if (y + height - 1 > Constants.WORLD_HEIGHT) return false;

	for (int i = 0; i < templateTiles.length; i++) {
	    
	    if (templateTiles[i] < 0) { //Matches special identifiers
		
		if (templateTiles[i] == -1) continue;
		
	    } else if (getTileFromRelativeCoords(x + (i / height), y + (i % height)) != templateTiles[i]) { //Normal tile index
		
		return false;
		
	    }
	    
	}

	return true;

    }

    public static Chunk getChunk(int chunkNum) { //Forcefully returns a chunk at that position from either the list, external storage or world generation

	short chunkNumber = (short) chunkNum;
	Chunk chunk = Constants.WORLD.get(chunkNumber); //Look in list first

	if (chunk == null) { //If not in list it is either (saved and unloaded) or (not generated yet)

	    chunk = Chunk.loadChunk(chunkNumber);
	    Constants.WORLD.put(chunkNumber, chunk);

	}

	return chunk;

    }

    public short getPreviousChunkNumber() {

	return (short) (chunkNumber + (chunkNumber == 0 ? 0 : (chunkNumber > 0 ? -1 : 1)));

    }

    public Chunk getPreviousChunk() {

	return getChunk(getPreviousChunkNumber());

    }

    public byte getPreviousBiomeHeightMap() {

	return getPreviousChunk().getBiomeHeightMap();

    }

    public byte getPreviousBiomeHeightMapChangeProbability() {

	return getPreviousChunk().getBiomeHeightMapChangeProbability();

    }

    public byte getPreviousBlockHeightValue() {

	return getPreviousChunk().getBlockHeightValue();

    }

    public byte getPreviousBiomeTemperatureMap() {

	return getPreviousChunk().getBiomeTemperatureMap();

    }

    public byte getPreviousBiomeTemperatureMapChangeProbability() {

	return getPreviousChunk().getBiomeTemperatureMapChangeProbability();

    }

    public void save() { //Save the chunk to disk to be able to unload

	File worldDirectory = new File("data/world_" + Constants.SEED); //File directory of the current world instance
	if (!worldDirectory.exists()) worldDirectory.mkdirs(); //Create directory

	//Write to new file
	try (DataOutputStream dataWriter = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(new File(worldDirectory, "chunk_" + chunkNumber + ".dat"))))) {

	    //Header
	    dataWriter.writeShort(chunkNumber);
	    dataWriter.writeByte(biomeHeightMap);
	    dataWriter.writeByte(biomeHeightMapChangeProbability);
	    dataWriter.writeByte(blockHeightValue);
	    dataWriter.writeByte(biomeTemperatureMap);
	    dataWriter.writeByte(biomeTemperatureMapChangeProbability);

	    //Body
	    dataWriter.write(tiles);

	} catch (IOException e) {e.printStackTrace();}

    }

    public static Chunk loadChunk(short chunkNumber) { //Load chunk from external storage if the file exists, otherwise (or if error thrown) generate new chunk

	Chunk chunk;
	File chunkFile = new File("data/world_" + Constants.SEED + "/chunk_" + chunkNumber + ".dat");

	try (DataInputStream dataReader = new DataInputStream(new BufferedInputStream(new FileInputStream(chunkFile)))) {

	    chunk = new Chunk(dataReader.readShort());
	    chunk.setBiomeHeightMap(dataReader.readByte());
	    chunk.setBiomeHeightMapChangeProbability(dataReader.readByte());
	    chunk.setBlockHeightValue(dataReader.readByte());
	    chunk.setBiomeTemperatureMap(dataReader.readByte());
	    chunk.setBiomeTemperatureMapChangeProbability(dataReader.readByte());
	    dataReader.readFully(chunk.tiles);
	    return chunk;

	} catch (Exception e) {}

	return WorldGenerator.generateChunk(chunkNumber);

    }

}