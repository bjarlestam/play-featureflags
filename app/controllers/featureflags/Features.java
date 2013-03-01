package controllers.featureflags;

import models.featureflags.Feature;
import play.mvc.Controller;

import java.util.List;

public class Features extends Controller {

	public static void adminPage() {
		List<Feature> features = Feature.findAll();
		render(features);
	}

	public static void updateFeatures(List<Feature> features) {
		for(Feature feature: features) {
			feature.update();
		}
		adminPage();
	}

	public static void delete(Long id) {
		Feature feature = Feature.findById(id);
		feature.delete();
	}
}
