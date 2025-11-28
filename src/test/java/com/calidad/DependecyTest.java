package com.calidad;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.invocation.InvocationOnMock;

import com.unittest.dependencia.Dependency;
import com.unittest.dependencia.SubDependency;

public class DependecyTest {
    private Dependency dependency;
    private SubDependency subDependency;

    @BeforeEach
    @SuppressWarnings("unused")
    void setUp() throws Exception {
subDependency = mock(SubDependency.class);
dependency = new Dependency(subDependency);
    }
 // Test: testSubDependencyClassName
// Este test verifica que la dependencia real retorne su nombre de clase correcto.
// Se espera un resultado de cadena "SubDependency.class".   
@Test
void testSubDependencyClassName(){
    String esperado = "SubDependency.class";
    String resultadoEjecucion = dependency.getSubdependencyClassName();
    assertThat(resultadoEjecucion,is(esperado));
}
// Test: testSubDependencyClassNameMock
// Este test simula (mock) la respuesta del método getClassName de la subdependencia.
// Se espera que el mock retorne "SubDependency.class" forzadament
@Test
void testSubDependencyClassNameMock(){
    String esperado = "SubDependency.class";

when(subDependency.getClassName()).thenReturn(esperado);

    String resultadoEjecucion = dependency.getSubdependencyClassName();

    assertThat(resultadoEjecucion,is(esperado));
}
// Test: testSumaDos
// Este test simula el comportamiento del método addTwo para que devuelva un valor fijo cuando recibe 10.
// Se espera un resultado de 12.
@Test
void testSumaDos(){
    int esperado = 12;
    when(subDependency.addTwo(10)).thenReturn(12);
    int resultadoEjecucion = subDependency.addTwo(10);
    assertThat(resultadoEjecucion,is(esperado));
}
// Test: testAddTwo
// Este test utiliza 'thenAnswer' para definir una lógica dinámica en el mock (sumar 20 al argumento).
// Se espera un resultado de 120 (el argumento 100 + 20).
@Test
    void testAddTwo(){
        when(subDependency.addTwo(anyInt())).thenAnswer((InvocationOnMock invoction) -> {
            int arg = (Integer) invoction.getArguments()[0];
            return arg +20;
        });
        Integer resultadoEjecucion = subDependency.addTwo(100);
        assertThat(120, is(resultadoEjecucion));
    }
}
