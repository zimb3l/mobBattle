package org.dlds.mobbattle.objects;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

public class MobCreature {

    private final EntityType entityType;
    private final String mobHead;
    private final String name;
    private boolean dead = false;

    public MobCreature(String name, EntityType entityType, String mobHead) {
        this.name = name;
        this.entityType = entityType;
        this.mobHead = mobHead;
    }

    public void killedMob() {
        this.dead = true;
    }

    public EntityType getEntityType() {
        return entityType;
    }

    public String getMobHead() {
        return mobHead;
    }

    public String getName() {
        return name;
    }

    public boolean isDead() {
        return dead;
    }
}