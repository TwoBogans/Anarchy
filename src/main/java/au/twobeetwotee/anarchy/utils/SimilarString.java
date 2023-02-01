package au.twobeetwotee.anarchy.utils;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SimilarString {
    private static final float THRESHOLD = (float) 0.75;

    private final String str;
    private final Map<Character, Integer> strMap;

    public SimilarString(String str) { //java.lang.String is final...
      this.str = str;
      this.strMap = this.generateCharMap(str);
    }

    @Override
    public String toString(){
      return this.str;
    }

    private Map<Character, Integer> generateCharMap(String str){
      Map<Character, Integer> map = new HashMap<>();
      Integer currentChar;
      for(char c: str.toCharArray()) {
        currentChar = map.get(c);
        if(currentChar == null) {
          map.put(c, 1);
        } else {
          map.put(c, currentChar+1);
        }
      }

      return map;
    }

    public boolean isSimilar(List<String> stringList) {
        AtomicBoolean b = new AtomicBoolean(false);
        for (String s : stringList) {
            b.set(isSimilar(s));
        }
        return b.get();
    }

    public boolean isSimilar(String compareStr){
      Map<Character, Integer> compareStrMap = this.generateCharMap(compareStr);
      Set<Character> charSet = compareStrMap.keySet();
      int similarChars = 0;
      int totalStrChars = this.str.length();
      float thisThreshold;

      if(totalStrChars < compareStrMap.size()){
        totalStrChars = compareStr.length();
      }

      Iterator<Character> it = charSet.iterator();
      char currentChar;
      Integer currentCountStrMap;
      Integer currentCountCompareStrMap;
      while (it.hasNext()){
        currentChar = it.next();
        currentCountStrMap = strMap.get(currentChar);
        if(currentCountStrMap != null){
          currentCountCompareStrMap = compareStrMap.get(currentChar);
          if (currentCountCompareStrMap >= currentCountStrMap){
            similarChars += currentCountStrMap;
          } else {
            similarChars += currentCountCompareStrMap;
          }
        }
      }

      thisThreshold = ((float) similarChars)/((float) totalStrChars);
      if (thisThreshold > THRESHOLD) {
          Logger.getLogger(SimilarString.class.getName()).log(Level.INFO, "similarChars: {0}, totalStrChars: {1}, thisThreshold: {2}", new Object[]{similarChars, totalStrChars, thisThreshold});
          return true;
      }
      return false;
    }
}