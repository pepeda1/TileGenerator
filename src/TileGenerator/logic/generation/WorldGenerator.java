package TileGenerator.logic.generation;

import TileGenerator.models.Chunk;


public class WorldGenerator {

	public static Chunk generateChunk(short chunkNumber) {
		
		Chunk chunk = TerrainGenerator.generateChunkTerrain(chunkNumber);
		chunk = FeatureGenerator.generateChunkFeatures(chunk);
		return chunk;
		
	}
	
}