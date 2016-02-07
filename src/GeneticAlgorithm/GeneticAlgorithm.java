package GeneticAlgorithm;

/**
 * @author Quique
 * @date 25/11/2015
 * @version 1.0
 */

import java.util.ArrayList;     // arrayLists are more versatile than arrays
import java.util.Collections;
import java.util.Random;

import dk.itu.mario.level.QuiqueLevel;
import grammar.GPL;

/**
 * Genetic Algorithm sample class <br/>
 * <b>The goal of this GA sample is to maximize the number of capital letters in a String</b> <br/>
 * compile using "javac GeneticAlgorithm.java" <br/>
 * test using "java GeneticAlgorithm" <br/>
 *
 */

public class GeneticAlgorithm{
	/** CONSTANTES **/
	static int CHROMOSOME_SIZE=256*2;
	static int POPULATION_SIZE=10;

	/** VARIABLES **/

	/**
	 * The population contains an ArrayList of genes (the choice of arrayList over
	 * a simple array is due to extra functionalities of the arrayList, such as sorting)
	 */
	static ArrayList<QuiqueLevel> mPopulation;

	public ArrayList<QuiqueLevel> getPopulation(){
		return mPopulation;
	}//getPopulation

	/** METODOS **/

	/**
	 * Creates the starting population of Gene classes, whose chromosome contents are random
	 * @param size: The size of the popultion is passed as an argument from the main class
	 */
	public GeneticAlgorithm(int size){
		// initialize the arraylist and each gene's initial weights HERE
		mPopulation = new ArrayList<QuiqueLevel>();
		for(int i = 0; i < size; i++){
			QuiqueLevel nivel = new QuiqueLevel(20,150);
			nivel.iniciar(null);
			mPopulation.add(nivel);
			
			//System.out.println("\tINDIVIDUO " + i + " random añadido");
		}//for
		//System.out.println("INICIALIZACION TERMINADA");
	}//Constructor


	// Genetic Algorithm maxA testing method
	public static void main( String[] args ){
		// Initializing the population (we chose 500 genes for the population,
		// but you can play with the population size to try different approaches)
		//System.out.println("START");
		GeneticAlgorithm population = new GeneticAlgorithm(POPULATION_SIZE);
		int generationCount = 0;
		// For the sake of this sample, evolution goes on forever.
		// If you wish the evolution to halt (for instance, after a number of
		//   generations is reached or the maximum fitness has been achieved),
		//   this is the place to make any such checks

		double avgFitness = 0;
		double minFitness;
		double maxFitness;
		QuiqueLevel bestIndividuo = null;

		while( avgFitness > 10){

			avgFitness=0.f;
			minFitness=Float.POSITIVE_INFINITY;
			maxFitness=Float.NEGATIVE_INFINITY;
			bestIndividuo = null;
			// --- evaluate current generation:
			//System.out.println("\tEVALUANDO GENERACION "+ generationCount);
			//System.out.println("\tGENERACION "+ generationCount + " EVALUADA");

			// --- print results here:
			// we choose to print the average fitness,
			// as well as the maximum and minimum fitness
			// as part of our progress monitoring
			String bestIndividual="";
			String worstIndividual="";

			for(int i = 0; i < population.size(); i++){
				QuiqueLevel individuo_aux = population.getLevel(i); 
				double currFitness = individuo_aux.getFitness();
				avgFitness += currFitness;

				if(currFitness > minFitness){
					minFitness = currFitness;
					worstIndividual = population.getLevel(i).levelToString();
				}//if

				if(currFitness < maxFitness){
					maxFitness = currFitness;
					bestIndividual = population.getLevel(i).levelToString();
					bestIndividuo = individuo_aux;
				}//if
			}//for

			if(population.size()>0){ 
				avgFitness = avgFitness/population.size(); 
			}//if

			String output = "Generation: " + generationCount;
			output += "\t AvgFitness: " + avgFitness;
			output += "\t MinFitness: " + minFitness;
			output += "\t MaxFitness: " + maxFitness + ":\n" + bestIndividual;


			System.out.println(output);
			// produce next generation:
			//System.out.println("\t\tPRODUCIENDO "+ (generationCount+1));
			population.produceNextGeneration();
			//System.out.println("\t\tGENERACION "+ (generationCount+1) + " PRODUCIDA");

			generationCount++;
		}//while

		GPL.toFile(bestIndividuo.getComponentes());

	}//main

