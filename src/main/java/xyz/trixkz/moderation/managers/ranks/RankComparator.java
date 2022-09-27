package xyz.trixkz.moderation.managers.ranks;

import java.util.Comparator;

public class RankComparator implements Comparator<Rank> {

    public int compare(Rank rankOne, Rank rankTwo) {
        return rankTwo.getWeight() - rankOne.getWeight();
    }
}
