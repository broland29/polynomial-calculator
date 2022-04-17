/**Class which connects the view (GUI) with the model (logic)
 - contains implementation of ActionListener and DocumentListener
 - handles exceptions as error messages displayed on the GUI
 */

package control;

import model.Message;
import model.MessageType;
import model.Operations;
import model.Polynomial;
import view.View;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.List;

public class Control{
    //for simplicity, i divided the operations in two categories
    private final List <String> TWO_OPERAND_OPERATIONS = Arrays.asList("Add","Subtract","Multiply","Divide");
    private final List <String> ONE_OPERAND_OPERATIONS = Arrays.asList("Integrate","Differentiate");

    private final View view;

    public Control(View view){
        this.view = view;

        //add listeners
        view.addButtonButtonsListener(new ButtonButtonsListener());
        view.addOperationButtonsListener(new OperationButtonsListener());
        view.addInputDocumentListener(new InputTextFieldListener());
    }

    class OperationButtonsListener implements ActionListener{
        public void actionPerformed(ActionEvent e){
            //get the text of pressed button
            Object objectSource = e.getSource();
            JButton buttonSource = (JButton) objectSource;
            String operationText = buttonSource.getText();

            //call needed routine
            if ( TWO_OPERAND_OPERATIONS.contains(operationText)){
                twoOperandsRoutine(operationText);
            }
            else if ( ONE_OPERAND_OPERATIONS.contains(operationText)){
                oneOperatorRoutine(operationText);
            }
        }

        //routine for operations with two operands
        private void twoOperandsRoutine(String operation){
            Polynomial firstPolynomial = new Polynomial();
            Polynomial secondPolynomial = new Polynomial();
            String firstString = view.getInputText(1);
            String secondString = view.getInputText(2);

            //create new message object, associated with the operation
            Message message = new Message();
            message.setMessageType(MessageType.CORRECT);

            //try getting polynomial from first input
            try {
                if (!model.InputEvaluator.evaluationRoutine(firstString, firstPolynomial, message)) {
                    message.setMessageType(MessageType.INCORRECT);
                    view.setResultPanel(message, 1);
                    return;
                }
            }
            catch (NumberFormatException nfe) {
                message.setMessageType(MessageType.INCORRECT);
                message.setText("Not a monomial / input overflow.");
                view.setResultPanel(message,1);
                return;
            }

            //try getting polynomial from second input
            try {
                if (!model.InputEvaluator.evaluationRoutine(secondString,secondPolynomial,message)) {
                    message.setMessageType(MessageType.INCORRECT);
                    view.setResultPanel(message, 2);
                    return;
                }
            }
            catch (NumberFormatException nfe){
                message.setMessageType(MessageType.INCORRECT);
                message.setText("Not a monomial / input overflow.");
                view.setResultPanel(message,2);
                return;
            }

            //try doing the operation
            try {
                switch (operation) {
                    case "Add" -> message.setText(Operations.addition(firstPolynomial, secondPolynomial).niceForm());
                    case "Subtract" -> message.setText(Operations.subtraction(firstPolynomial, secondPolynomial).niceForm());
                    case "Multiply" -> message.setText(Operations.multiplication(firstPolynomial, secondPolynomial).niceForm());
                    case "Divide" -> message.setText(Operations.division(firstPolynomial, secondPolynomial));
                    default -> System.out.println("Unexpected entry, in twoOperandsRoutine.");
                }
            }
            catch (Operations.OverflowException | ArithmeticException e ){
                message.setMessageType(MessageType.INCORRECT);
                message.setText(e.getMessage());
            }
            view.setResultPanel(message,0);
        }

        //routine for operations with one operand - in a similar manner as twoOperandsRoutine
        private void oneOperatorRoutine(String operation){
            Polynomial polynomial = new Polynomial();
            String string = view.getInputText(1);

            Message message = new Message();
            message.setMessageType(MessageType.CORRECT);

            //try getting polynomial from first input
            try {
                if (!model.InputEvaluator.evaluationRoutine(string, polynomial, message)) {
                    message.setMessageType(MessageType.INCORRECT);
                    view.setResultPanel(message, 1);
                    return;
                }
            }
            catch (NumberFormatException nfe) {
                message.setMessageType(MessageType.INCORRECT);
                message.setText("Not a monomial / input overflow.");
                view.setResultPanel(message,1);
                return;
            }

            //try doing the operation
            try {
                switch (operation) {
                    case "Integrate" -> message.setText(Operations.integration(polynomial));
                    case "Differentiate" -> message.setText(Operations.differentiation(polynomial).niceForm());
                    default -> System.out.println("Unexpected entry, in oneOperatorRoutine.");
                }
            }catch (Operations.OverflowException oe){
                message.setMessageType(MessageType.INCORRECT);
                message.setText(oe.getMessage());
            }
            view.setResultPanel(message,0);
        }
    }

    //action listener for buttons on the lower part of calculator
    class ButtonButtonsListener implements ActionListener{
        public void actionPerformed(ActionEvent e){

            //get text of button pressed
            Object objectSource = e.getSource();
            JButton buttonSource = (JButton) objectSource;
            String text = buttonSource.getText();

            switch (text){
                case "del":
                    view.deleteInputCharacter();
                    break;
                case "sw":
                    view.swapInputs();
                    break;
                case "rst":
                    view.resetInputs();
                    break;
                case "help":
                    view.openHelp();
                    break;
                default:
                    if (text.matches("[0-9]|\\+|-|\\^|x"))     //legal input characters
                        view.addInputCharacter(text);
                    else                                             //should never reach this
                        System.out.println("Unexpected entry, in ButtonButtonsListener.");
            }
        }
    }

    //document listener, to allow users type in values from keyboard and/or copy values inside
    class InputTextFieldListener implements DocumentListener {
        @Override
        public void insertUpdate(DocumentEvent e) {
            updateFieldState();
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            updateFieldState();
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            updateFieldState();
        }

        public void updateFieldState() {
            //since document listener and swap/reset operations interfered, I avoided this by always checking
            //if the document listener was triggered by them (ignore) or by user keyboard (update internal state)
            if (!view.getOp()){
                String textOne = view.getInputText(1);
                String textTwo = view.getInputText(2);
                view.setInputTexts(textOne,textTwo);
            }
        }
    }
}