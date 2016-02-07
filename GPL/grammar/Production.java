package grammar;

import java.util.ArrayList;


/** 
 * 
 * Class that offers a concrete implementation of the interface Production.
 * 
 **/

public class Production extends Element{
	
	/** Holder for the NonTerminal symbol at the left side of the Production. */
	private NonTerminal left;
	
	/** Holder for the NonTerminal and Terminal symbols at the right side of the Production. 
	 *  The position of a symbol in the array is the position of the symbol in the right side
	 *  of the Production. */
	private ArrayList<Element> right;
	
	private int length;
	
	
	
	/**
	 * 
	 * Void constructor.
	 *  
	 * * 
	 * 
	 **/
	public Production(int id, String symbol) throws GrammarException{
		super(id,symbol);
		right = new ArrayList<Element>();
	}
	
	
	/**
	 * 
	 * Constructor that receives the NonTerminal symbol of the left side of the Production,
	 * and the amount of symbols of the right side of the Production.
	 * 
	 * Throws a GrammarExceptionImpl if the left symbol is NULL, or l<=0.
	 * 
	 * @param left
	 * 			   The NonTerminal symbol of the left side of the Production.
	 * 
	 * @param l
	 * 		    The amount of symbols of the right side of the Production.
	 * 
	 *  
	 *  @param id
	 *  			The ID of the Production.
	 *  
	 *  @param symbol
	 *  				The symbol associated to the Production.
	 * 
	 * * 
	 * 
	 **/
	public Production(int id, String symbol, NonTerminal left, ArrayList<Element> right ) throws GrammarException{
		super(id,symbol);
		this.setId(id);
		this.setLeft(left);
		this.setRight(right);
		this.setDepth(-1);
	}
	
	/**
	 * 
	 * Insert the symbol that is in the position pos at the right side of the Production.
	 * 
	 * Returns TRUE if is possible insert the Element at the position pos, FALSE otherwise.
	 * 
	 * @param e
	 * 			The symbol that is at the right side of the Production.
	 * 
	 **/
	public void addRightElement(Element e) { this.right.add(e); }
	
	
	/**
	 * 
	 * Insert the symbol that is in the position pos at the right side of the Production.
	 * 
	 * Returns TRUE if is possible insert the Element at the position pos, FALSE otherwise.
	 * 
	 * @param e
	 * 			The symbol that is at the right side of the Production.
	 * 
	 * @param pos
	 * 			  The absolute position (starting in 1) of the symbol at the right side of the 
	 *            Production.
	 * 
	 **/
	public void addRightElement(Element e, int pos) { this.right.add(pos, e); }

	
	/** 
	 * 
	 * @param g
	 * 			 The Grammar associated to the Production.
	 * 			
	 * 
	 * Returns the NonTerminal symbol that is in the left side of the Production.
	 * 
	 * It does not belong to the Public API.
	 * 
	 * 
	 **/
	public NonTerminal getLeft() throws GrammarException { 
		if (this.left == null) {
			throw new GrammarException("Unexpected error, left is null");
		}
		return left;
	}
	
	
	/** 
	 * 
	 * Set the NonTerminal nt as the left side symbol of the Production.
	 * 
	 * Throws a GrammarExceptionImpl if the NonTerminal is NULL.
	 * 
	 * @param nt
	 * 			 The NonTerminal symbol to be associated to the Production.
	 * 
	 **/
	public void setLeft(NonTerminal left) throws GrammarException{
		if (left == null) {
			throw new GrammarException("Left cannot be null");
		}
		this.left = left; 
	}
	
	
	/** 
	 * 
	 * @param g
	 * 			The Grammar associated to the Production.
	 * 
	 * Returns a collection with the NonTerminals that are at the right side of the 
	 * Production.
	 * 
	 * Throws a GrammarException if it isn´t possible to add a NonTerminal to the Collection.
	 * 
	 * Return NULL if there aren´t symbols at the right side.
	 * 
	 **/
	public ArrayList<NonTerminal> getNonTerminalsRight() throws GrammarException {
		NonTerminal nt = new NonTerminal(0, "");
		ArrayList<NonTerminal> nts = new ArrayList<NonTerminal>();
		
		for (Element e: right) {
			if(nt.getClass().isInstance(e)) {
				nts.add((NonTerminal) e);
			}
		}
		if (nts.isEmpty()) {
			throw new GrammarException("Unexpected error, None NonTerminal is returned");
		}
		
		return nts;
	}


