package com.mactso.harderfarthergrimcitadels.events;

public class ClientSideDifficultyCache {
	
	private static float clientHardDifficulty = 0;
	private static float clientGrimDifficulty = 0;
	private static float clientTimeDifficulty = 0;
	private static float clientHighDifficulty = 0;

	

	public static float getGrimDifficulty() {
		return clientGrimDifficulty;
	}
	
	public static float getHardDifficulty() {
		return clientHardDifficulty;
	}
	
	public static float getTimeDifficulty() {
		return clientTimeDifficulty;
	}
	

	public static float getHighDifficulty() {

		return clientHighDifficulty;
	}
	

	
	public static void updateClientSideDifficultyCache(float hardDifficulty, float grimDifficulty, float timeDifficulty) {
	
		clientHardDifficulty = hardDifficulty;
		clientHighDifficulty = hardDifficulty;

		clientGrimDifficulty = grimDifficulty;
		clientHighDifficulty = Math.max(hardDifficulty, grimDifficulty);

		clientTimeDifficulty = timeDifficulty;  // this will be zero until TimeDifficulty is recreated (if ever).
		clientHighDifficulty = Math.max(clientHighDifficulty, timeDifficulty);

	}

}
