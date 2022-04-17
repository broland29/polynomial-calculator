/** Where the magic happens
 *   - contains all (static) methods needed to do operations on Polynomial(s)
 *   - has some private methods as well, to avoid code repetition
 */

package model;

import java.util.ArrayList;

public class Operations {

    /** Monomial addition - produces sum of two monomials
     *   - returns monomial equal to mOne + mTwo
     *   - will be used in addition of Polynomials
     *   - add coefficients, keep exponent
     *   - only called when mOne and mTwo have same exponent
     */
    public static Monomial monomialAddition(Monomial mOne, Monomial mTwo) throws OverflowException{
        int coeOne = mOne.getCoefficient();
        int coeTwo = mTwo.getCoefficient();

        //whenever there is a possibility for overflow, it is checked by methods and
        //an OverflowException custom exception is thrown if needed
        overflowCheckForAddition(coeOne,coeTwo);

        return new Monomial(coeOne+coeTwo,mOne.getExponent());
    }

    /** Polynomial addition
     *   - uses Monomial addition
     *   - follows an algorithm similar to merging two arrays
     *   - works, since Polynomials' Monomials are already ordered in decreasing order of their exponents
     */
    public static Polynomial addition(Polynomial pOne, Polynomial pTwo) throws OverflowException{
        Polynomial result = new Polynomial();

        int n = 0;
        int m = 0;
        int expOne;
        int expTwo;

        //while end not reached
        while (n < pOne.size() && m < pTwo.size()) {

            expOne = pOne.get(n).getExponent();
            expTwo = pTwo.get(m).getExponent();

            if (expOne == expTwo){
                result.add(monomialAddition(pOne.get(n),pTwo.get(m)));
                n++;
                m++;
            }
            else if (expOne > expTwo){
                result.add(pOne.get(n));
                n++;
            }
            else{
                result.add(pTwo.get(m));
                m++;
            }
        }

        //add what may be left over
        while(n < pOne.size()){
            result.add(pOne.get(n));
            n++;
        }
        while(m < pTwo.size()){
            result.add(pTwo.get(m));
            m++;
        }

        return result;
    }

    /** Polynomial subtraction
     *   - returns polynomial equal to pOne - pTwo
     *   - profits of the idea that a - b is just a + (-b)
     */
    public static Polynomial subtraction(Polynomial pOne, Polynomial pTwo) throws OverflowException{
        Polynomial flippedPTwo;
        Polynomial result = new Polynomial();

        try{
            flippedPTwo = flipPolynomial(pTwo);     //flip the second polynomial
            result = addition(pOne,flippedPTwo);    //add pOne to -pTwo
        }
        catch (OverflowException oe){
            //change overflow texts to match operation
            if (oe.getMessage().equals("Addition positive overflow."))
                throw new OverflowException("Subtraction positive overflow");
            if (oe.getMessage().equals("Addition negative overflow"))
                throw new OverflowException("Subtraction negative overflow.");
        }
        return result;
    }

    /** Monomial multiplication
     *   - used in Polynomial multiplication
     *   - returns Monomial equal to mOne * mTwo
     *   - multiply coefficients, add exponents
     */
    public static Monomial multiplyMonomial(Monomial mOne, Monomial mTwo) throws OverflowException{
        int coefficientOne = mOne.getCoefficient();
        int coefficientTwo = mTwo.getCoefficient();

        //there is a very small chance that the input is, for example 0 - Integer.MAX_VALUE
        //in this case it will be evaluated as 0 + (-)Integer.MAX_VALUE, and since the limits
        //are not symmetrical (-2147483648,2147483647), this may cause an overflow
        overflowCheckForAMultiplication(coefficientOne,coefficientTwo);

        int exponentOne = mOne.getExponent();
        int exponentTwo = mTwo.getExponent();

        overflowCheckForAddition(exponentOne,exponentTwo);

        return new Monomial(coefficientOne * coefficientTwo, exponentOne + exponentTwo);
    }

    /** Polynomial multiplication
     *   - uses Monomial multiplication
     *   - returns Polynomial equal to pOne * pTwo
     *   - after doing multiplication itself, simplification is done as well
     *      - this is essential, since otherwise returned Polynomial would not be "valid"
     */
    public static Polynomial multiplication(Polynomial pOne, Polynomial pTwo)throws OverflowException{
        Polynomial result = new Polynomial();

        ArrayList<Monomial> monomialsOne = pOne.getMonomials();
        ArrayList<Monomial> monomialsTwo = pTwo.getMonomials();

        //multiply every monomial on one side with every monomial on other side
        for (Monomial i : monomialsOne){
            for (Monomial j : monomialsTwo){
                result.add(multiplyMonomial(i,j));
            }
        }

        //simplify - sum each monomial's coefficient with the same exponent,
        //and create a new, minimized Polynomial from this data

        int i = result.biggestExponent();    //iterate from biggest exponent
        int sumCoefficient;

        Polynomial simplifiedResult = new Polynomial();
        ArrayList<Monomial> monomialsResult = result.getMonomials();

        while (i >= 0){
            sumCoefficient = 0;
            for (Monomial m : monomialsResult)
                if (m.getExponent() == i)
                    sumCoefficient += m.getCoefficient();
            if (sumCoefficient != 0)
                simplifiedResult.add(new Monomial(sumCoefficient,i));
            i--;
        }
        return simplifiedResult;
    }

    /** Monomial division
     *   - will be used in Polynomial division
     *   - returns a Monomial equal to mOne / mTwo
     *   - divide the coefficients and subtract the exponents
     */
    public static Monomial divideMonomial(Monomial mOne, Monomial mTwo) throws ArithmeticException{
        if (mOne.getExponent() < mTwo.getExponent()){
            throw new ArithmeticException("Cannot do");     //this should never be reached
        }
        return new Monomial(mOne.getCoefficient() / mTwo.getCoefficient(), mOne.getExponent() - mTwo.getExponent());
    }

