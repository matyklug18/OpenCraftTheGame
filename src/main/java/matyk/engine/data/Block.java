package matyk.engine.data;

import org.joml.Vector3i;

public abstract class Block {
    public int[] lightLevel = new int[6];

    public int[] aLightLevel = new int[6];

    abstract public boolean isFull();

    abstract public int getX(EnumSide side);

    abstract public boolean render();

    abstract public boolean hasAction();

    abstract public void onActivated(Vector3i pos);

    public int getLightLevel() {
        return 0;
    }

    abstract public boolean isTransparent();
}
