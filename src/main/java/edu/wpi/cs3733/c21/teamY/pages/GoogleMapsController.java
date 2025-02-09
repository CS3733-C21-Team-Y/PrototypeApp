package edu.wpi.cs3733.c21.teamY.pages;

import com.dlsc.gmapsfx.GoogleMapView;
import com.dlsc.gmapsfx.MapComponentInitializedListener;
import com.dlsc.gmapsfx.javascript.object.*;
import com.dlsc.gmapsfx.service.directions.*;
import com.dlsc.gmapsfx.shapes.Rectangle;
import com.dlsc.gmapsfx.shapes.RectangleOptions;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javax.swing.*;

public class GoogleMapsController extends SubPage
    implements Initializable, MapComponentInitializedListener, DirectionsServiceCallback {

  protected DirectionsService directionsService;
  protected DirectionsPane directionsPane;

  protected StringProperty from = new SimpleStringProperty();
  protected DirectionsRenderer directionsRenderer = null;

  @FXML protected GoogleMapView mapView;
  @FXML protected TextField fromTextField;
  @FXML private Button arrivedButton;

  @FXML
  private void textFieldAction(ActionEvent event) {
    if (directionsRenderer != null) {
      directionsRenderer.clearDirections();
      directionsRenderer
          .getJSObject()
          .eval(directionsRenderer.getVariableName() + ".setPanel(null);");
    }
    DirectionsRequest request =
        new DirectionsRequest(from.get(), "42.30173, -71.12738", TravelModes.DRIVING, false);
    directionsRenderer = new DirectionsRenderer(true, mapView.getMap(), directionsPane);
    directionsService.getRoute(request, this, directionsRenderer);
  }

  @FXML
  private void clearDirections(ActionEvent event) {
    if (directionsRenderer != null) {
      directionsRenderer.clearDirections();
      fromTextField.setText("");
      mapView.getMap().hideDirectionsPane();
      directionsRenderer
          .getJSObject()
          .eval(directionsRenderer.getVariableName() + ".setPanel(null);");
    }
  }

  @FXML
  private void sendToPage(ActionEvent event) {
    if (parent.isDesktop) {
      parent.loadRightSubPage("NavigationMap.fxml");
      parent.loadCenterSubPage("PathfindingPage.fxml");
    } else {
      parent.loadCenterSubPage("NavigationMap.fxml");
      parent.loadRightSubPage("MobilePathfindingPage.fxml");
    }
  }

  @Override
  public void directionsReceived(DirectionsResult results, DirectionStatus status) {
    System.out.println("Directions Received");
    //    System.out.println(results.getRoutes().get(0).getLegs().get(0).getSteps());
    //    System.out.println("Did we get it??");
    //    List<DirectionsLeg> legs = route.getLegs();
    //    List<DirectionsSteps> allSteps = null;
    if (parent.isDesktop) {
      mapView.getMap().showDirectionsPane();
    }
    System.out.println("Done Initializing");
    //    for (DirectionsLeg leg : legs) {
    //      for (DirectionsSteps step : leg.getSteps()) {
    //        allSteps.add(step);
    //      }
    //    }
    //    System.out.println("Done Parsing Legs");
    //    List<String> instructions = null;
    //    for (DirectionsSteps step : allSteps) {
    //      instructions.add(step.getInstructions());
    //    }
    //    System.out.println("Done Parsing instructions");
    //    for (String instruction : instructions) {
    //      System.out.println(instruction);
    //    }
    //    System.out.println("Done parsing directions");
  }

  @Override
  public void initialize(URL url, ResourceBundle rb) {
    mapView.addMapInitializedListener(this);
    mapView.setKey("AIzaSyCig6oYQaLjCSUUSL2T-eRIRLYfv_NeSMo");
    from.bindBidirectional(fromTextField.textProperty());
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
    if (!parent.isDesktop) {
      fromTextField.setPrefWidth(150.0);
    }
    mapView.createMap(options, false);
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

  //  public void toggleMapDirections() {
  //    GoogleMap gMap = mapView.getMap();
  //    if (directionsOpen) {
  //      directionsOpen = false;
  //    } else {
  //      gMap.showDirectionsPane();
  //
  //      directionsOpen = true;
  //    }
  //  }
}
