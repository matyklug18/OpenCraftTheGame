package matyk.engine.physics;

import org.joml.Vector3f;

public class AABB {
    public Vector3f a;
    public Vector3f b;

    public AABB(Vector3f a, Vector3f b) {
        this.a = a;
        this.b = b;
    }

    public boolean collides(AABB other) {
        Vector3f[] points = new Vector3f[] {
                other.a,
                other.a.add(new Vector3f(other.a.x - other.b.x, 0, 0)),
                other.a.add(new Vector3f(other.a.x - other.b.x, 0, other.a.z - other.b.z)),
                other.a.add(new Vector3f(0, 0, other.a.z - other.b.z)),

                other.b,
                other.b.add(new Vector3f(other.b.x - other.a.x, 0, 0)),
                other.b.add(new Vector3f(other.b.x - other.a.x, 0, other.b.z - other.a.z)),
                other.b.add(new Vector3f(0, 0, other.b.z - other.a.z)),
        };
        for(Vector3f p:points) {
            if
            (
                       b.x < p.x
                    && b.y < p.y
                    && b.z < p.z
                    && a.x > p.x
                    && a.y > p.y
                    && a.z > p.z
            )
            {
                return true;
            }
        }
        return false;
    }
}
