package edu.wpi.cs3733.c21.teamY.dataops;

import java.util.Random;

public class GeneratePassword {
  private static final String[] strs = {
    "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s",
    "t", "u", "v", "w", "x", "y", "z", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "!", "@",
    "#", "$", "-", "_", "+", "~"
  };

  public static String generatePassword() {
    Random random = new Random();
    String password = "";

    for (int i = 0; i < 12; i++) {
      int index = random.nextInt(strs.length);
      String str = strs[index];
      password += str;

      int angle = random.nextInt(91) - 45;
      double rotation = angle * Math.PI / 180;
    }
    return password;
  }
}