	/** 
	 * 
	 * @param g
	 * 			 The Grammar associated to the Production.
	 * 
	 * Returns a collection with the Terminals that are at the right side of the 
	 * Production.
	 * 
	 * Throws a GrammarException if it isn´t possible to add a Terminal to the Collection.
	 * 
	 * Return NULL if there aren´t symbols at the right side. 
	 * 
	 * 
	 **/
	public ArrayList<Terminal> getTerminalsRight() throws GrammarException {
		Terminal t = new Terminal(0, "");
		ArrayList<Terminal> ts = new ArrayList<Terminal>();
		
		for (Element e: this.right) {
			if(t.getClass().isInstance(e)) {
				ts.add((Terminal) e);
			}
		}
		
		if (ts.isEmpty()) {
			throw new GrammarException("Unexpected error, None Terminal is returned");
		}
		
		return ts;
	}
	
	
	/** 
	 * 
	 * @param g
	 * 			 The Grammar associated to the Production.
	 * 
	 * Returns a Collection with all the symbols at the right side with the order preserved.
	 * 
	 * Throws a GrammarException if it isn´t possible to add an Element to the Collection, or 
	 * if there are NULL elements mixed with non NULL elements at the right side of 
	 * the Production.
	 * 
	 * Return NULL if there aren´t symbols at the right side of the production.
	 * 
	 * 
	 **/
	public ArrayList<Element> getRight() throws GrammarException{ 
		if (this.right == null || this.right.size() == 0) {
			throw new GrammarException("Unexpected error, right is null or empty");
		}
		return this.right; 
	}
	
	public ArrayList<Element> getRightCopy() throws GrammarException{ 
		if (this.right == null || this.right.size() == 0) {
			throw new GrammarException("Unexpected error, right is null or empty");
		}
		ArrayList<Element> copy = new ArrayList<Element>(this.right.size());
		
		for(Element e: this.right){
			copy.add(e);
		}
		return copy; 
	}
		
	
	
	public void setRight(ArrayList<Element> right) throws GrammarException { 
		if (right.size() < 1) {
			throw new GrammarException("Lenght of right elements must be grater than cero");
		}
		this.right = right;
	}
	
	public Element getRightAt(int pos) throws GrammarException {
		if (this.right == null) {
			throw new GrammarException("Unexpected error, right is null");
		}
		return this.right.get(pos); 
	}
	
	
	/** 
	 * 
	 * If the Element e belongs to the right side of the Production, then returns an ordered Collection
	 * with the positions where the Element appears, otherwise returns NULL.
	 * 
	 * The positions are converted to the absolute position starting at 1.
	 * 
	 * Throws a GrammarException if it isn´t possible to add a position to the Collection. 
	 * 
	 * @param e
	 * 			 The Element symbol to be checked with the Production.
	 * 
	 * 
	 **/
	public ArrayList<Integer> getRightPosition(Element right) throws GrammarException {
		ArrayList<Integer> positions = new ArrayList<Integer>();
		
		/* Checks that e is not null and isnt a Production object. */
		if(right==null){
			throw new GrammarException("The right element cannot be null");
		}
		
		for(int i=0; i<this.right.size(); i++) {
			if(this.right.get(i).equals(right)) {
				positions.add(i);
			}
		}
		return positions;
	}
	
	
	/**
	 * 
	 * Returns the length of the production.
	 * 
	 * 
	 * @return int
	 * 				Length of right production
	 */
	public int length() {
		if (this.length == 0 ) {
			this.length = this.right.size(); 
		}
		return this.length;
	}
}
