package com.calidad;
import org.junit.jupiter.api.Test;
import com.unittest.calculadora.calculadora;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;


public class CalculadoraTest {
 public double operador1;
    public double operador2;
    public calculadora calc;

    @BeforeEach
        void setip(){
            operador1 = 10;
            operador2 = 5;
            calc = new calculadora();
            System.out.println("Inicializando...");
        }
    @AfterEach
    public void cleanUp(){
        System.out.println("Prueba finalizada!");
    }
    
    @Test
        void testSumaNumerosPositivos(){
            //Inicializar datos
            double resultadoEsperado = 15;

            //Ejercitar el código
            double resultado = calc.suma(operador1, operador2);

            //Verificar
            assertThat(resultadoEsperado, is(resultado));
        }
    @Test
        void testRestaNumerosPositivos(){
            //Inicializar datos
            double resultadoEsperado = 5;

            //Ejercitar el código
            double resultado = calc.resta(operador1, operador2);

            //Verificar
            assertThat(resultadoEsperado, is(resultado));
        }
    @Test
        void testMultiplicarNumerosPositivos(){
            //Inicializar datos
            double resultadoEsperado = 50;

            //Ejercitar el código
            double resultado = calc.multiplica(operador1, operador2);

            //Verificar
            assertThat(resultadoEsperado, is(resultado));
        }

    @Test
        void testDivideNumerosPositivos(){
            //Inicializar datos
            double resultadoEsperado = 2;

            //Ejercitar el código
            double resultado = calc.divide(operador1, operador2);

            //Verificar
            assertThat(resultadoEsperado, is(resultado));
        }
    /*@Test(expected = NullPointerException.class)    
    public void whenExceptionThrown_thenExpectationSatisfied(){
        String test = null;
        test.length();
    }*/

}

