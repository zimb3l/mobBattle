package org.dlds.mobbattle;

import java.util.List;
import java.util.UUID;

public class Category {

    private UUID id;
    private int categoryNumber;
    private int points;
    private List<MobCreature> mobs;

    public Category(int categoryNumber, int points, List<MobCreature> mobs){
        this.id = UUID.randomUUID();
        this.categoryNumber = categoryNumber;
        this.points = points;
        this.mobs = mobs;
    }

    public UUID getId() {
        return id;
    }

    public int getCategoryNumber() {
        return categoryNumber;
    }

    public int getPoints() {
        return points;
    }

    public List<MobCreature> getMobs() {
        return mobs;
    }
}
