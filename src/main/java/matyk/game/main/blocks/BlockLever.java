package matyk.game.main.blocks;

import matyk.engine.data.Block;
import matyk.engine.data.EnumSide;
import matyk.engine.data.World;
import org.joml.Vector3i;

public class BlockLever extends Block {
    public boolean active = false;


    @Override
    public boolean isFull() {
        return true;
    }

    @Override
    public int getX(EnumSide side) {
        return active ? 4 : 3;
    }

    @Override
    public boolean render() {
        return true;
    }

    @Override
    public boolean hasAction() {
        return true;
    }

    @Override
    public void onActivated(Vector3i pos) {
        active = !active;
        World.setBlockAt(pos.x, pos.y, pos.z, this);
    }

    @Override
    public int getLightLevel() {
        return active ? 15 : 0;
    }

    @Override
    public boolean isTransparent() {
        return false;
    }
}
