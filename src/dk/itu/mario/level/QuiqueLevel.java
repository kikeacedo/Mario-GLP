package dk.itu.mario.level;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Enrique Acedo
 * @version 1.0
 * @date 12/01/2016
 */

import java.util.Random;
import dk.itu.mario.MarioInterface.LevelInterface;
import dk.itu.mario.engine.sprites.SpriteTemplate;
import grammar.Derivation;
import grammar.GPL;
import grammar.GrammarException;

public class QuiqueLevel extends Level implements LevelInterface{

	/** ATRIBUTOS */
	Random random;
	private int floor;
	private double puntuacion;
	private ArrayList<Integer> altura_actual;
	private ArrayList<Integer> x_actual;
	private ArrayList<Integer> wide_actual;
	private ArrayList<Integer> alturas;
	private ArrayList<Integer> longitudes;
	private int num_elementos;
	private Derivation derivacion;
	private ArrayList<String> componentes;

	/** METODOS */
	public QuiqueLevel(int height, int width) {
		super(width, height);
		random = new Random();
		puntuacion = 0;
		altura_actual = new ArrayList<Integer>();
		altura_actual.add(0);
		x_actual = new ArrayList<Integer>();
		x_actual.add(0);
		wide_actual = new ArrayList<Integer>();
		wide_actual.add(width-10);
		alturas = new ArrayList<Integer>();
		longitudes = new ArrayList<Integer>();
		num_elementos = 0;
	}//constructor

	public void iniciarPorComponentes(ArrayList<String> componentes){
		this.componentes = componentes;

		//Creamos la base
		create();

		//String componente =" platform , 1 3 , 4 6 , 2 0 , 2 0 - platform , 1 3 , 9 6 , 50 , 4 0 ";
		//buildPlatform(componente);
		//Añadimos los componentes
		add(componentes);
	}//crearPorComponentes

	public void iniciar(Derivation d){
		//Cogemos los componentes de la gramatica
		if(d == null)
			derivacion = GPL.crearDerivacion();
		else
			derivacion = d;

		componentes = GPL.getComponentes(derivacion);

		//Creamos la base
		create();

		//		String componente =" monedas , 1 0 , 3 4 , 4 5 , 6 0";
		//		addElement(componente);
		//Añadimos los componentes
		add(componentes);
	}//

	/**
	 * Crea la base del nivel
	 */
	public void create() {
		floor = height - 1;
		xExit = this.width - 10;
		yExit = floor;

		for (int x = 0; x < this.width; x++) {
			for (int y = 0; y < height; y++) {
				if (y > floor) {
					setBlock(x, y, Level.GROUND);	// Not ground you can stand on!
					// Only decoration
				} else if (y == floor) {
					setBlock(x, y, Level.HILL_TOP); // A grassy block that you
					// can stand on.
				}//if-else
			}//for2
		}//for1
	}//create

	/**
	 * Añade los componentes del ArrayList
	 * @param componentes
	 */
	private void add(ArrayList<String> componentes){
		String[] elementos_2;
		for(int i = 0; i < componentes.size(); i++){
			String elementos = removeChar(componentes.get(i), ' ');
			switch (elementos.charAt(0)) {
			case '<':
				elementos_2 = componentes.get(i).split(";");
				for(int j = 0; j < elementos_2.length; j++)
					addSprite(elementos_2[j]);
				num_elementos++;
				break;

			case 'p':
				addElement(componentes.get(i));
				break;

			default:
				elementos_2 = componentes.get(i).split(";");
				for(int j = 0; j < elementos_2.length; j++)
					addElement(elementos_2[j]);
				break;
			}//switch
		}//for1
	}//add

	/**
	 * Añade enemigos
	 * @param componente
	 */
	private void addSprite(String componente){
		componente = removeChar(componente, "<".charAt(0));
		componente = removeChar(componente, ">".charAt(0));
		componente = removeChar(componente, " ".charAt(0));

		String[] elementos = componente.split(",");

		int x = desnormalizar(Integer.parseInt(elementos[0]), wide_actual.get(wide_actual.size()-1)) + x_actual.get(x_actual.size()-1);
		int y = desnormalizar(Integer.parseInt(elementos[1]),13);

		int tipo = getTipo(elementos[2]);
		boolean con_alas = (elementos[3].equals("true")) ? true:false;
		if(tipo >= 0){
			this.setSpriteTemplate(x, floor -3 - y, new SpriteTemplate(tipo, con_alas));
			puntuacion = puntuacion - 1;
			longitudes.add(x);
		}//if
	}//addSprite

