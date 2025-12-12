import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.Query;



public class Pruebas {
	private static EntityManagerFactory emf =Persistence.createEntityManagerFactory("Discografia");
    private static EntityManager em = emf.createEntityManager();

    public static void main(String[] args) {
		Scanner sc=new Scanner(System.in);
		int opcion=0;
		String nombre="";
		String nombreAlbum="";
		IniciarTablas();
		
		do {
			System.out.println("Introduce una opción:"+"\n"
					+"1-Buscar artista"+"\n"
					+"2-Añadir artista"+"\n"
					+"3-Eliminar artista"+"\n"
					+"4-Buscar album"+"\n"
					+"5-Añadir album"+"\n"
					+"6-Eliminar album"+"\n"
					+"7-Listar Albumes por Artista"+"\n"
					+"8-SALIR");
					opcion=sc.nextInt();
				
					switch(opcion) {
						case 1:
							sc.nextLine();
							System.out.println("Como se llama el cantante que busca");
							nombre=sc.nextLine();
							Cantante buscado=buscarCantantePorNombre(nombre);
							if(buscado!=null) System.out.println(buscado.toString());
							else System.out.println("Cantante no encontrado");
							break;
						case 2:
							sc.nextLine();
							System.out.println("Introduce el nombre del artista.");
							nombre=sc.nextLine();
							System.out.println("Introduce la nacionalidad.");
							String nacionalidad=sc.next();
							System.out.println("Introduce el año de nacimiento.");
							int año=sc.nextInt();
							System.out.println("Introduce el mes de nacimiento.");
							int mes=sc.nextInt();
							System.out.println("Introduce el dia de nacimiento.");
							int dia=sc.nextInt();
							System.out.println("Introduce 1 si esta en activo, 2 si esta retirado.");
							int estado=sc.nextInt();
							if(estado==1) añadirCantante(new Cantante(nombre, nacionalidad, LocalDate.of(año, mes, dia), Cantante.Estado.enActivo));
							else if(estado==2) añadirCantante(new Cantante(nombre, nacionalidad, LocalDate.of(año, mes, dia), Cantante.Estado.retirado));
							else System.out.println("opcion no valida");
							
							break;
						case 3:
							sc.nextLine();
							System.out.println("Como se llama el cantante que quiere eliminar");
							nombre=sc.nextLine();
							eliminarCantante(nombre);
							break;
						case 4:
							sc.nextLine();
							System.out.println("Como se llama el Album que busca");
							nombreAlbum=sc.nextLine();
							Album albumAuscado=buscarAlbumPorNombre(nombreAlbum);
							if(albumAuscado!=null) System.out.println(albumAuscado.toString());
							else System.out.println("Album no encontrado");
							break;
						case 5:
							sc.nextLine();
							System.out.println("Introduzca el nombre del cantante al que le quiere añadir un album");
							nombre=sc.nextLine();
							Cantante busqueda=buscarCantantePorNombre(nombre);
							System.out.println("Como se llama el album");
							nombreAlbum=sc.next();
							System.out.println("Cuantas canciones tiene");
							int numCanciones=sc.nextInt();
							System.out.println("Cuantas horas dura");
							int horas=sc.nextInt();
							System.out.println("Cuantos minutos dura");
							int min=sc.nextInt();
							System.out.println("año de salida");
							int añoSalida=sc.nextInt();
							System.out.println("Que mes");
							int mesSalida=sc.nextInt();
							System.out.println("Que dia");
							int diaSalida=sc.nextInt();
							añadirAlbumaACantante(busqueda,new Album(nombreAlbum,numCanciones,LocalTime.of(horas,min, 0),LocalDate.of(añoSalida, mesSalida, diaSalida)));
								
							break;
						case 6:
							sc.nextLine();
							System.out.println("Como se llama el cantante que quiere eliminar");
							nombre=sc.nextLine();
							eliminarAlbum(nombre);
							break;
						case 7:
							//System.out.println("Reemplazar");
							listarAlbumesPorArtista();
							break;
						case 8:
							System.out.println("FIN DEL PROGRAMA");
							break;
						default:
							System.out.println("Opción no valida.");
							break;
						
					}
		}while(opcion!=8);
		
		sc.close();
		
		em.close();
		emf.close();
	}
    
