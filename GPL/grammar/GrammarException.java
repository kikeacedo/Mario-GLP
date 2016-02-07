package grammar;

/**
 * 
 *  Class that implements a custom Exception in order to handle the exceptions that can appear processing
 *  the Grammar.
 *  
 * 
 * */

public class GrammarException extends  Exception{

	
	private static final long serialVersionUID = 1L;

	
	public GrammarException(String s){
		super(s);
	}
	
}
