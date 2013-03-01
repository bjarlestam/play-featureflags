package models.featureflags;

import play.Play;
import play.cache.Cache;
import play.db.jpa.Model;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.lang.Boolean;
import java.lang.String;

@Entity
public class Feature extends Model {

    @Column(unique = true, nullable = false)
    public String name;
    public boolean enabled;

    public Feature(String name) {
        this.name = name;
        this.enabled = Play.mode.isDev();
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
			String cacheKey = "featureflags_feature_" + name;
			Boolean enabled = Cache.get(cacheKey, Boolean.class);
			if(enabled == null) {
				enabled = findByNameOrCreate(name).enabled;
				Cache.set(cacheKey, enabled, random(5, 11, "s")); //TODO make cache time configurable
			}
			return enabled;
    }

		public void update() {
			Feature f = find("byName", name).first();
			f.enabled = enabled;
			f.save();
		}

    public static Feature enable(String name) {
        return findByNameOrCreate(name).enable().save();
    }

    public static Feature disable(String name) {
        return findByNameOrCreate(name).disable().save();
    }

    private static Feature findByNameOrCreate(String name) {
        Feature f = find("byName", name).first();
        if (f == null) {
            return new Feature(name).save();
        } else {
            return f;
        }
    }

	private static String random(int min, int max, String quantity) {
		int range = max - min;
		int random = min + (int)(Math.floor(Math.random() * range) + 1);
		return String.valueOf(random) + quantity;
	}

    @Override
    public String toString() {
        return name;
    }

}
