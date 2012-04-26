package fasp.pong;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class LevelCreator {

	private Brick[] level;

	public LevelCreator() {

	}

	public Brick[] getLevel() {
		return level;
	}

	public void createLevel(String fileName) throws IOException {
		try {
			BufferedReader file = null;
			file = new BufferedReader(new FileReader(fileName));

			String line;
			level = new Brick[getRowAmount(fileName)];

			// Creates an array storing all the bricks that are supposed to make
			// up the level from a text file.
			// The arguments are a (x-coordinate), b (y-coordinate), c (width),
			// e (height and f (durability)
			int i = 0;
			while ((line = file.readLine()) != null) {
				String[] row = line.split(" ");
				level[i] = new Brick(Float.parseFloat(row[0]),
						Float.parseFloat(row[1]), Float.parseFloat(row[2]),
						Float.parseFloat(row[3]), Integer.parseInt(row[4]));
				i++;
			}
		} catch (IOException e) {
		}
	}

	// Checks how many rows the file contains i.e. how many bricks that the level
	// will contain.
	public int getRowAmount(String fileName) throws IOException {
		String line;
		int rowAmount = 0;
		BufferedReader file = null;
		file = new BufferedReader(new FileReader(fileName));

		while ((line = file.readLine()) != null) {
			rowAmount++;
		}
		return rowAmount++;
	}

}
