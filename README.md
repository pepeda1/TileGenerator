# TileGenerator
2D procedural terrain generator, created for fun. All code and art was made by me.
<br><br><br>
Generates terrain "infinitely" chunk by chunk. Chunks on the negative side of the x axis are generated in reverse. Most settings (eg chunk width and height, sea level, tile size, biome natural heights etc) are directly editable in the `Constants` class. The origin chunk always generated the same terrain every time.

Each chunk has one of 15 biomes, determined by 2 variables:
+ `biomeHeightMap`: Determines the average height of the top level of terrain in the chunk
+ `biomeTemperatureMap`: Determines the temperature profile of the chunk

| ↓ height / temperature → | -1 (cold) | 0 (normal) | 1 (hot) |
| :--- | :--- | :--- | :--- |
| **-2 (deep ocean)** | Frozen ocean | Ocean | Kelp forest |
| **-1 (shallow coast)** | Ice sheet | Coast | Coral reef |
| **0 (flat land)** | Tundra | Plains | Desert |
| **1 (hills)** | Snowy hills | Hills | Sand dunes |
| **2 (mountains)** | Frozen mountains | Mountains | Desert mountains |

Currently, there are 2 non-terrain features, oak trees spawning with a 10% chance in the plains biomes and pine trees spawning with a 50% chance in the tundra biome. Palm trees, cacti, kelp and coral _may_ spawn in the coast, desert and ocean biomes respectively (no guarantees).
