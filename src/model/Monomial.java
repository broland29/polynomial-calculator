/** Everything related to Monomials as objects
 *   - purpose : build up small parts of Polynomial
 *   - ofter used to reduce Polynomial problems on Monomial level
 */

package model;

public class Monomial {
    //attributes are set as final, since they are never modified
    private final int coefficient;    //integer, containing sign as well
    private final int exponent;       //non-negative and integer, as specified in problem statement

    //simple constructor, used after extracting necessary data about a monomial
    public Monomial(int coefficient, int exponent) {
        this.coefficient = coefficient;
        this.exponent = exponent;
    }

    //prints a monomial in the form [coefficient][x][^exponent]
    public String niceForm(){

        //special cases - we get rid of them, if they occur, skip the rest of method
        if (coefficient == 0)
            return "0";

        if (exponent == 0)
            if (coefficient == 1)           //output is just "1", otherwise coefficient 1 is not shown
                return "1";
            else if (coefficient == -1)     //output is just "-1", otherwise coefficient -1 would be just "-"
                return "-1";

        //more general cases
        String output = "";

        //for coefficient; note that coefficient 0 is considered bad input, hence it is not dealt with
        if (this.coefficient == -1)
            output = output.concat("-");
        else if (this.coefficient != 1)
            output = output.concat(Integer.toString(this.coefficient));

        //for exponent
        if (this.exponent == 1)
            output = output.concat("x");
        else if (this.exponent != 0)
            output = output.concat("x^" + this.exponent);

        return output;
    }

    //checks if monomial's value is 0
    public boolean isNull(){
        return coefficient == 0;
    }

    //getters
    public int getExponent() {
        return exponent;
    }

    public int getCoefficient() {
        return coefficient;
    }
}
