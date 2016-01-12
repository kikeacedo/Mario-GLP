package dk.itu.mario.level;

import java.util.Random;

import dk.itu.mario.MarioInterface.LevelInterface;
import dk.itu.mario.engine.sprites.Enemy;
import dk.itu.mario.engine.sprites.SpriteTemplate;

public class MarioLevel extends Level implements LevelInterface {

	Random random;
	private int floor;

	public MarioLevel(int height, int width) {
		super(width, height);
		random = new Random();
		create();
	}

	public void create() {
		floor = height - 1;
		xExit = this.width - 10;
		yExit = floor;

		for (int x = 0; x < this.width; x++) {
			for (int y = 0; y < height; y++) {
				if (y > floor) {
					setBlock(x, y, Level.GROUND);	// Not ground you can stand on!
													// Only decoration
				} else if (y == floor) {
					setBlock(x, y, Level.HILL_TOP); // A grassy block that you
													// can stand on.
				}
			}

		}
		
		this.buildTubes(15, 3, 3, 2, 2);
		this.buildPlatform(20, 2, 10, "");
		this.buildGaps(50, 2, 5, 3, 3);
		this.buildHillStraight(80, 3, 5);
		this.buildCannons(100, 2, 2, 2, 2);
		
		
		this.setSpriteTemplate(6, floor - 3, new SpriteTemplate(SpriteTemplate.GREEN_TURTLE, false));


	}

	private int buildHillCannon(int x, int y, int wide, int wb, int wa) {

		int res = this.buildHillStraight(x, y, wide);

		int x2 = x + this.random.nextInt(wide);

		this.buildCannons(x2, y, this.random.nextInt(y) + 3, wb, wa);

		return res;

	}

	private int buildHillTube(int x, int y, int wide, int wb, int wa) {

		int res = this.buildHillStraight(x, y, wide);

		int x2 = x + this.random.nextInt(wide);

		this.buildTubes(x2, y, this.random.nextInt(y) + 3, wb, wa);

		return res;

	}

	private int buildPlatform(int x, int y, int wide, String mode) {
		for (int i = x; i <= x + wide; i++) {
			for (int j = floor - y - 1; j <= floor - 1; j++) {
				if (i == x) {
					if (j == floor - y - 1) {
						if (mode.equals("left"))
							if (this.getBlock(i, j) == Level.EMPTY)
								this.setBlock(i, j, Level.HILL_TOP_LEFT_IN);
							else
								this.setBlock(i, j, this.getBlock(i, j));
						else if (this.getBlock(i, j) == Level.EMPTY)
							this.setBlock(i, j, Level.HILL_TOP_LEFT);
						else
							this.setBlock(i, j, this.getBlock(i, j));
					} else if (this.getBlock(i, j) == Level.EMPTY)
						this.setBlock(i, j, Level.HILL_LEFT);
					else
						this.setBlock(i, j, this.getBlock(i, j));
				} else if (i == x + wide) {
					if (j == floor - y - 1) {
						if (mode.equals("right"))
							if (this.getBlock(i, j) == Level.EMPTY)
								this.setBlock(i, j, Level.HILL_TOP_RIGHT_IN);
							else
								this.setBlock(i, j, this.getBlock(i, j));
						else if (this.getBlock(i, j) == Level.EMPTY)
							this.setBlock(i, j, Level.HILL_TOP_RIGHT);
						else
							this.setBlock(i, j, this.getBlock(i, j));
					} else if (this.getBlock(i, j) == Level.EMPTY)
						this.setBlock(i, j, Level.HILL_RIGHT);
					else
						this.setBlock(i, j, this.getBlock(i, j));
				} else {
					if (j == floor - y - 1) {
						if (this.getBlock(i, j) == Level.EMPTY)
							this.setBlock(i, j, Level.HILL_TOP);
						else
							this.setBlock(i, j, this.getBlock(i, j));
					} else if (this.getBlock(i, j) == Level.EMPTY)
						this.setBlock(i, j, Level.HILL_FILL);
					else
						this.setBlock(i, j, this.getBlock(i, j));
				}

			}
		}

		return wide;

	}

	private int buildGaps(int x, int y, int wide, int wb, int wa) {
		Level old = null;
		try {
			old = this.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}

		this.buildPlatform(x - wb, y, wide + wa + 2, "");

		for (int i = x; i <= x + wide + 1; i++) {
			for (int j = floor - y - 1; j <= floor - 1; j++) {
				if (j == floor - y - 1) {
					if (i == x)
						this.setBlock(i, j, Level.HILL_TOP_RIGHT);
					else if (i == x + wide + 1)
						this.setBlock(i, j, Level.HILL_TOP_LEFT);
					else
						this.setBlock(i, j, old.getBlock(i, j));
				} else {
					if (i == x)
						this.setBlock(i, j, Level.HILL_RIGHT);
					else if (i == x + wide + 1)
						this.setBlock(i, j, Level.HILL_LEFT);
					else
						this.setBlock(i, j, old.getBlock(i, j));
				}
			}
		}

		return wide + wa + wb;
	}

	private int buildCannons(int x, int y, int h, int wb, int wa) {
		for (int i = x - wb; i < x; i++)
			this.setBlock(i, floor, Level.HILL_TOP);
		for (int i = x + 1; i < x + 1 + wa; i++)
			this.setBlock(i, floor, Level.HILL_TOP);
		for (int j = floor - h - 1; j < floor; j++) {
			if (j == floor - h - 1) {
				setBlock(x, j, Level.CANNON_TOP);
			} else if (j == floor - h) {
				setBlock(x, j, Level.CANNON_GRIP);
			} else
				setBlock(x, j, Level.CANNON_BASE);

		}

		return wa + wb + 1;
	}

	private int buildHillStraight(int x, int y, int wide) {
		Random r = new Random();
		this.buildPlatform(x, y, wide, "");

		int height = r.nextInt(y);
		height = height >= y ? 1 : height;

		int x2 = x + r.nextInt(wide);

		if (r.nextInt(2) > 1) {
			x2 *= -1;
			this.buildPlatform(x2, height, wide, "right");
		} else
			this.buildPlatform(x2, height, wide, "left");

		return wide + x2 - x;
	}

	private int buildTubes(int x, int y, int h, int wb, int wa) {

		for (int i = x - wb; i < x; i++)
			this.setBlock(i, floor, Level.HILL_TOP);
		for (int i = x + 2; i < x + 2 + wa; i++)
			this.setBlock(i, floor, Level.HILL_TOP);

		for (int i = x; i <= x + 1; i++) {
			for (int j = floor - h - 1; j < floor; j++) {
				if (j == floor - h - 1) {
					if (i == x)
						setBlock(i, j, Level.TUBE_TOP_LEFT);
					else
						setBlock(i, j, Level.TUBE_TOP_RIGHT);
				} else {
					if (i == x)
						setBlock(i, j, Level.TUBE_SIDE_LEFT);
					else
						setBlock(i, j, Level.TUBE_SIDE_RIGHT);
				}
			}

		}
		return wa + wb + 2;
	}

}
