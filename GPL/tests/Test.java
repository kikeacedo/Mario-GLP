package tests;

import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.StringTokenizer;

import grammar.Derivation;
import grammar.Grammar;
import grammar.GrammarException;

public class Test {

	public static void main(String[] args) {
		Derivation d;
		Grammar g;
		ArrayList<String> componentes = new ArrayList<String>();


		try {
			g = new Grammar("Grammars/gramaticaMario.gr");

			for (int i=0; i < 1; i++){
				d = new Derivation(g, 50);
				assertNotNull(d);
				StringTokenizer st = new StringTokenizer(d.getSpaceWord(), ";");
				while(st.hasMoreTokens())
					componentes.add(st.nextToken());
				
			}//for
			
			for(int i = 0; i < componentes.size(); i++){
				System.out.println(componentes.get(i));
			}//for
			
			crearNivel(componentes);

			System.exit(0);

		} catch (GrammarException e) {
			e.printStackTrace();
		}//try-catch

	}//main

	
	private static void crearNivel(ArrayList<String> componentes){
		
	}//crearNivel
}//class
