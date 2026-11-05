package TileGenerator.logic.functions;

import TileGenerator.main.Constants;

public class WeightedRandom {

    public static int randomItemFromIndexPool(int ... weights) { //Returns the index of the randomly chosen item from the weight pool

	//Calculate total
	int total = 0;
	for (int weight : weights) total += weight;
	//Random value chosen between 0 and the total
	int randomValue = Constants.RANDOM_NUMBER.nextInt(1, total + 1);

	//Loop through weights
	for (int i = 0; i < weights.length; i++) {

	    int currentWeight = weights[i];

	    if (randomValue > currentWeight) { //Not this item

		randomValue -= currentWeight;

	    } else { //Item has been found

		return i;

	    }

	}

	return weights.length - 1;

    }

    public static <item> item randomItemFromObjectPool(item[] objects, int[] weights) { //Returns the randomly chosen object from the weight pool, first object has first weight and so on

	//Calculate total
	int total = 0;
	for (int weight : weights) total += weight;
	//Random value chosen between 0 and the total
	int randomValue = Constants.RANDOM_NUMBER.nextInt(1, total + 1);

	//Loop through weights
	for (int i = 0; i < objects.length; i++) {

	    int currentWeight = weights[i];

	    if (randomValue > currentWeight) { //Not this item

		randomValue -= currentWeight;

	    } else { //Item has been found

		return objects[i];

	    }

	}

	return objects[objects.length - 1]; //Return last object

    }

}