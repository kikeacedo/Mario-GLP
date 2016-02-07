package grammar;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;



/** 
 * 
 * Class that offers a concrete implementation of the interface Grammar.
 * 
 **/

public class Grammar {

	/** Holds the maximum id used as key for the Elements of the Grammar (NonTerminals, Terminals and 
	 *  Productions). Then if a new Element is created, its ID will be idMax + 1. **/
	private int idMax;
	
	/** Holds the Terminals of the Grammar. */
	private Elements<Terminal> terminals;
	
	/** Holds the NonTerminals of the Grammar. */
	private Elements<NonTerminal> nonterminals;
	
	/** Holds the Productions of the Grammar. */
	private Productions productions;
	
	/** Stores the grammar axiom */
	private NonTerminal axiom;

	
	/**
	 * 
	 * Empty constructor.
	 *
	 */
	public Grammar(){
		this.idMax = 1;
		this.terminals = new Elements<Terminal>();
		this.nonterminals = new Elements<NonTerminal>();
		this.productions = new Productions();
		this.axiom = null;
	}
	
	/**
	 * 
	 * File constructor
	 * @throws GrammarException 
	 * 
	 */
	public Grammar(String filePath) throws GrammarException{
		this.idMax = 1;
		this.terminals = new Elements<Terminal>();
		this.nonterminals = new Elements<NonTerminal>();
		this.productions = new Productions();
		this.axiom = null;
		
		this.grammarParser(filePath);
		this.calculateDepths();
	}
	
	public Grammar(BufferedReader br) throws GrammarException{
		this.idMax = 1;
		this.terminals = new Elements<Terminal>();
		this.nonterminals = new Elements<NonTerminal>();
		this.productions = new Productions();
		this.axiom = null;
		
		this.grammarParser(br);
		this.calculateDepths();
	}
	
	private void grammarParser(BufferedReader file) throws GrammarException {
		String line;
		int numLine = 0;
		BufferedReader  buffer;
		
		buffer = file;
		
		try {
			while((line = buffer.readLine()) != null) {
				numLine++;
				
				/* Elements definition */
				if (line.startsWith("#")) {
					List<String> subLine = new LinkedList<String>(Arrays.asList(line.split("#")));
					subLine.removeAll(Collections.singleton(""));
					if (subLine.size() != 2) {
						throw new GrammarException("Syntax error at line "+ numLine + ". #{A,N,T}#{Axiom,List-of-terminals,List-of-non-terminals}");
					}
					
					switch(subLine.get(0).charAt(0)) {
					case 'A':
						List<String> a = new LinkedList<String>(Arrays.asList(subLine.get(1).split(" ")));
						a.removeAll(Collections.singleton(""));
						if (a.size() == 1) {
							this.axiom = new NonTerminal(this.idMax++, a.get(0));
							
						} else {
							throw new GrammarException("Syntax error at line "+ numLine + ". Just one axiom is required.");
						}
						break;
					case 'N':
						List<String> nt = new LinkedList<String>(Arrays.asList(subLine.get(1).split(" ")));
						nt.removeAll(Collections.singleton(""));
						if (nt.size() == 0) {
							throw new GrammarException("Syntax error at line "+ numLine + ". At least one non terminal is required");
						}
						for(String el: nt) {
							this.nonterminals.add(new NonTerminal(this.idMax++, el));
						}
						break;
					case 'T':
						List<String> t = new LinkedList<String>(Arrays.asList(subLine.get(1).split(" ")));
						t.removeAll(Collections.singleton(""));
						if (t.size() == 0) {
							throw new GrammarException("Syntax error at line "+ numLine + ". At least one non terminal is required");
						}
						for(String el: t) {
							this.terminals.add(new Terminal(this.idMax++, el));
						}
						break;
						default:
							throw new GrammarException("Syntax error at line "+ numLine);
					}
				/* Production definition */	
				} else { 
					Element leftElement;
					
					List<String> productions = new LinkedList<String>(Arrays.asList(line.split("::=")));
					productions.removeAll(Collections.singleton(""));
					if (productions.size() != 2) {
						continue;
					}
					
					List<String> left = new LinkedList<String>(Arrays.asList(productions.get(0).split(" ")));
					left.removeAll(Collections.singleton(""));
					if (left.size() != 1) {
						throw new GrammarException("Syntax error at line "+ numLine);
					}
					
					leftElement = this.nonterminals.get(left.get(0));
					if((leftElement == null)) {
						if (this.axiom.getSymbol().equals(left.get(0))) {
							leftElement = this.axiom;
						} else {
							throw new GrammarException("Syntax error at line "+ numLine + ". Undefined non terminal.");
						}
					}
					
					List<String> rights = new LinkedList<String>(Arrays.asList(productions.get(1).split("\\|")));
					rights.removeAll(Collections.singleton(""));
					
					for(String production: rights) {
						Production p = new Production(this.idMax++, line);
						p.setLeft((NonTerminal) leftElement);
						
						List<String> right = new LinkedList<String>(Arrays.asList(production.split(" ")));
						right.removeAll(Collections.singleton(""));
						if (right.size() < 1) {
							throw new GrammarException("Syntax error at line "+ numLine);
						}
						
						for (String el: right) {
							Element rightElement = nonterminals.get(el);
							if (rightElement == null) {
								rightElement = terminals.get(el);
							}
							if (rightElement == null) {
								throw new GrammarException("Syntax error at line "+ numLine + ". Undefined element.");
							}
							p.addRightElement(rightElement);
						}
						this.productions.add(p);
					}
				}
			}	
			file.close();
			if(this.axiom == null || this.terminals.isEmpty() || this.nonterminals.isEmpty() || this.productions.isEmpty()) {
				throw new GrammarException("Incomplete grammar");
			}
		} catch (IOException e) {
			throw new GrammarException(e.getMessage());
		}
	}
	
