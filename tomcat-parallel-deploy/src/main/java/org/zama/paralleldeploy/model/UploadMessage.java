package org.zama.paralleldeploy.model;

/**
 * UploadMessage.
 *
 * Bean to pass messages from deployment. Useful when running curl commands.
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