    /** Polynomial division
     *   - returns a String, which represents the result of pOne / pTwo
     *      - result is shown in format Q: ___  R: ___ where Q means quotient, R means remainder
     *   - may produce ArithmeticException if division with 0 happens
     *   - follows the Long Division Method
     */
    public static String division(Polynomial pOne, Polynomial pTwo) throws ArithmeticException, OverflowException{
        if (pOne.biggestExponent() < pTwo.biggestExponent())
            return "Q: 0  R: " + pOne.niceForm();

        Polynomial quotient = new Polynomial();         //"whole" part of division
        Polynomial remainder = new Polynomial(pOne);    //what we are left with after division

        Monomial currentQuotientM;      //what we get by dividing the largest power monomials
        Polynomial currentQuotientP;    //monomial and polynomial form

        Polynomial toSubtract;        //what we subtract at a given step

        int divisorExponent = pTwo.biggestExponent();       //these are
        Monomial divisorMonomial = pTwo.biggestMonomial();  //constants

        while (!remainder.niceForm().equals("0") && remainder.biggestExponent() >= divisorExponent){
            currentQuotientM = divideMonomial(remainder.biggestMonomial(),divisorMonomial);
            if (currentQuotientM.niceForm().equals("0"))
                break;
            System.out.println(currentQuotientM.niceForm());
            if (!currentQuotientM.isNull())
                quotient.add(currentQuotientM);

            currentQuotientP = new Polynomial();
            currentQuotientP.add(currentQuotientM);

            toSubtract = multiplication(currentQuotientP,pTwo);
            remainder = subtraction(remainder,toSubtract);
        }
        return "Q: " + quotient.niceForm() + "  R: " + remainder.niceForm();
    }


    /** Polynomial differentiation
     *   - returns a Polynomial equal to the differentiation of pOne w.r.t "x"
     *   - relatively simple: multiply coefficient with exponent, decrement exponent
     *   - differentiation of constants results in 0
     */
    public static Polynomial differentiation(Polynomial pOne) throws OverflowException{
        Polynomial result = new Polynomial();

        ArrayList<Monomial> monomials = pOne.getMonomials();
        int exponent;
        int coefficient;
        for (Monomial i : monomials){
            exponent = i.getExponent();
            if (exponent == 0){
                continue;
            }
            coefficient = i.getCoefficient();

            overflowCheckForAMultiplication(coefficient,exponent);

            result.add(new Monomial(coefficient * exponent,exponent-1));
        }

        return result;
    }

    /** Polynomial integration
     *   - returns a string representing the result of integrating pOne w.r.t. x
     *   - relatively simple, increment exponent, and "divide" by the new exponent
     *      - division in this case is symbolic, result will be represented as ___[/denominator][+]C
     *      - simplification of denominator and coefficient is done as well
     */
    public static String integration(Polynomial pOne) throws OverflowException{
        String result = "";

        ArrayList<Monomial> monomials = pOne.getMonomials();

        int exponent;
        int coefficient;
        int denominator;
        int gcd;
        boolean first = true;

        for (Monomial i : monomials){
            exponent = i.getExponent();
            coefficient = i.getCoefficient();

            if (!first && coefficient > 0)    //plus sign needs to be explicitly printed, if needed
                result = result.concat("+");

            //integrate
            overflowCheckForAddition(exponent,1);
            exponent++;
            denominator = exponent;

            //simplify if needed
            gcd = greatestCommonDenominator(coefficient,denominator);

            if (gcd > 1){
                denominator /= gcd;
                coefficient /= gcd;
            }

            if (denominator > 1)
                result = result.concat("(");

            result = result.concat(new Monomial(coefficient,exponent).niceForm());

            if (denominator > 1){
                result = result.concat(")/" + denominator);//coefficient 1 handled by niceForm
            }
            first = false;
        }

        //add the "C" at the end
        if (result.equals("0") || result.equals(""))
            return "C";
        result = result.concat("+C");
        return result;
    }

    //returns the greatest common denominator of a and b
    public static int greatestCommonDenominator(int a, int b){
        if (b == 0) return a;
        return greatestCommonDenominator(b,a%b);
    }

    //custom exception for overflow checking
    public static class OverflowException extends Exception {
        OverflowException(String msg){
            super(msg);
        }
    }

    private static void overflowCheckForAddition(int a, int b) throws OverflowException{
        long overflowTest = (long)a + (long)b;
        if (overflowTest > Integer.MAX_VALUE)
            throw new OverflowException("Addition positive overflow.");
        if (overflowTest < Integer.MIN_VALUE)
            throw new OverflowException("Addition negative overflow");
    }

    private static void overflowCheckForAMultiplication(int a, int b) throws OverflowException{
        long overflowTest = (long)a * (long)b;
        if (overflowTest > Integer.MAX_VALUE)
            throw new OverflowException("Multiplication positive overflow.");
        if (overflowTest < Integer.MIN_VALUE)
            throw new OverflowException("Multiplication negative overflow.");
    }

    //returns the "reverse" of inputPolynomial - flips each monomial's sign
    private static Polynomial flipPolynomial(Polynomial inputPolynomial) throws OverflowException{
        Polynomial invertedPolynomial = new Polynomial();
        ArrayList<Monomial> inputMonomials = inputPolynomial.getMonomials();
        for(Monomial m : inputMonomials){

            overflowCheckForAddition(m.getCoefficient(),-1);

            invertedPolynomial.add(new Monomial(m.getCoefficient()*-1,m.getExponent()));
        }
        return invertedPolynomial;
    }
}
