package systems.kscott.randomspawnplus.spawn;

import lombok.Getter;

public class SpawnRegion {
    @Getter
    int minX;

    @Getter
    int maxX;

    @Getter
    int minZ;

    @Getter
    int maxZ;

    public SpawnRegion(int minX, int maxX, int minZ, int maxZ) {
        this.minX = minX;
        this.maxX = maxX;
        this.minZ = minZ;
        this.maxZ = maxZ;
    }

}