    /**
     * Apartado 3.7
     * <br/><br/> 
     * Join Cantantes y Albumes
     */
    
    private static void listarAlbumesPorArtista() {		
    	String consulta = "SELECT c,a FROM Cantante c JOIN c.discografia a";
    	
    	try {
        	em.getTransaction().begin();
        	// Joins devuelven siempre una lista de Objetos genérica.
        	
        	// Para acceder a cada objeto debemos hacer castings en base a la posición
        	// de la lista devuelta
        	Query query = em.createQuery(consulta, Object[].class);
        	List<Object[]> listaAux = query.getResultList();
            em.getTransaction().commit();
            //System.out.println(listaAux.toString());
            
            for(Object[] fila : listaAux) {
            	Cantante c = (Cantante) fila[0];
            	Album a = (Album) fila[1];
            	System.out.println("[Artista] " + c.getNombre() + " - [Album] " + a.getNombre());            	
        	}

        } catch (Exception e) {
            e.printStackTrace();
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
        } 
    	
	}

	/**
     * 
     */
    public static void IniciarTablas() {
		List<Cantante> listaCantantes=asociarCantanteAlbum();
        try {
            for(Cantante cantante:listaCantantes) {
            	em.getTransaction().begin();
            	em.persist(cantante);
            	em.getTransaction().commit();
        	}
            System.out.println("Cantantes añadidos correctamente.");
        } catch (Exception e) {
            e.printStackTrace();
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.err.println("Error!");
        }
	}
    
