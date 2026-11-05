package TileGenerator.logic.generation;

import TileGenerator.logic.functions.WeightedRandom;
import TileGenerator.main.Constants;
import TileGenerator.models.Chunk;

/* 
 * Generates terrain based on a number of variables:
 * 
 * BiomeHeightMap -> Determines the height profile of the biome, between -2 to 2 (mountains, hills, plains, coast, ocean)
 * BiomeTemperatureMap -> Determines the temperature profile of the biome, between -1 to 1 (cold, normal, hot)
 * BlockHeightValue -> Highest single tile in the previous chunk so changes are smooth and not drastic
 * 
 * 
 * This leads to 15 different biomes, shown below in the form [height, temperature] -> name -> id:
 * 
 * [-2, -1] -> Frozen ocean -> 0
 * [-2, 0] -> Ocean -> 1
 * [-2, 1] -> Kelp forest -> 2
 * [-1, -1] -> Ice sheet -> 3
 * [-1, 0] -> Coast -> 4
 * [-1, 1] -> Coral reef -> 5
 * [0, -1] -> Tundra -> 6
 * [0, 0] -> Plains -> 7
 * [0, 1] -> Desert -> 8
 * [1, -1] -> Snowy hills -> 9
 * [1, 0] -> Hills -> 10
 * [1, 1] -> Sand dunes -> 11
 * [2, -1] -> Frozen mountains -> 12
 * [2, 0] -> Mountains -> 13
 * [2, 1] -> Desert mountains -> 14
 * 
 * */

public class TerrainGenerator {

    public static Chunk generateChunkTerrain(short chunkNumber) {

	if (chunkNumber == 0) { //Origin chunk

	    return generateOriginTerrain();

	} else { //Normal chunks

	    return generateNormalTerrain(chunkNumber);

	}

    }

    private static Chunk generateOriginTerrain() {

	Chunk chunk = new Chunk();

	for (int x = -Constants.CHUNK_EXTENSION; x <= Constants.CHUNK_EXTENSION; x++) {

	    for (int y = -Constants.WORLD_HEIGHT; y <= Constants.WORLD_HEIGHT; y++) {

		chunk.setTileFromRelativeCoords(x, y, y < Constants.ORIGIN_BLOCK_HEIGHT_VALUE - Constants.DIRT_DEPTH ? 2 : y < Constants.ORIGIN_BLOCK_HEIGHT_VALUE ? 4 :  y == Constants.ORIGIN_BLOCK_HEIGHT_VALUE ? 3 : 0);

	    }

	}

	return chunk;

    }

