package grammar.util;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import jdsl.core.api.Position;
import jdsl.core.api.PositionIterator;
import jdsl.core.api.Tree;
import jdsl.core.ref.NodeTree;
import grammar.Element;
import grammar.GrammarException;
import grammar.Production;

public final class Trees {
	
	public static int depth(Tree t, Position node){
		Collection<Position> cProcess;
		Collection<Position> cChilds;
		int num;
		int level = -1;
		
		if(node != null) {
			
			level++;
			
			/* Loads the node's childs in order. */
			num = t.numChildren(node);
			cProcess = new LinkedList<Position>();
			for(int i=0; i< num; i++){
				cProcess.add(t.childAtRank(node, i));
			}
			
			cChilds = new LinkedList<Position>();
			while(!cProcess.isEmpty()){
								
				level ++;
				
				for(Position p: cProcess){
					
					/* Load the childs of the actual node (p) in order. */
					num = t.numChildren(p);
					for(int i=0; i< num; i++){
						cChilds.add(t.childAtRank(p, i));
					}
				}
				
				cProcess = cChilds;
				cChilds = new LinkedList<Position>();
			}
		}
		return level;
	}
	
	/**
	 * 
	 * The function returns the level (starting at 1) at which the node "node" is located in the m-Tree t.
	 * 
	 * The function MUST be invoked with the parameter "level" = 0 (in order that the level returned starts at
	 * 1).
	 * 
	 */
	public static int depthBackwards(Tree t, Position node, int level){
		if(node != null){
			level++;
			if(!t.isRoot(node)){
				level = Trees.depthBackwards(t, t.parent(node), level);
			}
		}
		
		return level;
	}	
	
	
	/** 
	 * 
	 * 
	 * The function duplicates the Tree "t" starting at the position "node". The duplicate is load
	 * in the tree "result".  The call to the function must be whith an empty "result" (ie, the
	 * tree must be created with the tree's constructor function without parameters. The goal is
	 * to pass the tree by reference, then it must be not NULL.), and a NULL parameter "node2" 
	 * (it is used in the recursion that the funcion makes).
	 * 
	 *  
	 **/	
	public static void duplicateTree(Position node, Tree t, Tree result, Position parent) throws GrammarException{
		
		Production prod;
		Element el;
		Position pos;
		int num;
		Collection<Position> childs;
		
		if((t != null) && (node!=null)){
			
			if(result == null){
				result = new NodeTree();
			}
						
			/* Base step. Terminal node */
			if(t.isExternal(node)){
				if(parent == null){
					pos = result.root();
				}else{
					if(result.isExternal(parent)){
						pos = result.insertFirstChild(parent, null);
					}else{
							pos = result.insertLastChild(parent, null);
					}
				}
				el = (Element)node.get("Element");
				prod = (Production)node.get("Production");
				
				if(el==null){
					throw new GrammarException("The node doesn´t has a valid Production or Element associated");
				}
				pos.set("Production", prod);
				pos.set("Element", el);
				
			}
			/* Recursive step. */
			else{
				if(parent == null){
					pos = result.root();

				}else{
					if(result.isExternal(parent)){
							pos = result.insertFirstChild(parent, null);
					 }else{
							pos = result.insertLastChild(parent, null);	
					}
				}
				el = (Element)node.get("Element");
				prod = (Production)node.get("Production");
				
				if(el==null || (prod== null && !t.isRoot(node))){
					throw new GrammarException("The node doesn´t has a valid Production or Element associated");
				}
				pos.set("Production", prod);
				pos.set("Element", el);

				num = t.numChildren(node);
				childs = new LinkedList<Position>();
				
				for(int i=0; i< num; i++){
					childs.add(t.childAtRank(node, i));
				}
				
				/* Recursive call over children */
				for(Position p: childs){
					duplicateTree(p, t, result, pos);
				}
			}
		}
	}

	
	/**
	 * 
	 * Given a node (po) at the level "level" in the m-Tree t, the function returns
	 * the absolute rank of the node (po).
	 * 
	 * The function returns -1 if isn't possible return a result.
	 * @throws GrammarException 
	 * 
	 */
	public static int getAbsoluteRank(Tree t, Position po, int level, int depth) throws GrammarException{
		
		Position node = null;
		Collection<Position> cProcess;
		Collection<Position> cChilds;
		int num;
		int level2;
		int rank = -1;
		
		if((t != null)&&(po!=null)){
			
			if((level > depth) || (level<0)){
				throw new GrammarException("Unexpected error, the reached level exceeds the maximum depth");
			}
			
			level2 = 1;
			node = t.root();
			
			if(level2 == level){
				return 0;
			}
			
			/* Loads the childs of the root in order. */
			num = t.numChildren(node);
			cProcess = new LinkedList<Position>();
			for(int i=0; i< num; i++){
				cProcess.add(t.childAtRank(node, i));
			}
			
			cChilds = new LinkedList<Position>();
			while(!cProcess.isEmpty()){
								
				level2 ++;
				if(level2==level){
					num = 0;
					for(Position p: cProcess){
						if(p.equals(po)){
							return num;
						}
						num++;
					}
				}
				
				for(Position p: cProcess){
					
					/* Load the childs of the actual node in order. */
					num = t.numChildren(p);
					for(int i=0; i< num; i++){
						cChilds.add(t.childAtRank(p, i));
					}
				}
				
				cProcess = cChilds;
				cChilds = new LinkedList<Position>();
			}
		}
		return rank;
	}


