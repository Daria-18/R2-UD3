import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class Cantante{
	@Column (name="Artista")
	String nombre;
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	String nacionalidad;
	LocalDate fechaDebut;
	@Enumerated(EnumType.STRING)
	Estado estado;
	
	@OneToMany (cascade=CascadeType.ALL, mappedBy="cantante", fetch=FetchType.EAGER)
	Set<Album> discografia =new HashSet<Album>();
	
	public enum Estado{enActivo,retirada,retirado}

	public Cantante() {
		super();
	}
	public Cantante(String nombre, String nacionalidad,LocalDate fechaDebut,Estado estado) {
		super();
		setNombre(nombre);
		setNacionalidad(nacionalidad);
		setFechaDebut(fechaDebut);
		setEstado(estado);
	}
	
	public void addAlbum(Album a) {
		a.setCantante(this);
		discografia.add(a);
	}
	
	public String getNombre() {return nombre;}
	public void setNombre(String nombre) {this.nombre = nombre;}
	
	
	public String getNacionalidad() {return nacionalidad;}
	public void setNacionalidad(String nacionalidad) {this.nacionalidad=nacionalidad;}
	

	public LocalDate getFechaDebut() {return fechaDebut;}
	public void setFechaDebut(LocalDate fechaDebut) {this.fechaDebut = fechaDebut;}

	public Estado getEstado() {return estado;}
	public void setEstado(Estado estado) {this.estado = estado;}
	
	
	
	public Long getId() {return id;}
	public void setId(Long id) {this.id = id;
	}
	public Set<Album> getDiscografia() {return discografia;}
	public void setDiscografia(Set<Album> discografia) {this.discografia = discografia;}
	
	@Override
	public String toString() {
		return "Cantante [nombre=" + nombre + ", id=" + id + ", nacionalidad=" + nacionalidad + ", fechaDebut="
				+ fechaDebut + ", estado=" + estado + "]";
	}
	
	
	
}

