/** Class used for communicating between input evaluation and GUI
 *   - final purpose : to set the result or error on the resultLabel of View
 */

package model;

public class Message {

    private String text;
    private MessageType messageType;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public void setMessageType(MessageType messageType) {
        this.messageType = messageType;
    }
}