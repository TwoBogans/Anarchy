package au.twobeetwotee.anarchy.utils;

import java.util.Collection;
import java.util.TreeSet;

public class LimitedSortedSet<E> extends TreeSet<E> {
    private final int maxSize;

    public LimitedSortedSet(int maxSize) {
      this.maxSize = maxSize;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
      boolean added = super.addAll(c);
      if(size() > maxSize) {
        E firstToRemove = (E) toArray()[maxSize];
        removeAll(tailSet(firstToRemove));
      }
      return added;
    }

    @Override
    public boolean add(E o) {
      boolean added =  super.add(o);
      if(size() > maxSize) {
        E firstToRemove = (E) toArray()[maxSize];
        removeAll(tailSet(firstToRemove));
      }
      return added;
    }
}