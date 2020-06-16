package matyk.engine.meshing;

import matyk.engine.data.Block;
import matyk.engine.data.EnumSide;
import matyk.engine.data.Mesh;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector3i;

import java.util.ArrayList;
import java.util.Arrays;

public class Mesher {

    private static final int[] inds = new int[] {
            2, 1, 0,
            2, 3, 1
    };

    private static final Vector2f[] texs = new Vector2f[] {
            new Vector2f(0,0),
            new Vector2f(0,0.0625f),
            new Vector2f(0.0625f,0),
            new Vector2f(0.0625f,0.0625f),
    };

    private static final Vector3f nback = new Vector3f(0,0,-1);
    private static final Vector3f nfront = new Vector3f(0,0,1);

    private static final Vector3f nright = new Vector3f(1,0,0);
    private static final Vector3f nleft = new Vector3f(-1,0,0);

    private static final Vector3f ntop = new Vector3f(0,1,0);
    private static final Vector3f nbot = new Vector3f(0,-1,0);

    private static final Vector3f[] vback = new Vector3f[] {
            new Vector3f(0, 0, 0),
            new Vector3f(0, 1, 0),
            new Vector3f(1, 0, 0),
            new Vector3f(1, 1, 0),
    };


    private static final Vector3f[] vfront = new Vector3f[] {
            new Vector3f(0, 0, 1),
            new Vector3f(1, 0, 1),
            new Vector3f(0, 1, 1),
            new Vector3f(1, 1, 1),
    };

    private static final Vector3f[] vbot = new Vector3f[] {
            new Vector3f(0, 0, 0),
            new Vector3f(1, 0, 0),
            new Vector3f(0, 0, 1),
            new Vector3f(1, 0, 1),
    };

    private static final Vector3f[] vtop = new Vector3f[] {
            new Vector3f(0, 1, 0),
            new Vector3f(0, 1, 1),
            new Vector3f(1, 1, 0),
            new Vector3f(1, 1, 1),
    };

    private static final Vector3f[] vleft = new Vector3f[] {
            new Vector3f(0, 0, 0),
            new Vector3f(0, 0, 1),
            new Vector3f(0, 1, 0),
            new Vector3f(0, 1, 1),
    };

    private static final Vector3f[] vright = new Vector3f[] {
            new Vector3f(1, 0, 0),
            new Vector3f(1, 1, 0),
            new Vector3f(1, 0, 1),
            new Vector3f(1, 1, 1),
    };

