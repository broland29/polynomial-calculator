/** Class which contains all the (static) methods needed for working with user input
 *   - verification : is input valid?
 *   - evaluation : extract the necessary data, get Polynomial from String
 */

package model;

import java.util.ArrayList;

public class InputEvaluator {

    /** Evaluates the input string before it is converted into a Polynomial
     *   - delivers the cause of error through "message" (in case of invalid input)
     *   - mostly checks is the input is semantically correct, aka obviously bad
     *   - returns false if evaluation failed, true if evaluation passed
     */
    public static boolean preEvaluate(String inputString, Message message){
        //check if input string is empty
        if (inputString.isEmpty()){
            message.setText("Empty input");
            return false;
        }

        //check if contains anything except characters needed to describe a polynomial, using regex
        if (!inputString.matches("^[0-9x^+-]*$")){
            System.out.println("Illegal character in input.");
            message.setText("Illegal character in input.");
            return false;
        }

        //check for suspicious substrings, which may lead to failure when evaluating input
        final String[] prohibitedSubstrings = {"++", "--", "+-", "-+", "^^", "xx" , "^x" , "^0", "^1+","^1-","-^","+^","+0","-0"};
        for (String i : prohibitedSubstrings){
            if (inputString.contains(i)){
                System.out.println("Prohibited substring in input.");
                message.setText("Prohibited substring in input.");
                return false;
            }
        }
        return true;
    }

    /** Makes the conversion from a String (inputString) to a Polynomial (outputPolynomial)
     *   - this will only occur after passing pre-evaluation
     *   - may throw a NumberFormatException due to usage of Integer,parseInt on inputs like "^"
     *      - this will be handled when used
     */
    public static void evaluateInput(String inputString, Polynomial outputPolynomial) throws NumberFormatException{

        //split text into chunks, each chunk will be digested as a monomial
        String[] chunks = inputString.split("((?=\\+|-))");     //strings starting with + or -

        for (String chunk : chunks){

            //split one chunk into data related to coefficient and exponent
            String[] c = chunk.split("((?=x\\^|x))");           //strings starting with x or x^
            int coefficient;
            int exponent;

            if (c.length == 1){ //one token input

                if (c[0].contains("x")){
                    if (c[0].equals("x")){      //input : "x"
                        coefficient = 1;
                        exponent = 1;
                    }
                    else{                       //input : "x^(number)"
                        coefficient = 1;
                        exponent = Integer.parseInt(c[0].substring(2));
                    }
                }
                else{                           //input : "(number)"
                    coefficient = Integer.parseInt(c[0]);
                    exponent = 0;
                }
            }
            else{        //two token input
                if (c[0].equals("-"))
                    coefficient = -1;           //coefficient is just a "-", parseInt fails
                else if (c[0].equals("+")){
                    coefficient = 1;            //coefficient is just a "+", parseInt fails
                }
                else
                    coefficient = Integer.parseInt(c[0]);

                if (c[1].equals("x")){          //input: (number)x
                    exponent = 1;
                }
                else{                           //input: "(number1)x^(number2)"
                    exponent = Integer.parseInt(c[1].substring(2));
                }
            }
            outputPolynomial.add(new Monomial(coefficient,exponent));
        }
    }

    /** Checks if the Polynomial delivered by evaluateInput is in correct order and minimized
     *   - in other words, if Monomials are in strictly decreasing order w.r.t their exponent
     *   - returns false if evaluation failed, true if evaluation passed
     */
    public static boolean postEvaluate(Polynomial polynomial, Message message){
        ArrayList<Monomial> monomials = polynomial.getMonomials();

        boolean first = true;       //for first Monomial we do not have previous Monomial => nothing to check
        int previousExponent = -1;
        int currentExponent;

        for (Monomial m : monomials){
            currentExponent = m.getExponent();
            if (!first){
                if (currentExponent > previousExponent){
                    message.setText("Invalid order of monomials: " + currentExponent + " > " + previousExponent + ".");
                    return false;
                }
                else if (currentExponent == previousExponent){
                    message.setText("Multiple monomials with power " + currentExponent +".");
                    return false;
                }
            }
            previousExponent = currentExponent;
            first = false;
        }
        return true;
    }


    /** Method to tie together the previous three methods, in order to use them easily
     *   - it does all the needed evaluation, and also returns the resulted Polynomial through parameter "polynomial"
     *   - may throw a NumberFormatException due to evaluateInput
     *      - this will be handled when used
     *   - returns false if evaluation failed, true if evaluation passed
     */
    public static boolean evaluationRoutine(String string, Polynomial polynomial, Message message) throws NumberFormatException{

        //pre-evaluate
        if (!preEvaluate(string,message))
            return false;

        //evaluate
        try{
            evaluateInput(string,polynomial);
        }
        catch (NumberFormatException nfe){
            message.setText("Bad input: overflow / not polynomial");
            throw new NumberFormatException();
        }

        //post-evaluate
        return postEvaluate(polynomial,message);
    }
}