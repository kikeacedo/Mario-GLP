package grammar;

/**
 * 
 * Class that offers a concrete implementation of the interface Derivation.
 * 
 * The class encapsulates (hides) the implementation of a derivation of the Grammar (it could be a 
 * Tree or another structure, that structure is encapsulated).
 * 
 **/

import jdsl.core.api.PositionIterator;
import jdsl.core.api.Tree;
import jdsl.core.api.Position;
import jdsl.core.ref.NodeTree;
import jdsl.core.ref.PreOrderIterator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import grammar.util.Trees;

public class Derivation {
	
	
	/** The holder of the derivation of the Grammar. */
	private Tree derivation;
	
	/** For each element, map to a list with the Positions where appears in the Derivation. */
	
	private Map<String, LinkedList<Position>> map;
	
	/** Holds the Grammar related to the Derivation. */
	private Grammar grammar;
	
	/** Max depth allowed for the derivation */
	private int maxDepth;
	
	/** Current depth of derivation */
	private int depth;
	
	/**
	 * 
	 * Returns a new Grammar's Derivation, using the symbol nt as the starting point, that is: the Productions
	 * that have nt as the left symbol are selected, and one of them is selected in a random way. Next, the
	 * Non Terminals at the right side of the Production selected, are used as starting point, an so on, until
	 * only Terminal symbols are generated and the Derivation is complete.
	 * 
	 * The method getRandomDerivation (without a maximum depth), isn't implemented because it can consume
	 * the machine's memory. But the method can be simulated with the method getMaxRandomDerivation with
	 * a big value in the parameter maxDepthGlobal.
	 * 
	 * It's important to note that the Derivation generated for the function have a depth's maximum (maxDepthGlobal),
	 * that is: the Productions are selected in a random way, but if the production selected generates a Derivation
	 * that surpasses the maxDepthGlobal, the Production selected and the Derivation generated are discarded and
	 * a new Production is selected.
	 * 
	 * @param maxDepthGlobal
	 * 						 The depth's maximum that a Derivation is allow to reach.
	 * 
	 * 
	 */
	
	public Derivation(Grammar g, int maxDepth) throws GrammarException {
		if(g==null){
			throw new GrammarException("The Grammar is NULL.");
		}
		this.grammar = g;
		this.derivation = null;
		this.map = null;
		this.maxDepth = maxDepth;
		this.depth = -1;
		
		Tree resTree = null;
		Map<String, LinkedList<Position>> resMap;
		int minimumDepth;
		
		if(this.grammar == null){
			throw new GrammarException("The Grammar is NULL. ");
		}
		
		minimumDepth = this.grammar.minimumDepth();
		if( minimumDepth > this.maxDepth){
			throw new GrammarException("The maximum global depth given " + this.maxDepth + " is lower than the minimum depth " + minimumDepth );
		}
		
		resTree = getMaxRandomDerivation(resTree, 0, this.grammar.getAxiom(), null);
		resMap = Trees.updateMap(resTree);
		
		this.setTree(resTree);
		this.setMap(resMap);
	}
	
	
	public Derivation(Grammar g, int maxDepth, Tree t) throws GrammarException {
		if(g==null){
			throw new GrammarException("The Grammar is NULL.");
		}
		this.grammar = g;
		this.derivation = null;
		this.map = null;
		this.maxDepth = maxDepth;
		this.depth = -1;

		if(this.grammar == null){
			throw new GrammarException("The Grammar is NULL. ");
		}
		
		int minimumDepth = this.grammar.minimumDepth();
		if( minimumDepth > this.maxDepth){
			throw new GrammarException("The maximum global depth given " + this.maxDepth + " is lower than the minimum depth " + minimumDepth );
		}

		this.setTree(t);
		this.setMap(Trees.updateMap(t));
	}
	