	public static Position getPosition(Tree t, int pos, int level, int depth){
		
		Position node = null;
		Collection<Position> cProcess;
		Collection<Position> cChilds;
		int num;
		int level2;
		
		if((t != null)){
			
			if((level > depth)|| (level<0) || (pos<0)){
				return null;
			}
			
			level2 = 1;
			node = t.root();
			
			if(level2 == level){
				if(pos==0){
					return node;
				}else{
						return null;
				}
			}
			
			/* Loads the childs of the root in order. */
			num = t.numChildren(node);
			cProcess = new LinkedList<Position>();
			for(int i=0; i< num; i++){
				cProcess.add(t.childAtRank(node, i));
			}
			
			cChilds = new LinkedList<Position>();
			while(!cProcess.isEmpty()){
								
				level2 ++;
				if(level2==level){
					if(pos<cProcess.size()){
						num = 0;
						for(Position p: cProcess){
							if(num==pos){
								return p;
							}
							num++;
						}
					}else{
							return null;
					}
					
				}
				
				for(Position p: cProcess){
					
					/* Load the childs of the actual node in order. */
					num = t.numChildren(p);
					for(int i=0; i< num; i++){
						cChilds.add(t.childAtRank(p, i));
					}
				}
				
				cProcess = cChilds;
				cChilds = new LinkedList<Position>();
			}
		}
		return node;
	}

	
	/**
	 * 
	 * The function returns a Hash (Map), where for each Grammar's Element (the Element is the Hash's key), are
	 * holded (as a LinkedList) the nodes (position) where the Element appears in the Derivation (m-Tree).
	 * 
	 */
	public static Map<String, LinkedList<Position>> updateMap(Tree t){
		Position node;
		Collection<Position> cProcess;
		Collection<Position> cChilds;
		int num;
		Map<String, LinkedList<Position>> m = null;
		LinkedList<Position> l;
		Element e;
		
		if(t != null){
			
			m = new ConcurrentHashMap<String, LinkedList<Position>>();
			node = t.root();
			
			/* Process the root. */
			e = (Element)node.get("Element");
			if(m.containsKey(e.getSymbol())){
				l = m.get(e.getSymbol());
				l.add(node);
				m.put(e.getSymbol(), l);
			}else{
					l = new LinkedList<Position>();
					l.add(node);
					m.put(e.getSymbol(), l);
			}
			
			/* Loads the childs of the root in order. */
			num = t.numChildren(node);
			cProcess = new LinkedList<Position>();
			for(int i=0; i< num; i++){
				cProcess.add(t.childAtRank(node, i));
			}
			
			cChilds = new LinkedList<Position>();
			while(!cProcess.isEmpty()){
								
				
				for(Position p: cProcess){
					
					/*Process the actual node. */
					e = (Element)p.get("Element");
					if(m.containsKey(e.getSymbol())){
						l = m.get(e.getSymbol());
						l.add(p);
						m.put(e.getSymbol(), l);
					}else{
							l = new LinkedList<Position>();
							l.add(p);
							m.put(e.getSymbol(), l);
					}
					
					/* Load the childs of the actual node in order. */
					num = t.numChildren(p);
					for(int i=0; i< num; i++){
						cChilds.add(t.childAtRank(p, i));
					}
				}
				
				cProcess = cChilds;
				cChilds = new LinkedList<Position>();
			}
		}
		
		return m;
	}
	
	public static int rankOfChild(Tree tree, Position pos) {
		int rank = 0;
		PositionIterator it = tree.children(tree.parent(pos));
		
		while(it.hasNext()) {
			if(it.nextPosition().equals(pos)) {
				break;
			}
			rank++;
		}
		return rank;
	}

}