    /**
     * 
     * @return - Lista de cantantes
     */
    public static List<Cantante> asociarCantanteAlbum() {
		List<Cantante> listaCantantes = new ArrayList<>();
		
	    Cantante taylor = new Cantante("Taylor Swift", "Estadounidense", LocalDate.of(2006, 10, 24), Cantante.Estado.enActivo);
	    taylor.addAlbum(new Album("Taylor Swift", 15, LocalTime.of(0, 53, 0), LocalDate.of(2006, 10, 24)));
	    taylor.addAlbum(new Album("Fearless", 13, LocalTime.of(1, 5, 0), LocalDate.of(2008, 11, 11)));
	    taylor.addAlbum(new Album("Speak Now", 14, LocalTime.of(1, 3, 0), LocalDate.of(2010, 10, 25)));
	    listaCantantes.add(taylor);

	    Cantante ariana = new Cantante("Ariana Grande", "Estadounidense", LocalDate.of(2013, 3, 26), Cantante.Estado.enActivo);
	    ariana.addAlbum(new Album("Yours Truly", 13, LocalTime.of(0, 44, 0), LocalDate.of(2013, 3, 26)));
	    ariana.addAlbum(new Album("My Everything", 14, LocalTime.of(0, 48, 0), LocalDate.of(2014, 8, 25)));
	    ariana.addAlbum(new Album("Dangerous Woman", 13, LocalTime.of(0, 43, 0), LocalDate.of(2016, 5, 20)));
	    listaCantantes.add(ariana);

	    Cantante gaga = new Cantante("Lady Gaga", "Estadounidense", LocalDate.of(2008, 4, 8), Cantante.Estado.enActivo);
	    gaga.addAlbum(new Album("The Fame", 14, LocalTime.of(0, 50, 0), LocalDate.of(2008, 8, 19)));
	    gaga.addAlbum(new Album("The Fame Monster", 8, LocalTime.of(0, 32, 0), LocalDate.of(2009, 11, 18)));
	    gaga.addAlbum(new Album("Born This Way", 14, LocalTime.of(1, 0, 0), LocalDate.of(2011, 5, 23)));
	    listaCantantes.add(gaga);

	    Cantante johnny = new Cantante("Johnny Orlando", "Canadiense", LocalDate.of(2015, 12, 15), Cantante.Estado.enActivo);
	    johnny.addAlbum(new Album("All The Things That Could Go Wrong", 10, LocalTime.of(0, 35, 0), LocalDate.of(2015, 12, 15)));
	    johnny.addAlbum(new Album("Never Be the Same", 12, LocalTime.of(0, 40, 0), LocalDate.of(2018, 5, 4)));
	    johnny.addAlbum(new Album("It's Never Really Over", 11, LocalTime.of(0, 38, 0), LocalDate.of(2020, 3, 27)));
	    listaCantantes.add(johnny);

	    Cantante alec = new Cantante("Alec Benjamin", "Estadounidense", LocalDate.of(2013, 11, 1), Cantante.Estado.enActivo);
	    alec.addAlbum(new Album("Narrated for You", 8, LocalTime.of(0, 30, 0), LocalDate.of(2013, 11, 1)));
	    alec.addAlbum(new Album("These Two Windows", 12, LocalTime.of(0, 42, 0), LocalDate.of(2020, 5, 29)));
	    alec.addAlbum(new Album("Four Windows", 10, LocalTime.of(0, 36, 0), LocalDate.of(2021, 6, 15)));
	    listaCantantes.add(alec);

	    Cantante troye = new Cantante("Troye Sivan", "Australiano", LocalDate.of(2007, 6, 1), Cantante.Estado.enActivo);
	    troye.addAlbum(new Album("Blue Neighbourhood", 12, LocalTime.of(0, 45, 0), LocalDate.of(2015, 12, 4)));
	    troye.addAlbum(new Album("Bloom", 12, LocalTime.of(0, 46, 0), LocalDate.of(2018, 8, 31)));
	    troye.addAlbum(new Album("In A Dream", 8, LocalTime.of(0, 28, 0), LocalDate.of(2020, 8, 21)));
	    listaCantantes.add(troye);

	    Cantante olivia = new Cantante("Olivia Rodrigo", "Estadounidense", LocalDate.of(2021, 1, 8), Cantante.Estado.enActivo);
	    olivia.addAlbum(new Album("SOUR", 11, LocalTime.of(0, 42, 0), LocalDate.of(2021, 5, 21)));
	    olivia.addAlbum(new Album("GUTS", 12, LocalTime.of(0, 44, 0), LocalDate.of(2023, 9, 8)));
	    olivia.addAlbum(new Album("Singles Collection", 10, LocalTime.of(0, 36, 0), LocalDate.of(2024, 1, 1)));
	    listaCantantes.add(olivia);

	    Cantante charli = new Cantante("Charli XCX", "Británica", LocalDate.of(2008, 5, 1), Cantante.Estado.enActivo);
	    charli.addAlbum(new Album("True Romance", 12, LocalTime.of(0, 40, 0), LocalDate.of(2013, 4, 14)));
	    charli.addAlbum(new Album("Sucker", 12, LocalTime.of(0, 42, 0), LocalDate.of(2014, 12, 15)));
	    charli.addAlbum(new Album("Charli", 15, LocalTime.of(0, 50, 0), LocalDate.of(2019, 9, 13)));
	    listaCantantes.add(charli);

	    Cantante dua = new Cantante("Dua Lipa", "Británica", LocalDate.of(2015, 8, 18), Cantante.Estado.enActivo);
	    dua.addAlbum(new Album("Dua Lipa", 11, LocalTime.of(0, 40, 0), LocalDate.of(2017, 6, 2)));
	    dua.addAlbum(new Album("Future Nostalgia", 11, LocalTime.of(0, 37, 0), LocalDate.of(2020, 3, 27)));
	    dua.addAlbum(new Album("Club Future Nostalgia", 13, LocalTime.of(0, 43, 0), LocalDate.of(2020, 8, 28)));
	    listaCantantes.add(dua);

	    Cantante harry = new Cantante("Harry Styles", "Británico", LocalDate.of(2010, 7, 23), Cantante.Estado.enActivo);
	    harry.addAlbum(new Album("Harry Styles", 10, LocalTime.of(0, 41, 0), LocalDate.of(2017, 5, 12)));
	    harry.addAlbum(new Album("Fine Line", 12, LocalTime.of(0, 46, 0), LocalDate.of(2019, 12, 13)));
	    harry.addAlbum(new Album("Harry's House", 13, LocalTime.of(0, 48, 0), LocalDate.of(2022, 5, 20)));
	    listaCantantes.add(harry);

	    Cantante alessandra = new Cantante("Alessandra Mele", "Noruega", LocalDate.of(2022, 11, 5), Cantante.Estado.enActivo);
	    alessandra.addAlbum(new Album("Forever", 10, LocalTime.of(0, 35, 0), LocalDate.of(2022, 11, 5)));
	    alessandra.addAlbum(new Album("Overdrive", 12, LocalTime.of(0, 40, 0), LocalDate.of(2023, 3, 15)));
	    alessandra.addAlbum(new Album("New Horizons", 11, LocalTime.of(0, 38, 0), LocalDate.of(2023, 11, 1)));
	    listaCantantes.add(alessandra);

	    Cantante ed = new Cantante("Ed Sheeran", "Británico", LocalDate.of(2011, 4, 26), Cantante.Estado.enActivo);
	    ed.addAlbum(new Album("+", 12, LocalTime.of(0, 50, 0), LocalDate.of(2011, 9, 9)));
	    ed.addAlbum(new Album("x", 12, LocalTime.of(0, 53, 0), LocalDate.of(2014, 6, 20)));
	    ed.addAlbum(new Album("÷", 16, LocalTime.of(1, 3, 0), LocalDate.of(2017, 3, 3)));
	    listaCantantes.add(ed);

	    Cantante kyle = new Cantante("Kyle Alessandro", "Noruego", LocalDate.of(2017, 1, 1), Cantante.Estado.enActivo);
	    kyle.addAlbum(new Album("Debut Album", 10, LocalTime.of(0, 35, 0), LocalDate.of(2017, 1, 1)));
	    kyle.addAlbum(new Album("Rising Star", 11, LocalTime.of(0, 38, 0), LocalDate.of(2018, 5, 10)));
	    kyle.addAlbum(new Album("Next Level", 12, LocalTime.of(0, 42, 0), LocalDate.of(2019, 9, 5)));
	    listaCantantes.add(kyle);

	    Cantante sabrina = new Cantante("Sabrina Carpenter", "Estadounidense", LocalDate.of(2014, 3, 14), Cantante.Estado.enActivo);
	    sabrina.addAlbum(new Album("Eyes Wide Open", 13, LocalTime.of(0, 42, 0), LocalDate.of(2015, 7, 1)));
	    sabrina.addAlbum(new Album("Evolution", 12, LocalTime.of(0, 40, 0), LocalDate.of(2016, 10, 14)));
	    sabrina.addAlbum(new Album("Singular: Act I", 11, LocalTime.of(0, 38, 0), LocalDate.of(2018, 11, 9)));
	    listaCantantes.add(sabrina);

	    Cantante michael = new Cantante("Michael Jackson", "Estadounidense", LocalDate.of(1964, 1, 1), Cantante.Estado.retirado);
	    michael.addAlbum(new Album("Got to Be There", 10, LocalTime.of(0, 35, 0), LocalDate.of(1972, 1, 24)));
	    michael.addAlbum(new Album("Ben", 10, LocalTime.of(0, 34, 0), LocalDate.of(1972, 8, 4)));
	    michael.addAlbum(new Album("Music & Me", 10, LocalTime.of(0, 33, 0), LocalDate.of(1973, 12, 10)));
	    listaCantantes.add(michael);

	    Cantante ross = new Cantante("Ross Lynch", "Estadounidense", LocalDate.of(2012, 4, 2), Cantante.Estado.enActivo);
	    ross.addAlbum(new Album("Austin & Ally Soundtrack", 12, LocalTime.of(0, 40, 0), LocalDate.of(2012, 3, 6)));
	    ross.addAlbum(new Album("Teen Beach Movie", 13, LocalTime.of(0, 42, 0), LocalDate.of(2013, 7, 19)));
	    ross.addAlbum(new Album("Teen Beach 2", 12, LocalTime.of(0, 39, 0), LocalDate.of(2015, 6, 26)));
	    listaCantantes.add(ross);

	    return listaCantantes;
	}
    
