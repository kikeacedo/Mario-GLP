package dk.itu.mario.level.generator;

import dk.itu.mario.MarioInterface.GamePlay;
import dk.itu.mario.MarioInterface.LevelGenerator;
import dk.itu.mario.MarioInterface.LevelInterface;
import dk.itu.mario.level.MarioLevel;
import dk.itu.mario.level.QuiqueLevel;

public class CustomizedLevelGenerator implements LevelGenerator{

	public LevelInterface generateLevel() {
		LevelInterface level = new QuiqueLevel(20,150);
		return level;
	}
	
	public LevelInterface generateLevel(GamePlay playerMetrics) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public LevelInterface generateLevel(String detailedInfo) {
		// TODO Auto-generated method stub
		return null;
	} 

}
