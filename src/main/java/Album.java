

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="Album")
public class Album {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	Long id;
	String nombre;
	@ManyToOne
	Cantante cantante;
	@Column(name="TotalCanciones")
	int numCanciones;
	@Column(columnDefinition = "TIME(0)")
	LocalTime duracion;
	/*
	@ManyToMany(mappedBy="GENERO")
	Set<Album> generos=new HashSet<Album>();
	*/
	LocalDate fechaLanzamiento;
	
	public Album() {}
	
	public Album(String nombre, int numCanciones, LocalTime duracion,LocalDate fechaLanzamiento) {
		super();
		this.nombre = nombre;
		this.numCanciones = numCanciones;
		this.duracion = duracion;
		this.fechaLanzamiento = fechaLanzamiento;
	}
	public String getNombre() {return nombre;}
	public void setNombre(String nombre) {this.nombre = nombre;}
	
	public Cantante getCantante() {return cantante;}
	public void setCantante(Cantante cantante) {this.cantante = cantante;}
	
	public int getNumCanciones() {return numCanciones;}
	public void setNumCanciones(int numCanciones) {this.numCanciones = numCanciones;}
	
	public LocalTime getDuracion() {return duracion;}
	public void setDuracion(LocalTime duracion) {this.duracion = duracion;}
		
	public LocalDate getFechaLanzamiento() {return fechaLanzamiento;}
	public void setFechaLanzamiento(LocalDate fechaLanzamiento) {this.fechaLanzamiento = fechaLanzamiento;}
	
	@Override
	public String toString() {
		return "Album [nombre=" + nombre + ", cantante=" + cantante + ", numCanciones=" + numCanciones + ", duracion="
				+ duracion + ", fechaLanzamiento=" + fechaLanzamiento + "]";
	}
	
	
	
	
	
}