	/**
	 * Añade elemento que no sea enemigo
	 * @param componente
	 */
	private void addElement(String componente){
		componente = removeChar(componente, " ".charAt(0));
		String[] elementos = componente.split(",");

		num_elementos++;

		switch (elementos[0]) {
		case "gap":
			buildGap(componente);
			break;

		case "tube":
			buildTube(componente);
			break;

		case "platform":
			buildPlatform(componente);
			break;

		case "cannon":
			buildCannons(componente);
			break;

		case "tube_floor":
			buildTubeFloor(componente);
			break;

		case "bloque_power":
			bluildBloquePower(componente);
			break;

		case "monedas":
			buildCoin(componente);
			break;

		case "bloque":
			buildBloque(componente);
			break;

		case "bloque_monedas":
			buildBloqueCoin(componente);
			break;

		default:
			break;
		}//switch

	}//addSprite

	/**
	 * Construye una moneda
	 * @param componente
	 * @return
	 */
	private int buildCoin(String componente){
		componente = removeChar(componente, " ".charAt(0));
		String[] elementos = componente.split(",");

		int x = desnormalizar(Integer.parseInt(elementos[1]), wide_actual.get(wide_actual.size()-1)) + x_actual.get(x_actual.size()-1);
		int y = transformar_altura(desnormalizar(Integer.parseInt(elementos[2]),3))+altura_actual.get(altura_actual.size()-1);
		int number = desnormalizar(Integer.parseInt(elementos[3]), 15);
		int pisos = desnormalizar(Integer.parseInt(elementos[4]), 4);

		alturas.add(y);

		for(int i = 0; i < number; i++){
			for(int j = 0; j < pisos; j++){
				setBlock(x+i, y-j, Level.COIN);
				longitudes.add(x+i);
			}//for2
		}//for1
		return number;
	}//monedas

	/**
	 * Construye un bloque de monedas
	 * @param componente
	 * @return
	 */
	private int buildBloqueCoin(String componente){
		componente = removeChar(componente, " ".charAt(0));
		String[] elementos = componente.split(",");

		int x = desnormalizar(Integer.parseInt(elementos[1]), wide_actual.get(wide_actual.size()-1)) + x_actual.get(x_actual.size()-1);
		int y = transformar_altura(desnormalizar(Integer.parseInt(elementos[2]),3)+altura_actual.get(altura_actual.size()-1)) - 1 ;
		int number = desnormalizar(Integer.parseInt(elementos[3]), 10);

		alturas.add(y);

		for(int i = 0; i < number; i++){
			setBlock(x+i, y, Level.BLOCK_COIN);
			longitudes.add(x+i);
		}//for1

		return number;
	}//bloque_monedas

	/**
	 * Construye un bloque de ladrillos
	 * @param componente
	 * @return
	 */
	private int buildBloque(String componente){
		componente = removeChar(componente, " ".charAt(0));
		String[] elementos = componente.split(",");

		int x = desnormalizar(Integer.parseInt(elementos[1]), wide_actual.get(wide_actual.size()-1)) + x_actual.get(x_actual.size()-1);
		int y = transformar_altura(desnormalizar(Integer.parseInt(elementos[2]),3)+altura_actual.get(altura_actual.size()-1)) - 1 ;
		int number = desnormalizar(Integer.parseInt(elementos[3]), 10);

		alturas.add(y);

		for(int i = 0; i < number; i++){
			setBlock(x+i, y, Level.BLOCK_EMPTY);
			longitudes.add(x+i);
		}//for1
		return number;
	}//bloque

	/**
	 * Construye un bloque de power
	 * @param componente
	 * @return
	 */
	private int bluildBloquePower(String componente){
		puntuacion = puntuacion + 1;
		componente = removeChar(componente, " ".charAt(0));
		String[] elementos = componente.split(",");

		int x = desnormalizar(Integer.parseInt(elementos[1]), wide_actual.get(wide_actual.size()-1)) + x_actual.get(x_actual.size()-1);
		int y = transformar_altura(desnormalizar(Integer.parseInt(elementos[2]),3)+altura_actual.get(altura_actual.size()-1)) - 1 ;

		alturas.add(y);
		longitudes.add(x);

		setBlock(x, y, Level.BLOCK_POWERUP);

		return 1;
	}//monedas

