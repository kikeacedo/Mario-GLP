package grammar;


import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Iterator;


/** 
 * 
 * Class that offers a concrete implementation of the interface Elements.
 * 
 * 
 **/

class Elements <E> implements Iterable<E>{

	
	/** The holders of the class. */
	private Map<Integer,E> map;
	
	
	
	/**
	 * 
	 * Empty constructor.
	 *  
	 */
	Elements(){
		this.map = new ConcurrentHashMap<Integer,E>();
	}
	
	
	/** 
	 * 
	 * Return an iterator in order to iterate over the Elements that are in the Elements Object.
	 * 
	 * If Elements is empty returns NULL.
	 * 
	 * 
	 **/
	public Iterator<E> iterator(){
		if(!this.map.isEmpty()){
			return this.map.values().iterator();
		}else{
			   return null;
		}
	}
	
	
	/** 
	 * 
	 * Return the the Elements that are in the Elements object as a Collection.
	 * 
	 * If Elements is empty returns NULL. 
	 * 
	 **/
	public Collection<E> values(){
		if(!this.map.isEmpty()){
			return this.map.values();
		}else{
			   return null;
		}
	}

	
	/** 
	 * 
	 * Adds a Element to the Element object.
	 * 
	 * The method returns NULL if the ID of t doesn´t collide with other ID. If there is a collision, 
	 * then the new t replace the old_t, and the method returns old_t.
	 * 
	 * Throws a GrammarExceptionImpl if the Element is NULL.
	 * 
	 * @param t
	 * 			The Element to be added.
	 * 
	 **/
	public E add(E e) throws GrammarException{
		if((e==null)||(((Element) e).getId()<0)||(((Element) e).getSymbol()==null)){
			throw new GrammarException("The Element is NULL");
		}
		
		return this.map.put(((Element) e).getId(), e);
	}
	
	/** 
	 * 
	 * Return the Element of the Elements object, so that the ID of the Element fulfill
	 * that ID == t.getId(), returns NULL otherwise.
	 * 
	 * @param t
	 * 			The Element to be used as a filter, this only need to set the ID field.
	 * 
	 **/
	public E get(E e){
		E result = null;
		if ((e!=null) && (((Element) e).getId()>=0) && (this.map.containsKey(((Element) e).getId()))){
			result = this.map.get(((Element) e).getId());
		}
		return result;
	}
	
	
	/** 
	 * 
	 * Return the Element of the Elements object, so "id" must be greater than zero, returns NULL otherwise.
	 * 
	 * @param id
	 * 			The Element's id to be used as a filter.
	 * 
	 **/
	public E get(int id){
		E result = null;
		if ((id>=0) && (this.map.containsKey(id))){
			result = (this.map).get(id);
		}
		return result;
	}
	
	
	/** 
	 * 
	 * Return the Element of the Elements object, so "symbol" must be not NULL, returns NULL otherwise.
	 * 
	 * @param id
	 * 			The Element's symbol to be used as a filter.
	 * 
	 **/
	public E get(String symbol){
		if ( symbol != null){
			for (E it: this.values()) {
				if(((Element) it).getSymbol().equals(symbol))
					return it;
			}
		}
		return null;
	}

	/** 
	 * It updates for each Element that belongs to Elements its relation with the Production p.
	 * This is, for each Element its SetRelation method is called with p as argument.  
	 *  
	 * The method should be called when a new Production is added to the Grammar.
	 * 
	 * The method throws a GrammarException if there were any collision. 
	 * 
	 * @param g
	 * 			The Grammar associated to the Elements. 
	 * 
	 *  @param p
	 *  		 The Production that is necessary associate with the Elements symbols of the Grammar.
	 * 
	 *  
	 **/
	public void setRelations(Production p, Grammar g)throws GrammarException {

	}
	
	
	/**
	 * 
	 * Returns TRUE if Elements is empty, FALSE otherwise.
	 * 
	 * */
	public boolean isEmpty(){
		return (this.map).isEmpty();
	}
	
	public int size() { return this.map.size(); }
}
