package GeneticAlgorithm;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * @author Quique
 * @date 25/11/2015
 * @version 1.0
 */

import java.util.ArrayList;     // arrayLists are more versatile than arrays
import java.util.Random;
import java.util.Scanner;

import dk.itu.mario.engine.PlayGPL;
import dk.itu.mario.level.QuiqueLevel;
import grammar.GPL;

/**
 * QuiqueLeveltic Algorithm sample class <br/>
 * <b>The goal of this GA sample is to maximize the number of capital letters in a String</b> <br/>
 * compile using "javac QuiqueLevelticAlgorithm.java" <br/>
 * test using "java QuiqueLevelticAlgorithm" <br/>
 *
 */

public class GeneticAlgorithm{
	/** CONSTANTES **/
	static int POPULATION_SIZE=200;

	/** VARIABLES **/

	/**
	 * The population contains an ArrayList of QuiqueLevels (the choice of arrayList over
	 * a simple array is due to extra functionalities of the arrayList, such as sorting)
	 */
	static ArrayList<QuiqueLevel> mPopulation;

	static QuiqueLevel mejorIndividuo;

	public ArrayList<QuiqueLevel> getPopulation(){
		return mPopulation;
	}//getPopulation

	/** METODOS **/

	/**
	 * Creates the starting population of QuiqueLevel classes, whose chromosome contents are random
	 * @param size: The size of the popultion is passed as an argument from the main class
	 */
	public GeneticAlgorithm(int size){
		// initialize the arraylist and each QuiqueLevel's initial weights HERE
		mPopulation = new ArrayList<QuiqueLevel>();
		for(int i = 0; i < size; i++){
			QuiqueLevel nivel = new QuiqueLevel(20,150);
			nivel.iniciar(null);
			mPopulation.add(nivel);

			//System.out.println("\tINDIVIDUO " + i + " random añadido");
		}//for
		//System.out.println("INICIALIZACION TERMINADA");
	}//Constructor

	public static void main( String[] args ){

		Scanner in = new Scanner(System.in);

		boolean fin = false;
		System.out.println("Bienvenidos a MARIO BROS");
		while(!fin){
			System.out.println("_______________________________");
			System.out.println("1) Generar y jugar\n2) Jugar desde archivo\n3) Salir");
			int seleccion = in.nextInt();
			switch (seleccion) {
			case 1:
				generar();
				break;

			case 2:
				jugar();
				break;

			case 3:
				fin = true;
				break;

			default:
				break;
			}
		}//while
		
		in.close();

	}//main