    /**
     * mesh the blocks, by using simple culling
     * @param blocks the blocks to mesh
     * @return the mesh
     */
    public static Mesh mesh(Block[][][] blocks, Vector3f offset) {
        ArrayList<Vector3f> vertsA = new ArrayList<>();
        ArrayList<Vector3f> normsA = new ArrayList<>();
        ArrayList<Integer> indsA = new ArrayList<>();
        ArrayList<Vector2f> texsA = new ArrayList<>();
        ArrayList<Integer> lightsA = new ArrayList<>();
        ArrayList<Integer> aLightsA = new ArrayList<>();

        int indsC = 0;
        for(int i = 0; i < blocks.length; i++) {
            for(int j = 0; j < blocks[i].length; j++) {
                for(int k = 0; k < blocks[i][j].length; k++) {
                    if(((i < blocks.length - 1 && !blocks[i + 1][j][k].isFull()) || i == blocks.length - 1) && blocks[i][j][k].isFull()) {
                        addToArray(new Vector3f(i,j,k).add(offset), vright, vertsA);
                        for(int l = 0; l < 4; l++)
                            normsA.add(nright);
                        indsC ++;

                        Vector2f[] texCopy = new Vector2f[] {
                                new Vector2f(blocks[i][j][k].getX(EnumSide.RIGHT), 0).div(16).add(new Vector2f(texs[1])),
                                new Vector2f(blocks[i][j][k].getX(EnumSide.RIGHT), 0).div(16).add(new Vector2f(texs[0])),
                                new Vector2f(blocks[i][j][k].getX(EnumSide.RIGHT), 0).div(16).add(new Vector2f(texs[3])),
                                new Vector2f(blocks[i][j][k].getX(EnumSide.RIGHT), 0).div(16).add(new Vector2f(texs[2])),
                        };

                        texsA.addAll(Arrays.asList(texCopy));

                        for(int l = 0; l < 4; l++)
                            lightsA.add(blocks[i][j][k].lightLevel[0]);

                        for(int l = 0; l < 4; l++)
                            aLightsA.add(blocks[i][j][k].aLightLevel[0]);
                    }
                    if(((i > 0 && !blocks[i - 1][j][k].isFull()) || i == 0) && blocks[i][j][k].isFull()) {
                        addToArray(new Vector3f(i,j,k).add(offset), vleft, vertsA);
                        for(int l = 0; l < 4; l++)
                            normsA.add(nleft);
                        indsC ++;

                        Vector2f[] texCopy = new Vector2f[] {
                                new Vector2f(blocks[i][j][k].getX(EnumSide.LEFT), 0).div(16).add(new Vector2f(texs[3])),
                                new Vector2f(blocks[i][j][k].getX(EnumSide.LEFT), 0).div(16).add(new Vector2f(texs[1])),
                                new Vector2f(blocks[i][j][k].getX(EnumSide.LEFT), 0).div(16).add(new Vector2f(texs[2])),
                                new Vector2f(blocks[i][j][k].getX(EnumSide.LEFT), 0).div(16).add(new Vector2f(texs[0])),
                        };

                        texsA.addAll(Arrays.asList(texCopy));

                        for(int l = 0; l < 4; l++)
                            lightsA.add(blocks[i][j][k].lightLevel[1]);

                        for(int l = 0; l < 4; l++)
                            aLightsA.add(blocks[i][j][k].aLightLevel[1]);
                    }

                    if(((j < blocks[i].length - 1 && !blocks[i][j + 1][k].isFull()) || j == blocks[i].length - 1) && blocks[i][j][k].isFull()) {
                        addToArray(new Vector3f(i,j,k).add(offset), vtop, vertsA);
                        for(int l = 0; l < 4; l++)
                            normsA.add(ntop);
                        indsC ++;

                        Vector2f[] texCopy = new Vector2f[] {
                                new Vector2f(blocks[i][j][k].getX(EnumSide.TOP), 0).div(16).add(new Vector2f(texs[0])),
                                new Vector2f(blocks[i][j][k].getX(EnumSide.TOP), 0).div(16).add(new Vector2f(texs[1])),
                                new Vector2f(blocks[i][j][k].getX(EnumSide.TOP), 0).div(16).add(new Vector2f(texs[2])),
                                new Vector2f(blocks[i][j][k].getX(EnumSide.TOP), 0).div(16).add(new Vector2f(texs[3])),
                        };

                        texsA.addAll(Arrays.asList(texCopy));

                        for(int l = 0; l < 4; l++)
                            lightsA.add(blocks[i][j][k].lightLevel[2]);

                        for(int l = 0; l < 4; l++)
                            aLightsA.add(blocks[i][j][k].aLightLevel[2]);
                    }
                    if(((j > 0 && !blocks[i][j - 1][k].isFull()) || j == 0) && blocks[i][j][k].isFull()) {
                        addToArray(new Vector3f(i,j,k).add(offset), vbot, vertsA);
                        for(int l = 0; l < 4; l++)
                            normsA.add(nbot);
                        indsC ++;

                        Vector2f[] texCopy = new Vector2f[] {
                                new Vector2f(blocks[i][j][k].getX(EnumSide.BOT), 0).div(16).add(new Vector2f(texs[0])),
                                new Vector2f(blocks[i][j][k].getX(EnumSide.BOT), 0).div(16).add(new Vector2f(texs[1])),
                                new Vector2f(blocks[i][j][k].getX(EnumSide.BOT), 0).div(16).add(new Vector2f(texs[2])),
                                new Vector2f(blocks[i][j][k].getX(EnumSide.BOT), 0).div(16).add(new Vector2f(texs[3])),
                        };

                        texsA.addAll(Arrays.asList(texCopy));

                        for(int l = 0; l < 4; l++)
                            lightsA.add(blocks[i][j][k].lightLevel[3]);

                        for(int l = 0; l < 4; l++)
                            aLightsA.add(blocks[i][j][k].aLightLevel[3]);
                    }

                    if(((k < blocks[i][j].length - 1 && !blocks[i][j][k + 1].isFull()) || k == blocks[i][j].length - 1) && blocks[i][j][k].isFull()) {
                        addToArray(new Vector3f(i,j,k).add(offset), vfront, vertsA);
                        for(int l = 0; l < 4; l++)
                            normsA.add(nfront);
                        indsC ++;

                        Vector2f[] texCopy = new Vector2f[] {
                                new Vector2f(blocks[i][j][k].getX(EnumSide.FRONT), 0).div(16).add(new Vector2f(texs[3])),
                                new Vector2f(blocks[i][j][k].getX(EnumSide.FRONT), 0).div(16).add(new Vector2f(texs[1])),
                                new Vector2f(blocks[i][j][k].getX(EnumSide.FRONT), 0).div(16).add(new Vector2f(texs[2])),
                                new Vector2f(blocks[i][j][k].getX(EnumSide.FRONT), 0).div(16).add(new Vector2f(texs[0])),
                        };

                        texsA.addAll(Arrays.asList(texCopy));

                        for(int l = 0; l < 4; l++)
                            lightsA.add(blocks[i][j][k].lightLevel[4]);

                        for(int l = 0; l < 4; l++)
                            aLightsA.add(blocks[i][j][k].aLightLevel[4]);
                    }
                    if(((k > 0 && !blocks[i][j][k - 1].isFull()) || k == 0) && blocks[i][j][k].isFull()) {
                        addToArray(new Vector3f(i,j,k).add(offset), vback, vertsA);
                        for(int l = 0; l < 4; l++)
                            normsA.add(nback);
                        indsC ++;

                        Vector2f[] texCopy = new Vector2f[] {
                                new Vector2f(blocks[i][j][k].getX(EnumSide.BACK), 0).div(16).add(new Vector2f(texs[3])),
                                new Vector2f(blocks[i][j][k].getX(EnumSide.BACK), 0).div(16).add(new Vector2f(texs[2])),
                                new Vector2f(blocks[i][j][k].getX(EnumSide.BACK), 0).div(16).add(new Vector2f(texs[1])),
                                new Vector2f(blocks[i][j][k].getX(EnumSide.BACK), 0).div(16).add(new Vector2f(texs[0])),
                        };

                        texsA.addAll(Arrays.asList(texCopy));

                        for(int l = 0; l < 4; l++)
                            lightsA.add(blocks[i][j][k].lightLevel[5]);

                        for(int l = 0; l < 4; l++)
                            aLightsA.add(blocks[i][j][k].aLightLevel[5]);
                    }
                }
            }
        }
        for(int i = 0; i < indsC; i++) {
            for(int ind:inds)
                indsA.add(ind + i * 4);
        }

        Vector3f[] vertsArr = new Vector3f[vertsA.size()];
        for(int i = 0; i < vertsA.size(); i++) {
            vertsArr[i] = vertsA.get(i);
        }

        Vector3f[] normsArr = new Vector3f[normsA.size()];
        for(int i = 0; i < normsA.size(); i++) {
            normsArr[i] = normsA.get(i);
        }

        int[] indsArr = new int[indsA.size()];
        for(int i = 0; i < indsA.size(); i++) {
            indsArr[i] = indsA.get(i);
        }

        Vector2f[] texsArr = new Vector2f[texsA.size()];
        for(int i = 0; i < texsA.size(); i++) {
            texsArr[i] = texsA.get(i);
        }

        int[] lightsArr = new int[lightsA.size()];
        for(int i = 0; i < lightsA.size(); i++) {
            lightsArr[i] = lightsA.get(i);
        }

        int[] aLightsArr = new int[aLightsA.size()];
        for(int i = 0; i < aLightsA.size(); i++) {
            aLightsArr[i] = aLightsA.get(i);
        }

        return new Mesh().init(vertsArr, texsArr, normsArr, indsArr, lightsArr, aLightsArr);
    }

    private static void addToArray(Vector3f dis, Vector3f[] verts, ArrayList<Vector3f> array) {
        ArrayList<Vector3f> myVerts = new ArrayList<>();
        for (Vector3f vert : verts) {
            myVerts.add(new Vector3f(vert.x + dis.x, vert.y + dis.y, vert.z + dis.z));
        }
        array.addAll(myVerts);
    }
}