	/**
	 * Construye un hueco en el suelo
	 * @param componente
	 * @return
	 */
	private int buildGap(String componente){
		puntuacion = puntuacion - 0.5;
		componente = removeChar(componente, " ".charAt(0));
		String[] elementos = componente.split(",");

		int x = desnormalizar(Integer.parseInt(elementos[1]), wide_actual.get(wide_actual.size()-1)) + x_actual.get(x_actual.size()-1);
		int ancho = desnormalizar(Integer.parseInt(elementos[2]),5); //maximo de 5 para que pueda saltarlo

		puntuacion = puntuacion - normalizar(ancho, 5);

		for(int i = 0; i < ancho; i++){
			setBlock(x+i, 19,Level.EMPTY);
			longitudes.add(x+i);
		}//for

		return ancho;
	}//monedas

	/**
	 * Construte una plataforma simple
	 * @param componente
	 * @return
	 */
	private int buildPlatform(String componente) {
		componente = removeChar(componente, ' ');
		String[] elementos = componente.split("-");

		String[] plataforma = elementos[0].split(",");
		int x = desnormalizar(Integer.parseInt(plataforma[1]), wide_actual.get(wide_actual.size()-1)) + x_actual.get(x_actual.size()-1);
		int y = desnormalizar(Integer.parseInt(plataforma[2]), 5) + altura_actual.get(altura_actual.size()-1);
		int wide = desnormalizar(Integer.parseInt(plataforma[3]), wide_actual.get(wide_actual.size()-1));
		String mode = ((Integer.parseInt(plataforma[4])) > 49) ? "left" : "right";

		alturas.add(y);

		for(int i = 0; i < wide; i++)
			longitudes.add(x+i);

		//		System.out.println(x + ", " + y + ", " + wide + ", " + mode);
		crearPlataforma(x, y, wide, mode);

		x_actual.add(x);
		altura_actual.add(y);
		wide_actual.add(wide);

		for(int i = 1; i < elementos.length; i++){
			String[] elementos_2 = elementos[i].split(";");
			ArrayList<String> componentes = new ArrayList<String>();

			for(int j = 0; j < elementos_2.length; j++)
				componentes.add(elementos_2[j]);

			add(componentes);
		}//for

		x_actual.remove(x_actual.size()-1);
		altura_actual.remove(altura_actual.size()-1);
		wide_actual.remove(wide_actual.size()-1);

		return wide;

	}//buildPlatform

	/**
	 * Construye un cañon
	 * @param componente
	 * @return
	 */
	private int buildCannons(String componente) {
		puntuacion = puntuacion - 0.5;
		componente = removeChar(componente, " ".charAt(0));
		String[] elementos = componente.split(",");

		int x = desnormalizar(Integer.parseInt(elementos[1]), wide_actual.get(wide_actual.size()-1)) + x_actual.get(x_actual.size()-1);
		int h = desnormalizar(Integer.parseInt(elementos[2]), 4) + altura_actual.get(altura_actual.size()-1);
		int wb = desnormalizar(Integer.parseInt(elementos[3]), 5);;
		int wa = desnormalizar(Integer.parseInt(elementos[4]), 5);;

		alturas.add(h);
		longitudes.add(x);

		for (int i = x - wb; i < x; i++)
			this.setBlock(i, floor, Level.HILL_TOP);

		for (int i = x + 1; i < x + 1 + wa; i++)
			this.setBlock(i, floor, Level.HILL_TOP);

		for (int j = floor - h - 1; j < floor; j++) {
			if (j == floor - h - 1) {
				setBlock(x, j, Level.CANNON_TOP);
			} else if (j == floor - h) {
				setBlock(x, j, Level.CANNON_GRIP);
			} else
				setBlock(x, j, Level.CANNON_BASE);

		}//for

		return wa + wb + 1;
	}//buildCannons

