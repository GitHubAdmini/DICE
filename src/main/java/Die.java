import java.util.ArrayList;
import java.util.Scanner;

public class Die {
    int value;

    public static void main (String[] args) {
        Die die6 = new Die (6, 1,1,1,1,1,2);
        try {
            die6.roll ( );
        } catch (Exception e) {
            e.printStackTrace ( );
        }
        System.out.println ("Dice value: " + die6.value);

        // Sets the probabilities of die6
        die6.setProbabilities (new Number[]{3, 1, 2 , 5, 1, 1});
        try {
            die6.roll ( );
        } catch (Exception e) {
            e.printStackTrace ( );
        }
        System.out.println ("Weighted Dice value: " + die6.value);

        // Create a die with 20 sides using the DiceFactory class
        Die die20 = DiceFactory.makeDice (20);
        try {
            die20.roll ( );
        } catch (Exception e) {
            e.printStackTrace ( );
        }
        System.out.println ("Factory Dice value: " + die20.value);

        // get user input for the dice rolls
        System.out.println ( );
        System.out.print ("Type 'Roll' to roll again: \n");
        Scanner input = new Scanner(System.in);
        String roll_again = input.nextLine();
        System.out.println ("-------------------------------------------------------");
        main(args); //re-run
    }

    //Create a Die with variable sides
    public static class DiceFactory {
        public static Die makeDice(int dieSides){
            return new Die (dieSides);
        }
    }

    public void setProbabilities(Number[] newProbs){
        probabilities = newProbs;
    }
    //initialize the variables
    final int sides;
    Number[] probabilities;

    //Probability calculations constructor
    Die (int diceSides,
         Number... diceProbabilities) {
        this.sides = diceSides;
        this.probabilities = diceProbabilities;
    }

    // sides must be not be less than or equal to 0
    public void roll () throws Exception{
        if (sides <= 0){
            throw new Exception("sides has to be an integer greater than one");
        }
        if (sides == probabilities.length) {
            int addProb = 0;
            {
                int i = 0, probabilityLength = probabilities.length;
                while (i < probabilityLength) {
                    Number number = probabilities[i];
                    if (number.intValue ( ) == 0 || number.doubleValue ( ) % number.intValue ( ) == 0) {
                        addProb = addProb + number.intValue ( );
                    } else {
                        throw new Exception ("only integer values allowed");
                    }
                    if (addProb == 0) throw new Exception ("probability sum must be greater than 0");
                    i++;
                }
            }

/* probFraction is Probability fraction
   addProb is sum of the probabilities

*/
            ArrayList<Double> probFraction = new ArrayList<>();
            int i=0;
            if (i < probabilities.length)
                do {
                    int probableValue = probabilities[i].intValue ( );
                    if (probableValue < 0) throw new Exception ("negative probabilities not allowed");
                    if (i > 0) {
                        probFraction.add (((double) probableValue) / addProb + probFraction.get (i - 1));
                    } else {
                        probFraction.add (((double) probableValue) / addProb);
                    }
                    i++;
                } while (i < probabilities.length);

            double randoms = Math.random();
            ArrayList<Integer> randomValues = new ArrayList<>();
            {
                int x = 1;
                while (x <= sides) {
                    randomValues.add (x);
                    x++;
                }
            }
            int x = 0;
            while (x < probFraction.size()) {
                if (randoms < probFraction.get (x)) {
                    value = randomValues.get (x);
                }
                x++;
            }
        }
        //Random factory dice value  * the value on the sides +1
        value = (int) (Math.random ( ) * sides + 1);
    }

}