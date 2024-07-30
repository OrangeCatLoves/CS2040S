///////////////////////////////////
// This is the main shift register class.
// Notice that it implements the ILFShiftRegister interface.
// You will need to fill in the functionality.
///////////////////////////////////

import java.sql.SQLOutput;
import java.util.Arrays;

/**
 * class ShiftRegister
 * @author
 * Description: implements the ILFShiftRegister interface.
 */
public class ShiftRegister implements ILFShiftRegister {
    ///////////////////////////////////
    // Create your class variables here
    ///////////////////////////////////
    // TODO:
    private int size;
    private int tap;
    private static int[] shiftRegister;

    ///////////////////////////////////
    // Create your constructor here:
    ///////////////////////////////////
    ShiftRegister(int size, int tap) {
        // TODO:
        this.size = size;
        this.tap = tap;
        this.shiftRegister = new int[size];
    }

    ///////////////////////////////////
    // Create your class methods here:
    ///////////////////////////////////
    /**
     * setSeed
     * @param seed
     * Description: Populating the initialised shiftRegister array with the binary values inside the seed
     */
    @Override
    public void setSeed(int[] seed) {
        // TODO:
        for (int i = 0; i < seed.length; i++) {
            if (shiftRegister[i] != 0 && shiftRegister[i] != 1 ) {

                throw new RuntimeException(Integer.toString(shiftRegister[i]));
            } else {
                shiftRegister[i] = seed[i];
            }
        }
    }

    /**
     * shift
     * @return
     * Description: Calculating the feedbackBit first using XOR function. Followed by a right shift
     * and lastly, assigning feedbackBit to the least significant bit on the extreme left
     */
    @Override
    // Shift command handles right shifts
    public int shift() {
        // TODO:
        int feedbackBit = shiftRegister[tap] ^
                shiftRegister[shiftRegister.length - 1];
        //System.out.println(feedbackBit);


        for (int j = shiftRegister.length - 1; j > 0; j--) {
            shiftRegister[j] = shiftRegister[j - 1];
        }
        shiftRegister[0] = feedbackBit;
        //System.out.println(Arrays.toString());
        return feedbackBit;
    }
    /**
     * generate
     * @param k
     * @return
     * Description:
     */
    @Override
    public int generate(int k) {
        // TODO:
        int result = 0;
        for (int i = 0; i<k; i++){
            result += shift() * Math.pow(2, k-i-1);
        }
        return result;
    }

    /**
     * Returns the integer representation for a binary int array.
     * @param array
     * @return
     */
    private int toDecimal(int[] array) {
        // TODO:
        return 0;
    }
}
