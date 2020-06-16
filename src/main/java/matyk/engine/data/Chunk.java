package matyk.engine.data;

import matyk.engine.physics.AABB;

public class Chunk {
    public boolean dirty;

    public Block[][][] blocks = new Block[16][16][16];

    public boolean contains = false;

    public Mesh mesh;

    public AABB[][][] aabbs = new AABB[16][16][16];
}
