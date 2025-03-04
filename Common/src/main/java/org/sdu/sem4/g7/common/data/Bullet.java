package org.sdu.sem4.g7.common.data;

/*
 * Shootable and renderable bullet
 */
public class Bullet extends Entity {
    
    /*
     * Defines how many health an entity loses upon collision
     */
    private int damage;

    public Bullet() {
        super();
    }

    public Bullet(int damage) {
        super();
        this.damage = damage;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }


}