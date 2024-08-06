/*public class Solution {
    // TODO: Include your data structures here

    public Solution() {
        // TODO: Construct/Initialise your data structures here
    }

    void add(long energy, long value) {
        // TODO: Implement your insertion operation here
    }

    long query(long remainingEnergy) {
        // TODO: Implement your query operation here

        return 0L;
    }

}*/
import java.util.*;

public class Solution {


    private static class Quest implements Comparable<Quest> {
        long energy;
        long value;
        public Quest(long energy, long value) {
            this.energy = energy;
            this.value = value;
        }
        @Override
        public String toString() {
            return "ENERGY: " + this.energy + ", REWARD: " + value;
        }
        @Override
        public int compareTo(Quest other) {
            Long energy = this.energy;
            Long value = this.value;
            if (energy == other.energy) {
                return value.compareTo(other.value);
            }
            return energy.compareTo(other.energy);
        }
    }

    private TreeSet<Quest> tree;
    private ArrayList<Quest> quests;

    public Solution() {
        this.tree = new TreeSet<>();
        this.quests = new ArrayList<>();
    }

    void add(long energy, long value) {
        Quest quest = new Quest(energy, value);
        tree.add(quest);
        quests.add(quest);
    }

    long query(long remainingEnergy) {
        long totalGoldReward = 0;
        while (remainingEnergy > 0) {
            Quest quest = tree.lower(new Quest(remainingEnergy, Long.MIN_VALUE));

            if (quest == null) break;

            totalGoldReward += quest.value;
            remainingEnergy -= quest.energy;
            tree.remove(quest);
        }
        return totalGoldReward;
    }

    long naiveQuery(long remainingEnergy) {
        long totalGoldReward = 0;
        Collections.sort(quests);

        while (remainingEnergy > 0) {
            if (quests.size() == 0) break;
            if (quests.get(0).energy > remainingEnergy) break;
            for (int i = quests.size() - 1; i >= 0; i--) {
                Quest quest = quests.get(i);
                if (quest.energy <= remainingEnergy) {
                    totalGoldReward += quest.value;
                    remainingEnergy -= quest.energy;
                    quests.remove(i);
                    break;
                }
            }
        }

        return totalGoldReward;
    }


    public static void main(String[] args) {
        /**
         * 1. add 8 10
         * 2. add 3 25
         * 3. add 5 6
         * 4. query 7 (returns 6) 5. query 7 (returns 25)
         * 6. add 1 9
         * 7. add 2 13
         * 8. query 20 (returns 32) 9. query 1 (returns 0)
         */
        Solution solution = new Solution();

        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        for (int i = 0; i < n; i++) {
            String str = sc.next();
            if (str.equals("query")) {
                long remainingEnergy = sc.nextLong();
                System.out.println(solution.query(remainingEnergy));
            } else if (str.equals("add")) {
                long energy = sc.nextLong(), value = sc.nextLong();
                solution.add(energy, value);
            }
        }
    }

}
