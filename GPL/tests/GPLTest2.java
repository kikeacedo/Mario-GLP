package tests;

import java.util.ArrayList;

import dk.itu.mario.level.QuiqueLevel;

public class GPLTest2 {

	public static void main(String[] args) {

		ArrayList<QuiqueLevel> lista = new ArrayList<QuiqueLevel>();

		for(int i  = 0; i < 5; i++){
			QuiqueLevel nivel = new QuiqueLevel(20,150);
			nivel.iniciar(null);
			lista.add(nivel);
		}//for


		double media = 0;

		System.out.println("Nº\tPunt\tLin.\tDen.\t|\tFitness");
		for(int i  = 0; i < lista.size(); i++){
			System.out.println(lista.get(i).getNum() 
					+ "\t" + lista.get(i).getPuntuacion() 
					+ "\t" + round(lista.get(i).calcularLinealidad(),2)
					+ "\t" + round(lista.get(i).calcularDensidad(),2)
					+ "\t|\t" + round(lista.get(i).getFitness(),2));
			
			media += round(lista.get(i).getFitness(),2);
		}//for
		
		System.out.println("MEDIA: " + media/lista.size());
	}//main

	private static double round(double value, int places) {
		if (places < 0) throw new IllegalArgumentException();

		long factor = (long) Math.pow(10, places);
		value = value * factor;
		long tmp = Math.round(value);
		return (double) tmp / factor;
	}//round
}//class
