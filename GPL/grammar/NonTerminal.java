package grammar;

/** 
 * 
 * Class that offers a concrete implementation of the interface NonTerminal.
 * 
 **/

public class NonTerminal extends Element{

	/**
	 * 
	 *  Constructor that receives de ID and the Symbol.
	 *  
	 *  @param id
	 *  			The ID of the NonTerminal.
	 *  
	 *  @param symbol
	 *  				The symbol associated to the NonTerminal.
	 * 
	 **/
	public NonTerminal(int id, String symbol) throws GrammarException{
		super(id, symbol);
	}
}