package matyk.game.main.blocks;

import matyk.engine.data.Block;
import matyk.engine.data.EnumSide;
import org.joml.Vector3i;

public class BlockGrass extends Block {
    @Override
    public boolean isFull() {
        return true;
    }

    @Override
    public int getX(EnumSide side) {
        if(side == EnumSide.TOP)
            return 0;
        if(side == EnumSide.LEFT)
            return 2;
        if(side == EnumSide.RIGHT)
            return 2;
        if(side == EnumSide.FRONT)
            return 2;
        if(side == EnumSide.BACK)
            return 2;
        if(side == EnumSide.BOT)
            return 1;
        return 16;
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