	/**
	 * 
	 * Returns a new Grammar's Derivation, using the symbol nt as the starting point, that is: the Productions
	 * that have nt as the left symbol are selected, and one of them is selected in a random way. Next, the
	 * Non Terminals at the right side of the Production selected, are used as starting point, an so on, until
	 * only Terminal symbols are generated and the Derivation is complete.
	 * 
	 * The method getRandomDerivation (without a maximum depth), isn't implemented because it can consume
	 * the machine's memory. But the method can be simulated with the method getMaxRandomDerivation with
	 * a big value in the parameter maxDepthGlobal.
	 * 
	 * It's important to note that the Derivation generated for the function have a depth's maximum (maxDepthGlobal),
	 * that is: the Productions are selected in a random way, but if the production selected generates a Derivation
	 * that surpasses the maxDepthGlobal, the Production selected and the Derivation generated are discarded and
	 * a new Production is selected.
	 * 
	 * @param maxDepthGlobal
	 * 						 The depth's maximum that a Derivation is allow to reach.
	 * 
	 * @param knot
	 * 			 The Grammar's Non Terminal symbol used as starting point to generate a new Derivation.
	 * 
	 */
	
	private Tree getMaxRandomDerivation(Tree t, int currentDepth, Element knot, Position parent) throws GrammarException {
		Position pos;
		
		/* Checking possible errors */ 
		if(knot == null){
			throw new GrammarException("The start element is NULL");
		}
		
		if(!(knot instanceof NonTerminal) && !(knot instanceof Terminal)){
				throw new GrammarException("The nodes of the derivation should be a NonTerminal or Terminal");
		}
		
		if(currentDepth > this.maxDepth){
			throw new GrammarException("There was an error an the max depth was " + "crossed. ");
		}
	
		if(t == null){
			t = new NodeTree();
		}
		
		if(parent == null){
			pos = t.root();
		}else{
			/* The childs are added at the order that they are at the right side of
			 * the Production, then theirs rank (rankOfchild) are theirs positions
			 * in the right side of the Production. 
			 * Take care because ranks starts at 0. */
			if(t.isExternal(parent)){
				/* If father doesnt has childs. */
				pos = t.insertFirstChild(parent, null);
			}else{
				/* If father has childs. */
				pos = t.insertLastChild(parent, null);
			}	
		}
		
		/* Base step. Start Element is a terminal*/
		if(knot instanceof Terminal){
			/* p is the Production that generates the node(Position=node). It's only null for the
			 * root node. */
			pos.set("Production", null);
			/* e is the Element associated to the node (Position=node). */
			/* a copy of the terminal is introduced to let diferent internal values for each instance */
			pos.set("Element", new Terminal((Terminal) knot));
		}
		/* Recursive step */
		else{
			Collection<Production> cP = this.grammar.getProductions().getProductionsWithLeft((NonTerminal) knot);
			ArrayList<Production> cP_aux = new ArrayList<Production>();
			Collection<Element> cE;
			Production prod;
			
			for(Production p:cP){
				if((p.getDepth() + currentDepth) <= this.maxDepth){
					cP_aux.add(p);
				}
			}
			
			if(cP_aux.isEmpty()){
				throw new GrammarException("The NonTerminal " + knot.getSymbol() + " doesnt has a Production associated so that 1 + Length(Production)<= " + this.grammar.getAxiom().getDepth());
			}
			
			Random rand = new Random();
			rand.setSeed(System.nanoTime());
			
			/* Gets a random Production of the several that have the NonTerminal e at the left side. */
			prod = cP_aux.get(rand.nextInt(cP_aux.size()));
			cE = prod.getRight();
			if (prod == null || knot == null) {
				throw new GrammarException("The production or element of the position is null");
			}
			/* p is the Production of the node(Position=node).*/
			pos.set("Production", prod);
			/* e is the Element associated to the node (Position=node). */
			pos.set("Element", knot);
			
			/* The childs are added at the order that they are at the right side of
			 * the Production. */
			if(!cE.isEmpty()){
				for(Element ele:cE){
					t=getMaxRandomDerivation(t, currentDepth+1,  ele, pos);
				}
			}
		}
	
		return t;
	}
	
