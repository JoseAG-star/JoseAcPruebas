package com.calidad;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import com.unittest.calculadora.Calculadora; 

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class CalculadoraTest {
    
    public double operador1;
    public double operador2;
    public Calculadora calc; 

    @BeforeEach
      @SuppressWarnings("unused")
    void setup() { 
        operador2 = 5;
        calc = new Calculadora(); 
        System.out.println("Inicializando...");
    }

    @AfterEach
    public void cleanUp(){
        System.out.println("Prueba finalizada!");
    }
    
    @Test
    void testSumaNumerosPositivos(){
        double resultadoEsperado = 15;
        double resultado = calc.suma(operador1, operador2);
        assertThat(resultadoEsperado, is(resultado));
    }

    @Test
    void testRestaNumerosPositivos(){
        double resultadoEsperado = 5;
        double resultado = calc.resta(operador1, operador2);
        assertThat(resultadoEsperado, is(resultado));
    }

    @Test
    void testMultiplicarNumerosPositivos(){
        double resultadoEsperado = 50;
        double resultado = calc.multiplica(operador1, operador2);
        assertThat(resultadoEsperado, is(resultado));
    }

    @Test
    void testDivideNumerosPositivos(){
        double resultadoEsperado = 2;
        double resultado = calc.divide(operador1, operador2);
        assertThat(resultadoEsperado, is(resultado));
    }
}