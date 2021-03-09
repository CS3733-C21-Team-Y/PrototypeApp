package edu.wpi.cs3733.c21.teamY.dataops;

import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.ComboBox;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import lombok.SneakyThrows;
import me.xdrop.fuzzywuzzy.FuzzySearch;
import me.xdrop.fuzzywuzzy.model.ExtractedResult;

public class FuzzySearchComboBoxListener<T> implements EventHandler<KeyEvent> {

  private ComboBox comboBox;
  private StringBuilder sb;
  private ObservableList<T> data;
  private boolean moveCaretToPos = false;
  private int caretPos;

  public FuzzySearchComboBoxListener(final ComboBox comboBox) {
    this.comboBox = comboBox;
    sb = new StringBuilder();
    data = comboBox.getItems();

    this.comboBox.setEditable(true);
    this.comboBox.setOnKeyPressed(
        new EventHandler<KeyEvent>() {

          @Override
          public void handle(KeyEvent t) {
            comboBox.hide();
          }
        });
    this.comboBox.setOnKeyReleased(FuzzySearchComboBoxListener.this);
  }

  @SneakyThrows
  @Override
  public void handle(KeyEvent event) {

    if (event.getCode() == KeyCode.UP) {
      caretPos = -1;
      moveCaret(comboBox.getEditor().getText().length());
      return;
    } else if (event.getCode() == KeyCode.DOWN) {
      if (!comboBox.isShowing()) {
        comboBox.show();
      }
      caretPos = -1;
      moveCaret(comboBox.getEditor().getText().length());
      return;
    } else if (event.getCode() == KeyCode.BACK_SPACE) {
      moveCaretToPos = true;
      caretPos = comboBox.getEditor().getCaretPosition();
    } else if (event.getCode() == KeyCode.DELETE) {
      moveCaretToPos = true;
      caretPos = comboBox.getEditor().getCaretPosition();
    }

    if (event.getCode() == KeyCode.RIGHT
        || event.getCode() == KeyCode.LEFT
        || event.isControlDown()
        || event.getCode() == KeyCode.HOME
        || event.getCode() == KeyCode.END
        || event.getCode() == KeyCode.TAB) {
      return;
    }

    ObservableList list = FXCollections.observableArrayList();
    List<String> dataList = (List<String>) data.subList(0, data.size());

    List<ExtractedResult> resultList =
        FuzzySearch.extractSorted(
            FuzzySearchComboBoxListener.this.comboBox.getEditor().getText(), dataList);
    for (int i = 0; i < resultList.size(); i++) {
      ExtractedResult result = resultList.get(i);
      String toAdd = result.getString();
      list.add(toAdd);
    }

    String t = comboBox.getEditor().getText();
    comboBox.setItems(list);
    comboBox.getEditor().setText(t);
    if (!moveCaretToPos) {
      caretPos = -1;
    }
    moveCaret(t.length());
    if (!list.isEmpty()) {
      comboBox.show();
    }
  }

  private void moveCaret(int textLength) {
    if (caretPos == -1) {
      comboBox.getEditor().positionCaret(textLength);
    } else {
      comboBox.getEditor().positionCaret(caretPos);
    }
    moveCaretToPos = false;
  }
}
