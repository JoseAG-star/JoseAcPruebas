package com.calidad;
import org.junit.jupiter.api.Test;
import com.unittest.calculadora.calculadora;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;


public class CalculadoraTest {
     public double operador1 = 10;
    public double operador2 = 5;
       public  calculadora calc = new calculadora();
    @BeforeEach
    public void setup(){
        double operador1 = 10;
        double operador2 = 5;
         System.out.println("Inicializando.......");
    }
    @AfterEach
    public void cleanUp(){
        System.out.println("Prueba Finalizada");
    }
    @Test
void testSumaNumerosPositivos(){
 
   double resultadoEsperado = 15;
   

    double resultado = calc.suma(operador1, operador2);

    assertThat(resultadoEsperado, is(resultado));
}
void testRestaNumerosPositivos(){

    double resultadoEsperado = 5;
   

    double resultado = calc.resta(operador1, operador2);


    assertThat(resultadoEsperado, is(resultado));
}

}