	private void grammarParser(String path) throws GrammarException{
		String line;
		int numLine = 0;
		FileReader file;
		BufferedReader  buffer;
		
		/* Open grammar file */
		try {
			file = new FileReader(path);
			buffer = new BufferedReader(file);
		} catch (FileNotFoundException e) {
			throw new GrammarException(e.getMessage());
		}
		
		try {
			while((line = buffer.readLine()) != null) {
				numLine++;
				
				/* Elements definition */
				if (line.startsWith("#")) {
					List<String> subLine = new LinkedList<String>(Arrays.asList(line.split("#")));
					subLine.removeAll(Collections.singleton(""));
					if (subLine.size() != 2) {
						buffer.close();
						file.close();
						throw new GrammarException("Syntax error at line "+ numLine + ". #{A,N,T}#{Axiom,List-of-terminals,List-of-non-terminals}");
					}
					
					switch(subLine.get(0).charAt(0)) {
					case 'A':
						List<String> a = new LinkedList<String>(Arrays.asList(subLine.get(1).split(" ")));
						a.removeAll(Collections.singleton(""));
						if (a.size() == 1) {
							this.axiom = new NonTerminal(this.idMax++, a.get(0));
							
						} else {
							buffer.close();
							file.close();
							throw new GrammarException("Syntax error at line "+ numLine + ". Just one axiom is required.");
						}
						break;
					case 'N':
						List<String> nt = new LinkedList<String>(Arrays.asList(subLine.get(1).split(" ")));
						nt.removeAll(Collections.singleton(""));
						if (nt.size() == 0) {
							buffer.close();
							file.close();
							throw new GrammarException("Syntax error at line "+ numLine + ". At least one non terminal is required");
						}
						for(String el: nt) {
							this.nonterminals.add(new NonTerminal(this.idMax++, el));
						}
						break;
					case 'T':
						List<String> t = new LinkedList<String>(Arrays.asList(subLine.get(1).split(" ")));
						t.removeAll(Collections.singleton(""));
						if (t.size() == 0) {
							throw new GrammarException("Syntax error at line "+ numLine + ". At least one non terminal is required");
						}
						for(String el: t) {
							this.terminals.add(new Terminal(this.idMax++, el));
						}
						break;
						default:
							buffer.close();
							file.close();
							throw new GrammarException("Syntax error at line "+ numLine);
					}
				/* Production definition */	
				} else { 
					Element leftElement;
					
					List<String> productions = new LinkedList<String>(Arrays.asList(line.split("::=")));
					productions.removeAll(Collections.singleton(""));
					if (productions.size() != 2) {
						continue;
					}
					
					List<String> left = new LinkedList<String>(Arrays.asList(productions.get(0).split(" ")));
					left.removeAll(Collections.singleton(""));
					if (left.size() != 1) {
						buffer.close();
						file.close();
						throw new GrammarException("Syntax error at line "+ numLine);
					}
					
					leftElement = this.nonterminals.get(left.get(0));
					if((leftElement == null)) {
						if (this.axiom.getSymbol().equals(left.get(0))) {
							leftElement = this.axiom;
						} else {
							buffer.close();
							file.close();
							throw new GrammarException("Syntax error at line "+ numLine + ". Undefined non terminal.");
						}
					}
					
					List<String> rights = new LinkedList<String>(Arrays.asList(productions.get(1).split("\\|")));
					rights.removeAll(Collections.singleton(""));
					
					for(String production: rights) {
						Production p = new Production(this.idMax++, line);
						p.setLeft((NonTerminal) leftElement);
						
						List<String> right = new LinkedList<String>(Arrays.asList(production.split(" ")));
						right.removeAll(Collections.singleton(""));
						if (right.size() < 1) {
							buffer.close();
							file.close();
							throw new GrammarException("Syntax error at line "+ numLine);
						}
						
						for (String el: right) {
							Element rightElement = nonterminals.get(el);
							if (rightElement == null) {
								rightElement = terminals.get(el);
							}
							if (rightElement == null) {
								buffer.close();
								file.close();
								throw new GrammarException("Syntax error at line "+ numLine + ". Undefined element.");
							}
							p.addRightElement(rightElement);
						}
						this.productions.add(p);
					}
				}
			}	
			buffer.close();
			file.close();
			if(this.axiom == null || this.terminals.isEmpty() || this.nonterminals.isEmpty() || this.productions.isEmpty()) {
				throw new GrammarException("Incomplete grammar");
			}
		} catch (IOException e) {
			throw new GrammarException(e.getMessage());
		}
	}
	
