/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ageofbrawls.plataforma;

import ageofbrawls.contenido.ContenedorRecurso;
import ageofbrawls.contenido.Edificio;
import ageofbrawls.contenido.Personaje;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;

/**
 *
 * @author mprad
 */
public class Mapa {

    private ArrayList<ArrayList<Celda>> mapa;
    private int filas;
    private int columnas;
    HashMap<String, Personaje> personajes;
    HashMap<String, Edificio> edificios;

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";
    public static final String ANSI_BLACK_BACKGROUND = "\u001B[40m";
    public static final String ANSI_RED_BACKGROUND = "\u001B[41m";
    public static final String ANSI_GREEN_BACKGROUND = "\u001B[42m";
    public static final String ANSI_YELLOW_BACKGROUND = "\u001B[43m";
    public static final String ANSI_BLUE_BACKGROUND = "\u001B[44m";
    public static final String ANSI_PURPLE_BACKGROUND = "\u001B[45m";
    public static final String ANSI_CYAN_BACKGROUND = "\u001B[46m";
    public static final String ANSI_WHITE_BACKGROUND = "\u001B[47m";
    public static final String GREEN_BACKGROUND_BRIGHT = "\033[0;102m";// GREEN

    public Mapa(int filas, int columnas) {
        if (filas > 0 && columnas > 0) {
            mapa = new ArrayList<>();
            edificios = new HashMap<>();
            personajes = new HashMap<>();
            for (int i = 0; i < filas; i++) {
                ArrayList<Celda> b = new ArrayList<>();
                for (int j = 0; j < columnas; j++) {
                    b.add(j, new Celda(i, j, true));
                }
                mapa.add(i, b);
            }
            this.filas = filas;
            this.columnas = columnas;
        } else {
            System.out.println("Error construyendo Mapa.");
        }
    }

    public Mapa(int filas, boolean inicializar) { //Inicializo mapa cuadrado con elementos
        this(filas, filas);
        int columnas = filas;
        if (inicializar) {
            for (int i = 0; i < mapa.size(); i++) { //numero de filas, i recorriendo filas,i=y
                for (int j = 0; j < mapa.get(0).size(); j++) { //columnas, j j=x
                    if (i % 2 == 0 && j % 2 == 0) {
                        if (j > 1 && i % 4 == 0) {
                            if (getCelda(i, j - 2).getContenedorRec().getTipo() == ContenedorRecurso.PRADERA) {
                                this.makeBloqueRec(i, j);
                            }
                        } else if (j % 4 == 0 && i % 4 == 2) {
                            this.makeBloqueRec(i, j);
                        }
                    }
                }
            }

            this.makeAdyPrad((mapa.size() - 1) / 2, (mapa.size() - 1) / 2);
            Posicion posCiudadela = new Posicion((mapa.size() - 1) / 2, (mapa.size() - 1) / 2);
            String nombre = "Ciudadela-1";
            Edificio ciud = new Edificio(Edificio.CIUDADELA, posCiudadela, nombre);
            this.getCelda(posCiudadela).setEdificio(ciud);
//            int i=1;
//            while(!edificios.containsKey(nombre)){
//                nombre = nombre.replace("-"+i, "-"+(++i));
//            }
            edificios.put(nombre, ciud);
        }
    }

    public void inicializar(String paisano) {
        Posicion posPaisano = edificios.get("Ciudadela-1").getPosicion().PosicionAdyacenteLibre(this);
        Personaje paisano1 = new Personaje(Personaje.PAISANO, posPaisano, paisano);
        personajes.put(paisano, paisano1);
        this.getCelda(posPaisano).addPersonaje(paisano1);
        this.makeAdyVisible(posPaisano);
    }
    
    public void listarPersonajes (){
        Set <Map.Entry<String,Personaje>> pers= personajes.entrySet();
        for(Map.Entry<String,Personaje> entry : pers){
            System.out.println(entry.getKey() + entry.getValue().getPosicion());
            
        }
    }
    public void listarEdificios(){
        Set <Map.Entry<String,Edificio>> pers= edificios.entrySet();
        for(Map.Entry<String,Edificio> entry : pers){
            System.out.println(entry.getKey() + entry.getValue().getPosicion());
            
        }
    }
    
    public void mirar(Celda celda){
        if(this.getCelda(celda.getPosicion()).getEdificio()!= null){
            this.getCelda(celda.getPosicion()).getEdificio().describirEdificio();//En caso de que en la celda haya un edificio, lo describimos
        }
        else if (this.getCelda(celda.getPosicion()).getContenedorRec().getTipo()!= ContenedorRecurso.PRADERA){
            this.getCelda(celda.getPosicion()).getContenedorRec().describirContenedorRecurso();//En caso de estar en un contenedor de Recursos, imprimimos su descripción
        }
        if(this.getCelda(celda.getPosicion()).getContenedorRec().getTipo()==ContenedorRecurso.PRADERA && celda.getPersonajes() != null){
            this.getCelda(celda.getPosicion()).getPersonajes().get(0).describirPersonaje();//enseñamos la info del 0, ya que es el único de momento que se puede alamcenar en la celda
        }
        
    }