	private static void jugar(){

		String cadena;
		FileReader f;
		ArrayList<String> componentes = new ArrayList<String>();
		
		try {
			f = new FileReader("Grammars/generada.txt");
			BufferedReader b = new BufferedReader(f);
			
			while((cadena = b.readLine())!=null)
				componentes.add(cadena);
		
			b.close();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		QuiqueLevel nivel = new QuiqueLevel(20, 150);
		nivel.iniciarPorComponentes(componentes);

		mejorIndividuo = nivel;
		PlayGPL juego = new PlayGPL();
		juego.jugar();
	}//jugar

	// QuiqueLeveltic Algorithm maxA testing method
	public static void generar(){
		// Initializing the population (we chose 500 QuiqueLevels for the population,
		// but you can play with the population size to try different approaches)
		//System.out.println("START");
		GeneticAlgorithm population = new GeneticAlgorithm(POPULATION_SIZE);
		int QuiqueLevelrationCount = 0;
		// For the sake of this sample, evolution goes on forever.
		// If you wish the evolution to halt (for instance, after a number of
		//   QuiqueLevelrations is reached or the maximum fitness has been achieved),
		//   this is the place to make any such checks

		double avgFitness = 0;
		double minFitness;
		double maxFitness;
		QuiqueLevel bestIndividuo = null;

		while( avgFitness < 300){

			avgFitness=0.f;
			minFitness=Float.POSITIVE_INFINITY;
			maxFitness=Float.NEGATIVE_INFINITY;
			bestIndividuo = null;
			// --- evaluate current QuiqueLevelration:
			//System.out.println("\tEVALUANDO QuiqueLevelRACION "+ QuiqueLevelrationCount);
			//System.out.println("\tQuiqueLevelRACION "+ QuiqueLevelrationCount + " EVALUADA");

			// --- print results here:
			// we choose to print the average fitness,
			// as well as the maximum and minimum fitness
			// as part of our progress monitoring
			String bestIndividual="";

			for(int i = 0; i < population.size(); i++){
				QuiqueLevel individuo_aux = population.getLevel(i); 
				double currFitness = individuo_aux.getFitness();
				avgFitness += currFitness;

				if(currFitness < minFitness){
					minFitness = currFitness;
				}//if

				if(currFitness > maxFitness){
					maxFitness = currFitness;
					bestIndividual = population.getLevel(i).levelToString();
					bestIndividuo = individuo_aux;
				}//if
			}//for

			if(population.size()>0){ 
				avgFitness = avgFitness/population.size(); 
			}//if

			String output = "Generation: " + QuiqueLevelrationCount;
			output += "\t AvgFitness: " + round(avgFitness,2);
			output += "\t MinFitness: " + round(minFitness,2);
			output += "\t MaxFitness: " + round(maxFitness,2) + ":\n" + bestIndividual;


			System.out.println(output);
			// produce next QuiqueLevelration:
			//System.out.println("\t\tPRODUCIENDO "+ (QuiqueLevelrationCount+1));
			population.produceNextGeneration(bestIndividuo);
			//System.out.println("\t\tQuiqueLevelRACION "+ (QuiqueLevelrationCount+1) + " PRODUCIDA");

			QuiqueLevelrationCount++;
		}//while

		GPL.toFile(bestIndividuo.getComponentes());
		mejorIndividuo = bestIndividuo;
		PlayGPL juego = new PlayGPL();
		juego.jugar();
	}//main

	/**
	 * With each QuiqueLevel's fitness as a guide, chooses which QuiqueLevels should mate and produce offspring.
	 * The offspring are added to the population, replacing the previous QuiqueLevelration's QuiqueLevels either
	 * partially or completely. The population size, however, should always remain the same.
	 * If you want to use mutation, this function is where any mutation chances are rolled and mutation takes place.
	 */
	public void produceNextGeneration(QuiqueLevel best){
		// use one of the offspring techniques suggested in class (also applying any mutations) HERE
		ArrayList<QuiqueLevel> nuevaGeneracion;
		ArrayList<QuiqueLevel> descendencia = new ArrayList<QuiqueLevel>();

		QuiqueLevel individuo1, individuo2;
		final double PORCENTAJE_MUTACION = 3.00; 
		//		int count = 0;
		// Generar el doble individuos (el doble del tamaño de la población)
		while(descendencia.size() < POPULATION_SIZE*3/4){
			boolean reproducido = false;	
			//			count++;
			while(!reproducido){
				//System.out.println("\t\tREPRODUCCION " + count);
				individuo1 = seleccionarIndividuo(mPopulation);
				individuo2 = seleccionarIndividuo(mPopulation);

				if(individuo1.equals(individuo2)){
					reproducido = true;
					QuiqueLevel[] individuos = individuo1.reproduce(individuo2);
					//System.out.println("\t\tREPRODUCCION " + count  + " TERMINADA");

					double random;
					for(int i = 0; i < individuos.length; i++){
						//llamamos a mutate (mutara si el random es menor que el PORCENTAJE de MUTACION
						random = Math.random()*10;
						if(random < PORCENTAJE_MUTACION)
							individuos[i].mutate();
						//lo guardamos en la lista de QuiqueLevels nuevos
						descendencia.add(individuos[i]);
						//System.out.println("\t\t\t INDIVIDUO " + descendencia.size() + " METIDO");
					}//for
				}//if
			}//while2
		}//while

		// Seleccionamos los individuos que formaran la nueva QuiqueLevelracion
		nuevaGeneracion = seleccionarNuevaGeneracion(mPopulation, descendencia, mPopulation.size(),best);

		mPopulation = nuevaGeneracion;
	}//produceNextQuiqueLevelration

	/**
	 * Metodo que devuelve un arraylist con los individuos de la seleccion de la lista de actual y de la nueva
	 * Mezcla las 2 listas, y luego va seleccionando, utilizando la seleccion por torneo, individuos hasta llenar
	 * la nueva lista de individuos
	 * @param actual
	 * @param nueva
	 * @return lista con los individuos de la nueva QuiqueLevelracion
	 */
	private ArrayList<QuiqueLevel> seleccionarNuevaGeneracion(ArrayList<QuiqueLevel> actual, ArrayList<QuiqueLevel> descendencia, int tamano, QuiqueLevel best){
		ArrayList<QuiqueLevel> nueva = new ArrayList<QuiqueLevel>();

		//Meto el mejor
		nueva.add(best);

		//Meto la descendencia
		for(int i = 0; i < descendencia.size(); i++)
			nueva.add(descendencia.get(i));

		//Selecciono cuales de la anterior siguen
		//		int count = 1;
		while(nueva.size() != tamano){
			QuiqueLevel individuo = seleccionarIndividuo(actual);
			nueva.add(individuo);
			actual.remove(individuo);
			//System.out.println("\t\tINDIVIDUO " + count + " METIDDO EN LA NUEVA QuiqueLevelRACION");
			//count++;
		}//while

		return nueva;
	}//seleccionarNuevaQuiqueLevelracion



	/**
	 * Metodo que selecciona por torneo un individuo
	 * Seleccion por torneo. Escojo 4 random y se enfrentan entre si
	 *	 A: Ind1 vs Ind2  |
	 *                    |---->  IndA vs Ind1 |----> Ganador
	 *   B: Ind3 vs Ind4  |
	 *   
	 * @return individuo ganador
	 */
	private QuiqueLevel seleccionarIndividuo(ArrayList<QuiqueLevel> poblacion){

		QuiqueLevel ganador;

		int individuo1 = 0;
		int individuo2 = 0;
		int individuo3 = 0;
		int individuo4 = 0;

		// Para que no sean iguales los numeros aleatorios
		while(!sonDistintos(individuo1, individuo2, individuo3, individuo4)){
			Random rand = new Random();
			individuo1 = rand.nextInt(poblacion.size());
			individuo2 = rand.nextInt(poblacion.size());
			individuo3 = rand.nextInt(poblacion.size());
			individuo4 = rand.nextInt(poblacion.size());
		}//while

		QuiqueLevel genA = mPopulation.get(best(individuo1, individuo2, poblacion));
		QuiqueLevel genB = mPopulation.get(best(individuo3, individuo4, poblacion));

		ganador = (genA.getFitness() > genB.getFitness()) ? genA : genB;

		return ganador;
	}//torneo


	/**
	 * Metodo que devuelve el maximo entre dos numeros dados
	 * @param a
	 * @param b
	 * @return maximo entre a y b
	 */
	private int best(int a, int b, ArrayList<QuiqueLevel> poblacion){
		return (poblacion.get(a).getFitness() > poblacion.get(b).getFitness()) ? a : b;
	}//max

	/**
	 * Metodo para saber si hay algun numero repetido. Se usara a la hora de generar los randoms
	 * @param a
	 * @param b
	 * @param c
	 * @param d
	 * @return true si son todos distintos
	 * @return false si alguno es igual a otro
	 */
	private boolean sonDistintos(int a, int b, int c, int d){
		boolean sonDistintos = true;

		if(a == b || a == c || a == d || b == c || b == d || c == d)
			sonDistintos = false;

		return sonDistintos;
	}//sonDistintos


	/**
	 * @return the size of the population
	 */
	public int size(){ 
		return mPopulation.size(); 
	}//size()


	/**
	 * Returns the QuiqueLevel at position <b>index</b> of the mPopulation arrayList
	 * @param index: the position in the population of the QuiqueLevel we want to retrieve
	 * @return the QuiqueLevel at position <b>index</b> of the mPopulation arrayList
	 */
	public QuiqueLevel getLevel(int index){ 
		return mPopulation.get(index); 
	}//getQuiqueLevel

	private static double round(double value, int places) {
		if (places < 0) throw new IllegalArgumentException();

		long factor = (long) Math.pow(10, places);
		value = value * factor;
		long tmp = Math.round(value);
		return (double) tmp / factor;
	}//round

	public static QuiqueLevel getBestIndividuo(){
		return mejorIndividuo;
	}
}//class
