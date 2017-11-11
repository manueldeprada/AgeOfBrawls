/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ageofbrawls.contenido;

import ageofbrawls.plataforma.Mapa;
import ageofbrawls.plataforma.Posicion;
import java.util.Objects;

/**
 *
 * @author Santiago
 */
public class Personaje {

    public final static int PAISANO = 1, SOLDADO = 2;
    private int tipo, salud, armadura, ataque, capRec, cantRec;
    private Posicion posicion;
    private boolean estaMuerto;
    private String nombre;

    public Personaje(int tipo, Posicion posicion, String nombre) {
        if (tipo == 1 || tipo == 2) {
            this.tipo = tipo;
            this.posicion = posicion;
            this.nombre = nombre;
            if (tipo == Personaje.SOLDADO) {
                salud = 100;
                armadura = 200;
                ataque = 30;
                estaMuerto = false;
                cantRec = -1;
                capRec = -1;
            } else {
                salud = 50;
                armadura = 100;
                ataque = -1;
                cantRec = 0;
                capRec = 50;
                estaMuerto = false;
            }
        } else {
            System.out.println("Error seteando tipo");
        }
    }

    public int getTipo() {
        return tipo;
    }

    public int getSalud() {
        return salud;
    }

    public int getArmadura() {
        return armadura;
    }

    public int getAtaque() {
        return ataque;
    }

    public int getCapRec() {
        return capRec;
    }

    public int getCantRec() {
        return cantRec;
    }

    public Posicion getPosicion() {
        return posicion;
    }

    public void setCapRec(int valor) {
        if (valor > 0 && this.tipo == Personaje.PAISANO) {
            capRec = valor;
        } else {
            System.out.println("Error: capacidad introducida errónea");
        }
    }

    public void setCantRec(int valor) {
        if (valor > 0 && this.tipo == Personaje.PAISANO) {
            cantRec = valor;
        } else {
            System.out.println("Error: capacidad introducida errónea");
        }
    }

    public void setPosicion(Posicion posicion) {
        if (posicion != null) {
            this.posicion = posicion;
        } else {
            System.out.println("Error: posicion introducida errónea");
        }
    }

    public String getNombre() {
        return nombre;
    }

    public void describirPersonaje() {
        if (tipo == Personaje.SOLDADO) {
            System.out.println("Nombre: " + nombre);
            System.out.println("Salud :" + salud);
            System.out.println("Armadura :" + armadura);
            System.out.println("Ataque :" + ataque);
        } else {
            System.out.println("Nombre: " + nombre);
            System.out.println("Salud :" + salud);
            System.out.println("Capacidad de recolección :" + capRec);
            System.out.println("Cantidad de Recursos que lleva:" + cantRec);
        }
    }

    private void mover(Mapa mapa, Posicion posicion) {
        if (mapa.perteneceAMapa(posicion) && mapa.getCelda(posicion).esCeldaLibre()) {
            mapa.getCelda(this.posicion).removePersonaje(this);
            this.posicion = posicion;
            mapa.getCelda(posicion).addPersonaje(this);
            mapa.makeAdyVisible(posicion);
        } else {
            System.out.println("Error: No te puedes mover a esa celda.");
        }
    }

    public void mover(Mapa mapa, String direccion) {
        mover(mapa, posicion.get(direccion));
    }

    private void recolectar(Mapa mapa, int direccion) {
        if (tipo == Personaje.PAISANO) {
            if (capRec > 0) {
                if (!mapa.getCelda(posicion.get(direccion)).esCeldaLibre() && mapa.getCelda(posicion.get(direccion)).getEdificio() == null) {
                    if (this.getCantRec() + mapa.getCelda(posicion.get(direccion)).getContenedorRec().getCantidad() <= this.getCapRec()) {
                        this.setCantRec(this.getCantRec() + mapa.getCelda(posicion.get(direccion)).getContenedorRec().getCantidad());
                        mapa.getCelda(posicion.get(direccion)).getContenedorRec().setCantidad(0);
                        mapa.getCelda(posicion.get(direccion)).getContenedorRec().setTipo(0);

                    } else {
                        this.setCantRec(this.getCantRec() + (this.getCapRec() - this.getCantRec()));
                    }
                    mapa.getCelda(posicion.get(direccion)).getContenedorRec().setCantidad((mapa.getCelda(posicion.get(direccion)).getContenedorRec().getCantidad()) - (this.getCapRec() - this.getCantRec()));
                } else {
                    System.out.println("Error: No te puedes mover a esa celda.");
                }

            } else {
                System.out.println("El personaje no puede recolectar más");
            }

        } else {
            System.out.println("Error: Un soldado no puede recolectar");
        }
    }

    public void recolectar(Mapa mapa, String direccion) {

        switch (direccion.toLowerCase()) {
            case "norte":
                recolectar(mapa, Posicion.NORTE);
                break;
            case "sur":
                recolectar(mapa, Posicion.SUR);
                break;
            case "este":
                recolectar(mapa, Posicion.ESTE);
                break;
            case "oeste":
                recolectar(mapa, Posicion.OESTE);
                break;
            default:
                System.out.println("Error: direccion no valida.");
        }

    }

    public boolean consEdif(String tipoC, String dir, Mapa mapa) {
        if (tipo == Personaje.PAISANO) {
            Posicion posConstruir = posicion.get(dir);
            if (posConstruir.equals(posicion) || !mapa.perteneceAMapa(posConstruir) || !mapa.getCelda(posConstruir).esCeldaLibre()) { //direccion no valida
                System.out.println("Error: No se puede contruir en la celda de destino.");
                return false;
            }
                switch (tipoC) {
                    case "casa":
                        Edificio edif = new Edificio(Edificio.CASA,posConstruir,"casa"+mapa.contarEdificios(Edificio.CASA)+1);
                        mapa.getCelda(posConstruir).setEdificio(edif);
                        return true;
                    case "cuartel":
                        Edificio cuart = new Edificio(Edificio.CASA,posConstruir,"cuartel"+mapa.contarEdificios(Edificio.CUARTEL)+1);
                        mapa.getCelda(posConstruir).setEdificio(cuart);
                        return true;
                    default:
                        System.out.println("Error: tipos de construccion incorrecta.");
                }
            
        } else {
            System.out.println("Error: Un soldado no puede construir edificios");
        }
        return false;
    }

    public void almacenar() {
        if (tipo == Personaje.PAISANO) {

        } else {
            System.out.println("Error: Un soldado no puede almacenar recursos ");
        }
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + this.tipo;
        hash = 37 * hash + this.salud;
        hash = 37 * hash + this.armadura;
        hash = 37 * hash + this.ataque;
        hash = 37 * hash + this.capRec;
        hash = 37 * hash + this.cantRec;
        hash = 37 * hash + Objects.hashCode(this.posicion);
        hash = 37 * hash + (this.estaMuerto ? 1 : 0);
        hash = 37 * hash + Objects.hashCode(this.nombre);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Personaje other = (Personaje) obj;
        if (this.tipo != other.tipo) {
            return false;
        }
        if (this.salud != other.salud) {
            return false;
        }
        if (this.armadura != other.armadura) {
            return false;
        }
        if (this.ataque != other.ataque) {
            return false;
        }
        if (this.capRec != other.capRec) {
            return false;
        }
        if (this.cantRec != other.cantRec) {
            return false;
        }
        if (this.estaMuerto != other.estaMuerto) {
            return false;
        }
        if (!Objects.equals(this.nombre, other.nombre)) {
            return false;
        }
        if (!Objects.equals(this.posicion, other.posicion)) {
            return false;
        }
        return true;
    }
}
