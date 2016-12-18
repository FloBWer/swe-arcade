package handler;

import objects.StatRow;

import java.util.HashMap;

/**
 * Created by Shakreo on 18.12.2016.
 */
public class StatHandler {
  private HashMap stats;

  public StatHandler(String gameList) {
    stats = new HashMap<String, StatRow>();
  }
}
