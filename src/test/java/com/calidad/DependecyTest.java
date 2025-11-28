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
@Test
void testSubDependencyClassName(){
    String esperado = "SubDependency.class";
    String resultadoEjecucion = dependency.getSubdependencyClassName();
    assertThat(resultadoEjecucion,is(esperado));
}
@Test
void testSubDependencyClassNameMock(){
    String esperado = "SubDependency.class";

when(subDependency.getClassName()).thenReturn(esperado);

    String resultadoEjecucion = dependency.getSubdependencyClassName();

    assertThat(resultadoEjecucion,is(esperado));
}
@Test
void testSumaDos(){
    int esperado = 12;
    when(subDependency.addTwo(10)).thenReturn(12);
    int resultadoEjecucion = subDependency.addTwo(10);
    assertThat(resultadoEjecucion,is(esperado));
}
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
