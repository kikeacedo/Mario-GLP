package tests;

import static org.junit.Assert.*;

import java.util.StringTokenizer;

import org.junit.Test;

import grammar.Derivation;
import grammar.Grammar;
import grammar.GrammarException;

public class GPLtest {
	
	Derivation d;
	
	Grammar g;
	
	public GPLtest(){
		try {
			g = new Grammar("Grammars/gramaticaMario.gr");
		} catch (GrammarException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void test() throws GrammarException {
		for (int i=0; i < 20; i++){
			d = new Derivation(g, 50);
			assertNotNull(d);
			StringTokenizer st = new StringTokenizer(d.getSpaceWord(), "%");
			while(st.hasMoreTokens())
				System.out.println(st.nextToken());
			System.out.println("---------------------");
		}		
	}

}
