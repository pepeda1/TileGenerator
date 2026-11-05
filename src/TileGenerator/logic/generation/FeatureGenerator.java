package TileGenerator.logic.generation;

import TileGenerator.logic.functions.WeightedRandom;
import TileGenerator.main.Constants;
import TileGenerator.models.Chunk;
import TileGenerator.models.enums.TREES;


public class FeatureGenerator {

    private static Chunk currentChunk;

    public static Chunk generateChunkFeatures(Chunk chunk) {

	currentChunk = chunk;
	placeTrees();

	return currentChunk;

    }

    private static void placeTrees() {

	//Decides type and frequency of trees to be placed
	TREES[] treesObjects = null;
	int[] treesWeights = null;
	switch ((currentChunk.getBiomeHeightMap() + 2) * 3 + (currentChunk.getBiomeTemperatureMap() + 1)) {

	    case 6: //Tundra has pine trees

		treesObjects = new TREES[] {TREES.PINE, null};
		treesWeights = new int[] {1, 1};
		break;

	    case 7: //Plains has basic trees

		treesObjects = new TREES[] {TREES.BASIC, null};
		treesWeights = new int[] {1, 9};
		break;

	}
	if (treesObjects == null) return; //No trees can be placed in this chunk


	//Looping through chunk and placing trees
	for (int x = -Constants.CHUNK_EXTENSION; x <= Constants.CHUNK_EXTENSION; x++) {

	    for (int y = -Constants.WORLD_HEIGHT; y <= Constants.WORLD_HEIGHT; y++) {

		//Check first if a tree was chosen then if tree can be placed
		TREES tree = WeightedRandom.randomItemFromObjectPool(treesObjects, treesWeights);
		if (tree != null && currentChunk.doesRegionMatchTemplate(x, y, tree.getWidth(), tree.getHeight(), tree.getRequiredTiles())) {

		    currentChunk.setGroupOfTilesFromRelativeCoords(x, y, tree.getTiles(), tree.getHeight());
		    
		}

	    }

	}

    }

}