    private static Chunk generateNormalTerrain(short chunkNumber) {

	//Main variables
	Chunk chunk = new Chunk(chunkNumber);

	//Changing biome height
	if (Constants.RANDOM_NUMBER.nextInt(chunk.createBiomeHeightMapChangeProbability()) == 0) { //Changing biome height based on updated BHMCP

	    chunk.setBiomeHeightMap(updateBiomeHeightMap(chunk.getPreviousBiomeHeightMap()));
	    chunk.resetBiomeHeightMapChangeProbability();

	} else {

	    chunk.setBiomeHeightMap(chunk.getPreviousBiomeHeightMap());

	}
	//Changing biome temperature
	if (Constants.RANDOM_NUMBER.nextInt(chunk.createBiomeTemperatureMapChangeProbability()) == 0) { //Changing biome temperature based on updated BTMCP

	    chunk.setBiomeTemperatureMap(updateBiomeTemperatureMap(chunk.getPreviousBiomeTemperatureMap()));
	    chunk.resetBiomeTemperatureMapChangeProbability();

	} else {

	    chunk.setBiomeTemperatureMap(chunk.getPreviousBiomeTemperatureMap());

	}

	//Generating basic terrain
	byte currentBlockHeightValue = chunk.getPreviousBlockHeightValue();
	int currentBiome = (chunk.getBiomeHeightMap() + 2) * 3 + (chunk.getBiomeTemperatureMap() + 1);
	int positive = chunkNumber > 0 ? 1 : -1;
	for (int x = -Constants.CHUNK_EXTENSION; x <= Constants.CHUNK_EXTENSION; x++) {

	    //Set BHV for column
	    currentBlockHeightValue = setBlockHeightValueForColumn(currentBlockHeightValue, chunk.getBiomeHeightMap());

	    //Generating the vertical column
	    for (int y = -Constants.WORLD_HEIGHT; y <= Constants.WORLD_HEIGHT; y++) {

		switch (currentBiome) {

		    case 0: //Frozen ocean places stone until BHV then ice until sea level-1 then a block of snow

			if (y <= currentBlockHeightValue) chunk.setTileFromRelativeCoords(x * positive, y, 2); //Stone
			else if (y < Constants.SEA_LEVEL) chunk.setTileFromRelativeCoords(x * positive, y, 8); //Ice
			else if (y == Constants.SEA_LEVEL) chunk.setTileFromRelativeCoords(x * positive, y, 7); //Snow
			else chunk.setTileFromRelativeCoords(x * positive, y, 0); //Air
			break;


		    case 1: //Ocean places stone until BHV then water until sea level

			if (y <= currentBlockHeightValue) chunk.setTileFromRelativeCoords(x * positive, y, 2); //Stone
			else if (y <= Constants.SEA_LEVEL) chunk.setTileFromRelativeCoords(x * positive, y, 1); //Water
			else chunk.setTileFromRelativeCoords(x * positive, y, 0); //Air
			break;


		    case 2: //Kelp forest places stone until BHV-dirtDepth then dirtDepth layers of sand then water

			if (y <= currentBlockHeightValue - Constants.DIRT_DEPTH) chunk.setTileFromRelativeCoords(x * positive, y, 2); //Stone
			else if (y <= currentBlockHeightValue) chunk.setTileFromRelativeCoords(x * positive, y, 5); //Sand
			else if (y <= Constants.SEA_LEVEL) chunk.setTileFromRelativeCoords(x * positive, y, 1); //Water
			else chunk.setTileFromRelativeCoords(x * positive, y, 0); //Air
			break;


		    case 3: //Ice sheet places stone until BHV then ice until sea level-1 then a block of snow

			if (y <= currentBlockHeightValue) chunk.setTileFromRelativeCoords(x * positive, y, 2); //Stone
			else if (y < Constants.SEA_LEVEL) chunk.setTileFromRelativeCoords(x * positive, y, 8); //Ice
			else if (y == Constants.SEA_LEVEL) chunk.setTileFromRelativeCoords(x * positive, y, 7); //Snow
			else chunk.setTileFromRelativeCoords(x * positive, y, 0); //Air
			break;


		    case 4: //Coast places stone until BHV-dirtDepth then dirtDepth layers of sand then water

			if (y <= currentBlockHeightValue - Constants.DIRT_DEPTH) chunk.setTileFromRelativeCoords(x * positive, y, 2); //Stone
			else if (y <= currentBlockHeightValue) chunk.setTileFromRelativeCoords(x * positive, y, 5); //Sand
			else if (y <= Constants.SEA_LEVEL) chunk.setTileFromRelativeCoords(x * positive, y, 1); //Water
			else chunk.setTileFromRelativeCoords(x * positive, y, 0); //Air
			break;


		    case 5: //Coral places stone until BHV-dirtDepth*2 then dirtDepth layers of sandstone then dirtDepth layers of sand then water

			if (y <= currentBlockHeightValue - Constants.DIRT_DEPTH * 2) chunk.setTileFromRelativeCoords(x * positive, y, 2); //Stone
			else if (y <= currentBlockHeightValue - Constants.DIRT_DEPTH) chunk.setTileFromRelativeCoords(x * positive, y, 6); //Sandstone
			else if (y <= currentBlockHeightValue) chunk.setTileFromRelativeCoords(x * positive, y, 5); //Sand
			else if (y <= Constants.SEA_LEVEL) chunk.setTileFromRelativeCoords(x * positive, y, 1); //Water
			else chunk.setTileFromRelativeCoords(x * positive, y, 0); //Air
			break;


		    case 6: //Tundra places stone until BHV-dirtDepth then dirtDepth layers of dirt then snow

			if (y <= currentBlockHeightValue - Constants.DIRT_DEPTH) chunk.setTileFromRelativeCoords(x * positive, y, 2); //Stone
			else if (y < currentBlockHeightValue) chunk.setTileFromRelativeCoords(x * positive, y, 4); //Dirt
			else if ((y == currentBlockHeightValue && y >= Constants.SEA_LEVEL) || y == Constants.SEA_LEVEL) chunk.setTileFromRelativeCoords(x * positive, y, 7); //Snow
			else if (y < Constants.SEA_LEVEL) chunk.setTileFromRelativeCoords(x * positive, y, 8); //Ice lakes
			else chunk.setTileFromRelativeCoords(x * positive, y, 0); //Air
			break;


		    case 7: //Plains places stone until BHV-dirtDepth then dirtDepth layers of dirt then grass

			if (y <= currentBlockHeightValue - Constants.DIRT_DEPTH) chunk.setTileFromRelativeCoords(x * positive, y, 2); //Stone
			else if (y < currentBlockHeightValue) chunk.setTileFromRelativeCoords(x * positive, y, 4); //Dirt
			else if (y == currentBlockHeightValue && y >= Constants.SEA_LEVEL) chunk.setTileFromRelativeCoords(x * positive, y, 3); //Grass
			else if (y <= Constants.SEA_LEVEL) chunk.setTileFromRelativeCoords(x * positive, y, 1); //Lakes
			else chunk.setTileFromRelativeCoords(x * positive, y, 0); //Air
			break;


		    case 8: //Desert places stone until BHV-dirtDepth*2 then dirtDepth layers of sandstone then dirtDepth layers of sand

			if (y <= currentBlockHeightValue - Constants.DIRT_DEPTH * 2) chunk.setTileFromRelativeCoords(x * positive, y, 2); //Stone
			else if (y <= currentBlockHeightValue - Constants.DIRT_DEPTH) chunk.setTileFromRelativeCoords(x * positive, y, 6); //Sandstone
			else if (y <= currentBlockHeightValue) chunk.setTileFromRelativeCoords(x * positive, y, 5); //Sand
			else if (y <= Constants.SEA_LEVEL) chunk.setTileFromRelativeCoords(x * positive, y, 1); //Oasis
			else chunk.setTileFromRelativeCoords(x * positive, y, 0); //Air
			break;


		    case 9: //Snowy hills places stone until BHV-dirtDepth then dirtDepth layers of dirt then snow

			if (y <= currentBlockHeightValue - Constants.DIRT_DEPTH) chunk.setTileFromRelativeCoords(x * positive, y, 2); //Stone
			else if (y < currentBlockHeightValue) chunk.setTileFromRelativeCoords(x * positive, y, 4); //Dirt
			else if (y == currentBlockHeightValue) chunk.setTileFromRelativeCoords(x * positive, y, 7); //Snow
			else chunk.setTileFromRelativeCoords(x * positive, y, 0); //Air
			break;


		    case 10: //Hills places stone until BHV-dirtDepth then dirtDepth layers of dirt then grass

			if (y <= currentBlockHeightValue - Constants.DIRT_DEPTH) chunk.setTileFromRelativeCoords(x * positive, y, 2); //Stone
			else if (y < currentBlockHeightValue) chunk.setTileFromRelativeCoords(x * positive, y, 4); //Dirt
			else if (y == currentBlockHeightValue) chunk.setTileFromRelativeCoords(x * positive, y, 3); //Grass
			else chunk.setTileFromRelativeCoords(x * positive, y, 0); //Air
			break;


		    case 11: //Sand dunes places stone until BHV-dirtDepth*2 then dirtDepth layers of sandstone then dirtDepth layers of sand

			if (y <= currentBlockHeightValue - Constants.DIRT_DEPTH * 2) chunk.setTileFromRelativeCoords(x * positive, y, 2); //Stone
			else if (y <= currentBlockHeightValue - Constants.DIRT_DEPTH) chunk.setTileFromRelativeCoords(x * positive, y, 6); //Sandstone
			else if (y <= currentBlockHeightValue) chunk.setTileFromRelativeCoords(x * positive, y, 5); //Sand
			else chunk.setTileFromRelativeCoords(x * positive, y, 0); //Air
			break;


		    case 12: //Frozen mountains places stone until BHV-dirtDepth*2 then dirtDepth layers of ice then dirtDepth layers of snow

			if (y <= currentBlockHeightValue - Constants.DIRT_DEPTH * 2) chunk.setTileFromRelativeCoords(x * positive, y, 2); //Stone
			else if (y <= currentBlockHeightValue - Constants.DIRT_DEPTH) chunk.setTileFromRelativeCoords(x * positive, y, 8); //Ice
			else if (y <= currentBlockHeightValue) chunk.setTileFromRelativeCoords(x * positive, y, 7); //Snow
			else chunk.setTileFromRelativeCoords(x * positive, y, 0); //Air
			break;


		    case 13: //Mountains places stone until BHV-dirtDepth then dirtDepth layers of snow

			if (y <= currentBlockHeightValue - Constants.DIRT_DEPTH) chunk.setTileFromRelativeCoords(x * positive, y, 2); //Stone
			else if (y <= currentBlockHeightValue) chunk.setTileFromRelativeCoords(x * positive, y, 7); //Snow
			else chunk.setTileFromRelativeCoords(x * positive, y, 0); //Air
			break;


		    case 14: //Desert mountains places stone until BHV-dirtDepth*2 then dirtDepth*2 layers of sandstone

			if (y <= currentBlockHeightValue - Constants.DIRT_DEPTH * 2) chunk.setTileFromRelativeCoords(x * positive, y, 2); //Stone
			else if (y <= currentBlockHeightValue) chunk.setTileFromRelativeCoords(x * positive, y, 6); //Sandstone
			else chunk.setTileFromRelativeCoords(x * positive, y, 0); //Air
			break;

		}

	    }

	}

	//Set BHV for entire chunk
	chunk.setBlockHeightValue(currentBlockHeightValue);

	return chunk;

    }

