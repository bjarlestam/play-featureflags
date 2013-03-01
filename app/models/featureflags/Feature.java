package models.featureflags;

import play.db.jpa.Model;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class Feature extends Model {

    @Column(unique = true, nullable = false)
    public String name;
    public boolean enabled;

    public Feature(String name) {
        this(name, false);
    }

    public Feature(String name, boolean enabled) {
        this.name = name;
        this.enabled = enabled;
    }

    private Feature enable() {
        this.enabled = true;
        return this;
    }

    private Feature disable() {
        this.enabled = false;
        return this;
    }

    public static boolean isEnabled(String name) {
        return findByNameOrCreate(name).enabled;
    }


    public static void enable(String name) {
        findByNameOrCreate(name).enable().save();
    }

    public static void disable(String name) {
        findByNameOrCreate(name).disable().save();
    }

    private static Feature findByNameOrCreate(String name) {
        Feature f = find("byName", name).first();
        if (f == null) {
            return new Feature(name).save();
        } else {
            return f;
        }
    }


    @Override
    public String toString() {
        return name;
    }

}
