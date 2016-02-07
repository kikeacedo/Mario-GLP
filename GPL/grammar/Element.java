package grammar;


/** 
 * 
 * Class that offers a concrete implementation of the interface Element.
 * 
 **/

public class Element {

	
	
	/** The holder of the ID associated with the Element. */
	private int id;
	
	/** The holder of the symbols associated with the Element. */
	private String symbol;
	
	/** The depth of the element */
	private int depth;
	
	
	/**
	 * 
	 *  Constructor that receives de ID and the Symbol.
	 *  
	 *  @param id
	 *  			The ID of the Element.
	 *  
	 *  @param symbol
	 *  				The symbol associated to the Element.
	 * 
	 * */
	public Element(int id, String symbol) throws GrammarException{
		if(id<0 || symbol==null){
			throw new GrammarException("The Id is smaller than zero, or the symbol is NULL.");
		}
		this.id = id;
		this.symbol = symbol;
		this.depth = -1;
	}

	
	/** 
	 * 
	 * Return the ID of the Element.
	 * 
	 **/
	public int getId(){ return this.id; }
	
	
	/** 
	 * 
	 * Set the ID of the Element.
	 * 
	 * Throws a GrammarExceptionImpl if the ID<0.
	 * 
	 * @param id
	 * 			 The ID to be associated with the Element.
	 * 
	 **/
	public void setId(int id) throws GrammarException{
		if(id>=0){
			throw new GrammarException("The ID should be greater or equal than 0");
		}
		this.id = id;
	}

	
	/** 
	 * 
	 * Return the symbol of the Element.
	 * 
	 **/
	public String getSymbol() { return this.symbol; }

	
	/** 
	 * 
	 * Set the symbol of the Element.
	 * 
	 * @param s
	 * 			 The symbol to be associated with the Element.
	 * 
	 **/
	public void setSymbol(String s) throws GrammarException {
		if(symbol==null){
			throw new GrammarException("The symbol is NULL.");
		}
		this.symbol = s;
	}
	
	
	/**
	 * Set the depth of the Element. Necessary for Grammar Based Population Initalization
	 * 
	 * @param depth
	 * 				Depth to be set
	 * @throws GrammarException
	 * 				Throws an exception if depth is fewer than zero
	 */
	public void setDepth(int depth) throws GrammarException {
		if (depth < 0) {
			throw new GrammarException("The depth of the element should be equal or greater than zero");
		}
		this.depth = depth;
	}
	
	
	/**
	 * Returns the depth of the element
	 * 
	 * @return
	 * 		depth
	 */
	public int getDepth() { return this.depth; }
	
	public void clearElement () {
		this.depth = -1;
		this.id= -1;
		this.symbol= null;
	}
}