	/**
	 * With each gene's fitness as a guide, chooses which genes should mate and produce offspring.
	 * The offspring are added to the population, replacing the previous generation's Genes either
	 * partially or completely. The population size, however, should always remain the same.
	 * If you want to use mutation, this function is where any mutation chances are rolled and mutation takes place.
	 */
	public void produceNextGeneration(){
		// use one of the offspring techniques suggested in class (also applying any mutations) HERE
		ArrayList<QuiqueLevel> nuevaGeneracion;
		ArrayList<QuiqueLevel> descendencia = new ArrayList<QuiqueLevel>();

		Gene individuo1, individuo2;
		final double PORCENTAJE_MUTACION = 3.00; 
		//		int count = 0;
		// Generar el doble individuos (el doble del tamaño de la población)
		while(descendencia.size() < POPULATION_SIZE*2){
			boolean reproducido = false;	
			//			count++;
			while(!reproducido){
				//System.out.println("\t\tREPRODUCCION " + count);
				individuo1 = seleccionarIndividuo(mPopulation);
				individuo2 = seleccionarIndividuo(mPopulation);

				if(individuo1.getChromosome() != individuo2.getChromosome()){
					reproducido = true;
					Gene[] genes = individuo1.reproduce(individuo2);
					//System.out.println("\t\tREPRODUCCION " + count  + " TERMINADA");

					double random;
					for(int i = 0; i < genes.length; i++){
						//llamamos a mutate (mutara si el random es menor que el PORCENTAJE de MUTACION
						random = Math.random()*10;
						if(random < PORCENTAJE_MUTACION)
							genes[i].mutate();
						//lo guardamos en la lista de genes nuevos
						descendencia.add(genes[i]);
						//System.out.println("\t\t\t INDIVIDUO " + descendencia.size() + " METIDO");
					}//for
				}//if
			}//while2
		}//while

		// Seleccionamos los individuos que formaran la nueva generacion
		nuevaGeneracion = seleccionarNuevaGeneracion(mPopulation, descendencia, mPopulation.size());

		mPopulation = nuevaGeneracion;
	}//produceNextGeneration

	/**
	 * Metodo que devuelve un arraylist con los individuos de la seleccion de la lista de actual y de la nueva
	 * Mezcla las 2 listas, y luego va seleccionando, utilizando la seleccion por torneo, individuos hasta llenar
	 * la nueva lista de individuos
	 * @param actual
	 * @param nueva
	 * @return lista con los individuos de la nueva generacion
	 */
	private ArrayList<QuiqueLevel> seleccionarNuevaGeneracion(ArrayList<QuiqueLevel> actual, ArrayList<QuiqueLevel> descendencia, int tamano){
		ArrayList<QuiqueLevel> nueva = new ArrayList<QuiqueLevel>();
		ArrayList<QuiqueLevel> mezcla = mezclarListas(actual, descendencia);

		//		int count = 1;
		while(nueva.size() != tamano){
			Gene individuo = seleccionarIndividuo(mezcla);
			mezcla.remove(individuo);
			nueva.add(seleccionarIndividuo(mezcla));
			//System.out.println("\t\tINDIVIDUO " + count + " METIDDO EN LA NUEVA GENERACION");
			//			count++;
		}//while

		evaluateGeneration(nueva);
		return nueva;
	}//seleccionarNuevaGeneracion



	/**
	 * Metodo que selecciona por torneo un individuo
	 * Seleccion por torneo. Escojo 4 random y se enfrentan entre si
	 *	 A: Ind1 vs Ind2  |
	 *                    |---->  IndA vs Ind1 |----> Ganador
	 *   B: Ind3 vs Ind4  |
	 *   
	 * @return individuo ganador
	 */
	private Gene seleccionarIndividuo(ArrayList<QuiqueLevel> poblacion){

		Gene ganador;

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

		Gene genA = mPopulation.get(max(individuo1, individuo2, poblacion));
		Gene genB = mPopulation.get(max(individuo3, individuo4, poblacion));

		ganador = (genA.mFitness >= genB.mFitness) ? genA : genB;

		return ganador;
	}//torneo


	/**
	 * Metodo que devuelve el maximo entre dos numeros dados
	 * @param a
	 * @param b
	 * @return maximo entre a y b
	 */
	private int max(int a, int b, ArrayList<QuiqueLevel> poblacion){
		return (poblacion.get(a).mFitness >= poblacion.get(b).mFitness) ? a : b;
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
	 * Metodo que mezcla los elementos de 2 listas y los mete en una lista
	 * @param lista1
	 * @param lista2
	 * @return lista con los elementos de lista1 y lista 2 mezclados
	 */
	private ArrayList<QuiqueLevel> mezclarListas(ArrayList<QuiqueLevel> lista1, ArrayList<QuiqueLevel> lista2){
		lista1.addAll(lista2);
		Collections.shuffle(lista1);
		return lista1;
	}//mezclarLista


	/**
	 * @return the size of the population
	 */
	public int size(){ 
		return mPopulation.size(); 
	}//size()


	/**
	 * Returns the Gene at position <b>index</b> of the mPopulation arrayList
	 * @param index: the position in the population of the Gene we want to retrieve
	 * @return the Gene at position <b>index</b> of the mPopulation arrayList
	 */
	public QuiqueLevel getLevel(int index){ 
		return mPopulation.get(index); 
	}//getGene


}//class
