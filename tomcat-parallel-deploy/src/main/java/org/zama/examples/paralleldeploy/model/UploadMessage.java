package org.zama.examples.paralleldeploy.model;

/**
 * UploadMessage.
 *
 * @author Zakir Magdum
 */
public class UploadMessage {
    private String message;

    public UploadMessage() {}

    public UploadMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
