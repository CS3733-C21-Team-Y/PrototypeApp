package edu.wpi.cs3733.c21.teamY.googleMaps;

import com.dlsc.gmapsfx.javascript.object.GoogleMap;
import com.dlsc.gmapsfx.shapes.FillableMapShapeOptions;

public class GroundOverlayOptions extends FillableMapShapeOptions<GroundOverlayOptions> {
  private Boolean clickable;
  private GoogleMap map;
  private Double opacity;

  public GroundOverlayOptions(Boolean clickable, GoogleMap map, Double opacity) {
    this.clickable = clickable;
    this.map = map;
    this.opacity = opacity;
  }

  @Override
  protected GroundOverlayOptions getMe() {
    return this;
  }
}
