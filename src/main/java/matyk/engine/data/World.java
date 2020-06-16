package matyk.engine.data;

import matyk.engine.meshing.Mesher;
import matyk.engine.physics.AABB;
import matyk.engine.utils.PrintUtils;
import matyk.engine.utils.Utils;
import matyk.game.main.Main;
import matyk.game.main.blocks.BlockAir;
import org.joml.Vector3f;
import org.joml.Vector3i;

import java.util.Arrays;

public class World {
    public static final Vector3i size = new Vector3i(4,8,4);

    public static Chunk[][][] chunks = new Chunk[size.x][size.y][size.z];

    public static void mesh(Block[][][] blocks) {
        for(int x = 0; x < size.x; x++) {
            for (int y = 0; y < size.y; y++) {
                for (int z = 0; z < size.z; z++) {
                    chunks[x][y][z] = new Chunk();
                    for (int i = 0; i < 16; i++) {
                        for (int j = 0; j < 16; j++) {
                            for (int k = 0; k < 16; k++) {
                                chunks[x][y][z].blocks[i][j][k] = blocks[x * 16 + i][y * 16 + j][z * 16 + k];
                                if(blocks[x * 16 + i][y * 16 + j][z * 16 + k].render())
                                    chunks[x][y][z].contains = true;
                            }
                        }
                    }
                    chunks[x][y][z].mesh = Mesher.mesh(chunks[x][y][z].blocks, new Vector3f(x,y,z).mul(16));
                }
            }
        }
    }

    public static Block[][][] getAllBlocks() {
        Block[][][] returnV = new Block[World.size.x*16][World.size.y*16][World.size.z*16];
        for(int i = 0; i < World.size.x; i++) {
            for (int j = 0; j < World.size.y; j++) {
                for (int k = 0; k < World.size.z; k++) {
                    for (int x = 0; x < 16; x++) {
                        for (int y = 0; y < 16; y++) {
                            for (int z = 0; z < 16; z++) {
                                returnV[i*16+x][j*16+y][k*16+z] = chunks[i][j][k].blocks[x][y][z];
                            }
                        }
                    }
                }
            }
        }
        return returnV;
    }

    public static Block getBlockAt(int x, int y, int z) {
        return chunks[x/16][y/16][z/16].blocks[x%16][y%16][z%16];
    }

    public static void clean(int i, int j, int k) {
        if(World.chunks[i / 16][j / 16][k / 16].dirty)
            World.chunks[i / 16][j / 16][k / 16].mesh = Mesher.mesh(World.chunks[i / 16][j / 16][k / 16].blocks, new Vector3f(i / 16, j / 16, k / 16).mul(16));
        World.chunks[i / 16][j / 16][k / 16].dirty = false;
    }


    public static void clean() {
        for (int i = 0; i < World.size.x; i++) {
            for (int j = 0; j < World.size.y; j++) {
                for (int k = 0; k < World.size.z; k++) {
                    if(World.chunks[i][j][k].dirty) {
                        World.chunks[i][j][k].mesh = Mesher.mesh(World.chunks[i][j][k].blocks, new Vector3f(i, j, k));
                        World.chunks[i][j][k].dirty = false;
                    }
                }
            }
        }
    }

    public static void setBlockAt(int x, int y, int z, Block block) {
        chunks[x/16][y/16][z/16].blocks[x%16][y%16][z%16] = block;
        chunks[x/16][y/16][z/16].mesh = Mesher.mesh(chunks[x/16][y/16][z/16].blocks, new Vector3f(x/16,y/16,z/16).mul(16));
        chunks[x/16][y/16][z/16].aabbs = new AABB[16][16][16];

        Block[][][] blocks = chunks[x/16][y/16][z/16].blocks;

        for(int i = 0; i < 16; i++) {
            for(int j = 0; j < 16; j++) {
                for(int k = 0; k < 16; k++) {
                    if
                    (
                        ((i < blocks.length - 1 && !blocks[i + 1][j][k].isFull()) || i == blocks.length - 1) && blocks[i][j][k].isFull()
                        || ((i > 0 && !blocks[i - 1][j][k].isFull()) || i == 0) && blocks[i][j][k].isFull()
                        || ((j < blocks[i].length - 1 && !blocks[i][j + 1][k].isFull()) || j == blocks[i].length - 1) && blocks[i][j][k].isFull()
                        || ((j > 0 && !blocks[i][j - 1][k].isFull()) || j == 0) && blocks[i][j][k].isFull()
                        || ((k < blocks[i][j].length - 1 && !blocks[i][j][k + 1].isFull()) || k == blocks[i][j].length - 1) && blocks[i][j][k].isFull()
                        || ((k > 0 && !blocks[i][j][k - 1].isFull()) || k == 0) && blocks[i][j][k].isFull()
                    )
                    {
                        Vector3f a = new Vector3f(x/16*16+i+1,y/16*16+j+1,z/16*16+k+1);
                        Vector3f b = new Vector3f(x/16*16+i,y/16*16+j,z/16*16+k);
                        chunks[x/16][y/16][z/16].aabbs[i][j][k] = new AABB(a, b);
                    }
                }
            }
        }

        Block[][][] allBlocks = getAllBlocks();

        for (int i = 0; i < World.size.x * 16; i++) {
            for (int j = 0; j < World.size.y * 16; j++) {
                for (int k = 0; k < World.size.z * 16; k++) {
                    for(int l = 0; l < 6; l++) {
                        //allBlocks[i][j][k].lightLevel[l] = 0;
                    }
                }
            }
        }

        for (int i = 0; i < World.size.x * 16; i++) {
            for (int j = 0; j < World.size.y * 16; j++) {
                for (int k = 0; k < World.size.z * 16; k++) {
                    if(!(allBlocks[i][j][k].isTransparent()))
                        for (int l = j + 1; l < World.size.y * 16; l ++) {
                            if (l == World.size.y * 16 - 1) {
                                Main.propagate(new Vector3i(i,j,k), allBlocks, 15);
                                break;
                            }
                            if(!(allBlocks[i][l][k].isTransparent()))
                                break;
                        }
                }
            }
        }
        clean(x,y,z);
        //clean();
    }
}
