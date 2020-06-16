package matyk.game.main.blocks;

import matyk.engine.data.Block;
import matyk.engine.data.EnumSide;
import org.joml.Vector3i;

public class BlockStone extends Block {
    @Override
    public boolean isFull() {
        return true;
    }

    @Override
    public int getX(EnumSide side) {
        return 5;
    }

    @Override
    public boolean render() {
        return true;
    }

    @Override
    public boolean hasAction() {
        return false;
    }

    @Override
    public void onActivated(Vector3i pos) {

    }

    @Override
    public boolean isTransparent() {
        return false;
    }
}