	public Derivation mutate() throws GrammarException {
	    Tree copy = new NodeTree();
        Trees.duplicateTree(this.derivation.root(), this.derivation, copy, null);     
        Derivation dCopy = new Derivation(grammar, maxDepth, copy);
        
	    ArrayList<Position> NT = dCopy.getNonTerminalNodes();
	    Random rand = new Random();
        rand.setSeed(System.nanoTime());
        int pos = rand.nextInt(NT.size());
	    Position mPoint = NT.get(pos);
	    Element symbol = ((Element) mPoint.get("Element"));
	    
	    Tree subtree = new NodeTree();    
	    subtree = this.getMaxRandomDerivation( subtree, dCopy.depthBackwards(mPoint), symbol, null );
	    
	    copy.replaceSubtree(mPoint, subtree);
	    dCopy.setTree(copy);
        dCopy.setMap(Trees.updateMap(copy));
	    
	   return dCopy;
	}

	public void setMaxDepth(int d) throws GrammarException {
		int minimumDepth = this.grammar.minimumDepth();
		if( minimumDepth > d){
			throw new GrammarException("The maximum global depth given " + d + " is lower than the minimum depth " + minimumDepth );
		}
		this.maxDepth = d;
	}
	
	public String getWord(){
		String res = new String();
		if(this.derivation!=null){
			Position n = null;
			Element e = null;
			
			PreOrderIterator poi = new PreOrderIterator(this.derivation);
			while(poi.hasNext()){
				n = (Position) poi.nextObject();
				e = (Element)n.get("Element");
				if (e instanceof Terminal){
					res = res.concat(((Element)n.get("Element")).getSymbol() + "");	
				}					
			}
		}
		return res;
	}
	
	public String getSpaceWord(){
		String res = new String();
		if(this.derivation!=null){
			Position n = null;
			Element e = null;
			
			PreOrderIterator poi = new PreOrderIterator(this.derivation);
			while(poi.hasNext()){
				n = (Position) poi.nextObject();
				e = (Element)n.get("Element");
				if (e instanceof Terminal){
					res = res.concat(((Element)n.get("Element")).getSymbol() + " ");	
				}					
			}
		}
		return res;
	}
	
	
	public Tree getTree(){
		return this.derivation;
	}
	
	
	public void setTree(Tree t){
		this.derivation = t;
	}
	
	
	public Map<String, LinkedList<Position>> getMap(){
		return this.map;
	}
	
	
	public void setMap(Map<String, LinkedList<Position>> m){
		this.map = m;
	}
	