    private static byte updateBiomeHeightMap(byte currentBiomeHeightMap) {

	if (Math.abs(currentBiomeHeightMap) == 2) { //The extremeties in biome heights are more unstable

	    return (byte) (currentBiomeHeightMap / 2);

	} else { //Normal biome heights

	    return (byte) (currentBiomeHeightMap + (Constants.RANDOM_NUMBER.nextInt(2) * 2 - 1)); //Change BHM by either +1 or -1

	}

    }

    private static byte setBlockHeightValueForColumn(int blockHeightValue, short biomeHeightMap) {

	byte lowerNaturalElevationBound = Constants.NATURAL_ELEVATIONS[biomeHeightMap + 2];
	byte upperNaturalElevationBound = Constants.NATURAL_ELEVATIONS[biomeHeightMap + 3];
	if (blockHeightValue >= lowerNaturalElevationBound && blockHeightValue <= upperNaturalElevationBound) { //Within natural elevation bounds

	    //Set chances to change BHV
	    int blockHeightValueChange;
	    switch (biomeHeightMap) {

		case -2: 

		    blockHeightValueChange = (WeightedRandom.randomItemFromIndexPool(3, 10, 1) - 1); //Ocean
		    break;

		case -1: 

		    blockHeightValueChange = (WeightedRandom.randomItemFromIndexPool(1, 5, 15, 5, 1) - 2); //Coast
		    break;

		case 1: 

		    blockHeightValueChange = (WeightedRandom.randomItemFromIndexPool(1, 4, 8, 6, 1) - 2); //Hills
		    break;

		case 2: 

		    blockHeightValueChange = (WeightedRandom.randomItemFromIndexPool(3, 4, 3, 5, 4) - 2); //Mountains
		    break;

		default: 

		    blockHeightValueChange = (WeightedRandom.randomItemFromIndexPool(1, 10, 1) - 1); //Plains
		    break;


	    }
	    blockHeightValue += blockHeightValueChange; //Change by -2 blocks to +2 blocks

	} else if (blockHeightValue < lowerNaturalElevationBound) { //Lower than natural elevation bound

	    blockHeightValue += WeightedRandom.randomItemFromIndexPool(1, 5, 5, 2) - 1; //Change by -1 blocks to +2 blocks

	} else { //Higher than natural elevation bound

	    blockHeightValue -= WeightedRandom.randomItemFromIndexPool(1, 5, 5, 2) - 1; //Change by +1 blocks to -2 blocks

	}

	blockHeightValue = Math.clamp(blockHeightValue, -Constants.WORLD_HEIGHT, Constants.WORLD_HEIGHT);

	return (byte) blockHeightValue;

    }

    private static byte updateBiomeTemperatureMap(byte currentBiomeTemperatureMap) {

	if (currentBiomeTemperatureMap == 0) { //Normal

	    return (byte) (Constants.RANDOM_NUMBER.nextInt(2) * 2 - 1);

	} else { //Hot or cold

	    return 0;

	}

    }

}