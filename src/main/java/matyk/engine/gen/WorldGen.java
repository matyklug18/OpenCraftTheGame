package matyk.engine.gen;

import matyk.engine.data.Block;
import matyk.game.main.blocks.BlockAir;
import matyk.game.main.blocks.BlockDirt;

public class WorldGen {
    public static Block getBlock(int i, int j, int k) {
        boolean air = NoiseGen.getHeightmap(i, j, k, 32, 0.4f, 4, 1, 0.15f) < j - 64;
        return air ? new BlockAir() : new BlockDirt();
    }
}
