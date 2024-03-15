package ca.mcgill.ecse321.SportPlus.service.utilities;

import java.sql.Date;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

public class HelperMethods {

  public static String PasswordCheck(String password) {
    if (password.isEmpty()) {
      return "Password field cannot be empty.";
    } else if (password.length() < 5) {
      return "Password must contain at least five characters.";
    } else if (password.equals(password.toLowerCase())) {
      return "Password must contain an Uppercase letter.";
    }
    return "";
  }

  public static String ClientEmailCheck(String email) {
    if (email.isEmpty()) {
      return "Email field cannot be empty.";
    } else if (email.contains(" ")) {
      return "Email cannot contain any spaces.";
    } else if (email.endsWith("@sportplus.com")) {
      return "Email cannot contain Sportplus domain";
    } else {
      int count = 0;
      boolean valid = true;
      String allowedCharacters = "abcdefghijklmnopqrstuvwxyz._-@1234567890";
      for (char c : email.toLowerCase().toCharArray()) {
        if ('@' == c) {
          count++;
        }
        if (allowedCharacters.indexOf(c) == -1) {
          valid = false;
          break;
        }
      }
      if (count != 1 || !(email.endsWith(".com") || email.endsWith(".ca")) || !valid || email.startsWith("@")
          || email.contains(".@") || email.contains("@.")) {
        return "Invalid email";
      }
    }
    return "";
  }

  public static String InstructorEmailCheck(String email) {
    if (email.isEmpty()) {
      return "Email field cannot be empty.";
    } else if (email.contains(" ")) {
      return "Email cannot contain any spaces.";
    } else if (!email.endsWith("@sportplus.com")) {
      return "Email must contain Sportplus domain";
    } else {
      int count = 0;
      boolean valid = true;
      String allowedCharacters = "abcdefghijklmnopqrstuvwxyz._-@1234567890";
      for (char c : email.toLowerCase().toCharArray()) {
        if ('@' == c) {
          count++;
        }
        if (allowedCharacters.indexOf(c) == -1) {
          valid = false;
          break;
        }
      }
      if (count != 1 || !(email.endsWith(".com") || email.endsWith(".ca")) || !valid || email.startsWith("@")
          || email.contains(".@") || email.contains("@.")) {
        return "Invalid email";
      }
    }
    return "";
  }

  public static boolean hasDatePassed(Date inputDate) {
    Date currentDate = new Date(System.currentTimeMillis());
    return currentDate.after(inputDate);
  }

  public static <T> List<T> toList(Iterable<T> iterable) {
    List<T> resultList = new ArrayList<T>();
    for (T t : iterable) {
      resultList.add(t);
    }
    return resultList;
  }

}
