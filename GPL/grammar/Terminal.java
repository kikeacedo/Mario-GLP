package grammar;

/** 
 * 
 * Class that offers a concrete implementation of the interface Terminal.
 * 
 **/

public class Terminal extends Element {

	private Object value;
	
	/**
	 * 
	 *  Constructor that receives the ID and the Symbol.
	 *  
	 *  @param id
	 *  			The ID of the Terminal.
	 *  
	 *  @param symbol
	 *  				The symbol associated to the Terminal.
	 * 
	 **/
	public Terminal(int id, String symbol) throws GrammarException{
		super(id, symbol);
		this.setDepth(0);
		this.value = null;
	}
	
	
	/**
	 * 
	 *  Constructor that receives a terminal to get copied.
	 *  
	 *  @param terminal
	 *  			An instance of terminal.
	 * 
	 **/
	public Terminal(Terminal terminal) throws GrammarException{
		super(terminal.getId(), terminal.getSymbol());
		this.setDepth(0);
		this.value = null;
	}
	
	/**
	 * Set value object
	 * 
	 * @param object
	 * 				value of the termninal
	 * 
	 */
	public void value(Object value) { this.value = value; }
	
	
	/**
	 * get value object
	 * 
	 * @return object
	 * 				value of the terminal
	 * 
	 */
	public Object value() { return this.value; }
}