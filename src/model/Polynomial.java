/** Everything related to Polynomials as objects
 *   - purpose : main element of each operation
 *   - useful helper methods were added during implementation
 */

package model;

import java.util.ArrayList;

public class Polynomial {
    private final ArrayList<Monomial> monomials;


    //overridden default constructor, since we need to allocate space for the list of monomials
    public Polynomial(){
        this.monomials = new ArrayList<>();
    }


    //copy constructor - for division
    public Polynomial(Polynomial oldPolynomial){
        monomials = oldPolynomial.getMonomials();
    }


    //getter
    public ArrayList<Monomial> getMonomials() {
        return monomials;
    }


    //adds a monomial to the list of monomials
    public void add(Monomial monomial){
        if (monomial.isNull())
            return;
        monomials.add(monomial);
    }


    //prints the polynomial in an appropriate form - uses Monomial's niceForm
    public String niceForm(){

        //"empty" Polynomials still need to be processed in some cases
        if (monomials.isEmpty())
            return "0";

        boolean first = true;
        String output = "";

        for (Monomial monomial : monomials){
            if (!first && monomial.getCoefficient() > 0)    //plus sign needs to be explicitly printed, if needed
                output = output.concat("+");

            output = output.concat(monomial.niceForm());
            first = false;
        }
        return  output;
    }

    //return number of Monomials in Polynomial
    public int size(){
        return this.monomials.size();
    }

    //get the Monomial on position "index" from a Polynomial
    public Monomial get(int index){
        //"empty" Polynomials still need to be processed in some cases
        if (monomials.isEmpty())
            return new Monomial(0,0);
        return monomials.get(index);
    }

    //returns the biggest exponent of a Polynomial, aka the first Monomial's exponential
    public int biggestExponent(){
        //"empty" Polynomials still need to be processed in some cases
        if (monomials.isEmpty())
            return 0;

        return monomials.get(0).getExponent();
    }

    //returns the "biggest" Monomial of a Polynomial, aka the first Monomial
    public Monomial biggestMonomial(){
        //"empty" Polynomials still need to be processed in some cases
        if (monomials.isEmpty())
            return new Monomial(0,0);

        return monomials.get(0);
    }
}
