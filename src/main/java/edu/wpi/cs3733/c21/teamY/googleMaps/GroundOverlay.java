package edu.wpi.cs3733.c21.teamY.googleMaps;

import com.dlsc.gmapsfx.javascript.object.LatLongBounds;
import com.dlsc.gmapsfx.javascript.object.MapShape;

public class GroundOverlay extends MapShape {
  String url;
  LatLongBounds bounds;

  public GroundOverlay(String url, LatLongBounds bounds, GroundOverlayOptions opts) {
    super(MoreGMapObjectTypes.GROUND_OVERLAY, opts);
    this.url = url;
    this.bounds = bounds;
  }

  // TODO: getBounds

  // TODO: getMap

  // TODO: getOpacity

  // TODO: getUrl

  // TODO: setMap

  // TODO: setOpacity

}