	/**
	 * 
	 * The function has the same behavior that the function "crossoverGBX", and it has been added with the following 
	 * additional feature: it updates the nodes that have an Element with a symbol "s" (those nodes have associated a 
	 * function f and a value v). The node's update is the following: the function f associated to the node is updated
	 * ( f2 =f.updateFunction ), and f2 (that substitutes f in the node), is applied over v ( v2 = f2.applyFuntion[v] ),
	 * and the returning new value v2, substitutes v in the node.
	 * 
	 * 
	 * @param d1
	 * 			The first Derivation (m-Tree) used for the merge.
	 * 
	 * @param d2
	 * 			The second Derivation (m-Tree) used for the merge.
	 * 
	 * @param maxDepthGlobal
	 * 						 The new m-Trees generated by the merge aren't allowed to surpass the depth maxDepthGlobal.
	 * 
	 * @param s
	 * 		    The symbol to be used to select the Derivation's nodes that have associatet the function "f" and the 
	 *          value "v".		
	 *          
	 * @return
	 * 			Two new Trees generated by the merge between the m-Trees d1 and d2.          	 
	 * 
	 * @throws GrammarException
	 * 
	 */
	
	
	public ArrayList<Derivation> crossoverGBX(Derivation spouse) throws GrammarException{
		//Hay que cambiar las reglas de todas las posiciones  que esten en la misma regla hermanas!!!!!!!!!!!!!!!!!!!!!
		ArrayList<Derivation> sons = null;
		ArrayList<Position> d1Ele, d2Ele;
		
		try {
		if(this.derivation==null || spouse.getTree()==null){
			throw new GrammarException("At least one of two parents is null");
		}
		
		d1Ele = this.getNonTerminalNodes();
		d2Ele = spouse.getNonTerminalNodes();
		

		if (d1Ele.isEmpty() || d2Ele.isEmpty()) {
			throw new GrammarException("At least one of two parent have no nodes");
		}
		
		while(sons == null) {
			Position xPoint1 = null;
			Position xPoint2 = null;
			Tree pTree1, pTree2, sTree1, sTree2;
			Random rand = new Random();
			
			rand.setSeed(System.nanoTime());
			xPoint1 = d1Ele.get(rand.nextInt(d1Ele.size()));
			
			if( xPoint1 == null) {
				throw new GrammarException("Unexpected error, crossover point is null.");
			}
			
			/* Production info of parent1 is gathered */
			Production prod1 = (Production) this.getTree().parent(xPoint1).get("Production");
			//int parentRank = this.derivation.rankOfChild(xPoint1);
			int parentRank = Trees.rankOfChild(this.derivation,xPoint1);
			List<Element> rightElements1 = (List<Element>) prod1.getRightCopy();
			
			/* Compatible productions are selected to apply crossover */
			/* Only productions with the same left non terminal are compatible */
			ArrayList<Production> candidateProd = this.grammar.getProductions().getProductionsWithLeft(prod1.getLeft());
			ArrayList<NonTerminal> candidateNT = new ArrayList<NonTerminal>();
			
			//if (prod1.length() != 1) {
				rightElements1.remove(parentRank);
			//}
			for(Production candidate : candidateProd) {
				/*Only productions of the same length and which other right elements are identical are compatible*/
				if ((candidate.length() == prod1.length())) {
					List<Element> rightElements2 = (List<Element>) candidate.getRightCopy();
					Element e = rightElements2.get(parentRank); 
					//if (prod1.length() != 1) {
						rightElements2.remove(parentRank);
					//}
					
					if (rightElements1.equals(rightElements2) && e instanceof NonTerminal) {
						candidateNT.add((NonTerminal) e); //Terminal cannot be nonTerminal
					}
					
				}
			}
			
			/* A Non Terminal is chosen from candidates and all positions of parent2 with that Non Terminal are candidate
			 * for the crossover point */
			if (candidateNT.size() == 0) { continue; }
			ArrayList<Position> candidatePos = new ArrayList<Position>();
			NonTerminal xNT;
			
			if (candidateNT.size() == 1) {
				xNT = candidateNT.get(0);
			} else {
				xNT = candidateNT.get(rand.nextInt(candidateNT.size()-1));
			}

			for (Position p: d2Ele) {
				if (((Element) p.get("Element")).getSymbol().equals(xNT.getSymbol())) {
					candidatePos.add(p);
				}
			}
			
			/* if crossover exceed the maximum depth, another position is selected to apply crossover */
			while(!candidatePos.isEmpty()) {
				int index = 0;
				if (candidatePos.size() != 1) {
					index = rand.nextInt(candidatePos.size()-1);
				}
				xPoint2 = candidatePos.get(index);
				
				List<Element> rightElements2 = (List<Element>) ((Production) spouse.getTree().parent(xPoint2).get("Production")).getRightCopy();
				rightElements2.remove(parentRank);
				
				/* if depth is exceed or the rest of right elements are different the crossover 
				 * point is removed and another one is selected */
				if (this.depthBackwards(xPoint1) + spouse.depth(xPoint2) <= this.maxDepth
						&& spouse.depthBackwards(xPoint2) + this.depth(xPoint1) <= this.maxDepth
						&& rightElements1.equals(rightElements2)) {

						candidatePos.clear();
				} else {
					candidatePos.remove(index);
					continue;
				}
				
				pTree1 = this.getTree();
				pTree2 = spouse.getTree();
				
				sTree1 = new NodeTree();
				Trees.duplicateTree(pTree1.root(), pTree1, sTree1, null);
				
				sTree2 = new NodeTree();
				Trees.duplicateTree(pTree2.root(), pTree2, sTree2, null);

				if((sTree1!=null)&&(sTree2!=null)){
					Tree auxTree1, auxTree2;
					Position p1, p2;
					int level1, absRank1, level2, absRank2;
				
					level1 = this.depthBackwards(xPoint1);
					level1++; // Depht of jdls trees start at 1 instead of 0, as grammar depth does.
					absRank1 = Trees.getAbsoluteRank(pTree1, xPoint1, level1, this.depth()+1);
					p1 = Trees.getPosition(sTree1, absRank1, level1, this.depth()+1);
					
					auxTree1 = new NodeTree();
					Trees.duplicateTree(p1, sTree1, auxTree1, null);

					level2 = spouse.depthBackwards(xPoint2);
					level2++; // Depht of jdls trees start at 1 instead of 0, as grammar depth does.
					absRank2 = Trees.getAbsoluteRank(pTree2, xPoint2, level2, spouse.depth()+1);
					p2 = Trees.getPosition(sTree2, absRank2, level2, spouse.depth()+1);
					
					auxTree2 = new NodeTree();
					Trees.duplicateTree(p2, sTree2, auxTree2, null);

					if((auxTree1 != null) && (auxTree2 != null)){
						Production auxP =  (Production) sTree1.parent(p1).get("Production");
						sTree1.parent(p1).set("Production", sTree2.parent(p2).get("Production"));
						sTree2.parent(p2).set("Production", auxP);
						sTree1.replaceSubtree(p1, auxTree2);
						sTree2.replaceSubtree(p2, auxTree1);
						
						p1 = Trees.getPosition(sTree1, absRank1, level1, this.depth()+1);
						p2 = Trees.getPosition(sTree2, absRank2, level2, this.depth()+1);
								
						sons = new ArrayList<Derivation>(2);
						sons.add(new Derivation(this.grammar, this.maxDepth, sTree1));
						sons.add(new Derivation(this.grammar, this.maxDepth, sTree2));
					}
				}
			}
		}
		
		}catch (Exception e) {
			throw new GrammarException(e.getMessage());
		}
		
		return sons;
	}
	