	/**
	 * Construye un tubo
	 * @param componente
	 * @return
	 */
	private int buildTube(String componente) {
		puntuacion = puntuacion - 0.5;
		componente = removeChar(componente, " ".charAt(0));
		String[] elementos = componente.split(",");

		int x = desnormalizar(Integer.parseInt(elementos[1]), wide_actual.get(wide_actual.size()-1))+ x_actual.get(x_actual.size()-1);
		int h = desnormalizar(Integer.parseInt(elementos[2]), 4) + altura_actual.get(altura_actual.size()-1);
		int wb = desnormalizar(Integer.parseInt(elementos[3]), 5);
		int wa = desnormalizar(Integer.parseInt(elementos[4]), 5);

		alturas.add(h);
		longitudes.add(x);

		for (int i = x - wb; i < x; i++)
			this.setBlock(i, floor, Level.HILL_TOP);
		for (int i = x + 2; i < x + 2 + wa; i++)
			this.setBlock(i, floor, Level.HILL_TOP);

		for (int i = x; i <= x + 1; i++) {
			for (int j = floor - h - 1; j < floor; j++) {
				if (j == floor - h - 1) {
					if (i == x)
						setBlock(i, j, Level.TUBE_TOP_LEFT);
					else
						setBlock(i, j, Level.TUBE_TOP_RIGHT);
				} else {
					if (i == x)
						setBlock(i, j, Level.TUBE_SIDE_LEFT);
					else
						setBlock(i, j, Level.TUBE_SIDE_RIGHT);
				}//if-else
			}//for2
		}//for1

		return wa + wb + 2;
	}//buildTubes

	/**
	 * Construye un tubo con una flor que salta
	 * @param componente
	 * @return
	 */
	private int buildTubeFloor(String componente){
		puntuacion = puntuacion - 1;
		componente = removeChar(componente, " ".charAt(0));
		String[] elementos = componente.split(",");

		int x = desnormalizar(Integer.parseInt(elementos[1]), wide_actual.get(wide_actual.size()-1)) + x_actual.get(x_actual.size()-1);
		int h = desnormalizar(Integer.parseInt(elementos[2]), 4) +altura_actual.get(altura_actual.size()-1);

		this.setSpriteTemplate(x, transformar_altura(h), new SpriteTemplate(SpriteTemplate.JUMP_FLOWER, false));

		return buildTube(componente);
	}//

	/** METODOS AUXILIARES */

	/**
	 * Elimina una substring de un string
	 * @param palabra
	 * @param word
	 * @return
	 */
	@SuppressWarnings("unused")
	private String removeWord(String palabra, String word){
		String result = "";
		if(palabra.contains(word)){
			int start = palabra.indexOf(word);
			result = palabra.substring(0, start-1) + palabra.substring(start+word.length());
		}//if

		return result;
	}//removeWord

	/**
	 * Elimina un caracter de un string
	 * @param palabra
	 * @param letra
	 * @return
	 */
	private String removeChar(String palabra, char letra){
		String result = "";

		char[] aux = palabra.toCharArray();

		for(int i = 0; i < aux.length; i++)
			if(aux[i] != letra)
				result += aux[i];

		return result;
	}//remove

	/**
	 * Devuelve el tipo de enemigo dado un string
	 * @param tipo
	 * @return
	 */
	private int getTipo(String tipo){
		int sprite = -1;

		switch (tipo) {
		case "armored_turtle":
			sprite = SpriteTemplate.ARMORED_TURTLE;
			break;
		case "chomp_flower":
			sprite = SpriteTemplate.CHOMP_FLOWER;
			break;
		case "goompa":
			sprite = SpriteTemplate.GOOMPA;
			break;
		case "green_turtle":
			sprite = SpriteTemplate.GREEN_TURTLE;
			break;
		case "red_turtle":
			sprite = SpriteTemplate.RED_TURTLE;
			break;
		default:
			break;
		}//switch

		return sprite;
	}//getTipo

	/**
	 * Desnormaliza x dado el max
	 * @param x
	 * @param max
	 * @return
	 */
	private int desnormalizar(int x, int max){	
		return x*max/100;
	}//convertToLength

	/**
	 * Normaliza x a 1
	 * @param x
	 * @param max
	 * @return
	 */
	private double normalizar(int x, int max){
		return x/max;
	}//normalizar

	/**
	 * Transfora la altura al valor que hay que pasarle
	 * @param altura
	 * @return
	 */
	private int transformar_altura(int altura){
		return floor - 1 - altura;
	}//transformar

