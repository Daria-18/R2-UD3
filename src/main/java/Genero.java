import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.JoinColumn;
/*
@Entity
public class Genero {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	String nombre;
	@ManyToMany
	@JoinTable(
			name="album_genero",
			joinColumns=@JoinColumn(name="album_id"),
			inverseJoinColumns=@JoinColumn(name="genero_id")
	)
	private Set<Album> albumes = new HashSet<>();
	
	public Genero() {}
	
	public Genero(String nombre) {
		this.nombre=nombre;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Set<Album> getAlbumes() {
		return albumes;
	}

	public void setAlbumes(Set<Album> albumes) {
		this.albumes = albumes;
	}
	
	

}
*/
