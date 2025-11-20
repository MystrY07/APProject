package eventboard;

/**
 *
 * @author aorpr
 */

//Custom Exception class for Invalid actions 
public class InvalidActionException extends Exception {
    public InvalidActionException(String message){
        super(message);
    }
}
