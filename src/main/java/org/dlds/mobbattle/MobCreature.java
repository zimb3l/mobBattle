package org.dlds.mobbattle;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

public class MobCreature {

    private EntityType entityType;
    private String mobHead;
    private boolean dead = false;

    public MobCreature(EntityType entityType, String mobHead){
        this.entityType = entityType;
        this.mobHead = mobHead;
    }

    public void killedMob(){
        this.dead = true;
    }

    public EntityType getEntityType() {
        return entityType;
    }

    public String getMobHead() {
        return mobHead;
    }

    public boolean isDead() {
        return dead;
    }
}