	private void crearPlataforma(int x, int y, int wide, String mode) {
		for (int i = x; i <= x + wide; i++) {
			for (int j = floor - y - 1; j <= floor - 1; j++) {
				if (i == x) {
					if (j == floor - y - 1) {
						if (mode.equals("left"))
							if (this.getBlock(i, j) == Level.EMPTY)
								this.setBlock(i, j, Level.HILL_TOP_LEFT_IN);
							else
								this.setBlock(i, j, this.getBlock(i, j));
						else if (this.getBlock(i, j) == Level.EMPTY)
							this.setBlock(i, j, Level.HILL_TOP_LEFT);
						else
							this.setBlock(i, j, this.getBlock(i, j));
					} else if (this.getBlock(i, j) == Level.EMPTY)
						this.setBlock(i, j, Level.HILL_LEFT);
					else
						this.setBlock(i, j, this.getBlock(i, j));
				} else if (i == x + wide) {
					if (j == floor - y - 1) {
						if (mode.equals("right"))
							if (this.getBlock(i, j) == Level.EMPTY)
								this.setBlock(i, j, Level.HILL_TOP_RIGHT_IN);
							else
								this.setBlock(i, j, this.getBlock(i, j));
						else if (this.getBlock(i, j) == Level.EMPTY)
							this.setBlock(i, j, Level.HILL_TOP_RIGHT);
						else
							this.setBlock(i, j, this.getBlock(i, j));
					} else if (this.getBlock(i, j) == Level.EMPTY)
						this.setBlock(i, j, Level.HILL_RIGHT);
					else
						this.setBlock(i, j, this.getBlock(i, j));
				} else {
					if (j == floor - y - 1) {
						if (this.getBlock(i, j) == Level.EMPTY)
							this.setBlock(i, j, Level.HILL_TOP);
						else
							this.setBlock(i, j, this.getBlock(i, j));
					} else if (this.getBlock(i, j) == Level.EMPTY)
						this.setBlock(i, j, Level.HILL_FILL);
					else
						this.setBlock(i, j, this.getBlock(i, j));
				}//if-else
			}//for2
		}//for1
	}//crearPlataforma

	public double calcularLinealidad(){
		double result = 0;
		double altura_media = getAlturaMedia();

		for(int i= 0; i < alturas.size(); i++)
			result += Math.abs((altura_media - alturas.get(i)));

		return result/alturas.size();
	}//calcularLinealidad

	public double calcularDensidad(){
		double result = 0;
		double[] densidades = new double[width];

		for(int i = 0; i < densidades.length; i++){
			int cont = 0;
			for(int j = 0; j < longitudes.size(); j++){
				if(longitudes.get(j) == i)
					cont++;
			}//for2
			densidades[i] = cont;
		}//for1

		for(int i = 0; i < densidades.length; i++)
			result += densidades[i];

		return result/num_elementos;
	}//calcularDensidad

	public double getFitness(){
		double result = 0;
		double densidad = calcularDensidad();
		double linealidad = calcularLinealidad();
		
		if(densidad == 0.0)
			densidad = 0.1;
		
		if(linealidad == 0.0)
			linealidad = 0.1;
		
		if(puntuacion == 0.0)
			puntuacion = 0.1;
			
		result = ( getAlturaMedia() + num_elementos )/ (linealidad + densidad + puntuacion);
		return (result);
	}//calcularFitness


	public QuiqueLevel[] reproduce(QuiqueLevel level){
		QuiqueLevel[] result = null;
		List<Derivation> individuos;
		try {
			individuos = derivacion.crossoverWX(level.getDerivation());
			result = new QuiqueLevel[individuos.size()];

			for(int i = 0; i < individuos.size(); i++){
				QuiqueLevel nivel =  new QuiqueLevel(20, 150);
				nivel.iniciar(individuos.get(i));
				result[i] = nivel;
			}//for

		} catch (GrammarException e) {
		}
		return result;
	}//reproduce

	public void mutate(){
		try {
			derivacion.mutate();
		} catch (GrammarException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}//mutate


	/** GETTERS AND SETTERS */
	public double getPuntuacion(){
		return puntuacion;
	}//getPuntuacion

	public double getAlturaMedia(){
		double result = 0;

		for(int i= 0; i < alturas.size(); i++)
			result += alturas.get(i);

		return result/alturas.size();
	}//getAlturaMedia

	public int getNum(){
		return num_elementos;
	}//getNum

	public ArrayList<String> getComponentes(){
		return componentes;
	}//getComponentes

	public Derivation getDerivation(){
		return derivacion;
	}//getDerivation

	public String levelToString() {
		String result = "";
		for(int i = 0; i < componentes.size(); i++){
			result+=componentes.get(i);
		}//for
		return result;
	}//genotipoToString
}//class