	private void calculateDepths() throws GrammarException {
		calculateDepths(axiom);
	}
	
	
	private int calculateDepths(Element el) throws GrammarException {
		int d = el.getDepth();
		
		/* If the depth of the element is already known it, it is returned */
		if(d != -1) {
			return d;
		}
		
		if(el instanceof Production){
			/* The deth of a Production is the maximum depth of its Elements plus one */
			int max = 0;

			for(Element e: ((Production) el).getRight()) {
				if (((Production) el).getLeft().equals(e)) {
					if (((Production) el).getRight().size() == 1) {
						throw new GrammarException("The Production" + ((Production) el).getSymbol() + "is not valid. Infinite loop");
					}
					/* A high value of depth is set to igonre the producion in the next recursive step */
					el.setDepth(999);
				}
				d = this.calculateDepths(e);

				if (d == -1) {
					throw new GrammarException("Syntax error. Uncomplete grammar");
				}
				if (d > max) { max = d; }
			}
			/* if real depth is obtained it is set otherwise a default value is returned */
			d = max+1;
			el.setDepth(d);			
		} else {
			/* The depth of a Non Terminal is the minimum depth of its productions depth*/ 
			int min = 999;
			for(Production p: this.productions.getProductionsWithLeft((NonTerminal) el)) {
				d = this.calculateDepths(p);
				if (d == -1) {
					throw new GrammarException("Incorrect semantic declaration of grammar, all non terminals should have associated a production");
				}
				if (d < min) { min = d; }
			}

			d = min;
			el.setDepth(d);
		}
		return d;
	}
	
	public int minimumDepth() { return axiom.getDepth(); }
	
	
	public NonTerminal getAxiom(){ return this.axiom; }
	
	
	public int getAmbiguity() throws GrammarException {
		int max = 0;
		
		for(NonTerminal nt: this.nonterminals){
			if(nt.equals(this.getAxiom())) {
				continue;
			}
			
			int ambiguity = productions.getProductionsWithRight(nt).size();
			
			if (ambiguity > max) {
				max = ambiguity;
			}
		}
		
		return max;
	}
	public Elements<Terminal> getTerminals() { return this.terminals; }
	public Elements<NonTerminal> getNonTerminals() { return this.nonterminals; }
	public Productions getProductions() { return this.productions; }
}