	/**
	 * Permite buscar un artista usando un long para buscar por clave primaria
	 * @param l - Donde ID como long
	 * @return Cantante
	 */
	public static Cantante buscarCantantePorID(long l) {
		
		Cantante p=null;
		try {
        	em.getTransaction().begin();
            p =em.find(Cantante.class,l);
            em.getTransaction().commit();

        } catch (Exception e) {
            e.printStackTrace();
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
        } 
		return p;
	}
	/**
	 * Método que obtiene un cantante en base al parametro pasado. Realiza una consulta SELECT con la condición WHERE donde se da el nombre.
	 * @param nombre - String la cual se empleará en la busqueda.
	 * @return Cantante - Devuelve un objeto {@link Cantante} de una lista de un único elemento 
	 */
	public static Cantante buscarCantantePorNombre(String nombre) {
		List<Cantante> lista = null;
		try {
        	em.getTransaction().begin();
        	lista =em.createQuery("SELECT c FROM Cantante c where c.nombre= :nombre", Cantante.class)
        			.setParameter("nombre", nombre)
                    .getResultList();
            em.getTransaction().commit();

        } catch (Exception e) {
            e.printStackTrace();
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
        } 
		return lista.getFirst();
	}
	/**
	 * Método para eliminar un cantante por su nombre
	 * @param nombre
	 */
	public static void eliminarCantante(String nombre) {
		Cantante c=buscarCantantePorNombre(nombre);
		if (c!=null) {
			em.getTransaction().begin();
			em.remove(c);
			System.out.println(nombre+" eliminado correctamente");
			em.getTransaction().commit();
		}else System.out.println("Cantante no encontrado");
		
	}
	/**
	 * 
	 * @param c
	 */
	public static void añadirCantante (Cantante c) {
		em.getTransaction().begin();
		em.persist(c);
		System.out.println("Cantante añadido correctamente");
		em.getTransaction().commit();
	}
	
	/**
	 * 
	 * @param c
	 * @param a
	 */
	public static void añadirAlbumaACantante(Cantante c, Album a) {
		c.addAlbum(a);
		em.getTransaction().begin();
		em.merge(c);
		//em.persist(c);
		em.getTransaction().commit();
	}
	
	/**
	 * 
	 * @param nombre
	 * @return
	 */
	public static Album buscarAlbumPorNombre(String nombre) {
		List<Album> lista = null;
		try {
        	em.getTransaction().begin();
        	lista =em.createQuery("SELECT a FROM Album a where a.nombre= :nombre", Album.class)
        			.setParameter("nombre", nombre)
                    .getResultList();
            em.getTransaction().commit();

        } catch (Exception e) {
            e.printStackTrace();
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
        } 
		return lista.getFirst();
	}
	
	/**
	 * 
	 * @param nombre
	 */
	public static void eliminarAlbum(String nombre) {
		Album a=buscarAlbumPorNombre(nombre);
		if (a!=null) {
			em.getTransaction().begin();
			em.remove(a);
			System.out.println(nombre+" eliminado correctamente");
			em.getTransaction().commit();
		}else System.out.println("Cantante no encontrado");
		
	}
	
	

}
