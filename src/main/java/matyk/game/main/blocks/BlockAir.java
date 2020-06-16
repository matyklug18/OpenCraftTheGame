package matyk.game.main.blocks;

import matyk.engine.data.Block;
import matyk.engine.data.EnumSide;
import org.joml.Vector3i;

public class BlockAir extends Block {
    @Override
    public boolean isFull() {
        return false;
    }

    @Override
    public int getX(EnumSide side) {
        return 16;
    }

    @Override
    public boolean render() {
        return false;
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
        return true;
    }
}
