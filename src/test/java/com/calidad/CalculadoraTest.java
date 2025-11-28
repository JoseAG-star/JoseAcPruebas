package com.calidad;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import com.unittest.calculadora.calculadora; 

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class CalculadoraTest {
    
    public double operador1;
    public double operador2;
    public calculadora calc; 

    @BeforeEach
      @SuppressWarnings("unused")
    void setup() { 
        operador2 = 5;
        calc = new calculadora(); 
        System.out.println("Inicializando...");
    }

    @AfterEach
    public void cleanUp(){
        System.out.println("Prueba finalizada!");
    }
// Test: testSumaNumerosPositivos
// Este test verifica que el método de suma adicione correctamente dos números positivos (operador1 y operador2).
// Se espera un resultado de 15.0 
    @Test
        void testSumaNumerosPositivos(){
            //Inicializar datos
            double resultadoEsperado = 15;

            //Ejercitar el código
            double resultado = calc.suma(operador1, operador2);

            //Verificar
            assertThat(resultadoEsperado, is(resultado));
        }
       // Test: testRestaNumerosPositivos
// Este test verifica que el método de resta sustraiga correctamente el operador2 del operador1.
// Se espera un resultado de 5.0 
    @Test
        void testRestaNumerosPositivos(){
            //Inicializar datos
            double resultadoEsperado = 5;

            //Ejercitar el código
            double resultado = calc.resta(operador1, operador2);

            //Verificar
            assertThat(resultadoEsperado, is(resultado));
        }
       // Test: testMultiplicarNumerosPositivos
// Este test verifica que el método de multiplicación calcule el producto de dos números.
// Se espera un resultado de 50.0. 
    @Test
        void testMultiplicarNumerosPositivos(){
            //Inicializar datos
            double resultadoEsperado = 50;

            //Ejercitar el código
            double resultado = calc.multiplica(operador1, operador2);

            //Verificar
            assertThat(resultadoEsperado, is(resultado));
        }
// Test: testDivideNumerosPositivos
// Este test verifica que el método de división realice el cociente correctamente.
// Se espera un resultado de 2.0.
    @Test
    void testDivideNumerosPositivos(){
        double resultadoEsperado = 2;
        double resultado = calc.divide(operador1, operador2);
        assertThat(resultadoEsperado, is(resultado));
    }
}