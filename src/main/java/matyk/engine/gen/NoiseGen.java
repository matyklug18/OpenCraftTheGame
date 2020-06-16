package matyk.engine.gen;

import matyk.engine.utils.FastNoise;

public class NoiseGen {
    private static FastNoise noise = new FastNoise();

    /**
     * get the normalized height of noise
     * uses simplex noise
     * @param x x of the noise
     * @param y y of the noise
     * @param z z of the noise
     * @param strength strength of the nose
     * @param size size of the noise
     * @param octaves the amount of octaves
     * @param persistence the persistence of the noise
     * @return
     */
    private static double getNormalizedHeightmap(float x, float y, float z, float strength, float size, int octaves, double persistence) {
        double total = 0;
        double maxValue = 0;  // Used for normalizing result to 0.0 - 1.0
        for(int i=0;i<octaves;i++) {
            total += noise.GetSimplex(x * size, y * size, z * size) * strength;

            maxValue += strength;

            strength *= persistence;
            size *= 2;
        }

        return (total/maxValue);
    }


    /**
     * get the heightmap
     * @param x x of the noise
     * @param y y of the noise
     * @param z z of the noise
     * @param strength strength of the nose
     * @param size size of the noise
     * @param octaves the amount of octaves
     * @param persistence the persistence of the noise
     * @param height the height multiplier of the noise
     * @return the height at that location
     */
    public static double getHeightmap(float x, float y, float z, float strength, float size, int octaves, double persistence, float height) {
        return getNormalizedHeightmap(x, y, z, strength, size, octaves, persistence) * strength + height * strength;
    }
}