	public List<Derivation> crossoverWX(Derivation spouse) throws GrammarException{
		ArrayList<Derivation> sons = null;
		ArrayList<Position> d1Ele, d2Ele;
		
		if(this.derivation==null || spouse.getTree()==null){
			throw new GrammarException("At least one of two parents is null");
		}
		
		d1Ele = this.getNonTerminalNodes();
		d2Ele = spouse.getNonTerminalNodes();
		
		if (d1Ele.isEmpty() || d2Ele.isEmpty()) {
			throw new GrammarException("At least one of two parent have no nodes");
		}
		
		while(sons == null) {
			Position xPoint1 = null;
			Position xPoint2 = null;
			Tree pTree1, pTree2, sTree1, sTree2;
			Random rand = new Random();
			
			rand.setSeed(System.nanoTime());
			xPoint1 = d1Ele.get(rand.nextInt(d1Ele.size()));
			
			if( xPoint1 == null) {
				throw new GrammarException("Unexpected error, crossover point is null. ");
			}
			
			ArrayList<Position> candidatePos = new ArrayList<Position>();
			for (Position p: d2Ele) {
				if (((Element) p.get("Element")).getSymbol().equals(((Element) xPoint1.get("Element")).getSymbol())) {
					candidatePos.add(p);
				}
			}
			
			/* if crossover exceed the maximum depth, another position is selected to apply crossover */
			while(candidatePos.size() > 0) {
				int index;
				if (candidatePos.size() == 1) {
					index = 0;
				} else {
					index = rand.nextInt(candidatePos.size()-1);
				}
				xPoint2 = candidatePos.get(index);
				
				/* if depth is exceed the crossover point is removed and another one is selected */
				if (this.depthBackwards(xPoint1) + spouse.depth(xPoint2) > this.maxDepth
						|| spouse.depthBackwards(xPoint2) + this.depth(xPoint1) > this.maxDepth) {
					candidatePos.remove(index);
					continue;
				} else {
					candidatePos.clear();
				}
				
				pTree1 = this.getTree();
				pTree2 = spouse.getTree();
				
				sTree1 = new NodeTree();
				Trees.duplicateTree(pTree1.root(), pTree1, sTree1, null);
				
				sTree2 = new NodeTree();
				Trees.duplicateTree(pTree2.root(), pTree2, sTree2, null);

				if((sTree1!=null)&&(sTree2!=null)){
					Tree auxTree1, auxTree2;
					Position p1, p2;
					int level, absRank;
				
					level = this.depthBackwards(xPoint1);
					level++; // Depht of jdls trees start at 1 instead of 0, as grammar depth does.
					absRank = Trees.getAbsoluteRank(pTree1, xPoint1, level, this.depth()+1);
					p1 = Trees.getPosition(sTree1, absRank, level, this.depth()+1);
				
					auxTree1 = new NodeTree();
					Trees.duplicateTree(p1, sTree1, auxTree1, null);

					level = spouse.depthBackwards(xPoint2);
					level++; // Depht of jdls trees start at 1 instead of 0, as grammar depth does.
					absRank = Trees.getAbsoluteRank(pTree2, xPoint2, level, spouse.depth()+1);
					p2 = Trees.getPosition(sTree2, absRank, level, spouse.depth()+1);
					
					auxTree2 = new NodeTree();
					Trees.duplicateTree(p2, sTree2, auxTree2, null);

					if((auxTree1 != null) && (auxTree2 != null)){
						sTree1.replaceSubtree(p1, auxTree2);
						sTree2.replaceSubtree(p2, auxTree1);
	
						sons = new ArrayList<Derivation>(2);
						sons.add(new Derivation(this.grammar, this.maxDepth, sTree1));
						sons.add(new Derivation(this.grammar, this.maxDepth, sTree2));
					}
				}
			}
		}

		return sons;
	}
	
	
	/**
	 * 
	 * The function prints for each m-Tree's level (from the level 1 to maximum level), the nodes from the 
	 * left to the right (a new level is started when the nodes from the level before are finished). The information
	 * printed for each node is the associated symbol and the production that generates the node.
	 *
	 */
	public void crossByLevels(){
		if(this.derivation!=null){
			Position node;
			Collection<Position> cProcess;
			Collection<Position> cChilds;
			int num;
			int level;
			Production prod;
			
			if(this.derivation != null){
				level = 0;
				node = this.derivation.root();
				
				/* Process the root. */
				System.out.println("Level: " + level);
				level++;
				System.out.println("Root: " + ((Element)node.get("Element")).getSymbol());
				System.out.println();
				
				/* Loads the childs of the root in order. */
				num = this.derivation.numChildren(node);
				cProcess = new LinkedList<Position>();
				for(int i=0; i< num; i++){
					cProcess.add(this.derivation.childAtRank(node, i));
				}
				
				cChilds = new LinkedList<Position>();
				while(!cProcess.isEmpty()){
									
					System.out.println("Level: " + level);
					level ++;
					
					for(Position p: cProcess){
						
						/*Process the actual node. */
						System.out.print(((Element)p.get("Element")).getSymbol());
						System.out.print("  ");
						prod = (Production)p.get("Production");
						if(prod!=null){
							System.out.print("Prod: " + prod.getSymbol());
							System.out.print("  ||  ");
						}
						
						/* Load the childs of the actual node in order. */
						num = this.derivation.numChildren(p);
						for(int i=0; i< num; i++){
							cChilds.add(this.derivation.childAtRank(p, i));
						}
					}
					
					System.out.println();
					System.out.println();
					
					cProcess = cChilds;
					cChilds = new LinkedList<Position>();
				}
			}
		}
	}
	
	
	/**
	 * 
	 * The function prints for each m-Tree's level (from the level 1 to maximum level), the nodes from the 
	 * left to the right (a new level is started when the nodes from the level before are finished). The information
	 * printed for each node is the associated symbol, the function (if the node has one) operator and increment, the
	 * value associated to the node, and the production that generates the node.
	 * 
	 * @param s
	 * 			The node's symbol of the nodes with a Function.
	 *
	 */
	public String crossByLevelsToString(){
		if(this.derivation==null){
			return null;
		}
		
		Position node;
		Collection<Position> cProcess;
		Collection<Position> cChilds;
		int num;
		Production prod;
		String tree = new String("");
		
		if(this.derivation != null){
			node = this.derivation.root();
			
			/* Process the root. */
			tree = tree.concat(((Element)node.get("Element")).getSymbol());
			
			/* Loads the childs of the root in order. */
			num = this.derivation.numChildren(node);
			cProcess = new LinkedList<Position>();
			for(int i=0; i< num; i++){
				cProcess.add(this.derivation.childAtRank(node, i));
			}
			
			cChilds = new LinkedList<Position>();
			while(!cProcess.isEmpty()){
				for(Position p: cProcess){
					/*Process the actual node. */
					tree = tree.concat(((Element)p.get("Element")).getSymbol());
					prod = (Production)p.get("Production");
					if(prod!=null){
						tree = tree.concat(prod.getSymbol());
					}
					
					/* Load the childs of the actual node in order. */
					num = this.derivation.numChildren(p);
					for(int i=0; i< num; i++){
						cChilds.add(this.derivation.childAtRank(p, i));
					}
				}				
				cProcess = cChilds;
				cChilds = new LinkedList<Position>();
			}
		}
		
		return tree;
	}

	
	/**
	 * 
	 * As a Derivation is a m-Tree (a Tree where each node has several nodes), the function returns the 
	 * Derivation's non terminal nodes from left to right.
	 * 
	 * @return
	 * 			A Collection with the Derivation's nodes.
	 * 
	 */
	public ArrayList<Position> getNonTerminalNodes() throws GrammarException {
		ArrayList <Position> nodes = new ArrayList<Position>(this.grammar.getNonTerminals().size()-1);
		PositionIterator it =	derivation.positions();
		
		while(it.hasNext()) {
			Position p = (Position) it.nextObject();
			Element e = ((Element)p.get("Element"));
			if ((e instanceof NonTerminal) && !e.equals(this.grammar.getAxiom())) {
				nodes.add(p);
			}
		}
		
		return nodes;
	}
	
