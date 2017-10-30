/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ageofbrawls;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

/**
 *
 * @author mprad
 */
public class Mapa {

    private ArrayList<ArrayList<Celda>> mapa;
    private int filas;
    private int columnas;

    public Mapa(int filas, int columnas) {
        mapa = new ArrayList<>();
        for (int i = 0; i < filas; i++) {
            ArrayList<Celda> b = new ArrayList<>();
            for (int j = 0; j < columnas; j++) {
                b.add(j, new Celda());
            }
            mapa.add(i, b);
        }
        this.filas = filas;
        this.columnas = columnas;
    }

    public void inicializar() {
        for (int i = 0; i < mapa.size(); i++) {
            for (int j = 0; j < mapa.get(0).size(); j++) {
                if (i % 2 == 0 && j % 2 == 0) {
                    if ((i == filas - 2 && j == 0) || (i == 0 && j == columnas - 2) || (i == 0 && j == 0) || (i == filas - 2 && j == columnas - 2))//si estamos en las esquinas
                    {
                        
                    } else if (j > 1 && i % 4 == 0) {
                        if (getCelda(i, j - 2).getContenedorRec().getTipo() == ContenedorRecurso.PRADERA)
                            this.makeBloqueRec(i, j);
                    } else if (j % 4 == 0 && i % 4 == 2) 
                        this.makeBloqueRec(i, j);
                }
            }
        }
        this.makeAdyPrad((mapa.size() - 1) / 2, (mapa.size() - 1) / 2);
        Posicion ciudadela = new Posicion((mapa.size() - 1) / 2, (mapa.size() - 1) / 2);
        this.getCelda(ciudadela).setEdificio(Edificio.CIUDADELA);
        //this.putCelda(new Celda(Edificio.CIUDADELA), (mapa.size() - 1) / 2, (mapa.size() - 1) / 2);

    }

    

    private void makeAdyPrad(int i, int j) {
        for (int h = i - 1; h < i + 3; h++) {
            for (int k = j - 1; k < j + 3; k++) {
                this.putCelda(new Celda(), h, k);
            }
        }

    }

    private void makeBloqueRec(int i, int j) {
        int b = 0;
        ArrayList<Integer> bloque = new ArrayList<>(3);
        for (int k = 1; k < 4; k++) {
            bloque.add(k);
        }
        Collections.shuffle(bloque);
        Random rt = new Random();
        Celda[] celdasA = {new Celda(bloque.get(0), rt.nextInt(100 - 1 + 1) + 1), new Celda(bloque.get(1), rt.nextInt(100 - 1 + 1) + 1), new Celda(bloque.get(2), rt.nextInt(100 - 1 + 1) + 1), new Celda()};
        ArrayList<Celda> celdas = new ArrayList(Arrays.asList(celdasA));
        Collections.shuffle(celdas);
        this.putCelda(celdas.get(0), i, j);
        this.putCelda(celdas.get(1), i + 1, j);
        this.putCelda(celdas.get(2), i, j + 1);
        this.putCelda(celdas.get(3), i + 1, j + 1);

    }

    public Mapa() {
        this(10, 10);
    }

    public void putCelda(Celda celda, int i, int j) {
        ArrayList<Celda> fila = mapa.get(i);
        fila.set(j, celda);
        mapa.set(i, fila);
    }

    public Celda getCelda(int i, int j) {
        return mapa.get(i).get(j);
    }
    public Celda getCelda(Posicion posicion){
        return mapa.get(posicion.getX()).get(posicion.getY());
    }

    public boolean esCeldaVacia(int i, int j) {
        return (mapa.get(i).get(j) == null); //expresion logica= true si celda==null
    }

    public void imprimir() {
        for (int i = 0; i < columnas; i++) {
            System.out.print("───");
        }
        System.out.println();

        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                System.out.print(mapa.get(i).get(j));
            }
            System.out.print("│");
            System.out.println();
            for (int j = 0; j < columnas; j++) {
                System.out.print("───");
            }
            System.out.println();

        }

        System.out.println();
    }
}