    public void makeAdyVisible(Posicion posicion) {
        int i = posicion.getX(), j = posicion.getY();
        for (int h = i - 1; h < i + 2; h++) {
            for (int k = j - 1; k < j + 2; k++) {
                if (this.getCelda(h,k)!=null && (h == i || j == k || (this.getCelda(h, k).getEdificio() != null && this.getCelda(h, k).getEdificio().getTipo() == Edificio.CIUDADELA))) {
                    this.getCelda(h, k).setOculto(false);
                }
            }
        }
    }

    private void makeAdyPrad(int i, int j) { //Hacer todas las celdas asyacentes pradera
        for (int h = i - 1; h < i + 2; h++) {
            for (int k = j - 1; k < j + 2; k++) {
                this.getCelda(h, k).getContenedorRec().set(ContenedorRecurso.PRADERA, 0);
            }
        }
    }

    private void makeBloqueRec(int i, int j) {//Hacer un bloque de 4 celdas de recursos a partir de la celda dada
        ArrayList<Integer> bloque = new ArrayList<>(4);
        for (int k = 0; k < 4; k++) {
            bloque.add(k);
        }
        Collections.shuffle(bloque); //Cada int es un recurso, aleatorizo orden
        Random rt = new Random();
        int[] cantidad = new int[]{rt.nextInt(100) + 1, rt.nextInt(100) + 1, rt.nextInt(100) + 1, rt.nextInt(100) + 1};
        Posicion posicion = new Posicion(i, j);
        getCelda(posicion).getContenedorRec().set(bloque.get(0), cantidad[0]);
        getCelda(posicion.get(Posicion.ESTE)).getContenedorRec().set(bloque.get(1), cantidad[1]);
        getCelda(posicion.get(Posicion.SUR)).getContenedorRec().set(bloque.get(2), cantidad[2]);
        getCelda(posicion.get(Posicion.SURESTE)).getContenedorRec().set(bloque.get(3), cantidad[3]);
    }

    public Mapa() {
        this(10, true);
    }

    public Celda getCelda(int x, int y) {
        if (x < columnas && y < filas && x > -1 && y > -1) {
            return mapa.get(y).get(x);
        }
        System.out.println("getCelda devuelve null" + x + y);
        return null;
    }

    public HashMap<String, Personaje> getPersonajes() {
        return personajes;
    }

    public HashMap<String, Edificio> getEdificios() {
        return edificios;
    }
    

    public Celda getCelda(Posicion posicion) {
        if (posicion.getX() < columnas && posicion.getY() < filas && posicion.getX() > -1 && posicion.getY() > -1) {
            return mapa.get(posicion.getY()).get(posicion.getX());
        }
        return null;
    }

    public void imprimir() {
        System.out.print("\r  │");
        for (int i = 0; i < columnas; i++) {
            System.out.print("C" + i + " │");
        }
        System.out.println();
        //Primera linea: numeracion de columna

        for (int i = 0; i < filas; i++) {

            System.out.print("  ");
            for (int j = 0; j < columnas; j++) {
                System.out.print("───");
            }
            System.out.println();
            //Linea de separacion entre filas

            System.out.print("F" + i); //Numeracion de fila
            boolean flagpers = false;
            for (int j = 0; j < columnas; j++) {
                System.out.print(ANSI_RESET + "│" + mapa.get(i).get(j).toString());
                if (!mapa.get(i).get(j).getPersonajes().isEmpty()) {
                    flagpers = true;
                }
            }
            System.out.print(ANSI_RESET + "│");//Ultimo separador de fila
            if (flagpers) {
                for (int j = 0; j < columnas; j++) {
                    if (!mapa.get(i).get(j).getPersonajes().isEmpty()) {
                        System.out.print(" F" + i + ",C" + j + ": ");
                        for (int k = 0; k < mapa.get(i).get(j).getPersonajes().size(); k++) {
                            if (k != 0) {
                                System.out.print(", ");
                            }
                            if (mapa.get(i).get(j).getPersonajes().get(k).getTipo() == Personaje.PAISANO) {
                                System.out.print("paisano " + mapa.get(i).get(j).getPersonajes().get(k).getNombre());
                            } else {
                                System.out.print("soldado " + mapa.get(i).get(j).getPersonajes().get(k).getNombre());
                            }

                        }
                    }
                }
            }
            System.out.println();
            //Linea de mapa
        }
        System.out.println();
    }
}