	/**
	 * 
	 * As a Derivation is a m-Tree (a Tree where each node has several nodes), the function returns the 
	 * Derivation's terminal nodes from left to right.
	 * 
	 * @return
	 * 			A Collection with the Derivation's nodes.
	 * 
	 */
	public ArrayList<Terminal> getTerminalNodes() {
		ArrayList <Terminal> nodes = new ArrayList<Terminal>();

		if(this.derivation != null){		
			PreOrderIterator poi = new PreOrderIterator(this.derivation);
			while(poi.hasNext()){
				Position p = (Position) poi.nextObject();
				Element e = (Element) p.get("Element");
				if (e instanceof Terminal){
					nodes.add((Terminal) e);	
				}					
			}
		}
		return nodes;

	}
	
	
	/**
	 * 
	 * The function returns the maximum level (the root is at the level 1) of the m-Tree that is the Derivation.
	 * 
	 * 
	 */
	public int depth() { 
		if (this.depth == -1) {
			this.depth = Trees.depth(this.derivation, this.derivation.root());
		}
		return depth;
	}
	
	private int depth(Position node) { 
		return Trees.depth(this.derivation, node);
	}
	
	/**
	 * 
	 * The function returns the level (starting at 1) at which the node "node" is located in the m-Tree t.
	 * 
	 * The function MUST be invoked with the parameter "level" = 0 (in order that the level returned starts at
	 * 1).
	 * 
	 */
	private int depthBackwards(Position node) {
		int level = -1;
		return Trees.depthBackwards(this.derivation, node, level);
	}

}
