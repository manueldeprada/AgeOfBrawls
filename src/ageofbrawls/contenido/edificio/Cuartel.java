/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ageofbrawls.contenido.edificio;

import ageofbrawls.contenido.Personajes.Personaje;
import ageofbrawls.contenido.Personajes.Soldado;
import ageofbrawls.plataforma.Civilizacion;
import ageofbrawls.plataforma.Posicion;
import ageofbrawls.z.excepciones.Argumentos.ExcepcionArgumentosInternos;
import ageofbrawls.z.excepciones.Recursos.EscasezRecursos.EscasezRecursosCreacion;
import ageofbrawls.z.excepciones.Recursos.EscasezRecursos.ExcepcionEspacioInsuficiente;

/**
 *
 * @author Santiago
 */
public final class Cuartel extends Edificio {

    public Cuartel(Posicion posicion, String nombre, Civilizacion civilizacion) throws ExcepcionArgumentosInternos {
        super(posicion, nombre, civilizacion, 500, 5);//salud, capaloj
    }

    @Override
    public void crearPersonaje() throws ExcepcionEspacioInsuficiente, EscasezRecursosCreacion {
        Civilizacion civilizacion=super.getCivilizacion();
        if (civilizacion.getPersonajes().size() >= civilizacion.contarEdificios(Casa.class) * Casa.CAPALOJ) {
            throw new ExcepcionEspacioInsuficiente("No hay suficiente espacio para crear personajes",civilizacion.getPersonajes().size());
        }
        if (civilizacion.getAlimentos() < 100) {
            throw new EscasezRecursosCreacion("No hay suficientes recursos para crear un personaje.",100-civilizacion.getAlimentos());
        }
        Posicion pos = super.getPosicion().posicionAdyacenteLibre(civilizacion.getMapa());
        int i = 1;
        String nombrePers = "soldado1";
        while (civilizacion.getPersonajes().containsKey(nombrePers)) {
            nombrePers = nombrePers.replace("soldado" + i, "soldado" + (++i));
        }
        Personaje person = new Soldado(pos, nombrePers, civilizacion);
        civilizacion.getMapa().getCelda(pos).addPersonaje(person);
        civilizacion.getPersonajes().put(person.getNombre(), person);
        civilizacion.getMapa().getCelda(pos).setOculto(civilizacion, false);
        civilizacion.setAlimentos(-100, true);
        civilizacion.makeAdyVisible(pos);
        System.out.println();
        civilizacion.getMapa().imprimirCabecera();
        civilizacion.getMapa().imprimir(civilizacion);
        System.out.println("Coste de creacion: 100 unidades de comida");
        System.out.println("Te quedan " + ((civilizacion.contarEdificios(Casa.class) * Casa.CAPALOJ) - civilizacion.getPersonajes().size()) + " unidades de capacidad de alojamiento");
        System.out.println("Se ha creado " + person.getNombre() + " en la celda de " + pos);
    }

    @Override
    public int getMaxVida() {
        return 500;
    }

    @Override
    public String toString() {
        return "cuartel";
    }

}
