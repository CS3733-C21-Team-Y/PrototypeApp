package edu.wpi.cs3733.c21.teamY.pages;

import com.dlsc.gmapsfx.GoogleMapView;
import com.dlsc.gmapsfx.MapComponentInitializedListener;
import com.dlsc.gmapsfx.javascript.object.*;
import com.dlsc.gmapsfx.service.directions.DirectionStatus;
import com.dlsc.gmapsfx.service.directions.DirectionsRenderer;
import com.dlsc.gmapsfx.service.directions.DirectionsRequest;
import com.dlsc.gmapsfx.service.directions.DirectionsResult;
import com.dlsc.gmapsfx.service.directions.DirectionsService;
import com.dlsc.gmapsfx.service.directions.DirectionsServiceCallback;
import com.dlsc.gmapsfx.service.directions.TravelModes;
import com.dlsc.gmapsfx.shapes.Rectangle;
import com.dlsc.gmapsfx.shapes.RectangleOptions;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;

public class GoogleMapsController extends RightPage
    implements Initializable, MapComponentInitializedListener, DirectionsServiceCallback {

  protected DirectionsService directionsService;
  protected DirectionsPane directionsPane;

  protected StringProperty from = new SimpleStringProperty();
  protected StringProperty to = new SimpleStringProperty();
  protected DirectionsRenderer directionsRenderer = null;

  @FXML protected GoogleMapView mapView;

  //  @FXML protected TextField fromTextField; //Uncomment to enable Google Maps Directions 1/4
  //  @FXML protected TextField toTextField; //Uncomment to enable Google Maps Directions 2/4

  @FXML
  private void toTextFieldAction(ActionEvent event) {
    DirectionsRequest request =
        new DirectionsRequest(from.get(), to.get(), TravelModes.DRIVING, true);
    directionsRenderer = new DirectionsRenderer(true, mapView.getMap(), directionsPane);
    directionsService.getRoute(request, this, directionsRenderer);
  }

  @FXML
  private void clearDirections(ActionEvent event) {
    directionsRenderer.clearDirections();
  }

  @Override
  public void directionsReceived(DirectionsResult results, DirectionStatus status) {}

  @Override
  public void initialize(URL url, ResourceBundle rb) {
    mapView.addMapInitializedListener(this);
    mapView.setKey("AIzaSyCig6oYQaLjCSUUSL2T-eRIRLYfv_NeSMo");
    //    to.bindBidirectional(toTextField.textProperty()); //Uncomment to enable Google Maps Directions 3/4
    //    from.bindBidirectional(fromTextField.textProperty()); //Uncomment to enable Google Maps Directions 4/4
  }

  @Override
  public void mapInitialized() {
    MapOptions options = new MapOptions();
    options
        .center(new LatLong(42.30130, -71.12800))
        .zoomControl(true)
        .zoom(18)
        .overviewMapControl(false)
        .mapType(MapTypeIdEnum.ROADMAP);
    GoogleMap map = mapView.createMap(options);
    map.setHeading(50.0);
    //    addRectToMap(map);
    directionsService = new DirectionsService();
    directionsPane = mapView.getDirec();
  }

  /** ################# Helper Functions ################# */

  /**
   * Example: Adds a blue rectangle to the Google Map View
   *
   * @param map
   */
  public void addRectToMap(GoogleMap map) {
    LatLong NE = new LatLong(42.30132, -71.12820);
    LatLong SW = new LatLong(42.30120, -71.12800);
    LatLongBounds bounds = new LatLongBounds(NE, SW);
    Image parking = new Image("/edu/wpi/cs3733/c21/teamY/images/FaulknerCampusIT2.png");
    RectangleOptions options = new RectangleOptions();
    options.fillColor("Blue");
    Rectangle rect = new Rectangle(options);
    rect.setBounds(bounds);
    map.addMapShape(rect);
  }

  /**
   * Example: Adds a red balloon marker to the Google Map View
   *
   * @param map
   */
  public void addMarkerToMap(GoogleMap map) {
    LatLong NE = new LatLong(42.30132, -71.12820);
    MarkerOptions markerOptions = new MarkerOptions();
    markerOptions.position(NE);
    markerOptions.visible(Boolean.TRUE);
    markerOptions.title("test");
    Marker marker = new Marker(markerOptions);
    map.addMarker(marker);
  }
}
